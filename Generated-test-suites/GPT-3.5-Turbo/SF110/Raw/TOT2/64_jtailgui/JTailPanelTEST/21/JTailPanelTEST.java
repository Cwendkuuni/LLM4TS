import static org.junit.Assert.*;
import org.junit.Test;

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
        JTailModel model = new JTailModel();
        JTailPanel panel = new JTailPanel(model);
        model.setLineNumbers("1\n2\n3");
        model.setContent("A\nB\nC");
        panel.update(model, new JTailModelEvent(JTailModelEvent.Type.RESIZED, 1, 1, 3, 3, "", ""));
        model.setLineNumbers("1\n2\n3");
        model.setContent("A\nB");
        panel.update(model, new JTailModelEvent(JTailModelEvent.Type.SCROLLED, 1, 1, 3, 3, "", ""));
    }

    @Test
    public void testUpdateDataTable() {
        JTailModel model = new JTailModel();
        JTailPanel panel = new JTailPanel(model);
        panel.updateDataTable("1\n2\n3", "A\nB\nC");
    }
}