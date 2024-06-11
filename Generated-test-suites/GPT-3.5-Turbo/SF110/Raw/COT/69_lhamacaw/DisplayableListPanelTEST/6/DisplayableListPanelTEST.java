
package macaw.util;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class DisplayableListPanelTest {

    private DisplayableListPanel displayableListPanel;

    @Before
    public void setUp() {
        displayableListPanel = new DisplayableListPanel(null, null, "", true);
    }

    @Test
    public void testSetNewRecord() {
        displayableListPanel.setNewRecord(true);
        assertTrue(displayableListPanel.isNewRecord);

        displayableListPanel.setNewRecord(false);
        assertFalse(displayableListPanel.isNewRecord);
    }

    @Test
    public void testGetPanel() {
        assertNotNull(displayableListPanel.getPanel());
    }

    @Test
    public void testUpdateButtonStates() {
        displayableListPanel.updateButtonStates();

        displayableListPanel.allowWriteAccess = false;
        displayableListPanel.isNewRecord = true;
        displayableListPanel.displayableList.setDisplayItems(new ArrayList<>());
        displayableListPanel.updateButtonStates();

        displayableListPanel.allowWriteAccess = true;
        displayableListPanel.isNewRecord = false;
        displayableListPanel.displayableList.setDisplayItems(new ArrayList<>());
        displayableListPanel.updateButtonStates();

        displayableListPanel.displayableList.setDisplayItems(new ArrayList<>(List.of(new Displayable())));
        displayableListPanel.updateButtonStates();

        displayableListPanel.allowRecordEditing = false;
        displayableListPanel.updateButtonStates();
    }

    @Test
    public void testSetDisplayableItems() {
        ArrayList<Displayable> displayableList = new ArrayList<>(List.of(new Displayable(), new Displayable()));
        displayableListPanel.setDisplayableItems(displayableList);

        assertEquals(displayableList, displayableListPanel.masterList);
        assertEquals(displayableList, displayableListPanel.displayableList.getAllItems());
    }

    @Test
    public void testSave() {
        ArrayList<Displayable> displayableList = new ArrayList<>(List.of(new Displayable(), new Displayable()));
        displayableListPanel.setDisplayableItems(displayableList);

        displayableListPanel.save();

        assertEquals(displayableList, displayableListPanel.masterList);
    }

    @Test
    public void testGetAllListItems() {
        ArrayList<Displayable> displayableList = new ArrayList<>(List.of(new Displayable(), new Displayable()));
        displayableListPanel.setDisplayableItems(displayableList);

        assertEquals(displayableList, displayableListPanel.getAllListItems());
    }

}
