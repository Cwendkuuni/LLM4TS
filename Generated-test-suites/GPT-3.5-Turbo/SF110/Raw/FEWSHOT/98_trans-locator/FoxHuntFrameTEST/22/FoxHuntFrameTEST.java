import org.junit.Test;
import static org.junit.Assert.*;

public class FoxHuntFrameTest {

    @Test
    public void testAddPoint() {
        FoxHuntFrame frame = new FoxHuntFrame();
        
        // Test adding a point
        frame.theta.setText("45");
        frame.lat.setText("37.7749");
        frame.lon.setText("-122.4194");
        frame.addPoint();
        assertEquals(1, frame.points.size());
        assertEquals(1, frame.fox.size());
        
        // Test adding another point
        frame.theta.setText("90");
        frame.lat.setText("34.0522");
        frame.lon.setText("-118.2437");
        frame.addPoint();
        assertEquals(2, frame.points.size());
        assertEquals(2, frame.fox.size());
        
        // Test adding a point with invalid input
        frame.theta.setText("invalid");
        frame.lat.setText("invalid");
        frame.lon.setText("invalid");
        frame.addPoint();
        assertEquals(2, frame.points.size());
        assertEquals(2, frame.fox.size());

    }

    @Test
    public void testLoadLandmarks() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.loadLandmarks();
        assertEquals(1, frame.display.landmarks.size());
    }
}