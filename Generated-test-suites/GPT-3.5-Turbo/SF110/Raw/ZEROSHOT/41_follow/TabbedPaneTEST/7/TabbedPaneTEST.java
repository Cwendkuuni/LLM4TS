package ghm.follow.gui;

import org.junit.*;
import static org.junit.Assert.*;
import javax.swing.*;
import ghm.follow.config.*;
import java.awt.*;
import java.io.*;

public class TabbedPaneTest {

    private FollowAppAttributes attributes;

    @Before
    public void setUp() {
        attributes = new FollowAppAttributes();
    }

    @Test
    public void testFindComponentAt_InvalidCoordinates_ReturnsNull() {
        TabbedPane tabbedPane = new TabbedPane(attributes);
        Component result = tabbedPane.findComponentAt(-1, -1);
        assertNull(result);
    }

    @Test
    public void testFindComponentAt_ValidCoordinates_ReturnsComponent() {
        TabbedPane tabbedPane = new TabbedPane(attributes);
        JPanel panel = new JPanel();
        tabbedPane.add(panel);
        Component result = tabbedPane.findComponentAt(10, 10);
        assertEquals(panel, result);
    }

    @Test
    public void testSetSelectedIndex_HandlesSelectedFile() {
        TabbedPane tabbedPane = new TabbedPane(attributes);
        FileFollowingPane pane = new FileFollowingPane();
        tabbedPane.add(pane);
        tabbedPane.setSelectedIndex(0);
        File result = attributes.getLastFileChooserDirectory();
        assertEquals(pane.getFollowedFile().getParentFile(), result);
    }

    @Test
    public void testSetSelectedComponent_HandlesSelectedFile() {
        TabbedPane tabbedPane = new TabbedPane(attributes);
        FileFollowingPane pane = new FileFollowingPane();
        tabbedPane.setSelectedComponent(pane);
        File result = attributes.getLastFileChooserDirectory();
        assertEquals(pane.getFollowedFile().getParentFile(), result);
    }

    @Test
    public void testRemoveTabAt_HandlesSelectedFile() {
        TabbedPane tabbedPane = new TabbedPane(attributes);
        FileFollowingPane pane = new FileFollowingPane();
        tabbedPane.add(pane);
        tabbedPane.removeTabAt(0);
        File result = attributes.getLastFileChooserDirectory();
        assertNull(result);
    }
}