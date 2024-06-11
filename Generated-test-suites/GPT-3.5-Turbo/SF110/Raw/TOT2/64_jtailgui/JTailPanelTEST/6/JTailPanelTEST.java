import static org.junit.Assert.*;
import org.junit.Test;
import fr.pingtimeout.jtail.gui.view.JTailPanel;
import fr.pingtimeout.jtail.gui.model.JTailModel;

public class JTailPanelTest {
    
    @Test
    public void testGetCharacterHeight() {
        JTailModel model = new JTailModel();
        JTailPanel panel = new JTailPanel(model);
        
        assertEquals(panel.getCharacterHeight(), 1); // Assuming default font size
    }
    
    @Test
    public void testUpdateDataTable() {
        JTailModel model = new JTailModel();
        JTailPanel panel = new JTailPanel(model);
        
        // Test with empty data
        panel.updateDataTable("", "");
        
        assertNull(panel.dataTable.getValueAt(0, 0));
        assertNull(panel.dataTable.getValueAt(0, 1));
        
        // Test with non-empty data
        panel.updateDataTable("1\n2\n3", "a\nb\nc");
        
        assertEquals(panel.dataTable.getValueAt(0, 0), "1");
        assertEquals(panel.dataTable.getValueAt(1, 1), "b");
    }
    
    // Add more test cases for other methods if needed

}