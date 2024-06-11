import static org.junit.Assert.*;
import org.junit.Test;
import java.awt.Component;
import java.io.File;
import javax.swing.JTabbedPane;
import ghm.follow.config.FollowAppAttributes;
import ghm.follow.gui.FileFollowingPane;
import ghm.follow.gui.TabbedPane;

public class TabbedPaneTest {
  
  private FollowAppAttributes attributes;
  private TabbedPane tabbedPane;
  
  @Before
  public void setUp() {
    attributes = new FollowAppAttributes();
    tabbedPane = new TabbedPane(attributes);
  }
  
  @Test
  public void testFindComponentAt(){
    // Typical case: x and y are within the bounds of the TabbedPane
    Component expected1 = tabbedPane.getComponentAt(0);
    Component actual1 = tabbedPane.findComponentAt(10, 10);
    assertEquals(expected1, actual1);
    
    // Edge case: x and y are on the boundary of the TabbedPane
    Component expected2 = tabbedPane.getComponentAt(0);
    Component actual2 = tabbedPane.findComponentAt(tabbedPane.getWidth(), tabbedPane.getHeight());
    assertEquals(expected2, actual2);
    
    // Error scenario: x and y are outside the bounds of the TabbedPane
    Component actual3 = tabbedPane.findComponentAt(-10, -10);
    assertNull(actual3);
  }
  
  @Test
  public void testSetSelectedIndex(){
    // Typical case: Selecting an existing index
    int index1 = 1;
    tabbedPane.addTab("Tab 1", new JPanel());
    tabbedPane.addTab("Tab 2", new JPanel());
    tabbedPane.setSelectedIndex(index1);
    assertEquals(index1, tabbedPane.getSelectedIndex());
    
    // Error scenario: Selecting an invalid index
    int index2 = -1;
    tabbedPane.setSelectedIndex(index2);
    assertEquals(index1, tabbedPane.getSelectedIndex());
  }
  
  @Test
  public void testSetSelectedComponent(){
    // Typical case: Selecting an existing FileFollowingPane as the selected component
    FileFollowingPane pane1 = new FileFollowingPane(new File("file1.txt"));
    tabbedPane.addTab("Tab 1", pane1);
    tabbedPane.setSelectedComponent(pane1);
    assertEquals(pane1, tabbedPane.getSelectedComponent());
    
    // Error scenario: Selecting a non-existing FileFollowingPane as the selected component
    FileFollowingPane pane2 = new FileFollowingPane(new File("file2.txt"));
    tabbedPane.setSelectedComponent(pane2);
    assertEquals(pane1, tabbedPane.getSelectedComponent());
  }
  
  @Test
  public void testRemoveTabAt(){
    // Typical case: Removing an existing tab at a valid index
    tabbedPane.addTab("Tab 1", new JPanel());
    tabbedPane.addTab("Tab 2", new JPanel());
    int index = 1;
    tabbedPane.removeTabAt(index);
    assertEquals("Tab 2", tabbedPane.getTitleAt(index));
    
    // Error scenario: Removing a non-existing tab at an invalid index
    int invalidIndex = -1;
    int count = tabbedPane.getTabCount();
    tabbedPane.removeTabAt(invalidIndex);
    assertEquals(count, tabbedPane.getTabCount());
  }
  
  @Test
  public void testHandleSelectedFile(){
    // Typical case: Selecting a valid FileFollowingPane
    FileFollowingPane pane1 = new FileFollowingPane(new File("file1.txt"));
    tabbedPane.addTab("Tab 1", pane1);
    tabbedPane.setSelectedComponent(pane1);
    File expected1 = pane1.getFollowedFile().getParentFile();
    tabbedPane.handleSelectedFile();
    assertEquals(expected1, attributes.getLastFileChooserDirectory());
    
    // Error scenario: Selecting an invalid FileFollowingPane
    FileFollowingPane pane2 = new FileFollowingPane(new File("file2.txt"));
    tabbedPane.setSelectedComponent(pane2);
    tabbedPane.handleSelectedFile();
    assertEquals(expected1, attributes.getLastFileChooserDirectory());
  }
}