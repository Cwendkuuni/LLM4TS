package Joshua.FoxHunt;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.swing.ImageIcon;


class TrigPoint {
    private double x;
    private double y;
    private double theta;
    private boolean fox;
    
    public TrigPoint(double x, double y, double theta, boolean fox) {
        this.x = x;
        this.y = y;
        this.theta = theta;
        this.fox = fox;
    }
    
    public double getX() {
        return x;
    }
    
    public double getY() {
        return y;
    }
    
    public double getTheta() {
        return theta;
    }
    
    public boolean isFox() {
        return fox;
    }
}

class Landmark {
    private double lat;
    private double lon;
    private ImageIcon icon;
    
    public Landmark(double lat, double lon, ImageIcon icon) {
        this.lat = lat;
        this.lon = lon;
        this.icon = icon;
    }
    
    public double getLat() {
        return lat;
    }
    
    public double getLon() {
        return lon;
    }
    
    public ImageIcon getIcon() {
        return icon;
    }
}

public class HuntDisplayTest {
    
    HuntDisplay huntDisplay;
    
    @Before
    public void setUp() {
        huntDisplay = new HuntDisplay();
    }

    @Test
    public void testAddPoint() {
        TrigPoint point1 = new TrigPoint(0.5, 0.5, 45.0, false);
        huntDisplay.addPoint(point1);
        Vector<TrigPoint> points = huntDisplay.points;
        assertEquals(1, points.size());
        assertEquals(point1, points.elementAt(0));
        
        TrigPoint point2 = new TrigPoint(0.3, 0.7, 60.0, true);
        huntDisplay.addPoint(point2);
        assertEquals(2, points.size());
        assertEquals(point2, points.elementAt(1));
    }
    
    @Test
    public void testAddLandmark() {
        ImageIcon icon1 = new ImageIcon(new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB));
        Landmark landmark1 = new Landmark(0.2, 0.4, icon1);
        huntDisplay.addLandMark(landmark1);
        Vector<Landmark> landmarks = huntDisplay.landmarks;
        assertEquals(1, landmarks.size());
        assertEquals(landmark1, landmarks.elementAt(0));
        
        ImageIcon icon2 = new ImageIcon(new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB));
        Landmark landmark2 = new Landmark(0.6, 0.8, icon2);
        huntDisplay.addLandMark(landmark2);
        assertEquals(2, landmarks.size());
        assertEquals(landmark2, landmarks.elementAt(1));
    }
    
    @Test
    public void testPaintComponent() {
        HuntDisplay huntDisplay = new HuntDisplay() {
            @Override
            public void paintComponent(final Graphics g) {
                Vector<TrigPoint> points = new Vector<>();
                points.add(new TrigPoint(0.5, 0.5, 45.0, false));
                points.add(new TrigPoint(0.3, 0.7, 60.0, true));
                this.points = points;
    
                Vector<Landmark> landmarks = new Vector<>();
                ImageIcon icon = new ImageIcon(new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB));
                landmarks.add(new Landmark(0.2, 0.4, icon));
                landmarks.add(new Landmark(0.6, 0.8, icon));
                this.landmarks = landmarks;
    
                final Color point = Color.BLACK;
                final Color fox = Color.GREEN;
                final TrigPoint[] trigpoints = new TrigPoint[this.points.size()];
                for (int a = 0; a < this.points.size(); ++a) {
                    trigpoints[a] = this.points.elementAt(a);
                }
    
                final Landmark[] landMarks = new Landmark[this.landmarks.size()];
                if (this.landmarks.size() != 0) {
                    for (int z = 0; z < this.points.size(); ++z) {
                        landMarks[z] = this.landmarks.elementAt(z);
                    }
                }
    
                double trigPointAvgX = 0.0;
                for (int b = 0; b < trigpoints.length; ++b) {
                    trigPointAvgX += trigpoints[b].getX();
                }
                trigPointAvgX /= trigpoints.length;
    
                double trigPointAvgY = 0.0;
                for (int b2 = 0; b2 < trigpoints.length; ++b2) {
                    trigPointAvgY += trigpoints[b2].getY();
                }
                trigPointAvgY /= trigpoints.length;
    
                final double xAdd = 0.5 - trigPointAvgX;
                final double yAdd = 0.5 - trigPointAvgY;
    
                for (int t = 0; t < trigpoints.length; ++t) {
                    if (trigpoints[t].isFox()) {
                        g.setColor(fox);
                    } else {
                        g.setColor(point);
                    }
                    final int X = (int) ((trigpoints[t].getX() + xAdd) * this.getWidth());
                    final int Y = (int) ((trigpoints[t].getY() + yAdd) * this.getHeight());
                    g.fillOval(X, Y, 5, 5);
                }
    
                g.setColor(this.getForeground());
    
                if (this.landmarks.size() != 0) {
                    for (int l = 0; l < landMarks.length; ++l) {
                        final int X = (int) ((landMarks[l].getLon() + xAdd) * this.getWidth());
                        final int Y = (int) ((landMarks[l].getLat() + yAdd) * this.getHeight());
                        final Image image = landMarks[l].getIcon().getImage();
                        g.drawImage(image, X, Y, 10, 10, this.getBackground(), this);
                    }
                }
            }
        };
    
        BufferedImage image = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.createGraphics();
        huntDisplay.paintComponent(g);
    }
}