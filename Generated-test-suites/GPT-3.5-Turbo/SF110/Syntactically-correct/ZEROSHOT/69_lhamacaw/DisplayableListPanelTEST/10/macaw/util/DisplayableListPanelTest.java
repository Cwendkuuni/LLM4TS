import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import static org.junit.Assert.*;

public class DisplayableListPanelTest {

    private DisplayableListPanel displayableListPanel;

    @Before
    public void setUp() {
        SessionProperties sessionProperties = new SessionProperties();
        DisplayableListParentForm parentForm = new DisplayableListParentForm();
        String panelTitleText = "Test Panel";
        boolean allowWriteAccess = true;
        displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, panelTitleText, allowWriteAccess);
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
        assertNull(displayableListPanel.listItemSelector);
        DisplayableListItemSelector listItemSelector = new DisplayableListItemSelector();
        displayableListPanel.setDisplayableListItemSelector(listItemSelector);
        assertEquals(listItemSelector, displayableListPanel.listItemSelector);
    }

    @Test
    public void testSetDisplayableListItemAdder() {
        assertNull(displayableListPanel.listItemAdder);
        DisplayableListItemAdder listItemAdder = new DisplayableListItemAdder();
        displayableListPanel.setDisplayableListItemAdder(listItemAdder);
        assertEquals(listItemAdder, displayableListPanel.listItemAdder);
    }

    @Test
    public void testSetDisplayableListItemEditor() {
        assertNull(displayableListPanel.listItemEditor);
        DisplayableListItemEditor listItemEditor = new DisplayableListItemEditor();
        displayableListPanel.setDisplayableListItemEditor(listItemEditor);
        assertEquals(listItemEditor, displayableListPanel.listItemEditor);
    }

    @Test
    public void testSetDisplayableListItemViewer() {
        assertNull(displayableListPanel.listItemViewer);
        DisplayableListItemViewer listItemViewer = new DisplayableListItemViewer();
        displayableListPanel.setDisplayableListItemViewer(listItemViewer);
        assertEquals(listItemViewer, displayableListPanel.listItemViewer);
    }

    @Test
    public void testSetDisplayableListItemDeleter() {
        assertNull(displayableListPanel.listItemDeleter);
        DisplayableListItemDeleter listItemDeleter = new DisplayableListItemDeleter();
        displayableListPanel.setDisplayableListItemDeleter(listItemDeleter);
        assertEquals(listItemDeleter, displayableListPanel.listItemDeleter);
    }

    @Test
    public void testUpdateButtonStates() {
        displayableListPanel.updateButtonStates();
        assertFalse(displayableListPanel.add.isEnabled());
        assertFalse(displayableListPanel.select.isEnabled());
        assertFalse(displayableListPanel.delete.isEnabled());
        assertFalse(displayableListPanel.edit.isEnabled());
        assertFalse(displayableListPanel.view.isEnabled());
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
        displayableListPanel.setDisplayableItems(new ArrayList<>());
        ArrayList<Displayable> currentList = new ArrayList<>();
        displayableListPanel.save();
        assertEquals(currentList, displayableListPanel.masterList);
    }

    @Test
    public void testGetAllListItems() {
        ArrayList<Displayable> masterList = new ArrayList<>();
        displayableListPanel.setDisplayableItems(masterList);
        assertEquals(masterList, displayableListPanel.getAllListItems());
    }

    @Test
    public void testSetListOwnerName() {
        assertNull(displayableListPanel.listOwnerName);
        String listOwnerName = "Test Owner";
        displayableListPanel.setListOwnerName(listOwnerName);
        assertEquals(listOwnerName, displayableListPanel.listOwnerName);
    }
}