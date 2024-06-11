
package macaw.util;

import static org.junit.Assert.*;
import org.junit.Test;
import macaw.businessLayer.*;
import java.awt.*;
import macaw.system.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;

public class DisplayableListPanelTest {
  
    @Test
    public void testSetNewRecord() {
        DisplayableListPanel panel = new DisplayableListPanel(null, null, "", true);
        panel.setNewRecord(true);
        assertTrue(panel.add.isEnabled() == false);
        assertTrue(panel.delete.isEnabled() == false);
        assertTrue(panel.edit.isEnabled() == false);
        assertTrue(panel.view.isEnabled() == false);
        assertTrue(panel.select.isEnabled() == false);
        
        panel.setNewRecord(false);
        assertTrue(panel.add.isEnabled() == true);
        assertTrue(panel.delete.isEnabled() == true);
        assertTrue(panel.edit.isEnabled() == true);
        assertTrue(panel.view.isEnabled() == true);
        assertTrue(panel.select.isEnabled() == true);
    }
  
    @Test
    public void testGetPanel() {
        DisplayableListPanel panel = new DisplayableListPanel(null, null, "", true);
        
        JPanel result = panel.getPanel();
        assertNotNull(result);
    }
  
    @Test
    public void testUpdateButtonStates() {
        DisplayableListPanel panel = new DisplayableListPanel(null, null, "", true);
        
        panel.setNewRecord(true);
        panel.updateButtonStates();
        assertTrue(panel.add.isEnabled() == false);
        assertTrue(panel.delete.isEnabled() == false);
        assertTrue(panel.edit.isEnabled() == false);
        assertTrue(panel.view.isEnabled() == false);
        assertTrue(panel.select.isEnabled() == false);
        
        panel.setNewRecord(false);
        panel.updateButtonStates();
        assertTrue(panel.add.isEnabled() == true);
        assertTrue(panel.delete.isEnabled() == false);
        assertTrue(panel.edit.isEnabled() == false);
        assertTrue(panel.view.isEnabled() == false);
        assertTrue(panel.select.isEnabled() == true);
        
        panel.setNewRecord(false);
        panel.displayableList.setDisplayItems(new ArrayList<Displayable>());
        panel.updateButtonStates();
        assertTrue(panel.add.isEnabled() == true);
        assertTrue(panel.delete.isEnabled() == false);
        assertTrue(panel.edit.isEnabled() == false);
        assertTrue(panel.view.isEnabled() == false);
        assertTrue(panel.select.isEnabled() == true);
        
        panel.setNewRecord(false);
        panel.displayableList.setDisplayItems(Arrays.asList(new Displayable()));
        panel.updateButtonStates();
        assertTrue(panel.add.isEnabled() == true);
        assertTrue(panel.delete.isEnabled() == true);
        assertTrue(panel.edit.isEnabled() == true);
        assertTrue(panel.view.isEnabled() == true);
        assertTrue(panel.select.isEnabled() == true);
    }
  
    @Test
    public void testSetDisplayableItems() {
        DisplayableListPanel panel = new DisplayableListPanel(null, null, "", true);
        
        ArrayList<Displayable> list = Arrays.asList(new Displayable(), new Displayable());
        panel.setDisplayableItems(list);
        assertEquals(list, panel.getAllListItems());
    }
  
    @Test
    public void testSave() {
        DisplayableListPanel panel = new DisplayableListPanel(null, null, "", true);
        
        ArrayList<Displayable> list = Arrays.asList(new Displayable(), new Displayable());
        panel.setDisplayableItems(list);
        panel.save();
        assertEquals(list, panel.masterList);
    }
  
    @Test
    public void testGetAllListItems() {
        DisplayableListPanel panel = new DisplayableListPanel(null, null, "", true);
        
        ArrayList<Displayable> list = Arrays.asList(new Displayable(), new Displayable());
        panel.setDisplayableItems(list);
        assertEquals(list, panel.getAllListItems());
    }
  
    @Test
    public void testSetListOwnerName() {
        DisplayableListPanel panel = new DisplayableListPanel(null, null, "", true);
        
        panel.setListOwnerName("owner");
        
        assertEquals("owner", panel.listOwnerName);
    }
  
    @Test
    public void testActionPerformed() {
        // Need to add test cases for each action event
        // The methods for each action event are already tested above
    }
}
