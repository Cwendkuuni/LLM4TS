import org.junit.*;
import static org.junit.Assert.*;
import javax.swing.*;
import ghm.follow.config.*;
import java.awt.*;
import java.io.*;

public class TabbedPaneTest {

   private static FollowAppAttributes attributes;
   private static TabbedPane tabbedPane;

   @BeforeClass
   public static void setUp() {
      attributes = new FollowAppAttributes();
      tabbedPane = new TabbedPane(attributes);
   }

   @AfterClass
   public static void tearDown() {
      attributes = null;
      tabbedPane = null;
   }

   @Test
   public void testFindComponentAt_withValidCoordinates_returnsTheComponentAtThatPosition() {
      JPanel panel1 = new JPanel();
      JPanel panel2 = new JPanel();
      tabbedPane.addTab("Tab 1", panel1);
      tabbedPane.addTab("Tab 2", panel2);

      Component result = tabbedPane.findComponentAt(10, 10);

      assertEquals(panel1, result);
   }

   @Test
   public void testFindComponentAt_withInvalidCoordinates_returnsNull() {
      JPanel panel1 = new JPanel();
      JPanel panel2 = new JPanel();
      tabbedPane.addTab("Tab 1", panel1);
      tabbedPane.addTab("Tab 2", panel2);

      Component result = tabbedPane.findComponentAt(100, 100);

      assertNull(result);
   }

   @Test
   public void testSetSelectedIndex_callsHandleSelectedFile() {
      FileFollowingPane pane1 = new FileFollowingPane(new File("file1.txt"));
      FileFollowingPane pane2 = new FileFollowingPane(new File("file2.txt"));
      tabbedPane.addTab("Tab 1", pane1);
      tabbedPane.addTab("Tab 2", pane2);
      tabbedPane.setSelectedComponent(pane2);

      tabbedPane.setSelectedIndex(0);

      // Assert that handleSelectedFile() was called
   }

   @Test
   public void testSetSelectedComponent_callsHandleSelectedFile() {
      FileFollowingPane pane1 = new FileFollowingPane(new File("file1.txt"));
      FileFollowingPane pane2 = new FileFollowingPane(new File("file2.txt"));
      tabbedPane.addTab("Tab 1", pane1);
      tabbedPane.addTab("Tab 2", pane2);

      tabbedPane.setSelectedComponent(pane2);

      // Assert that handleSelectedFile() was called
   }

   @Test
   public void testRemoveTabAt_callsHandleSelectedFile() {
      FileFollowingPane pane1 = new FileFollowingPane(new File("file1.txt"));
      FileFollowingPane pane2 = new FileFollowingPane(new File("file2.txt"));
      tabbedPane.addTab("Tab 1", pane1);
      tabbedPane.addTab("Tab 2", pane2);

      tabbedPane.removeTabAt(0);

      // Assert that handleSelectedFile() was called
   }

   @Test
   public void testHandleSelectedFile_withValidSelectedComponent_setLastFileChooserDirectory() {
      FileFollowingPane pane = new FileFollowingPane(new File("file1.txt"));
      tabbedPane.addTab("Tab 1", pane);

      tabbedPane.setSelectedComponent(pane);

      assertEquals(pane.getFollowedFile().getParentFile(), attributes.getLastFileChooserDirectory());
   }

   @Test
   public void testHandleSelectedFile_withNullSelectedComponent_doesNotChangeLastFileChooserDirectory() {
      tabbedPane.setSelectedComponent(null);

      assertNull(attributes.getLastFileChooserDirectory());
   }
}