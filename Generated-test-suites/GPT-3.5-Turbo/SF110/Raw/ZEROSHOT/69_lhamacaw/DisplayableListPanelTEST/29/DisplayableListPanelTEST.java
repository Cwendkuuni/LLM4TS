import org.junit.Before;
import org.junit.Test;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class DisplayableListPanelTest {
  
  DisplayableListPanel displayableListPanel;
  
  @Before
  public void setUp() {
    displayableListPanel = new DisplayableListPanel(null, null, "", true);
  }
  
  @Test
  public void testSetNewRecord() {
    displayableListPanel.setNewRecord(false);
    boolean isNewRecord = displayableListPanel.isNewRecord;
    assert isNewRecord == false;
    
    displayableListPanel.setNewRecord(true);
    isNewRecord = displayableListPanel.isNewRecord;
    assert isNewRecord == true;
  }
  
  @Test
  public void testBuildUI() {
    displayableListPanel.buildUI();
    assert displayableListPanel.panel != null;
  }
  
  @Test
  public void testSetDisplayableItems() {
    ArrayList<Displayable> masterList = new ArrayList<>();
    masterList.add(new Displayable());
    displayableListPanel.setDisplayableItems(masterList);
    ArrayList<Displayable> displayableItems = displayableListPanel.displayableList.getAllItems();
    assert displayableItems.size() == 1;
    assert displayableItems.get(0) instanceof Displayable;
  }
  
  @Test
  public void testSave() {
    ArrayList<Displayable> masterList = new ArrayList<>();
    masterList.add(new Displayable());
    displayableListPanel.setDisplayableItems(masterList);
    displayableListPanel.save();
    ArrayList<Displayable> allListItems = displayableListPanel.getAllListItems();
    assert allListItems.size() == 1;
    assert allListItems.get(0) instanceof Displayable;
  }
  
  @Test
  public void testActionPerformed() {
    ActionEvent event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "");
    
    displayableListPanel.actionPerformed(event);
    //Expected behavior is no exception thrown
  }
}