import java.awt.*;
import java.awt.event.*;

public class Tank {
    private static final int XSPEED = 5;
    private static final int YSPEED = 5;

    private static final int WIDTH = 30;
    private static final int HEIGHT = 30;
    private boolean isTankLive = true;
    TankClient tc;
    private int x, y;
    private boolean isEnemy;
    private  boolean bL = false, bU =false, bR = false, bD= false;

    public void setTankLive(boolean isTankLive) {
        this.isTankLive = isTankLive;
    }

    public boolean getTankLive() {
        return this.isTankLive ;
    }


    enum Direction{ L, LU ,U , RU, R, RD, D, LD, STOP };
    private Direction dir = Direction.STOP;
    private Direction ptDir = Direction.D;

    public Tank(int x, int y, boolean isEnemy){
        this.x = x;
        this.y = y;
        this.isEnemy = isEnemy;
    }

    public Tank(int x, int y, boolean isEnemy, TankClient tc){
        this(x, y, isEnemy);
        this.tc = tc;
    }

    public void draw(Graphics g){
        if(isTankLive == false)return; //如果没活着，直接不画 返回

        Color c = g.getColor();
        if(isEnemy == false) {  //ME
            g.setColor(Color.RED);
        }else {
            g.setColor(Color.BLUE);
        }
        g.fillOval(x,y,WIDTH,HEIGHT);
        g.setColor(c);
        move();

        switch(ptDir){
            case L:
                g.drawLine(x + Tank.WIDTH/2, y+Tank.HEIGHT/2, x, y+Tank.HEIGHT/2);
                break;
            case LU:
                g.drawLine(x + Tank.WIDTH/2, y+Tank.HEIGHT/2, x, y);
                break;
            case U:
                g.drawLine(x + Tank.WIDTH/2, y+Tank.HEIGHT/2, x + Tank.WIDTH/2, y );
                break;
            case RU:
                g.drawLine(x + Tank.WIDTH/2, y+Tank.HEIGHT/2, x + Tank.WIDTH, y);
                break;
            case R:
                g.drawLine(x + Tank.WIDTH/2, y+Tank.HEIGHT/2, x + Tank.WIDTH, y + Tank.HEIGHT/2);
                break;
            case RD:
                g.drawLine(x + Tank.WIDTH/2, y+Tank.HEIGHT/2, x + Tank.WIDTH, y + Tank.HEIGHT);
                break;
            case D:
                g.drawLine(x + Tank.WIDTH/2, y+Tank.HEIGHT/2, x + Tank.WIDTH/2, y + Tank.HEIGHT);
                break;
            case LD:
                g.drawLine(x + Tank.WIDTH/2, y+Tank.HEIGHT/2, x, y+Tank.HEIGHT);
                break;
        }
    }

    void move(){
        //System.out.println(dir);
        switch(dir){
            case L:
                x -= XSPEED;
                break;
            case LU:
                x -= XSPEED;
                y -= YSPEED;
                break;
            case U:
                y -= YSPEED;
                break;
            case RU:
                x += XSPEED;
                y -= YSPEED;
                break;
            case R:
                x += XSPEED;
                break;
            case RD:
                x += XSPEED;
                y += YSPEED;
                break;
            case D:
                y += YSPEED;
                break;
            case LD:
                x -= XSPEED;
                y += YSPEED;
                break;
            case STOP:
                break;
        }
        if(this.dir != Direction.STOP){
            this.ptDir =this.dir;
        }

        if(x < 0) x=0;
        if(y < 30) y=30;
        if( x + Tank.WIDTH > TankClient.GAME_WIDTH ){
            x = TankClient.GAME_WIDTH - TankClient.WIDTH;
        }
        if( y + Tank.HEIGHT > TankClient.GAME_HEIGHT ){
            y = TankClient.GAME_HEIGHT - TankClient.HEIGHT ;
        }

    }

    public void keyPressed(KeyEvent e){
        int key = e.getKeyCode();
        // System.out.println("ok");
        switch (key){
            case KeyEvent.VK_LEFT:
                bL = true;
                break;
            case KeyEvent.VK_UP:
                bU = true;
                break;
            case KeyEvent.VK_RIGHT:
                bR = true;
                break;
            case KeyEvent.VK_DOWN:
                bD = true;
                break;
        }
        locateDirection();
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        // System.out.println("ok");
        switch (key){
            case KeyEvent.VK_CONTROL:
                fire();
                break;
            case KeyEvent.VK_LEFT:
                bL = false;
                break;
            case KeyEvent.VK_UP:
                bU = false;
                break;
            case KeyEvent.VK_RIGHT:
                bR = false;
                break;
            case KeyEvent.VK_DOWN:
                bD = false;
                break;
        }
        locateDirection();
    }

    void locateDirection() {
        //System.out.println("locateDirection");
        if (bL && !bU && !bR && !bD){
            dir = Direction.L;
        }else if (bL && bU && !bR && !bD){
            dir = Direction.LU;
        }else if(!bL && bU  && !bR && !bD){
            dir = Direction.U;
        }else if(!bL && bU  &&  bR && !bD){
            dir = Direction.RU;
        }else if(!bL && !bU && bR  && !bD){////////////////////////////////
            dir = Direction.R;
            System.out.println("R");
        }else if(!bL && !bU && bR  &&  bD){
            dir = Direction.RD;
        }else if(!bL && !bU && !bR &&  bD){
            dir = Direction.D;
            System.out.println("D");
        }else if(bL  && !bU && !bR &&  bD){
            dir = Direction.LD;
        }else if(!bL  && !bU && !bR &&  !bD){
            dir = Direction.STOP;
        }
    }

    public Missile fire(){
        int x = this.x + Tank.WIDTH/2 - Missile.HEIGHT/2;
        int y = this.y + Tank.WIDTH/2 - Missile.HEIGHT/2;
        Missile m = new Missile(x , y, ptDir, this.tc);
        tc.missileList.add(m);
        return m;
    }

    public Rectangle getRect(){//碰撞检测类
        return new Rectangle( x, y, WIDTH, HEIGHT );
    }

}