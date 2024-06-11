import org.junit.Test;
import static org.junit.Assert.*;
import javax.swing.JComponent;
import ghm.follow.gui.TabbedPane;
import ghm.follow.config.FollowAppAttributes;
import java.io.File;

public class TabbedPaneTest {

    @Test
    public void testFindComponentAt() {
        FollowAppAttributes attributes = new FollowAppAttributes();
        TabbedPane tabbedPane = new TabbedPane(attributes);
        
        // Test typical use case
        JComponent component = new JComponent() {};
        tabbedPane.add(component);
        assertSame(component, tabbedPane.findComponentAt(50, 50));
        
        // Test edge case: x and y coordinates out of bounds
        assertNull(tabbedPane.findComponentAt(-10, -10));
        
        // Test error scenario: no component found at given coordinates
        assertNull(tabbedPane.findComponentAt(100, 100));
    }
    
    @Test
    public void testSetSelectedIndex() {
        FollowAppAttributes attributes = new FollowAppAttributes();
        TabbedPane tabbedPane = new TabbedPane(attributes);
        
        // Test typical use case
        JComponent component1 = new JComponent() {};
        JComponent component2 = new JComponent() {};
        JComponent component3 = new JComponent() {};
        tabbedPane.add(component1);
        tabbedPane.add(component2);
        tabbedPane.add(component3);
        tabbedPane.setSelectedIndex(1);
        assertSame(component2, tabbedPane.getSelectedComponent());
        
        // Test edge case: index out of bounds
        try {
            tabbedPane.setSelectedIndex(-1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Test passed
        }
        
        // Test edge case: empty tabbed pane
        tabbedPane.removeAll();
        tabbedPane.setSelectedIndex(0);
        assertNull(tabbedPane.getSelectedComponent());
    }
    
    @Test
    public void testSetSelectedComponent() {
        FollowAppAttributes attributes = new FollowAppAttributes();
        TabbedPane tabbedPane = new TabbedPane(attributes);
        
        // Test typical use case
        FileFollowingPane pane1 = new FileFollowingPane(new File("file1.txt"));
        FileFollowingPane pane2 = new FileFollowingPane(new File("file2.txt"));
        FileFollowingPane pane3 = new FileFollowingPane(new File("file3.txt"));
        tabbedPane.add(pane1);
        tabbedPane.add(pane2);
        tabbedPane.add(pane3);
        tabbedPane.setSelectedComponent(pane2);
        assertSame(pane2, tabbedPane.getSelectedComponent());
        
        // Test edge case: null component
        tabbedPane.setSelectedComponent(null);
        assertNull(tabbedPane.getSelectedComponent());
    }
    
    @Test
    public void testRemoveTabAt() {
        FollowAppAttributes attributes = new FollowAppAttributes();
        TabbedPane tabbedPane = new TabbedPane(attributes);
        
        // Test typical use case
        JComponent component1 = new JComponent() {};
        JComponent component2 = new JComponent() {};
        JComponent component3 = new JComponent() {};
        tabbedPane.add(component1);
        tabbedPane.add(component2);
        tabbedPane.add(component3);
        tabbedPane.removeTabAt(1);
        assertSame(component3, tabbedPane.getComponentAt(1));
        
        // Test edge case: index out of bounds
        try {
            tabbedPane.removeTabAt(3);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Test passed
        }
        
        // Test edge case: empty tabbed pane
        tabbedPane.removeAll();
        tabbedPane.removeTabAt(0);
        assertEquals(0, tabbedPane.getTabCount());
    }
    
    @Test
    public void testHandleSelectedFile() {
        FollowAppAttributes attributes = new FollowAppAttributes();
        TabbedPane tabbedPane = new TabbedPane(attributes);
        
        // Test typical use case
        FileFollowingPane pane = new FileFollowingPane(new File("file1.txt"));
        tabbedPane.add(pane);
        tabbedPane.setSelectedComponent(pane);
        assertEquals(pane.getFollowedFile().getParentFile(), attributes.getLastFileChooserDirectory());
        
        // Test edge case: selected component is null
        tabbedPane.setSelectedComponent(null);
        assertNull(attributes.getLastFileChooserDirectory());
    }
}