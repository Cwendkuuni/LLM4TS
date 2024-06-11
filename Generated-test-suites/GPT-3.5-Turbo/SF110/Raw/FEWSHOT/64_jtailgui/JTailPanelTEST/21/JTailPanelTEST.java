
package fr.pingtimeout.jtail.gui.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.awt.FontMetrics;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentListener;
import java.awt.BorderLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import fr.pingtimeout.jtail.gui.controller.JTailSizeController;
import fr.pingtimeout.jtail.gui.controller.ScrollBarAdjustmentController;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.model.JTailModelEvent;
import fr.pingtimeout.jtail.util.JTailLogger;

public class JTailPanelTest {

    @Mock
    private JTailModel model;

    @Mock
    private JScrollPane scrollPane;

    @Mock
    private JScrollBar verticalScrollBar;

    @Mock
    private JTable dataTable;

    @Mock
    private DefaultTableModel tableModel;

    @Mock
    private FontMetrics fontMetrics;

    private JTailPanel jTailPanel;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        jTailPanel = new JTailPanel(model) {
            @Override
            public FontMetrics getFontMetrics(Font font) {
                return fontMetrics;
            }
        };
        jTailPanel.scrollPane = scrollPane;
        jTailPanel.verticalScrollBar = verticalScrollBar;
        jTailPanel.dataTable = dataTable;
        jTailPanel.tableModel = tableModel;
    }

    @Test
    public void testConstructor() {
        Mockito.verify(model).addObserver(jTailPanel);
        Mockito.verify(scrollPane).setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        Mockito.verify(scrollPane).setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        Mockito.verify(verticalScrollBar).addAdjustmentListener(Mockito.any(AdjustmentListener.class));
        Mockito.verify(jTailPanel).setLayout(new BorderLayout());
        Mockito.verify(jTailPanel).add(scrollPane, BorderLayout.CENTER);
        Mockito.verify(jTailPanel).add(verticalScrollBar, BorderLayout.EAST);
        assertNotNull(jTailPanel.model);
        assertNotNull(jTailPanel.scrollPane);
        assertNotNull(jTailPanel.verticalScrollBar);
        assertNotNull(jTailPanel.dataTable);
        assertNotNull(jTailPanel.tableModel);
        assertEquals(0, jTailPanel.maxCharacterWidth);
    }

    @Test
    public void testGetCharacterHeight() {
        int expectedHeight = 10;
        Mockito.when(fontMetrics.getHeight()).thenReturn(expectedHeight);
        Mockito.when(dataTable.getFontMetrics(dataTable.getFont())).thenReturn(fontMetrics);
        int characterHeight = jTailPanel.getCharacterHeight();
        assertEquals(expectedHeight - 1, characterHeight);
    }

    @Test
    public void testGetVerticalScrollBar() {
        JScrollBar returnedScrollBar = jTailPanel.getVerticalScrollBar();
        assertEquals(verticalScrollBar, returnedScrollBar);
    }

    @Test
    public void testUpdate() {
        Observable observable = new Observable();
        Object arg = new Object();
        JTailModelEvent event = new JTailModelEvent(JTailModelEvent.Type.RESIZED, 0, 0, 0, 0);
        Mockito.when(model.getLineNumbers()).thenReturn("1\n2\n3");
        Mockito.when(model.getContent()).thenReturn("line1\nline2\nline3");

        jTailPanel.update(observable, arg);
        Mockito.verify(model).getLineNumbers();
        Mockito.verify(model).getContent();
        Mockito.verify(tableModel, Mockito.times(3)).removeRow(0);
        Mockito.verify(tableModel).addRow(new String[] { "1", "line1" });
        Mockito.verify(tableModel).addRow(new String[] { "2", "line2" });
        Mockito.verify(tableModel).addRow(new String[] { "3", "line3" });
        Mockito.verify(dataTable).setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        jTailPanel.update(observable, event);
        Mockito.verify(verticalScrollBar.getModel()).setValueIsAdjusting(true);
        Mockito.verify(verticalScrollBar.getModel()).setMinimum(event.minLine);
        Mockito.verify(verticalScrollBar.getModel()).setValue(event.firstLine);
        Mockito.verify(verticalScrollBar.getModel()).setExtent(event.nbDisplayedLines);
        Mockito.verify(verticalScrollBar.getModel()).setMaximum(event.maxLine);
        Mockito.verify(verticalScrollBar.getModel()).setValueIsAdjusting(false);
        Mockito.verify(verticalScrollBar, Mockito.times(2)).setEnabled(true);
        Mockito.verify(jTailPanel).updateDataTable("1\n2\n3", "line1\nline2\nline3");

        event = new JTailModelEvent(JTailModelEvent.Type.SCROLLED, 0, 0, 0, 0);
        jTailPanel.update(observable, event);
        Mockito.verify(jTailPanel, Mockito.times(2)).updateDataTable("1\n2\n3", "line1\nline2\nline3");
    }

    @Test
    public void testUpdateDataTable() {
        String lineNumbers = "1\n2\n3";
        String lines = "line1\nline2\nline3";
        Mockito.when(dataTable.getWidth()).thenReturn(100);
        Mockito.when(dataTable.getColumnModel()).thenReturn(Mockito.mock(TableColumnModel.class));
        TableColumn numbersColumn = Mockito.mock(TableColumn.class);
        TableColumn linesColumn = Mockito.mock(TableColumn.class);
        Mockito.when(dataTable.getColumnModel().getColumn(0)).thenReturn(numbersColumn);
        Mockito.when(dataTable.getColumnModel().getColumn(1)).thenReturn(linesColumn);
        Mockito.when(fontMetrics.stringWidth(Mockito.anyString())).thenReturn(10);
        Mockito.when(fontMetrics.stringWidth(Mockito.eq("0"))).thenReturn(5);

        jTailPanel.updateDataTable(lineNumbers, lines);
        Mockito.verify(tableModel, Mockito.times(3)).removeRow(0);
        Mockito.verify(tableModel).addRow(new String[] { "1", "line1" });
        Mockito.verify(tableModel).addRow(new String[] { "2", "line2" });
        Mockito.verify(tableModel).addRow(new String[] { "3", "line3" });
        Mockito.verify(dataTable).setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        Mockito.verify(numbersColumn).setPreferredWidth(12);
        Mockito.verify(linesColumn).setPreferredWidth(88);
    }
}

