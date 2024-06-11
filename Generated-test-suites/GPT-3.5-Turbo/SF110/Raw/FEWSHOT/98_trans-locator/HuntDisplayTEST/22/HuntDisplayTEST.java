import static org.junit.Assert.*;

import org.junit.Test;

import Joshua.FoxHunt.HuntDisplay;
import Joshua.FoxHunt.Landmark;
import Joshua.FoxHunt.TrigPoint;

public class HuntDisplayTest {

    @Test
    public void testAddPoint() {
        HuntDisplay display = new HuntDisplay();
        TrigPoint point = new TrigPoint(45.0, 90.0, 0.0, true);
        display.addPoint(point);
        
        assertEquals(1, display.points.size());
        
        TrigPoint addedPoint = (TrigPoint) display.points.get(0);
        assertEquals(point.getY() / 90.0, addedPoint.getY(), 0.001);
        assertEquals(point.getX() / 180.0, addedPoint.getX(), 0.001);
        assertEquals(point.getTheta(), addedPoint.getTheta(), 0.001);
        assertEquals(point.isFox(), addedPoint.isFox());
    }
    
    @Test
    public void testAddLandMark() {
        HuntDisplay display = new HuntDisplay();
        Landmark landmark = new Landmark(0.5, 0.5, new ImageIcon());
        display.addLandMark(landmark);
        
        assertEquals(1, display.landmarks.size());
        
        Landmark addedLandmark = (Landmark) display.landmarks.get(0);
        assertEquals(landmark.getLat() / 90.0, addedLandmark.getLat(), 0.001);
        assertEquals(landmark.getLon() / 180.0, addedLandmark.getLon(), 0.001);
        assertEquals(landmark.getIcon(), addedLandmark.getIcon());
    }
    
    @Test
    public void testPaintComponent() {
        // Test the paintComponent method behavior with different scenarios
        // It is challenging to write comprehensive tests for a graphical component like HuntDisplay
        // We can check if the method runs without any errors
        
        HuntDisplay display = new HuntDisplay();
        Graphics g = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB).getGraphics();
        display.paintComponent(g);
        
        display.addPoint(new TrigPoint(0.25, 0.5, 0.0, false));
        display.addPoint(new TrigPoint(0.75, 0.5, 0.0, true));
        display.addLandMark(new Landmark(0.5, 0.5, new ImageIcon()));
        g = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB).getGraphics();
        display.paintComponent(g);
        
        // Add more test scenarios as required
    }
}