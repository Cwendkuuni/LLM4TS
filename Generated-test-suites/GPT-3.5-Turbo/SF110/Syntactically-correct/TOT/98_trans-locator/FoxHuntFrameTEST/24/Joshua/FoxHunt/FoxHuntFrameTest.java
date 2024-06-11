import org.junit.Test;
import static org.junit.Assert.*;

public class FoxHuntFrameTest {
    
    @Test
    public void testAddPoint_WithPositiveAngle_AddsPointToListAndFoxVectorSizeIncreases() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.theta.setText("45");
        frame.lat.setText("37.7749");
        frame.lon.setText("-122.4194");
        
        frame.addPoint();
        
        assertEquals(1, frame.points.size());
        assertEquals(1, frame.fox.size());
    }
    
    @Test
    public void testAddPoint_WhenPointsSizeGreaterThanOne_CalculatesAndAddsFoxPoint() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.points.add(new TrigPoint(37.7749, -122.4194, 45));
        frame.theta.setText("90");
        frame.lat.setText("40.7128");
        frame.lon.setText("-74.0060");
        
        frame.addPoint();
        
        assertEquals(2, frame.points.size());
        assertEquals(1, frame.fox.size());
    }
    
    @Test
    public void testAddPoint_WithInvalidAngle_ThrowsNumberFormatException() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.theta.setText("abc");
        frame.lat.setText("37.7749");
        frame.lon.setText("-122.4194");
        
        try {
            frame.addPoint();
            fail("Expected NumberFormatException to be thrown");
        } catch (NumberFormatException e) {
            // Test passes
        }
    }
    
    @Test
    public void testAddPoint_WithInvalidLatitude_ThrowsNumberFormatException() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.theta.setText("45");
        frame.lat.setText("abc");
        frame.lon.setText("-122.4194");
        
        try {
            frame.addPoint();
            fail("Expected NumberFormatException to be thrown");
        } catch (NumberFormatException e) {
            // Test passes
        }
    }
    
    @Test
    public void testAddPoint_WithInvalidLongitude_ThrowsNumberFormatException() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.theta.setText("45");
        frame.lat.setText("37.7749");
        frame.lon.setText("abc");
        
        try {
            frame.addPoint();
            fail("Expected NumberFormatException to be thrown");
        } catch (NumberFormatException e) {
            // Test passes
        }
    }
    
    // Add more test cases for other methods
    
}