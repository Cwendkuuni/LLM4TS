import org.junit.*;
import static org.junit.Assert.*;

public class JTailPanelTest {

    @Test
    public void testGetCharacterHeight() {
        JTailModel model = new JTailModel();
        JTailPanel panel = new JTailPanel(model);
        
        int expectedHeight = 19; // Assuming a default font with height 20
        
        assertEquals(expectedHeight, panel.getCharacterHeight());
    }
    
    @Test
    public void testGetVerticalScrollBar() {
        JTailModel model = new JTailModel();
        JTailPanel panel = new JTailPanel(model);
        
        assertNotNull(panel.getVerticalScrollBar());
    }
    
    @Test
    public void testUpdate_ResizedEvent_MinLineEqualsFirstLineMaxLineEqualsNbDisplayedLines() {
        JTailModel model = new JTailModel();
        JTailPanel panel = new JTailPanel(model);
        
        JTailModelEvent event = new JTailModelEvent(JTailModelEvent.Type.RESIZED, 10, 10, 10, 10);
        
        panel.update(model, event);
        
        // Assert that verticalScrollBar is disabled
        assertFalse(panel.getVerticalScrollBar().isEnabled());
        
        // Assert that tableModel is updated
        assertEquals(1, panel.dataTable.getRowCount());
        assertEquals("10", panel.dataTable.getValueAt(0, 0));
        assertEquals("", panel.dataTable.getValueAt(0, 1));
    }
    
    @Test
    public void testUpdate_ResizedEvent_MinLineNotEqualsFirstLineMaxLineNotEqualsNbDisplayedLines() {
        JTailModel model = new JTailModel();
        JTailPanel panel = new JTailPanel(model);
        
        JTailModelEvent event = new JTailModelEvent(JTailModelEvent.Type.RESIZED, 10, 5, 20, 10);
        
        panel.update(model, event);
        
        // Assert that verticalScrollBar is enabled
        assertTrue(panel.getVerticalScrollBar().isEnabled());
        
        // Assert that tableModel is updated
        assertEquals(1, panel.dataTable.getRowCount());
        assertEquals("10", panel.dataTable.getValueAt(0, 0));
        assertEquals("", panel.dataTable.getValueAt(0, 1));
    }
    
    @Test
    public void testUpdate_ScrolledEvent() {
        JTailModel model = new JTailModel();
        JTailPanel panel = new JTailPanel(model);
        
        // Simulate scrolling event
        model.notifyObservers(new JTailModelEvent(JTailModelEvent.Type.SCROLLED, 0, 0, 0, 0));
        
        // Assert that tableModel is updated
        // ... Please add assertions based on the expected behavior of the method
        
    }
    
    @Test
    public void testUpdate_InvalidEvent() {
        JTailModel model = new JTailModel();
        JTailPanel panel = new JTailPanel(model);
        
        // Simulate invalid event (not resized or scrolled)
        model.notifyObservers(new JTailModelEvent(JTailModelEvent.Type.OTHER, 0, 0, 0, 0));
        
        // Assert that tableModel is not updated
        // ... Please add assertions based on the expected behavior of the method
        
    }
    
    @Test
    public void testUpdateDataTable() {
        JTailModel model = new JTailModel();
        JTailPanel panel = new JTailPanel(model);
        
        // Mock data
        String lineNumbers = "1\n2\n3\n4\n5";
        String lines = "A\nB\nC\nD\nE";
        
        panel.updateDataTable(lineNumbers, lines);
        
        // Assert that tableModel is updated
        assertEquals(5, panel.dataTable.getRowCount());
        assertEquals("1", panel.dataTable.getValueAt(0, 0));
        assertEquals("A", panel.dataTable.getValueAt(0, 1));
        // ... Please add more assertions based on the expected behavior of the method
        
    }
}