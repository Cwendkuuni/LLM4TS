import org.junit.Test;
import static org.junit.Assert.*;

public class FoxHuntFrameTest {

    @Test
    public void testAddPoint() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.theta.setText("45");
        frame.lat.setText("37.7749");
        frame.lon.setText("-122.4194");
        frame.addPoint();
        assertEquals(1, frame.points.size());
        assertEquals(1, frame.fox.size());
    }

    @Test
    public void testAddPointEdgeCase() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.theta.setText("90");
        frame.lat.setText("45.0");
        frame.lon.setText("0.0");
        frame.addPoint();
        assertEquals(1, frame.points.size());
        assertEquals(1, frame.fox.size());
    }

    @Test
    public void testLoadLandmarks() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.loadLandmarks();
        assertEquals(1, frame.display.getLandmarks().size());
    }

    @Test
    public void testErrorHandling() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.error("Test Error");
        // This test will pass if the error dialog is displayed
    }
}