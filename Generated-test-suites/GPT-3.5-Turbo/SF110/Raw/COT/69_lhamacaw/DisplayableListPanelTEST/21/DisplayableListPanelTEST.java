
import org.junit.Test;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import static org.junit.Assert.*;

public class DisplayableListPanelTest {

    @Test
    public void testSetNewRecord() {
        SessionProperties sessionProperties = new SessionProperties();
        DisplayableListParentForm parentForm = new DisplayableListParentForm();
        String panelTitleText = "Test Panel";
        boolean allowWriteAccess = true;
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, panelTitleText, allowWriteAccess);
        
        displayableListPanel.setNewRecord(true);
        assertTrue(displayableListPanel.isNewRecord);
        displayableListPanel.setNewRecord(false);
        assertFalse(displayableListPanel.isNewRecord);
    }
    
    @Test
    public void testGetPanel() {
        SessionProperties sessionProperties = new SessionProperties();
        DisplayableListParentForm parentForm = new DisplayableListParentForm();
        String panelTitleText = "Test Panel";
        boolean allowWriteAccess = true;
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, panelTitleText, allowWriteAccess);
        
        JPanel panel = displayableListPanel.getPanel();
        assertNotNull(panel);
    }
    
    @Test
    public void testSetDisplayableItems() {
        DisplayableListPanel displayableListPanel = new DisplayableListPanel();
        ArrayList<Displayable> masterList = new ArrayList<Displayable>();
        Displayable item1 = new Displayable("Item 1");
        Displayable item2 = new Displayable("Item 2");
        
        masterList.add(item1);
        displayableListPanel.setDisplayableItems(masterList);
        ArrayList<Displayable> resultList = displayableListPanel.getAllListItems();
        
        assertEquals(masterList.size(), resultList.size());
        assertTrue(resultList.contains(item1));
        
        masterList.add(item2);
        displayableListPanel.setDisplayableItems(masterList);
        resultList = displayableListPanel.getAllListItems();
        
        assertEquals(masterList.size(), resultList.size());
        assertTrue(resultList.contains(item2));
    }
    
    @Test
    public void testSave() {
        DisplayableListPanel displayableListPanel = new DisplayableListPanel();
        ArrayList<Displayable> masterList = new ArrayList<Displayable>();
        Displayable item1 = new Displayable("Item 1");
        Displayable item2 = new Displayable("Item 2");
        
        displayableListPanel.setDisplayableItems(masterList);
        displayableListPanel.save();
        assertTrue(displayableListPanel.masterList.isEmpty());
        
        masterList.add(item1);
        masterList.add(item2);
        displayableListPanel.setDisplayableItems(masterList);
        displayableListPanel.save();
        assertEquals(masterList.size(), displayableListPanel.masterList.size());
    }
    
    @Test
    public void testActionPerformed() {
        // TODO: Include test cases for each action event
        
    }

    // TODO: Add test cases for other public methods of DisplayableListPanel

}
