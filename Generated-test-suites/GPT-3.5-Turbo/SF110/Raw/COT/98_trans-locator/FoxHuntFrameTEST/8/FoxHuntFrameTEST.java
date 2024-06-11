
import org.junit.Test;

public class FoxHuntFrameTest {

    @Test
    public void testAddPoint() {
        // Create a new instance of FoxHuntFrame
        FoxHuntFrame frame = new FoxHuntFrame();
        
        // Test adding a new point
        frame.theta.setText("90");
        frame.lat.setText("37.7749");
        frame.lon.setText("-122.4194");
        frame.addPoint();
        
        // Verify that the point is added to the points list
        assertEquals(1, frame.points.size());
        
        // Verify that the point is added to the display
        assertEquals(1, frame.display.getNumPoints());
        
        // Verify that no fox point is added when there is only one point
        assertEquals(0, frame.fox.size());
        
        // Test adding a second point
        frame.theta.setText("45");
        frame.lat.setText("34.0522");
        frame.lon.setText("-118.2437");
        frame.addPoint();
        
        // Verify that the second point is added to the points list
        assertEquals(2, frame.points.size());
        
        // Verify that the second point is added to the display
        assertEquals(2, frame.display.getNumPoints());
        
        // Verify that a fox point is added when there are two points
        assertEquals(1, frame.fox.size());
    }
    
    @Test
    public void testLoadLandmarks() {
        // Create a new instance of FoxHuntFrame
        FoxHuntFrame frame = new FoxHuntFrame();
        
        // Test loading landmarks from the file
        frame.loadLandmarks();
        
        // Verify that the landmarks are added to the display
        assertEquals(1, frame.display.getNumLandmarks());

        // Verify that the landmark coordinates are loaded correctly
        Landmark landmark = frame.display.getLandmark(0);
        assertEquals(37.7749, landmark.getLat(), 0.0001);
        assertEquals(-122.4194, landmark.getLon(), 0.0001);
    }
    
    @Test
    public void testError() {
        // Create a new instance of FoxHuntFrame
        FoxHuntFrame frame = new FoxHuntFrame();
        
        // Test displaying an error message and aborting
        frame.error("Can't Find the file");
        
        // Verify that the program is exited
        // (This test can't be automated, but it should be manually verified)
    }

}
