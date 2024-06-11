import static org.junit.Assert.*;
import org.junit.Test;

public class FoxHuntFrameTest {
    // Test case for addPoint method
    @Test
    public void testAddPoint() {
        FoxHuntFrame foxHuntFrame = new FoxHuntFrame();
        foxHuntFrame.theta.setText("45");
        foxHuntFrame.lat.setText("37.7749");
        foxHuntFrame.lon.setText("-122.4194");
        
        foxHuntFrame.addPoint();
        
        assertEquals(1, foxHuntFrame.points.size());
        assertEquals(1, foxHuntFrame.fox.size());
    }
    
    // Test case for loadLandmarks method
    @Test
    public void testLoadLandmarks() {
        FoxHuntFrame foxHuntFrame = new FoxHuntFrame();
        foxHuntFrame.loadLandmarks();
        
        // Check if landmarks are loaded successfully
        assertNotNull(foxHuntFrame.display.landmarks);
    }
    
    // Test case for error method
    @Test
    public void testError() {
        FoxHuntFrame foxHuntFrame = new FoxHuntFrame();
        foxHuntFrame.error("Test Error Message");
        
        //TODO: Find a way to trigger the JOptionPane dialog and assert the system exit
    }
}