import org.junit.*;
import static org.junit.Assert.*;

public class TabbedPaneTest {

    // Test cases for each method
    
    @Test
    public void testConstructor() {
        // test case for public TabbedPane(final FollowAppAttributes attributes)
        // ...
    }
    
    @Test
    public void testFindComponentAt() {
        // test case for public Component findComponentAt(final int x, final int y)
        // ...
    }
    
    @Test
    public void testSetSelectedIndex() {
        // test case for public void setSelectedIndex(final int index)
        // ...
    }
    
    @Test
    public void testSetSelectedComponent() {
        // test case for public void setSelectedComponent(final FileFollowingPane pane)
        // ...
    }
    
    @Test
    public void testRemoveTabAt() {
        // test case for public void removeTabAt(final int index)
        // ...
    }

    // Additional test cases for edge cases and exception handling scenarios
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullAttributes() {
        // edge case for public TabbedPane(final FollowAppAttributes attributes)
        // ...
    }
    
    @Test
    public void testFindComponentAtOutOfBounds() {
        // edge case for public Component findComponentAt(final int x, final int y)
        // ...
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetSelectedIndexNegativeIndex() {
        // exception handling for public void setSelectedIndex(final int index)
        // ...
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetSelectedComponentNullPane() {
        // exception handling for public void setSelectedComponent(final FileFollowingPane pane)
        // ...
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveTabAtNegativeIndex() {
        // exception handling for public void removeTabAt(final int index)
        // ...
    }
    
    // Utility methods or setup methods if required
    
}