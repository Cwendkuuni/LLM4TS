import org.junit.*;
import static org.junit.Assert.*;
import macaw.util.*;
import macaw.businessLayer.*;
import macaw.system.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;

public class DisplayableListPanelTest {

    @Test
    public void testSetNewRecord() {
        DisplayableListPanel panel = new DisplayableListPanel(null, null, "", false);
        panel.setNewRecord(true);
        assertTrue(panel.isNewRecord);

        panel.setNewRecord(false);
        assertFalse(panel.isNewRecord);
    }

    @Test
    public void testGetPanel() {
        DisplayableListPanel panel = new DisplayableListPanel(null, null, "", false);
        JPanel actualPanel = panel.getPanel();
        assertNotNull(actualPanel);
    }

    @Test
    public void testSetDisplayableListItemSelector() {
        DisplayableListPanel panel = new DisplayableListPanel(null, null, "", false);
        panel.setDisplayableListItemSelector(null);
        assertNull(panel.listItemSelector);

        DisplayableListItemSelector selector = new DisplayableListItemSelector();
        panel.setDisplayableListItemSelector(selector);
        assertEquals(panel.listItemSelector, selector);
    }

    @Test
    public void testSetDisplayableListItemAdder() {
        DisplayableListPanel panel = new DisplayableListPanel(null, null, "", false);
        panel.setDisplayableListItemAdder(null);
        assertNull(panel.listItemAdder);

        DisplayableListItemAdder adder = new DisplayableListItemAdder();
        panel.setDisplayableListItemAdder(adder);
        assertEquals(panel.listItemAdder, adder);
    }

    @Test
    public void testSetDisplayableListItemEditor() {
        DisplayableListPanel panel = new DisplayableListPanel(null, null, "", false);
        panel.setDisplayableListItemEditor(null);
        assertNull(panel.listItemEditor);

        DisplayableListItemEditor editor = new DisplayableListItemEditor();
        panel.setDisplayableListItemEditor(editor);
        assertEquals(panel.listItemEditor, editor);
    }

    @Test
    public void testSetDisplayableListItemViewer() {
        DisplayableListPanel panel = new DisplayableListPanel(null, null, "", false);
        panel.setDisplayableListItemViewer(null);
        assertNull(panel.listItemViewer);

        DisplayableListItemViewer viewer = new DisplayableListItemViewer();
        panel.setDisplayableListItemViewer(viewer);
        assertEquals(panel.listItemViewer, viewer);
    }

    @Test
    public void testSetDisplayableListItemDeleter() {
        DisplayableListPanel panel = new DisplayableListPanel(null, null, "", false);
        panel.setDisplayableListItemDeleter(null);
        assertNull(panel.listItemDeleter);

        DisplayableListItemDeleter deleter = new DisplayableListItemDeleter();
        panel.setDisplayableListItemDeleter(deleter);
        assertEquals(panel.listItemDeleter, deleter);
    }

    @Test
    public void testSetListOwnerName() {
        DisplayableListPanel panel = new DisplayableListPanel(null, null, "", false);
        panel.setListOwnerName("Owner1");
        assertEquals("Owner1", panel.listOwnerName);
    }

    @Test
    public void testUpdateButtonStates() {
        DisplayableListPanel panel = new DisplayableListPanel(null, null, "", false);
        panel.updateButtonStates();
        assertFalse(panel.add.isEnabled());
        assertFalse(panel.select.isEnabled());
        assertFalse(panel.delete.isEnabled());
        assertFalse(panel.edit.isEnabled());
        assertFalse(panel.view.isEnabled());

        panel.setNewRecord(true);
        panel.updateButtonStates();
        assertFalse(panel.add.isEnabled());
        assertFalse(panel.select.isEnabled());
        assertFalse(panel.delete.isEnabled());
        assertFalse(panel.edit.isEnabled());
        assertFalse(panel.view.isEnabled());

        panel.setNewRecord(false);
        panel.setDisplayableItems(new ArrayList<Displayable>());
        panel.updateButtonStates();
        assertFalse(panel.add.isEnabled());
        assertFalse(panel.select.isEnabled());
        assertFalse(panel.delete.isEnabled());
        assertFalse(panel.edit.isEnabled());
        assertFalse(panel.view.isEnabled());

        panel.setDisplayableItems(new ArrayList<Displayable>() {{
            add(new Displayable());
        }});
        panel.updateButtonStates();
        assertTrue(panel.add.isEnabled());
        assertTrue(panel.select.isEnabled());
        assertTrue(panel.delete.isEnabled());
        assertFalse(panel.edit.isEnabled());
        assertTrue(panel.view.isEnabled());

        DisplayableListItemEditor editor = new DisplayableListItemEditor();
        panel.setDisplayableListItemEditor(editor);
        panel.updateButtonStates();
        assertTrue(panel.add.isEnabled());
        assertTrue(panel.select.isEnabled());
        assertTrue(panel.delete.isEnabled());
        assertTrue(panel.edit.isEnabled());
        assertFalse(panel.view.isEnabled());

        panel.setNewRecord(true);
        panel.updateButtonStates();
        assertFalse(panel.add.isEnabled());
        assertFalse(panel.select.isEnabled());
        assertFalse(panel.delete.isEnabled());
        assertFalse(panel.edit.isEnabled());
        assertFalse(panel.view.isEnabled());
    }

    @Test
    public void testSave() {
        DisplayableListPanel panel = new DisplayableListPanel(null, null, "", false);
        panel.setNewRecord(false);
        panel.setDisplayableItems(new ArrayList<Displayable>());

        ArrayList<Displayable> expectedList = new ArrayList<Displayable>() {{
            add(new Displayable());
            add(new Displayable());
        }};

        panel.setDisplayableItems(expectedList);
        panel.save();
        assertEquals(expectedList, panel.masterList);
    }

    @Test
    public void testGetAllListItems() {
        DisplayableListPanel panel = new DisplayableListPanel(null, null, "", false);
        ArrayList<Displayable> expectedList = new ArrayList<Displayable>() {{
            add(new Displayable());
            add(new Displayable());
        }};
        panel.setDisplayableItems(expectedList);
        assertEquals(expectedList, panel.getAllListItems());
    }

    @Test
    public void testActionPerformed() {
        DisplayableListPanel panel = new DisplayableListPanel(null, null, "", false);
        panel.setDisplayableListItemSelector(null);
        panel.setDisplayableListItemAdder(null);
        panel.setDisplayableListItemEditor(null);
        panel.setDisplayableListItemViewer(null);
        panel.setDisplayableListItemDeleter(null);

        ActionEvent selectEvent = new ActionEvent(new Object(), 0, "");
        panel.actionPerformed(selectEvent);

        ActionEvent addEvent = new ActionEvent(new Object(), 0, "");
        panel.actionPerformed(addEvent);

        ActionEvent editEvent = new ActionEvent(new Object(), 0, "");
        panel.actionPerformed(editEvent);

        ActionEvent viewEvent = new ActionEvent(new Object(), 0, "");
        panel.actionPerformed(viewEvent);

        ActionEvent deleteEvent = new ActionEvent(new Object(), 0, "");
        panel.actionPerformed(deleteEvent);
    }
}