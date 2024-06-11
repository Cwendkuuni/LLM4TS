import static org.junit.Assert.*;
import org.junit.Test;

public class TabbedPaneTest {
    private FollowAppAttributes attributes = new FollowAppAttributes();
    private TabbedPane tabbedPane = new TabbedPane(attributes);
    private FileFollowingPane pane = new FileFollowingPane();

    @Test
    public void testFindComponentAt_basic() {
        Component result = tabbedPane.findComponentAt(0, 0);
        assertNull(result);
    }

    @Test
    public void testSetSelectedIndex_basic() {
        tabbedPane.setSelectedIndex(0);
        // Check if selected index is set correctly
        assertEquals(0, tabbedPane.getSelectedIndex());
    }

    @Test
    public void testSetSelectedComponent_basic() {
        tabbedPane.setSelectedComponent(pane);
        // Check if selected component is set correctly
        assertEquals(pane, tabbedPane.getSelectedComponent());
    }

    @Test
    public void testHandleSelectedFile_basic() {
        tabbedPane.setSelectedComponent(pane);
        tabbedPane.handleSelectedFile();
        // Check if last file chooser directory is set correctly
        assertEquals(pane.getFollowedFile().getParentFile(), attributes.getLastFileChooserDirectory());
    }

    @Test
    public void testRemoveTabAt_basic() {
        tabbedPane.removeTabAt(0);
        // Check if tab is removed correctly
        assertEquals(0, tabbedPane.getTabCount());
    }
}