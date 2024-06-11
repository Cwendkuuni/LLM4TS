package macaw.util;

import org.junit.Before;
import org.junit.Test;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import java.util.ArrayList;
import macaw.businessLayer.Displayable;
import macaw.system.SessionProperties;
import macaw.system.UserInterfaceFactory;
import macaw.businessLayer.User;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JScrollPane;
import macaw.businessLayer.DisplayableList;

public class DisplayableListPanelTest {

    private DisplayableListPanel displayableListPanel;
    private SessionProperties sessionProperties;
    private DisplayableListParentForm parentForm;
    private String panelTitleText;
    private boolean allowWriteAccess;
    private boolean allowRecordEditing;
    private UserInterfaceFactory userInterfaceFactory;

    @Before
    public void setUp() {
        sessionProperties = new SessionProperties();
        parentForm = new DisplayableListParentForm();
        panelTitleText = "Test Panel";
        allowWriteAccess = true;
        allowRecordEditing = true;
        userInterfaceFactory = new UserInterfaceFactory();
        displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, panelTitleText, allowWriteAccess, allowRecordEditing);
    }

    @Test
    public void testActionPerformed() {
        ActionEvent event = new ActionEvent(new JButton(), 1, "Test Event");
        displayableListPanel.actionPerformed(event);
    }

    @Test
    public void testSetNewRecord() {
        displayableListPanel.setNewRecord(false);
    }

    @Test
    public void testGetPanel() {
        JPanel panel = displayableListPanel.getPanel();
    }

    @Test
    public void testSetDisplayableListItemSelector() {
        DisplayableListItemSelector listItemSelector = new DisplayableListItemSelector();
        displayableListPanel.setDisplayableListItemSelector(listItemSelector);
    }

    @Test
    public void testSetDisplayableListItemAdder() {
        DisplayableListItemAdder listItemAdder = new DisplayableListItemAdder();
        displayableListPanel.setDisplayableListItemAdder(listItemAdder);
    }

    @Test
    public void testSetDisplayableListItemEditor() {
        DisplayableListItemEditor listItemEditor = new DisplayableListItemEditor();
        displayableListPanel.setDisplayableListItemEditor(listItemEditor);
    }

    @Test
    public void testSetDisplayableListItemViewer() {
        DisplayableListItemViewer listItemViewer = new DisplayableListItemViewer();
        displayableListPanel.setDisplayableListItemViewer(listItemViewer);
    }

    @Test
    public void testSetDisplayableListItemDeleter() {
        DisplayableListItemDeleter listItemDeleter = new DisplayableListItemDeleter();
        displayableListPanel.setDisplayableListItemDeleter(listItemDeleter);
    }

    @Test
    public void testAddListItem() {
        displayableListPanel.addListItem();
    }

    @Test
    public void testEditSelectedListItem() {
        displayableListPanel.editSelectedListItem();
    }

    @Test
    public void testViewSelectedListItem() {
        displayableListPanel.viewSelectedListItem();
    }

    @Test
    public void testDeleteSelectedListItems() {
        displayableListPanel.deleteSelectedListItems();
    }

    @Test
    public void testSelectListItems() {
        displayableListPanel.selectListItems();
    }

    @Test
    public void testUpdateButtonStates() {
        displayableListPanel.updateButtonStates();
    }

    @Test
    public void testSetDisplayableItems() {
        ArrayList<Displayable> masterList = new ArrayList<>();
        displayableListPanel.setDisplayableItems(masterList);
    }

    @Test
    public void testSave() {
        displayableListPanel.save();
    }

    @Test
    public void testGetAllListItems() {
        ArrayList<Displayable> allListItems = displayableListPanel.getAllListItems();
    }

    @Test
    public void testSetListOwnerName() {
        displayableListPanel.setListOwnerName("Test Owner");
    }

}