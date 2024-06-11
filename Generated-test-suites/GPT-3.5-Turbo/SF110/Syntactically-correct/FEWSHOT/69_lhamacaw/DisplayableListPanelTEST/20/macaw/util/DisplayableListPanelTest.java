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
    private SessionProperties sessionProperties;
    private DisplayableListParentForm parentForm;
    private DisplayableListPanel displayableListPanel;
    
    @Before
    public void setUp() {
        sessionProperties = new SessionProperties();
        parentForm = new DisplayableListParentForm();
        String panelTitleText = "Test Panel";
        boolean allowWriteAccess = true;
        displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, panelTitleText, allowWriteAccess);
        ArrayList<Displayable> masterList = new ArrayList<Displayable>();
        displayableListPanel.setDisplayableItems(masterList);
    }
    
    @Test
    public void testSetNewRecord() {
        displayableListPanel.setNewRecord(true);
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
    public void testUpdateButtonStates() {
        displayableListPanel.updateButtonStates();
    }
    
    @Test
    public void testSetDisplayableItems() {
        ArrayList<Displayable> masterList = new ArrayList<Displayable>();
        displayableListPanel.setDisplayableItems(masterList);
    }
    
    @Test
    public void testSave() {
        displayableListPanel.save();
    }
    
    @Test
    public void testGetAllListItems() {
        ArrayList<Displayable> listItems = displayableListPanel.getAllListItems();
    }
    
    @Test
    public void testSetListOwnerName() {
        String listOwnerName = "Test Owner";
        displayableListPanel.setListOwnerName(listOwnerName);
    }
    
    @Test
    public void testActionPerformed() {
        ActionEvent event = new ActionEvent(new JButton(), 0, "");
        displayableListPanel.actionPerformed(event);
    }
}