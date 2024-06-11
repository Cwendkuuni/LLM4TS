import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class DisplayableListPanelTest {
    private DisplayableListPanel displayableListPanel;
    
    @Before
    public void setUp() {
        displayableListPanel = new DisplayableListPanel(new SessionProperties(), new DisplayableListParentForm(), "Test Panel", true);
    }

    @Test
    public void testSetNewRecord() {
        displayableListPanel.setNewRecord(true);
        assertTrue(displayableListPanel.add.isEnabled());
        assertFalse(displayableListPanel.delete.isEnabled());
        assertFalse(displayableListPanel.edit.isEnabled());
        assertFalse(displayableListPanel.view.isEnabled());
        assertFalse(displayableListPanel.select.isEnabled());

        displayableListPanel.setNewRecord(false);
        assertTrue(displayableListPanel.add.isEnabled());
        assertTrue(displayableListPanel.delete.isEnabled());
        assertFalse(displayableListPanel.edit.isEnabled());
        assertFalse(displayableListPanel.view.isEnabled());
        assertTrue(displayableListPanel.select.isEnabled());
    }

    @Test
    public void testGetPanel() {
        assertNotNull(displayableListPanel.getPanel());
    }

    @Test
    public void testSetDisplayableListItemSelector() {
        displayableListPanel.setDisplayableListItemSelector(new DisplayableListItemSelector());
        assertNotNull(displayableListPanel.listItemSelector);
    }

    @Test
    public void testSetDisplayableListItemAdder() {
        displayableListPanel.setDisplayableListItemAdder(new DisplayableListItemAdder());
        assertNotNull(displayableListPanel.listItemAdder);
    }

    @Test
    public void testSetDisplayableListItemEditor() {
        displayableListPanel.setDisplayableListItemEditor(new DisplayableListItemEditor());
        assertNotNull(displayableListPanel.listItemEditor);
    }

    @Test
    public void testSetDisplayableListItemViewer() {
        displayableListPanel.setDisplayableListItemViewer(new DisplayableListItemViewer());
        assertNotNull(displayableListPanel.listItemViewer);
    }

    @Test
    public void testSetDisplayableListItemDeleter() {
        displayableListPanel.setDisplayableListItemDeleter(new DisplayableListItemDeleter());
        assertNotNull(displayableListPanel.listItemDeleter);
    }

    @Test
    public void testAddListItem() {
        displayableListPanel.setDisplayableListItemAdder(new DisplayableListItemAdder());
        displayableListPanel.addListItem();

        assertTrue(displayableListPanel.displayableList.getItemCount() > 0);
    }

    @Test
    public void testEditSelectedListItem() {
        displayableListPanel.setDisplayableListItemEditor(new DisplayableListItemEditor());
        displayableListPanel.addListItem();
        displayableListPanel.editSelectedListItem();

        // TODO: Verify if the item has been edited
    }

    @Test
    public void testViewSelectedListItem() {
        displayableListPanel.setDisplayableListItemViewer(new DisplayableListItemViewer());
        displayableListPanel.addListItem();
        displayableListPanel.viewSelectedListItem();

        // TODO: Verify if the item has been viewed
    }

    @Test
    public void testDeleteSelectedListItems() {
        displayableListPanel.setDisplayableListItemDeleter(new DisplayableListItemDeleter());
        displayableListPanel.addListItem();
        displayableListPanel.deleteSelectedListItems();

        assertTrue(displayableListPanel.displayableList.getItemCount() == 0);
    }

    @Test
    public void testSetDisplayableItems() {
        ArrayList<Displayable> masterList = new ArrayList<>();
        masterList.add(new Displayable());
        displayableListPanel.setDisplayableItems(masterList);

        assertTrue(displayableListPanel.displayableList.getItemCount() == 1);
    }

    @Test
    public void testSave() {
        ArrayList<Displayable> masterList = new ArrayList<>();
        masterList.add(new Displayable());
        displayableListPanel.setDisplayableItems(masterList);

        displayableListPanel.save();

        assertEquals(masterList, displayableListPanel.getAllListItems());
    }

    @Test
    public void testGetAllListItems() {
        ArrayList<Displayable> masterList = new ArrayList<>();
        masterList.add(new Displayable());
        displayableListPanel.setDisplayableItems(masterList);

        assertEquals(masterList, displayableListPanel.getAllListItems());
    }

    @Test
    public void testSetListOwnerName() {
        displayableListPanel.setListOwnerName("Owner");

        assertEquals("Owner", displayableListPanel.listOwnerName);
    }
}