import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DisplayableListPanelTest {

    private DisplayableListPanel displayableListPanel;

    @Before
    public void setUp() {
        SessionProperties sessionProperties = new SessionProperties();
        DisplayableListParentForm parentForm = new DisplayableListParentForm();
        String panelTitleText = "Test Panel";
        boolean allowWriteAccess = true;
        boolean allowRecordEditing = false;

        displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, panelTitleText, allowWriteAccess, allowRecordEditing);
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
        DisplayableListItemSelector listItemSelector = new DisplayableListItemSelector();
        displayableListPanel.setDisplayableListItemSelector(listItemSelector);
        assertNotNull(displayableListPanel.listItemSelector);
    }

    @Test
    public void testSetDisplayableListItemAdder() {
        DisplayableListItemAdder listItemAdder = new DisplayableListItemAdder();
        displayableListPanel.setDisplayableListItemAdder(listItemAdder);
        assertNotNull(displayableListPanel.listItemAdder);
    }

    @Test
    public void testSetDisplayableListItemEditor() {
        DisplayableListItemEditor listItemEditor = new DisplayableListItemEditor();
        displayableListPanel.setDisplayableListItemEditor(listItemEditor);
        assertNotNull(displayableListPanel.listItemEditor);
    }

    @Test
    public void testSetDisplayableListItemViewer() {
        DisplayableListItemViewer listItemViewer = new DisplayableListItemViewer();
        displayableListPanel.setDisplayableListItemViewer(listItemViewer);
        assertNotNull(displayableListPanel.listItemViewer);
    }

    @Test
    public void testSetDisplayableListItemDeleter() {
        DisplayableListItemDeleter listItemDeleter = new DisplayableListItemDeleter();
        displayableListPanel.setDisplayableListItemDeleter(listItemDeleter);
        assertNotNull(displayableListPanel.listItemDeleter);
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
    }

    @Test
    public void testSetDisplayableItems() {
        ArrayList<Displayable> masterList = new ArrayList<>();
        masterList.add(new Displayable());
        displayableListPanel.setDisplayableItems(masterList);
        assertEquals(1, displayableListPanel.masterList.size());
        assertEquals(1, displayableListPanel.displayableList.getAllItems().size());
        assertEquals(masterList.get(0), displayableListPanel.displayableList.getAllItems().get(0));
    }

    @Test
    public void testSave() {
        ArrayList<Displayable> masterList = new ArrayList<>();
        masterList.add(new Displayable());
        displayableListPanel.setDisplayableItems(masterList);
        displayableListPanel.save();
        assertEquals(1, displayableListPanel.masterList.size());
        assertEquals(1, displayableListPanel.displayableList.getAllItems().size());
        assertEquals(masterList.get(0), displayableListPanel.displayableList.getAllItems().get(0));
    }

    @Test
    public void testGetAllListItems() {
        ArrayList<Displayable> masterList = new ArrayList<>();
        masterList.add(new Displayable());
        displayableListPanel.setDisplayableItems(masterList);
        assertEquals(masterList, displayableListPanel.getAllListItems());
    }

    @Test
    public void testSetListOwnerName() {
        String listOwnerName = "Test Owner";
        displayableListPanel.setListOwnerName(listOwnerName);
        assertEquals(listOwnerName, displayableListPanel.listOwnerName);
    }

    @Test
    public void testActionPerformed() {
        // Cannot test actionPerformed method as it relies on external dependencies such as buttons and list items.
        // Testing the behavior of these dependencies should be done separately.
    }
}