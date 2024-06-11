package ghm.follow.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import javax.swing.*;
import ghm.follow.config.*;
import java.awt.*;
import java.io.*;

public class TabbedPaneTest {
    @Test
    public void testFindComponentAt() {
        FollowAppAttributes attributes = new FollowAppAttributes();
        TabbedPane tabbedPane = new TabbedPane(attributes);

        // Test Case 1: x and y values outside the bounds of the component
        assertNull(tabbedPane.findComponentAt(-1, -1));

        // Test Case 2: x and y values inside the bounds of the component
        JPanel panel = new JPanel();
        panel.setBounds(10, 10, 100, 100);
        tabbedPane.add(panel);
        assertEquals(panel, tabbedPane.findComponentAt(50, 50));
      
        // Add more test cases for different scenarios
    }

    @Test
    public void testSetSelectedIndex() {
        FollowAppAttributes attributes = new FollowAppAttributes();
        TabbedPane tabbedPane = new TabbedPane(attributes);

        // Test Case 1: Valid index
        tabbedPane.addTab("Tab 1", new JPanel());
        tabbedPane.addTab("Tab 2", new JPanel());
        tabbedPane.setSelectedIndex(1);
        assertEquals(1, tabbedPane.getSelectedIndex());

        // Test Case 2: Invalid index (out of bounds)
        tabbedPane.setSelectedIndex(100);
        // Add assertion for expected behavior

        // Add more test cases for different scenarios
    }

    @Test
    public void testSetSelectedComponent() {
        FollowAppAttributes attributes = new FollowAppAttributes();
        TabbedPane tabbedPane = new TabbedPane(attributes);

        // Test Case 1: Valid component
        JPanel panel = new JPanel();
        tabbedPane.addTab("Tab 1", panel);
        tabbedPane.setSelectedComponent(panel);
        assertEquals(panel, tabbedPane.getSelectedComponent());

        // Test Case 2: Invalid component (not present in the tabbed pane)
        JPanel invalidPanel = new JPanel();
        tabbedPane.setSelectedComponent(invalidPanel);
        // Add assertion for expected behavior

        // Add more test cases for different scenarios
    }

    @Test
    public void testRemoveTabAt() {
        FollowAppAttributes attributes = new FollowAppAttributes();
        TabbedPane tabbedPane = new TabbedPane(attributes);

        // Test Case 1: Valid index
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        tabbedPane.addTab("Tab 1", panel1);
        tabbedPane.addTab("Tab 2", panel2);
        tabbedPane.removeTabAt(1);
        assertEquals(1, tabbedPane.getTabCount());

        // Test Case 2: Invalid index (out of bounds)
        tabbedPane.removeTabAt(100);
        // Add assertion for expected behavior

        // Add more test cases for different scenarios
    }

    @Test
    public void testHandleSelectedFile() {
        FollowAppAttributes attributes = new FollowAppAttributes();
        TabbedPane tabbedPane = new TabbedPane(attributes);

        // Test Case 1: Selected component is null
        tabbedPane.handleSelectedFile();
        // Add assertion for expected behavior

        // Test Case 2: Selected component is not null
        FileFollowingPane pane = new FileFollowingPane();
        attributes.setLastFileChooserDirectory(new File("directory"));
        tabbedPane.addTab("Tab 1", pane);
        tabbedPane.setSelectedComponent(pane);
        tabbedPane.handleSelectedFile();
        assertEquals(new File("directory"), attributes.getLastFileChooserDirectory());

        // Add more test cases for different scenarios
    }
}