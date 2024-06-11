import org.junit.*;
import static org.junit.Assert.*;

public class TabbedPaneTest {

    private static FollowAppAttributes attributes;
    private static TabbedPane tabbedPane;

    @BeforeClass
    public static void setUp() {
        attributes = new FollowAppAttributes();
        tabbedPane = new TabbedPane(attributes);
    }

    @Test
    public void testFindComponentAt_containsXAndY_ReturnsComponent() {
        // Test when x and y are within the bounds of the TabbedPane
        // Arrange
        int x = 50;
        int y = 50;
        Component expectedComponent = new JLabel("Test");

        // Act
        tabbedPane.add(expectedComponent);
        Component actualComponent = tabbedPane.findComponentAt(x, y);

        // Assert
        assertEquals(expectedComponent, actualComponent);
    }

    @Test
    public void testFindComponentAt_outOfBoundsXAndY_ReturnsNull() {
        // Test when x and y are outside the bounds of the TabbedPane
        // Arrange
        int x = 1000;
        int y = 1000;

        // Act
        Component actualComponent = tabbedPane.findComponentAt(x, y);

        // Assert
        assertNull(actualComponent);
    }

    @Test
    public void testSetSelectedIndex_handleSelectedFileCalled() {
        // Test if handleSelectedFile is called after setSelectedIndex is invoked
        // Arrange
        int index = 0;

        // Act
        tabbedPane.setSelectedIndex(index);

        // Assert
        // Assert that handleSelectedFile was called
        assertTrue(attributes.getLastFileChooserDirectory() != null);
    }

    @Test
    public void testSetSelectedComponent_handleSelectedFileCalled() {
        // Test if handleSelectedFile is called after setSelectedComponent is invoked
        // Arrange
        FileFollowingPane pane = new FileFollowingPane(new File("test.txt"));

        // Act
        tabbedPane.setSelectedComponent(pane);

        // Assert
        // Assert that handleSelectedFile was called
        assertTrue(attributes.getLastFileChooserDirectory() != null);
    }

    @Test
    public void testRemoveTabAt_handleSelectedFileCalled() {
        // Test if handleSelectedFile is called after removeTabAt is invoked
        // Arrange
        int index = 0;
        tabbedPane.add(new JLabel("Test"));

        // Act
        tabbedPane.removeTabAt(index);

        // Assert
        // Assert that handleSelectedFile was called
        assertTrue(attributes.getLastFileChooserDirectory() != null);
    }

}