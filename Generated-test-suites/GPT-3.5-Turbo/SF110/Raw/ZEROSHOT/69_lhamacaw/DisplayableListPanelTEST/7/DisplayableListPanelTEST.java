package macaw.util;

import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.*;
import macaw.system.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;

public class DisplayableListPanelTest {

    @Test
    public void testSetNewRecord() {
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(new SessionProperties(), new DisplayableListParentForm(), "Test", true);
        displayableListPanel.setNewRecord(true);
        assertTrue(displayableListPanel.isNewRecord);
    }
    
    @Test
    public void testGetPanel() {
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(new SessionProperties(), new DisplayableListParentForm(), "Test", true);
        assertNotNull(displayableListPanel.getPanel());
    }
    
    @Test
    public void testSetDisplayableListItemSelector() {
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(new SessionProperties(), new DisplayableListParentForm(), "Test", true);
        displayableListPanel.setDisplayableListItemSelector(new DisplayableListItemSelector());
        assertNotNull(displayableListPanel.listItemSelector);
    }
    
    @Test
    public void testSetDisplayableListItemAdder() {
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(new SessionProperties(), new DisplayableListParentForm(), "Test", true);
        displayableListPanel.setDisplayableListItemAdder(new DisplayableListItemAdder());
        assertNotNull(displayableListPanel.listItemAdder);
    }
    
    @Test
    public void testSetDisplayableListItemEditor() {
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(new SessionProperties(), new DisplayableListParentForm(), "Test", true);
        displayableListPanel.setDisplayableListItemEditor(new DisplayableListItemEditor());
        assertNotNull(displayableListPanel.listItemEditor);
    }
    
    @Test
    public void testSetDisplayableListItemViewer() {
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(new SessionProperties(), new DisplayableListParentForm(), "Test", true);
        displayableListPanel.setDisplayableListItemViewer(new DisplayableListItemViewer());
        assertNotNull(displayableListPanel.listItemViewer);
    }
    
    @Test
    public void testSetDisplayableListItemDeleter() {
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(new SessionProperties(), new DisplayableListParentForm(), "Test", true);
        displayableListPanel.setDisplayableListItemDeleter(new DisplayableListItemDeleter());
        assertNotNull(displayableListPanel.listItemDeleter);
    }
    
    @Test
    public void testUpdateButtonStates_allowWriteAccess() {
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(new SessionProperties(), new DisplayableListParentForm(), "Test", true);
        displayableListPanel.setNewRecord(true);
        displayableListPanel.updateButtonStates();
        assertFalse(displayableListPanel.add.isEnabled());
        assertFalse(displayableListPanel.delete.isEnabled());
        assertFalse(displayableListPanel.edit.isEnabled());
        assertFalse(displayableListPanel.view.isEnabled());
        assertFalse(displayableListPanel.select.isEnabled());
        
        displayableListPanel.setNewRecord(false);
        displayableListPanel.updateButtonStates();
        assertTrue(displayableListPanel.add.isEnabled());
        assertTrue(displayableListPanel.select.isEnabled());
        assertFalse(displayableListPanel.delete.isEnabled());
        assertFalse(displayableListPanel.edit.isEnabled());
        assertTrue(displayableListPanel.view.isEnabled());
    }
    
    @Test
    public void testUpdateButtonStates_noWriteAccess() {
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(new SessionProperties(), new DisplayableListParentForm(), "Test", false);
        displayableListPanel.setNewRecord(true);
        displayableListPanel.updateButtonStates();
        assertFalse(displayableListPanel.add.isEnabled());
        assertFalse(displayableListPanel.delete.isEnabled());
        assertFalse(displayableListPanel.view.isEnabled());
        
        displayableListPanel.setNewRecord(false);
        displayableListPanel.updateButtonStates();
        assertFalse(displayableListPanel.add.isEnabled());
        assertFalse(displayableListPanel.delete.isEnabled());
        assertTrue(displayableListPanel.view.isEnabled());
    }
    
    @Test
    public void testSetDisplayableItems() {
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(new SessionProperties(), new DisplayableListParentForm(), "Test", true);
        ArrayList<Displayable> masterList = new ArrayList<>();
        displayableListPanel.setDisplayableItems(masterList);
        assertEquals(masterList, displayableListPanel.masterList);
        assertEquals(masterList, displayableListPanel.displayableList.getDisplayItems());
    }
    
    @Test
    public void testSave() {
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(new SessionProperties(), new DisplayableListParentForm(), "Test", true);
        ArrayList<Displayable> masterList = new ArrayList<>();
        Displayable displayable = new Displayable();
        displayable.setDisplayName("Item1");
        masterList.add(displayable);
        displayableListPanel.setDisplayableItems(masterList);
        displayableListPanel.save();
        masterList.clear();
        assertEquals(masterList, displayableListPanel.masterList);
    }
    
    @Test
    public void testGetAllListItems() {
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(new SessionProperties(), new DisplayableListParentForm(), "Test", true);
        ArrayList<Displayable> masterList = new ArrayList<>();
        Displayable displayable = new Displayable();
        displayable.setDisplayName("Item1");
        masterList.add(displayable);
        displayableListPanel.setDisplayableItems(masterList);
        assertEquals(masterList, displayableListPanel.getAllListItems());
    }
    
    @Test
    public void testSetListOwnerName() {
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(new SessionProperties(), new DisplayableListParentForm(), "Test", true);
        displayableListPanel.setListOwnerName("Owner");
        assertEquals("Owner", displayableListPanel.listOwnerName);
    }

}