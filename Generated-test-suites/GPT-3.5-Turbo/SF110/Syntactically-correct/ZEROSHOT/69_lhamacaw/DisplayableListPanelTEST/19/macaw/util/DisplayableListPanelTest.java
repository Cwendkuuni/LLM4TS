package macaw.util;
import org.junit.*;
import static org.junit.Assert.*;
import java.awt.*;
import macaw.system.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;

public class DisplayableListPanelTest {

    private DisplayableListPanel displayableListPanel;
    private SessionProperties sessionProperties;
    private DisplayableListParentForm parentForm;
    
    @Before
    public void setUp() {
        sessionProperties = new SessionProperties();
        parentForm = new DisplayableListParentForm();
        displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true);
    }
    
    @After
    public void tearDown() {
        sessionProperties = null;
        parentForm = null;
        displayableListPanel = null;
    }
    
    @Test
    public void testSetNewRecord() {
        displayableListPanel.setNewRecord(true);
        assertTrue(displayableListPanel.newRecord);
        
        displayableListPanel.setNewRecord(false);
        assertFalse(displayableListPanel.newRecord);
    }
    
    @Test
    public void testGetPanel() {
        JPanel panel = displayableListPanel.getPanel();
        assertNotNull(panel);
    }
    
    @Test
    public void testUpdateButtonStates() {
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
        
        displayableListPanel.setNewRecord(false);
        displayableListPanel.displayableList = new DisplayableList();
        displayableListPanel.updateButtonStates();
        assertFalse(displayableListPanel.delete.isEnabled());
        assertTrue(displayableListPanel.edit.isEnabled());
        
        displayableListPanel.allowRecordEditing = false;
        displayableListPanel.updateButtonStates();
        assertFalse(displayableListPanel.edit.isEnabled());
        assertTrue(displayableListPanel.view.isEnabled());
        
        displayableListPanel=new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", false);
        displayableListPanel.allowWriteAccess = false;
        displayableListPanel.updateButtonStates();
        assertFalse(displayableListPanel.add.isEnabled());
        assertFalse(displayableListPanel.delete.isEnabled());
        assertFalse(displayableListPanel.view.isEnabled());
    }
    
    @Test
    public void testSetDisplayableItems() {
        ArrayList<Displayable> masterList = new ArrayList<Displayable>();
        displayableListPanel.setDisplayableItems(masterList);
        assertEquals(masterList, displayableListPanel.masterList);
        assertEquals(masterList, displayableListPanel.displayableList.getAllItems());
    }
    
    @Test
    public void testSave() {
        ArrayList<Displayable> masterList = new ArrayList<Displayable>();
        masterList.add(new Displayable());
        displayableListPanel.displayableList.setDisplayItems(masterList);
        displayableListPanel.save();
        assertEquals(masterList, displayableListPanel.masterList);
    }
    
    @Test
    public void testGetAllListItems() {
        ArrayList<Displayable> masterList = new ArrayList<Displayable>();
        masterList.add(new Displayable());
        displayableListPanel.displayableList.setDisplayItems(masterList);
        assertEquals(masterList, displayableListPanel.getAllListItems());
    }  
}