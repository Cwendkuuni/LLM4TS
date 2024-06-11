package ghm.follow.gui;

import static org.junit.Assert.*;
import org.junit.Test;
import javax.swing.*;
import ghm.follow.config.*;
import java.awt.*;
import java.io.*;
import java.io.File;

public class TabbedPaneTest {

    @Test
    public void testFindComponentAt() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        Component component = tabbedPane.findComponentAt(0, 0);
        assertNull(component);
        
        component = tabbedPane.findComponentAt(100, 100);
        assertNotNull(component);
        
        component = tabbedPane.findComponentAt(-10, -10);
        assertNull(component);
    }

    @Test
    public void testSetSelectedIndex() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        tabbedPane.add(new FileFollowingPane(new File("file1.txt")));
        tabbedPane.add(new FileFollowingPane(new File("file2.txt")));
        tabbedPane.setSelectedIndex(0);
        assertEquals(0, tabbedPane.getSelectedIndex());
        
        tabbedPane.setSelectedIndex(1);
        assertEquals(1, tabbedPane.getSelectedIndex());
    }

    @Test
    public void testSetSelectedComponent() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        FileFollowingPane pane1 = new FileFollowingPane(new File("file1.txt"));
        tabbedPane.add(pane1);
        FileFollowingPane pane2 = new FileFollowingPane(new File("file2.txt"));
        tabbedPane.add(pane2);
        tabbedPane.setSelectedComponent(pane1);
        assertEquals(pane1, tabbedPane.getSelectedComponent());
        
        tabbedPane.setSelectedComponent(pane2);
        assertEquals(pane2, tabbedPane.getSelectedComponent());
    }

    @Test
    public void testRemoveTabAt() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        FileFollowingPane pane1 = new FileFollowingPane(new File("file1.txt"));
        tabbedPane.add(pane1);
        FileFollowingPane pane2 = new FileFollowingPane(new File("file2.txt"));
        tabbedPane.add(pane2);
        tabbedPane.removeTabAt(1);
        assertEquals(1, tabbedPane.getTabCount());
        
        tabbedPane.removeTabAt(0);
        assertEquals(0, tabbedPane.getTabCount());
    }

    @Test
    public void testHandleSelectedFile() {
        FollowAppAttributes attributes = new FollowAppAttributes();
        TabbedPane tabbedPane = new TabbedPane(attributes);
        FileFollowingPane pane1 = new FileFollowingPane(new File("file1.txt"));
        tabbedPane.add(pane1);
        tabbedPane.setSelectedComponent(pane1);
        
        tabbedPane.handleSelectedFile();
        assertEquals(pane1.getFollowedFile().getParentFile(), attributes.getLastFileChooserDirectory());
        
        FileFollowingPane pane2 = new FileFollowingPane(new File("file2.txt"));
        tabbedPane.add(pane2);
        tabbedPane.setSelectedComponent(pane2);
        
        tabbedPane.handleSelectedFile();
        assertEquals(pane2.getFollowedFile().getParentFile(), attributes.getLastFileChooserDirectory());
    }
}