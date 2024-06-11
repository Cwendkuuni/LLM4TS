import org.junit.Test;
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
        
        frame.theta.setText("60");
        frame.lat.setText("40.7128");
        frame.lon.setText("-74.0060");
        
        frame.addPoint();
        assertEquals(2, frame.points.size());
        assertEquals(2, frame.fox.size());
        assertEquals(2, frame.posModel.getRowCount());
    }

    @Test
    public void testFinalPrep() {
        FoxHuntFrame frame = new FoxHuntFrame();
        assertNotNull(frame.zoom);
        assertNotNull(frame.panel3);
        assertNotNull(frame.panel);
    }

    @Test
    public void testTextPrep() {
        FoxHuntFrame frame = new FoxHuntFrame();
        assertNotNull(frame.theta);
        assertNotNull(frame.lat);
        assertNotNull(frame.lon);
        assertNotNull(frame.add);
        assertNotNull(frame.panel1);
    }

    @Test
    public void testTablePrep() {
        FoxHuntFrame frame = new FoxHuntFrame();
        assertNotNull(frame.posModel);
        assertNotNull(frame.pos);
        assertEquals(0, frame.posModel.getRowCount());
    }

    @Test
    public void testDisplayPrep() {
        FoxHuntFrame frame = new FoxHuntFrame();
        assertNotNull(frame.display);
    }
}