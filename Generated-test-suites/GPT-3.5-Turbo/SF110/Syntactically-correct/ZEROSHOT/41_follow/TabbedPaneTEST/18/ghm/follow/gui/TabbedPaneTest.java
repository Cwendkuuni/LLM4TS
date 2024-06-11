package ghm.follow.gui;

import org.junit.Test;
import javax.swing.*;
import ghm.follow.config.*;
import java.awt.*;
import java.io.*;

public class TabbedPaneTest {

    private FollowAppAttributes attributes;
    private TabbedPane tabbedPane;

    @Before
    public void setUp() {
        this.attributes = new FollowAppAttributes();
        this.tabbedPane = new TabbedPane(attributes);
    }

    @Test
    public void testFindComponentAt() {
        // Test case 1
        Component result1 = tabbedPane.findComponentAt(10,10);
        assertNull(result1);

        // Test case 2
        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 100, 100);

        Component result2 = tabbedPane.findComponentAt(50, 50);
        assertEquals(panel, result2);

        // Test case 3
        JPanel nestedPanel = new JPanel();
        nestedPanel.setBounds(10, 10, 50, 50);
        panel.add(nestedPanel);

        Component result3 = tabbedPane.findComponentAt(60, 60);
        assertEquals(nestedPanel, result3);
    }

    @Test
    public void testSetSelectedIndex() {
        // Test case 1
        tabbedPane.addTab("Tab 1", new JPanel());
        tabbedPane.addTab("Tab 2", new JPanel());
        tabbedPane.setSelectedIndex(0);
        assertEquals(0, tabbedPane.getSelectedIndex());

        // Test case 2
        tabbedPane.setSelectedIndex(1);
        assertEquals(1, tabbedPane.getSelectedIndex());
    }

    @Test
    public void testSetSelectedComponent() {
        // Test case 1
        FileFollowingPane pane1 = new FileFollowingPane();
        FileFollowingPane pane2 = new FileFollowingPane();
        tabbedPane.add(pane1);
        tabbedPane.add(pane2);
        tabbedPane.setSelectedComponent(pane1);
        assertEquals(pane1, tabbedPane.getSelectedComponent());

        // Test case 2
        tabbedPane.setSelectedComponent(pane2);
        assertEquals(pane2, tabbedPane.getSelectedComponent());
    }

    @Test
    public void testRemoveTabAt() {
        // Test case 1
        tabbedPane.addTab("Tab 1", new JPanel());
        tabbedPane.addTab("Tab 2", new JPanel());
        tabbedPane.removeTabAt(0);
        assertEquals(1, tabbedPane.getTabCount());

        // Test case 2
        tabbedPane.removeTabAt(0);
        assertEquals(0, tabbedPane.getTabCount());
    }

    @Test
    public void testHandleSelectedFile() {
        // Test case 1
        FileFollowingPane pane = new FileFollowingPane();
        pane.setFollowedFile(new File("test.txt"));
        tabbedPane.add(pane);
        tabbedPane.setSelectedComponent(pane);
        assertEquals(new File("."), attributes.getLastFileChooserDirectory());

        // Test case 2
        FileFollowingPane pane2 = new FileFollowingPane();
        pane2.setFollowedFile(new File("example.txt"));
        tabbedPane.add(pane2);
        tabbedPane.setSelectedComponent(pane2);
        assertEquals(new File("."), attributes.getLastFileChooserDirectory());
    }
}