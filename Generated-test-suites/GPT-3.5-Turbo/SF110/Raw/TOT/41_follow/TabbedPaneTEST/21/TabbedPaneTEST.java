import static org.junit.Assert.*;
import org.junit.*;

public class TabbedPaneTest {

    private TabbedPane tabbedPane;
    private FollowAppAttributes attributes;

    @Before
    public void setUp() {
        attributes = new FollowAppAttributes();
        tabbedPane = new TabbedPane(attributes);
    }
    
    // Test case for TabbedPane constructor
    @Test
    public void testTabbedPaneConstructor() {
        assertEquals(attributes.getTabPlacement(), tabbedPane.getTabPlacement());
    }
    
    // Test case for findComponentAt method with valid coordinates
    @Test
    public void testFindComponentAt_ValidCoordinates() {
        // Create a mock component
        Component mockComponent = new JLabel("Mock Component");
        tabbedPane.add(mockComponent);
        
        // Get the component at valid coordinates
        Component result = tabbedPane.findComponentAt(10, 10);
        
        // Assert that the result is the mock component
        assertEquals(mockComponent, result);
    }
    
    // Test case for findComponentAt method with invalid coordinates
    @Test
    public void testFindComponentAt_InvalidCoordinates() {
        // Create a mock component
        Component mockComponent = new JLabel("Mock Component");
        tabbedPane.add(mockComponent);
        
        // Get the component at invalid coordinates
        Component result = tabbedPane.findComponentAt(100, 100);
        
        // Assert that the result is null
        assertNull(result);
    }
    
    // Test case for setSelectedIndex method
    @Test
    public void testSetSelectedIndex() {
        // Add some components to the tabbed pane
        Component component1 = new JLabel("Component 1");
        Component component2 = new JLabel("Component 2");
        tabbedPane.add(component1);
        tabbedPane.add(component2);
        
        // Select the second component
        tabbedPane.setSelectedIndex(1);
        
        // Assert that the second component is selected
        assertEquals(component2, tabbedPane.getSelectedComponent());
    }
    
    // Test case for setSelectedComponent method
    @Test
    public void testSetSelectedComponent() {
        // Add some file following panes to the tabbed pane
        FileFollowingPane pane1 = new FileFollowingPane(new File("file1.txt"));
        FileFollowingPane pane2 = new FileFollowingPane(new File("file2.txt"));
        tabbedPane.add(pane1);
        tabbedPane.add(pane2);
        
        // Select the second file following pane
        tabbedPane.setSelectedComponent(pane2);
        
        // Assert that the second file following pane is selected
        assertEquals(pane2, tabbedPane.getSelectedComponent());
        
        // Assert that the last file chooser directory is set to the parent directory of the selected file
        assertEquals(pane2.getFollowedFile().getParentFile(), attributes.getLastFileChooserDirectory());
    }
    
    // Test case for removeTabAt method
    @Test
    public void testRemoveTabAt() {
        // Add some components to the tabbed pane
        Component component1 = new JLabel("Component 1");
        Component component2 = new JLabel("Component 2");
        tabbedPane.add(component1);
        tabbedPane.add(component2);
        
        // Remove the first component
        tabbedPane.removeTabAt(0);
        
        // Assert that the first component is removed
        assertEquals(1, tabbedPane.getTabCount());
        assertEquals(component2, tabbedPane.getSelectedComponent());
    }
}