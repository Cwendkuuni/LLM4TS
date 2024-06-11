package ghm.follow.gui;

import static org.junit.Assert.*;
import org.junit.Test;
import javax.swing.*;
import ghm.follow.config.*;
import java.awt.*;
import java.io.*;

public class TabbedPaneTest {
    
    @Test
    public void testFindComponentAt() {
        // Test when component is null
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        Component component = tabbedPane.findComponentAt(0, 0);
        assertNull(component);
        
        // Test when component is not null
        JPanel panel = new JPanel();
        tabbedPane.addTab("Panel", panel);
        component = tabbedPane.findComponentAt(0, 0);
        assertNotNull(component);
        assertEquals(panel, component);
    }
    
    @Test
    public void testSetSelectedIndex() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        tabbedPane.addTab("Tab 1", new JPanel());
        tabbedPane.addTab("Tab 2", new JPanel());
        
        // Test when index is within range
        tabbedPane.setSelectedIndex(1);
        assertEquals(1, tabbedPane.getSelectedIndex());
        
        // Test when index is out of range
        tabbedPane.setSelectedIndex(10);
        assertEquals(1, tabbedPane.getSelectedIndex());
    }
    
    @Test
    public void testSetSelectedComponent() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        FileFollowingPane pane1 = new FileFollowingPane(new File("file1.txt"));
        FileFollowingPane pane2 = new FileFollowingPane(new File("file2.txt"));
        tabbedPane.addTab("Pane 1", pane1);
        tabbedPane.addTab("Pane 2", pane2);
        
        // Test when selected component is valid
        tabbedPane.setSelectedComponent(pane2);
        assertEquals(pane2, tabbedPane.getSelectedComponent());
        
        // Test when selected component is null
        tabbedPane.setSelectedComponent(null);
        assertNull(tabbedPane.getSelectedComponent());
    }
    
    @Test
    public void testRemoveTabAt() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        tabbedPane.addTab("Panel 1", panel1);
        tabbedPane.addTab("Panel 2", panel2);
        
        // Test when index is within range
        tabbedPane.removeTabAt(1);
        assertEquals(1, tabbedPane.getTabCount());
        assertFalse(tabbedPane.isAncestorOf(panel2));
        
        // Test when index is out of range
        tabbedPane.removeTabAt(10);
        assertEquals(1, tabbedPane.getTabCount());
    }
    
    @Test
    public void testHandleSelectedFile() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        FileFollowingPane pane = new FileFollowingPane(new File("file.txt"));
        tabbedPane.addTab("Pane", pane);
        
        // Test when selected component is not null
        tabbedPane.setSelectedComponent(pane);
        tabbedPane.handleSelectedFile();
        assertEquals(new File("."), tabbedPane.getAttributes().getLastFileChooserDirectory());
        
        // Test when selected component is null
        tabbedPane.setSelectedComponent(null);
        tabbedPane.handleSelectedFile();
        assertNull(tabbedPane.getAttributes().getLastFileChooserDirectory());
    }
    
    // Additional test cases for getters and setters:
    
    @Test
    public void testGetAttributes() {
        FollowAppAttributes attributes = new FollowAppAttributes();
        TabbedPane tabbedPane1 = new TabbedPane(attributes);
        TabbedPane tabbedPane2 = new TabbedPane(attributes);
        
        assertEquals(attributes, tabbedPane1.getAttributes());
        assertEquals(attributes, tabbedPane2.getAttributes());
    }
    
    @Test
    public void testSetAttributes() {
        FollowAppAttributes attributes1 = new FollowAppAttributes();
        FollowAppAttributes attributes2 = new FollowAppAttributes();
        TabbedPane tabbedPane = new TabbedPane(attributes1);
        
        tabbedPane.setAttributes(attributes2);
        assertEquals(attributes2, tabbedPane.getAttributes());
    }
}