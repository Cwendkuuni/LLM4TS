
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class TabbedPaneTest {
    @Test
    public void testFindComponentAt() {
        // Test case when tabbedPane does not contain specified coordinates
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        assertNull(tabbedPane.findComponentAt(0, 0));

        // Test case when tabbedPane contains specified coordinates
        FileFollowingPane pane = new FileFollowingPane();
        pane.setBounds(0, 0, 100, 100);
        tabbedPane.add("Tab 1", pane);
        assertEquals(pane, tabbedPane.findComponentAt(50, 50));
    }

    @Test
    public void testSetSelectedIndex() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        
        // Test case when index is set to a valid value
        tabbedPane.add("Tab 1", new FileFollowingPane());
        tabbedPane.add("Tab 2", new FileFollowingPane());
        tabbedPane.setSelectedIndex(1);
        assertEquals(1, tabbedPane.getSelectedIndex());

        // Test case when index is set to an invalid value
        tabbedPane.setSelectedIndex(10);
        assertEquals(1, tabbedPane.getSelectedIndex());
    }

    @Test
    public void testSetSelectedComponent() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        FileFollowingPane pane1 = new FileFollowingPane();
        FileFollowingPane pane2 = new FileFollowingPane();

        // Test case when pane is set to a component present in the tabbedPane
        tabbedPane.add("Tab 1", pane1);
        tabbedPane.add("Tab 2", pane2);
        tabbedPane.setSelectedComponent(pane2);
        assertEquals(pane2, tabbedPane.getSelectedComponent());

        // Test case when pane is set to a component not present in the tabbedPane
        FileFollowingPane pane3 = new FileFollowingPane();
        tabbedPane.setSelectedComponent(pane3);
        assertNull(tabbedPane.getSelectedComponent());
    }

    @Test
    public void testRemoveTabAt() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        FileFollowingPane pane1 = new FileFollowingPane();
        FileFollowingPane pane2 = new FileFollowingPane();

        // Test case when index is valid
        tabbedPane.add("Tab 1", pane1);
        tabbedPane.add("Tab 2", pane2);
        tabbedPane.removeTabAt(1);
        assertEquals(1, tabbedPane.getTabCount());

        // Test case when index is invalid
        tabbedPane.removeTabAt(10);
        assertEquals(1, tabbedPane.getTabCount());
    }
}
