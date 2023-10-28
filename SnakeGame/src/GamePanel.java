import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;


import javax.swing.JPanel;


public class GamePanel extends JPanel implements ActionListener{

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25; // size of apples, changes size of grid and snake body parts
    static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE; // number of objects that can fit on screen
    static final int DELAY = 100; // higher the number = slower the game

    final int x[] = new int[GAME_UNITS]; // holds x co-ordinates of body parts
    final int y[] = new int[GAME_UNITS]; // // holds y co-ordinates of body parts
    int bodyParts = 6; // initial number of body parts
    int applesEaten;
    int applesX; // x co-ordinate of apple
    int applesY; // y co-ordinate of apple
    char direction = 'R'; // beginning direction
    boolean running = false;
    Timer timer;
    Random random;

    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();

    }

    public void startGame(){
        newApple();
        running = true;
        timer = new Timer(DELAY,this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){

        if(running){

            /*for(int i=0;i<SCREEN_HEIGHT/UNIT_SIZE;i++){// draws lines to create grid, each item takes up one grid space
                g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);// draws vertical lines
                g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);// draws horizontal lines
            }
             */
            g.setColor(Color.red);
            g.fillOval(applesX, applesY, UNIT_SIZE, UNIT_SIZE); // draws apple as circle

            for(int i=0;i<bodyParts;i++){
                if(i==0){
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
                else{
                    g.setColor(new Color(45,180,0));
                    //g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));// random color
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }

            g.setColor(Color.red);
            g.setFont(new Font("Ink Free", Font.BOLD, 75));
            FontMetrics metrics1 = getFontMetrics(g.getFont());// gets font metrics (used to line up font)
            g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize()); // lines up font in center of screen

        }
        else{
            gameOver(g);
        }
    }

    public void newApple(){
        applesX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;// cast to int bc random.nextInt() returns int
        applesY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
    }

    public void move(){
        for(int i=bodyParts;i>0;i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch(direction){
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }

    public void checkApple(){
        if((x[0] == applesX) && (y[0] == applesY)){ // if head of snake is on apple
            bodyParts++; // add body part
            applesEaten++; // add to score
            newApple(); // create new apple
        }
    }

    public void checkCollisions(){
        for(int i=bodyParts;i>0;i--){ // checks if head collides with body
            if((x[0] == x[i]) && (y[0] == y[i])){ // x[0] and y[0] are head of snake
                running = false; // if head collides with body then game is over
            }
        }
        if(x[0] < 0){ // checks if head touches left border
            running = false;
        }
        if(x[0] > SCREEN_WIDTH){ // checks if head touches right border
            running = false;
        }
        if(y[0] < 0){ // checks if head touches top border
            running = false;
        }
        if(y[0] > SCREEN_HEIGHT){ // checks if head touches bottom border
            running = false;
        }
        if(!running){ // if game is over then stop timer
            timer.stop();
        }
    }

    public void gameOver(Graphics g){
        //Score
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());// gets font metrics (used to line up font)
        g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics2.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize()); // lines up font in center of screen


        //game over text
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics1 = getFontMetrics(g.getFont());// gets font metrics (used to line up font)
        g.drawString("Game Over", (SCREEN_WIDTH - metrics1.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2); // lines up font in center of screen
    }


    @Override
    public void actionPerformed(ActionEvent e) {

        if(running){ // if game is running then move and check for collisions
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            switch(e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if(direction != 'R'){
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L'){
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D'){
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U'){
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}
