import org.junit.*;
import static org.junit.Assert.*;
import java.util.ArrayList;

public class DisplayableListPanelTest {

    DisplayableListPanel displayableListPanel;

    @Before
    public void setUp() {
        displayableListPanel = new DisplayableListPanel(new SessionProperties(), new DisplayableListParentForm(), "Panel Title", true);
        displayableListPanel.setDisplayableListItemSelector(new DisplayableListItemSelector());
        displayableListPanel.setDisplayableListItemAdder(new DisplayableListItemAdder());
        displayableListPanel.setDisplayableListItemEditor(new DisplayableListItemEditor());
        displayableListPanel.setDisplayableListItemViewer(new DisplayableListItemViewer());
        displayableListPanel.setDisplayableListItemDeleter(new DisplayableListItemDeleter());
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
    public void testSetDisplayableItems() {
        ArrayList<Displayable> masterList = new ArrayList<Displayable>();
        displayableListPanel.setDisplayableItems(masterList);
        assertEquals(masterList, displayableListPanel.getAllListItems());
    }

    @Test
    public void testSave() {
        ArrayList<Displayable> masterList = new ArrayList<Displayable>();
        displayableListPanel.setDisplayableItems(masterList);
        displayableListPanel.save();
        assertEquals(masterList, displayableListPanel.getAllListItems());
    }

    @Test
    public void testActionPerformed() {
        // Button select
        displayableListPanel.actionPerformed(new ActionEvent(displayableListPanel.select, ActionEvent.ACTION_PERFORMED, "select"));
        // Button add
        displayableListPanel.actionPerformed(new ActionEvent(displayableListPanel.add, ActionEvent.ACTION_PERFORMED, "add"));
        // Button edit
        displayableListPanel.actionPerformed(new ActionEvent(displayableListPanel.edit, ActionEvent.ACTION_PERFORMED, "edit"));
        // Button view
        displayableListPanel.actionPerformed(new ActionEvent(displayableListPanel.view, ActionEvent.ACTION_PERFORMED, "view"));
        // Button delete
        displayableListPanel.actionPerformed(new ActionEvent(displayableListPanel.delete, ActionEvent.ACTION_PERFORMED, "delete"));
    }
}