package macaw.util;

import macaw.businessLayer.Displayable;
import macaw.testLayer.DisplayableListItemAdder;
import macaw.testLayer.DisplayableListItemDeleter;
import macaw.testLayer.DisplayableListItemEditor;
import macaw.testLayer.DisplayableListItemSelector;
import macaw.testLayer.DisplayableListItemViewer;
import macaw.testLayer.DisplayableListParentForm;
import macaw.testLayer.MacawErrorType;
import macaw.testLayer.MacawException;
import macaw.testLayer.MacawMessages;
import macaw.testLayer.SessionProperties;
import macaw.testLayer.User;
import macaw.testLayer.UserInterfaceFactory;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class DisplayableListPanelTest {

    private SessionProperties sessionProperties;
    private DisplayableListParentForm parentForm;
    private UserInterfaceFactory userInterfaceFactory;
    private DisplayableListItemSelector listItemSelector;
    private DisplayableListItemAdder listItemAdder;
    private DisplayableListItemEditor listItemEditor;
    private DisplayableListItemViewer listItemViewer;
    private DisplayableListItemDeleter listItemDeleter;
    private Log log;
    
    @Before
    public void setup() {
        sessionProperties = Mockito.mock(SessionProperties.class);
        parentForm = Mockito.mock(DisplayableListParentForm.class);
        userInterfaceFactory = Mockito.mock(UserInterfaceFactory.class);
        listItemSelector = Mockito.mock(DisplayableListItemSelector.class);
        listItemAdder = Mockito.mock(DisplayableListItemAdder.class);
        listItemEditor = Mockito.mock(DisplayableListItemEditor.class);
        listItemViewer = Mockito.mock(DisplayableListItemViewer.class);
        listItemDeleter = Mockito.mock(DisplayableListItemDeleter.class);
        log = Mockito.mock(Log.class);
        
        Properties properties = new Properties();
        properties.put("currentUser", new User());
        properties.put("log", log);
        
        Mockito.when(sessionProperties.getProperty(Mockito.anyString())).thenReturn(properties);
        Mockito.when(sessionProperties.getLog()).thenReturn(log);
        Mockito.when(sessionProperties.getUserInterfaceFactory()).thenReturn(userInterfaceFactory);
    }

    @Test
    public void testSetNewRecord() {
        boolean isNewRecord = true;
        createDisplayableListPanel().setNewRecord(isNewRecord);
    }

    @Test
    public void testGetPanel() {
        createDisplayableListPanel().getPanel();
    }

    @Test
    public void testSetDisplayableListItemSelector() {
        createDisplayableListPanel().setDisplayableListItemSelector(listItemSelector);
    }

    @Test
    public void testSetDisplayableListItemAdder() {
        createDisplayableListPanel().setDisplayableListItemAdder(listItemAdder);
    }

    @Test
    public void testSetDisplayableListItemEditor() {
        createDisplayableListPanel().setDisplayableListItemEditor(listItemEditor);
    }

    @Test
    public void testSetDisplayableListItemViewer() {
        createDisplayableListPanel().setDisplayableListItemViewer(listItemViewer);
    }

    @Test
    public void testSetDisplayableListItemDeleter() {
        createDisplayableListPanel().setDisplayableListItemDeleter(listItemDeleter);
    }

    @Test
    public void testAddListItem() throws MacawException {
        createDisplayableListPanel().addListItem();
    }

    @Test
    public void testDeleteSelectedListItems() throws MacawException {
        createDisplayableListPanel().deleteSelectedListItems();
    }

    @Test
    public void testSelectListItems() throws MacawException {
        createDisplayableListPanel().selectListItems();
    }

    @Test
    public void testUpdateButtonStates() {
        createDisplayableListPanel().updateButtonStates();
    }

    @Test
    public void testSetDisplayableItems() {
        ArrayList<Displayable> masterList = new ArrayList<>();
        createDisplayableListPanel().setDisplayableItems(masterList);
    }
    
    private DisplayableListPanel createDisplayableListPanel() {
        return new DisplayableListPanel(sessionProperties, parentForm, "Test", true);
    }
}