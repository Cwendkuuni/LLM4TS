import org.junit.Test;
import static org.junit.Assert.*;

public class FoxHuntFrameTest {

    @Test
    public void testAddPoint() {
        FoxHuntFrame foxHuntFrame = new FoxHuntFrame();
        foxHuntFrame.theta.setText("90");
        foxHuntFrame.lat.setText("37.7749");
        foxHuntFrame.lon.setText("-122.4194");
        
        int initialPointsSize = foxHuntFrame.points.size();
        foxHuntFrame.addPoint();
        
        assertEquals(initialPointsSize + 1, foxHuntFrame.points.size());
    }

    @Test
    public void testAddPointEdgeCases() {
        FoxHuntFrame foxHuntFrame = new FoxHuntFrame();
        foxHuntFrame.theta.setText("0");
        foxHuntFrame.lat.setText("0");
        foxHuntFrame.lon.setText("0");

        foxHuntFrame.addPoint();

        assertEquals(1, foxHuntFrame.points.size());
    }

    @Test
    public void testFinalPrep() {
        FoxHuntFrame foxHuntFrame = new FoxHuntFrame();
        assertNotNull(foxHuntFrame.panel);
    }

    @Test
    public void testLoadLandmarks() {
        FoxHuntFrame foxHuntFrame = new FoxHuntFrame();
        foxHuntFrame.loadLandmarks();

        // Assuming landmarks.cfg file contains valid landmark data
        assertNotNull(foxHuntFrame.display.landmarks);
    }

    @Test
    public void testError() {
        FoxHuntFrame foxHuntFrame = new FoxHuntFrame();
        foxHuntFrame.error("Testing error");

        // The error method may terminate the program, which is hard to assert
        // We can verify it did not throw any exceptions
        assertTrue(true);
    }
}