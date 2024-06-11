package macaw.util;

import org.junit.*;
import static org.junit.Assert.*;
import macaw.businessLayer.*;
import java.awt.*;
import macaw.system.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;

public class DisplayableListPanelTest {
  private DisplayableListPanel displayableListPanel;
  private SessionProperties sessionProperties;
  private DisplayableListParentForm parentForm;
  private DisplayableListItemSelector listItemSelector;
  private DisplayableListItemAdder listItemAdder;
  private DisplayableListItemEditor listItemEditor;
  private DisplayableListItemViewer listItemViewer;
  private DisplayableListItemDeleter listItemDeleter;
  
  @Before
  public void setUp() {
    sessionProperties = new SessionProperties();
    parentForm = new DisplayableListParentForm();
    displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Title", true);
    listItemSelector = new DisplayableListItemSelector();
    listItemAdder = new DisplayableListItemAdder();
    listItemEditor = new DisplayableListItemEditor();
    listItemViewer = new DisplayableListItemViewer();
    listItemDeleter = new DisplayableListItemDeleter();
    
    displayableListPanel.setDisplayableListItemSelector(listItemSelector);
    displayableListPanel.setDisplayableListItemAdder(listItemAdder);
    displayableListPanel.setDisplayableListItemEditor(listItemEditor);
    displayableListPanel.setDisplayableListItemViewer(listItemViewer);
    displayableListPanel.setDisplayableListItemDeleter(listItemDeleter);
  }
  
  @Test
  public void testSetNewRecord() {
    displayableListPanel.setNewRecord(true);
    assertFalse(displayableListPanel.add.isEnabled());
    assertFalse(displayableListPanel.delete.isEnabled());
    assertFalse(displayableListPanel.edit.isEnabled());
    assertFalse(displayableListPanel.view.isEnabled());
    assertFalse(displayableListPanel.select.isEnabled());
    
    displayableListPanel.setNewRecord(false);
    assertTrue(displayableListPanel.add.isEnabled());
    assertTrue(displayableListPanel.select.isEnabled());
  }
  
  @Test
  public void testGetPanel() {
    JPanel panel = displayableListPanel.getPanel();
    assertTrue(panel instanceof JPanel);
  }
  
  @Test
  public void testUpdateButtonStates() {
    displayableListPanel.setDisplayableItems(new ArrayList<>());
    assertFalse(displayableListPanel.view.isEnabled());
    
    displayableListPanel.setNewRecord(true);
    displayableListPanel.updateButtonStates();
    assertFalse(displayableListPanel.edit.isEnabled());
    assertFalse(displayableListPanel.view.isEnabled());
    
    displayableListPanel.setNewRecord(false);
    displayableListPanel.updateButtonStates();
    assertTrue(displayableListPanel.edit.isEnabled());
    assertTrue(displayableListPanel.view.isEnabled());
    
    displayableListPanel.setDisplayableItems(Arrays.asList(new Displayable()));
    displayableListPanel.updateButtonStates();
    assertTrue(displayableListPanel.view.isEnabled());
    
    displayableListPanel.setAllowWriteAccess(false);
    displayableListPanel.updateButtonStates();
    assertFalse(displayableListPanel.add.isEnabled());
    assertFalse(displayableListPanel.delete.isEnabled());
    assertFalse(displayableListPanel.edit.isEnabled());
    assertTrue(displayableListPanel.view.isEnabled());
  }
  
  @Test
  public void testSetDisplayableItems() {
    ArrayList<Displayable> items = new ArrayList<>(Arrays.asList(new Displayable()));
    displayableListPanel.setDisplayableItems(items);
    assertEquals(items, displayableListPanel.getAllListItems());
  }
  
  @Test
  public void testSave() {
    ArrayList<Displayable> items = new ArrayList<>(Arrays.asList(new Displayable()));
    displayableListPanel.setDisplayableItems(items);
    displayableListPanel.save();
    assertEquals(items, displayableListPanel.getAllListItems());
  }
  
  @Test
  public void testSetListOwnerName() {
    displayableListPanel.setListOwnerName("Owner");
    assertEquals("Owner", displayableListPanel.listOwnerName);
  }
  
  @Test
  public void testActionPerformed() {
    JButton button = new JButton();
    
    displayableListPanel.select = button;
    displayableListPanel.actionPerformed(new ActionEvent(button, ActionEvent.ACTION_PERFORMED, ""));
    assertTrue(listItemSelector.isSelectListItemsCalled());
    
    displayableListPanel.add = button;
    displayableListPanel.actionPerformed(new ActionEvent(button, ActionEvent.ACTION_PERFORMED, ""));
    assertTrue(listItemAdder.isAddItemsCalled());
    
    displayableListPanel.edit = button;
    displayableListPanel.actionPerformed(new ActionEvent(button, ActionEvent.ACTION_PERFORMED, ""));
    assertTrue(listItemEditor.isEditListItemCalled());
    
    displayableListPanel.view = button;
    displayableListPanel.actionPerformed(new ActionEvent(button, ActionEvent.ACTION_PERFORMED, ""));
    assertTrue(listItemViewer.isViewListItemCalled());
    
    displayableListPanel.delete = button;
    displayableListPanel.actionPerformed(new ActionEvent(button, ActionEvent.ACTION_PERFORMED, ""));
    assertTrue(listItemDeleter.isDeleteListItemsCalled());
  }
}