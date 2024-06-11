import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FoxHuntFrameTest {
    
    private FoxHuntFrame frame;
    
    @Before
    public void setUp() {
        frame = new FoxHuntFrame();
    }
    
    @Test
    public void testAddPoint() {
        // Test adding a single point
        frame.theta.setText("45");
        frame.lat.setText("37.7749");
        frame.lon.setText("-122.4194");
        frame.addPoint();
        assertEquals(1, frame.points.size());
        assertEquals(1, frame.fox.size());
        
        // Test adding multiple points
        frame.theta.setText("90");
        frame.lat.setText("41.8781");
        frame.lon.setText("-87.6298");
        frame.addPoint();
        assertEquals(2, frame.points.size());
        assertEquals(2, frame.fox.size());
    }
    
    @Test
    public void testLoadLandmarks() {
        // Test loading landmarks from a file
        frame.loadLandmarks();
        assertEquals(3, frame.display.getLandmarks().size());
    }
    
    @Test
    public void testActionPerformed() {
        // Test actionPerformed method with ActionEvent
        ActionEvent event = new ActionEvent(frame.add, ActionEvent.ACTION_PERFORMED, "");
        frame.actionPerformed(event);
        assertEquals(1, frame.points.size());
        assertEquals(1, frame.fox.size());
    }
    
    @Test
    public void testTableModel() {
        // Test FoxTableModel getColumnCount method
        FoxHuntFrame.FoxTableModel model = frame.posModel;
        assertEquals(2, model.getColumnCount());
        
        // Test FoxTableModel getRowCount method
        assertEquals(0, model.getRowCount());
        
        // Test FoxTableModel getColumnName method
        assertEquals("Lattitude", model.getColumnName(0));
        assertEquals("Longitude", model.getColumnName(1));
        
        // Test FoxTableModel getValueAt method
        assertEquals(0, model.getValueAt(0, 0));
        assertEquals(0, model.getValueAt(0, 1));
        
        // Test FoxTableModel getColumnClass method
        assertEquals(String.class, model.getColumnClass(0));
        assertEquals(String.class, model.getColumnClass(1));
        
        // Test FoxTableModel isCellEditable method
        assertFalse(model.isCellEditable(0, 0));
        assertFalse(model.isCellEditable(0, 1));
    }
}