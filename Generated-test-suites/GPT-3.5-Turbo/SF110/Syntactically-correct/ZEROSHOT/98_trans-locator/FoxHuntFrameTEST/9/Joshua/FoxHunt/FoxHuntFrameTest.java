package Joshua.FoxHunt;

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
        
        // Verify that the point was added to the points vector
        assertEquals(1, frame.points.size());
        
        // Verify that the point was added to the display
        assertEquals(1, frame.display.getNumPoints());
        
        // Test adding a second point
        frame.theta.setText("90");
        frame.lat.setText("40.7128");
        frame.lon.setText("-74.0060");
        frame.addPoint();
        
        // Verify that the second point was added to the points vector
        assertEquals(2, frame.points.size());
        
        // Verify that the second point was added to the display
        assertEquals(2, frame.display.getNumPoints());
        
        // Verify that the first fox calculation was performed and added to the fox vector
        assertEquals(1, frame.fox.size());
        
        // Verify that the first fox calculation was added to the table
        assertEquals(1, frame.posModel.getRowCount());
    }

    @Test
    public void testLoadLandmarks() {
        FoxHuntFrame frame = new FoxHuntFrame();

        // Verify that the loadLandmarks function adds landmarks to the display
        assertEquals(10, frame.display.getNumLandmarks());
    }

}