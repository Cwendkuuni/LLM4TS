package macaw.util;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class DisplayableListPanelTest {

    private DisplayableListPanel displayableListPanel;
    private SessionProperties sessionProperties;
    private DisplayableListParentForm parentForm;
    private String panelTitleText;
    private boolean allowWriteAccess;

    @Before
    public void setUp() {
        sessionProperties = Mockito.mock(SessionProperties.class);
        parentForm = Mockito.mock(DisplayableListParentForm.class);
        panelTitleText = "Test Panel";
        allowWriteAccess = true;
        displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, panelTitleText, allowWriteAccess);
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
    public void testUpdateButtonStatesWithAllowWriteAccess() {
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
        assertFalse(displayableListPanel.view.isEnabled());
        
        displayableListPanel.displayableList.setDisplayItems(new ArrayList<>());
        
        displayableListPanel.updateButtonStates();
        
        assertFalse(displayableListPanel.delete.isEnabled());
        
        displayableListPanel.allowRecordEditing = true;
        
        displayableListPanel.updateButtonStates();
        
        assertTrue(displayableListPanel.edit.isEnabled());
        assertFalse(displayableListPanel.view.isEnabled());
    }
    
    @Test
    public void testUpdateButtonStatesWithoutAllowWriteAccess() {
        displayableListPanel.allowWriteAccess = false;
        
        displayableListPanel.updateButtonStates();
        
        assertFalse(displayableListPanel.add.isEnabled());
        assertFalse(displayableListPanel.delete.isEnabled());
        assertFalse(displayableListPanel.edit.isEnabled());
        assertFalse(displayableListPanel.view.isEnabled());
        assertFalse(displayableListPanel.select.isEnabled());
        
        displayableListPanel.displayableList.setDisplayItems(new ArrayList<>());
        
        displayableListPanel.updateButtonStates();
        
        assertFalse(displayableListPanel.view.isEnabled());
        
        displayableListPanel.isNewRecord = false;
        
        displayableListPanel.updateButtonStates();
        
        assertTrue(displayableListPanel.view.isEnabled());
    }
    
    @Test
    public void testSetDisplayableItems() {
        ArrayList<Displayable> masterList = new ArrayList<>();
        displayableListPanel.setDisplayableItems(masterList);
        assertEquals(masterList, displayableListPanel.masterList);
        assertEquals(masterList, displayableListPanel.displayableList.getAllItems());
    }
    
    @Test
    public void testSave() {
        displayableListPanel.displayableList.setDisplayItems(new ArrayList<>());
        
        displayableListPanel.save();
        
        assertTrue(displayableListPanel.masterList.isEmpty());
        
        ArrayList<Displayable> masterList = new ArrayList<>();
        masterList.add(Mockito.mock(Displayable.class));
        displayableListPanel.displayableList.setDisplayItems(masterList);
        
        displayableListPanel.save();
        
        assertEquals(masterList, displayableListPanel.masterList);
    }
    
    @Test
    public void testGetAllListItems() {
        ArrayList<Displayable> masterList = new ArrayList<>();
        displayableListPanel.displayableList.setDisplayItems(masterList);
        
        assertEquals(masterList, displayableListPanel.getAllListItems());
    }
    
    @Test
    public void testSetListOwnerName() {
        String listOwnerName = "Test Owner";
        displayableListPanel.setListOwnerName(listOwnerName);
        assertEquals(listOwnerName, displayableListPanel.listOwnerName);
    }
}