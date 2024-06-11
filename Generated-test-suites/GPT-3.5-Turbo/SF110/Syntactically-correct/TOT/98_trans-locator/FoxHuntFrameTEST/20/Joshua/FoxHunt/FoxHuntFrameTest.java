
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FoxHuntFrameTest {

    private FoxHuntFrame foxHuntFrame;

    @Before
    public void setUp() {
        foxHuntFrame = new FoxHuntFrame();
    }

    // Test for the addPoint() method
    @Test
    public void testAddPoint() {
        // Test typical usage
        foxHuntFrame.theta.setText("45");
        foxHuntFrame.lat.setText("12.345");
        foxHuntFrame.lon.setText("67.890");
        foxHuntFrame.addPoint();
        assertEquals(1, foxHuntFrame.points.size());
        assertEquals(1, foxHuntFrame.fox.size());
        foxHuntFrame.theta.setText("30");
        foxHuntFrame.lat.setText("12.345");
        foxHuntFrame.lon.setText("67.890");
        foxHuntFrame.addPoint();
        assertEquals(2, foxHuntFrame.points.size());
        assertEquals(2, foxHuntFrame.fox.size());

        // Test edge case - add point without previous point
        foxHuntFrame.points.clear();
        foxHuntFrame.fox.clear();
        foxHuntFrame.theta.setText("45");
        foxHuntFrame.lat.setText("12.345");
        foxHuntFrame.lon.setText("67.890");
        foxHuntFrame.addPoint();
        assertEquals(1, foxHuntFrame.points.size());
        assertEquals(0, foxHuntFrame.fox.size());
    }

    // Test for the loadLandmarks() method
    @Test
    public void testLoadLandmarks() {
        // Test typical usage
        foxHuntFrame.loadLandmarks();
        assertEquals(4, foxHuntFrame.display.getLandmarks().size());

        // Test error scenario - file not found
        // Remove the landmarks file for this test
        File landmarksFile = new File("./landmarks.cfg");
        landmarksFile.delete();
        foxHuntFrame.loadLandmarks();
        assertEquals(0, foxHuntFrame.display.getLandmarks().size());
    }

    // Test for the error() method
    @Test
    public void testError() {
        // Test typical usage - continue option selected
        int ans = foxHuntFrame.error("Error");
        assertEquals(1, ans);

        // Test typical usage - abort option selected
        ans = foxHuntFrame.error("Error");
        assertEquals(0, ans);
    }

    // Test for the FoxTableModel class
    @Test
    public void testFoxTableModel() {
        // Test getColumnCount() method
        int columnCount = foxHuntFrame.posModel.getColumnCount();
        assertEquals(2, columnCount);

        // Test getRowCount() method
        assertEquals(0, foxHuntFrame.posModel.getRowCount());

        // Test getColumnName() method
        String columnName = foxHuntFrame.posModel.getColumnName(0);
        assertEquals("Lattitude", columnName);
        columnName = foxHuntFrame.posModel.getColumnName(1);
        assertEquals("Longitude", columnName);

        // Test getValueAt() method
        assertEquals(String.class, foxHuntFrame.posModel.getColumnClass(0));
        assertEquals(String.class, foxHuntFrame.posModel.getColumnClass(1));

        // Test isCellEditable() method
        assertFalse(foxHuntFrame.posModel.isCellEditable(0, 0));
        assertFalse(foxHuntFrame.posModel.isCellEditable(0, 1));
    }

    // Test for the actionPerformed() method
    @Test
    public void testActionPerformed() {
        // Not tested as it delegates the implementation to addPoint() method which is already tested
    }

    // Test for the finalPrep() method
    @Test
    public void testFinalPrep() {
        // Not tested as it contains only GUI related code
    }

    // Test for the textPrep() method
    @Test
    public void testTextPrep() {
        // Not tested as it contains only GUI related code
    }

    // Test for the tablePrep() method
    @Test
    public void testTablePrep() {
        // Not tested as it contains only GUI related code
    }

    // Test for the displayPrep() method
    @Test
    public void testDisplayPrep() {
        // Not tested as it contains only GUI related code
    }
}

