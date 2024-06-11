import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.model.JTailModelEvent;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class JTailPanelTest {
    private JTailModel model;
    private JScrollPane scrollPane;
    private JScrollBar verticalScrollBar;
    private JTable dataTable;
    private DefaultTableModel tableModel;
    private JTailPanel jTailPanel;

    @Before
    public void setUp() {
        model = mock(JTailModel.class);
        scrollPane = mock(JScrollPane.class);
        verticalScrollBar = mock(JScrollBar.class);
        dataTable = mock(JTable.class);
        tableModel = mock(DefaultTableModel.class);

        when(scrollPane.getVerticalScrollBar()).thenReturn(verticalScrollBar);
        when(dataTable.getModel()).thenReturn(tableModel);

        jTailPanel = new JTailPanel(model);
        jTailPanel.scrollPane = scrollPane;
        jTailPanel.dataTable = dataTable;
    }

    @Test
    public void testConstructor() {
        assertNotNull(jTailPanel.model);
        assertNotNull(jTailPanel.scrollPane);
        assertNotNull(jTailPanel.verticalScrollBar);
        assertNotNull(jTailPanel.dataTable);
    }

    @Test
    public void testGetCharacterHeight() {
        int characterHeight = 20; // Example character height
        when(dataTable.getFontMetrics(dataTable.getFont()).getHeight()).thenReturn(characterHeight);
        assertEquals(characterHeight - 1, jTailPanel.getCharacterHeight());
    }

    @Test
    public void testGetVerticalScrollBar() {
        assertEquals(verticalScrollBar, jTailPanel.getVerticalScrollBar());
    }

    @Test
    public void testUpdate_resizedEvent() {
        JTailModelEvent event = new JTailModelEvent(JTailModelEvent.Type.RESIZED, 1, 1, 10, 10);
        jTailPanel.update(model, event);

        verify(verticalScrollBar.getModel()).setValueIsAdjusting(true);
        verify(verticalScrollBar.getModel()).setMinimum(event.minLine);
        verify(verticalScrollBar.getModel()).setValue(event.firstLine);
        verify(verticalScrollBar.getModel()).setExtent(event.nbDisplayedLines);
        verify(verticalScrollBar.getModel()).setMaximum(event.maxLine);
        verify(verticalScrollBar.getModel()).setValueIsAdjusting(false);
        verify(verticalScrollBar).setEnabled(false);
        verify(tableModel, times(2)).removeRow(0);
        verify(tableModel).addRow(new String[] { "1", "" });
    }

    @Test
    public void testUpdate_scrolledEvent() {
        JTailModelEvent event = new JTailModelEvent(JTailModelEvent.Type.SCROLLED, 0, 0, 10, 10);
        jTailPanel.update(model, event);

        verify(tableModel, times(2)).removeRow(0);
        verify(tableModel).addRow(new String[] { "0", "" });
    }

    @Test
    public void testUpdate_invalidEvent() {
        Object obj = new Object();
        jTailPanel.update(model, obj);

        verifyZeroInteractions(verticalScrollBar);
        verifyZeroInteractions(tableModel);
    }
}