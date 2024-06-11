import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.io.File;

import static org.junit.Assert.*;

public class TabbedPaneTest {
    
    private TabbedPane tabbedPane;
    private FollowAppAttributes attributes;
    
    @Before
    public void setUp() {
        attributes = new FollowAppAttributes();
        tabbedPane = new TabbedPane(attributes);
    }
    
    @Test
    public void testTabbedPaneConstructor() {
        assertNotNull(tabbedPane);
        assertEquals(attributes.getTabPlacement(), tabbedPane.getTabPlacement());
    }
    
    @Test
    public void testFindComponentAt_WithInvalidCoordinates_ShouldReturnNull() {
        assertNull(tabbedPane.findComponentAt(-10, -10));
    }
    
    @Test
    public void testFindComponentAt_WithContainerVisible_ShouldReturnComponent() {
        JPanel panel = new JPanel();
        panel.setVisible(true);
        tabbedPane.add(panel);
        
        assertEquals(panel, tabbedPane.findComponentAt(10, 10));
    }
    
    @Test
    public void testFindComponentAt_WithContainerInvisible_ShouldReturnNull() {
        JPanel panel = new JPanel();
        panel.setVisible(false);
        tabbedPane.add(panel);
        
        assertNull(tabbedPane.findComponentAt(10, 10));
    }
    
    @Test
    public void testFindComponentAt_WithNestedContainerVisible_ShouldReturnComponent() {
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        panel1.add(panel2);
        panel1.setVisible(true);
        panel2.setVisible(true);
        tabbedPane.add(panel1);
        
        assertEquals(panel2, tabbedPane.findComponentAt(10, 10));
    }
    
    @Test
    public void testFindComponentAt_WithNestedContainerInvisible_ShouldReturnNull() {
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        panel1.add(panel2);
        panel1.setVisible(true);
        panel2.setVisible(false);
        tabbedPane.add(panel1);
        
        assertNull(tabbedPane.findComponentAt(10, 10));
    }
    
    @Test
    public void testFindComponentAt_WithComponentInvisible_ShouldReturnNull() {
        JButton button = new JButton();
        button.setVisible(false);
        tabbedPane.add(button);
        
        assertNull(tabbedPane.findComponentAt(10, 10));
    }
    
    @Test
    public void testFindComponentAt_WithValidCoordinates_ShouldReturnComponent() {
        JButton button = new JButton();
        button.setVisible(true);
        tabbedPane.add(button);
        
        assertEquals(button, tabbedPane.findComponentAt(10, 10));
    }
    
    @Test
    public void testSetSelectedIndex_ShouldSelectIndexAndCallHandleSelectedFile() {
        File file = new File("test.txt");
        FileFollowingPane pane = new FileFollowingPane(file);
        tabbedPane.addTab("Test", pane);
        
        tabbedPane.setSelectedIndex(0);
        
        assertEquals(0, tabbedPane.getSelectedIndex());
        assertEquals(file.getParentFile(), attributes.getLastFileChooserDirectory());
    }
    
    @Test
    public void testSetSelectedComponent_ShouldSelectComponentAndCallHandleSelectedFile() {
        File file = new File("test.txt");
        FileFollowingPane pane = new FileFollowingPane(file);
        tabbedPane.addTab("Test", pane);
        
        tabbedPane.setSelectedComponent(pane);
        
        assertEquals(pane, tabbedPane.getSelectedComponent());
        assertEquals(file.getParentFile(), attributes.getLastFileChooserDirectory());
    }
    
    @Test
    public void testRemoveTabAt_ShouldRemoveTabAndCallHandleSelectedFile() {
        File file = new File("test.txt");
        FileFollowingPane pane = new FileFollowingPane(file);
        tabbedPane.addTab("Test", pane);
        
        tabbedPane.removeTabAt(0);
        
        assertEquals(0, tabbedPane.getTabCount());
        assertNull(attributes.getLastFileChooserDirectory());
    }
    
    @Test
    public void testHandleSelectedFile_WithValidSelectedComponent_ShouldSetLastFileChooserDirectory() {
        File file = new File("test.txt");
        FileFollowingPane pane = new FileFollowingPane(file);
        tabbedPane.addTab("Test", pane);
        tabbedPane.setSelectedComponent(pane);
        
        tabbedPane.handleSelectedFile();
        
        assertEquals(file.getParentFile(), attributes.getLastFileChooserDirectory());
    }
    
    @Test
    public void testHandleSelectedFile_WithNullSelectedComponent_ShouldNotSetLastFileChooserDirectory() {
        tabbedPane.setSelectedComponent(null);
        
        tabbedPane.handleSelectedFile();
        
        assertNull(attributes.getLastFileChooserDirectory());
    }
}