
import org.junit.Test;
import static org.junit.Assert.*;

public class TabbedPaneTest {

    @Test
    public void testFindComponentAt() {
        FollowAppAttributes attributes = new FollowAppAttributes();
        TabbedPane tabbedPane = new TabbedPane(attributes);
        // Test with x, y coordinates outside the component
        assertNull(tabbedPane.findComponentAt(-1, -1));
        // Test with x, y coordinates inside the component
        assertNotNull(tabbedPane.findComponentAt(10, 10));
    }
    
    @Test
    public void testSetSelectedIndex() {
        FollowAppAttributes attributes = new FollowAppAttributes();
        TabbedPane tabbedPane = new TabbedPane(attributes);
        // Test with a valid index
        tabbedPane.setSelectedIndex(0);
        assertEquals(0, tabbedPane.getSelectedIndex());
    }
    
    @Test
    public void testSetSelectedComponent() {
        FollowAppAttributes attributes = new FollowAppAttributes();
        TabbedPane tabbedPane = new TabbedPane(attributes);
        FileFollowingPane pane = new FileFollowingPane();
        // Test with a valid pane
        tabbedPane.setSelectedComponent(pane);
        assertEquals(pane, tabbedPane.getSelectedComponent());
    }
    
    @Test
    public void testRemoveTabAt() {
        FollowAppAttributes attributes = new FollowAppAttributes();
        TabbedPane tabbedPane = new TabbedPane(attributes);
        // Test with a valid index
        tabbedPane.addTab("Tab 1", new JPanel());
        tabbedPane.addTab("Tab 2", new JPanel());
        tabbedPane.removeTabAt(0);
        assertEquals(1, tabbedPane.getTabCount());
    }
}
