package ghm.follow.gui;

import static org.junit.Assert.*;
import org.junit.Test;

public class TabbedPaneTest {

    @Test
    public void testFindComponentAt() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        assertNull(tabbedPane.findComponentAt(0, 0));
    }

    @Test
    public void testSetSelectedIndex() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        tabbedPane.setSelectedIndex(0);
        // add assertions here
    }

    @Test
    public void testSetSelectedComponent() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        tabbedPane.setSelectedComponent(null);
        // add assertions here
    }

    @Test
    public void testRemoveTabAt() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        tabbedPane.removeTabAt(0);
        // add assertions here
    }

    @Test
    public void testHandleSelectedFile() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        tabbedPane.handleSelectedFile();
        // add assertions here
    }
}