import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JTailPanelTest {
    
    JTailModel model;
    JTailPanel panel;
    
    @Before
    public void setUp() {
        model = new JTailModel();
        panel = new JTailPanel(model);
    }
    
    @Test
    public void testGetCharacterHeight() {
        assertEquals(panel.getCharacterHeight(), panel.characterHeight);
    }
    
    @Test
    public void testGetVerticalScrollBar() {
        assertEquals(panel.getVerticalScrollBar(), panel.verticalScrollBar);
    }
    
    @Test
    public void testUpdateDataTable() {
        String lineNumbers = "1\n2\n3";
        String lines = "A\nB\nC";
        
        panel.updateDataTable(lineNumbers, lines);
        
        DefaultTableModel tableModel = (DefaultTableModel) panel.dataTable.getModel();
        
        assertEquals(tableModel.getValueAt(0, 0), "1");
        assertEquals(tableModel.getValueAt(0, 1), "A");
        
        assertEquals(tableModel.getValueAt(1, 0), "2");
        assertEquals(tableModel.getValueAt(1, 1), "B");
        
        assertEquals(tableModel.getValueAt(2, 0), "3");
        assertEquals(tableModel.getValueAt(2, 1), "C");
    }
    
    // Add more test methods for other methods in JTailPanel class
    
}