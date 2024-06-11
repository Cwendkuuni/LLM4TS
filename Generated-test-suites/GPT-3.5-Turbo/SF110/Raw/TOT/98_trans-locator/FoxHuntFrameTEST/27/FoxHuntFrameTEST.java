import org.junit.*;
import static org.junit.Assert.*;

public class FoxHuntFrameTest {

    @Test
    public void testAddPoint() {
        FoxHuntFrame frame = new FoxHuntFrame();
        
        // Test typical use case
        frame.theta.setText("45");
        frame.lat.setText("37.7749");
        frame.lon.setText("-122.4194");
        frame.addPoint();
        assertEquals(1, frame.points.size());
        assertEquals(1, frame.fox.size());
        
        // Test edge case
        frame.theta.setText("0");
        frame.lat.setText("0");
        frame.lon.setText("0");
        frame.addPoint();
        assertEquals(2, frame.points.size());
        assertEquals(2, frame.fox.size());
        
        // Test error scenario
        frame.theta.setText("1000"); // Invalid angle
        frame.lat.setText("37.7749");
        frame.lon.setText("-122.4194");
        frame.addPoint();
        assertEquals(2, frame.points.size()); // No new point should be added
        assertEquals(2, frame.fox.size()); // No new fox should be added
    }

    @Test
    public void testLoadLandmarks() {
        FoxHuntFrame frame = new FoxHuntFrame();
        
        // Test typical use case
        frame.loadLandmarks();
        assertEquals(5, frame.display.getLandmarks.size());
        
        // Test edge case
        // If the "landmarks.cfg" file is empty or missing, no landmarks should be added
        frame.loadLandmarks();
        assertEquals(5, frame.display.getLandmarks.size()); // No additional landmarks should be added
        
        // Test error scenario
        // If the "landmarks.cfg" file is not found or there is an error communicating with the file,
        // the error() method should be called and the program should exit
    }

    @Test
    public void testError() {
        FoxHuntFrame frame = new FoxHuntFrame();
        
        // Test typical use case
        frame.error("Can't Find the file");
        // The error dialog should be displayed with options to "Abort" or "Continue"
        // If "Abort" is chosen, the program should exit
        // If "Continue" is chosen, the program should continue running
    }

}