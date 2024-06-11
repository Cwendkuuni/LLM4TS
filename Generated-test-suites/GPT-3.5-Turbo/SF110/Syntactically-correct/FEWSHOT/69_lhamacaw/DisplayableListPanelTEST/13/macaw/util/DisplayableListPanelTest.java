package macaw.util;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class DisplayableListPanelTest {
    
    private DisplayableListPanel displayableListPanel;
    
    @Before
    public void setUp() {
        // Initialize DisplayableListPanel object
        displayableListPanel = new DisplayableListPanel(null, null, "", true);
    }
    
    @Test
    public void testSetNewRecord() {
        // Test when isNewRecord is true
        displayableListPanel.setNewRecord(true);
        assertTrue(displayableListPanel.add.isEnabled());
        assertFalse(displayableListPanel.delete.isEnabled());
        assertFalse(displayableListPanel.edit.isEnabled());
        assertFalse(displayableListPanel.view.isEnabled());
        assertFalse(displayableListPanel.select.isEnabled());
        
        // Test when isNewRecord is false
        displayableListPanel.setNewRecord(false);
        assertTrue(displayableListPanel.add.isEnabled());
        assertTrue(displayableListPanel.delete.isEnabled());
        assertTrue(displayableListPanel.select.isEnabled());
        
        // Test when allowWriteAccess is false
        displayableListPanel = new DisplayableListPanel(null, null, "", false);
        displayableListPanel.setNewRecord(true);
        assertFalse(displayableListPanel.add.isEnabled());
        assertFalse(displayableListPanel.delete.isEnabled());
        assertFalse(displayableListPanel.edit.isEnabled());
        assertFalse(displayableListPanel.view.isEnabled());
        assertFalse(displayableListPanel.select.isEnabled());
    }
    
    @Test
    public void testSetDisplayableItems() {
        // Create a list of Displayable objects
        ArrayList<Displayable> masterList = new ArrayList<>();
        masterList.add(new Displayable());
        masterList.add(new Displayable());
        
        // Set the displayable items
        displayableListPanel.setDisplayableItems(masterList);
        
        // Get the displayed items
        ArrayList<Displayable> displayedItems = displayableListPanel.getAllListItems();
        
        // Assert that the displayed items are the same as the master list
        assertEquals(masterList, displayedItems);
    }
    
    @Test
    public void testSave() {
        // Create a list of Displayable objects
        ArrayList<Displayable> masterList = new ArrayList<>();
        masterList.add(new Displayable());
        masterList.add(new Displayable());
        
        // Set the displayable items
        displayableListPanel.setDisplayableItems(masterList);
        
        // Save the displayable items
        displayableListPanel.save();
        
        // Get the master list
        ArrayList<Displayable> savedMasterList = displayableListPanel.getAllListItems();
        
        // Assert that the saved master list is the same as the original master list
        assertEquals(masterList, savedMasterList);
    }
}