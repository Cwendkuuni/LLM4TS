
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class JTailPanelTest {
    private JTailModel model;
    private JTailPanel jTailPanel;

    @Before
    public void setup() {
        model = mock(JTailModel.class);
        jTailPanel = new JTailPanel(model);
    }

    @Test
    public void testGetCharacterHeight() {
        assertEquals(5, jTailPanel.getCharacterHeight());
    }

    @Test
    public void testGetVerticalScrollBar() {
        assertEquals(jTailPanel.getVerticalScrollBar(), jTailPanel.verticalScrollBar);
    }

    @Test
    public void testUpdate_resizedEvent() {
        JTailModelEvent event = new JTailModelEvent(JTailModelEvent.Type.RESIZED, 0, 0, 0, 0);
        jTailPanel.update(model, event);

        verify(model, times(1)).getLineNumbers();
        verify(model, times(1)).getContent();
        verify(jTailPanel.tableModel, times(1)).removeRow(anyInt());
        verify(jTailPanel.tableModel, times(1)).setRowCount(0);
        verify(jTailPanel.tableModel, times(1)).setColumnIdentifiers(new String[] { "#", "" });
        verify(jTailPanel.verticalScrollBar.getModel(), times(1)).setValueIsAdjusting(true);
        verify(jTailPanel.verticalScrollBar.getModel(), times(1)).setMinimum(0);
        verify(jTailPanel.verticalScrollBar.getModel(), times(1)).setValue(0);
        verify(jTailPanel.verticalScrollBar.getModel(), times(1)).setExtent(0);
        verify(jTailPanel.verticalScrollBar.getModel(), times(1)).setMaximum(0);
        verify(jTailPanel.verticalScrollBar.getModel(), times(1)).setValueIsAdjusting(false);
        verify(jTailPanel.verticalScrollBar, times(1)).setEnabled(false);
        verify(jTailPanel, times(1)).updateDataTable(anyString(), anyString());
    }

    @Test
    public void testUpdate_scrolledEvent() {
        JTailModelEvent event = new JTailModelEvent(JTailModelEvent.Type.SCROLLED, 0, 0, 0, 0);
        jTailPanel.update(model, event);

        verify(model, times(1)).getLineNumbers();
        verify(model, times(1)).getContent();
        verify(jTailPanel.tableModel, times(1)).removeRow(anyInt());
        verify(jTailPanel.tableModel, times(1)).setRowCount(0);
        verify(jTailPanel.tableModel, times(1)).setColumnIdentifiers(new String[] { "#", "" });
        verify(jTailPanel, times(1)).updateDataTable(anyString(), anyString());
    }
    
    @Test
    public void testUpdate_otherEvent() {
        JTailModelEvent event = new JTailModelEvent(JTailModelEvent.Type.OTHER, 0, 0, 0, 0);
        jTailPanel.update(model, event);

        verify(model, never()).getLineNumbers();
        verify(model, never()).getContent();
        verify(jTailPanel.tableModel, never()).removeRow(anyInt());
        verify(jTailPanel.tableModel, never()).setRowCount(0);
        verify(jTailPanel.tableModel, never()).setColumnIdentifiers(new String[] { "#", "" });
        verify(jTailPanel, never()).updateDataTable(anyString(), anyString());
    }
}
