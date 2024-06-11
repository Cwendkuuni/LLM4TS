import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.event.AdjustmentEvent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JScrollBar;
import javax.swing.table.DefaultTableModel;
import fr.pingtimeout.jtail.gui.model.JTailModel;

public class JTailPanelTest {
    
    @Test
    public void testGetCharacterHeight() {
        JTailModel model = new JTailModel();
        JTailPanel panel = new JTailPanel(model);
        int expectedHeight = panel.dataTable.getFontMetrics(panel.dataTable.getFont()).getHeight() - 1;
        assertEquals(expectedHeight, panel.getCharacterHeight());
    }
    
    @Test
    public void testGetVerticalScrollBar() {
        JTailModel model = new JTailModel();
        JTailPanel panel = new JTailPanel(model);
        assertEquals(panel.verticalScrollBar, panel.getVerticalScrollBar());
    }
    
    @Test
    public void testObserverUpdateResizedEvent() {
        JTailModel model = new JTailModel();
        JTailPanel panel = new JTailPanel(model);
        JScrollPane scrollPane = panel.scrollPane;
        JTable dataTable = panel.dataTable;
        DefaultTableModel tableModel = panel.tableModel;
        JScrollBar verticalScrollBar = panel.verticalScrollBar;
        model.addObserver(panel);
        
        // Simulate JTailModelEvent Type RESIZED
        model.setLineNumbers("1\n2\n3\n4\n5");
        model.setContent("A\nB\nC\nD\nE");
        model.notifyObservers(new JTailModelEvent(JTailModelEvent.Type.RESIZED, 1, 3, 5));
        
        // Check if the table model has been updated correctly
        assertEquals(3, tableModel.getRowCount());
        assertEquals("1", tableModel.getValueAt(0, 0));
        assertEquals("A", tableModel.getValueAt(0, 1));
        assertEquals("2", tableModel.getValueAt(1, 0));
        assertEquals("B", tableModel.getValueAt(1, 1));
        assertEquals("3", tableModel.getValueAt(2, 0));
        assertEquals("C", tableModel.getValueAt(2, 1));
        
        // Check if the vertical scroll bar is enabled correctly
        assertFalse(verticalScrollBar.isEnabled());
        
        // Check if the scroll pane is updated correctly
        assertEquals(4, verticalScrollBar.getMinimum());
        assertEquals(1, verticalScrollBar.getValue());
        assertEquals(3, verticalScrollBar.getExtent());
        assertEquals(5, verticalScrollBar.getMaximum());
    }
    
    @Test
    public void testObserverUpdateScrolledEvent() {
        JTailModel model = new JTailModel();
        JTailPanel panel = new JTailPanel(model);
        JScrollPane scrollPane = panel.scrollPane;
        JTable dataTable = panel.dataTable;
        DefaultTableModel tableModel = panel.tableModel;
        JScrollBar verticalScrollBar = panel.verticalScrollBar;
        model.addObserver(panel);
        
        // Simulate JTailModelEvent Type SCROLLED
        model.setLineNumbers("1\n2\n3\n4\n5");
        model.setContent("A\nB\nC\nD\nE");
        model.notifyObservers(new JTailModelEvent(JTailModelEvent.Type.SCROLLED, 1, 3, 5));
        
        // Check if the table model has been updated correctly
        assertEquals(3, tableModel.getRowCount());
        assertEquals("1", tableModel.getValueAt(0, 0));
        assertEquals("A", tableModel.getValueAt(0, 1));
        assertEquals("2", tableModel.getValueAt(1, 0));
        assertEquals("B", tableModel.getValueAt(1, 1));
        assertEquals("3", tableModel.getValueAt(2, 0));
        assertEquals("C", tableModel.getValueAt(2, 1));
        
        // Check if the vertical scroll bar is enabled correctly
        assertTrue(verticalScrollBar.isEnabled());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testObserverUpdateInvalidEvent() {
        JTailModel model = new JTailModel();
        JTailPanel panel = new JTailPanel(model);
        model.addObserver(panel);
        
        // Simulate invalid JTailModelEvent Type
        model.setLineNumbers("1\n2\n3\n4\n5");
        model.setContent("A\nB\nC\nD\nE");
        model.notifyObservers(new JTailModelEvent(JTailModelEvent.Type.UNKNOWN, 1, 3, 5));
    }
    
}