import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TabbedPaneTest {
    private TabbedPane tabbedPane;
    private FollowAppAttributes attributes;
    
    @Before
    public void setUp() {
        attributes = new FollowAppAttributes();
        tabbedPane = new TabbedPane(attributes);
    }

    @Test
    public void testFindComponentAt_WhenInvalidCoordinates_ReturnsNull() {
        assertNull(tabbedPane.findComponentAt(-1, -1));
    }

    @Test
    public void testFindComponentAt_WhenValidCoordinatesInsideComponent_ReturnsComponent() {
        // Assuming there is a FileFollowingPane at index 0
        FileFollowingPane pane = new FileFollowingPane();
        tabbedPane.addTab("Tab 1", pane);

        Component result = tabbedPane.findComponentAt(10, 10);
        assertEquals(pane, result);
    }

    @Test
    public void testFindComponentAt_WhenValidCoordinatesInsideNestedComponent_ReturnsNestedComponent() {
        // Assuming there is a JPanel inside a FileFollowingPane at index 0
        JPanel nestedPanel = new JPanel();
        FileFollowingPane pane = new FileFollowingPane();
        pane.add(nestedPanel);
        tabbedPane.addTab("Tab 1", pane);

        Component result = tabbedPane.findComponentAt(10, 10);
        assertEquals(nestedPanel, result);
    }

    // Other test cases for findComponentAt method ...

    @Test
    public void testSetSelectedIndex_WhenValidIndex_UpdatesSelectedIndexAndHandlesSelectedFile() {
        int index = 1; // Assuming there are at least 2 tabs

        tabbedPane.setSelectedIndex(index);
        
        int selectedIndex = tabbedPane.getSelectedIndex();
        assertEquals(index, selectedIndex);

        // Verify handleSelectedFile() was called after setting the selected index
        // by checking if attributes' lastFileChooserDirectory was updated
        FileFollowingPane pane = (FileFollowingPane) tabbedPane.getSelectedComponent();
        File parent = pane.getFollowedFile().getParentFile();
        assertEquals(parent, attributes.getLastFileChooserDirectory());
    }

    // Other test cases for setSelectedIndex method ...

    @Test
    public void testSetSelectedComponent_WhenValidComponent_UpdatesSelectedComponentAndHandlesSelectedFile() {
        FileFollowingPane pane = new FileFollowingPane();

        tabbedPane.setSelectedComponent(pane);

        assertSame(pane, tabbedPane.getSelectedComponent());

        // Verify handleSelectedFile() was called after setting the selected component
        // by checking if attributes' lastFileChooserDirectory was updated
        File parent = pane.getFollowedFile().getParentFile();
        assertEquals(parent, attributes.getLastFileChooserDirectory());
    }

    // Other test cases for setSelectedComponent method ...

    @Test
    public void testRemoveTabAt_WhenValidIndex_RemovesTabAndHandlesSelectedFile() {
        int index = 0; // Assuming there is at least 1 tab

        tabbedPane.removeTabAt(index);
        
        assertEquals(0, tabbedPane.getTabCount()); // Verify tab is removed

        // Verify handleSelectedFile() was called after removing the tab
        // by checking if attributes' lastFileChooserDirectory was updated
        assertNull(attributes.getLastFileChooserDirectory());
    }

    // Other test cases for removeTabAt method ...

}