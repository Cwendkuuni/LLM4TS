package ghm.follow.gui;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import javax.swing.*;
import ghm.follow.config.*;
import java.awt.*;
import java.io.*;

public class TabbedPaneTest {

    private TabbedPane tabbedPane;
    private FollowAppAttributes attributes;
    private FileFollowingPane pane;

    @Before
    public void setUp() {
        attributes = new FollowAppAttributes();
        tabbedPane = new TabbedPane(attributes);
        pane = new FileFollowingPane(new File("testFile.txt"));
    }

    @Test
    public void testFindComponentAt() {
        assertNull(tabbedPane.findComponentAt(0, 0));
        tabbedPane.add(pane);
        assertSame(pane, tabbedPane.findComponentAt(0, 0));
    }

    @Test
    public void testSetSelectedIndex() {
        tabbedPane.setSelectedIndex(0);
        assertEquals(0, tabbedPane.getSelectedIndex());
    }

    @Test
    public void testSetSelectedComponent() {
        tabbedPane.setSelectedComponent(pane);
        assertSame(pane, tabbedPane.getSelectedComponent());
    }

    @Test
    public void testRemoveTabAt() {
        tabbedPane.add(pane);
        tabbedPane.removeTabAt(0);
        assertEquals(0, tabbedPane.getTabCount());
    }

    @Test
    public void testHandleSelectedFile() {
        tabbedPane.add(pane);
        tabbedPane.setSelectedIndex(0);
        assertEquals(new File("testFile.txt").getParentFile(), attributes.getLastFileChooserDirectory());
    }
}