import org.junit.*;
import static org.junit.Assert.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

public class JTailPanelTest {
    
    @Test
    public void testGetCharacterHeight() {
        JTailModel model = new JTailModel();
        JTailPanel panel = new JTailPanel(model);
        assertEquals(panel.getCharacterHeight(), panel.dataTable.getFontMetrics(panel.dataTable.getFont()).getHeight() - 1);
    }
    
    @Test
    public void testGetVerticalScrollBar() {
        JTailModel model = new JTailModel();
        JTailPanel panel = new JTailPanel(model);
        assertNotNull(panel.getVerticalScrollBar());
    }
    
    @Test
    public void testUpdate() {
        // Need to mock JTailModelEvent and Observable for testing update method
    }
    
    @Test
    public void testUpdateDataTable() {
        JTailModel model = new JTailModel();
        JTailPanel panel = new JTailPanel(model);
        String lineNumbers = "1\n2\n3";
        String lines = "line 1\nline 2\nline 3";
        panel.updateDataTable(lineNumbers, lines);
        assertEquals(panel.tableModel.getRowCount(), 3);
    }
}