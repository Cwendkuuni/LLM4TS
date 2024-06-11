import static org.junit.Assert.*;
import org.junit.Test;
import javax.swing.*;
import java.awt.*;
import java.io.*;

public class TabbedPaneTest {
    
    //  
    
    @Test
    public void testFindComponentAt() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        
        // Test when the TabbedPane does not contain any components
        assertNull(tabbedPane.findComponentAt(0, 0));
        
        // Test when the TabbedPane contains one component which is not visible
        JPanel panel1 = new JPanel();
        panel1.setVisible(false);
        tabbedPane.add(panel1);
        assertNull(tabbedPane.findComponentAt(0, 0));
        
        // Test when the TabbedPane contains one visible component
        JPanel panel2 = new JPanel();
        tabbedPane.add(panel2);
        assertEquals(panel2, tabbedPane.findComponentAt(0, 0));
        
        // Test when the TabbedPane contains one visible container component
        JPanel containerPanel = new JPanel();
        JPanel componentPanel = new JPanel();
        containerPanel.add(componentPanel);
        tabbedPane.add(containerPanel);
        assertEquals(componentPanel, tabbedPane.findComponentAt(0, 0));
        
        // Test when the TabbedPane contains one visible non-container component
        JLabel label = new JLabel();
        containerPanel.add(label);
        assertEquals(componentPanel, tabbedPane.findComponentAt(0, 0));
    }
    
    @Test
    public void testSetSelectedIndex() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        
        // Test when selecting an index within the range of the tabbed pane
        tabbedPane.add(new JPanel());
        tabbedPane.add(new JPanel());
        tabbedPane.setSelectedIndex(0);
        assertEquals(0, tabbedPane.getSelectedIndex());
        
        tabbedPane.setSelectedIndex(1);
        assertEquals(1, tabbedPane.getSelectedIndex());
        
        // Test when selecting an index outside the range of the tabbed pane
        tabbedPane.setSelectedIndex(-1);
        assertEquals(1, tabbedPane.getSelectedIndex());
        
        tabbedPane.setSelectedIndex(2);
        assertEquals(1, tabbedPane.getSelectedIndex());
    }
    
    @Test
    public void testSetSelectedComponent() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        
        // Test when selecting a component that is in the tabbed pane
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        tabbedPane.add(panel1);
        tabbedPane.add(panel2);
        tabbedPane.setSelectedComponent(panel1);
        assertEquals(panel1, tabbedPane.getSelectedComponent());
        
        tabbedPane.setSelectedComponent(panel2);
        assertEquals(panel2, tabbedPane.getSelectedComponent());
        
        // Test when selecting a component that is not in the tabbed pane
        JPanel panel3 = new JPanel();
        tabbedPane.setSelectedComponent(panel3);
        assertNull(tabbedPane.getSelectedComponent());
    }
    
    @Test
    public void testRemoveTabAt() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        
        // Test when removing a tab by index
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        tabbedPane.add(panel1);
        tabbedPane.add(panel2);
        tabbedPane.removeTabAt(0);
        assertEquals(1, tabbedPane.getTabCount());
        assertEquals(panel2, tabbedPane.getSelectedComponent());
        
        // Test when removing a tab by index outside the range of the tabbed pane
        tabbedPane.removeTabAt(-1);
        assertEquals(1, tabbedPane.getTabCount());
        
        tabbedPane.removeTabAt(1);
        assertEquals(1, tabbedPane.getTabCount());
        
        // Test when removing all tabs
        tabbedPane.removeTabAt(0);
        assertEquals(0, tabbedPane.getTabCount());
        assertNull(tabbedPane.getSelectedComponent());
    }
    
    // Additional test cases for edge cases and exception handling scenarios
    
    @Test
    public void testFindComponentAt_EdgeCases() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        
        // Test when the x and y coordinates are outside the bounds of the TabbedPane
        assertNull(tabbedPane.findComponentAt(-1, -1));
        
        // Test when the component at the given coordinates is not visible
        JPanel panel1 = new JPanel();
        panel1.setBounds(0, 0, 100, 100);
        panel1.setVisible(false);
        tabbedPane.add(panel1);
        assertNull(tabbedPane.findComponentAt(50, 50));
    }
    
    @Test
    public void testSetSelectedIndex_Exception() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        
        // Test when the index is negative
        try {
            tabbedPane.setSelectedIndex(-1);
            fail("An IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // Exception caught as expected
        }
    }
    
    @Test
    public void testSetSelectedComponent_Exception() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        
        // Test when the component is not in the tabbed pane
        JPanel panel = new JPanel();
        try {
            tabbedPane.setSelectedComponent(panel);
            fail("An IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // Exception caught as expected
        }
    }
    
    // 
}