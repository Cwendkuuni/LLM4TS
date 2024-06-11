
package ghm.follow.gui;

import org.junit.Test;
import static org.junit.Assert.*;

public class TabbedPaneTest {

    @Test
    public void testFindComponentAt() {
        // Test case for when the component is not contained
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        assertNull(tabbedPane.findComponentAt(0, 0));
        
        // Test case for when the component is contained
        JButton button = new JButton();
        tabbedPane.add(button);
        assertEquals(button, tabbedPane.findComponentAt(0, 0));
    }

    @Test
    public void testSetSelectedIndex() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        tabbedPane.add(new FileFollowingPane());
        
        // Test case for setting a valid index
        tabbedPane.setSelectedIndex(0);
        assertEquals(0, tabbedPane.getSelectedIndex());
        
        // Test case for setting an invalid index
        tabbedPane.setSelectedIndex(1);
        assertEquals(0, tabbedPane.getSelectedIndex());
    }
    
    @Test
    public void testSetSelectedComponent() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        FileFollowingPane pane = new FileFollowingPane();
        
        // Test case for when the component is in the tabbedPane
        tabbedPane.add(pane);
        tabbedPane.setSelectedComponent(pane);
        assertEquals(pane, tabbedPane.getSelectedComponent());
        
        // Test case for when the component is not in the tabbedPane
        FileFollowingPane otherPane = new FileFollowingPane();
        tabbedPane.setSelectedComponent(otherPane);
        assertNotEquals(otherPane, tabbedPane.getSelectedComponent());
    }
    
    @Test
    public void testRemoveTabAt() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        FileFollowingPane pane = new FileFollowingPane();
        tabbedPane.add(pane);
        
        // Test case for removing a tab
        tabbedPane.removeTabAt(0);
        assertEquals(0, tabbedPane.getTabCount());
        
        // Test case for removing a non-existing tab
        tabbedPane.removeTabAt(1);
        assertEquals(0, tabbedPane.getTabCount());
    }
}
