import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.Component;
import java.io.File;

public class TabbedPaneTest {
    
    private TabbedPane tabbedPane;
    private FollowAppAttributes attributes;
    
    @Before
    public void setup() {
        attributes = new FollowAppAttributes();
        tabbedPane = new TabbedPane(attributes);
    }
    
    @Test
    public void testFindComponentAt_WhenComponentIsFound_ThenReturnComponent() {
        // Arrange
        Component expected = new JPanel();
        tabbedPane.add("Tab 1", expected);
        
        // Act
        Component result = tabbedPane.findComponentAt(0, 0);
        
        // Assert
        assertNotNull(result);
        assertEquals(expected, result);
    }
    
    @Test
    public void testFindComponentAt_WhenComponentIsNotVisible_ThenReturnNull() {
        // Arrange
        Component component = new JPanel();
        component.setVisible(false);
        tabbedPane.add("Tab 1", component);
        
        // Act
        Component result = tabbedPane.findComponentAt(0, 0);
        
        // Assert
        assertNull(result);
    }
    
    @Test
    public void testFindComponentAt_WhenComponentIsNotContainer_ThenReturnNull() {
        // Arrange
        Component component = new JButton();
        tabbedPane.add("Tab 1", component);
        
        // Act
        Component result = tabbedPane.findComponentAt(0, 0);
        
        // Assert
        assertNull(result);
    }
    
    @Test
    public void testSetSelectedIndex_WhenIndexIsValid_ThenHandleSelectedFile() {
        // Arrange
        FileFollowingPane pane = new FileFollowingPane(new File("test.txt"));
        tabbedPane.add("Tab 1", pane);
        tabbedPane.add("Tab 2", new JPanel());
        int index = 1;
        
        // Act
        tabbedPane.setSelectedIndex(index);
        
        // Assert
        assertEquals(pane, tabbedPane.getSelectedComponent());
        assertEquals(new File("test.txt").getParentFile(), attributes.getLastFileChooserDirectory());
    }
    
    @Test
    public void testSetSelectedIndex_WhenIndexIsInvalid_ThenHandleSelectedFile() {
        // Arrange
        FileFollowingPane pane = new FileFollowingPane(new File("test.txt"));
        tabbedPane.add("Tab 1", pane);
        tabbedPane.add("Tab 2", new JPanel());
        int index = 10;
        
        // Act
        tabbedPane.setSelectedIndex(index);
        
        // Assert
        assertNull(tabbedPane.getSelectedComponent());
    }
    
    @Test
    public void testSetSelectedComponent_WhenComponentIsFileFollowingPane_ThenHandleSelectedFile() {
        // Arrange
        FileFollowingPane expected = new FileFollowingPane(new File("test.txt"));
        tabbedPane.add("Tab 1", expected);
        
        // Act
        tabbedPane.setSelectedComponent(expected);
        
        // Assert
        assertEquals(expected, tabbedPane.getSelectedComponent());
        assertEquals(new File("test.txt").getParentFile(), attributes.getLastFileChooserDirectory());
    }
    
    @Test
    public void testSetSelectedComponent_WhenComponentIsNotFileFollowingPane_ThenHandleSelectedFile() {
        // Arrange
        Component component = new JPanel();
        tabbedPane.add("Tab 1", component);
        
        // Act
        tabbedPane.setSelectedComponent(component);
        
        // Assert
        assertNull(tabbedPane.getSelectedComponent());
    }
    
    @Test
    public void testRemoveTabAt_WhenIndexIsValid_ThenHandleSelectedFile() {
        // Arrange
        FileFollowingPane pane = new FileFollowingPane(new File("test.txt"));
        tabbedPane.add("Tab 1", pane);
        tabbedPane.add("Tab 2", new JPanel());
        int index = 1;
        
        // Act
        tabbedPane.removeTabAt(index);
        
        // Assert
        assertEquals(null, tabbedPane.getComponentAt(index));
        assertEquals(new File("test.txt").getParentFile(), attributes.getLastFileChooserDirectory());
    }
    
    @Test
    public void testRemoveTabAt_WhenIndexIsInvalid_ThenHandleSelectedFile() {
        // Arrange
        FileFollowingPane pane = new FileFollowingPane(new File("test.txt"));
        tabbedPane.add("Tab 1", pane);
        tabbedPane.add("Tab 2", new JPanel());
        int index = 10;
        
        // Act
        tabbedPane.removeTabAt(index);
        
        // Assert
        assertEquals(null, tabbedPane.getComponentAt(index));
    }
    
    @Test
    public void testHandleSelectedFile_WhenSelectedComponentIsFileFollowingPane_ThenSetLastFileChooserDirectory() {
        // Arrange
        FileFollowingPane pane = new FileFollowingPane(new File("test.txt"));
        tabbedPane.add("Tab 1", pane);
        
        // Act
        tabbedPane.setSelectedComponent(pane);
        tabbedPane.handleSelectedFile();
        
        // Assert
        assertEquals(new File("test.txt").getParentFile(), attributes.getLastFileChooserDirectory());
    }
    
    @Test
    public void testHandleSelectedFile_WhenSelectedComponentIsNotFileFollowingPane_ThenDoNothing() {
        // Arrange
        Component component = new JPanel();
        tabbedPane.add("Tab 1", component);
        
        // Act
        tabbedPane.setSelectedComponent(component);
        tabbedPane.handleSelectedFile();
        
        // Assert
        assertNull(attributes.getLastFileChooserDirectory());
    }
}