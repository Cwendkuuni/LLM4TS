import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import javax.swing.*;
import java.awt.*;
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
    public void testFindComponentAt() {
        Component comp = tabbedPane.findComponentAt(100, 100);
        assertNull(comp);
    }

    @Test
    public void testSetSelectedIndex() {
        tabbedPane.setSelectedIndex(0);
        // Verify if the method changes the selected index correctly
        assertEquals(0, tabbedPane.getSelectedIndex());
    }

    @Test
    public void testSetSelectedComponent() {
        FileFollowingPane pane = new FileFollowingPane(new File("test.txt"));
        tabbedPane.setSelectedComponent(pane);
        // Verify if the selected component is set correctly
        assertEquals(pane, tabbedPane.getSelectedComponent());
    }

    @Test
    public void testRemoveTabAt() {
        // Test if the method removes a tab at given index without errors
        tabbedPane.add("Tab 1", new JPanel());
        tabbedPane.add("Tab 2", new JPanel());
        tabbedPane.removeTabAt(1);
        assertEquals(1, tabbedPane.getTabCount());
    }

    @Test
    public void testHandleSelectedFile() {
        File file = new File("test.txt");
        FileFollowingPane pane = new FileFollowingPane(file);
        tabbedPane.setSelectedComponent(pane);
        tabbedPane.handleSelectedFile();
        // Verify if the selected file's directory is correctly set in attributes
        assertEquals(file.getParentFile(), attributes.getLastFileChooserDirectory());
    }
}