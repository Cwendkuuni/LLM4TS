package ghm.follow.gui;

import javax.swing.*;
import ghm.follow.config.*;
import java.awt.*;
import java.io.*;

public class TabbedPaneTest {

    @Test
    public void testFindComponentAt() {
        // create a new TabbedPane object with mocked attributes
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());

        // test when the TabbedPane does not contain the given coordinates
        Component result1 = tabbedPane.findComponentAt(0, 0);
        assertNull(result1);

        // test when the TabbedPane contains the given coordinates
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();
        tabbedPane.add(panel1, "Panel 1");
        tabbedPane.add(panel2, "Panel 2");
        tabbedPane.add(panel3, "Panel 3");

        Component result2 = tabbedPane.findComponentAt(10, 10);
        assertEquals(panel1, result2);

        Component result3 = tabbedPane.findComponentAt(50, 50);
        assertEquals(panel2, result3);

        Component result4 = tabbedPane.findComponentAt(100, 100);
        assertEquals(panel3, result4);
    }

    @Test
    public void testSetSelectedIndex() {
        // create a new TabbedPane object with mocked attributes
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());

        // test setSelectedIndex method
        tabbedPane.setSelectedIndex(0);
        // write assertions here
    }

    @Test
    public void testSetSelectedComponent() {
        // create a new TabbedPane object with mocked attributes
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());

        // test setSelectedComponent method
        FileFollowingPane fileFollowingPane = new FileFollowingPane();
        tabbedPane.setSelectedComponent(fileFollowingPane);
        // write assertions here
    }

    @Test
    public void testRemoveTabAt() {
        // create a new TabbedPane object with mocked attributes
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());

        // test removeTabAt method
        tabbedPane.addTab("Tab 1", new JPanel());
        tabbedPane.addTab("Tab 2", new JPanel());

        tabbedPane.removeTabAt(0);
        // write assertions here
    }

    @Test
    public void testHandleSelectedFile() {
        // create a new TabbedPane object with mocked attributes
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());

        // test handleSelectedFile method
        FileFollowingPane fileFollowingPane = new FileFollowingPane();
        // set the selected component to fileFollowingPane
        tabbedPane.setSelectedComponent(fileFollowingPane);

        // write assertions here
    }
}