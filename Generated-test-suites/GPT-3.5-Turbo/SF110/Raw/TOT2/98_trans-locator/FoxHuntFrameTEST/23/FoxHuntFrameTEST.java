import org.junit.Test;
import static org.junit.Assert.*;

public class FoxHuntFrameTest {
    // Test addPoint method with valid input
    @Test
    public void testAddPointValidInput() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.theta.setText("45");
        frame.lat.setText("37.7749");
        frame.lon.setText("-122.4194");
        frame.addPoint();
        assertEquals(1, frame.points.size());
    }

    // Test addPoint method with invalid angle input
    @Test(expected = NumberFormatException.class)
    public void testAddPointInvalidAngleInput() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.theta.setText("abc");
        frame.lat.setText("37.7749");
        frame.lon.setText("-122.4194");
        frame.addPoint();
    }

    // Test addPoint method with invalid latitude input
    @Test(expected = NumberFormatException.class)
    public void testAddPointInvalidLatInput() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.theta.setText("45");
        frame.lat.setText("abc");
        frame.lon.setText("-122.4194");
        frame.addPoint();
    }

    // Test addPoint method with invalid longitude input
    @Test(expected = NumberFormatException.class)
    public void testAddPointInvalidLonInput() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.theta.setText("45");
        frame.lat.setText("37.7749");
        frame.lon.setText("abc");
        frame.addPoint();
    }

    // Test loadLandmarks method with existing landmarks file
    @Test
    public void testLoadLandmarksExistingFile() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.loadLandmarks();
    }

    // Test loadLandmarks method with non-existing landmarks file
    @Test
    public void testLoadLandmarksNonExistingFile() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.error("Can't Find the file");
    }

    // Test error method with user choosing to continue
    @Test
    public void testErrorContinue() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.error("Error communicating with the file");
    }

    // Test FoxTableModel method getColumnCount
    @Test
    public void testFoxTableModelGetColumnCount() {
        FoxHuntFrame frame = new FoxHuntFrame();
        FoxHuntFrame.FoxTableModel model = frame.new FoxTableModel();
        assertEquals(2, model.getColumnCount());
    }

    // Test FoxTableModel method getRowCount
    @Test
    public void testFoxTableModelGetRowCount() {
        FoxHuntFrame frame = new FoxHuntFrame();
        FoxHuntFrame.FoxTableModel model = frame.new FoxTableModel();
        assertEquals(0, model.getRowCount());
    }
}