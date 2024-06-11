package macaw.util;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class DisplayableListPanelTest {
    
    private DisplayableListPanel displayableListPanel;
    private ArrayList<Displayable> masterList;
    private SessionProperties sessionProperties;
    private DisplayableListParentForm parentForm;
    private String panelTitleText;
    private boolean allowWriteAccess;

    @Before
    public void setUp() {
        sessionProperties = new SessionProperties();
        parentForm = new DisplayableListParentForm();
        panelTitleText = "Test Panel";
        allowWriteAccess = true;
        displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, panelTitleText, allowWriteAccess);
        masterList = new ArrayList<>();
        displayableListPanel.setDisplayableItems(masterList);
    }

    @Test
    public void testSetNewRecord() {
        displayableListPanel.setNewRecord(true);
        assertTrue(displayableListPanel.getAdd().isEnabled());
        assertFalse(displayableListPanel.getSelect().isEnabled());
        assertFalse(displayableListPanel.getDelete().isEnabled());
        assertFalse(displayableListPanel.getEdit().isEnabled());
        assertFalse(displayableListPanel.getView().isEnabled());
    }

    @Test
    public void testGetPanel() {
        assertNotNull(displayableListPanel.getPanel());
    }

    @Test
    public void testAddListItem() {
        DisplayableListItemAdder listItemAdder = new DisplayableListItemAdder() {
            @Override
            public void addItems(String listOwnerName, DisplayableList displayableList) {
                // implementation for test
            }
        };
        displayableListPanel.setDisplayableListItemAdder(listItemAdder);
        displayableListPanel.addListItem();
        // assert something
    }

    @Test
    public void testEditSelectedListItem() {
        DisplayableListItemEditor listItemEditor = new DisplayableListItemEditor() {
            @Override
            public void editListItem(String listOwnerName, Displayable selectedListItem, DisplayableList displayableList) {
                // implementation for test
            }
        };
        displayableListPanel.setDisplayableListItemEditor(listItemEditor);
        displayableListPanel.editSelectedListItem();
        // assert something
    }

    @Test
    public void testViewSelectedListItem() {
        DisplayableListItemViewer listItemViewer = new DisplayableListItemViewer() {
            @Override
            public void viewListItem(Displayable selectedListItem) {
                // implementation for test
            }
        };
        displayableListPanel.setDisplayableListItemViewer(listItemViewer);
        displayableListPanel.viewSelectedListItem();
        // assert something
    }

    @Test
    public void testDeleteSelectedListItems() {
        DisplayableListItemDeleter listItemDeleter = new DisplayableListItemDeleter() {
            @Override
            public void deleteListItems(String listOwnerName, DisplayableList displayableList) {
                // implementation for test
            }
        };
        displayableListPanel.setDisplayableListItemDeleter(listItemDeleter);
        displayableListPanel.deleteSelectedListItems();
        // assert something
    }

    @Test
    public void testSetDisplayableItems() {
        ArrayList<Displayable> testList = new ArrayList<>();
        displayableListPanel.setDisplayableItems(testList);
        assertEquals(testList, displayableListPanel.getAllListItems());
    }

    @Test
    public void testSave() {
        ArrayList<Displayable> testList = new ArrayList<>();
        displayableListPanel.setDisplayableItems(testList);
        displayableListPanel.save();
        assertEquals(testList, displayableListPanel.getAllListItems());
    }

    @Test
    public void testGetAllListItems() {
        ArrayList<Displayable> testList = new ArrayList<>();
        displayableListPanel.setDisplayableItems(testList);
        assertEquals(testList, displayableListPanel.getAllListItems());
    }

    @Test
    public void testSetListOwnerName() {
        String testListOwnerName = "Test Owner";
        displayableListPanel.setListOwnerName(testListOwnerName);
        // assert something
    }
}