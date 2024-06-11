
package fr.pingtimeout.jtail.gui.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.event.AdjustmentEvent;
import java.util.Observable;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.model.JTailModelEvent;

public class JTailPanelTest {
    
    private JTailModel mockModel;
    private JTailPanel jTailPanel;
    
    @Before
    public void setUp() {
        mockModel = new JTailModel();
        jTailPanel = new JTailPanel(mockModel);
    }
    
    @Test
    public void testGetCharacterHeight() {
        assertEquals(jTailPanel.getCharacterHeight(), jTailPanel.dataTable.getFontMetrics(jTailPanel.dataTable.getFont()).getHeight() - 1);
    }
    
    @Test
    public void testGetVerticalScrollBar() {
        assertEquals(jTailPanel.getVerticalScrollBar(), jTailPanel.verticalScrollBar);
    }
    
    @Test
    public void testUpdate_ResizedEvent() {
        JTailModelEvent mockEvent = new JTailModelEvent(JTailModelEvent.Type.RESIZED, 0, 10, 0, 10);
        jTailPanel.update(mockModel, mockEvent);
        
        assertFalse(jTailPanel.verticalScrollBar.getValueIsAdjusting());
        assertEquals(jTailPanel.verticalScrollBar.getMinimum(), mockEvent.getMinLine());
        assertEquals(jTailPanel.verticalScrollBar.getValue(), mockEvent.getFirstLine());
        assertEquals(jTailPanel.verticalScrollBar.getExtent(), mockEvent.getNbDisplayedLines());
        assertEquals(jTailPanel.verticalScrollBar.getMaximum(), mockEvent.getMaxLine());
        assertTrue(jTailPanel.verticalScrollBar.isEnabled());
    }
    
    @Test
    public void testUpdate_ScrolledEvent() {
        JTailModelEvent mockEvent = new JTailModelEvent(JTailModelEvent.Type.SCROLLED, 0, 0, 0, 0);
        jTailPanel.update(mockModel, mockEvent);
        
        assertTrue(jTailPanel.tableModel.getRowCount() == 0);
        
        String[] lineNumbersArray = new String[1];
        lineNumbersArray[0] = "1";
        
        String[] linesArray = new String[1];
        linesArray[0] = "Test Line";
        
        mockModel.setLineNumbers("1");
        mockModel.setContent("Test Line");
        
        jTailPanel.update(mockModel, mockEvent);
        
        assertEquals(jTailPanel.tableModel.getRowCount(), 1);
        assertEquals(jTailPanel.tableModel.getValueAt(0, 0), lineNumbersArray[0]);
        assertEquals(jTailPanel.tableModel.getValueAt(0, 1), linesArray[0]);
    }
    
}
