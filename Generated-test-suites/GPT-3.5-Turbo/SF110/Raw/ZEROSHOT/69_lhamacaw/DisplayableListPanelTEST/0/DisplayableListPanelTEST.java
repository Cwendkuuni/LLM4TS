package macaw.util;

import org.junit.*;
import static org.junit.Assert.*;

public class DisplayableListPanelTest {

    @Test
    public void testSetNewRecord() {
        DisplayableListPanel displayableListPanel = new DisplayableListPanel();
        displayableListPanel.setNewRecord(true);
        assertTrue(displayableListPanel.isNewRecord);

        displayableListPanel.setNewRecord(false);
        assertFalse(displayableListPanel.isNewRecord);
    }

    @Test
    public void testGetPanel() {
        DisplayableListPanel displayableListPanel = new DisplayableListPanel();
        assertNotNull(displayableListPanel.getPanel());
    }

    @Test
    public void testSetDisplayableItems() {
        DisplayableListPanel displayableListPanel = new DisplayableListPanel();
        ArrayList<Displayable> masterList = new ArrayList<Displayable>();
        displayableListPanel.setDisplayableItems(masterList);
        assertEquals(masterList, displayableListPanel.masterList);
    }

    @Test
    public void testSave() {
        DisplayableListPanel displayableListPanel = new DisplayableListPanel();
        ArrayList<Displayable> masterList = new ArrayList<Displayable>();
        Displayable displayable = new Displayable();
        masterList.add(displayable);
        displayableListPanel.setDisplayableItems(masterList);
        displayableListPanel.save();
        assertEquals(masterList, displayableListPanel.masterList);
    }

    @Test
    public void testGetAllListItems() {
        DisplayableListPanel displayableListPanel = new DisplayableListPanel();
        ArrayList<Displayable> masterList = new ArrayList<Displayable>();
        Displayable displayable = new Displayable();
        masterList.add(displayable);
        displayableListPanel.setDisplayableItems(masterList);
        assertEquals(masterList, displayableListPanel.getAllListItems());
    }

}