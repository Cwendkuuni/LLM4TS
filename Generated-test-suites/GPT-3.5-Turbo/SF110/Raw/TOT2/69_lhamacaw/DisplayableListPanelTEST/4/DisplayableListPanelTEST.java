import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;

public class DisplayableListPanelTest {
    private DisplayableListPanel displayableListPanel;
    
    @Before
    public void setup() {
        displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true);
    }
    
    @Test
    public void testSetNewRecord() {
        displayableListPanel.setNewRecord(true);
        assertTrue(displayableListPanel.isNewRecord);
    }
    
    @Test
    public void testGetPanel() {
        assertNotNull(displayableListPanel.getPanel());
    }
    
    @Test
    public void testSetDisplayableListItemSelector() {
        assertNotNull("Must set DisplayableListItemSelector", displayableListPanel.listItemSelector);
    }
    
    @Test
    public void testSetDisplayableListItemAdder() {
        assertNotNull("Must set DisplayableListItemAdder", displayableListPanel.listItemAdder);
    }
    
    @Test
    public void testSetDisplayableListItemEditor() {
        assertNotNull("Must set DisplayableListItemEditor", displayableListPanel.listItemEditor);
    }
    
    @Test
    public void testSetDisplayableListItemViewer() {
        assertNotNull("Must set DisplayableListItemViewer", displayableListPanel.listItemViewer);
    }
    
    @Test
    public void testSetDisplayableListItemDeleter() {
        assertNotNull("Must set DisplayableListItemDeleter", displayableListPanel.listItemDeleter);
    }
    
    @Test
    public void testUpdateButtonStates() {
        displayableListPanel.setNewRecord(false);
        displayableListPanel.setDisplayableItems(new ArrayList<Displayable>());
        displayableListPanel.updateButtonStates();
        assertFalse(displayableListPanel.select.isEnabled());
        assertFalse(displayableListPanel.add.isEnabled());
        assertFalse(displayableListPanel.delete.isEnabled());
        assertFalse(displayableListPanel.edit.isEnabled());
        assertFalse(displayableListPanel.view.isEnabled());
    }
    
    @Test
    public void testSave() {
        displayableListPanel.setDisplayableItems(new ArrayList<Displayable>());
        displayableListPanel.save();
        assertTrue(displayableListPanel.getAllListItems().isEmpty());
    }
    
    @Test
    public void testSetListOwnerName() {
        displayableListPanel.setListOwnerName("Owner");
        assertEquals("Owner", displayableListPanel.listOwnerName);
    }
}