import macaw.util.*;
import macaw.businessLayer.*;
import macaw.system.SessionProperties;
import org.junit.Before;
import org.junit.Test;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class DisplayableListPanelTest {

    private DisplayableListPanel displayableListPanel;
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

    @Before
    public void setUp() {
        this.sessionProperties = new SessionProperties();
        this.parentForm = new DisplayableListParentForm();
        this.panelTitleText = "Panel Title";
        this.allowWriteAccess = true;
        this.allowRecordEditing = true;
        this.listItemSelector = new DisplayableListItemSelector();
        this.listItemAdder = new DisplayableListItemAdder();
        this.listItemEditor = new DisplayableListItemEditor();
        this.listItemViewer = new DisplayableListItemViewer();
        this.listItemDeleter = new DisplayableListItemDeleter();
        this.masterList = new ArrayList<Displayable>();
        this.displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, panelTitleText, allowWriteAccess, allowRecordEditing);
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
        assertTrue(displayableListPanel.add.isEnabled());
        assertFalse(displayableListPanel.delete.isEnabled());
        assertFalse(displayableListPanel.edit.isEnabled());
        assertFalse(displayableListPanel.view.isEnabled());
        assertFalse(displayableListPanel.select.isEnabled());

        displayableListPanel.setNewRecord(false);
        assertTrue(displayableListPanel.add.isEnabled());
        assertTrue(displayableListPanel.select.isEnabled());
        assertFalse(displayableListPanel.delete.isEnabled());
        if(displayableListPanel.displayableList.isEmpty()){
            assertFalse(displayableListPanel.edit.isEnabled());
        }else{
            assertTrue(displayableListPanel.edit.isEnabled());
        }
        if(displayableListPanel.displayableList.isEmpty()){
            assertFalse(displayableListPanel.view.isEnabled());
        }else{
            assertTrue(displayableListPanel.view.isEnabled());
        }
    }

    @Test
    public void testGetPanel() {
        assertNotNull(displayableListPanel.getPanel());
    }

    @Test
    public void testSetDisplayableListItemSelector() {
        displayableListPanel.setDisplayableListItemSelector(listItemSelector);
        assertEquals(listItemSelector, displayableListPanel.listItemSelector);
    }

    @Test
    public void testSetDisplayableListItemAdder() {
        displayableListPanel.setDisplayableListItemAdder(listItemAdder);
        assertEquals(listItemAdder, displayableListPanel.listItemAdder);
    }

    @Test
    public void testSetDisplayableListItemEditor() {
        displayableListPanel.setDisplayableListItemEditor(listItemEditor);
        assertEquals(listItemEditor, displayableListPanel.listItemEditor);
    }

    @Test
    public void testSetDisplayableListItemViewer() {
        displayableListPanel.setDisplayableListItemViewer(listItemViewer);
        assertEquals(listItemViewer, displayableListPanel.listItemViewer);
    }

    @Test
    public void testSetDisplayableListItemDeleter() {
        displayableListPanel.setDisplayableListItemDeleter(listItemDeleter);
        assertEquals(listItemDeleter, displayableListPanel.listItemDeleter);
    }

    @Test
    public void testAddListItem() {
        displayableListPanel.addListItem();
        //Test addListItem logic here
    }

    @Test
    public void testEditSelectedListItem() {
        displayableListPanel.editSelectedListItem();
        //Test editSelectedListItem logic here
    }

    @Test
    public void testViewSelectedListItem() {
        displayableListPanel.viewSelectedListItem();
        //Test viewSelectedListItem logic here
    }

    @Test
    public void testDeleteSelectedListItems() {
        displayableListPanel.deleteSelectedListItems();
        //Test deleteSelectedListItems logic here
    }

    @Test
    public void testSelectListItems() {
        displayableListPanel.selectListItems();
        //Test selectListItems logic here
    }

    @Test
    public void testUpdateButtonStates() {
        displayableListPanel.updateButtonStates();
        //Test updateButtonStates logic here
    }

    @Test
    public void testSetDisplayableItems() {
        ArrayList<Displayable> newMasterList = new ArrayList<Displayable>();
        displayableListPanel.setDisplayableItems(newMasterList);
        assertEquals(newMasterList, displayableListPanel.masterList);
    }

    @Test
    public void testSave() {
        displayableListPanel.save();
        //Test save logic here
    }

    @Test
    public void testGetAllListItems() {
        ArrayList<Displayable> allListItems = displayableListPanel.getAllListItems();
        assertEquals(masterList, allListItems);
    }

    @Test
    public void testSetListOwnerName() {
        String listOwnerName = "List Owner";
        displayableListPanel.setListOwnerName(listOwnerName);
        assertEquals(listOwnerName, displayableListPanel.listOwnerName);
    }

    @Test
    public void testActionPerformed() {
        ActionEvent event = new ActionEvent(this, 1, "Button Pressed");
        displayableListPanel.actionPerformed(event);
        //Test actionPerformed logic here
    }
}