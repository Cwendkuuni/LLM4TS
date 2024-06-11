import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import macaw.util.DisplayableListPanel;
import macaw.businessLayer.Displayable;

public class DisplayableListPanelTest {

    private DisplayableListPanel displayableListPanel;

    @Before
    public void setUp() {
        displayableListPanel = new DisplayableListPanel(null, null, "Test Panel", true);
    }

    @Test
    public void testSetNewRecord() {
        // Typical use case - setting new record to true
        displayableListPanel.setNewRecord(true);
        assertTrue(displayableListPanel.getAdd().isEnabled());
        assertFalse(displayableListPanel.getSelect().isEnabled());
        assertFalse(displayableListPanel.getDelete().isEnabled());
        assertFalse(displayableListPanel.getEdit().isEnabled());
        assertFalse(displayableListPanel.getView().isEnabled());

        // Typical use case - setting new record to false
        displayableListPanel.setNewRecord(false);
        assertTrue(displayableListPanel.getAdd().isEnabled());
        assertTrue(displayableListPanel.getSelect().isEnabled());
    }

    @Test
    public void testGetPanel() {
        assertNotNull(displayableListPanel.getPanel());
    }

    @Test
    public void testSetDisplayableItems() {
        // Typical use case - setting displayable items
        ArrayList<Displayable> displayableList = new ArrayList<>();
        displayableList.add(new Displayable());
        displayableList.add(new Displayable());
        displayableListPanel.setDisplayableItems(displayableList);
        assertEquals(2, displayableListPanel.getAllListItems().size());

        // Edge case - setting empty displayable items list
        displayableListPanel.setDisplayableItems(new ArrayList<>());
        assertEquals(0, displayableListPanel.getAllListItems().size());

        // Edge case - setting null displayable items
        displayableListPanel.setDisplayableItems(null);
        assertNull(displayableListPanel.getAllListItems());
    }

    @Test
    public void testSave() {
        ArrayList<Displayable> displayableList = new ArrayList<>();
        displayableList.add(new Displayable());
        displayableList.add(new Displayable());
        displayableListPanel.setDisplayableItems(displayableList);

        // Typical use case - saving displayable items
        displayableListPanel.save();
        assertEquals(2, displayableListPanel.getAllListItems().size());
        assertEquals(2, displayableListPanel.getMasterList().size());

        // Edge case - saving empty displayable items
        displayableListPanel.setDisplayableItems(new ArrayList<>());
        displayableListPanel.save();
        assertEquals(0, displayableListPanel.getAllListItems().size());
        assertEquals(0, displayableListPanel.getMasterList().size());
    }

    @Test
    public void testSetListOwnerName() {
        // Typical use case - setting list owner name
        displayableListPanel.setListOwnerName("Owner");
        assertEquals("Owner", displayableListPanel.getListOwnerName());

        // Edge case - setting null list owner name
        displayableListPanel.setListOwnerName(null);
        assertNull(displayableListPanel.getListOwnerName());
    }

}