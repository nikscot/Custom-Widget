package edu.jsu.mcis;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class CustomWidget extends JPanel implements MouseListener {
    private java.util.List<ShapeObserver> observers;
    private final Color HEXAGON_COLOR = Color.green;
    private final Color OCTAGON_COLOR = Color.red;
    private final Color DEFAULT_COLOR = Color.white;
    
    
    private boolean hexagon;
	private boolean octagon;
    private Point[] hexagonVertices;
    private Point[] octagonVertices;

    
    public CustomWidget() {
        observers = new ArrayList<>();
        
        hexagonVertices = new Point[6];
        for(int i = 0; i < hexagonVertices.length; i++) { 
            hexagonVertices[i] = new Point(); 
        }
        
        octagonVertices = new Point[8];
        for(int i = 0; i < octagonVertices.length; i++) { 
            octagonVertices[i] = new Point(); 
        }
		
        Dimension dim = getPreferredSize();
        calculateVertices(dim.width, dim.height);
        setBorder(BorderFactory.createLineBorder(Color.black));
        addMouseListener(this);
    }

    
    public void addShapeObserver(ShapeObserver observer) {
        if(!observers.contains(observer)) observers.add(observer);
    }
    public void removeShapeObserver(ShapeObserver observer) {
        observers.remove(observer);
    }
    private void notifyObservers() {
        ShapeEvent event = new ShapeEvent(hexagon, octagon);
        for(ShapeObserver obs : observers) {
            obs.shapeChanged(event);
        }
    }
    
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(200, 200);
    }

    private void calculateVertices(int width, int height) {
        // Square size should be half of the smallest dimension (width or height).
        int side = Math.min(width, height) / 2;
        for(int i = 0; i < hexagonVertices.length; i++) {
            double r = 0 + (i * (Math.PI / (hexagonVertices.length / 2)));
            double x = Math.cos(r);
            double y = Math.sin(r);
            hexagonVertices[i].setLocation(width/3 + (x * (side/4)), height/2 + (y * (side/4)));
        }
        
        for(int i = 0; i < octagonVertices.length; i++) {
            double r = Math.PI * 0.125 + (i * (Math.PI / (octagonVertices.length / 2)));
            double x = Math.cos(r);
            double y = Math.sin(r);
            octagonVertices[i].setLocation(width - (width/3) + (x * (side/4)), height/2 + (y * (side/4)));
        }
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
		Graphics2D graph2d = (Graphics2D)g;
        calculateVertices(getWidth(), getHeight());
        Shape[] shape = getShapes();
        graph2d.setColor(Color.black);
        graph2d.draw(shape[0]);
        graph2d.draw(shape[1]);

        if(hexagon) {
            graph2d.setColor(HEXAGON_COLOR);
            graph2d.fill(shape[0]);
            graph2d.setColor(DEFAULT_COLOR);
            graph2d.fill(shape[1]);
        }
        if(octagon){
            graph2d.setColor(OCTAGON_COLOR);
            graph2d.fill(shape[1]);
            graph2d.setColor(DEFAULT_COLOR);
            graph2d.fill(shape[0]);
        }
        if(!hexagon && !octagon) {
            graph2d.setColor(DEFAULT_COLOR);
            graph2d.fill(shape[0]);
            graph2d.setColor(DEFAULT_COLOR);
            graph2d.fill(shape[1]);       
        
    }

    public void mouseClicked(MouseEvent event) {
        if(shape[0].contains(event.getX(), event.getY())) {
            hexagon = true;
            notifyObservers();
        }
        if(shape[1].contains(event.getX(), event.getY())) {
            octagon = false;
            notifyObservers();
        }
        repaint(shape[0].getBounds());
        repaint(shape[1].getBounds());
    }
	
	public void mousePressed(MouseEvent event) {}
	public void mouseReleased(MouseEvent event) {}
	public void mouseEntered(MouseEvent event) {}
	public void mouseExited(MouseEvent event) {}
    
    public Shape[] getShapes() {
        Shape[] shape = new Shape[2];
        int[] x = new int[hexagonVertices.length];
        int[] y = new int[hexagonVertices.length];
        for(int i = 0; i < hexagonVertices.length; i++) {
            x[i] = hexagonVertices[i].x;
            y[i] = hexagonVertices[i].y;
        }
        shape[0] = new Polygon(x, y, hexagonVertices.length);
        
        x = new int[octagonVertices.length];
        y = new int[octagonVertices.length];
        for(int i = 0; i < octagonVertices.length; i++) {
            x[i] = octagonVertices[i].x;
            y[i] = octagonVertices[i].y;
        }
        
        shape[1] = new Polygon(x, y, octagonVertices.length);
        return shape;
    }
	
	public boolean isHexagon() { return hexagon; }
	public boolean isOctagon() { return octagon; }



	public static void main(String[] args) {
		JFrame window = new JFrame("Custom Widget");
        window.add(new CustomWidget());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(300, 300);
        window.setVisible(true);
	}
}
