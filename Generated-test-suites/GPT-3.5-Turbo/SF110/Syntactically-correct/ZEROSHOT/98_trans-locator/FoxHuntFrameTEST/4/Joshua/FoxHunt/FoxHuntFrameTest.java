import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FoxHuntFrameTest {

    private FoxHuntFrame foxHuntFrame;

    @Before
    public void setUp() {
        foxHuntFrame = new FoxHuntFrame();
    }

    @Test
    public void testAddPoint() {
        foxHuntFrame.theta.setText("45");
        foxHuntFrame.lat.setText("37.7749");
        foxHuntFrame.lon.setText("-122.4194");
        foxHuntFrame.addPoint();
        assertEquals(1, foxHuntFrame.points.size());
        assertEquals(1, foxHuntFrame.display.getPointCount());
    }

    @Test
    public void testFinalPrep() {
        foxHuntFrame.finalPrep();
        assertNotNull(foxHuntFrame.zoom);
        assertNotNull(foxHuntFrame.display);
        assertNotNull(foxHuntFrame.panel3);
        assertNotNull(foxHuntFrame.panel);
    }

    @Test
    public void testTextPrep() {
        foxHuntFrame.textPrep();
        assertNotNull(foxHuntFrame.theta);
        assertNotNull(foxHuntFrame.lat);
        assertNotNull(foxHuntFrame.lon);
        assertNotNull(foxHuntFrame.add);
    }

    @Test
    public void testTablePrep() {
        foxHuntFrame.tablePrep();
        assertNotNull(foxHuntFrame.posModel);
        assertNotNull(foxHuntFrame.pos);
    }

    @Test
    public void testDisplayPrep() {
        foxHuntFrame.displayPrep();
        assertNotNull(foxHuntFrame.display);
    }

    @Test
    public void testLoadLandmarks() {
        foxHuntFrame.loadLandmarks();
        assertTrue(foxHuntFrame.display.getLandmarkCount() > 0);
    }

    @Test
    public void testError() {
        foxHuntFrame.error("Test Error");
        // Test that the error message is displayed to the user
    }

    @Test
    public void testFoxTableModelGetColumnCount() {
        FoxHuntFrame.FoxTableModel model = foxHuntFrame.new FoxTableModel();
        assertEquals(2, model.getColumnCount());
    }

    @Test
    public void testFoxTableModelGetRowCount() {
        FoxHuntFrame.FoxTableModel model = foxHuntFrame.new FoxTableModel();
        foxHuntFrame.fox.add(new TrigPoint(37.7749, -122.4194, 45));
        assertEquals(1, model.getRowCount());
    }

    @Test
    public void testFoxTableModelGetColumnName() {
        FoxHuntFrame.FoxTableModel model = foxHuntFrame.new FoxTableModel();
        assertEquals("Lattitude", model.getColumnName(0));
        assertEquals("Longitude", model.getColumnName(1));
    }

    @Test
    public void testFoxTableModelGetValueAt() {
        FoxHuntFrame.FoxTableModel model = foxHuntFrame.new FoxTableModel();
        foxHuntFrame.fox.add(new TrigPoint(37.7749, -122.4194, 45));
        assertEquals("37.7749", model.getValueAt(0, 0));
        assertEquals("-122.4194", model.getValueAt(0, 1));
    }

    @Test
    public void testFoxTableModelGetColumnClass() {
        FoxHuntFrame.FoxTableModel model = foxHuntFrame.new FoxTableModel();
        assertEquals(String.class, model.getColumnClass(0));
        assertEquals(String.class, model.getColumnClass(1));
    }

    @Test
    public void testFoxTableModelIsCellEditable() {
        FoxHuntFrame.FoxTableModel model = foxHuntFrame.new FoxTableModel();
        assertFalse(model.isCellEditable(0, 0));
        assertFalse(model.isCellEditable(0, 1));
    }

}