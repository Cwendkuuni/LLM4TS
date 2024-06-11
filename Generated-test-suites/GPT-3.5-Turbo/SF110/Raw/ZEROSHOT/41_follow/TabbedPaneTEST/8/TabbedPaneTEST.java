import org.junit.Test;
import static org.junit.Assert.*;

public class TabbedPaneTest {

    @Test
    public void findComponentAtTest() {
        // Create a TabbedPane object
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());

        // Test when x and y coordinates are within the TabbedPane
        Component result1 = tabbedPane.findComponentAt(10, 10);
        assertNull(result1);

        // Test when x and y coordinates are outside the TabbedPane
        Component result2 = tabbedPane.findComponentAt(1000, 1000);
        assertNull(result2);
    }

    @Test
    public void setSelectedIndexTest() {
        // Create a TabbedPane object
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());

        // Test setting selected index
        tabbedPane.setSelectedIndex(0);

        // Assert the selected index is set correctly
        assertEquals(0, tabbedPane.getSelectedIndex());
    }

    @Test
    public void setSelectedComponentTest() {
        // Create a TabbedPane object
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());

        // Create a FileFollowingPane object
        FileFollowingPane pane = new FileFollowingPane();

        // Set the selected component
        tabbedPane.setSelectedComponent(pane);

        // Assert the selected component is set correctly
        assertEquals(pane, tabbedPane.getSelectedComponent());
    }

    @Test
    public void removeTabAtTest() {
        // Create a TabbedPane object
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());

        // Add a tab
        tabbedPane.addTab("Tab 1", new FileFollowingPane());

        // Remove the tab at index 0
        tabbedPane.removeTabAt(0);

        // Assert the tab is removed correctly
        assertEquals(0, tabbedPane.getTabCount());
    }

    @Test
    public void handleSelectedFileTest() {
        // Create a TabbedPane object
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());

        // Create a FileFollowingPane object with a file
        FileFollowingPane pane = new FileFollowingPane();
        pane.setFollowedFile(new File("test.txt"));

        // Add the FileFollowingPane to the TabbedPane
        tabbedPane.addTab("Tab 1", pane);

        // Set the selected component
        tabbedPane.setSelectedComponent(pane);

        // Call the handleSelectedFile method
        tabbedPane.handleSelectedFile();

        // Get the last file chooser directory from attributes
        File lastFileChooserDirectory = tabbedPane.getAttributes().getLastFileChooserDirectory();

        // Assert the last file chooser directory is set correctly
        assertEquals(pane.getFollowedFile().getParentFile(), lastFileChooserDirectory);
    }

}