package macaw.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

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
    public void testSetDisplayableListItemSelector() {
        assertNull(displayableListPanel.listItemSelector);
        displayableListPanel.setDisplayableListItemSelector(new DisplayableListItemSelector() {
            @Override
            public void selectListItems(String listOwnerName, DisplayableList displayableList) {
                // TODO Auto-generated method stub
                
            }
        });
        assertNotNull(displayableListPanel.listItemSelector);
    }
    
    @Test
    public void testSetDisplayableListItemAdder() {
        assertNull(displayableListPanel.listItemAdder);
        displayableListPanel.setDisplayableListItemAdder(new DisplayableListItemAdder() {
            @Override
            public void addItems(String listOwnerName, DisplayableList displayableList) {
                // TODO Auto-generated method stub
                
            }
        });
        assertNotNull(displayableListPanel.listItemAdder);
    }
    
    @Test
    public void testSetDisplayableListItemEditor() {
        assertNull(displayableListPanel.listItemEditor);
        displayableListPanel.setDisplayableListItemEditor(new DisplayableListItemEditor() {

            @Override
            public void editListItem(String listOwnerName, Displayable selectedListItem,
                    DisplayableList displayableList) {
                // TODO Auto-generated method stub
                
            }
        });
        assertNotNull(displayableListPanel.listItemEditor);
    }
    
    @Test
    public void testSetDisplayableListItemViewer() {
        assertNull(displayableListPanel.listItemViewer);
        displayableListPanel.setDisplayableListItemViewer(new DisplayableListItemViewer() {

            @Override
            public void viewListItem(Displayable selectedListItem) {
                // TODO Auto-generated method stub
                
            }
        });
        assertNotNull(displayableListPanel.listItemViewer);
    }
    
    @Test
    public void testSetDisplayableListItemDeleter() {
        assertNull(displayableListPanel.listItemDeleter);
        displayableListPanel.setDisplayableListItemDeleter(new DisplayableListItemDeleter() {

            @Override
            public void deleteListItems(String listOwnerName, DisplayableList displayableList) {
                // TODO Auto-generated method stub
                
            }
        });
        assertNotNull(displayableListPanel.listItemDeleter);
    }
    
    @Test
    public void testAddListItem() {
        // TODO Add test cases for addListItem()
    }
    
    @Test
    public void testEditSelectedListItem() {
        // TODO Add test cases for editSelectedListItem()
    }
    
    @Test
    public void testViewSelectedListItem() {
        // TODO Add test cases for viewSelectedListItem()
    }
    
    @Test
    public void testDeleteSelectedListItems() {
        // TODO Add test cases for deleteSelectedListItems()
    }
    
    @Test
    public void testSelectListItems() {
        // TODO Add test cases for selectListItems()
    }
    
    @Test
    public void testUpdateButtonStates() {
        // TODO Add test cases for updateButtonStates()
    }
    
    @Test
    public void testSetDisplayableItems() {
        // TODO Add test cases for setDisplayableItems()
    }
    
    @Test
    public void testSave() {
        // TODO Add test cases for save()
    }
    
    @Test
    public void testGetAllListItems() {
        // TODO Add test cases for getAllListItems()
    }
    
    @Test
    public void testSetListOwnerName() {
        // TODO Add test cases for setListOwnerName()
    }
    
    @Test
    public void testActionPerformed() {
        // TODO Add test cases for actionPerformed()
    }
}