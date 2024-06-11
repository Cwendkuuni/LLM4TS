package ghm.follow.gui;

import org.junit.Test;
import static org.junit.Assert.*;
import ghm.follow.config.*;
import java.io.File;
import javax.swing.JPanel;

public class TabbedPaneTest {

    @Test
    public void testFindComponentAt() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        JPanel panel = new JPanel();
        tabbedPane.addTab("Test", panel);
        Component result = tabbedPane.findComponentAt(10, 10);
        assertEquals(panel, result);
    }

    @Test
    public void testSetSelectedIndex() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        tabbedPane.addTab("Tab 1", new JPanel());
        tabbedPane.addTab("Tab 2", new JPanel());
        tabbedPane.setSelectedIndex(1);
        assertEquals(1, tabbedPane.getSelectedIndex());
    }

    @Test
    public void testSetSelectedComponent() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        tabbedPane.addTab("Tab 1", panel1);
        tabbedPane.addTab("Tab 2", panel2);
        tabbedPane.setSelectedComponent(panel2);
        assertEquals(panel2, tabbedPane.getSelectedComponent());
    }

    @Test
    public void testRemoveTabAt() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        tabbedPane.addTab("Tab 1", panel1);
        tabbedPane.addTab("Tab 2", panel2);
        tabbedPane.removeTabAt(1);
        assertEquals(1, tabbedPane.getTabCount());
    }

    @Test
    public void testHandleSelectedFile() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        File file = new File("test.txt");
        FileFollowingPane pane = new FileFollowingPane(file);
        tabbedPane.addTab("Tab 1", pane);
        tabbedPane.setSelectedComponent(pane);
        tabbedPane.handleSelectedFile();
        File parent = pane.getFollowedFile().getParentFile();
        assertEquals(parent, tabbedPane.attributes.getLastFileChooserDirectory());
    }
}