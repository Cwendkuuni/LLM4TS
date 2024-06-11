import static org.junit.Assert.*;
import org.junit.Test;

public class TabbedPaneTest {
    
    @Test
    public void testFindComponentAt() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        // Typical use case: Mouse click within a component
        Component result1 = tabbedPane.findComponentAt(50, 50);
        assertNotNull("Expected non-null component", result1);
        
        // Edge case: Mouse click at the top-left corner of the component
        Component result2 = tabbedPane.findComponentAt(0, 0);
        assertNotNull("Expected non-null component", result2);
        
        // Error scenario: Mouse click outside of the component
        Component result3 = tabbedPane.findComponentAt(-10, -10);
        assertNull("Expected null component", result3);
    }
    
    @Test
    public void testSetSelectedIndex() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        // Typical use case: Index within the valid range
        tabbedPane.setSelectedIndex(0);
        assertEquals("Expected selected index to be 0", 0, tabbedPane.getSelectedIndex());
        
        // Error scenario: Negative index
        tabbedPane.setSelectedIndex(-1);
        assertEquals("Expected selected index to remain unchanged", 0, tabbedPane.getSelectedIndex());
        
        // Error scenario: Index outside of the valid range
        tabbedPane.setSelectedIndex(10);
        assertEquals("Expected selected index to remain unchanged", 0, tabbedPane.getSelectedIndex());
    }
    
    @Test
    public void testSetSelectedComponent() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        FileFollowingPane pane1 = new FileFollowingPane();
        FileFollowingPane pane2 = new FileFollowingPane();
        
        // Typical use case: Selecting a valid component
        tabbedPane.add("Pane1", pane1);
        tabbedPane.add("Pane2", pane2);
        tabbedPane.setSelectedComponent(pane1);
        assertEquals("Expected selected component to be pane1", pane1, tabbedPane.getSelectedComponent());
        
        // Error scenario: Selecting a null component
        tabbedPane.setSelectedComponent(null);
        assertEquals("Expected selected component to remain unchanged", pane1, tabbedPane.getSelectedComponent());
        
        // Error scenario: Selecting a component not in the pane
        FileFollowingPane pane3 = new FileFollowingPane();
        tabbedPane.setSelectedComponent(pane3);
        assertEquals("Expected selected component to remain unchanged", pane1, tabbedPane.getSelectedComponent());
    }
    
    @Test
    public void testRemoveTabAt() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        FileFollowingPane pane1 = new FileFollowingPane();
        FileFollowingPane pane2 = new FileFollowingPane();
        
        // Typical use case: Removing a tab at a valid index
        tabbedPane.add("Pane1", pane1);
        tabbedPane.add("Pane2", pane2);
        tabbedPane.removeTabAt(0);
        assertEquals("Expected tab count to be 1", 1, tabbedPane.getTabCount());
        
        // Error scenario: Removing a tab at a negative index
        tabbedPane.removeTabAt(-1);
        assertEquals("Expected tab count to remain unchanged", 1, tabbedPane.getTabCount());
        
        // Error scenario: Removing a tab at an index outside of the valid range
        tabbedPane.removeTabAt(10);
        assertEquals("Expected tab count to remain unchanged", 1, tabbedPane.getTabCount());
    }
}