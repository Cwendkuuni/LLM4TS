import static org.junit.Assert.*;
import org.junit.Test;

public class JTailPanelTest {

    @Test
    public void testJTailPanel() {
        JTailModel model = new JTailModel();
        JTailPanel panel = new JTailPanel(model);

        assertNotNull(panel.model);
        assertNotNull(panel.scrollPane);
        assertNotNull(panel.verticalScrollBar);
        assertNotNull(panel.dataTable);
        assertNotNull(panel.tableModel);
        assertEquals(0, panel.characterHeight);
        assertEquals(0, panel.maxCharacterWidth);
    }

    @Test
    public void testGetCharacterHeight() {
        JTailModel model = new JTailModel();
        JTailPanel panel = new JTailPanel(model);
        
        assertEquals(panel.dataTable.getFontMetrics(panel.dataTable.getFont()).getHeight() - 1, panel.getCharacterHeight());
    }

    @Test
    public void testGetVerticalScrollBar() {
        JTailModel model = new JTailModel();
        JTailPanel panel = new JTailPanel(model);
        
        assertEquals(panel.verticalScrollBar, panel.getVerticalScrollBar());
    }

    @Test
    public void testUpdate() {
        JTailModel model = new JTailModel();
        JTailPanel panel = new JTailPanel(model);
        Observable observable = new Observable();
        Object arg = new Object();

        // Simulate event type RESIZED
        JTailModelEvent eventResized = new JTailModelEvent(JTailModelEvent.Type.RESIZED, 0, 0, 0, 0);
        panel.update(observable, eventResized);
        
        // Simulate event type SCROLLED
        JTailModelEvent eventScrolled = new JTailModelEvent(JTailModelEvent.Type.SCROLLED, 0, 0, 0, 0);
        panel.update(observable, eventScrolled);
    }

    @Test
    public void testUpdateDataTable() {
        JTailModel model = new JTailModel();
        JTailPanel panel = new JTailPanel(model);
        String lineNumbers = "1\n2\n3";
        String lines = "a\nb\nc";

        panel.updateDataTable(lineNumbers, lines);

        // Check if table model is updated correctly
        assertEquals(lineNumbers.split("\n").length, panel.tableModel.getRowCount());
        assertEquals(lineNumbers.split("\n")[0], panel.tableModel.getValueAt(0, 0));
        assertEquals(lines.split("\n")[0], panel.tableModel.getValueAt(0, 1));

        // Check if column widths are set correctly
        // TODO: Add assertions for column widths
    }
}