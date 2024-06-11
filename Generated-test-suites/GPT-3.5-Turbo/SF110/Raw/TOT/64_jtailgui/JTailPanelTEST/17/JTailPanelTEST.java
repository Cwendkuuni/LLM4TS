import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JTailPanelTest {

    private JTailModel model;
    private JTailPanel panel;
    
    @Before
    public void setUp() {
        model = new JTailModel();
        panel = new JTailPanel(model);
    }
    
    // Test cases for JTailPanel constructor
    @Test
    public void testJTailPanelConstructor() {
        assertNotNull(panel.model);
        assertNotNull(panel.scrollPane);
        assertNotNull(panel.verticalScrollBar);
        assertNotNull(panel.dataTable);
        assertNotNull(panel.tableModel);
        assertEquals(0, panel.tableModel.getRowCount());
        assertEquals(2, panel.tableModel.getColumnCount());
        assertEquals(0, panel.characterHeight);
    }
    
    // Test cases for getCharacterHeight method
    @Test
    public void testGetCharacterHeight() {
        int expectedHeight = panel.dataTable.getFontMetrics(panel.dataTable.getFont()).getHeight() - 1;
        assertEquals(expectedHeight, panel.getCharacterHeight());
    }
    
    // Test cases for getVerticalScrollBar method
    @Test
    public void testGetVerticalScrollBar() {
        assertEquals(panel.verticalScrollBar, panel.getVerticalScrollBar());
    }
    
    // Test cases for update method
    @Test
    public void testUpdateResizedEvent() {
        JTailModelEvent event = new JTailModelEvent(JTailModelEvent.Type.RESIZED, 0, 0, 0, 0);
        panel.update(panel.model, event);
        
        // Assert that the vertical scroll bar is disabled when minLine equals firstLine and maxLine equals nbDisplayedLines
        assertFalse(panel.verticalScrollBar.isEnabled());
        
        // Assert that the vertical scroll bar is enabled when minLine does not equal firstLine or maxLine does not equal nbDisplayedLines
        event = new JTailModelEvent(JTailModelEvent.Type.RESIZED, 0, 1, 2, 2);
        panel.update(panel.model, event);
        assertTrue(panel.verticalScrollBar.isEnabled());
    }
    
    @Test
    public void testUpdateScrolledEvent() {
        JTailModelEvent event = new JTailModelEvent(JTailModelEvent.Type.SCROLLED, 0, 0, 0, 0);
        
        // Assert that the data table is updated with the line numbers and content
        panel.update(panel.model, event);
        assertNotNull(panel.tableModel.getValueAt(0, 0));
        assertNotNull(panel.tableModel.getValueAt(0, 1));
    }
    
    // Test cases for updateDataTable method
    @Test
    public void testUpdateDataTable() {
        // Assert that the table model is cleared before updating
        panel.tableModel.addRow(new String[]{"1", "Line 1"});
        panel.updateDataTable("1\n2\n", "Line 2\nLine 3\n");
        assertEquals(0, panel.tableModel.getRowCount());
        
        // Assert that the table model is updated correctly with line numbers and content
        panel.updateDataTable("1\n2\n3\n", "Line 1\nLine 2\nLine 3\n");
        assertEquals(3, panel.tableModel.getRowCount());
        assertEquals("1", panel.tableModel.getValueAt(0, 0));
        assertEquals("Line 1", panel.tableModel.getValueAt(0, 1));
        assertEquals("2", panel.tableModel.getValueAt(1, 0));
        assertEquals("Line 2", panel.tableModel.getValueAt(1, 1));
        assertEquals("3", panel.tableModel.getValueAt(2, 0));
        assertEquals("Line 3", panel.tableModel.getValueAt(2, 1));
        
        // Assert that the column widths are resized correctly
        panel.dataTable.setPreferredSize(new Dimension(100, 100));
        panel.updateDataTable("1\n2\n3\n4\n5\n6\n", "Line 1\nLine 2\nLine 3\nLine 4\nLine 5\nLine 6\n");
        assertEquals(50, panel.dataTable.getColumnModel().getColumn(0).getWidth());
        assertEquals(50, panel.dataTable.getColumnModel().getColumn(1).getWidth());
    }
}