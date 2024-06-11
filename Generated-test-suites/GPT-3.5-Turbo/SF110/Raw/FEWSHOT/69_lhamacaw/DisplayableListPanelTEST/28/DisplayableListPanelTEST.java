package macaw.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class DisplayableListPanelTest {
    
    private DisplayableListPanel displayableListPanel;
    
    @Before
    public void setUp() {
        displayableListPanel = new DisplayableListPanel(null, null, "", false);
    }
    
    @Test
    public void testSetNewRecord() {
        displayableListPanel.setNewRecord(true);
        assertTrue(displayableListPanel.isNewRecord);
        displayableListPanel.setNewRecord(false);
        assertFalse(displayableListPanel.isNewRecord);
    }
    
    @Test
    public void testUpdateButtonStates() {
        displayableListPanel.setNewRecord(true);
        displayableListPanel.updateButtonStates();
        assertFalse(displayableListPanel.add.isEnabled());
        assertFalse(displayableListPanel.select.isEnabled());
        assertFalse(displayableListPanel.delete.isEnabled());
        assertFalse(displayableListPanel.edit.isEnabled());
        assertFalse(displayableListPanel.view.isEnabled());
        
        displayableListPanel.setNewRecord(false);
        displayableListPanel.displayableList.setDisplayItems(null);
        displayableListPanel.updateButtonStates();
        assertTrue(displayableListPanel.add.isEnabled());
        assertTrue(displayableListPanel.select.isEnabled());
        assertFalse(displayableListPanel.delete.isEnabled());
        assertFalse(displayableListPanel.edit.isEnabled());
        assertFalse(displayableListPanel.view.isEnabled());
        
        displayableListPanel.displayableList.setDisplayItems(new ArrayList<Displayable>());
        displayableListPanel.updateButtonStates();
        assertTrue(displayableListPanel.add.isEnabled());
        assertTrue(displayableListPanel.select.isEnabled());
        assertFalse(displayableListPanel.delete.isEnabled());
        assertFalse(displayableListPanel.edit.isEnabled());
        assertFalse(displayableListPanel.view.isEnabled());
        
        displayableListPanel.displayableList.setDisplayItems(mockDisplayableList());
        displayableListPanel.updateButtonStates();
        assertTrue(displayableListPanel.add.isEnabled());
        assertTrue(displayableListPanel.select.isEnabled());
        assertTrue(displayableListPanel.delete.isEnabled());
        assertFalse(displayableListPanel.edit.isEnabled());
        assertFalse(displayableListPanel.view.isEnabled());
        
        displayableListPanel.allowRecordEditing = true;
        displayableListPanel.updateButtonStates();
        assertTrue(displayableListPanel.add.isEnabled());
        assertTrue(displayableListPanel.select.isEnabled());
        assertTrue(displayableListPanel.delete.isEnabled());
        assertTrue(displayableListPanel.edit.isEnabled());
        assertFalse(displayableListPanel.view.isEnabled());
        
        displayableListPanel.allowWriteAccess = false;
        displayableListPanel.updateButtonStates();
        assertFalse(displayableListPanel.add.isEnabled());
        assertFalse(displayableListPanel.select.isEnabled());
        assertTrue(displayableListPanel.delete.isEnabled());
        assertFalse(displayableListPanel.edit.isEnabled());
        assertFalse(displayableListPanel.view.isEnabled());
        
        displayableListPanel.displayableList.setDisplayItems(null);
        displayableListPanel.isNewRecord = false;
        displayableListPanel.allowWriteAccess = true;
        displayableListPanel.updateButtonStates();
        assertFalse(displayableListPanel.add.isEnabled());
        assertFalse(displayableListPanel.select.isEnabled());
        assertFalse(displayableListPanel.delete.isEnabled());
        assertFalse(displayableListPanel.edit.isEnabled());
        assertFalse(displayableListPanel.view.isEnabled());
        
        displayableListPanel.allowWriteAccess = false;
        displayableListPanel.displayableList.setDisplayItems(mockDisplayableList());
        displayableListPanel.updateButtonStates();
        assertFalse(displayableListPanel.add.isEnabled());
        assertFalse(displayableListPanel.select.isEnabled());
        assertTrue(displayableListPanel.delete.isEnabled());
        assertFalse(displayableListPanel.edit.isEnabled());
        assertFalse(displayableListPanel.view.isEnabled());
    }
    
    @Test
    public void testSetDisplayableItems() {
        ArrayList<Displayable> displayables = mockDisplayableList();
        displayableListPanel.setDisplayableItems(displayables);
        assertEquals(displayables, displayableListPanel.masterList);
        assertEquals(displayables, displayableListPanel.displayableList.getAllItems());
    }
    
    private ArrayList<Displayable> mockDisplayableList() {
        ArrayList<Displayable> displayables = new ArrayList<>();
        displayables.add(new Displayable());
        displayables.add(new Displayable());
        return displayables;
    }
}