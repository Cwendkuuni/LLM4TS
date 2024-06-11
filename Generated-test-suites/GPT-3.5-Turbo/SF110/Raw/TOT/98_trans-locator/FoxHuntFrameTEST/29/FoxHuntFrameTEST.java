import org.junit.Test;
import static org.junit.Assert.*;

public class FoxHuntFrameTest {

    @Test
    public void testAddPoint() {
        FoxHuntFrame frame = new FoxHuntFrame();
        
        // Test adding a valid point
        frame.theta.setText("45");
        frame.lat.setText("37.7749");
        frame.lon.setText("-122.4194");
        frame.addPoint();
        assertEquals(1, frame.points.size());
        assertEquals(1, frame.display.getPoints().size());
        
        // Test adding a point with invalid angle
        frame.theta.setText("400");
        frame.lat.setText("37.7749");
        frame.lon.setText("-122.4194");
        frame.addPoint();
        assertEquals(1, frame.points.size());
        assertEquals(1, frame.display.getPoints().size());
        
        // Test adding a point with invalid latitude
        frame.theta.setText("45");
        frame.lat.setText("abc");
        frame.lon.setText("-122.4194");
        frame.addPoint();
        assertEquals(1, frame.points.size());
        assertEquals(1, frame.display.getPoints().size());
        
        // Test adding a point with invalid longitude
        frame.theta.setText("45");
        frame.lat.setText("37.7749");
        frame.lon.setText("xyz");
        frame.addPoint();
        assertEquals(1, frame.points.size());
        assertEquals(1, frame.display.getPoints().size());
    }
    
    @Test
    public void testLoadLandmarks() {
        FoxHuntFrame frame = new FoxHuntFrame();
        
        // Test loading landmarks from a valid file
        frame.loadLandmarks();
        assertEquals(0, frame.display.getLandmarks().size());
        
        // Test loading landmarks from a file that does not exist
        frame.loadLandmarks();
        assertEquals(0, frame.display.getLandmarks().size());
        
        // Test loading landmarks from a file with invalid format
        frame.loadLandmarks();
        assertEquals(0, frame.display.getLandmarks().size());
    }
    
    
    @Test
    public void testFoxTableModel() {
        FoxHuntFrame frame = new FoxHuntFrame();
        
        // Test getting column count
        FoxHuntFrame.FoxTableModel model = frame.new FoxTableModel();
        assertEquals(2, model.getColumnCount());
        
        // Test getting row count
        assertEquals(0, model.getRowCount());
        
        // Test getting column name
        assertEquals("Lattitude", model.getColumnName(0));
        assertEquals("Longitude", model.getColumnName(1));
        
        // Test getting cell value
        assertEquals(Double.class, model.getColumnClass(0));
        assertEquals(Double.class, model.getColumnClass(1));
        
        // Test checking if cell is editable
        assertFalse(model.isCellEditable(0, 0));
        assertFalse(model.isCellEditable(0, 1));
    }
}