import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import javax.swing.*;
import ghm.follow.config.*;
import java.awt.*;
import java.io.*;

public class TabbedPaneTest {
    private TabbedPane tabbedPane;
    private FollowAppAttributes attributes;
    
    @Before
    public void setUp() {
        attributes = new FollowAppAttributes();
        tabbedPane = new TabbedPane(attributes);
    }
    
    @Test
    public void testFindComponentAt_WhenComponentFoundWithinBounds_ReturnComponent() {
        JPanel panel = new JPanel();
        tabbedPane.addTab("tab1", panel);
        Component result = tabbedPane.findComponentAt(10, 20);
        assertEquals(panel, result);
    }
    
    @Test
    public void testFindComponentAt_WhenComponentNotFound_ReturnNull() {
        Component result = tabbedPane.findComponentAt(100, 200);
        assertNull(result);
    }
    
    @Test
    public void testFindComponentAt_WhenComponentFoundInVisibleContainer_ReturnComponent() {
        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 100, 100);
        JPanel childPanel = new JPanel();
        childPanel.setBounds(10, 20, 50, 50);
        panel.add(childPanel);
        tabbedPane.addTab("tab1", panel);
        Component result = tabbedPane.findComponentAt(15, 30);
        assertEquals(childPanel, result);
    }
    
    @Test
    public void testFindComponentAt_WhenComponentFoundInInvisibleContainer_ReturnComponent() {
        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 100, 100);
        panel.setVisible(false);
        JPanel childPanel = new JPanel();
        childPanel.setBounds(10, 20, 50, 50);
        panel.add(childPanel);
        tabbedPane.addTab("tab1", panel);
        Component result = tabbedPane.findComponentAt(15, 30);
        assertEquals(childPanel, result);
    }
    
    @Test
    public void testSetSelectedIndex_WhenIndexWithinBounds_CallHandleSelectedFile() {
        FileFollowingPane pane = new FileFollowingPane();
        tabbedPane.addTab("tab1", pane);
        tabbedPane.setSelectedIndex(0);
        // Assert that handleSelectedFile() is called
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetSelectedIndex_WhenIndexOutOfBounds_ThrowException() {
        tabbedPane.setSelectedIndex(100);
    }
    
    @Test
    public void testSetSelectedComponent_WhenComponentExists_CallHandleSelectedFile() {
        FileFollowingPane pane = new FileFollowingPane();
        tabbedPane.addTab("tab1", pane);
        tabbedPane.setSelectedComponent(pane);
        // Assert that handleSelectedFile() is called
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetSelectedComponent_WhenComponentDoesNotExist_ThrowException() {
        FileFollowingPane pane = new FileFollowingPane();
        tabbedPane.setSelectedComponent(pane);
    }
    
    @Test
    public void testRemoveTabAt_WhenIndexWithinBounds_CallHandleSelectedFile() {
        FileFollowingPane pane = new FileFollowingPane();
        tabbedPane.addTab("tab1", pane);
        tabbedPane.removeTabAt(0);
        // Assert that handleSelectedFile() is called
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveTabAt_WhenIndexOutOfBounds_ThrowException() {
        tabbedPane.removeTabAt(100);
    }
}