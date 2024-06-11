package ghm.follow.gui;
import org.junit.Test;
import junit.framework.TestCase;
import javax.swing.*;
import ghm.follow.config.*;
import java.awt.*;
import java.io.*;

public class TabbedPaneTest extends TestCase {

    private FollowAppAttributes attributes;

    public void setUp() {
        attributes = new FollowAppAttributes();
    }

    @Test
    public void testFindComponentAt() {
        TabbedPane tabbedPane = new TabbedPane(attributes);
        //Test when x and y are outside the tabbed pane
        Component result = tabbedPane.findComponentAt(100, 100);
        assertNull(result);
        
        //Test when x and y are inside the tabbed pane
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        tabbedPane.add("Panel 1", panel1);
        tabbedPane.add("Panel 2", panel2);
        
        result = tabbedPane.findComponentAt(50, 50);
        assertEquals(panel1, result);
        
        result = tabbedPane.findComponentAt(200, 200);
        assertEquals(tabbedPane, result);
    }

    @Test
    public void testSetSelectedIndex() {
        TabbedPane tabbedPane = new TabbedPane(attributes);
        
        //Test when index is within the valid range
        tabbedPane.add("Panel 1", new JPanel());
        tabbedPane.add("Panel 2", new JPanel());
        
        tabbedPane.setSelectedIndex(1);
        assertEquals(1, tabbedPane.getSelectedIndex());

        //Test when index is out of range
        tabbedPane.setSelectedIndex(2);
        assertEquals(1, tabbedPane.getSelectedIndex());
    }

    @Test
    public void testSetSelectedComponent() {
        TabbedPane tabbedPane = new TabbedPane(attributes);
        FileFollowingPane pane1 = new FileFollowingPane(new File("test1.txt"));
        FileFollowingPane pane2 = new FileFollowingPane(new File("test2.txt"));

        tabbedPane.addTab("File 1", pane1);
        tabbedPane.addTab("File 2", pane2);
        
        tabbedPane.setSelectedComponent(pane2);
        assertEquals(pane2, tabbedPane.getSelectedComponent());
    }

    @Test
    public void testRemoveTabAt() {
        TabbedPane tabbedPane = new TabbedPane(attributes);
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        
        tabbedPane.add("Panel 1", panel1);
        tabbedPane.add("Panel 2", panel2);
        
        tabbedPane.removeTabAt(1);
        assertEquals(1, tabbedPane.getTabCount());
        assertEquals("Panel 1", tabbedPane.getTitleAt(0));
    }

    @Test
    public void testHandleSelectedFile() {
        TabbedPane tabbedPane = new TabbedPane(attributes);
        FileFollowingPane pane = new FileFollowingPane(new File("test.txt"));
        JPanel panel = new JPanel();
        
        tabbedPane.add("File 1", pane);
        tabbedPane.add("Panel 1", panel);
        
        tabbedPane.setSelectedComponent(pane);
        tabbedPane.handleSelectedFile();
        
        File parent = pane.getFollowedFile().getParentFile();
        assertEquals(parent, attributes.getLastFileChooserDirectory());
    }
}