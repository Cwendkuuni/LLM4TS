import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.FontMetrics;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentListener;
import java.awt.BorderLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JScrollBar;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.junit.Before;
import org.junit.Test;

public class JTailPanelTest {
    private JTailModel model;
    private JTailPanel panel;
    private DefaultTableModel tableModel;
    private JTable dataTable;
    
    @Before
    public void setUp() {
        model = new JTailModel();
        panel = new JTailPanel(model);
        tableModel = (DefaultTableModel) panel.getDataTable().getModel();
        dataTable = panel.getDataTable();
    }
    
    @Test
    public void testConstructor() {
        assertSame(model, panel.getModel());
        assertSame(dataTable, panel.getDataTable());
        assertSame(tableModel, dataTable.getModel());
        assertTrue(tableModel.isCellEditable(0, 0));
    }
    
    @Test
    public void testGetCharacterHeight() {
        FontMetrics metrics = dataTable.getFontMetrics(dataTable.getFont());
        int expectedHeight = metrics.getHeight() - 1;
        assertEquals(expectedHeight, panel.getCharacterHeight());
    }
    
    @Test
    public void testGetVerticalScrollBar() {
        JScrollBar expectedScrollBar = panel.getVerticalScrollBar();
        assertEquals(expectedScrollBar, panel.getVerticalScrollBar());
    }
    
    @Test
    public void testUpdate_ResizedEvent() {
        JTailModelEvent event = new JTailModelEvent(JTailModelEvent.Type.RESIZED, 0, 0, 0, 0);
        panel.update(model, event);
        
        JScrollBar scrollBar = panel.getVerticalScrollBar();
        BoundedRangeModel rangeModel = scrollBar.getModel();
        assertFalse(rangeModel.getValueIsAdjusting());
        assertEquals(0, rangeModel.getMinimum());
        assertEquals(0, rangeModel.getValue());
        assertEquals(0, rangeModel.getExtent());
        assertEquals(0, rangeModel.getMaximum());
        assertFalse(scrollBar.isEnabled());
        
        assertNull(tableModel.getValueAt(0, 0));
        assertNull(tableModel.getValueAt(0, 1));
        assertEquals(0, tableModel.getRowCount());
    }
    
    @Test
    public void testUpdate_ScrolledEvent() {
        JTailModelEvent event = new JTailModelEvent(JTailModelEvent.Type.SCROLLED, 0, 0, 0, 0);
        panel.update(model, event);
        
        assertNull(tableModel.getValueAt(0, 0));
        assertNull(tableModel.getValueAt(0, 1));
        assertEquals(0, tableModel.getRowCount());
    }
    
    @Test
    public void testUpdate_NullEvent() {
        panel.update(model, null);
        
        assertNull(tableModel.getValueAt(0, 0));
        assertNull(tableModel.getValueAt(0, 1));
        assertEquals(0, tableModel.getRowCount());
    }
    
    @Test
    public void testUpdateDataTable_EmptyData() {
        panel.updateDataTable("", "");
        
        assertNull(tableModel.getValueAt(0, 0));
        assertNull(tableModel.getValueAt(0, 1));
        assertEquals(0, tableModel.getRowCount());
    }
    
    @Test
    public void testUpdateDataTable_NonEmptyData() {
        String lineNumbers = "1\n2\n3\n4\n5";
        String content = "abc\ndef\nghi\njkl\nmno";
        panel.updateDataTable(lineNumbers, content);
        
        assertEquals("1", tableModel.getValueAt(0, 0));
        assertEquals("abc", tableModel.getValueAt(0, 1));
        assertEquals("2", tableModel.getValueAt(1, 0));
        assertEquals("def", tableModel.getValueAt(1, 1));
        assertEquals("3", tableModel.getValueAt(2, 0));
        assertEquals("ghi", tableModel.getValueAt(2, 1));
        assertEquals("4", tableModel.getValueAt(3, 0));
        assertEquals("jkl", tableModel.getValueAt(3, 1));
        assertEquals("5", tableModel.getValueAt(4, 0));
        assertEquals("mno", tableModel.getValueAt(4, 1));
        assertEquals(5, tableModel.getRowCount());
    }
    
    @Test
    public void testUpdateDataTable_MissingContent() {
        String lineNumbers = "1\n2\n3";
        String content = "abc\ndef";
        panel.updateDataTable(lineNumbers, content);
        
        assertEquals("1", tableModel.getValueAt(0, 0));
        assertEquals("abc", tableModel.getValueAt(0, 1));
        assertEquals("2", tableModel.getValueAt(1, 0));
        assertEquals("def", tableModel.getValueAt(1, 1));
        assertEquals("3", tableModel.getValueAt(2, 0));
        assertEquals("", tableModel.getValueAt(2, 1));
        assertEquals(3, tableModel.getRowCount());
    }
    
    @Test
    public void testUpdateDataTable_ColumnWidths() {
        panel.getDataTable().setSize(200, 200);
        
        String lineNumbers = "1\n2\n3";
        String content = "abc\ndef\nghi";
        panel.updateDataTable(lineNumbers, content);
        
        TableColumn numbersColumn = dataTable.getColumnModel().getColumn(0);
        TableColumn linesColumn = dataTable.getColumnModel().getColumn(1);
        int numbersColumnWidth = 1 * panel.getMaxCharacterWidth();
        int linesColumnWidth = panel.getDataTable().getWidth() - numbersColumnWidth;
        assertEquals(numbersColumnWidth, numbersColumn.getPreferredWidth());
        assertEquals(linesColumnWidth, linesColumn.getPreferredWidth());
    }
}