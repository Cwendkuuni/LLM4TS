import org.junit.Test;
import java.awt.*;
import java.io.File;
import static org.junit.Assert.*;

public class TabbedPaneTest {

    @Test
    public void testFindComponentAt_typical() {
        // Create a TabbedPane object
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        
        // Create a mock component to add to the TabbedPane
        Component mockComponent = new Component() {};
        tabbedPane.addTab("Tab", mockComponent);
        
        // Get the component at a specific location in the TabbedPane
        Component result = tabbedPane.findComponentAt(10, 10);
        
        // Assert that the returned component is the same as the added component
        assertEquals(mockComponent, result);
    }

    @Test
    public void testFindComponentAt_nullResult() {
        // Create a TabbedPane object without adding any components
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        
        // Get the component at a specific location in the TabbedPane
        Component result = tabbedPane.findComponentAt(10, 10);
        
        // Assert that the returned component is null
        assertNull(result);
    }

    @Test
    public void testSetSelectedIndex() {
        // Create a TabbedPane object
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        int initialSelectedIndex = tabbedPane.getSelectedIndex();
        
        // Set a new selected index
        tabbedPane.setSelectedIndex(1);
        
        // Assert that the selected index has changed
        assertNotEquals(initialSelectedIndex, tabbedPane.getSelectedIndex());
    }

    @Test
    public void testSetSelectedComponent() {
        // Create a TabbedPane object
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        
        // Create a mock FileFollowingPane object to set as the selected component
        FileFollowingPane mockPane = new FileFollowingPane(new File("test.txt"));
        
        // Set the selected component to the mock pane
        tabbedPane.setSelectedComponent(mockPane);
        
        // Assert that the selected component is the same as the mock pane
        assertEquals(mockPane, tabbedPane.getSelectedComponent());
    }

    @Test
    public void testRemoveTabAt() {
        // Create a TabbedPane object
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());

        // Create a mock component and add it to the TabbedPane
        Component mockComponent = new Component() {};
        tabbedPane.addTab("Tab", mockComponent);

        int initialTabCount = tabbedPane.getTabCount();

        // Remove the tab at a specific index
        tabbedPane.removeTabAt(0);

        // Assert that the tab count has decreased by 1
        assertEquals(initialTabCount - 1, tabbedPane.getTabCount());
    }

    @Test
    public void testHandleSelectedFile_typical() {
        // Create a TabbedPane object
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());

        // Create a mock FileFollowingPane object
        FileFollowingPane mockPane = new FileFollowingPane(new File("test.txt"));

        // Set the mock pane as the selected component
        tabbedPane.setSelectedComponent(mockPane);

        File expected = mockPane.getFollowedFile().getParentFile();

        // Call the handleSelectedFile method
        tabbedPane.handleSelectedFile();

        // Assert that the lastFileChooserDirectory has been updated with the expected value
        assertEquals(expected, tabbedPane.attributes.getLastFileChooserDirectory());
    }

    @Test
    public void testHandleSelectedFile_nullSelectedComponent() {
        // Create a TabbedPane object
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());

        // Set the selected component to null
        tabbedPane.setSelectedComponent(null);

        // Call the handleSelectedFile method
        tabbedPane.handleSelectedFile();

        // Assert that the lastFileChooserDirectory remains the same (null)
        assertNull(tabbedPane.attributes.getLastFileChooserDirectory());
    }
}