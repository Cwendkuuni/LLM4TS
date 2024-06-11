package macaw.util;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;

public class DisplayableListPanelTest {

    DisplayableListPanel displayableListPanel;
    
    @Before
    public void setup() {
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
    public void testSetDisplayableListItemSelector() {
        DisplayableListItemSelector listItemSelector = new DisplayableListItemSelector() {
            public void selectListItems(String listOwnerName, DisplayableList displayableList) throws MacawException {}
        };
        
        displayableListPanel.setDisplayableListItemSelector(listItemSelector);
        assertNotNull(displayableListPanel.listItemSelector);
    }
    
    @Test
    public void testSetDisplayableListItemAdder() {
        DisplayableListItemAdder listItemAdder = new DisplayableListItemAdder() {
            public void addItems(String listOwnerName, DisplayableList displayableList) throws MacawException {}
        };
        
        displayableListPanel.setDisplayableListItemAdder(listItemAdder);
        assertNotNull(displayableListPanel.listItemAdder);
    }
    
    @Test
    public void testSetDisplayableListItemEditor() {
        DisplayableListItemEditor listItemEditor = new DisplayableListItemEditor() {
            public void editListItem(String listOwnerName, Displayable selectedItem, DisplayableList displayableList) throws MacawException {}
        };
        
        displayableListPanel.setDisplayableListItemEditor(listItemEditor);
        assertNotNull(displayableListPanel.listItemEditor);
    }
    
    @Test
    public void testSetDisplayableListItemViewer() {
        DisplayableListItemViewer listItemViewer = new DisplayableListItemViewer() {
            public void viewListItem(Displayable selectedItem) throws MacawException {}
        };
        
        displayableListPanel.setDisplayableListItemViewer(listItemViewer);
        assertNotNull(displayableListPanel.listItemViewer);
    }
    
    @Test
    public void testSetDisplayableListItemDeleter() {
        DisplayableListItemDeleter listItemDeleter = new DisplayableListItemDeleter() {
            public void deleteListItems(String listOwnerName, DisplayableList displayableList) throws MacawException {}
        };
        
        displayableListPanel.setDisplayableListItemDeleter(listItemDeleter);
        assertNotNull(displayableListPanel.listItemDeleter);
    }
    
    @Test
    public void testAddListItem() {
        //To be implmeneted
    }
    
    @Test
    public void testUpdateButtonStates() {
        //To be implmeneted
    }
    
    @Test
    public void testSetDisplayableItems() {
        ArrayList<Displayable> masterList = new ArrayList<>();
        displayableListPanel.setDisplayableItems(masterList);
        assertEquals(masterList, displayableListPanel.masterList);
    }
    
    @Test
    public void testSave() {
        ArrayList<Displayable> currentList = new ArrayList<>();
        displayableListPanel.displayableList.setDisplayItems(currentList);
        displayableListPanel.save();
        assertEquals(currentList, displayableListPanel.masterList);
    }
    
    @Test
    public void testGetAllListItems() {
        ArrayList<Displayable> currentList = new ArrayList<>();
        displayableListPanel.displayableList.setDisplayItems(currentList);
        assertEquals(currentList, displayableListPanel.getAllListItems());
    }
    
    @Test
    public void testSetListOwnerName() {
        displayableListPanel.setListOwnerName("listOwnerName");
        assertEquals("listOwnerName", displayableListPanel.listOwnerName);
    }
}