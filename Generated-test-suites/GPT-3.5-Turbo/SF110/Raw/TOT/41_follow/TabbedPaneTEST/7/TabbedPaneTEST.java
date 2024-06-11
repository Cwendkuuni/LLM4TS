import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import javax.swing.*;
import ghm.follow.config.*;
import java.awt.*;
import java.io.*;

public class TabbedPaneTest {
  
  private TabbedPane tabbedPane;
  
  @Before
  public void setUp() {
    FollowAppAttributes attributes = new FollowAppAttributes();
    tabbedPane = new TabbedPane(attributes);
  }
  
  @Test
  public void testFindComponentAt_WhenComponentNotFound_ReturnsNull() {
    Component result = tabbedPane.findComponentAt(100, 100);
    assertNull(result);
  }
  
  @Test
  public void testFindComponentAt_WhenComponentFound_ReturnsComponent() {
    JButton button = new JButton("Test Button");
    tabbedPane.add("Tab1", button);
    
    Component result = tabbedPane.findComponentAt(button.getX() + 10, button.getY() + 10);
    assertEquals(button, result);
  }
  
  @Test
  public void testSetSelectedIndex_WhenValidIndex_SetSelectedIndex() {
    tabbedPane.add("Tab1", new JPanel());
    tabbedPane.add("Tab2", new JPanel());
    
    tabbedPane.setSelectedIndex(1);
    
    int selectedIndex = tabbedPane.getSelectedIndex();
    assertEquals(1, selectedIndex);
  }
  
  @Test
  public void testSetSelectedIndex_WhenInvalidIndex_ThrowsException() {
    try {
      tabbedPane.setSelectedIndex(10);
      fail("Expected IndexOutOfBoundsException to be thrown");
    } catch (IndexOutOfBoundsException e) {
      // Test passed
    }
  }
  
  @Test
  public void testSetSelectedComponent_WhenValidComponent_SetSelectedComponent() {
    JPanel panel1 = new JPanel();
    JPanel panel2 = new JPanel();
    tabbedPane.add("Tab1", panel1);
    tabbedPane.add("Tab2", panel2);
    
    tabbedPane.setSelectedComponent(panel2);
    
    Component selectedComponent = tabbedPane.getSelectedComponent();
    assertEquals(panel2, selectedComponent);
  }
  
  @Test
  public void testSetSelectedComponent_WhenInvalidComponent_ThrowsException() {
    JPanel panel = new JPanel();
    
    try {
      tabbedPane.setSelectedComponent(panel);
      fail("Expected IllegalArgumentException to be thrown");
    } catch (IllegalArgumentException e) {
      // Test passed
    }
  }
  
  @Test
  public void testRemoveTabAt_WhenValidIndex_RemoveTab() {
    JPanel panel1 = new JPanel();
    JPanel panel2 = new JPanel();
    tabbedPane.add("Tab1", panel1);
    tabbedPane.add("Tab2", panel2);
    
    tabbedPane.removeTabAt(1);
    
    int tabCount = tabbedPane.getTabCount();
    assertEquals(1, tabCount);
  }
  
  @Test
  public void testRemoveTabAt_WhenInvalidIndex_ThrowsException() {
    try {
      tabbedPane.removeTabAt(10);
      fail("Expected IndexOutOfBoundsException to be thrown");
    } catch (IndexOutOfBoundsException e) {
      // Test passed
    }
  }
  
  @Test
  public void testHandleSelectedFile_WhenFileFollowingPaneSelected_ChangeLastFileChooserDirectory() {
    FileFollowingPane pane = new FileFollowingPane();
    pane.setFollowedFile(new File("test.txt"));
    tabbedPane.add("Tab1", pane);
    
    tabbedPane.setSelectedComponent(pane);
    
    File lastFileChooserDirectory = tabbedPane.getAttributes().getLastFileChooserDirectory();
    assertEquals(new File("test.txt").getParentFile(), lastFileChooserDirectory);
  }

}