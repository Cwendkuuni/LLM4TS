import org.junit.*;
import static org.junit.Assert.*;

public class TabbedPaneTest {

    @Test
    public void testFindComponentAt() {
        // Test case 1: x and y coordinates outside the tabbed pane bounds
        TabbedPane pane = new TabbedPane(new FollowAppAttributes());
        Component result = pane.findComponentAt(-10, -10);
        assertNull(result);
        
        // Test case 2: x and y coordinates inside the tabbed pane bounds
        pane.setSelectedIndex(0);
        result = pane.findComponentAt(10, 10);
        assertNotNull(result);
    }

    @Test
    public void testSetSelectedIndex() {
        // Test case: Selecting a valid index
        TabbedPane pane = new TabbedPane(new FollowAppAttributes());
        pane.add(new JPanel(), "Tab 1");
        pane.add(new JPanel(), "Tab 2");
        pane.setSelectedIndex(1);
        int selectedIndex = pane.getSelectedIndex();
        assertEquals(1, selectedIndex);
        // Additional edge case to test with an empty tabbed pane
        pane = new TabbedPane(new FollowAppAttributes());
        pane.setSelectedIndex(0);
        selectedIndex = pane.getSelectedIndex();
        assertEquals(-1, selectedIndex);
    }

    @Test
    public void testSetSelectedComponent() {
        // Test case: Selecting a specific component
        TabbedPane pane = new TabbedPane(new FollowAppAttributes());
        FileFollowingPane component1 = new FileFollowingPane();
        pane.add(component1);
        FileFollowingPane component2 = new FileFollowingPane();
        pane.add(component2);
        pane.setSelectedComponent(component1);
        Component selectedComponent = pane.getSelectedComponent();
        assertSame(component1, selectedComponent);
    }

    @Test
    public void testRemoveTabAt() {
        // Test case: Removing a tab at a valid index
        TabbedPane pane = new TabbedPane(new FollowAppAttributes());
        pane.add(new JPanel(), "Tab 1");
        pane.add(new JPanel(), "Tab 2");
        pane.removeTabAt(1);
        int tabCount = pane.getTabCount();
        assertEquals(1, tabCount);
        // Additional edge case to test with an empty tabbed pane
        pane = new TabbedPane(new FollowAppAttributes());
        pane.removeTabAt(0);
        tabCount = pane.getTabCount();
        assertEquals(0, tabCount);
    }

    // Additional test case for handleSelectedFile() method
    @Test
    public void testHandleSelectedFile() {
        TabbedPane pane = new TabbedPane(new FollowAppAttributes());
        FileFollowingPane component1 = new FileFollowingPane();
        component1.setFollowedFile(new File("path/to/file1.txt"));
        pane.add(component1);
        FileFollowingPane component2 = new FileFollowingPane();
        component2.setFollowedFile(new File("path/to/file2.txt"));
        pane.add(component2);
        pane.setSelectedComponent(component2);
        String lastFileChooserDirectory = pane.getAttributes().getLastFileChooserDirectory().getAbsolutePath();
        assertEquals("path/to", lastFileChooserDirectory);
    }
}