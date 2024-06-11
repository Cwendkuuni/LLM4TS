import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JTailPanelTest {

    private JTailModel model;
    private JTailPanel jTailPanel;

    @Before
    public void setUp() {
        model = new JTailModel();
        jTailPanel = new JTailPanel(model);
    }

    @Test
    public void testGetCharacterHeight() {
        int characterHeight = jTailPanel.getCharacterHeight();
        assertEquals(15, characterHeight);
    }

    @Test
    public void testGetVerticalScrollBar() {
        JScrollBar verticalScrollBar = jTailPanel.getVerticalScrollBar();
        assertNotNull(verticalScrollBar);
    }

    @Test
    public void testUpdate() {
        model.resizeWindow(0, 10, 5, 20);
        model.scrollFile();

        // Verify expected values after update
        assertEquals(10, jTailPanel.getVerticalScrollBar().getMinimum());
        assertEquals(5, jTailPanel.getVerticalScrollBar().getValue());
        assertEquals(20, jTailPanel.getVerticalScrollBar().getMaximum());
        assertFalse(jTailPanel.getVerticalScrollBar().getValueIsAdjusting());
    }

    @Test
    public void testUpdateDataTable() {
        String lineNumbers = "1\n2\n3\n4\n5";
        String lines = "A\nB\nC\nD\nE";
        jTailPanel.updateDataTable(lineNumbers, lines);

        // Verify table model row count
        assertEquals(5, jTailPanel.tableModel.getRowCount());

        // Verify table model content
        assertEquals("1", jTailPanel.tableModel.getValueAt(0, 0));
        assertEquals("A", jTailPanel.tableModel.getValueAt(0, 1));
        assertEquals("2", jTailPanel.tableModel.getValueAt(1, 0));
        assertEquals("B", jTailPanel.tableModel.getValueAt(1, 1));
        assertEquals("3", jTailPanel.tableModel.getValueAt(2, 0));
        assertEquals("C", jTailPanel.tableModel.getValueAt(2, 1));
        assertEquals("4", jTailPanel.tableModel.getValueAt(3, 0));
        assertEquals("D", jTailPanel.tableModel.getValueAt(3, 1));
        assertEquals("5", jTailPanel.tableModel.getValueAt(4, 0));
        assertEquals("E", jTailPanel.tableModel.getValueAt(4, 1));

        // Verify column widths
        assertEquals(1, jTailPanel.dataTable.getColumnModel().getColumn(0).getPreferredWidth());
        assertEquals(4, jTailPanel.dataTable.getColumnModel().getColumn(1).getPreferredWidth());
    }

}