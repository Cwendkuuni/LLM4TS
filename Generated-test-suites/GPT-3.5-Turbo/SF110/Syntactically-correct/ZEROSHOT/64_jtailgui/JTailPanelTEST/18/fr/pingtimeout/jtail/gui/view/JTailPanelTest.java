import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JTailPanelTest {
    
    JTailPanel panel;
    JTailModel model;
    
    @Before
    public void setUp() {
        model = new JTailModel();
        panel = new JTailPanel(model);
    }
    
    @Test
    public void testGetCharacterHeight() {
        int expectedHeight = panel.dataTable.getFontMetrics(panel.dataTable.getFont()).getHeight() - 1;
        assertEquals(expectedHeight, panel.getCharacterHeight());
    }
    
    @Test
    public void testGetVerticalScrollBar() {
        assertEquals(panel.verticalScrollBar, panel.getVerticalScrollBar());
    }
    
    @Test
    public void testUpdate_ResizedEvent() {
        JTailModelEvent event = new JTailModelEvent(JTailModelEvent.Type.RESIZED, 1, 10, 5, 20);
        panel.update(model, event);
        assertEquals(1, panel.verticalScrollBar.getModel().getMinimum());
        assertEquals(10, panel.verticalScrollBar.getModel().getValue());
        assertEquals(5, panel.verticalScrollBar.getModel().getExtent());
        assertEquals(20, panel.verticalScrollBar.getModel().getMaximum());
        assertTrue(panel.verticalScrollBar.getModel().getValueIsAdjusting());
        assertFalse(panel.verticalScrollBar.isEnabled());
        assertNotNull(panel.dataTable.getModel());
    }
    
    @Test
    public void testUpdate_ScrolledEvent() {
        JTailModelEvent event = new JTailModelEvent(JTailModelEvent.Type.SCROLLED, 0, 0, 0, 0);
        panel.update(model, event);
        assertNotNull(panel.dataTable.getModel());
    }
    
    @Test
    public void testUpdate_OtherEvent() {
        JTailModelEvent event = new JTailModelEvent(JTailModelEvent.Type.OTHER, 0, 0, 0, 0);
        panel.update(model, event);
        assertNotNull(panel.dataTable.getModel());
    }
    
    @Test
    public void testUpdateDataTable() {
        panel.updateDataTable("1\n2\n3", "Line 1\nLine 2\nLine 3");
        assertEquals(3, panel.tableModel.getRowCount());
        assertEquals("1", panel.tableModel.getValueAt(0, 0));
        assertEquals("Line 1", panel.tableModel.getValueAt(0, 1));
        assertEquals("2", panel.tableModel.getValueAt(1, 0));
        assertEquals("Line 2", panel.tableModel.getValueAt(1, 1));
        assertEquals("3", panel.tableModel.getValueAt(2, 0));
        assertEquals("Line 3", panel.tableModel.getValueAt(2, 1));
    }
}