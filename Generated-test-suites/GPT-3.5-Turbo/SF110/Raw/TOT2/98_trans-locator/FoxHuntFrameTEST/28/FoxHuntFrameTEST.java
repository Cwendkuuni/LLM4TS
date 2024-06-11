import static org.junit.Assert.*;
import org.junit.Test;

public class FoxHuntFrameTest {

    @Test
    public void testAddPoint() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.theta.setText("45");
        frame.lat.setText("37.7749");
        frame.lon.setText("-122.4194");
        frame.addPoint();
        // Assert that a point is added successfully
        assertEquals(1, frame.points.size());
    }

    @Test
    public void testFinalPrep() {
        FoxHuntFrame frame = new FoxHuntFrame();
        // Test the final preparation method
        assertNotNull(frame.panel3);
    }

    @Test
    public void testTextPrep() {
        FoxHuntFrame frame = new FoxHuntFrame();
        // Test the text field preparation method
        assertNotNull(frame.add);
    }

    @Test
    public void testTablePrep() {
        FoxHuntFrame frame = new FoxHuntFrame();
        // Test the table preparation method
        assertNotNull(frame.panel2);
    }

    @Test
    public void testDisplayPrep() {
        FoxHuntFrame frame = new FoxHuntFrame();
        // Test the display preparation method
        assertNotNull(frame.display);
    }

    @Test
    public void testLoadLandmarks() {
        FoxHuntFrame frame = new FoxHuntFrame();
        // Test loading landmarks
        frame.loadLandmarks();
        assertEquals(0, frame.points.size());
    }

    @Test
    public void testError() {
        FoxHuntFrame frame = new FoxHuntFrame();
        // Test error handling
        frame.error("Test Error");
        // No assertion since it exits the program
    }

    @Test
    public void testFoxTableModel() {
        FoxHuntFrame.FoxTableModel model = new FoxHuntFrame().new FoxTableModel();
        // Test the FoxTableModel class
        assertEquals(2, model.getColumnCount());
        assertEquals(0, model.getRowCount());
    }
}