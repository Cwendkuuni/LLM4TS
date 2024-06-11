import org.junit.*;
import static org.junit.Assert.*;

public class FoxHuntFrameTest {

    @Test
    public void testAddPoint() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.theta.setText("45");
        frame.lat.setText("37.7749");
        frame.lon.setText("-122.4194");
        frame.addPoint();
        assertEquals(1, frame.points.size());
        assertEquals(1, frame.fox.size());
        assertEquals(1, frame.posModel.getRowCount());
        assertEquals("37.7749", frame.posModel.getValueAt(0, 0));
        assertEquals("-122.4194", frame.posModel.getValueAt(0, 1));
        assertEquals(1, frame.display.points.size());
        assertEquals(1, frame.display.landmarks.size());
    }

    @Test
    public void testAddMultiplePoints() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.theta.setText("45");
        frame.lat.setText("37.7749");
        frame.lon.setText("-122.4194");
        frame.addPoint();
        assertEquals(1, frame.points.size());
        assertEquals(1, frame.fox.size());
        
        frame.theta.setText("90");
        frame.lat.setText("37.7749");
        frame.lon.setText("-122.4194");
        frame.addPoint();
        assertEquals(2, frame.points.size());
        assertEquals(2, frame.fox.size());
        
        assertEquals(2, frame.posModel.getRowCount());
        assertEquals("37.7749", frame.posModel.getValueAt(0, 0));
        assertEquals("-122.4194", frame.posModel.getValueAt(0, 1));
        assertEquals("37.7749", frame.posModel.getValueAt(1, 0));
        assertEquals("-122.4194", frame.posModel.getValueAt(1, 1));
        
        assertEquals(2, frame.display.points.size());
        assertEquals(2, frame.display.landmarks.size());
    }

    @Test
    public void testZoom() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.zoom.setText("2");
        frame.actionPerformed(null);
        assertEquals(2, frame.display.zoom);
    }

    @Test
    public void testLoadLandmarks() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.loadLandmarks();
        assertEquals(1, frame.display.landmarks.size());
    }

    @Test
    public void testErrorAbort() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.error("Can't Find the file");
        assertEquals(0, frame.getState());
    }

    @Test
    public void testErrorContinue() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.error("Error communicating with the file");
        assertNotEquals(0, frame.getState());
    }
}

