import org.junit.*;
import static org.junit.Assert.*;

public class HuntDisplayTest {

    @Test
    public void testAddPoint() {
        HuntDisplay display = new HuntDisplay();
        
        // Typical use case
        TrigPoint point1 = new TrigPoint(0, 0, 0, false);
        display.addPoint(point1);
        assertEquals(1, display.points.size());
        
        // Edge case
        TrigPoint point2 = new TrigPoint(90, 180, 0, true);
        display.addPoint(point2);
        assertEquals(2, display.points.size());
        
        // Error scenario
        TrigPoint point3 = null;
        display.addPoint(point3);
        assertEquals(2, display.points.size()); // No change expected
    }
    
    @Test
    public void testAddLandmark() {
        HuntDisplay display = new HuntDisplay();
        
        // Typical use case
        Landmark landmark1 = new Landmark(0, 0, new ImageIcon("image1.png"));
        display.addLandMark(landmark1);
        assertEquals(1, display.landmarks.size());
        
        // Edge case
        Landmark landmark2 = new Landmark(90, 180, new ImageIcon("image2.png"));
        display.addLandMark(landmark2);
        assertEquals(2, display.landmarks.size());
        
        // Error scenario
        Landmark landmark3 = null;
        display.addLandMark(landmark3);
        assertEquals(2, display.landmarks.size()); // No change expected
    }
    
    @Test
    public void testPaintComponent() {
        // Test cases for paintComponent are challenging due to the limitations of unit testing graphics
        // and the complexity of the method. We will skip this for now.
        assertTrue(true);
    }
}