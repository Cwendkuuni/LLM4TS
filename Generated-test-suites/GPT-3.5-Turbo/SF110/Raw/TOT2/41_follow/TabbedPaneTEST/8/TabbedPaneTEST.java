import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.awt.Component;
import java.io.File;
import javax.swing.JTabbedPane;
import ghm.follow.config.FollowAppAttributes;

public class TabbedPaneTest {
    
    private TabbedPane tabbedPane;
    
    @Before
    public void setUp() {
        FollowAppAttributes attributes = new FollowAppAttributes();
        tabbedPane = new TabbedPane(attributes);
    }

    @Test
    public void testFindComponentAt() {
        Component comp = tabbedPane.findComponentAt(0, 0);
        assertNull(comp);
    }

    @Test
    public void testSetSelectedIndex() {
        tabbedPane.setSelectedIndex(0);
        // Test additional functionality here if needed
    }

    @Test
    public void testSetSelectedComponent() {
        FileFollowingPane pane = new FileFollowingPane(new File("test.txt"));
        tabbedPane.setSelectedComponent(pane);
        // Test additional functionality here if needed
    }

    @Test
    public void testRemoveTabAt() {
        tabbedPane.removeTabAt(0);
        // Test additional functionality here if needed
    }

    @Test
    public void testHandleSelectedFile() {
        // Set up the tabbedPane to have a selected component
        FileFollowingPane pane = new FileFollowingPane(new File("test.txt"));
        tabbedPane.setSelectedComponent(pane);

        tabbedPane.handleSelectedFile();
        assertEquals(new File("test.txt").getParentFile(), tabbedPane.attributes.getLastFileChooserDirectory());
    }
}