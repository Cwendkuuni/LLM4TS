
package fr.pingtimeout.jtail.gui.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.event.ComponentEvent;
import java.util.Observer;

import javax.swing.JScrollPane;
import javax.swing.JScrollBar;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import fr.pingtimeout.jtail.gui.controller.JTailSizeController;
import fr.pingtimeout.jtail.gui.controller.ScrollBarAdjustmentController;
import fr.pingtimeout.jtail.gui.model.BoundedRangeModel;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.model.JTailModelEvent;
import fr.pingtimeout.jtail.util.JTailLogger;

public class JTailPanelTest {

    private JTailModel model;
    private JTailPanel panel;
    private JScrollPane scrollPane;
    private JScrollBar verticalScrollBar;
    private JTable dataTable;
    private DefaultTableModel tableModel;
    private int characterHeight;
    private int maxCharacterWidth;

    @Before
    public void setUp() {
        JTailLogger.turnOffLogging();

        model = Mockito.mock(JTailModel.class);
        scrollPane = Mockito.mock(JScrollPane.class);
        verticalScrollBar = Mockito.mock(JScrollBar.class);
        dataTable = Mockito.mock(JTable.class);
        tableModel = Mockito.mock(DefaultTableModel.class);
        characterHeight = 20;
        maxCharacterWidth = 10;

        panel = new JTailPanel(model);
        panel.scrollPane = scrollPane;
        panel.verticalScrollBar = verticalScrollBar;
        panel.dataTable = dataTable;
        panel.tableModel = tableModel;
        panel.characterHeight = characterHeight;
        panel.maxCharacterWidth = maxCharacterWidth;
    }

    @Test
    public void testConstructor() {
        Mockito.verify(model).addObserver(panel);

        Mockito.verify(scrollPane).setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        Mockito.verify(scrollPane).setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        Mockito.verify(verticalScrollBar).addAdjustmentListener(Mockito.any(ScrollBarAdjustmentController.class));

        Mockito.verify(panel).setLayout(Mockito.any(BorderLayout.class));
        Mockito.verify(panel).add(scrollPane, BorderLayout.CENTER);
        Mockito.verify(panel).add(verticalScrollBar, BorderLayout.EAST);
    }

    @Test
    public void testGetCharacterHeight() {
        assertEquals(characterHeight, panel.getCharacterHeight());
    }

    @Test
    public void testGetVerticalScrollBar() {
        assertEquals(verticalScrollBar, panel.getVerticalScrollBar());
    }

    @Test
    public void testUpdate_resized() {
        JTailModelEvent event = new JTailModelEvent(JTailModelEvent.Type.RESIZED, 1, 0, 10, 10);
        panel.update(model, event);

        Mockito.verify(verticalScrollBar.getModel()).setValueIsAdjusting(true);
        Mockito.verify(verticalScrollBar.getModel()).setMinimum(1);
        Mockito.verify(verticalScrollBar.getModel()).setValue(0);
        Mockito.verify(verticalScrollBar.getModel()).setExtent(10);
        Mockito.verify(verticalScrollBar.getModel()).setMaximum(10);
        Mockito.verify(verticalScrollBar.getModel()).setValueIsAdjusting(false);
        Mockito.verify(panel).updateDataTable(Mockito.anyString(), Mockito.anyString());

        Mockito.verify(verticalScrollBar).setEnabled(false);
    }

    @Test
    public void testUpdate_scrolled() {
        JTailModelEvent event = new JTailModelEvent(JTailModelEvent.Type.SCROLLED, 0, 0, 10, 10);
        panel.update(model, event);

        Mockito.verify(panel).updateDataTable(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testUpdate_invalidType() {
        JTailModelEvent event = new JTailModelEvent(JTailModelEvent.Type.UNKNOWN, 0, 0, 10, 10);
        panel.update(model, event);

        Mockito.verify(panel, Mockito.never()).updateDataTable(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testUpdate_invalidObservable() {
        Observer observer = Mockito.mock(Observer.class);
        panel.update(observer, null);
        panel.update(null, null);

        Mockito.verify(panel, Mockito.never()).updateDataTable(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void testUpdateDataTable() {
        String lineNumbers = "1\n2\n3";
        String lines = "one\ntwo\nthree";

        panel.updateDataTable(lineNumbers, lines);

        Mockito.verify(tableModel, Mockito.times(3)).removeRow(0);

        Mockito.verify(tableModel).addRow(new String[] { "1", "one" });
        Mockito.verify(tableModel).addRow(new String[] { "2", "two" });
        Mockito.verify(tableModel).addRow(new String[] { "3", "three" });

        Mockito.verify(dataTable).setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        Mockito.verify(dataTable).getColumnModel();
        Mockito.verifyNoMoreInteractions(dataTable);

        Mockito.verify(dataTable).getWidth();
        Mockito.verifyNoMoreInteractions(dataTable);

        Mockito.verify(dataTable).getColumnModel();
        TableColumn numbersColumn = Mockito.mock(TableColumn.class);
        TableColumn linesColumn = Mockito.mock(TableColumn.class);
        Mockito.when(dataTable.getColumnModel()).thenReturn(Mockito.mock(TableColumnModel.class));
        Mockito.when(dataTable.getColumnModel().getColumn(0)).thenReturn(numbersColumn);
        Mockito.when(dataTable.getColumnModel().getColumn(1)).thenReturn(linesColumn);

        Mockito.verify(numbersColumn).setPreferredWidth(10);
        Mockito.verify(linesColumn).setPreferredWidth(50);
        Mockito.verifyNoMoreInteractions(numbersColumn);
        Mockito.verifyNoMoreInteractions(linesColumn);
    }
}
