import static org.junit.Assert.*;
import org.junit.Test;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

public class JTailPanelTest {
    @Test
    public void testJTailPanel() {
        JTailModel model = new JTailModel();
        JTailPanel panel = new JTailPanel(model);

        // Test that the model is initialized correctly
        assertSame(model, panel.model);
        assertNotNull(panel.scrollPane);
        assertNotNull(panel.verticalScrollBar);
        assertNotNull(panel.dataTable);
        assertNotNull(panel.tableModel);
        assertEquals(0, panel.tableModel.getColumnCount());
        assertEquals(0, panel.tableModel.getRowCount());
        assertEquals(0, panel.characterHeight);
        assertEquals(0, panel.maxCharacterWidth);

        // Test that the layout is set correctly
        assertTrue(panel.getLayout() instanceof BorderLayout);
        assertEquals(panel.scrollPane, panel.getComponent(0));
        assertEquals(panel.verticalScrollBar, panel.getComponent(1));
    }

    @Test
    public void testGetCharacterHeight() {
        JTailModel model = new JTailModel();
        JTailPanel panel = new JTailPanel(model);

        // Test that the character height is returned correctly
        assertEquals(0, panel.getCharacterHeight());
        panel.characterHeight = 10;
        assertEquals(10, panel.getCharacterHeight());
    }

    @Test
    public void testGetVerticalScrollBar() {
        JTailModel model = new JTailModel();
        JTailPanel panel = new JTailPanel(model);

        // Test that the vertical scroll bar is returned correctly
        assertSame(panel.verticalScrollBar, panel.getVerticalScrollBar());
    }

    @Test
    public void testUpdate() {
        JTailModel model = new JTailModel();
        JTailPanel panel = new JTailPanel(model);
        JTailModelEvent event = new JTailModelEvent(JTailModelEvent.Type.RESIZED);
        event.minLine = 0;
        event.firstLine = 0;
        event.nbDisplayedLines = 10;
        event.maxLine = 20;

        // Test that the table model is updated correctly for a resize event
        panel.update(model, event);
        assertEquals(10, panel.tableModel.getRowCount());
        assertEquals("0", panel.tableModel.getValueAt(0, 0));
        assertEquals("", panel.tableModel.getValueAt(0, 1));
        assertEquals("9", panel.tableModel.getValueAt(9, 0));
        assertEquals("", panel.tableModel.getValueAt(9, 1));

        // Test that the table model is updated correctly for a scroll event
        event = new JTailModelEvent(JTailModelEvent.Type.SCROLLED);
        model.setLineNumbers("0\n1\n2\n3\n4\n5\n6\n7\n8\n9");
        model.setContent("a\nb\nc\nd\ne\nf\ng\nh\ni\nj");
        panel.update(model, event);
        assertEquals(10, panel.tableModel.getRowCount());
        assertEquals("0", panel.tableModel.getValueAt(0, 0));
        assertEquals("a", panel.tableModel.getValueAt(0, 1));
        assertEquals("9", panel.tableModel.getValueAt(9, 0));
        assertEquals("", panel.tableModel.getValueAt(9, 1));
    }

    @Test
    public void testUpdateDataTable() {
        JTailModel model = new JTailModel();
        JTailPanel panel = new JTailPanel(model);

        // Test that the data table is updated correctly
        panel.updateDataTable("0\n1\n2\n3\n4", "a\nb\nc\nd\ne");
        assertEquals(5, panel.tableModel.getRowCount());
        assertEquals("0", panel.tableModel.getValueAt(0, 0));
        assertEquals("a", panel.tableModel.getValueAt(0, 1));
        assertEquals("4", panel.tableModel.getValueAt(4, 0));
        assertEquals("", panel.tableModel.getValueAt(4, 1));

        // Test that the column widths are set correctly
        TableColumn numbersColumn = panel.dataTable.getColumnModel().getColumn(0);
        TableColumn linesColumn = panel.dataTable.getColumnModel().getColumn(1);
        assertEquals(3, numbersColumn.getPreferredWidth());
        assertEquals(0, linesColumn.getPreferredWidth());
    }
}