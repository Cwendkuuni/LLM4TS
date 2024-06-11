
import org.junit.*;
import static org.junit.Assert.*;

public class FoxHuntFrameTest {

    @Test
    public void testAddPoint() {
        // Create an instance of FoxHuntFrame
        FoxHuntFrame frame = new FoxHuntFrame();
        
         // Set input values for the text fields
         frame.theta.setText("45");
         frame.lat.setText("37.7749");
         frame.lon.setText("122.4194");
         
         // Perform the addPoint method
         frame.addPoint();
        
         // Get the last element of the points vector
         TrigPoint point = frame.points.elementAt(frame.points.size() - 1);
         
         // Assert that the point is added correctly
         assertEquals(45, point.getAngle());
         assertEquals(37.7749, point.getLat(), 0.0001);
         assertEquals(122.4194, point.getLon(), 0.0001);
         
         // Assert that the display and table are updated correctly
         assertEquals(1, frame.display.getPoints().size());
         assertEquals(1, frame.posModel.getRowCount());
    }

    @Test
    public void testLoadLandmarks() {
        // Create an instance of FoxHuntFrame
        FoxHuntFrame frame = new FoxHuntFrame();
        
        // Call the loadLandmarks method
        frame.loadLandmarks();
        
        // Assert that landmarks are loaded correctly
        assertEquals(3, frame.display.getLandmarks().size());
    }

    @Test
    public void testError() {
        // Create an instance of FoxHuntFrame
        FoxHuntFrame frame = new FoxHuntFrame();
        
        // Call the error method with an error message
        frame.error("Can't Find the file");
        
        // Assert that the error message is displayed correctly
        String errorMessage = frame.getOptionPaneMessage();
        assertEquals("Can't Find the file", errorMessage);
        
        // Assert that the program exits if the user chooses to abort
        boolean programExited = frame.hasExited();
        assertTrue(programExited);
    }
}
