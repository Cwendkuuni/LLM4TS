import static org.junit.Assert.*;
import org.junit.Test;

public class FoxHuntFrameTest {

    @Test
    public void testAddPoint() {
        FoxHuntFrame testFrame = new FoxHuntFrame();
        testFrame.theta.setText("45");
        testFrame.lat.setText("40.7128");
        testFrame.lon.setText("74.0060");
        testFrame.addPoint();
        
        // Test if a TrigPoint is added to points and fox Vectors
        assertEquals(1, testFrame.points.size());
        assertEquals(1, testFrame.fox.size());
        
        // Test if the TrigPoint added matches the input values
        TrigPoint addedPoint = (TrigPoint) testFrame.points.get(0);
        assertEquals(45, addedPoint.getAngle());
        assertEquals(40.7128, addedPoint.getY(), 0.001);
        assertEquals(74.0060, addedPoint.getX(), 0.001);
    }

    @Test
    public void testLoadLandmarks() {
        FoxHuntFrame testFrame = new FoxHuntFrame();
        
        // Test if landmarks are loaded successfully
        testFrame.loadLandmarks();
        assertEquals(2, testFrame.display.getLandmarks().size()); // Assuming there are 2 landmarks in the test file
        
        // Test if an error message is displayed when file not found
        testFrame.loadLandmarks();
        // Need to assert here that the error message dialog is displayed
    }

    @Test
    public void testError() {
        FoxHuntFrame testFrame = new FoxHuntFrame();
        
        // Test if system exits on abort option
        testFrame.error("Test error message");
        // Need to assert here that system exits on abort
        
        // Test if system continues on continue option
        testFrame.error("Test error message");
        // Need to assert here that system continues
    }
}