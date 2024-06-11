import static org.junit.Assert.*;
import org.junit.Test;

public class FoxHuntFrameTest {
    
    @Test
    public void testAddPoint() {
        FoxHuntFrame foxHunt = new FoxHuntFrame();
        foxHunt.theta.setText("45");
        foxHunt.lat.setText("37.7749");
        foxHunt.lon.setText("-122.4194");
        
        foxHunt.addPoint();
        
        // Check if the point is added correctly
        assertEquals(1, foxHunt.points.size());
        
        // Additional test for adding second point
        foxHunt.theta.setText("90");
        foxHunt.lat.setText("40.7128");
        foxHunt.lon.setText("-74.0060");
        
        foxHunt.addPoint();
        
        // Check if the fox point is calculated and added correctly
        assertEquals(2, foxHunt.fox.size());
    }
    
    // Add more test cases for other public methods
    
    // Test cases for edge cases and exceptions
    
}