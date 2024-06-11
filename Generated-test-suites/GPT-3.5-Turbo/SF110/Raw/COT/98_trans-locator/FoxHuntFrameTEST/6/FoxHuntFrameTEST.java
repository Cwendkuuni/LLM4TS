import org.junit.*;
import static org.junit.Assert.*;

public class FoxHuntFrameTest {

    // Public methods to test:
    // - actionPerformed(ActionEvent)
    // - addPoint()
    // - getColumnCount()
    // - getRowCount()
    // - getColumnName(int)
    // - getValueAt(int, int)
    // - getColumnClass(int)
    // - isCellEditable(int, int)

    @Test
    public void testActionPerformed() {
        // TODO: Write test case for actionPerformed(ActionEvent)
    }

    @Test
    public void testAddPoint() {
        // TODO: Write test case for addPoint()
    }

    @Test
    public void testGetColumnCount() {
        FoxHuntFrame.FoxTableModel model = new FoxHuntFrame().new FoxTableModel();
        int columnCount = model.getColumnCount();
        assertEquals(2, columnCount);
    }

    @Test
    public void testGetRowCount() {
        FoxHuntFrame foxHuntFrame = new FoxHuntFrame();
        foxHuntFrame.addPoint();
        FoxHuntFrame.FoxTableModel model = foxHuntFrame.new FoxTableModel();
        int rowCount = model.getRowCount();
        assertEquals(1, rowCount);
    }

    @Test
    public void testGetColumnName() {
        FoxHuntFrame.FoxTableModel model = new FoxHuntFrame().new FoxTableModel();
        String columnName = model.getColumnName(0);
        assertEquals("Lattitude", columnName);
    }

    @Test
    public void testGetValueAt() {
        FoxHuntFrame foxHuntFrame = new FoxHuntFrame();
        foxHuntFrame.addPoint();
        FoxHuntFrame.FoxTableModel model = foxHuntFrame.new FoxTableModel();
        Object valueAt = model.getValueAt(0, 0);
        assertTrue(valueAt instanceof String);
    }

    @Test
    public void testGetColumnClass() {
        FoxHuntFrame.FoxTableModel model = new FoxHuntFrame().new FoxTableModel();
        Class columnClass = model.getColumnClass(0);
        assertEquals(String.class, columnClass);
    }

    @Test
    public void testIsCellEditable() {
        FoxHuntFrame.FoxTableModel model = new FoxHuntFrame().new FoxTableModel();
        boolean isCellEditable = model.isCellEditable(0, 0);
        assertFalse(isCellEditable);
    }

    // TODO: Write test cases for potential edge cases and exception handling scenarios

    // 

    // TODO: Add individual test cases here

    // 
}