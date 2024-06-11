package ghm.follow.gui;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import ghm.follow.config.*;
import java.awt.*;
import java.io.*;

public class TabbedPaneTest {

    private TabbedPane tabbedPane;

    @Before
    public void setUp() {
        FollowAppAttributes attributes = new FollowAppAttributes();
        tabbedPane = new TabbedPane(attributes);
    }

    @Test
    public void testFindComponentAt_Contains() {
        Component component = tabbedPane.findComponentAt(10, 10);
        Assert.assertNull(component);
    }

    @Test
    public void testFindComponentAt_NotContains() {
        Component component = tabbedPane.findComponentAt(-10, -10);
        Assert.assertNull(component);
    }

    @Test
    public void testFindComponentAt_Container() {
        JPanel container = new JPanel();
        container.setBounds(0, 0, 100, 100);
        tabbedPane.add(container);
        Component component = tabbedPane.findComponentAt(50, 50);
        Assert.assertEquals(container, component);
    }

    @Test
    public void testFindComponentAt_Component() {
        JLabel label = new JLabel();
        label.setBounds(0, 0, 50, 50);
        tabbedPane.add(label);
        Component component = tabbedPane.findComponentAt(25, 25);
        Assert.assertEquals(label, component);
    }

    @Test
    public void testSetSelectedIndex() {
        int initialIndex = tabbedPane.getSelectedIndex();
        int newIndex = initialIndex + 1;
        tabbedPane.setSelectedIndex(newIndex);
        Assert.assertEquals(newIndex, tabbedPane.getSelectedIndex());
    }

    @Test
    public void testSetSelectedComponent() {
        FileFollowingPane pane = new FileFollowingPane();
        tabbedPane.add(pane);
        tabbedPane.setSelectedComponent(pane);
        Assert.assertEquals(pane, tabbedPane.getSelectedComponent());
    }

    @Test
    public void testRemoveTabAt() {
        FileFollowingPane pane = new FileFollowingPane();
        tabbedPane.add(pane);
        int initialCount = tabbedPane.getTabCount();
        tabbedPane.removeTabAt(0);
        int newCount = tabbedPane.getTabCount();
        Assert.assertEquals(initialCount - 1, newCount);
    }
  
    @Test
    public void testHandleSelectedFile() {
        FileFollowingPane pane = new FileFollowingPane();
        pane.setFollowedFile(new File("test.txt"));
        tabbedPane.add(pane);
        tabbedPane.setSelectedComponent(pane);
        tabbedPane.handleSelectedFile();
        File parent = pane.getFollowedFile().getParentFile();
        Assert.assertEquals(parent, tabbedPane.attributes.getLastFileChooserDirectory());
    }

}