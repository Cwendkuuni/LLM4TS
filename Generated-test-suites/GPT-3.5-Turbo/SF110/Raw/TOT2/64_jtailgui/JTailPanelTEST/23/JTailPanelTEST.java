import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class JTailPanelTest {
    
    private JTailModel model;
    private JTailPanel panel;
    
    @Before
    public void setUp() {
        model = new JTailModel();
        panel = new JTailPanel(model);
    }
    
    @Test
    public void testGetCharacterHeight() {
        assertEquals(panel.getCharacterHeight(), panel.dataTable.getFontMetrics(panel.dataTable.getFont()).getHeight() - 1);
    }
    
    @Test
    public void testGetVerticalScrollBar() {
        assertNotNull(panel.getVerticalScrollBar());
    }
    
    @Test
    public void testUpdate_Resize() {
        model.setJTailModelEvent(new JTailModelEvent(JTailModelEvent.Type.RESIZED, 0, 10, 5, 20));
        panel.update(model, model.getJTailModelEvent());
        assertEquals(panel.verticalScrollBar.getValue(), 10);
    }
    
    @Test
    public void testUpdate_Scroll() {
        model.setJTailModelEvent(new JTailModelEvent(JTailModelEvent.Type.SCROLLED, 0, 0, 0, 0));
        panel.update(model, model.getJTailModelEvent());
        assertEquals(panel.tableModel.getRowCount(), 0);
    }
    
    @Test
    public void testUpdateDataTable() {
        panel.updateDataTable("1\n2\n3\n", "A\nB\nC\n");
        assertEquals(panel.tableModel.getRowCount(), 3);
    }
}