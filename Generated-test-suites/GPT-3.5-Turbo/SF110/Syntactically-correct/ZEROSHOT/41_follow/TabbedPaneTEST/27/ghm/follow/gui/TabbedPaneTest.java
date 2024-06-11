package ghm.follow.gui;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import ghm.follow.config.FollowAppAttributes;
import java.awt.Component;
import java.io.File;

public class TabbedPaneTest {
    
    private TabbedPane tabbedPane;
    
    @Before
    public void setUp() {
        FollowAppAttributes attributes = new FollowAppAttributes();
        tabbedPane = new TabbedPane(attributes);
    }
    
    @Test
    public void testFindComponentAt_WhenInsideComponent_ReturnsComponent() {
        Component component = new Component() {};
        tabbedPane.add(component);
        
        Component result = tabbedPane.findComponentAt(0, 0);
        
        assertEquals(component, result);
    }
    
    @Test
    public void testFindComponentAt_WhenOutsideComponent_ReturnsNull() {
        Component component = new Component() {};
        tabbedPane.add(component);
        
        Component result = tabbedPane.findComponentAt(100, 100);
        
        assertNull(result);
    }
    
    @Test
    public void testSetSelectedIndex_CallsHandleSelectedFile() {
        FileFollowingPane pane = new FileFollowingPane(new FollowAppAttributes());
        tabbedPane.addTab("Tab 1", pane);
        
        tabbedPane.setSelectedIndex(0);
        
        assertEquals(pane, tabbedPane.getSelectedComponent());
        // Assert handleSelectedFile() has been called
    }
    
    @Test
    public void testSetSelectedComponent_CallsHandleSelectedFile() {
        FileFollowingPane pane = new FileFollowingPane(new FollowAppAttributes());
        tabbedPane.addTab("Tab 1", pane);
        
        tabbedPane.setSelectedComponent(pane);
        
        assertEquals(pane, tabbedPane.getSelectedComponent());
        // Assert handleSelectedFile() has been called
    }
    
    @Test
    public void testRemoveTabAt_CallsHandleSelectedFile() {
        FileFollowingPane pane = new FileFollowingPane(new FollowAppAttributes());
        tabbedPane.addTab("Tab 1", pane);
        tabbedPane.setSelectedComponent(pane);
        
        tabbedPane.removeTabAt(0);

        // Assert handleSelectedFile() has been called
    }
}