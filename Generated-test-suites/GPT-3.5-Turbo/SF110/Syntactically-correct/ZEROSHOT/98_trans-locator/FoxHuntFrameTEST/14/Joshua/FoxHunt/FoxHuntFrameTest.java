import org.junit.Test;
import static org.junit.Assert.*;

public class FoxHuntFrameTest {

    @Test
    public void testAddPoint() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.theta.setText("45");
        frame.lat.setText("37.1234");
        frame.lon.setText("-122.5678");

        frame.addPoint();

        assertEquals(1, frame.points.size());
        assertEquals(1, frame.fox.size());
        assertEquals(1, frame.posModel.getRowCount());
    }

    @Test
    public void testTablePrep() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.tablePrep();

        assertNotNull(frame.posModel);
        assertNotNull(frame.pos);
    }

    @Test
    public void testDisplayPrep() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.displayPrep();

        assertNotNull(frame.display);
    }

    @Test
    public void testLoadLandmarks() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.loadLandmarks();

        assertNotNull(frame.display.landmarks);
        assertEquals(0, frame.display.landmarks.size());
    }

    @Test
    public void testFoxTableModel() {
        FoxHuntFrame.FoxTableModel model = new FoxHuntFrame.FoxTableModel();

        assertEquals(2, model.getColumnCount());
        assertEquals(0, model.getRowCount());
        assertEquals("Lattitude", model.getColumnName(0));
        assertEquals("Longitude", model.getColumnName(1));
        assertEquals("", model.getValueAt(0, 0));
        assertEquals("", model.getValueAt(0, 1));
        assertSame(String.class, model.getColumnClass(0));
        assertSame(String.class, model.getColumnClass(1));
        assertFalse(model.isCellEditable(0, 0));
        assertFalse(model.isCellEditable(0, 1));
    }

}