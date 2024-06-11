import org.junit.Test;
import static org.junit.Assert.*;

public class FoxHuntFrameTest {

    @Test
    public void testAddPoint() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.theta.setText("90");
        frame.lat.setText("37.7749");
        frame.lon.setText("-122.4194");
        frame.addPoint();
        assertEquals(1, frame.points.size());
    }

    @Test
    public void testAddPoint_multiplePoints() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.theta.setText("90");
        frame.lat.setText("37.7749");
        frame.lon.setText("-122.4194");
        frame.addPoint();
        frame.theta.setText("180");
        frame.lat.setText("34.0522");
        frame.lon.setText("-118.2437");
        frame.addPoint();
        assertEquals(2, frame.points.size());
        assertEquals(1, frame.fox.size());
    }

    @Test
    public void testAddPoint_noPoints() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.theta.setText("90");
        frame.lat.setText("37.7749");
        frame.lon.setText("-122.4194");
        frame.addPoint();
        assertEquals(1, frame.points.size());
        assertEquals(0, frame.fox.size());
    }

    @Test
    public void testLoadLandmarks() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.loadLandmarks();
        assertNotNull(frame.display.landmarks);
        assertEquals(2, frame.display.landmarks.size());
    }

    @Test
    public void testLoadLandmarks_noFile() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.loadLandmarks();
        assertNotNull(frame.display.landmarks);
    }

    @Test
    public void testLoadLandmarks_invalidFile() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.loadLandmarks();
        assertNotNull(frame.display.landmarks);
    }
}