import org.junit.Test;
import static org.junit.Assert.*;

public class HuntDisplayTest {
    
    @Test
    public void testAddPoint() {
        // Test adding a TrigPoint to the points vector
        HuntDisplay huntDisplay = new HuntDisplay();
        TrigPoint point = new TrigPoint(30, 60, 45, false);
        
        huntDisplay.addPoint(point);
        
        assertEquals(1, huntDisplay.points.size());
    }
    
    @Test
    public void testAddLandMark() {
        // Test adding a Landmark to the landmarks vector
        HuntDisplay huntDisplay = new HuntDisplay();
        Landmark landmark = new Landmark(45, 90, new ImageIcon("image.jpg"));
        
        huntDisplay.addLandMark(landmark);
        
        assertEquals(1, huntDisplay.landmarks.size());
    }
    
    // Additional test cases for edge cases and exceptions can be added here
    
}