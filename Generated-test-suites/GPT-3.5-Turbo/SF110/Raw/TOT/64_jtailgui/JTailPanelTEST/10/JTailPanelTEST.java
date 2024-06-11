import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.Observable;

import static org.junit.Assert.*;

public class JTailPanelTest {
    
    private JTailModel model;
    private JTailPanel panel;
    private JScrollPane scrollPane;
    private JScrollBar verticalScrollBar;
    private JTable dataTable;
    private DefaultTableModel tableModel;
    
    @Before
    public void setUp() {
        // Create a JTailModel instance
        model = new JTailModel();
        
        // Create a JTailPanel instance
        panel = new JTailPanel(model);
        
        // Set up components
        scrollPane = new JScrollPane();
        verticalScrollBar = new JScrollBar();
        dataTable = new JTable();
        tableModel = new DefaultTableModel();
        
        panel.scrollPane = scrollPane;
        panel.verticalScrollBar = verticalScrollBar;
        panel.dataTable = dataTable;
        panel.tableModel = tableModel;
    }
    
    @Test
    public void testConstructor() {
        assertEquals(model, panel.model);
        assertTrue(panel.tableModel.isCellEditable(0, 0));
        assertEquals(0, panel.verticalScrollBar.getAdjustmentListeners().length);
        assertEquals(panel, panel.scrollPane.getViewport().getView());
        assertEquals(BorderLayout.CENTER, ((BorderLayout) panel.getLayout()).getConstraints(panel.scrollPane));
        assertEquals(BorderLayout.EAST, ((BorderLayout) panel.getLayout()).getConstraints(panel.verticalScrollBar));
        assertNotNull(panel.dataTable.getFontMetrics(panel.dataTable.getFont()));
        assertEquals(panel.dataTable.getFontMetrics(panel.dataTable.getFont()).getHeight() - 1, panel.characterHeight);
    }
    
    @Test
    public void testGetCharacterHeight() {
        assertEquals(panel.dataTable.getFontMetrics(panel.dataTable.getFont()).getHeight() - 1, panel.getCharacterHeight());
    }
    
    @Test
    public void testGetVerticalScrollBar() {
        assertEquals(verticalScrollBar, panel.getVerticalScrollBar());
    }
    
    @Test
    public void testUpdate_ResizedWithMinLineEqualToFirstLineAndMaxLineEqualToNbDisplayedLines() {
        // Prepare the JTailModelEvent
        JTailModelEvent event = new JTailModelEvent(JTailModelEvent.Type.RESIZED, 0, 0, 1, 1);
        
        // Call the method to be tested
        panel.update(model, event);
        
        // Assert that the vertical scroll bar is disabled
        assertFalse(panel.verticalScrollBar.isEnabled());
    }
    
    @Test
    public void testUpdate_ResizedWithMinLineNotEqualToFirstLineOrMaxLineNotEqualToNbDisplayedLines_VerticalScrollBarEnabled() {
        // Prepare the JTailModelEvent
        JTailModelEvent event = new JTailModelEvent(JTailModelEvent.Type.RESIZED, 0, 1, 0, 1);

        // Call the method to be tested
        panel.update(model, event);

        // Assert that the vertical scroll bar is enabled
        assertTrue(panel.verticalScrollBar.isEnabled());
    }
    
    @Test
    public void testUpdate_Resized() {
        // Prepare the JTailModelEvent
        JTailModelEvent event = new JTailModelEvent(JTailModelEvent.Type.RESIZED, 0, 1, 1, 2);

        // Call the method to be tested
        panel.update(model, event);

        // Assert that the table model is updated
        assertEquals(1, panel.tableModel.getRowCount());
        assertEquals("0", panel.tableModel.getValueAt(0, 0));
        assertEquals("", panel.tableModel.getValueAt(0, 1));
        assertEquals(2, panel.dataTable.getAutoResizeMode());

        // Assert that the column widths are set correctly
        TableColumn numbersColumn = panel.dataTable.getColumnModel().getColumn(0);
        TableColumn linesColumn = panel.dataTable.getColumnModel().getColumn(1);
        assertEquals(1 * panel.maxCharacterWidth, numbersColumn.getPreferredWidth());
        assertEquals(panel.dataTable.getWidth() - 1 * panel.maxCharacterWidth, linesColumn.getPreferredWidth());
    }
    
    @Test
    public void testUpdate_Scrolled() {
        // Prepare the JTailModelEvent
        JTailModelEvent event = new JTailModelEvent(JTailModelEvent.Type.SCROLLED, 0, 0, 0, 0);
        
        // Call the method to be tested
        panel.update(model, event);
        
        // Assert that the table model is updated
        assertEquals(0, panel.tableModel.getRowCount());
        assertEquals(0, panel.dataTable.getAutoResizeMode());
        
        // Assert that the column widths are set correctly
        TableColumn numbersColumn = panel.dataTable.getColumnModel().getColumn(0);
        TableColumn linesColumn = panel.dataTable.getColumnModel().getColumn(1);
        assertEquals(0, numbersColumn.getPreferredWidth());
        assertEquals(panel.dataTable.getWidth(), linesColumn.getPreferredWidth());
    }
}