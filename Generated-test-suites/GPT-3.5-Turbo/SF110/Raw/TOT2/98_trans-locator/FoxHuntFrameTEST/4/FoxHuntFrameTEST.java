import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FoxHuntFrameTest {
    
    @Test
    public void testAddPoint() {
        FoxHuntFrame foxHuntFrame = new FoxHuntFrame();

        foxHuntFrame.theta.setText("45");
        foxHuntFrame.lat.setText("37.7749");
        foxHuntFrame.lon.setText("-122.4194");

        foxHuntFrame.addPoint();

        assertEquals(1, foxHuntFrame.points.size());
        assertEquals(1, foxHuntFrame.fox.size());
    }

    @Test
    public void testLoadLandmarks_FileNotFoundError() {
        FoxHuntFrame foxHuntFrame = new FoxHuntFrame();

        assertThrows(FileNotFoundException.class, () -> {
            foxHuntFrame.loadLandmarks();
        });
    }

    @Test
    public void testError_AbortOption() {
        FoxHuntFrame foxHuntFrame = new FoxHuntFrame();

        assertEquals(1, foxHuntFrame.error("Can't Find the file"));
    }
}