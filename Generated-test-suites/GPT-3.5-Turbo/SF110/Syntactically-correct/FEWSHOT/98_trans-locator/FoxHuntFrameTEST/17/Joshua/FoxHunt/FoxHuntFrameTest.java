package Joshua.FoxHunt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class FoxHuntFrameTest {
    @Test
    public void testAddPoint() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.theta.setText("45");
        frame.lat.setText("37.7749");
        frame.lon.setText("-122.4194");
        frame.addPoint();
        
        assertEquals(1, frame.points.size());
        assertEquals(1, frame.display.getPoints().size());
        assertEquals(0, frame.fox.size());
        assertEquals(0, frame.posModel.getRowCount());
        
        frame.lat.setText("34.0522");
        frame.lon.setText("-118.2437");
        frame.addPoint();
        
        assertEquals(2, frame.points.size());
        assertEquals(2, frame.display.getPoints().size());
        assertEquals(1, frame.fox.size());
        assertEquals(1, frame.posModel.getRowCount());
    }
    
    @Test
    public void testLoadLandmarks() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.loadLandmarks();
        
        assertEquals(0, frame.display.getLandmarks().size());
    }
    
    @Test
    public void testError() {
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.error("Test Error");
        
        // Verify that the dialog is shown and program exits
    }
    
    @Test
    public void testFoxTableModel() {
        FoxHuntFrame frame = new FoxHuntFrame();
        FoxHuntFrame.FoxTableModel model = frame.new FoxTableModel();
        
        assertEquals(2, model.getColumnCount());
        assertEquals(0, model.getRowCount());
        assertEquals("Lattitude", model.getColumnName(0));
        assertEquals("Longitude", model.getColumnName(1));
        assertEquals(Double.class, model.getValueAt(0, 0).getClass());
        assertEquals(Double.class, model.getValueAt(0, 1).getClass());
        assertEquals(false, model.isCellEditable(0, 0));
        assertEquals(false, model.isCellEditable(0, 1));
        
        // Test other methods if applicable
    }
}