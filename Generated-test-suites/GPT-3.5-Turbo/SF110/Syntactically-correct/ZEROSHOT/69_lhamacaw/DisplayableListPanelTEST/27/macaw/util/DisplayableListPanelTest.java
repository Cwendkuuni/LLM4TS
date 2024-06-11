
package macaw.util;

import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import static org.junit.Assert.*;

public class DisplayableListPanelTest {

    private DisplayableListPanel displayableListPanel;

    @Before
    public void setUp() {
        displayableListPanel = new DisplayableListPanel(new SessionProperties(), new DisplayableListParentForm(), "Test Panel", true);
    }

    @Test
    public void testSetNewRecord() {
        displayableListPanel.setNewRecord(true);
        assertTrue(displayableListPanel.isNewRecord());

        displayableListPanel.setNewRecord(false);
        assertFalse(displayableListPanel.isNewRecord());
    }

    @Test
    public void testGetPanel() {
        assertNotNull(displayableListPanel.getPanel());
    }

    @Test
    public void testSetDisplayableListItemSelector() {
        DisplayableListItemSelector selector = new DisplayableListItemSelector() {
            @Override
            public void selectListItems(String listOwnerName, DisplayableList displayableList) {

            }
        };
        displayableListPanel.setDisplayableListItemSelector(selector);

        assertEquals(selector, displayableListPanel.getListItemSelector());
    }

    @Test
    public void testSetDisplayableListItemAdder() {
        DisplayableListItemAdder adder = new DisplayableListItemAdder() {
            @Override
            public void addItems(String listOwnerName, DisplayableList displayableList) {

            }
        };
        displayableListPanel.setDisplayableListItemAdder(adder);

        assertEquals(adder, displayableListPanel.getListItemAdder());
    }

    @Test
    public void testSetDisplayableListItemEditor() {
        DisplayableListItemEditor editor = new DisplayableListItemEditor() {
            @Override
            public void editListItem(String listOwnerName, Displayable selectedListItem, DisplayableList displayableList) {

            }
        };
        displayableListPanel.setDisplayableListItemEditor(editor);

        assertEquals(editor, displayableListPanel.getListItemEditor());
    }

    @Test
    public void testSetDisplayableListItemViewer() {
        DisplayableListItemViewer viewer = new DisplayableListItemViewer() {
            @Override
            public void viewListItem(Displayable selectedListItem) {

            }
        };
        displayableListPanel.setDisplayableListItemViewer(viewer);

        assertEquals(viewer, displayableListPanel.getListItemViewer());
    }

    @Test
    public void testSetDisplayableListItemDeleter() {
        DisplayableListItemDeleter deleter = new DisplayableListItemDeleter() {
            @Override
            public void deleteListItems(String listOwnerName, DisplayableList displayableList) {

            }
        };
        displayableListPanel.setDisplayableListItemDeleter(deleter);

        assertEquals(deleter, displayableListPanel.getListItemDeleter());
    }

    @Test
    public void testAddListItem() {
        fail("Not implemented yet");
    }

    @Test
    public void testUpdateButtonStates() {
        fail("Not implemented yet");
    }

    @Test
    public void testSetDisplayableItems() {
        ArrayList<Displayable> masterList = new ArrayList<>();
        masterList.add(new Displayable() {
            @Override
            public String getDisplayName() {
                return "Item 1";
            }
        });
        masterList.add(new Displayable() {
            @Override
            public String getDisplayName() {
                return "Item 2";
            }
        });

        displayableListPanel.setDisplayableItems(masterList);

        assertEquals(masterList, displayableListPanel.getMasterList());
        assertEquals(masterList.size(), displayableListPanel.getDisplayableList().getAllItems().size());
    }

    @Test
    public void testSave() {
        ArrayList<Displayable> masterList = new ArrayList<>();
        masterList.add(new Displayable() {
            @Override
            public String getDisplayName() {
                return "Item 1";
            }
        });
        masterList.add(new Displayable() {
            @Override
            public String getDisplayName() {
                return "Item 2";
            }
        });

        displayableListPanel.setDisplayableItems(masterList);
        displayableListPanel.save();

        assertEquals(masterList, displayableListPanel.getMasterList());
    }

    @Test
    public void testGetAllListItems() {
        ArrayList<Displayable> masterList = new ArrayList<>();
        masterList.add(new Displayable() {
            @Override
            public String getDisplayName() {
                return "Item 1";
            }
        });
        masterList.add(new Displayable() {
            @Override
            public String getDisplayName() {
                return "Item 2";
            }
        });

        displayableListPanel.setDisplayableItems(masterList);

        assertEquals(masterList, displayableListPanel.getAllListItems());
    }

    @Test
    public void testSetListOwnerName() {
        displayableListPanel.setListOwnerName("Owner");

        assertEquals("Owner", displayableListPanel.getListOwnerName());
    }

    @Test
    public void testActionPerformed() {
        fail("Not implemented yet");
    }
}

