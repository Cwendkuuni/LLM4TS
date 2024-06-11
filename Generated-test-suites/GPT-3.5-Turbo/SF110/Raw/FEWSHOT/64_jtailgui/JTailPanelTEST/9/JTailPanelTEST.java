
package fr.pingtimeout.jtail.gui.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.FontMetrics;
import java.awt.event.AdjustmentEvent;
import java.awt.event.ComponentEvent;
import java.util.Observer;

import javax.swing.JScrollPane;
import javax.swing.JScrollBar;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import org.junit.Before;
import org.junit.Test;

import fr.pingtimeout.jtail.gui.controller.JTailSizeController;
import fr.pingtimeout.jtail.gui.controller.ScrollBarAdjustmentController;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.model.JTailModelEvent;
import fr.pingtimeout.jtail.util.JTailLogger;

public class JTailPanelTest {
    
    private JTailModel model;
    private JTailPanel jTailPanel;
    
    @Before
    public void setup() {
        model = new JTailModel();
        jTailPanel = new JTailPanel(model);
    }
    
    @Test
    public void testConstructor() {
        assertNotNull(jTailPanel.model);
        assertNotNull(jTailPanel.scrollPane);
        assertNotNull(jTailPanel.verticalScrollBar);
        assertNotNull(jTailPanel.dataTable);
        assertNotNull(jTailPanel.tableModel);
        assertNotNull(jTailPanel.tableModel.isCellEditable(0, 0));
        assertTrue(jTailPanel.scrollPane.getVerticalScrollBarPolicy() == JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        assertTrue(jTailPanel.scrollPane.getHorizontalScrollBarPolicy() == JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }
    
    @Test
    public void testGetCharacterHeight() {
        FontMetrics fontMetrics = jTailPanel.dataTable.getFontMetrics(jTailPanel.dataTable.getFont());
        assertEquals(fontMetrics.getHeight() - 1, jTailPanel.getCharacterHeight());
    }
    
    @Test
    public void testGetVerticalScrollBar() {
        assertEquals(jTailPanel.verticalScrollBar, jTailPanel.getVerticalScrollBar());
    }
    
    @Test
    public void testUpdate_ResizeEvent() {
        JTailModelEvent event = new JTailModelEvent(JTailModelEvent.Type.RESIZED, 1, 10, 1, 10);
        jTailPanel.update(model, event);
        
        JScrollBar verticalScrollBar = jTailPanel.verticalScrollBar;
        assertEquals(1, verticalScrollBar.getMinimum());
        assertEquals(1, verticalScrollBar.getValue());
        assertEquals(10, verticalScrollBar.getExtent());
        assertEquals(10, verticalScrollBar.getMaximum());
        assertFalse(verticalScrollBar.getValueIsAdjusting());
        assertFalse(verticalScrollBar.isEnabled());
        
        tableModel = jTailPanel.tableModel;
        tableModel.addRow(new String[] {"1", "Line 1"});
        
        JTable dataTable = jTailPanel.dataTable;
        tableModel = (DefaultTableModel) dataTable.getModel();
        
        assertTrue(dataTable.getAutoResizeMode() == JTable.AUTO_RESIZE_ALL_COLUMNS);
        TableColumn numbersColumn = dataTable.getColumnModel().getColumn(0);
        TableColumn linesColumn = dataTable.getColumnModel().getColumn(1);
        assertTrue(numbersColumn.getPreferredWidth() > 0);
        assertTrue(linesColumn.getPreferredWidth() > 0);
    }
    
    @Test
    public void testUpdate_ScrollEvent() {
        JTailModelEvent event = new JTailModelEvent(JTailModelEvent.Type.SCROLLED, 1, 10, 1, 10);
        jTailPanel.update(model, event);
        
        JScrollBar verticalScrollBar = jTailPanel.verticalScrollBar;
        assertNull(verticalScrollBar);
        
        DefaultTableModel tableModel = jTailPanel.tableModel;
        assertNotNull(tableModel);
        
        JTable dataTable = jTailPanel.dataTable;
        assertNotNull(dataTable);
        
        assertTrue(dataTable.getAutoResizeMode() == JTable.AUTO_RESIZE_ALL_COLUMNS);
        TableColumn numbersColumn = dataTable.getColumnModel().getColumn(0);
        TableColumn linesColumn = dataTable.getColumnModel().getColumn(1);
        assertNotNull(numbersColumn);
        assertNotNull(linesColumn);
    }
    
}
