import static org.junit.Assert.*;
import org.junit.Test;

public class FoxHuntFrameTest {

    /* Test case for addPoint method */
    @Test
    public void testAddPoint() {
        FoxHuntFrame foxHuntFrame = new FoxHuntFrame();
        foxHuntFrame.theta.setText("90");
        foxHuntFrame.lat.setText("37.7749");
        foxHuntFrame.lon.setText("-122.4194");
        
        foxHuntFrame.addPoint();
        
        assertTrue(foxHuntFrame.points.size() == 1);
        assertTrue(foxHuntFrame.fox.size() == 0); // Fox not added yet
    }

    /* Test case for loadLandmarks method */
    @Test
    public void testLoadLandmarks() {
        FoxHuntFrame foxHuntFrame = new FoxHuntFrame();
        
        // Assuming landmarks.cfg file exists
        foxHuntFrame.loadLandmarks();
        
        assertTrue(foxHuntFrame.display.getLandmarks().size() > 0);
    }

    /* Test case for error method with option "Continue" selected */
    @Test
    public void testErrorContinue() {
        FoxHuntFrame foxHuntFrame = new FoxHuntFrame();
        
        foxHuntFrame.error("Error message");
    }

    /* Test case for FoxTableModel getColumnCount method */
    @Test
    public void testFoxTableModelGetColumnCount() {
        FoxHuntFrame foxHuntFrame = new FoxHuntFrame();
        FoxHuntFrame.FoxTableModel foxTableModel = foxHuntFrame.new FoxTableModel();
        
        int columnCount = foxTableModel.getColumnCount();
        
        assertEquals(2, columnCount);
    }

    /* Test case for FoxTableModel getValueAt method */
    @Test
    public void testFoxTableModelGetValueAt() {
        FoxHuntFrame foxHuntFrame = new FoxHuntFrame();
        FoxHuntFrame.FoxTableModel foxTableModel = foxHuntFrame.new FoxTableModel();
        
        Object value = foxTableModel.getValueAt(0, 0);
        
        assertNotNull(value);
    }

    /* Test case for FoxTableModel isCellEditable method */
    @Test
    public void testFoxTableModelIsCellEditable() {
        FoxHuntFrame foxHuntFrame = new FoxHuntFrame();
        FoxHuntFrame.FoxTableModel foxTableModel = foxHuntFrame.new FoxTableModel();
        
        boolean isEditable = foxTableModel.isCellEditable(0, 0);
        
        assertFalse(isEditable);
    }
}