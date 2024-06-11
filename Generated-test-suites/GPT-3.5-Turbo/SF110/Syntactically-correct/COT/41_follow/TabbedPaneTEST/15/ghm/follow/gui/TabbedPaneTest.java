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
    private TabbedPane tabbedPane;
    
    @Before
    public void setUp() {
        attributes = new FollowAppAttributes();
        tabbedPane = new TabbedPane(attributes);
    }
    
    @Test
    public void testFindComponentAt() {
        assertNull(tabbedPane.findComponentAt(0, 0));
        // Test with a visible component
        JPanel panel1 = new JPanel();
        panel1.setBounds(0, 0, 100, 100);
        tabbedPane.add(panel1);
        assertEquals(panel1, tabbedPane.findComponentAt(50, 50));
        // Test with an invisible component
        JPanel panel2 = new JPanel();
        panel2.setBounds(0, 0, 100, 100);
        panel2.setVisible(false);
        tabbedPane.add(panel2);
        assertNull(tabbedPane.findComponentAt(50, 50));
    }
    
    @Test
    public void testSetSelectedIndex() {
        int index = 0;
        tabbedPane.setSelectedIndex(index);
        assertEquals(index, tabbedPane.getSelectedIndex());
        // Add a component to test handleSelectedFile()
        FileFollowingPane pane = new FileFollowingPane();
        tabbedPane.add(pane);
        pane.setFollowedFile(new File("test.txt"));
        tabbedPane.setSelectedIndex(index + 1);
        assertEquals(new File("test.txt").getParentFile(), attributes.getLastFileChooserDirectory());
    }
    
    @Test
    public void testSetSelectedComponent() {
        FileFollowingPane pane = new FileFollowingPane();
        tabbedPane.setSelectedComponent(pane);
        assertEquals(pane, tabbedPane.getSelectedComponent());
        // Test handleSelectedFile()
        pane.setFollowedFile(new File("test.txt"));
        assertEquals(new File("test.txt").getParentFile(), attributes.getLastFileChooserDirectory());
    }
    
    @Test
    public void testRemoveTabAt() {
        int index = 0;
        // Add a component to test handleSelectedFile()
        FileFollowingPane pane = new FileFollowingPane();
        tabbedPane.add(pane);
        pane.setFollowedFile(new File("test.txt"));
        tabbedPane.removeTabAt(index);
        assertNull(tabbedPane.getSelectedComponent());
        assertEquals(null, attributes.getLastFileChooserDirectory());
    }
}