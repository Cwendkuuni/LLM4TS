import org.junit.*;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class TabbedPaneTest {
    
    private TabbedPane tabbedPane;
    
    public TabbedPaneTest() {
        this.tabbedPane = new TabbedPane(new FollowAppAttributes());
    }
    
    // Test case for constructor
    @Test
    public void testTabbedPaneConstructor() {
        assertNotNull(tabbedPane);
    }
    
    // Test case for findComponentAt()
    @Test
    public void testFindComponentAt() {
        // Typical use case: x and y within the tabbed pane dimensions
        Component component = tabbedPane.findComponentAt(10, 10);
        assertNull(component);
        
        // Edge case: x and y at the upper bound of the tabbed pane dimensions
        component = tabbedPane.findComponentAt(tabbedPane.getWidth(), tabbedPane.getHeight());
        assertNull(component);
        
        // Error scenario: x and y outside the tabbed pane dimensions
        component = tabbedPane.findComponentAt(-10, -10);
        assertNull(component);
    }
    
    // Test case for setSelectedIndex()
    @Test
    public void testSetSelectedIndex() {
        // Typical use case: valid index within the range of tabbed pane components
        tabbedPane.setSelectedIndex(0);
        assertEquals(0, tabbedPane.getSelectedIndex());
        
        // Error scenario: negative index
        tabbedPane.setSelectedIndex(-1);
        assertEquals(0, tabbedPane.getSelectedIndex());
        
        // Error scenario: index greater than the number of tabbed pane components
        tabbedPane.setSelectedIndex(tabbedPane.getTabCount());
        assertEquals(0, tabbedPane.getSelectedIndex());
    }
    
    // Test case for setSelectedComponent()
    @Test
    public void testSetSelectedComponent() {
        // Typical use case: valid file following pane component
        FileFollowingPane pane = new FileFollowingPane();
        tabbedPane.setSelectedComponent(pane);
        assertSame(pane, tabbedPane.getSelectedComponent());
        
        // Error scenario: null component
        tabbedPane.setSelectedComponent(null);
        assertNull(tabbedPane.getSelectedComponent());
    }
    
    // Test case for removeTabAt()
    @Test
    public void testRemoveTabAt() {
        // Typical use case: valid index within the range of tabbed pane components
        tabbedPane.addTab("Tab 1", new JPanel());
        tabbedPane.addTab("Tab 2", new JPanel());
        tabbedPane.addTab("Tab 3", new JPanel());
        int initialTabCount = tabbedPane.getTabCount();
        
        tabbedPane.removeTabAt(1);
        assertEquals(initialTabCount - 1, tabbedPane.getTabCount());
        
        // Error scenario: index greater than the number of tabbed pane components
        tabbedPane.removeTabAt(tabbedPane.getTabCount());
        assertEquals(initialTabCount - 1, tabbedPane.getTabCount());
        
        // Error scenario: negative index
        tabbedPane.removeTabAt(-1);
        assertEquals(initialTabCount - 1, tabbedPane.getTabCount());
    }
    
    // Test case for handleSelectedFile()
    @Test
    public void testHandleSelectedFile() {
        // Typical use case: valid selected file
        FileFollowingPane pane = new FileFollowingPane();
        File file = new File("test.txt");
        pane.setFollowedFile(file);
        tabbedPane.setSelectedComponent(pane);
        tabbedPane.handleSelectedFile();
        assertEquals(file.getParentFile(), tabbedPane.getAttributes().getLastFileChooserDirectory());
        
        // Error scenario: null selected component
        tabbedPane.setSelectedComponent(null);
        tabbedPane.handleSelectedFile();
        assertNull(tabbedPane.getAttributes().getLastFileChooserDirectory());
        
        // Error scenario: selected component without a file
        pane = new FileFollowingPane();
        tabbedPane.setSelectedComponent(pane);
        tabbedPane.handleSelectedFile();
        assertNull(tabbedPane.getAttributes().getLastFileChooserDirectory());
    }
    
}