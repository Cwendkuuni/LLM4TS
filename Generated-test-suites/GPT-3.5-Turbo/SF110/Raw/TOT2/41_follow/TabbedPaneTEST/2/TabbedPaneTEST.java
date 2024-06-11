import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import org.mockito.Mockito;
import ghm.follow.config.FollowAppAttributes;

public class TabbedPaneTest {

    private FollowAppAttributes attributes;
    private TabbedPane tabbedPane;

    @Test
    public void testFindComponentAt() {
        tabbedPane = new TabbedPane(attributes);
        // Test normal functionality
        assertNotNull(tabbedPane.findComponentAt(50, 50));
        // Test when x and y coordinates are outside the tabbed pane
        assertNull(tabbedPane.findComponentAt(-10, -10));
    }

    @Test
    public void testSetSelectedIndex() {
        tabbedPane = new TabbedPane(attributes);
        // Test setting selected index
        tabbedPane.setSelectedIndex(0);
        // Add assertion here
        // Test handleSelectedFile method is called after setting index
    }

    @Test
    public void testSetSelectedComponent() {
        tabbedPane = new TabbedPane(attributes);
        // Create a mock FileFollowingPane
        FileFollowingPane paneMock = Mockito.mock(FileFollowingPane.class);
        tabbedPane.setSelectedComponent(paneMock);
        // Add assertion here
        // Test handleSelectedFile method is called after setting selected component
    }

    @Test
    public void testRemoveTabAt() {
        tabbedPane = new TabbedPane(attributes);
        // Add tab first
        tabbedPane.addTab("New Tab", new JPanel());
        // Test removing tab
        tabbedPane.removeTabAt(0);
        // Test handleSelectedFile method is called after removing tab
    }

    @Test
    public void testHandleSelectedFile() {
        tabbedPane = new TabbedPane(attributes);
        // Create a mock FileFollowingPane
        FileFollowingPane paneMock = Mockito.mock(FileFollowingPane.class);
        Mockito.when(paneMock.getFollowedFile()).thenReturn(new File("testFile.txt"));
        tabbedPane.setSelectedComponent(paneMock);
        tabbedPane.handleSelectedFile();
        // Add assertion here
        // Test if last file chooser directory is set correctly
    }
}