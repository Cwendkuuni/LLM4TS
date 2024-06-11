import static org.junit.Assert.*;
import org.junit.Test;
import ghm.follow.gui.TabbedPane;
import ghm.follow.config.FollowAppAttributes;
import ghm.follow.gui.FileFollowingPane;
import java.io.File;

public class TabbedPaneTest {

    @Test
    public void testFindComponentAt() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        
        // Test when x, y coordinates are outside of components
        assertNull(tabbedPane.findComponentAt(-1, -1));
        
        // Test when x, y coordinates are inside a component
        FileFollowingPane pane = new FileFollowingPane(new File("test.txt"));
        tabbedPane.add("Test", pane);
        assertEquals(pane, tabbedPane.findComponentAt(10, 10));
    }

    @Test
    public void testSetSelectedIndex() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        
        // Test setting selected index
        tabbedPane.add("Tab 1", new FileFollowingPane(new File("test1.txt")));
        tabbedPane.add("Tab 2", new FileFollowingPane(new File("test2.txt")));
        tabbedPane.setSelectedIndex(1);
        assertEquals(1, tabbedPane.getSelectedIndex());
    }

    @Test
    public void testSetSelectedComponent() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        
        // Test setting selected component
        FileFollowingPane pane = new FileFollowingPane(new File("test.txt"));
        tabbedPane.setSelectedComponent(pane);
        assertEquals(pane, tabbedPane.getSelectedComponent());
    }

    @Test
    public void testRemoveTabAt() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        
        // Test removing tab at index
        FileFollowingPane pane = new FileFollowingPane(new File("test.txt"));
        tabbedPane.add("Test", pane);
        tabbedPane.add("Test 2", new FileFollowingPane(new File("test2.txt")));
        
        tabbedPane.removeTabAt(0);
        assertEquals(1, tabbedPane.getTabCount());
    }
}