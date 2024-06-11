import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JComponent;
import java.util.Vector;

public class HuntDisplayTest {

    @Test
    public void testAddPoint() {
        HuntDisplay display = new HuntDisplay();
        TrigPoint point = new TrigPoint(45, 90, 0, true);
        display.addPoint(point);
        Vector<TrigPoint> points = display.points;
        assertEquals(1, points.size());
        assertEquals(point.getY() / 90.0, points.get(0).getY(), 0.001);
        assertEquals(point.getX() / 180.0, points.get(0).getX(), 0.001);
        assertEquals(point.getTheta(), points.get(0).getTheta());
        assertEquals(point.isFox(), points.get(0).isFox());
    }
    
    @Test
    public void testAddLandMark() {
        HuntDisplay display = new HuntDisplay();
        Landmark landmark = new Landmark(45, 90, new ImageIcon());
        display.addLandMark(landmark);
        Vector<Landmark> landmarks = display.landmarks;
        assertEquals(1, landmarks.size());
        assertEquals(landmark.getLat() / 90.0, landmarks.get(0).getLat(), 0.001);
        assertEquals(landmark.getLon() / 180.0, landmarks.get(0).getLon(), 0.001);
        assertEquals(landmark.getIcon(), landmarks.get(0).getIcon());
    }
    
    @Test
    public void testPaintComponent() {
        HuntDisplay display = new HuntDisplay();
        Graphics g = null; // Mocking Graphics object for testing
        
        // Test with no points or landmarks
        display.paintComponent(g);
        
        // Test with points and landmarks
        TrigPoint point1 = new TrigPoint(45, 90, 0, true);
        TrigPoint point2 = new TrigPoint(30, 60, 45, false);
        display.addPoint(point1);
        display.addPoint(point2);
        Landmark landmark1 = new Landmark(45, 90, new ImageIcon());
        Landmark landmark2 = new Landmark(30, 60, new ImageIcon());
        display.addLandMark(landmark1);
        display.addLandMark(landmark2);
        display.paintComponent(g);
    }

}