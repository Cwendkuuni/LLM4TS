
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.awt.Component;
import java.io.File;

public class TabbedPaneTest {

    private TabbedPane tabbedPane;
    private FollowAppAttributes attributes;

    @Before
    public void setUp() {
        attributes = new FollowAppAttributes();
        tabbedPane = new TabbedPane(attributes);
    }

    @Test
    public void testFindComponentAt_validCoordinates_returnsComponent() {
        // Arrange
        Component component = new JPanel();
        tabbedPane.addTab("Tab 1", component);

        // Act
        Component result = tabbedPane.findComponentAt(10, 10);

        // Assert
        Assert.assertEquals(component, result);
    }

    @Test
    public void testFindComponentAt_invalidCoordinates_returnsNull() {
        // Arrange
        Component component = new JPanel();
        tabbedPane.addTab("Tab 1", component);

        // Act
        Component result = tabbedPane.findComponentAt(100, 100);

        // Assert
        Assert.assertNull(result);
    }

    @Test
    public void testSetSelectedIndex_validIndex_callsHandleSelectedFile() {
        // Arrange
        tabbedPane.addTab("Tab 1", new JPanel());

        // Act
        tabbedPane.setSelectedIndex(0);

        // Assert
        // Check if handleSelectedFile method was called
        // Add assertion here
    }

    @Test
    public void testSetSelectedComponent_validPane_callsHandleSelectedFile() {
        // Arrange
        FileFollowingPane pane = new FileFollowingPane(new File("Test.txt"));
        tabbedPane.addTab("Tab 1", pane);

        // Act
        tabbedPane.setSelectedComponent(pane);

        // Assert
        // Check if handleSelectedFile method was called
        // Add assertion here
    }

    @Test
    public void testRemoveTabAt_validIndex_callsHandleSelectedFile() {
        // Arrange
        tabbedPane.addTab("Tab 1", new JPanel());
        tabbedPane.addTab("Tab 2", new JPanel());

        // Act
        tabbedPane.removeTabAt(1);

        // Assert
        // Check if handleSelectedFile method was called
        // Add assertion here
    }

    @Test
    public void testHandleSelectedFile_validPane_updatesLastFileChooserDirectory() {
        // Arrange
        FileFollowingPane pane = new FileFollowingPane(new File("Test.txt"));
        tabbedPane.addTab("Tab 1", pane);
        
        // Set initial lastFileChooserDirectory value
        attributes.setLastFileChooserDirectory(new File("C:\\"));

        // Act
        tabbedPane.setSelectedComponent(pane);

        // Assert
        Assert.assertEquals(pane.getFollowedFile().getParentFile(), attributes.getLastFileChooserDirectory());
    }
}

