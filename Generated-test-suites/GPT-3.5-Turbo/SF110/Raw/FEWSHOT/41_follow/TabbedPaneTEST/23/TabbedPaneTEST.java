package ghm.follow.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TabbedPaneTest {
    @Test
    public void testFindComponentAt() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        
        // Test when x and y are outside the component, should return null
        assertNull(tabbedPane.findComponentAt(-10, -10));
        
        // Test when x and y are inside the component
        FileFollowingPane pane = new FileFollowingPane();
        tabbedPane.addTab("Tab", pane);
        assertEquals(pane, tabbedPane.findComponentAt(10, 10));
        
        // Test when x and y are inside a nested component
        JPanel nestedPanel = new JPanel();
        pane.add(nestedPanel);
        Component nestedComponent = new Component();
        nestedPanel.add(nestedComponent);
        assertEquals(nestedComponent, tabbedPane.findComponentAt(10, 10));
        
        // Test when x and y are inside a hidden component
        nestedComponent.setVisible(false);
        assertNull(tabbedPane.findComponentAt(10, 10));
    }

    @Test
    public void testSetSelectedIndex() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        
        // Test selected index changes and handleSelectedFile method is called
        FileFollowingPane pane1 = new FileFollowingPane();
        FileFollowingPane pane2 = new FileFollowingPane();
        tabbedPane.addTab("Tab1", pane1);
        tabbedPane.addTab("Tab2", pane2);
        
        tabbedPane.setSelectedIndex(1);
        assertEquals(1, tabbedPane.getSelectedIndex());
        assertTrue(pane2.isHandleSelectedFileCalled());
    }

    @Test
    public void testSetSelectedComponent() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        
        // Test selected component changes and handleSelectedFile method is called
        FileFollowingPane pane1 = new FileFollowingPane();
        FileFollowingPane pane2 = new FileFollowingPane();
        tabbedPane.addTab("Tab1", pane1);
        tabbedPane.addTab("Tab2", pane2);
        
        tabbedPane.setSelectedComponent(pane2);
        assertEquals(pane2, tabbedPane.getSelectedComponent());
        assertTrue(pane2.isHandleSelectedFileCalled());
    }

    @Test
    public void testRemoveTabAt() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        
        // Test tab removal and handleSelectedFile method is called
        FileFollowingPane pane1 = new FileFollowingPane();
        FileFollowingPane pane2 = new FileFollowingPane();
        tabbedPane.addTab("Tab1", pane1);
        tabbedPane.addTab("Tab2", pane2);
        
        tabbedPane.removeTabAt(0);
        assertEquals(1, tabbedPane.getTabCount());
        assertTrue(pane1.isHandleSelectedFileCalled());
    }
}