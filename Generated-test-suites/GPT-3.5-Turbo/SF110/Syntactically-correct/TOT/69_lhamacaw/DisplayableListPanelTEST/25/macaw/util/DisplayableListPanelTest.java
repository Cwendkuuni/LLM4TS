import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DisplayableListPanelTest {

    private DisplayableListPanel displayableListPanel;

    @Before
    public void setUp() {
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
        JPanel panel = displayableListPanel.getPanel();
        assertNotNull(panel);
    }

    @Test
    public void testSetDisplayableListItemSelector() {
        DisplayableListItemSelector listItemSelector = new DisplayableListItemSelector();
        displayableListPanel.setDisplayableListItemSelector(listItemSelector);
        assertNotNull(displayableListPanel.listItemSelector);
        assertEquals(listItemSelector, displayableListPanel.listItemSelector);
    }

    @Test
    public void testSetDisplayableListItemAdder() {
        DisplayableListItemAdder listItemAdder = new DisplayableListItemAdder();
        displayableListPanel.setDisplayableListItemAdder(listItemAdder);
        assertNotNull(displayableListPanel.listItemAdder);
        assertEquals(listItemAdder, displayableListPanel.listItemAdder);
    }

    @Test
    public void testSetDisplayableListItemEditor() {
        DisplayableListItemEditor listItemEditor = new DisplayableListItemEditor();
        displayableListPanel.setDisplayableListItemEditor(listItemEditor);
        assertNotNull(displayableListPanel.listItemEditor);
        assertEquals(listItemEditor, displayableListPanel.listItemEditor);
    }

    @Test
    public void testSetDisplayableListItemViewer() {
        DisplayableListItemViewer listItemViewer = new DisplayableListItemViewer();
        displayableListPanel.setDisplayableListItemViewer(listItemViewer);
        assertNotNull(displayableListPanel.listItemViewer);
        assertEquals(listItemViewer, displayableListPanel.listItemViewer);
    }

    @Test
    public void testSetDisplayableListItemDeleter() {
        DisplayableListItemDeleter listItemDeleter = new DisplayableListItemDeleter();
        displayableListPanel.setDisplayableListItemDeleter(listItemDeleter);
        assertNotNull(displayableListPanel.listItemDeleter);
        assertEquals(listItemDeleter, displayableListPanel.listItemDeleter);
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

        // test when displayableList is empty
        displayableListPanel.setDisplayableItems(new ArrayList<>());
        displayableListPanel.updateButtonStates();
        assertFalse(displayableListPanel.delete.isEnabled());
        assertFalse(displayableListPanel.edit.isEnabled());
        assertFalse(displayableListPanel.view.isEnabled());

        // test when displayableList is not empty
        displayableListPanel.setDisplayableItems(new ArrayList<>());
        displayableListPanel.getAllListItems().add(new Displayable());
        displayableListPanel.updateButtonStates();
        assertTrue(displayableListPanel.delete.isEnabled());
        assertTrue(displayableListPanel.edit.isEnabled());
        assertFalse(displayableListPanel.view.isEnabled());
    }

    @Test
    public void testSetDisplayableItems() {
        ArrayList<Displayable> masterList = new ArrayList<>();
        displayableListPanel.setDisplayableItems(masterList);
        assertEquals(masterList, displayableListPanel.getAllListItems());
    }

    @Test
    public void testSave() {
        ArrayList<Displayable> masterList = new ArrayList<>();
        masterList.add(new Displayable());
        displayableListPanel.setDisplayableItems(masterList);
        displayableListPanel.save();
        assertEquals(masterList, displayableListPanel.masterList);
    }

    // Add other test cases for each method, covering typical use cases, edge cases, and error scenarios

}