import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class FoxHuntFrameTest {

    private FoxHuntFrame foxHuntFrame;

    @Before
    public void setUp() throws Exception {
        foxHuntFrame = new FoxHuntFrame();
    }

    @Test
    public void testAddPoint() {
        // Typical use case
        foxHuntFrame.theta.setText("45");
        foxHuntFrame.lat.setText("37.7749");
        foxHuntFrame.lon.setText("-122.4194");
        foxHuntFrame.addPoint();
        
        // Edge case - testing with negative angle
        foxHuntFrame.theta.setText("-45");
        foxHuntFrame.lat.setText("37.7749");
        foxHuntFrame.lon.setText("-122.4194");
        foxHuntFrame.addPoint();
        
        // Edge case - testing with invalid latitude and longitude
        foxHuntFrame.theta.setText("45");
        foxHuntFrame.lat.setText("invalid");
        foxHuntFrame.lon.setText("invalid");
        foxHuntFrame.addPoint();
    }

    @Test
    public void testLoadLandmarks() {
        // Typical use case
        foxHuntFrame.loadLandmarks();

        // Edge case - testing when file is not found
        // This should throw a FileNotFoundException
        // Test case for error scenario when landmarks file is not found
        assertThrows(FileNotFoundException.class, () -> foxHuntFrame.loadLandmarks());
        
        // Edge case - testing with incorrect file format
        // This should throw an IOException
        // Test case for error scenario when landmarks file has incorrect format
        assertThrows(IOException.class, () -> foxHuntFrame.loadLandmarks());
    }
}