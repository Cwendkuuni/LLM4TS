import macaw.util.DisplayableListPanel;
import macaw.system.*;
import macaw.businessLayer.Displayable;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class DisplayableListPanelTest {

    private SessionProperties sessionProperties;
    private DisplayableListParentForm parentForm;
    private DisplayableListPanel displayableListPanel;

    @Before
    public void setUp() {
        sessionProperties = new SessionProperties();
        parentForm = new DisplayableListParentForm();
        displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true);
    }

    @Test
    public void testSetNewRecord() {
        displayableListPanel.setNewRecord(true);
        assertTrue(displayableListPanel.select.isEnabled());
        assertTrue(displayableListPanel.add.isEnabled());
        assertTrue(displayableListPanel.edit.isEnabled());
        assertTrue(displayableListPanel.view.isEnabled());
        assertTrue(displayableListPanel.delete.isEnabled());

        displayableListPanel.setNewRecord(false);
        assertTrue(displayableListPanel.select.isEnabled());
        assertTrue(displayableListPanel.add.isEnabled());
        assertTrue(displayableListPanel.edit.isEnabled());
        assertTrue(displayableListPanel.view.isEnabled());
        assertTrue(displayableListPanel.delete.isEnabled());
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
    public void testAddListItem() {
        displayableListPanel.setDisplayableListItemAdder(new DisplayableListItemAdder());
        displayableListPanel.addListItem();
        // Assert the success condition
    }

    @Test
    public void testEditSelectedListItem() {
        displayableListPanel.setDisplayableListItemEditor(new DisplayableListItemEditor());
        displayableListPanel.editSelectedListItem();
        // Assert the success condition
    }

    @Test
    public void testViewSelectedListItem() {
        displayableListPanel.setDisplayableListItemViewer(new DisplayableListItemViewer());
        displayableListPanel.viewSelectedListItem();
        // Assert the success condition
    }

    @Test
    public void testDeleteSelectedListItems() {
        displayableListPanel.setDisplayableListItemDeleter(new DisplayableListItemDeleter());
        displayableListPanel.deleteSelectedListItems();
        // Assert the success condition
    }

    @Test
    public void testSelectListItems() {
        displayableListPanel.setDisplayableListItemSelector(new DisplayableListItemSelector());
        displayableListPanel.selectListItems();
        // Assert the success condition
    }

    @Test
    public void testUpdateButtonStates() {
        displayableListPanel.updateButtonStates();
        // Assert the success condition
    }

    @Test
    public void testSetDisplayableItems() {
        displayableListPanel.setDisplayableItems(new ArrayList<Displayable>());
        // Assert the success condition
    }

    @Test
    public void testSave() {
        displayableListPanel.save();
        // Assert the success condition
    }

    @Test
    public void testGetAllListItems() {
        ArrayList<Displayable> items = displayableListPanel.getAllListItems();
        assertNotNull(items);
    }

    @Test
    public void testSetListOwnerName() {
        displayableListPanel.setListOwnerName("OwnerName");
        assertNotNull(displayableListPanel.listOwnerName);
    }

    @Test
    public void testActionPerformed() {
        ActionEvent event = new ActionEvent(new JButton(), 1, "Command");
        displayableListPanel.actionPerformed(event);
        // Assert the success condition
    }
}