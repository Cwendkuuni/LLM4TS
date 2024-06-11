package fr.pingtimeout.jtail.gui.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.FontMetrics;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentListener;
import java.awt.BorderLayout;

import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JScrollBar;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.junit.Before;
import org.junit.Test;

import fr.pingtimeout.jtail.gui.controller.JTailSizeController;
import fr.pingtimeout.jtail.gui.controller.ScrollBarAdjustmentController;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.model.JTailModelEvent;
import fr.pingtimeout.jtail.util.JTailLogger;

public class JTailPanelTest {
    private JTailModel model;
    private JTailPanel jtailPanel;
    
    @Before
    public void setUp() {
        model = new JTailModel();
        jtailPanel = new JTailPanel(model);
    }
    
    @Test
    public void testJTailPanel() {
        assertNotNull(jtailPanel.model);
        assertNotNull(jtailPanel.scrollPane);
        assertNotNull(jtailPanel.verticalScrollBar);
        assertNotNull(jtailPanel.dataTable);
        assertNotNull(jtailPanel.tableModel);
        assertNotNull(jtailPanel.addComponentListener());
        assertEquals(BorderLayout.class, jtailPanel.getLayout().getClass());
    }
    
    @Test
    public void testGetCharacterHeight() {
        FontMetrics metrics = jtailPanel.dataTable.getFontMetrics(jtailPanel.dataTable.getFont());
        int expectedHeight = metrics.getHeight() - 1;
        assertEquals(expectedHeight, jtailPanel.getCharacterHeight());
    }
    
    @Test
    public void testGetVerticalScrollBar() {
        assertEquals(JScrollBar.class, jtailPanel.getVerticalScrollBar().getClass());
    }
    
    @Test
    public void testUpdate() {
        Observable observable = new Observable();
        Object arg = new JTailModelEvent();
        jtailPanel.update(observable, arg);
        // add more assertions based on the logic in the update method
    }
    
    @Test
    public void testUpdateDataTable() {
        String lineNumbers = "1\n2\n3";
        String lines = "one\ntwo\nthree";
        jtailPanel.updateDataTable(lineNumbers, lines);
        // add assertions to check if the data table has been updated correctly
    }
    
    @Test
    public void testRemoveAllRows() {
        DefaultTableModel defaultTableModel = new DefaultTableModel(new String[] { "#", "" }, 0);
        defaultTableModel.addRow(new String[] { "1", "one" });
        defaultTableModel.addRow(new String[] { "2", "two" });
        jtailPanel.removeAllRows(defaultTableModel);
        assertEquals(0, defaultTableModel.getRowCount());
    }
    
    @Test
    public void testSetColumnWidths() {
        String[] lineNumbersArray = { "1", "2", "3" };
        TableColumn numbersColumn = new TableColumn();
        TableColumn linesColumn = new TableColumn();
        jtailPanel.setColumnWidths(numbersColumn, linesColumn, lineNumbersArray);
        // add assertions to check if the column widths have been set correctly
    }
}