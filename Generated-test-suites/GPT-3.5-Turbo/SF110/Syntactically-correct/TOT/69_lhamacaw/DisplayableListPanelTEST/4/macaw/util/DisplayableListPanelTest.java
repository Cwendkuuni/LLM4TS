import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import java.util.ArrayList;

public class DisplayableListPanelTest {
    
    @Test
    public void testSetNewRecord() {
        DisplayableListPanel testPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true);
        testPanel.setNewRecord(true);
        assertFalse(testPanel.add.isEnabled());
        assertFalse(testPanel.delete.isEnabled());
        assertFalse(testPanel.edit.isEnabled());
        assertFalse(testPanel.view.isEnabled());
        assertFalse(testPanel.select.isEnabled());
        testPanel.setNewRecord(false);
        assertTrue(testPanel.add.isEnabled());
        assertTrue(testPanel.select.isEnabled());
        assertFalse(testPanel.edit.isEnabled());
        assertFalse(testPanel.view.isEnabled());
        assertFalse(testPanel.delete.isEnabled());
    }
    
    @Test
    public void testGetPanel() {
        DisplayableListPanel testPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true);
        JPanel panel = testPanel.getPanel();
        assertNotNull(panel);
    }
    
    @Test
    public void testAddListItem() {
        DisplayableListPanel testPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true);
        testPanel.setDisplayableListItemAdder(new DisplayableListItemAdder());
        testPanel.addListItem();
        //Assert that the items were added correctly
    }
    
    @Test
    public void testEditSelectedListItem() {
        DisplayableListPanel testPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true);
        testPanel.setDisplayableListItemEditor(new DisplayableListItemEditor());
        testPanel.editSelectedListItem();
        //Assert that the selected item was edited correctly
    }
    
    @Test
    public void testViewSelectedListItem() {
        DisplayableListPanel testPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true);
        testPanel.setDisplayableListItemViewer(new DisplayableListItemViewer());
        testPanel.viewSelectedListItem();
        //Assert that the selected item was viewed correctly
    }
    
    @Test
    public void testDeleteSelectedListItems() {
        DisplayableListPanel testPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true);
        testPanel.setDisplayableListItemDeleter(new DisplayableListItemDeleter());
        testPanel.deleteSelectedListItems();
        //Assert that the selected items were deleted correctly
    }
    
    @Test
    public void testSelectListItems() {
        DisplayableListPanel testPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true);
        testPanel.setDisplayableListItemSelector(new DisplayableListItemSelector());
        testPanel.selectListItems();
        //Assert that the list items were selected correctly
    }
    
    @Test
    public void testUpdateButtonStates() {
        DisplayableListPanel testPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true);
        testPanel.setNewRecord(true);
        testPanel.updateButtonStates();
        assertFalse(testPanel.add.isEnabled());
        assertFalse(testPanel.delete.isEnabled());
        assertFalse(testPanel.edit.isEnabled());
        assertFalse(testPanel.view.isEnabled());
        assertFalse(testPanel.select.isEnabled());
        
        testPanel.setNewRecord(false);
        testPanel.setDisplayableListItemSelector(new DisplayableListItemSelector());
        testPanel.setDisplayableItems(new ArrayList<>());
        testPanel.updateButtonStates();
        assertTrue(testPanel.add.isEnabled());
        assertTrue(testPanel.select.isEnabled());
        assertFalse(testPanel.edit.isEnabled());
        assertFalse(testPanel.view.isEnabled());
        assertFalse(testPanel.delete.isEnabled());
        
        testPanel.setDisplayableItems(new ArrayList<Displayable>());
        testPanel.updateButtonStates();
        assertTrue(testPanel.add.isEnabled());
        assertTrue(testPanel.select.isEnabled());
        assertFalse(testPanel.edit.isEnabled());
        assertFalse(testPanel.view.isEnabled());
        assertFalse(testPanel.delete.isEnabled());
        
        testPanel.setDisplayableItems(new ArrayList<Displayable>());
        testPanel.setAllowRecordEditing(true);
        testPanel.setDisplayableListItemEditor(new DisplayableListItemEditor());
        testPanel.updateButtonStates();
        assertTrue(testPanel.add.isEnabled());
        assertTrue(testPanel.select.isEnabled());
        assertFalse(testPanel.edit.isEnabled());
        assertFalse(testPanel.view.isEnabled());
        assertFalse(testPanel.delete.isEnabled());
        
        testPanel.setDisplayableItems(new ArrayList<Displayable>());
        testPanel.setAllowRecordEditing(false);
        testPanel.setDisplayableListItemViewer(new DisplayableListItemViewer());
        testPanel.updateButtonStates();
        assertTrue(testPanel.add.isEnabled());
        assertTrue(testPanel.select.isEnabled());
        assertFalse(testPanel.edit.isEnabled());
        assertFalse(testPanel.view.isEnabled());
        assertFalse(testPanel.delete.isEnabled());
        
        testPanel.setDisplayableItems(new ArrayList<Displayable>());
        testPanel.setDisplayableListItemDeleter(new DisplayableListItemDeleter());
        testPanel.updateButtonStates();
        assertTrue(testPanel.add.isEnabled());
        assertTrue(testPanel.select.isEnabled());
        assertFalse(testPanel.edit.isEnabled());
        assertFalse(testPanel.view.isEnabled());
        assertFalse(testPanel.delete.isEnabled());
    }
    
    @Test
    public void testSetDisplayableItems() {
        DisplayableListPanel testPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true);
        ArrayList<Displayable> masterList = new ArrayList<>();
        testPanel.setDisplayableItems(masterList);
        assertEquals(masterList, testPanel.getAllListItems());
    }
    
    @Test
    public void testSave() {
        DisplayableListPanel testPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true);
        ArrayList<Displayable> currentList = new ArrayList<>();
        testPanel.setDisplayableItems(currentList);
        testPanel.save();
        assertEquals(currentList, testPanel.getAllListItems());
    }

}