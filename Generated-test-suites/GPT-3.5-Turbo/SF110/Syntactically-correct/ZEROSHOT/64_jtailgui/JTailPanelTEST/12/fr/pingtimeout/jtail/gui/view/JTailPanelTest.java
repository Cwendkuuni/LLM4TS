import org.junit.Test;
import static org.junit.Assert.*;

public class JTailPanelTest {
    
    @Test
    public void testGetCharacterHeight() {
        JTailModel model = new JTailModel();
        JTailPanel panel = new JTailPanel(model);
        assertEquals(panel.getCharacterHeight(), 0);
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
        panel.update(model, new JTailModelEvent(JTailModelEvent.Type.RESIZED, 0, 0, 0, 0));
        panel.update(model, new JTailModelEvent(JTailModelEvent.Type.SCROLLED, 0, 0, 0, 0));
    }
    
    @Test
    public void testUpdateDataTable() {
        JTailModel model = new JTailModel();
        JTailPanel panel = new JTailPanel(model);
        panel.updateDataTable("1\n2\n3", "line 1\nline 2\nline 3");
    }
}