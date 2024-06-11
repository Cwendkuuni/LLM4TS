import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class DisplayableListPanelTest {
    
    private DisplayableListPanel panel;
    
    @Before
    public void setUp() {
        SessionProperties sessionProperties = new SessionProperties();
        DisplayableListParentForm parentForm = new DisplayableListParentForm();
        String panelTitleText = "Test Panel";
        boolean allowWriteAccess = true;
        panel = new DisplayableListPanel(sessionProperties, parentForm, panelTitleText, allowWriteAccess);
    }
    
    @Test
    public void testSetNewRecord() {
        panel.setNewRecord(true);
        assertTrue(panel.isNewRecord);
        panel.setNewRecord(false);
        assertFalse(panel.isNewRecord);
    }
    
    @Test
    public void testGetPanel() {
        assertNotNull(panel.getPanel());
    }
    
    @Test
    public void testSetDisplayableListItemSelector() {
        DisplayableListItemSelector listItemSelector = new DisplayableListItemSelector();
        panel.setDisplayableListItemSelector(listItemSelector);
        assertEquals(listItemSelector, panel.listItemSelector);
    }
    
    @Test
    public void testSetDisplayableListItemAdder() {
        DisplayableListItemAdder listItemAdder = new DisplayableListItemAdder();
        panel.setDisplayableListItemAdder(listItemAdder);
        assertEquals(listItemAdder, panel.listItemAdder);
    }
    
    @Test
    public void testSetDisplayableListItemEditor() {
        DisplayableListItemEditor listItemEditor = new DisplayableListItemEditor();
        panel.setDisplayableListItemEditor(listItemEditor);
        assertEquals(listItemEditor, panel.listItemEditor);
    }
    
    @Test
    public void testSetDisplayableListItemViewer() {
        DisplayableListItemViewer listItemViewer = new DisplayableListItemViewer();
        panel.setDisplayableListItemViewer(listItemViewer);
        assertEquals(listItemViewer, panel.listItemViewer);
    }
    
    @Test
    public void testSetDisplayableListItemDeleter() {
        DisplayableListItemDeleter listItemDeleter = new DisplayableListItemDeleter();
        panel.setDisplayableListItemDeleter(listItemDeleter);
        assertEquals(listItemDeleter, panel.listItemDeleter);
    }
    
    @Test
    public void testUpdateButtonStates() {
        panel.setNewRecord(true);
        panel.updateButtonStates();
        assertFalse(panel.add.isEnabled());
        assertFalse(panel.delete.isEnabled());
        assertFalse(panel.edit.isEnabled());
        assertFalse(panel.view.isEnabled());
        assertFalse(panel.select.isEnabled());
        
        panel.setNewRecord(false);
        panel.updateButtonStates();
        assertTrue(panel.add.isEnabled());
        assertTrue(panel.select.isEnabled());
        
    }
    
    @Test
    public void testSetDisplayableItems() {
        ArrayList<Displayable> masterList = new ArrayList<Displayable>();
        panel.setDisplayableItems(masterList);
        assertEquals(masterList, panel.masterList);
        assertEquals(masterList, panel.displayableList.getDisplayItems());
    }
    
    @Test
    public void testSave() {
        ArrayList<Displayable> masterList = new ArrayList<Displayable>();
        panel.setDisplayableItems(masterList);
        ArrayList<Displayable> savedList = panel.getAllListItems();
        assertEquals(masterList, savedList);
    }
    
    @Test
    public void testSetListOwnerName() {
        String listOwnerName = "Owner";
        panel.setListOwnerName(listOwnerName);
        assertEquals(listOwnerName, panel.listOwnerName);
    }
}