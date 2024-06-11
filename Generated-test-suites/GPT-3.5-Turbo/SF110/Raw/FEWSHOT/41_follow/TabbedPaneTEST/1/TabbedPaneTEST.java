
package ghm.follow.gui;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class TabbedPaneTest {

    private TabbedPane tabbedPane;

    @Before
    public void setup() {
        FollowAppAttributes attributes = new FollowAppAttributes();
        tabbedPane = new TabbedPane(attributes);
    }

    @Test
    public void testFindComponentAt_notContained() {
        Component component = tabbedPane.findComponentAt(0, 0);
        assertNull(component);
    }

    @Test
    public void testFindComponentAt_containerVisible() {
        JPanel panel = new JPanel();
        panel.setBounds(0,0,100,100);
        tabbedPane.add(panel);
        Component component = tabbedPane.findComponentAt(50, 50);
        assertEquals(panel, component);
    }

    @Test
    public void testFindComponentAt_componentVisible() {
        JButton button = new JButton();
        button.setBounds(0,0,50,50);
        button.setVisible(true);
        tabbedPane.add(button);
        Component component = tabbedPane.findComponentAt(25, 25);
        assertEquals(button, component);
    }

    @Test
    public void testSetSelectedIndex() {
        tabbedPane.setSelectedIndex(0);
        // Add assertion here
    }

    @Test
    public void testSetSelectedComponent() {
        FileFollowingPane pane = new FileFollowingPane(new File("test.txt"));
        tabbedPane.setSelectedComponent(pane);
        // Add assertion here
    }

    @Test
    public void testRemoveTabAt() {
        tabbedPane.addTab("Tab", new JPanel());
        tabbedPane.removeTabAt(0);
        // Add assertion here
    }

    @Test
    public void testHandleSelectedFile() {
        FollowAppAttributes attributes = new FollowAppAttributes();
        attributes.setLastFileChooserDirectory(new File("parent"));
        tabbedPane = new TabbedPane(attributes);
        FileFollowingPane pane = new FileFollowingPane(new File("test.txt"));
        tabbedPane.setSelectedComponent(pane);
        File expected = new File("test.txt").getParentFile();
        File actual = attributes.getLastFileChooserDirectory();
        assertEquals(expected, actual);
    }
}
