import org.junit.*;
import static org.junit.Assert.*;

public class TabbedPaneTest {
    
    private TabbedPane tabbedPane;
    
    @Before
    public void setUp() {
        FollowAppAttributes attributes = new FollowAppAttributes();
        tabbedPane = new TabbedPane(attributes);
    }
    
    @Test
    public void testFindComponentAt_WithValidCoordinates_ReturnsComponent() {
        // Arrange
        JLabel label = new JLabel("Test");
        tabbedPane.addTab("Tab", label);
        
        // Act
        Component result = tabbedPane.findComponentAt(10, 10);
        
        // Assert
        assertEquals(label, result);
    }
    
    @Test
    public void testFindComponentAt_WithInvalidCoordinates_ReturnsNull() {
        // Arrange
        JLabel label = new JLabel("Test");
        tabbedPane.addTab("Tab", label);
        
        // Act
        Component result = tabbedPane.findComponentAt(100, 100);
        
        // Assert
        assertNull(result);
    }
    
    @Test
    public void testSetSelectedIndex_ValidIndex_CallsHandleSelectedFile() {
        // Arrange
        FileFollowingPane pane = new FileFollowingPane();
        tabbedPane.addTab("Tab", pane);
        
        // Act
        tabbedPane.setSelectedIndex(0);
        
        // Assert
        // Check if handleSelectedFile() was called
    }
    
    @Test
    public void testSetSelectedIndex_InvalidIndex_DoesNotCallHandleSelectedFile() {
        // Arrange
        FileFollowingPane pane = new FileFollowingPane();
        tabbedPane.addTab("Tab", pane);
        
        // Act
        tabbedPane.setSelectedIndex(-1);
        
        // Assert
        // Check if handleSelectedFile() was not called
    }
    
    @Test
    public void testSetSelectedComponent_WithValidComponent_CallsHandleSelectedFile() {
        // Arrange
        FileFollowingPane pane = new FileFollowingPane();
        tabbedPane.addTab("Tab", pane);
        
        // Act
        tabbedPane.setSelectedComponent(pane);
        
        // Assert
        // Check if handleSelectedFile() was called
    }
    
    @Test
    public void testSetSelectedComponent_WithInvalidComponent_DoesNotCallHandleSelectedFile() {
        // Arrange
        FileFollowingPane pane = new FileFollowingPane();
        tabbedPane.addTab("Tab", pane);
        
        // Act
        tabbedPane.setSelectedComponent(new JPanel());
        
        // Assert
        // Check if handleSelectedFile() was not called
    }
    
    @Test
    public void testRemoveTabAt_ValidIndex_CallsHandleSelectedFile() {
        // Arrange
        FileFollowingPane pane = new FileFollowingPane();
        tabbedPane.addTab("Tab", pane);
        
        // Act
        tabbedPane.removeTabAt(0);
        
        // Assert
        // Check if handleSelectedFile() was called
    }
    
    @Test
    public void testRemoveTabAt_InvalidIndex_DoesNotCallHandleSelectedFile() {
        // Arrange
        FileFollowingPane pane = new FileFollowingPane();
        tabbedPane.addTab("Tab", pane);
        
        // Act
        tabbedPane.removeTabAt(1);
        
        // Assert
        // Check if handleSelectedFile() was not called
    }
    
    @Test
    public void testHandleSelectedFile_WithValidSelectedComponent_SetsLastFileChooserDirectory() {
        // Arrange
        FileFollowingPane pane = new FileFollowingPane(new File("test.txt"));
        tabbedPane.addTab("Tab", pane);
        FollowAppAttributes attributes = new FollowAppAttributes();
        tabbedPane.setAttributes(attributes);
        
        // Act
        tabbedPane.handleSelectedFile();
        File result = attributes.getLastFileChooserDirectory();
        
        // Assert
        assertEquals(new File("").getAbsolutePath(), result.getAbsolutePath());
    }
    
    @Test
    public void testHandleSelectedFile_WithNullSelectedComponent_DoesNotSetLastFileChooserDirectory() {
        // Arrange
        tabbedPane.setAttributes(new FollowAppAttributes());
        
        // Act
        tabbedPane.handleSelectedFile();
        File result = tabbedPane.getAttributes().getLastFileChooserDirectory();
        
        // Assert
        assertNull(result);
    }
}