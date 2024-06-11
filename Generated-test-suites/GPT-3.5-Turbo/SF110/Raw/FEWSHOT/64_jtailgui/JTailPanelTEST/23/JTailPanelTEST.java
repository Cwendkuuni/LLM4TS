import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import fr.pingtimeout.jtail.gui.model.JTailModel;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

public class JTailPanelTest {
    
    private JTailModel mockModel;
    private JScrollBar mockScrollBar;
    private JTable mockDataTable;
    private DefaultTableModel mockTableModel;
    private JScrollPane mockScrollPane;
    private JTailPanel jTailPanel;
    
    @Before
    public void setUp() {
        mockModel = mock(JTailModel.class);
        mockScrollBar = mock(JScrollBar.class);
        mockDataTable = mock(JTable.class);
        mockTableModel = mock(DefaultTableModel.class);
        mockScrollPane = mock(JScrollPane.class);
        
        jTailPanel = new JTailPanel(mockModel);
        jTailPanel.verticalScrollBar = mockScrollBar;
        jTailPanel.dataTable = mockDataTable;
        jTailPanel.tableModel = mockTableModel;
        jTailPanel.scrollPane = mockScrollPane;
    }
    
    @Test
    public void testConstructor() {
        assertEquals(mockModel, jTailPanel.model);
        verify(mockModel).addObserver(jTailPanel);
        verify(mockDataTable).setModel(mockTableModel);
        verify(mockScrollPane).setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        verify(mockScrollPane).setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }
    
    @Test
    public void testGetCharacterHeight() {
        jTailPanel.characterHeight = 10;
        int result = jTailPanel.getCharacterHeight();
        assertEquals(10, result);
    }
    
    @Test
    public void testGetVerticalScrollBar() {
        JScrollBar result = jTailPanel.getVerticalScrollBar();
        assertEquals(mockScrollBar, result);
    }
    
    @Test
    public void testUpdate_Resized() {
        JTailModelEvent mockEvent = mock(JTailModelEvent.class);
        when(mockEvent.type).thenReturn(JTailModelEvent.Type.RESIZED);
        when(mockEvent.minLine).thenReturn(1);
        when(mockEvent.firstLine).thenReturn(2);
        when(mockEvent.nbDisplayedLines).thenReturn(3);
        when(mockEvent.maxLine).thenReturn(4);
        
        BoundedRangeModel mockRangeModel = mock(BoundedRangeModel.class);
        when(mockScrollBar.getModel()).thenReturn(mockRangeModel);
        
        jTailPanel.update(mockModel, mockEvent);
        
        verify(mockRangeModel).setValueIsAdjusting(true);
        verify(mockRangeModel).setMinimum(1);
        verify(mockRangeModel).setValue(2);
        verify(mockRangeModel).setExtent(3);
        verify(mockRangeModel).setMaximum(4);
        verify(mockRangeModel).setValueIsAdjusting(false);
        
        verify(mockScrollBar).setEnabled(true);
        verify(mockDataTable).setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        verify(mockDataTable.getColumn(0)).setPreferredWidth(0);
        verify(mockDataTable.getColumn(1)).setPreferredWidth(0);
    }
    
    @Test
    public void testUpdate_Scrolled() {
        JTailModelEvent mockEvent = mock(JTailModelEvent.class);
        when(mockEvent.type).thenReturn(JTailModelEvent.Type.SCROLLED);
        
        jTailPanel.update(mockModel, mockEvent);
        
        verifyZeroInteractions(mockScrollBar);
        verify(mockDataTable).setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        verify(mockDataTable.getColumn(0)).setPreferredWidth(0);
        verify(mockDataTable.getColumn(1)).setPreferredWidth(0);
    }
    
    @Test
    public void testUpdate_NonModelEvent() {
        Object mockEvent = mock(Object.class);
        
        jTailPanel.update(mockModel, mockEvent);
        
        verifyZeroInteractions(mockScrollBar);
        verifyZeroInteractions(mockDataTable);
    }
}