import macaw.businessLayer.Displayable;
import macaw.businessLayer.User;
import macaw.system.MacawException;
import macaw.system.MacawMessages;
import macaw.system.SessionProperties;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class DisplayableListPanelTest {
    
    private DisplayableListPanel displayableListPanel;
    private SessionProperties sessionProperties;
    private String panelTitleText = "Test Panel";
    
    @Before
    public void setup() {
        sessionProperties = new SessionProperties();
        displayableListPanel = new DisplayableListPanel(sessionProperties, null, panelTitleText, true);
    }
    
    @Test
    public void testGetPanel() {
        JPanel panel = displayableListPanel.getPanel();
        assertEquals(panelTitleText, panel.getName());
    }
    
    @Test
    public void testSetNewRecord() {
        displayableListPanel.setNewRecord(true);
        assertTrue(displayableListPanel.isNewRecord);
    }
    
    @Test
    public void testSetDisplayableItems() {
        ArrayList<Displayable> masterList = new ArrayList<>();
        Displayable displayable = new Displayable();
        masterList.add(displayable);
        
        displayableListPanel.setDisplayableItems(masterList);
        assertEquals(masterList, displayableListPanel.getAllListItems());
    }
    
    @Test
    public void testAddListItem_withException() {
        try {
            displayableListPanel.addListItem();
        } catch (MacawException e) {
            assertEquals(MacawErrorType.MISSING_DISPLAY_LIST_ADDER, e.getErrorType());
        }
    }
    
    @Test
    public void testEditSelectedListItem_withErrorMessage() {
        JLabel logLabel = new JLabel();
        sessionProperties.setProperty("log", logLabel);
        
        displayableListPanel.editSelectedListItem();
        assertEquals(MacawMessages.getMessage("general.error.unselectedItems"), logLabel.getText());
    }
    
    @Test
    public void testViewSelectedListItem_withException() {
        try {
            displayableListPanel.viewSelectedListItem();
        } catch (MacawException e) {
            assertEquals(MacawErrorType.MISSING_DISPLAY_LIST_SELECTOR, e.getErrorType());
        }
    }
    
    @Test
    public void testDeleteSelectedListItems_withErrorMessage() {
        JLabel logLabel = new JLabel();
        sessionProperties.setProperty("log", logLabel);
        
        displayableListPanel.deleteSelectedListItems();
        assertEquals(MacawMessages.getMessage("general.error.unselectedItems"), logLabel.getText());
    }
    
    @Test
    public void testSelectListItems_withException() {
        try {
            displayableListPanel.selectListItems();
        } catch (MacawException e) {
            assertEquals(e.getMessage(), logLabel.getText());
        }
    }

    // Add more test cases for other methods as per requirements

}