package macaw.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class DisplayableListPanelTest {

    private DisplayableListPanel displayableListPanel;

    @Before
    public void setup() {
        displayableListPanel = new DisplayableListPanel(null, null, "", true);
    }

    @Test
    public void testSetNewRecord() {
        displayableListPanel.setNewRecord(true);
        assertTrue(displayableListPanel.getNewRecord());
        displayableListPanel.setNewRecord(false);
        assertFalse(displayableListPanel.getNewRecord());
    }

    @Test
    public void testGetPanel() {
        JPanel panel = displayableListPanel.getPanel();
        assertNotNull(panel);
    }

    @Test
    public void testSetDisplayableListItemSelector() {
        displayableListPanel.setDisplayableListItemSelector(null);
        assertNull(displayableListPanel.getListItemSelector());
    }

    @Test
    public void testSetDisplayableListItemAdder() {
        displayableListPanel.setDisplayableListItemAdder(null);
        assertNull(displayableListPanel.getListItemAdder());
    }

    @Test
    public void testSetDisplayableListItemEditor() {
        displayableListPanel.setDisplayableListItemEditor(null);
        assertNull(displayableListPanel.getListItemEditor());
    }

    @Test
    public void testSetDisplayableListItemViewer() {
        displayableListPanel.setDisplayableListItemViewer(null);
        assertNull(displayableListPanel.getListItemViewer());
    }

    @Test
    public void testSetDisplayableListItemDeleter() {
        displayableListPanel.setDisplayableListItemDeleter(null);
        assertNull(displayableListPanel.getListItemDeleter());
    }

    @Test
    public void testAddListItem() {
        displayableListPanel.addListItem();
        // add your assertions here
    }

    @Test
    public void testEditSelectedListItem() {
        displayableListPanel.editSelectedListItem();
        // add your assertions here
    }

    @Test
    public void testViewSelectedListItem() {
        displayableListPanel.viewSelectedListItem();
        // add your assertions here
    }

    @Test
    public void testDeleteSelectedListItems() {
        displayableListPanel.deleteSelectedListItems();
        // add your assertions here
    }

    @Test
    public void testSelectListItems() {
        displayableListPanel.selectListItems();
        // add your assertions here
    }

    @Test
    public void testUpdateButtonStates() {
        displayableListPanel.updateButtonStates();
        // add your assertions here
    }

    @Test
    public void testSetDisplayableItems() {
        displayableListPanel.setDisplayableItems(null);
        assertNull(displayableListPanel.getMasterList());
    }

    @Test
    public void testSave() {
        displayableListPanel.save();
        // add your assertions here
    }

    @Test
    public void testGetAllListItems() {
        ArrayList<Displayable> items = displayableListPanel.getAllListItems();
        assertNotNull(items);
    }

    @Test
    public void testSetListOwnerName() {
        displayableListPanel.setListOwnerName(null);
        assertNull(displayableListPanel.getListOwnerName());
    }

}