
package macaw.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class DisplayableListPanelTest implements ActionListener {

    private SessionProperties sessionProperties;
    private DisplayableListParentForm parentForm;
    private String panelTitleText;
    private boolean allowWriteAccess;
    private boolean allowRecordEditing;
    private DisplayableListItemSelector listItemSelector;
    private DisplayableListItemAdder listItemAdder;
    private DisplayableListItemEditor listItemEditor;
    private DisplayableListItemViewer listItemViewer;
    private DisplayableListItemDeleter listItemDeleter;
    private ArrayList<Displayable> masterList;
    private DisplayableListPanel displayableListPanel;

    @Before
    public void setUp() {
        sessionProperties = new SessionProperties();
        parentForm = new DisplayableListParentForm();
        panelTitleText = "Test Panel";
        allowWriteAccess = true;
        allowRecordEditing = true;
        listItemSelector = new DisplayableListItemSelector() {
            @Override
            public void selectListItems(String listOwnerName, DisplayableList displayableList) {
                // Implementation
            }
        };
        listItemAdder = new DisplayableListItemAdder() {
            @Override
            public void addItems(String listOwnerName, DisplayableList displayableList) {
                // Implementation
            }
        };
        listItemEditor = new DisplayableListItemEditor() {
            @Override
            public void editListItem(String listOwnerName, Displayable listItem, DisplayableList displayableList) {
                // Implementation
            }
        };
        listItemViewer = new DisplayableListItemViewer() {
            @Override
            public void viewListItem(Displayable listItem) {
                // Implementation
            }
        };
        listItemDeleter = new DisplayableListItemDeleter() {
            @Override
            public void deleteListItems(String listOwnerName, DisplayableList displayableList) {
                // Implementation
            }
        };
        masterList = new ArrayList<>();
        displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, panelTitleText, allowWriteAccess, allowRecordEditing);
        displayableListPanel.setDisplayableListItemSelector(listItemSelector);
        displayableListPanel.setDisplayableListItemAdder(listItemAdder);
        displayableListPanel.setDisplayableListItemEditor(listItemEditor);
        displayableListPanel.setDisplayableListItemViewer(listItemViewer);
        displayableListPanel.setDisplayableListItemDeleter(listItemDeleter);
        displayableListPanel.setDisplayableItems(masterList);
    }

    @Test
    public void testSetNewRecord() {
        displayableListPanel.setNewRecord(true);
        Assert.assertTrue(displayableListPanel.select.isEnabled());
        Assert.assertFalse(displayableListPanel.add.isEnabled());
        Assert.assertFalse(displayableListPanel.edit.isEnabled());
        Assert.assertFalse(displayableListPanel.view.isEnabled());
        Assert.assertFalse(displayableListPanel.delete.isEnabled());

        displayableListPanel.setNewRecord(false);
        Assert.assertFalse(displayableListPanel.select.isEnabled());
        Assert.assertTrue(displayableListPanel.add.isEnabled());
        Assert.assertTrue(displayableListPanel.edit.isEnabled());
        Assert.assertTrue(displayableListPanel.view.isEnabled());
        Assert.assertTrue(displayableListPanel.delete.isEnabled());
    }

    @Test
    public void testGetPanel() {
        Assert.assertNotNull(displayableListPanel.getPanel());
    }

    @Test
    public void testSetDisplayableItems() {
        ArrayList<Displayable> newMasterList = new ArrayList<>();
        displayableListPanel.setDisplayableItems(newMasterList);
        Assert.assertEquals(newMasterList, displayableListPanel.getAllListItems());
    }

    @Test
    public void testSave() {
        ArrayList<Displayable> newMasterList = new ArrayList<>();
        displayableListPanel.setDisplayableItems(newMasterList);
        displayableListPanel.save();
        Assert.assertEquals(newMasterList, displayableListPanel.getAllListItems());
        Assert.assertEquals(newMasterList, displayableListPanel.masterList);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        // Implementation
    }
}
