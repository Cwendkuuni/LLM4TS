package macaw.util;

import macaw.businessLayer.*;
import java.awt.*;
import macaw.system.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;
import org.junit.Before;
import org.junit.Test;

public class DisplayableListPanelTest {

    private DisplayableListPanel displayableListPanel;

    @Before
    public void setUp() {
        SessionProperties sessionProperties = new SessionProperties();
        DisplayableListParentForm parentForm = new DisplayableListParentForm();
        String panelTitleText = "Test Panel";
        boolean allowWriteAccess = true;
        boolean allowRecordEditing = true;
        displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, panelTitleText, allowWriteAccess, allowRecordEditing);
    }

    @Test
    public void testGetPanel() {
        JPanel panel = displayableListPanel.getPanel();
        assertNotNull(panel);
    }

    @Test
    public void testSetDisplayableListItemSelector() {
        assertNotNull(displayableListPanel);
        DisplayableListItemSelector listItemSelector = new DisplayableListItemSelector();
        displayableListPanel.setDisplayableListItemSelector(listItemSelector);
    }

    @Test
    public void testSetDisplayableListItemAdder() {
        assertNotNull(displayableListPanel);
        DisplayableListItemAdder listItemAdder = new DisplayableListItemAdder();
        displayableListPanel.setDisplayableListItemAdder(listItemAdder);
    }

    @Test
    public void testSetDisplayableListItemEditor() {
        assertNotNull(displayableListPanel);
        DisplayableListItemEditor listItemEditor = new DisplayableListItemEditor();
        displayableListPanel.setDisplayableListItemEditor(listItemEditor);
    }

    @Test
    public void testSetDisplayableListItemViewer() {
        assertNotNull(displayableListPanel);
        DisplayableListItemViewer listItemViewer = new DisplayableListItemViewer();
        displayableListPanel.setDisplayableListItemViewer(listItemViewer);
    }

    @Test
    public void testSetDisplayableListItemDeleter() {
        assertNotNull(displayableListPanel);
        DisplayableListItemDeleter listItemDeleter = new DisplayableListItemDeleter();
        displayableListPanel.setDisplayableListItemDeleter(listItemDeleter);
    }

    @Test
    public void testAddListItem() {
        assertNotNull(displayableListPanel);
        displayableListPanel.addListItem();
        displayableListPanel.setNewRecord(false);
        displayableListPanel.addListItem();
    }

    @Test
    public void testDeleteSelectedListItems() {
        assertNotNull(displayableListPanel);
        displayableListPanel.deleteSelectedListItems();
    }

    @Test
    public void testUpdateButtonStates() {
        assertNotNull(displayableListPanel);
        displayableListPanel.updateButtonStates();
    }

    @Test
    public void testSetDisplayableItems() {
        assertNotNull(displayableListPanel);
        ArrayList<Displayable> masterList = new ArrayList<>();
        displayableListPanel.setDisplayableItems(masterList);
    }

    @Test
    public void testSave() {
        assertNotNull(displayableListPanel);
        displayableListPanel.save();
    }

    @Test
    public void testGetAllListItems() {
        assertNotNull(displayableListPanel);
        ArrayList<Displayable> listItems = displayableListPanel.getAllListItems();
        assertNotNull(listItems);
    }

    @Test
    public void testSetListOwnerName() {
        assertNotNull(displayableListPanel);
        String listOwnerName = "Test Owner";
        displayableListPanel.setListOwnerName(listOwnerName);
    }

    @Test
    public void testActionPerformed() {
        assertNotNull(displayableListPanel);
        ActionEvent event = new ActionEvent(new JButton(), 0, "Test Event");
        displayableListPanel.actionPerformed(event);
    }
}