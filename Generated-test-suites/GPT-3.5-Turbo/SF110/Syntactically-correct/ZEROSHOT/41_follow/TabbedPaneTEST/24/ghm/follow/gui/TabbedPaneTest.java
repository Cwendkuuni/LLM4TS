package ghm.follow.gui;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
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
    public void testFindComponentAt_Null() {
        TabbedPane pane = new TabbedPane(attributes);
        Component result = pane.findComponentAt(0, 0);
        assertNull(result);
    }

    @Test
    public void testFindComponentAt_VisibleContainer() {
        TabbedPane pane = new TabbedPane(attributes);
        JPanel container = new JPanel();
        container.setVisible(true);
        pane.add(container);
        Component result = pane.findComponentAt(10, 10);
        assertEquals(container, result);
    }

    @Test
    public void testFindComponentAt_InvisibleContainer() {
        TabbedPane pane = new TabbedPane(attributes);
        JPanel container = new JPanel();
        container.setVisible(false);
        pane.add(container);
        Component result = pane.findComponentAt(10, 10);
        assertEquals(pane, result);
    }

    @Test
    public void testFindComponentAt_NestedContainer() {
        TabbedPane pane = new TabbedPane(attributes);
        JPanel container = new JPanel();
        container.setVisible(true);
        JPanel nestedContainer = new JPanel();
        nestedContainer.setVisible(true);
        container.add(nestedContainer);
        pane.add(container);
        Component result = pane.findComponentAt(10, 10);
        assertEquals(nestedContainer, result);
    }

    @Test
    public void testFindComponentAt_NestedInvisibleContainer() {
        TabbedPane pane = new TabbedPane(attributes);
        JPanel container = new JPanel();
        container.setVisible(true);
        JPanel nestedContainer = new JPanel();
        nestedContainer.setVisible(false);
        container.add(nestedContainer);
        pane.add(container);
        Component result = pane.findComponentAt(10, 10);
        assertEquals(container, result);
    }

    @Test
    public void testSetSelectedIndex() {
        TabbedPane pane = new TabbedPane(attributes);
        pane.addTab("Tab1", new JLabel("Tab 1"));
        pane.addTab("Tab2", new JLabel("Tab 2"));
        pane.setSelectedIndex(1);
        assertEquals(1, pane.getSelectedIndex());
    }

    @Test
    public void testSetSelectedComponent() {
        TabbedPane pane = new TabbedPane(attributes);
        FileFollowingPane filePane1 = new FileFollowingPane();
        FileFollowingPane filePane2 = new FileFollowingPane();
        pane.addTab("Tab1", filePane1);
        pane.addTab("Tab2", filePane2);
        pane.setSelectedComponent(filePane2);
        assertEquals(filePane2, pane.getSelectedComponent());
    }

    @Test
    public void testRemoveTabAt() {
        TabbedPane pane = new TabbedPane(attributes);
        FileFollowingPane filePane1 = new FileFollowingPane();
        FileFollowingPane filePane2 = new FileFollowingPane();
        pane.addTab("Tab1", filePane1);
        pane.addTab("Tab2", filePane2);
        pane.removeTabAt(1);
        assertEquals(1, pane.getTabCount());
    }

    @Test
    public void testHandleSelectedFile() {
        TabbedPane pane = new TabbedPane(attributes);
        FileFollowingPane filePane = new FileFollowingPane();
        pane.addTab("Tab1", filePane);
        pane.setSelectedComponent(filePane);
        pane.handleSelectedFile();
        File parent = filePane.getFollowedFile().getParentFile();
        assertEquals(parent, attributes.getLastFileChooserDirectory());
    }
}