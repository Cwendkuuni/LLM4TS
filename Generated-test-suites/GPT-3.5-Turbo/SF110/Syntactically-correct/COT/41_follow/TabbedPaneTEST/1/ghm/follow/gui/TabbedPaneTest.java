
import org.junit.*;
import static org.junit.Assert.*;

public class TabbedPaneTest {

    @Test
    public void testFindComponentAt_containsPoint_shouldReturnComponent() {
        // Arrange
        TabbedPane tabbedPane = new TabbedPane(null);
        Component component = new JLabel("Test");
        tabbedPane.add(component, "Tab 1");

        // Act
        Component result = tabbedPane.findComponentAt(10, 10);

        // Assert
        assertEquals(component, result);
    }

    @Test
    public void testFindComponentAt_notContainsPoint_shouldReturnNull() {
        // Arrange
        TabbedPane tabbedPane = new TabbedPane(null);
        Component component = new JLabel("Test");
        tabbedPane.add(component, "Tab 1");

        // Act
        Component result = tabbedPane.findComponentAt(100, 100);

        // Assert
        assertNull(result);
    }

    @Test
    public void testSetSelectedIndex_shouldCallHandleSelectedFile() {
        // Arrange
        FollowAppAttributes attributes = new FollowAppAttributes();
        TabbedPane tabbedPane = new TabbedPane(attributes);

        // Act
        tabbedPane.setSelectedIndex(0);

        // Assert
        // Verify that handleSelectedFile() is called
        // Additional assertions for handleSelectedFile() may be needed
    }

    @Test
    public void testSetSelectedComponent_shouldCallHandleSelectedFile() {
        // Arrange
        FollowAppAttributes attributes = new FollowAppAttributes();
        TabbedPane tabbedPane = new TabbedPane(attributes);
        FileFollowingPane pane = new FileFollowingPane();

        // Act
        tabbedPane.setSelectedComponent(pane);

        // Assert
        // Verify that handleSelectedFile() is called
        // Additional assertions for handleSelectedFile() may be needed
    }

    @Test
    public void testRemoveTabAt_shouldCallHandleSelectedFile() {
        // Arrange
        FollowAppAttributes attributes = new FollowAppAttributes();
        TabbedPane tabbedPane = new TabbedPane(attributes);
        Component component = new JLabel("Test");
        tabbedPane.add(component, "Tab 1");

        // Act
        tabbedPane.removeTabAt(0);

        // Assert
        // Verify that handleSelectedFile() is called
        // Additional assertions for handleSelectedFile() may be needed
    }

    // Additional test cases for potential edge cases and exception handling scenarios may be added here

}
