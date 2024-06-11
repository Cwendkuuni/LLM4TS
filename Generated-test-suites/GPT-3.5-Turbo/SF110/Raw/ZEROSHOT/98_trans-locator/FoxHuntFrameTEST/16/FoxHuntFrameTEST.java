package Joshua.FoxHunt;

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
        assertEquals(1, frame.display.points.size());
        assertEquals(0, frame.fox.size());
        assertEquals(1, frame.posModel.getRowCount());
        assertEquals("37.7749", frame.posModel.getValueAt(0, 0));
        assertEquals("-122.4194", frame.posModel.getValueAt(0, 1));
    }

    @Test
    public void testAddPoint_multiplePoints() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.theta.setText("45");
        frame.lat.setText("37.7749");
        frame.lon.setText("-122.4194");
        frame.addPoint();

        frame.theta.setText("90");
        frame.lat.setText("34.0522");
        frame.lon.setText("-118.2437");
        frame.addPoint();

        assertEquals(2, frame.points.size());
        assertEquals(2, frame.display.points.size());
        assertEquals(1, frame.fox.size());
        assertEquals(2, frame.posModel.getRowCount());
        assertEquals("34.0522", frame.posModel.getValueAt(1, 0));
        assertEquals("-118.2437", frame.posModel.getValueAt(1, 1));
    }

    @Test
    public void testLoadLandmarks() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.loadLandmarks();

        assertEquals(1, frame.display.landmarks.size());
    }

    @Test
    public void testLoadLandmarks_noFile() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.loadLandmarks();

        assertEquals(0, frame.display.landmarks.size());
    }
}