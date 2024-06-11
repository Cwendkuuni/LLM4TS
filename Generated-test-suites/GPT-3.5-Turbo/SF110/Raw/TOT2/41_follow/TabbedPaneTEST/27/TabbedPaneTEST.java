import org.junit.Test;
import static org.junit.Assert.*;

public class TabbedPaneTest {

    @Test
    public void testFindComponentAt() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        // Test functionality
        assertNotNull(tabbedPane.findComponentAt(10, 10));
        assertNull(tabbedPane.findComponentAt(100, 100));
        // Test edge cases
        assertNull(tabbedPane.findComponentAt(-1, -1));
    }

    @Test
    public void testSetSelectedIndex() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        // Test functionality
        tabbedPane.setSelectedIndex(0);
        // Test edge cases
        tabbedPane.setSelectedIndex(-1);
    }

    @Test
    public void testSetSelectedComponent() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        // Test functionality
        FileFollowingPane pane = new FileFollowingPane(new File("test.txt"));
        tabbedPane.setSelectedComponent(pane);
        // Test error scenario
        assertNull(tabbedPane.getSelectedComponent());
    }

    @Test
    public void testRemoveTabAt() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        // Test functionality
        tabbedPane.removeTabAt(0);
        // Test edge cases
        tabbedPane.removeTabAt(-1);
    }

    @Test
    public void testHandleSelectedFile() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        // Test functionality
        FileFollowingPane pane = new FileFollowingPane(new File("test.txt"));
        tabbedPane.setSelectedComponent(pane);
        tabbedPane.handleSelectedFile();
        // Test error scenario
        assertNull(tabbedPane.handleSelectedFile());
    }
}