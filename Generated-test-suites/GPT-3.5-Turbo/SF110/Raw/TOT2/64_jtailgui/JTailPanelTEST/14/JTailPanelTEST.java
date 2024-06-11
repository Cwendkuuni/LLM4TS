
import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.*;
import java.util.Observable;

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
    public void testUpdate() {
        Observable observable = model;
        Object arg = new JTailModelEvent(JTailModelEvent.Type.RESIZED, 0, 10, 5, 20);
        panel.update(observable, arg);

        assertTrue(panel.verticalScrollBar.isEnabled());
    }

    @Test
    public void testUpdateDataTable() {
        String line = "1\n2\n3\n4\n5";
        String content = "A\nB\nC\nD\nE";
        panel.updateDataTable(line, content);

        assertEquals(panel.tableModel.getRowCount(), 5);
        assertEquals(panel.tableModel.getValueAt(0, 0), "1");
        assertEquals(panel.tableModel.getValueAt(0, 1), "A");
    }

    @Test(expected = NullPointerException.class)
    public void testUpdateWithNullArgument() {
        panel.update(null, null);
    }
}
