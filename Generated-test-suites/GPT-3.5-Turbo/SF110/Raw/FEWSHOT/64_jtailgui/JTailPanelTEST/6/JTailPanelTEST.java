
package fr.pingtimeout.jtail.gui.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import fr.pingtimeout.jtail.gui.model.JTailModel;

public class JTailPanelTest {

    private JTailModel model;
    private JTailPanel panel;

    @Before
    public void setup() {
        model = new JTailModel();
        panel = new JTailPanel(model);
    }

    @Test
    public void testConstructor() {
        assertNotNull(panel.getModel());
        assertNotNull(panel.getVerticalScrollBar());
        assertNotNull(panel.getDataTable());
        assertEquals(panel.getLayout().getClass(), BorderLayout.class);
    }

    @Test
    public void testGetCharacterHeight() {
        int characterHeight = panel.getCharacterHeight();
        assertTrue(characterHeight > 0);
    }

    @Test
    public void testUpdate() {
        // TODO: Add test for update method
    }

    @Test
    public void testUpdateDataTable() {
        // TODO: Add test for updateDataTable method
    }
}
