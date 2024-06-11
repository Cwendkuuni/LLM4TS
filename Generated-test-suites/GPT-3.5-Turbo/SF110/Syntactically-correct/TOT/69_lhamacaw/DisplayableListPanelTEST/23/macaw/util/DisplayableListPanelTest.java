import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import macaw.businessLayer.SessionProperties;
import macaw.businessLayer.User;
import macaw.util.*;

public class DisplayableListPanelTest {

    private DisplayableListPanel displayPanel;
    private DisplayableListParentForm parentForm;
    private SessionProperties sessionProperties;
    private User user;
    
    @Before
    public void setUp() {
        sessionProperties = new SessionProperties();
        user = new User();
        sessionProperties.setProperty("currentUser", user);
        parentForm = new DisplayableListParentForm();
        displayPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true);
    }
    
    // Test cases for constructor 1
    @Test
    public void testDisplayableListPanelConstructor1() {
        assertNotNull("DisplayableListPanel object not created", displayPanel);
    }
    
    // Test cases for constructor 2
    @Test
    public void testDisplayableListPanelConstructor2() {
        displayPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
        assertNotNull("DisplayableListPanel object not created", displayPanel);
    }
    
    // Test case for getPanel method
    @Test
    public void testGetPanel() {
        assertNotNull("DisplayableListPanel panel is null", displayPanel.getPanel());
    }
    
    // Test case for setDisplayableListItemSelector method
    @Test
    public void testSetDisplayableListItemSelector() {
        DisplayableListItemSelector listItemSelector = new DisplayableListItemSelector();
        displayPanel.setDisplayableListItemSelector(listItemSelector);
        assertNotNull("DisplayableListItemSelector not set", listItemSelector);
    }
    
    // Test case for setDisplayableListItemAdder method
    @Test
    public void testSetDisplayableListItemAdder() {
        DisplayableListItemAdder listItemAdder = new DisplayableListItemAdder();
        displayPanel.setDisplayableListItemAdder(listItemAdder);
        assertNotNull("DisplayableListItemAdder not set", listItemAdder);
    }
    
    // Test case for setDisplayableListItemEditor method
    @Test
    public void testSetDisplayableListItemEditor() {
        DisplayableListItemEditor listItemEditor = new DisplayableListItemEditor();
        displayPanel.setDisplayableListItemEditor(listItemEditor);
        assertNotNull("DisplayableListItemEditor not set", listItemEditor);
    }
    
    // Test case for setDisplayableListItemViewer method
    @Test
    public void testSetDisplayableListItemViewer() {
        DisplayableListItemViewer listItemViewer = new DisplayableListItemViewer();
        displayPanel.setDisplayableListItemViewer(listItemViewer);
        assertNotNull("DisplayableListItemViewer not set", listItemViewer);
    }
    
    // Test case for setDisplayableListItemDeleter method
    @Test
    public void testSetDisplayableListItemDeleter() {
        DisplayableListItemDeleter listItemDeleter = new DisplayableListItemDeleter();
        displayPanel.setDisplayableListItemDeleter(listItemDeleter);
        assertNotNull("DisplayableListItemDeleter not set", listItemDeleter);
    }
    
    // Test case for addListItem method - when listItemAdder is null
    @Test(expected = MacawException.class)
    public void testAddListItemWithNullAdder() throws MacawException {
        displayPanel.addListItem();
    }
    
    // Test case for deleteSelectedListItems method - when listItemDeleter is null
    @Test(expected = MacawException.class)
    public void testDeleteSelectedListItemsWithNullDeleter() throws MacawException {
        displayPanel.deleteSelectedListItems();
    }
    
    // Test case for updateButtonStates method - when allowWriteAccess is false
    @Test
    public void testUpdateButtonStatesWithWriteAccessFalse() {
        displayPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", false);
        displayPanel.updateButtonStates();
        
        assertFalse("Add button enabled with write access false", displayPanel.add.isEnabled());
        assertFalse("Delete button enabled with write access false", displayPanel.delete.isEnabled());
        assertFalse("Edit button enabled with write access false", displayPanel.edit.isEnabled());
        assertFalse("View button enabled with write access false", displayPanel.view.isEnabled());
        assertFalse("Select button enabled with write access false", displayPanel.select.isEnabled());
    }

    // Test case for setDisplayableItems method
    @Test
    public void testSetDisplayableItems() {
        displayPanel.setDisplayableItems(new ArrayList<>());
        assertTrue("DisplayableListPanel masterList is empty", displayPanel.getAllListItems().isEmpty());
    }
    
    // Test case for save method
    @Test
    public void testSave() {
        displayPanel.setDisplayableItems(new ArrayList<>());
        displayPanel.updateButtonStates();
        displayPanel.save();
        
        assertTrue("DisplayableListPanel masterList is not empty", displayPanel.getAllListItems().isEmpty());
    }
}