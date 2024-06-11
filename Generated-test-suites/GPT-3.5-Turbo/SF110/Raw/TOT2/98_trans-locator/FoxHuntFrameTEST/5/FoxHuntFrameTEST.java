import static org.junit.Assert.*;
import org.junit.Test;

public class FoxHuntFrameTest {

    @Test
    public void testAddPoint() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.theta.setText("45");
        frame.lat.setText("37.7749295");
        frame.lon.setText("-122.4194155");
        frame.addPoint();
        assertEquals(1, frame.points.size());
        assertEquals(1, frame.fox.size());
    }

    @Test
    public void testLoadLandmarks() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.loadLandmarks();
        // Assuming landmarks.cfg file has correct format, ensure landmarks are loaded successfully
        assertEquals(3, frame.display.getNumLandmarks());
    }

    @Test
    public void testError() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.error("Test Error");
        // Assuming user chooses to continue, no system exit should occur
        //Assertion for no system exit
    }

    // Add more test cases for each public method in FoxHuntFrame class to cover typical, edge, and error scenarios

}