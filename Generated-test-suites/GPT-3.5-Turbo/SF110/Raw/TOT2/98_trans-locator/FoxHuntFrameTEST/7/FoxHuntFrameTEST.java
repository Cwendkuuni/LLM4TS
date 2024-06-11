import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.event.ActionEvent;

public class FoxHuntFrameTest {
    
    @Test
    public void testAddPoint() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.theta.setText("45");
        frame.lat.setText("10.123");
        frame.lon.setText("20.456");
        frame.addPoint();
        // Check if point is added to the list
        assertEquals(1, frame.points.size());
        
        frame.theta.setText("90");
        frame.lat.setText("15.678");
        frame.lon.setText("25.678");
        frame.addPoint();
        // Check if second point is added and fox is calculated correctly
        assertEquals(2, frame.points.size());
        assertEquals(1, frame.fox.size());
    }
    
    @Test
    public void testLoadLandmarks() {
        FoxHuntFrame frame = new FoxHuntFrame();
        // Check if landmarks are loaded correctly
        frame.loadLandmarks();
        assertEquals(2, frame.display.getLandmarks().size());
    }
    
    @Test
    public void testError() {
        FoxHuntFrame frame = new FoxHuntFrame();
        // Check if error handling works correctly
        frame.error("Test Error Message");
    }
}