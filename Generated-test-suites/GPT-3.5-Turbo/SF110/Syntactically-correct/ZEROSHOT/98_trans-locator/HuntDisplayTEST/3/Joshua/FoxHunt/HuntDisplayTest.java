import org.junit.*;
import static org.junit.Assert.*;

public class HuntDisplayTest {

    @Test
    public void testAddPoint() {
        HuntDisplay display = new HuntDisplay();
        TrigPoint point = new TrigPoint(1.0, 1.0, 45, true);
        display.addPoint(point);
        
        // Test if point is added to points vector
        assertEquals(1, display.points.size());
        
        // Test if point is added with correct values
        TrigPoint addedPoint = display.points.elementAt(0);
        assertEquals(1.0, addedPoint.getY(), 0.01);
        assertEquals(1.0, addedPoint.getX(), 0.01);
        assertEquals(45, addedPoint.getTheta());
        assertTrue(addedPoint.isFox());
    }
    
    @Test
    public void testAddLandMark() {
        HuntDisplay display = new HuntDisplay();
        Landmark landmark = new Landmark(1.0, 1.0, new ImageIcon());
        display.addLandMark(landmark);
        
        // Test if landmark is added to landmarks vector
        assertEquals(1, display.landmarks.size());
        
        // Test if landmark is added with correct values
        Landmark addedLandmark = display.landmarks.elementAt(0);
        assertEquals(1.0, addedLandmark.getLat(), 0.01);
        assertEquals(1.0, addedLandmark.getLon(), 0.01);
        assertNotNull(addedLandmark.getIcon());
    }
    
    @Test
    public void testPaintComponent() {
        HuntDisplay display = new HuntDisplay();
        TrigPoint point1 = new TrigPoint(0.5, 0.5, 0, false);
        TrigPoint point2 = new TrigPoint(0.6, 0.4, 135, true);
        display.addPoint(point1);
        display.addPoint(point2);
        
        Landmark landmark = new Landmark(0.3, 0.7, new ImageIcon());
        display.addLandMark(landmark);
        
        // Test if paintComponent method paints points and landmarks correctly
        // (cannot be fully tested as it involves Graphics rendering)
    }
}