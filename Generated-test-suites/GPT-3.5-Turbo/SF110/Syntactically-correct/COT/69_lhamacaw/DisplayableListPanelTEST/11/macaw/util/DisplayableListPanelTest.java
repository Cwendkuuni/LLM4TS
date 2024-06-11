
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class DisplayableListPanelTest {

    private DisplayableListPanel displayableListPanel;

    @Test
    public void testSetNewRecord() {
        displayableListPanel = new DisplayableListPanel(null, null, "", true);
        displayableListPanel.setNewRecord(true);
        assertTrue(displayableListPanel.isNewRecord);
    }

    @Test
    public void testGetPanel() {
        displayableListPanel = new DisplayableListPanel(null, null, "", true);
        assertNotNull(displayableListPanel.getPanel());
    }

    @Test
    public void testSetDisplayableListItemSelector() {
        displayableListPanel = new DisplayableListPanel(null, null, "", true);
        assertNull(displayableListPanel.listItemSelector);
        DisplayableListItemSelector listItemSelector = new DisplayableListItemSelector();
        displayableListPanel.setDisplayableListItemSelector(listItemSelector);
        assertNotNull(displayableListPanel.listItemSelector);
    }

    @Test
    public void testSetDisplayableListItemAdder() {
        displayableListPanel = new DisplayableListPanel(null, null, "", true);
        assertNull(displayableListPanel.listItemAdder);
        DisplayableListItemAdder listItemAdder = new DisplayableListItemAdder();
        displayableListPanel.setDisplayableListItemAdder(listItemAdder);
        assertNotNull(displayableListPanel.listItemAdder);
    }

    @Test
    public void testSetDisplayableListItemEditor() {
        displayableListPanel = new DisplayableListPanel(null, null, "", true);
        assertNull(displayableListPanel.listItemEditor);
        DisplayableListItemEditor listItemEditor = new DisplayableListItemEditor();
        displayableListPanel.setDisplayableListItemEditor(listItemEditor);
        assertNotNull(displayableListPanel.listItemEditor);
    }

    @Test
    public void testSetDisplayableListItemViewer() {
        displayableListPanel = new DisplayableListPanel(null, null, "", true);
        assertNull(displayableListPanel.listItemViewer);
        DisplayableListItemViewer listItemViewer = new DisplayableListItemViewer();
        displayableListPanel.setDisplayableListItemViewer(listItemViewer);
        assertNotNull(displayableListPanel.listItemViewer);
    }

    @Test
    public void testSetDisplayableListItemDeleter() {
        displayableListPanel = new DisplayableListPanel(null, null, "", true);
        assertNull(displayableListPanel.listItemDeleter);
        DisplayableListItemDeleter listItemDeleter = new DisplayableListItemDeleter();
        displayableListPanel.setDisplayableListItemDeleter(listItemDeleter);
        assertNotNull(displayableListPanel.listItemDeleter);
    }

    @Test
    public void testAddListItem() {
        displayableListPanel = new DisplayableListPanel(null, null, "", true);
        try {
            displayableListPanel.addListItem();
        } catch (Exception exception) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testEditSelectedListItem() {
        displayableListPanel = new DisplayableListPanel(null, null, "", true);
        try {
            displayableListPanel.editSelectedListItem();
        } catch (Exception exception) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testViewSelectedListItem() {
        displayableListPanel = new DisplayableListPanel(null, null, "", true);
        try {
            displayableListPanel.viewSelectedListItem();
        } catch (Exception exception) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testDeleteSelectedListItems() {
        displayableListPanel = new DisplayableListPanel(null, null, "", true);
        try {
            displayableListPanel.deleteSelectedListItems();
        } catch (Exception exception) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testUpdateButtonStates() {
        displayableListPanel = new DisplayableListPanel(null, null, "", true);
        try {
            displayableListPanel.updateButtonStates();
        } catch (Exception exception) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testSetDisplayableItems() {
        displayableListPanel = new DisplayableListPanel(null, null, "", true);
        ArrayList<Displayable> masterList = new ArrayList<>();
        try {
            displayableListPanel.setDisplayableItems(masterList);
        } catch (Exception exception) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testSave() {
        displayableListPanel = new DisplayableListPanel(null, null, "", true);
        try {
            displayableListPanel.save();
        } catch (Exception exception) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testGetAllListItems() {
        displayableListPanel = new DisplayableListPanel(null, null, "", true);
        try {
            displayableListPanel.getAllListItems();
        } catch (Exception exception) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testSetListOwnerName() {
        displayableListPanel = new DisplayableListPanel(null, null, "", true);
        displayableListPanel.setListOwnerName("Owner");
        assertEquals("Owner", displayableListPanel.listOwnerName);
    }
}
