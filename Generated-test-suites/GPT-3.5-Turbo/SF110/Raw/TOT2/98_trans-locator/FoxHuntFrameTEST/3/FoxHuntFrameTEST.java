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
    public void testLoadLandmarks() {
        FoxHuntFrame foxHuntFrame = new FoxHuntFrame();
        foxHuntFrame.loadLandmarks();
        assertNotNull(foxHuntFrame.display.landmarks);
    }

    @Test
    public void testError() {
        FoxHuntFrame foxHuntFrame = new FoxHuntFrame();
        foxHuntFrame.error("Test Error Message");
    }

    // Test other public methods similarly

    // Additional edge cases and exception handling tests

    @Test
    public void testAddPointMultiplePoints() {
        FoxHuntFrame foxHuntFrame = new FoxHuntFrame();
        foxHuntFrame.theta.setText("45");
        foxHuntFrame.lat.setText("37.7749");
        foxHuntFrame.lon.setText("-122.4194");
        foxHuntFrame.addPoint();
        foxHuntFrame.theta.setText("90");
        foxHuntFrame.lat.setText("40.7128");
        foxHuntFrame.lon.setText("-74.0060");
        foxHuntFrame.addPoint();
        assertEquals(2, foxHuntFrame.points.size());
    }

    // Test other edge cases and exception handling scenarios

}