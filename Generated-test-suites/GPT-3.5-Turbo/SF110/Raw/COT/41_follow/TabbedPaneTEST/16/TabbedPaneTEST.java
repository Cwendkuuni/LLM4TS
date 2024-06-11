import org.junit.*;
import static org.junit.Assert.*;

public class TabbedPaneTest {
    
    @Test
    public void testTabbedPane() {
        FollowAppAttributes attributes = new FollowAppAttributes();
        TabbedPane tabbedPane = new TabbedPane(attributes);
        assertNotNull(tabbedPane);
    }
    
    @Test
    public void testFindComponentAt() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        Component component = tabbedPane.findComponentAt(10, 10);
        assertNotNull(component);
    }
    
    @Test
    public void testSetSelectedIndex() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        tabbedPane.setSelectedIndex(0);
        assertEquals(0, tabbedPane.getSelectedIndex());
    }
    
    @Test
    public void testSetSelectedComponent() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        FileFollowingPane pane = new FileFollowingPane();
        tabbedPane.setSelectedComponent(pane);
        assertEquals(pane, tabbedPane.getSelectedComponent());
    }
    
    @Test
    public void testRemoveTabAt() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        tabbedPane.addTab("Tab 1", new JPanel());
        tabbedPane.addTab("Tab 2", new JPanel());
        tabbedPane.removeTabAt(0);
        assertEquals(1, tabbedPane.getTabCount());
    }
    
    // Additional test cases for edge cases and exception handling
    
    @Test(expected = IllegalArgumentException.class)
    public void testTabbedPaneWithNullAttributes() {
        TabbedPane tabbedPane = new TabbedPane(null);
    }
    
    @Test
    public void testFindComponentAtWithInvalidCoordinates() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        Component component = tabbedPane.findComponentAt(-10, -10);
        assertNull(component);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetSelectedIndexWithInvalidIndex() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        tabbedPane.setSelectedIndex(-1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetSelectedComponentWithNullPane() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        tabbedPane.setSelectedComponent(null);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveTabAtWithInvalidIndex() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        tabbedPane.removeTabAt(10);
    }
}