
package fr.pingtimeout.jtail.gui.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.FontMetrics;

import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

public class JTailPanelTest {
    @Mock
    JTailModel modelMock;

    @InjectMocks
    JTailPanel jtailPanel;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testConstructor() {
        JTailPanel panel = new JTailPanel(modelMock);
        verify(modelMock).addObserver(panel);
    }

    @Test
    public void testGetCharacterHeight() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        FontMetrics fontMetricsMock = mock(FontMetrics.class);
        JTable dataTableMock = mock(JTable.class);
        when(dataTableMock.getFontMetrics(dataTableMock.getFont())).thenReturn(fontMetricsMock);
        when(fontMetricsMock.getHeight()).thenReturn(10);
        jtailPanel.dataTable = dataTableMock;

        assertEquals(9, jtailPanel.getCharacterHeight());
        verify(dataTableMock).getFontMetrics(dataTableMock.getFont());
        
        System.setOut(System.out);
    }

    @Test
    public void testGetVerticalScrollBar() {
        JScrollBar bar = jtailPanel.getVerticalScrollBar();
        assertEquals(jtailPanel.verticalScrollBar, bar);
    }

    @Test
    public void testUpdate_Resized() {
        BoundedRangeModel rangeModelMock = mock(BoundedRangeModel.class);
        JTailModelEvent eventMock = mock(JTailModelEvent.class);
        when(eventMock.getMinLine()).thenReturn(1);
        when(eventMock.getFirstLine()).thenReturn(1);
        when(eventMock.getNbDisplayedLines()).thenReturn(5);
        when(eventMock.getMaxLine()).thenReturn(10);
        when(jtailPanel.verticalScrollBar.getModel()).thenReturn(rangeModelMock);
        doReturn(true).when(rangeModelMock).getValueIsAdjusting();

        jtailPanel.update(modelMock, eventMock);
        
        verify(rangeModelMock).setValueIsAdjusting(true);
        verify(rangeModelMock).setMinimum(1);
        verify(rangeModelMock).setValue(1);
        verify(rangeModelMock).setExtent(5);
        verify(rangeModelMock).setMaximum(10);
        verify(rangeModelMock).setValueIsAdjusting(false);
        verify(jtailPanel.verticalScrollBar).setEnabled(true);
        
        verify(jtailPanel).updateDataTable(anyString(), anyString());
    }

    @Test
    public void testUpdate_Scrolled() {
        JTailModelEvent eventMock = mock(JTailModelEvent.class);
        jtailPanel.update(modelMock, eventMock);
        
        verify(jtailPanel).updateDataTable(anyString(), anyString());
    }

    @Test
    public void testUpdate_OtherEvents() {
        JTailModelEvent eventMock = mock(JTailModelEvent.class);
        when(eventMock.getType()).thenReturn(JTailModelEvent.Type.OTHER);
        jtailPanel.update(modelMock, eventMock);
        
        verifyNoInteractions(jtailPanel, eventMock);
    }

    private class ScrollBarAdjustmentControllerTest implements AdjustmentListener {
        public void adjustmentValueChanged(AdjustmentEvent e) {
            // Test code here
        }
    }

    private class JTailSizeControllerTest extends ComponentAdapter {
        public void componentResized(ComponentEvent e) {
            // Test code here
        }
    }
}
