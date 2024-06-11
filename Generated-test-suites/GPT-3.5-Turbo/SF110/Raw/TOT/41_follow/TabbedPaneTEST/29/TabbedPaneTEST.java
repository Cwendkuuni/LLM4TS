
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TabbedPaneTest {

   private TabbedPane tabbedPane;

   @Before
   public void setUp() {
      FollowAppAttributes attributes = new FollowAppAttributes();
      tabbedPane = new TabbedPane(attributes);
   }

   @Test
   public void testFindComponentAt() {
      Component component = tabbedPane.findComponentAt(10, 10);
      assertNull(component);

      // Testing with positive values
      tabbedPane.add(new JLabel("Tab 1"));
      component = tabbedPane.findComponentAt(50, 50);
      assertNotNull(component);

      // Testing with negative values
      component = tabbedPane.findComponentAt(-5, -5);
      assertNull(component);

      // Testing with zero values
      component = tabbedPane.findComponentAt(0, 0);
      assertNull(component);
   }

   @Test
   public void testSetSelectedIndex() {
      // Testing with valid index
      int index = 1;
      tabbedPane.add(new JLabel("Tab 1"));
      tabbedPane.add(new JLabel("Tab 2"));
      tabbedPane.add(new JLabel("Tab 3"));

      tabbedPane.setSelectedIndex(index);
      assertEquals(index, tabbedPane.getSelectedIndex());

      // Testing with negative index
      index = -1;
      tabbedPane.setSelectedIndex(index);
      assertEquals(index, tabbedPane.getSelectedIndex());

      // Testing with index greater than number of tabs
      index = 5;
      tabbedPane.setSelectedIndex(index);
      assertEquals(index, tabbedPane.getSelectedIndex());
   }

   @Test
   public void testSetSelectedComponent() {
      // Testing with valid component
      FileFollowingPane pane = new FileFollowingPane(new File("test.txt"));
      tabbedPane.add(pane);

      tabbedPane.setSelectedComponent(pane);
      assertEquals(pane, tabbedPane.getSelectedComponent());

      // Testing with invalid component
      FileFollowingPane invalidPane = new FileFollowingPane(null);
      tabbedPane.setSelectedComponent(invalidPane);
      assertEquals(invalidPane, tabbedPane.getSelectedComponent());
   }

   @Test
   public void testRemoveTabAt() {
      // Testing with valid index
      int index = 0;
      tabbedPane.add(new JLabel("Tab 1"));
      tabbedPane.add(new JLabel("Tab 2"));
      tabbedPane.add(new JLabel("Tab 3"));

      tabbedPane.removeTabAt(index);
      assertEquals(index, tabbedPane.getSelectedIndex());

      // Testing with negative index
      index = -1;
      tabbedPane.removeTabAt(index);
      assertEquals(index, tabbedPane.getSelectedIndex());

      // Testing with index greater than number of tabs
      index = 5;
      tabbedPane.removeTabAt(index);
      assertEquals(index, tabbedPane.getSelectedIndex());
   }

   @Test
   public void testHandleSelectedFile() {
      // Testing with valid selected component
      File file = new File("test.txt");
      FileFollowingPane pane = new FileFollowingPane(file);
      tabbedPane.add(pane);
      tabbedPane.setSelectedComponent(pane);

      tabbedPane.handleSelectedFile();
      assertEquals(file.getParent(), tabbedPane.getAttributes().getLastFileChooserDirectory());

      // Testing with null selected component
      tabbedPane.setSelectedComponent(null);
      tabbedPane.handleSelectedFile();
      assertNull(tabbedPane.getAttributes().getLastFileChooserDirectory());
   }
}

