import org.junit.Test;
import static org.junit.Assert.*;

public class JTailPanelTest {

    // Test JTailPanel constructor
    @Test
    public void testConstructor(){
        JTailModel model = new JTailModel();
        JTailPanel panel = new JTailPanel(model);
        assertNotNull(panel);
        assertNull(panel.getLayout());
        assertNotNull(panel.getVerticalScrollBar());
        assertNotNull(panel.dataTable);
        assertEquals(0, panel.tableModel.getRowCount());
    }

    // Test JTailPanel update method
    @Test
    public void testUpdate(){
        JTailModel model = new JTailModel();
        JTailPanel panel = new JTailPanel(model);
        model.notifyObservers(new JTailModelEvent(JTailModelEvent.Type.RESIZED, 1, 10, 5, 20));
        assertEquals(5, panel.tableModel.getRowCount());
        
        model.notifyObservers(new JTailModelEvent(JTailModelEvent.Type.SCROLLED));
        assertEquals(5, panel.tableModel.getRowCount());

        panel.update(null, new JTailModelEvent(JTailModelEvent.Type.RESIZED, 1, 10, 5, 15));
        assertEquals(5, panel.tableModel.getRowCount());

        panel.update(null, new JTailModelEvent(JTailModelEvent.Type.SCROLLED));
        assertEquals(5, panel.tableModel.getRowCount());
    }

    // Test JTailPanel updateDataTable method
    @Test
    public void testUpdateDataTable(){
        JTailModel model = new JTailModel();
        JTailPanel panel = new JTailPanel(model);
        model.setContent("Line 1\nLine 2\nLine 3");
        model.setLineNumbers("1\n2\n3");
        panel.updateDataTable(model.getLineNumbers(), model.getContent());
        assertEquals(3, panel.tableModel.getRowCount());

        model.setContent("Line 1");
        model.setLineNumbers("1");
        panel.updateDataTable(model.getLineNumbers(), model.getContent());
        assertEquals(1, panel.tableModel.getRowCount());

        model.setContent("");
        model.setLineNumbers("");
        panel.updateDataTable(model.getLineNumbers(), model.getContent());
        assertEquals(0, panel.tableModel.getRowCount());
    }
}