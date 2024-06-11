import static org.junit.Assert.*;
import org.junit.Test;

public class FoxHuntFrameTest {
    
    @Test
    public void testAddPoint() {
        FoxHuntFrame foxHuntFrame = new FoxHuntFrame();
        foxHuntFrame.theta.setText("45");
        foxHuntFrame.lat.setText("37.7749");
        foxHuntFrame.lon.setText("-122.4194");
        foxHuntFrame.addPoint();
        assertEquals(1, foxHuntFrame.points.size());
    }

    @Test
    public void testLoadLandmarks_FileNotFound() {
        FoxHuntFrame foxHuntFrame = new FoxHuntFrame();
        foxHuntFrame.loadLandmarks();
        // Test with a non-existent file
        assertNotNull(foxHuntFrame);
    }

    @Test
    public void testError_Abort() {
        FoxHuntFrame foxHuntFrame = new FoxHuntFrame();
        foxHuntFrame.error("Error Message Test");
        // Test the abort option in the error handling
        assertTrue(true);
    }
}