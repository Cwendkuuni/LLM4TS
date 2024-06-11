import static org.junit.Assert.*;
import org.junit.*;
import fr.pingtimeout.jtail.util.*;
import fr.pingtimeout.jtail.gui.controller.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import fr.pingtimeout.jtail.gui.model.*;
import javax.swing.*;
import javax.swing.table.*;

public class JTailPanelTest {

    @Test
    public void testGetCharacterHeight() {
        JTailModel model = new JTailModel();
        JTailPanel panel = new JTailPanel(model);
        assertEquals(15, panel.getCharacterHeight());
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

        model.setContent("1\n2\n3");
        model.setLineNumbers("1\n2\n3");
        model.notifyObservers(new JTailModelEvent(JTailModelEvent.Type.RESIZED, 1, 1, 3, 3));

        DefaultTableModel tableModel = (DefaultTableModel) panel.dataTable.getModel();
        assertEquals(3, tableModel.getRowCount());
        assertEquals("1", tableModel.getValueAt(0, 0));
        assertEquals("2", tableModel.getValueAt(1, 0));
        assertEquals("3", tableModel.getValueAt(2, 0));
        assertEquals("", tableModel.getValueAt(0, 1));
        assertEquals("", tableModel.getValueAt(1, 1));
        assertEquals("", tableModel.getValueAt(2, 1));
    }

    @Test
    public void testUpdateDataTable() {
        JTailModel model = new JTailModel();
        JTailPanel panel = new JTailPanel(model);

        String lineNumbers = "1\n2\n3";
        String lines = "Line 1\nLine 2\nLine 3";
        panel.updateDataTable(lineNumbers, lines);

        DefaultTableModel tableModel = (DefaultTableModel) panel.dataTable.getModel();
        assertEquals(3, tableModel.getRowCount());
        assertEquals("1", tableModel.getValueAt(0, 0));
        assertEquals("2", tableModel.getValueAt(1, 0));
        assertEquals("3", tableModel.getValueAt(2, 0));
        assertEquals("Line 1", tableModel.getValueAt(0, 1));
        assertEquals("Line 2", tableModel.getValueAt(1, 1));
        assertEquals("Line 3", tableModel.getValueAt(2, 1));
    }
}