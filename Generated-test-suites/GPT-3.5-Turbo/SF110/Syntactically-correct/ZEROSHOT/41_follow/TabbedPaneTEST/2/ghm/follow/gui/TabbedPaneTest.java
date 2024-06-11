package ghm.follow.gui;

import static org.junit.Assert.*;
import org.junit.Test;

import javax.swing.*;
import ghm.follow.config.*;
import java.awt.*;
import java.io.*;

public class TabbedPaneTest {

    @Test
    public void testFindComponentAt_null() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        Component result = tabbedPane.findComponentAt(0, 0);
        assertNull(result);
    }
    
    @Test
    public void testFindComponentAt_visibleContainer() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        JPanel panel = new JPanel();
        panel.setVisible(true);
        tabbedPane.addTab("Tab", panel);
        
        Component result = tabbedPane.findComponentAt(0, 0);
        assertEquals(panel, result);
    }
    
    @Test
    public void testFindComponentAt_invisibleContainer() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        JPanel panel = new JPanel();
        panel.setVisible(false);
        tabbedPane.addTab("Tab", panel);
        
        Component result = tabbedPane.findComponentAt(0, 0);
        assertEquals(tabbedPane, result);
    }
    
    @Test
    public void testFindComponentAt_visibleComponent() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        JComponent component = new JPanel();
        JPanel panel = new JPanel();
        panel.add(component);
        tabbedPane.addTab("Tab", panel);
        
        Component result = tabbedPane.findComponentAt(0, 0);
        assertEquals(component, result);
    }
    
    @Test
    public void testFindComponentAt_invisibleComponent() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        JComponent component = new JPanel();
        component.setVisible(false);
        JPanel panel = new JPanel();
        panel.add(component);
        tabbedPane.addTab("Tab", panel);
        
        Component result = tabbedPane.findComponentAt(0, 0);
        assertEquals(tabbedPane, result);
    }
    
    @Test
    public void testSetSelectedIndex() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        int index = 1;
        tabbedPane.addTab("Tab1", new JPanel());
        tabbedPane.addTab("Tab2", new JPanel());
        tabbedPane.setSelectedIndex(index);
        
        assertEquals(index, tabbedPane.getSelectedIndex());
    }
    
    @Test
    public void testSetSelectedComponent() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        FileFollowingPane pane = new FileFollowingPane();
        tabbedPane.addTab("Tab", pane);
        tabbedPane.setSelectedComponent(pane);
        
        assertEquals(pane, tabbedPane.getSelectedComponent());
    }
    
    @Test
    public void testRemoveTabAt() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        int index = 1;
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        tabbedPane.addTab("Tab1", panel1);
        tabbedPane.addTab("Tab2", panel2);
        
        tabbedPane.removeTabAt(index);
        assertEquals(1, tabbedPane.getTabCount());
        assertEquals(panel1, tabbedPane.getComponentAt(0));
    }
    
    @Test
    public void testHandleSelectedFile() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        File file = new File("test.txt");
        FileFollowingPane pane = new FileFollowingPane(file);
        
        tabbedPane.addTab("Tab", pane);
        tabbedPane.setSelectedComponent(pane);
        tabbedPane.handleSelectedFile();
        
        assertEquals(file.getParentFile(), tabbedPane.attributes.getLastFileChooserDirectory());
    }
}