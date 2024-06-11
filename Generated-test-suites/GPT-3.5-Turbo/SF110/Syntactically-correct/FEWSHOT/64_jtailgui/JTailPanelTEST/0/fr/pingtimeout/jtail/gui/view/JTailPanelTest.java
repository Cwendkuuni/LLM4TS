package fr.pingtimeout.jtail.gui.view;

import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.model.JTailModelEvent;
import fr.pingtimeout.jtail.util.JTailLogger;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.Observable;

import static org.junit.Assert.*;

public class JTailPanelTest {

    private JTailModel mockModel;
    private JTailPanel jTailPanel;

    @Before
    public void setUp() {
        mockModel = new JTailModel();
        jTailPanel = new JTailPanel(mockModel);
    }

    @Test
    public void testConstructor() {
        assertNotNull(jTailPanel);
        assertEquals(mockModel, jTailPanel.model);
        assertNotNull(jTailPanel.verticalScrollBar);
        assertNotNull(jTailPanel.tableModel);
        assertNotNull(jTailPanel.dataTable);
        assertNotNull(jTailPanel.scrollPane);
        assertEquals(0, jTailPanel.scrollPane.getVerticalScrollBarPolicy());
        assertEquals(1, jTailPanel.scrollPane.getHorizontalScrollBarPolicy());
    }

    @Test
    public void testGetCharacterHeight() {
        Font font = new Font("Arial", Font.PLAIN, 12);
        FontMetrics fontMetrics = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0].getFontMetrics(font);
        int expectedHeight = fontMetrics.getHeight() - 1;
        assertEquals(expectedHeight, jTailPanel.getCharacterHeight());
    }

    @Test
    public void testGetVerticalScrollBar() {
        assertNotNull(jTailPanel.getVerticalScrollBar());
        assertEquals(jTailPanel.verticalScrollBar, jTailPanel.getVerticalScrollBar());
    }

    @Test
    public void testUpdate_ResizeEvent() {
        String lineNumbers = "1\n2\n3\n4\n5\n";
        String content = "Line 1\nLine 2\nLine 3\nLine 4\nLine 5\n";
        mockModel.setLineNumbers(lineNumbers);
        mockModel.setContent(content);

        JTailModelEvent event = new JTailModelEvent(JTailModelEvent.Type.RESIZED, 1, 2, 3, 5);
        jTailPanel.update(mockModel, event);
        assertFalse(jTailPanel.verticalScrollBar.getModel().getValueIsAdjusting());
        assertEquals(1, jTailPanel.verticalScrollBar.getMinimum());
        assertEquals(2, jTailPanel.verticalScrollBar.getValue());
        assertEquals(3, jTailPanel.verticalScrollBar.getExtent());
        assertEquals(5, jTailPanel.verticalScrollBar.getMaximum());
        assertTrue(jTailPanel.verticalScrollBar.isEnabled());

        String[] expectedLineNumbersArray = {"1", "2", "3", "4", "5"};
        String[] expectedLinesArray = {"Line 1", "Line 2", "Line 3", "Line 4", "Line 5"};
        for (int i = 0; i < expectedLineNumbersArray.length; i++) {
            assertEquals(expectedLineNumbersArray[i], jTailPanel.tableModel.getValueAt(i, 0));
            assertEquals(expectedLinesArray[i], jTailPanel.tableModel.getValueAt(i, 1));
        }

        assertEquals(3, jTailPanel.dataTable.getAutoResizeMode());
        TableColumn numbersColumn = jTailPanel.dataTable.getColumnModel().getColumn(0);
        TableColumn linesColumn = jTailPanel.dataTable.getColumnModel().getColumn(1);
        int expectedNumbersColumnWidth = expectedLineNumbersArray[expectedLineNumbersArray.length - 1].length() * jTailPanel.maxCharacterWidth;
        int expectedLinesColumnWidth = jTailPanel.dataTable.getWidth() - expectedNumbersColumnWidth;
        assertEquals(expectedNumbersColumnWidth, numbersColumn.getPreferredWidth());
        assertEquals(expectedLinesColumnWidth, linesColumn.getPreferredWidth());
    }

    @Test
    public void testUpdate_ScrollEvent() {
        String lineNumbers = "1\n2\n3\n4\n5\n";
        String content = "Line 1\nLine 2\nLine 3\nLine 4\nLine 5\n";
        mockModel.setLineNumbers(lineNumbers);
        mockModel.setContent(content);

        JTailModelEvent event = new JTailModelEvent(JTailModelEvent.Type.SCROLLED, 1, 2, 3, 5);
        jTailPanel.update(mockModel, event);

        String[] expectedLineNumbersArray = {"1", "2", "3", "4", "5"};
        String[] expectedLinesArray = {"Line 1", "Line 2", "Line 3", "Line 4", "Line 5"};
        for (int i = 0; i < expectedLineNumbersArray.length; i++) {
            assertEquals(expectedLineNumbersArray[i], jTailPanel.tableModel.getValueAt(i, 0));
            assertEquals(expectedLinesArray[i], jTailPanel.tableModel.getValueAt(i, 1));
        }
    }
}