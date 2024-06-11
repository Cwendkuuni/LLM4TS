import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class DisplayableListPanelTest {

  @Test
  public void testSetNewRecord() {
    DisplayableListPanel panel = new DisplayableListPanel(null, null, null, false);
    panel.setNewRecord(true);
    assertTrue(panel.isNewRecord);

    panel.setNewRecord(false);
    assertFalse(panel.isNewRecord);
  }

  @Test
  public void testUpdateButtonStates() {
    DisplayableListPanel panel = new DisplayableListPanel(null, null, null, false);
    panel.setNewRecord(false);

    panel.updateButtonStates();
    assertFalse(panel.add.isEnabled());
    assertFalse(panel.delete.isEnabled());
    assertTrue(panel.view.isEnabled());

    panel.setNewRecord(true);
    panel.updateButtonStates();
    assertFalse(panel.add.isEnabled());
    assertFalse(panel.delete.isEnabled());
    assertFalse(panel.view.isEnabled());

    panel.setNewRecord(false);
    panel.displayableList.setDisplayItems(new ArrayList<Displayable>());
    panel.updateButtonStates();
    assertFalse(panel.add.isEnabled());
    assertFalse(panel.delete.isEnabled());
    assertFalse(panel.view.isEnabled());

    panel.displayableList.setDisplayItems(Arrays.asList(new Displayable()));
    panel.updateButtonStates();
    assertTrue(panel.add.isEnabled());
    assertFalse(panel.delete.isEnabled());
    assertTrue(panel.view.isEnabled());

    panel.allowWriteAccess = true;
    panel.updateButtonStates();
    assertTrue(panel.add.isEnabled());
    assertFalse(panel.delete.isEnabled());
    assertFalse(panel.view.isEnabled());

    panel.allowRecordEditing = true;
    panel.updateButtonStates();
    assertTrue(panel.add.isEnabled());
    assertFalse(panel.delete.isEnabled());
    assertTrue(panel.edit.isEnabled());

    panel.displayableList.setDisplayItems(new ArrayList<Displayable>(Arrays.asList(new Displayable())));
    panel.updateButtonStates();
    assertTrue(panel.add.isEnabled());
    assertTrue(panel.delete.isEnabled());
    assertTrue(panel.edit.isEnabled());

    panel.allowRecordEditing = false;
    panel.updateButtonStates();
    assertTrue(panel.add.isEnabled());
    assertTrue(panel.delete.isEnabled());
    assertFalse(panel.edit.isEnabled());

    panel.allowWriteAccess = false;
    panel.updateButtonStates();
    assertFalse(panel.add.isEnabled());
    assertFalse(panel.delete.isEnabled());
    assertTrue(panel.view.isEnabled());
  }

  @Test
  public void testSetDisplayableItems() {
    DisplayableListPanel panel = new DisplayableListPanel(null, null, null, false);
    ArrayList<Displayable> masterList = new ArrayList<>();
    panel.setDisplayableItems(masterList);
    assertEquals(masterList, panel.masterList);
    assertEquals(masterList, panel.displayableList.getAllItems());
    panel.allowWriteAccess = true;
    panel.updateButtonStates();

    masterList.add(new Displayable());
    panel.setDisplayableItems(masterList);
    assertEquals(masterList, panel.masterList);
    assertEquals(masterList, panel.displayableList.getAllItems());
    panel.allowWriteAccess = true;
    panel.updateButtonStates();
  }

  @Test
  public void testDeleteSelectedListItems() {
    DisplayableListPanel panel = new DisplayableListPanel(null, null, null, false);
    panel.listItemDeleter = null;
    panel.displayableList.setDisplayItems(new ArrayList<>());

    assertNull(panel.listItemDeleter);
    assertFalse(panel.deleteSelectedListItems());
  }

  @Test
  public void testAddListItem() {
    DisplayableListPanel panel = new DisplayableListPanel(null, null, null, false);
    panel.allowWriteAccess = false;

    panel.listItemAdder = null;
    assertFalse(panel.addListItem());

    panel.allowWriteAccess = true;
    panel.listItemAdder = null;
    assertFalse(panel.addListItem());

    panel.listItemAdder = new DummyListItemAdder();
    assertTrue(panel.addListItem());
  }

  class DummyListItemAdder implements DisplayableListItemAdder {

    @Override
    public void addItems(String listOwnerName, DisplayableList displayableList) {
      // Dummy implementation
    }
  }
}