
package fr.pingtimeout.jtail.gui.view;

import static org.junit.Assert.*;
import org.junit.Test;

public class JTailPanelTest {
    @Test
    public void testGetCharacterHeight() {
        JTailModel model = new JTailModel();
        JTailPanel panel = new JTailPanel(model);
        int expectedHeight = panel.characterHeight;
        assertEquals(expectedHeight, panel.getCharacterHeight());
    }

    @Test
    public void testGetVerticalScrollBar() {
        JTailModel model = new JTailModel();
        JTailPanel panel = new JTailPanel(model);
        assertNotNull(panel.getVerticalScrollBar());
    }

    @Test
    public void testUpdate_ResizableEvent() {
        JTailModel model = new JTailModel();
        JTailPanel panel = new JTailPanel(model);
        Observable observable = model;
        Object arg = new JTailModelEvent(JTailModelEvent.Type.RESIZED, 0, 0, 10, 20);
        
        panel.update(observable, arg);

        int expectedMinimum = 0;
        int expectedValue = 0;
        int expectedExtent = 10;
        int expectedMaximum = 20;
        int expectedRowCount = 10;
        boolean expectedEnabled = true;

        BoundedRangeModel rangeModel = panel.verticalScrollBar.getModel();
        assertEquals(expectedMinimum, rangeModel.getMinimum());
        assertEquals(expectedValue, rangeModel.getValue());
        assertEquals(expectedExtent, rangeModel.getExtent());
        assertEquals(expectedMaximum, rangeModel.getMaximum());
        assertEquals(expectedRowCount, panel.tableModel.getRowCount());
        assertEquals(expectedEnabled, panel.verticalScrollBar.isEnabled());
    }

    @Test
    public void testUpdate_ScrolledEvent() {
        JTailModel model = new JTailModel();
        JTailPanel panel = new JTailPanel(model);
        Observable observable = model;
        Object arg = new JTailModelEvent(JTailModelEvent.Type.SCROLLED, 0, 0, 10, 20);

        panel.update(observable, arg);

        int expectedRowCount = 10;

        assertEquals(expectedRowCount, panel.tableModel.getRowCount());
    }

    @Test
    public void testUpdate_NullEvent() {
        JTailModel model = new JTailModel();
        JTailPanel panel = new JTailPanel(model);
        Observable observable = model;
        Object arg = null;

        panel.update(observable, arg);

        int expectedRowCount = 0;

        assertEquals(expectedRowCount, panel.tableModel.getRowCount());
    }
}
