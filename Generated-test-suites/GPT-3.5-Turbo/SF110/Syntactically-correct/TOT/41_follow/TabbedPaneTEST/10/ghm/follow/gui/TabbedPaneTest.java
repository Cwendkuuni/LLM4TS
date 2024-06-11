import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;

public class TabbedPaneTest {
    
    private TabbedPane tabbedPane;
    
    @Before
    public void setUp() {
        FollowAppAttributes attributes = new FollowAppAttributes();
        tabbedPane = new TabbedPane(attributes);
    }
    
    @Test
    public void testFindComponentAt_InsideContainerVisible_ReturnsComponent() {
        JPanel panel = new JPanel();
        panel.setSize(100, 100);
        panel.setVisible(true);
        panel.setLocation(0, 0);
        
        JButton button = new JButton();
        button.setSize(50, 50);
        button.setVisible(true);
        button.setLocation(25, 25);
        
        panel.add(button);
        tabbedPane.add(panel);
        
        Component result = tabbedPane.findComponentAt(30, 30);
        assertEquals(button, result);
    }
    
    @Test
    public void testFindComponentAt_OutsideContainer_ReturnsNull() {
        JPanel panel = new JPanel();
        panel.setSize(100, 100);
        panel.setVisible(true);
        panel.setLocation(0, 0);
        
        JButton button = new JButton();
        button.setSize(50, 50);
        button.setVisible(true);
        button.setLocation(25, 25);
        
        panel.add(button);
        tabbedPane.add(panel);
        
        Component result = tabbedPane.findComponentAt(150, 150);
        assertNull(result);
    }
    
    @Test
    public void testFindComponentAt_NonVisibleComponent_ReturnsNull() {
        JPanel panel = new JPanel();
        panel.setSize(100, 100);
        panel.setVisible(true);
        panel.setLocation(0, 0);
        
        JButton button = new JButton();
        button.setSize(50, 50);
        button.setVisible(false);
        button.setLocation(25, 25);
        
        panel.add(button);
        tabbedPane.add(panel);
        
        Component result = tabbedPane.findComponentAt(30, 30);
        assertNull(result);
    }
    
    @Test
    public void testSetSelectedIndex_ValidIndex_HandlesSelectedFile() {
        FileFollowingPane pane1 = new FileFollowingPane();
        FileFollowingPane pane2 = new FileFollowingPane();
        tabbedPane.add(pane1);
        tabbedPane.add(pane2);
        
        tabbedPane.setSelectedIndex(1);
        
        FileFollowingPane selectedPane = (FileFollowingPane) tabbedPane.getSelectedComponent();
        assertEquals(pane2, selectedPane);
        assertNotNull(selectedPane.getFollowedFile());
        assertNotNull(tabbedPane.attributes.getLastFileChooserDirectory());
    }
    
    @Test
    public void testSetSelectedIndex_InvalidIndex_DoesNotHandleSelectedFile() {
        FileFollowingPane pane1 = new FileFollowingPane();
        FileFollowingPane pane2 = new FileFollowingPane();
        tabbedPane.add(pane1);
        tabbedPane.add(pane2);
        
        tabbedPane.setSelectedIndex(2);
        
        FileFollowingPane selectedPane = (FileFollowingPane) tabbedPane.getSelectedComponent();
        assertNull(selectedPane);
        assertNull(tabbedPane.attributes.getLastFileChooserDirectory());
    }
    
    @Test
    public void testSetSelectedComponent_ValidComponent_HandlesSelectedFile() {
        FileFollowingPane pane1 = new FileFollowingPane();
        FileFollowingPane pane2 = new FileFollowingPane();
        tabbedPane.add(pane1);
        tabbedPane.add(pane2);
        
        tabbedPane.setSelectedComponent(pane2);
        
        FileFollowingPane selectedPane = (FileFollowingPane) tabbedPane.getSelectedComponent();
        assertEquals(pane2, selectedPane);
        assertNotNull(selectedPane.getFollowedFile());
        assertNotNull(tabbedPane.attributes.getLastFileChooserDirectory());
    }
    
    @Test
    public void testSetSelectedComponent_NullComponent_DoesNotHandleSelectedFile() {
        FileFollowingPane pane1 = new FileFollowingPane();
        FileFollowingPane pane2 = new FileFollowingPane();
        tabbedPane.add(pane1);
        tabbedPane.add(pane2);
        
        tabbedPane.setSelectedComponent(null);
        
        FileFollowingPane selectedPane = (FileFollowingPane) tabbedPane.getSelectedComponent();
        assertNull(selectedPane);
        assertNull(tabbedPane.attributes.getLastFileChooserDirectory());
    }
    
    @Test
    public void testRemoveTabAt_ValidIndex_HandlesSelectedFile() {
        FileFollowingPane pane1 = new FileFollowingPane();
        FileFollowingPane pane2 = new FileFollowingPane();
        tabbedPane.add(pane1);
        tabbedPane.add(pane2);
        
        tabbedPane.removeTabAt(1);
        
        FileFollowingPane selectedPane = (FileFollowingPane) tabbedPane.getSelectedComponent();
        assertEquals(pane1, selectedPane);
        assertNotNull(selectedPane.getFollowedFile());
        assertNotNull(tabbedPane.attributes.getLastFileChooserDirectory());
    }
    
    @Test
    public void testRemoveTabAt_InvalidIndex_DoesNotHandleSelectedFile() {
        FileFollowingPane pane1 = new FileFollowingPane();
        FileFollowingPane pane2 = new FileFollowingPane();
        tabbedPane.add(pane1);
        tabbedPane.add(pane2);
        
        tabbedPane.removeTabAt(2);
        
        FileFollowingPane selectedPane = (FileFollowingPane) tabbedPane.getSelectedComponent();
        assertNull(selectedPane);
        assertNull(tabbedPane.attributes.getLastFileChooserDirectory());
    }
    
    @Test
    public void testHandleSelectedFile_ValidComponent_SetsLastFileChooserDirectory() {
        File file = new File("test.txt");
        FileFollowingPane pane = new FileFollowingPane();
        pane.setFollowedFile(file);
        tabbedPane.add(pane);
        
        tabbedPane.handleSelectedFile();
        
        assertEquals(file.getParentFile(), tabbedPane.attributes.getLastFileChooserDirectory());
    }
    
    @Test
    public void testHandleSelectedFile_NullComponent_DoesNotSetLastFileChooserDirectory() {
        tabbedPane.handleSelectedFile();
        assertNull(tabbedPane.attributes.getLastFileChooserDirectory());
    }
}