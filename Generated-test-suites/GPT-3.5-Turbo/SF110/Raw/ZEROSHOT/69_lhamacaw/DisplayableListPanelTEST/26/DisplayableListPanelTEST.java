package macaw.util;

import org.junit.*;
import java.util.*;

public class DisplayableListPanelTest {
    
    @Test
    public void testSetNewRecord() {
        DisplayableListPanel panel = new DisplayableListPanel();
        panel.setNewRecord(true);
        Assert.assertTrue(panel.isNewRecord);
        
        panel.setNewRecord(false);
        Assert.assertFalse(panel.isNewRecord);
    }
    
    @Test
    public void testGetPanel() {
        DisplayableListPanel panel = new DisplayableListPanel();
        JPanel panel = panel.getPanel();
        Assert.assertNotNull(panel);
    }
    
    @Test
    public void testSetDisplayableListItemSelector() {
        DisplayableListPanel panel = new DisplayableListPanel();
        DisplayableListItemSelector selector = new DisplayableListItemSelector();
        panel.setDisplayableListItemSelector(selector);
        Assert.assertEquals(selector, panel.listItemSelector);
    }
    
    @Test
    public void testSetDisplayableListItemAdder() {
        DisplayableListPanel panel = new DisplayableListPanel();
        DisplayableListItemAdder adder = new DisplayableListItemAdder();
        panel.setDisplayableListItemAdder(adder);
        Assert.assertEquals(adder, panel.listItemAdder);
    }
    
    @Test
    public void testSetDisplayableListItemEditor() {
        DisplayableListPanel panel = new DisplayableListPanel();
        DisplayableListItemEditor editor = new DisplayableListItemEditor();
        panel.setDisplayableListItemEditor(editor);
        Assert.assertEquals(editor, panel.listItemEditor);
    }
    
    @Test
    public void testSetDisplayableListItemViewer() {
        DisplayableListPanel panel = new DisplayableListPanel();
        DisplayableListItemViewer viewer = new DisplayableListItemViewer();
        panel.setDisplayableListItemViewer(viewer);
        Assert.assertEquals(viewer, panel.listItemViewer);
    }
    
    @Test
    public void testSetDisplayableListItemDeleter() {
        DisplayableListPanel panel = new DisplayableListPanel();
        DisplayableListItemDeleter deleter = new DisplayableListItemDeleter();
        panel.setDisplayableListItemDeleter(deleter);
        Assert.assertEquals(deleter, panel.listItemDeleter);
    }
    
    @Test
    public void testAddListItem() {
        DisplayableListPanel panel = new DisplayableListPanel();
        panel.addListItem();
        
        // TODO: Add more test cases
    }
    
    @Test
    public void testEditSelectedListItem() {
        DisplayableListPanel panel = new DisplayableListPanel();
        panel.editSelectedListItem();
        
        // TODO: Add more test cases
    }
    
    @Test
    public void testViewSelectedListItem() {
        DisplayableListPanel panel = new DisplayableListPanel();
        panel.viewSelectedListItem();
        
        // TODO: Add more test cases
    }
    
    @Test
    public void testDeleteSelectedListItems() {
        DisplayableListPanel panel = new DisplayableListPanel();
        panel.deleteSelectedListItems();
        
        // TODO: Add more test cases
    }
    
    @Test
    public void testUpdateButtonStates() {
        DisplayableListPanel panel = new DisplayableListPanel();
        panel.updateButtonStates();
        
        // TODO: Add more test cases
    }
    
    @Test
    public void testSetDisplayableItems() {
        DisplayableListPanel panel = new DisplayableListPanel();
        ArrayList<Displayable> masterList = new ArrayList<>();
        panel.setDisplayableItems(masterList);
        Assert.assertEquals(masterList, panel.masterList);
    }
    
    @Test
    public void testSave() {
        DisplayableListPanel panel = new DisplayableListPanel();
        ArrayList<Displayable> masterList = new ArrayList<>();
        panel.masterList = masterList;
        panel.save();
        Assert.assertTrue(panel.masterList.isEmpty());
    }
    
    @Test
    public void testGetAllListItems() {
        DisplayableListPanel panel = new DisplayableListPanel();
        ArrayList<Displayable> masterList = new ArrayList<>();
        panel.displayableList.setDisplayItems(masterList);
        Assert.assertEquals(masterList, panel.getAllListItems());
    }
    
    @Test
    public void testSetListOwnerName() {
        DisplayableListPanel panel = new DisplayableListPanel();
        String listOwnerName = "TestOwner";
        panel.setListOwnerName(listOwnerName);
        Assert.assertEquals(listOwnerName, panel.listOwnerName);
    }
    
    @Test
    public void testActionPerformed() {
        DisplayableListPanel panel = new DisplayableListPanel();
        
        // TODO: Add test cases for different action events
        
    }

}