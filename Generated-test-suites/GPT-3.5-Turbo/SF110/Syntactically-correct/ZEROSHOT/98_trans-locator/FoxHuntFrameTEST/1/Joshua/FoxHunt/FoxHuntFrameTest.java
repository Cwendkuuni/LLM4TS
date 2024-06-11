import org.junit.Test;
import static org.junit.Assert.*;

public class FoxHuntFrameTest {
    
    @Test
    public void testAddPoint() {
        FoxHuntFrame frame = new FoxHuntFrame();
        
        frame.theta.setText("45");
        frame.lat.setText("50.12345");
        frame.lon.setText("60.98765");
        
        frame.addPoint();
        
        assertEquals(1, frame.points.size());
        assertEquals(1, frame.fox.size());
        assertNotNull(frame.display);
        assertNotNull(frame.posModel);
    }
    
    @Test
    public void testLoadLandmarks() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.loadLandmarks();
        
        assertTrue(frame.display.landmarks.size() > 0);
    }
    
    @Test
    public void testError() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.error("Test Error");
        
        // Assuming the user selects "Abort" or closes the dialog
        assertFalse(frame.isVisible());
    }
    
    @Test
    public void testFoxTableModel() {
        FoxHuntFrame frame = new FoxHuntFrame();
        FoxHuntFrame.FoxTableModel model = frame.new FoxTableModel();
        
        assertEquals(2, model.getColumnCount());
        assertEquals(frame.fox.size(), model.getRowCount());
        
        for (int i = 0; i < model.getRowCount(); i++) {
            assertEquals(String.class, model.getColumnClass(i));
            assertEquals(frame.fox.elementAt(i).getY(), model.getValueAt(i, 0));
            assertEquals(frame.fox.elementAt(i).getX(), model.getValueAt(i, 1));
        }
    }
}