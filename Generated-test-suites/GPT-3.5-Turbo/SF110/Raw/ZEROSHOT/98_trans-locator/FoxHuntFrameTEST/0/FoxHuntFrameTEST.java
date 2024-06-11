import org.junit.*;
import static org.junit.Assert.*;

public class FoxHuntFrameTest {

    @Test
    public void testActionPerformed() {
        // Test when action is performed
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.lat.setText("40.7128");
        frame.lon.setText("74.0060");
        frame.theta.setText("45");
        frame.addPoint();
        
        // Assert that the fox vector size is increased by 1
        assertEquals(1, frame.fox.size());
        
        // Assert that the points vector size is increased by 1
        assertEquals(1, frame.points.size());
    }

    @Test
    public void testAddPoint() {
        // Test when a point is added
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.lat.setText("40.7128");
        frame.lon.setText("74.0060");
        frame.theta.setText("45");
        frame.addPoint();
        
        // Assert that the fox vector size is increased by 1
        assertEquals(1, frame.fox.size());
        
        // Assert that the points vector size is increased by 1
        assertEquals(1, frame.points.size());
    }

    @Test
    public void testFinalPrep() {
        // Test the finalPrep() method
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.finalPrep();
        
        // Assert that the zoom JTextField is initialized
        assertNotNull(frame.zoom);
        
        // Assert that the panel3 JPanel is initialized
        assertNotNull(frame.panel3);
        
        // Assert that the display HuntDisplay object is initialized
        assertNotNull(frame.display);
        
        // Assert that the panel JPanel is initialized
        assertNotNull(frame.panel);
    }

    @Test
    public void testTextPrep() {
        // Test the textPrep() method
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.textPrep();
        
        // Assert that the theta JTextField is initialized
        assertNotNull(frame.theta);
        
        // Assert that the lat JTextField is initialized
        assertNotNull(frame.lat);
        
        // Assert that the lon JTextField is initialized
        assertNotNull(frame.lon);
        
        // Assert that the add JButton is initialized
        assertNotNull(frame.add);
        
        // Assert that the panel1 JPanel is initialized
        assertNotNull(frame.panel1);
    }

    @Test
    public void testTablePrep() {
        // Test the tablePrep() method
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.tablePrep();
        
        // Assert that the posModel FoxTableModel object is initialized
        assertNotNull(frame.posModel);
        
        // Assert that the pos JTable object is initialized
        assertNotNull(frame.pos);
    }

    @Test
    public void testDisplayPrep() {
        // Test the displayPrep() method
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.displayPrep();
        
        // Assert that the display HuntDisplay object is initialized
        assertNotNull(frame.display);
    }

    @Test
    public void testLoadLandmarks() {
        // Test the loadLandmarks() method
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.loadLandmarks();
        
        // Assert that the landmarks are loaded successfully
        assertEquals(2, frame.display.landmarks.size());
    }

    @Test
    public void testError() {
        // Test the error() method
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.error("Error: Can't Find the file");
        
        // Assert that the error dialog is displayed
        assertFalse(frame.isShowing());
    }

    @Test
    public void testFoxTableModel() {
        // Test the FoxTableModel constructor
        FoxHuntFrame.FoxTableModel model = new FoxHuntFrame.FoxTableModel();
        
        // Assert that the column names array is initialized
        assertNotNull(model.colNames);
    }

    @Test
    public void testGetColumnCount() {
        // Test the getColumnCount() method
        FoxHuntFrame.FoxTableModel model = new FoxHuntFrame.FoxTableModel();
        
        // Assert that the column count is 2
        assertEquals(2, model.getColumnCount());
    }

    @Test
    public void testGetRowCount() {
        // Test the getRowCount() method
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.fox.add(new TrigPoint(40.7128, 74.0060, 45));
        frame.fox.add(new TrigPoint(37.7749, 122.4194, 30));
        
        // Assert that the row count is 2
        assertEquals(2, frame.posModel.getRowCount());
    }

    @Test
    public void testGetColumnName() {
        // Test the getColumnName() method
        FoxHuntFrame.FoxTableModel model = new FoxHuntFrame.FoxTableModel();
        
        // Assert that the column name at index 0 is "Lattitude"
        assertEquals("Lattitude", model.getColumnName(0));
        
        // Assert that the column name at index 1 is "Longitude"
        assertEquals("Longitude", model.getColumnName(1));
    }

    @Test
    public void testGetValueAt() {
        // Test the getValueAt() method
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.fox.add(new TrigPoint(40.7128, 74.0060, 45));
        
        // Assert that the value at row 0, column 0 is "40.7128"
        assertEquals("40.7128", frame.posModel.getValueAt(0, 0));
        
        // Assert that the value at row 0, column 1 is "74.0060"
        assertEquals("74.0060", frame.posModel.getValueAt(0, 1));
    }

    @Test
    public void testGetColumnClass() {
        // Test the getColumnClass() method
        FoxHuntFrame frame = new FoxHuntFrame();
        frame.fox.add(new TrigPoint(40.7128, 74.0060, 45));
        
        // Assert that the column class at index 0 is String
        assertEquals(String.class, frame.posModel.getColumnClass(0));
        
        // Assert that the column class at index 1 is String
        assertEquals(String.class, frame.posModel.getColumnClass(1));
    }

    @Test
    public void testIsCellEditable() {
        // Test the isCellEditable() method
        FoxHuntFrame.FoxTableModel model = new FoxHuntFrame.FoxTableModel();
        
        // Assert that the cell at row 0, column 0 is not editable
        assertFalse(model.isCellEditable(0, 0));
        
        // Assert that the cell at row 0, column 1 is not editable
        assertFalse(model.isCellEditable(0, 1));
    }
}