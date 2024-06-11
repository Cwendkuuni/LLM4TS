import org.junit.Test;
import static org.junit.Assert.*;
import fr.pingtimeout.jtail.gui.view.JTailPanel;
import fr.pingtimeout.jtail.gui.model.JTailModel;

public class JTailPanelTest {
    
    @Test
    public void testGetCharacterHeight() {
        JTailModel model = new JTailModel();
        JTailPanel panel = new JTailPanel(model);
        assertEquals(panel.getCharacterHeight(), panel.dataTable.getFontMetrics(panel.dataTable.getFont()).getHeight() - 1);
    }
    
    @Test
    public void testUpdateDataTable() {
        JTailModel model = new JTailModel();
        JTailPanel panel = new JTailPanel(model);
        panel.updateDataTable("1\n2\n3", "Line 1\nLine 2\nLine 3");
        assertEquals(panel.tableModel.getRowCount(), 3);
    }
    
    // Additional test cases for edge cases and exceptions can be added here
    
}