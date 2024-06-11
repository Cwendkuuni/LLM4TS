import static org.junit.Assert.*;
import org.junit.Test;
import fr.pingtimeout.jtail.gui.view.JTailPanel;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import javax.swing.*;
import javax.swing.table.*;

public class JTailPanelTest {
    private JTailModel createMockModel() {
        JTailModel model = new JTailModel();
        model.setContent("Line 1\nLine 2\nLine 3\nLine 4\nLine 5\n");
        model.setLineNumbers("1\n2\n3\n4\n5\n");
        return model;
    }

    private JTailPanel createJTailPanel(JTailModel model) {
        return new JTailPanel(model);
    }

    @Test
    public void testConstructor() {
        JTailModel model = createMockModel();
        JTailPanel panel = createJTailPanel(model);

        assertNotNull(panel.model);
        assertNotNull(panel.scrollPane);
        assertNotNull(panel.verticalScrollBar);
        assertNotNull(panel.dataTable);
        assertNotNull(panel.tableModel);
        assertEquals(5, panel.tableModel.getRowCount());
        assertEquals(2, panel.tableModel.getColumnCount());
        assertFalse(panel.tableModel.isCellEditable(0, 0));
        assertFalse(panel.tableModel.isCellEditable(0, 1));
        assertEquals(0, panel.scrollPane.getVerticalScrollBarPolicy());
        assertEquals(0, panel.scrollPane.getHorizontalScrollBarPolicy());
        assertNotNull(panel.verticalScrollBar.getAdjustmentListeners());
        assertNotNull(panel.getComponentListeners());
        assertEquals(1, panel.getLayout().getLayoutComponent(panel.scrollPane));
        assertEquals(1, panel.getLayout().getLayoutComponent(panel.verticalScrollBar));
        assertEquals(4, panel.characterHeight);
    }

    @Test
    public void testGetCharacterHeight() {
        JTailModel model = createMockModel();
        JTailPanel panel = createJTailPanel(model);

        assertEquals(4, panel.getCharacterHeight());
    }

    @Test
    public void testGetVerticalScrollBar() {
        JTailModel model = createMockModel();
        JTailPanel panel = createJTailPanel(model);

        assertEquals(panel.verticalScrollBar, panel.getVerticalScrollBar());
    }

    @Test
    public void testUpdate_ResizedEvent() {
        JTailModel model = createMockModel();
        JTailPanel panel = createJTailPanel(model);
        JTailModelEvent event = new JTailModelEvent(JTailModelEvent.Type.RESIZED, 1, 0, 5, 5);

        panel.update(model, event);

        assertEquals(1, panel.verticalScrollBar.getModel().getMinimum());
        assertEquals(0, panel.verticalScrollBar.getModel().getValue());
        assertEquals(5, panel.verticalScrollBar.getModel().getExtent());
        assertEquals(5, panel.verticalScrollBar.getModel().getMaximum());
        assertFalse(panel.verticalScrollBar.getModel().getValueIsAdjusting());
        assertFalse(panel.verticalScrollBar.isEnabled());
        assertEquals("1", panel.tableModel.getValueAt(0, 0));
        assertEquals("Line 1", panel.tableModel.getValueAt(0, 1));
    }

    @Test
    public void testUpdate_ScrolledEvent() {
        JTailModel model = createMockModel();
        JTailPanel panel = createJTailPanel(model);
        JTailModelEvent event = new JTailModelEvent(JTailModelEvent.Type.SCROLLED, 0, 0, 5, 5);

        panel.update(model, event);

        assertEquals("1", panel.tableModel.getValueAt(0, 0));
        assertEquals("Line 1", panel.tableModel.getValueAt(0, 1));
    }

    @Test
    public void testUpdate_InvalidEvent() {
        JTailModel model = createMockModel();
        JTailPanel panel = createJTailPanel(model);
        JTailModelEvent event = new JTailModelEvent(JTailModelEvent.Type.INVALID, 0, 0, 5, 5);

        panel.update(model, event);

        assertEquals(5, panel.tableModel.getRowCount());
    }
}