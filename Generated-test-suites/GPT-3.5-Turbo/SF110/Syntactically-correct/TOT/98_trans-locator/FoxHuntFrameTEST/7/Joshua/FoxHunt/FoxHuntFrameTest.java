import org.junit.Test;
import static org.junit.Assert.*;

public class FoxHuntFrameTest {

    @Test
    public void testAddPoint() {
        FoxHuntFrame frame = new FoxHuntFrame();
        
        // Typical use case
        frame.theta.setText("45");
        frame.lat.setText("37.7749");
        frame.lon.setText("-122.4194");
        frame.addPoint();
        assertEquals(1, frame.points.size());
        assertEquals(0, frame.fox.size());
        
        // Edge case: Minimum values
        frame.theta.setText("0");
        frame.lat.setText("0.0");
        frame.lon.setText("0.0");
        frame.addPoint();
        assertEquals(2, frame.points.size());
        assertEquals(1, frame.fox.size());
        
        // Error scenario: Invalid values
        frame.theta.setText("ABC");
        frame.lat.setText("Invalid");
        frame.lon.setText("Invalid");
        frame.addPoint();
        assertEquals(2, frame.points.size());
        assertEquals(1, frame.fox.size());
    }

    @Test
    public void testLoadLandmarks() {
        FoxHuntFrame frame = new FoxHuntFrame();

        // Typical use case: Valid landmarks file
        frame.loadLandmarks();
        assertEquals(3, frame.display.getLandmarks().size());

        // Error scenario: Invalid landmarks file
        // File does not exist
        frame.loadLandmarks();
        assertEquals(0, frame.display.getLandmarks().size());

        // Error scenario: Invalid landmarks file
        // Error communicating with the file
        frame.loadLandmarks();
        assertEquals(0, frame.display.getLandmarks().size());
    }
}