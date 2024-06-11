
import org.junit.*;
import static org.junit.Assert.*;
import javax.swing.*;
import ghm.follow.config.*;
import java.awt.*;
import java.io.*;

public class TabbedPaneTest {

    private FollowAppAttributes attributes;
    private TabbedPane tabbedPane;
    private FileFollowingPane pane;

    @Before
    public void setUp() {
        attributes = new FollowAppAttributes();
        tabbedPane = new TabbedPane(attributes);
        pane = new FileFollowingPane(new File("test.txt"));
    }

    @Test
    public void testFindComponentAt() {
        tabbedPane.addTab("Test", pane);
        Component result = tabbedPane.findComponentAt(10, 10);
        assertEquals(pane, result);
    }

    @Test
    public void testSetSelectedIndex() {
        tabbedPane.addTab("Test", pane);
        tabbedPane.setSelectedIndex(0);
        int selectedIndex = tabbedPane.getSelectedIndex();
        assertEquals(0, selectedIndex);
    }

    @Test
    public void testSetSelectedComponent() {
        tabbedPane.addTab("Test", pane);
        tabbedPane.setSelectedComponent(pane);
        Component selectedComponent = tabbedPane.getSelectedComponent();
        assertEquals(pane, selectedComponent);
    }

    @Test
    public void testRemoveTabAt() {
        tabbedPane.addTab("Test", pane);
        tabbedPane.removeTabAt(0);
        int tabCount = tabbedPane.getTabCount();
        assertEquals(0, tabCount);
    }

    @Test
    public void testHandleSelectedFile() {
        tabbedPane.addTab("Test", pane);
        tabbedPane.setSelectedComponent(pane);
        tabbedPane.handleSelectedFile();
        File lastFileChooserDirectory = attributes.getLastFileChooserDirectory();
        assertEquals(new File("").getAbsolutePath(), lastFileChooserDirectory.getAbsolutePath());
    }

}

