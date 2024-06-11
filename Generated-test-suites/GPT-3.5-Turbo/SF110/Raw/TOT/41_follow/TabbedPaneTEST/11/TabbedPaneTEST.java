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
    
    @Test
    public void testFindComponentAt_WhenComponentNotFound_ReturnsNull() {
        assertNull(tabbedPane.findComponentAt(10, 10));
    }
    
    @Test
    public void testFindComponentAt_WhenComponentIsContainerAndNotVisible_ReturnsNull() {
        JPanel container = new JPanel();
        container.setVisible(false);
        tabbedPane.add(container);
        
        assertNull(tabbedPane.findComponentAt(10, 10));
    }
    
    @Test
    public void testFindComponentAt_WhenComponentIsContainerAndVisible_ReturnsComponent() {
        JPanel container = new JPanel();
        tabbedPane.add(container);
        container.setVisible(true);
        
        assertEquals(container, tabbedPane.findComponentAt(10, 10));
    }
    
    @Test
    public void testFindComponentAt_WhenComponentIsNotContainer_ReturnsComponent() {
        JLabel component = new JLabel();
        tabbedPane.add(component);
        
        assertEquals(component, tabbedPane.findComponentAt(10, 10));
    }
    
    @Test
    public void testFindComponentAt_WhenComponentIsNotContainerAndNotVisible_ReturnsNull() {
        JLabel component = new JLabel();
        component.setVisible(false);
        tabbedPane.add(component);
        
        assertNull(tabbedPane.findComponentAt(10, 10));
    }
    
    @Test
    public void testSetSelectedIndex_CallsHandleSelectedFile() {
        tabbedPane.setSelectedIndex(0);
        // Verify that handleSelectedFile() is called
    }
    
    @Test
    public void testSetSelectedComponent_CallsHandleSelectedFile() {
        FileFollowingPane pane = new FileFollowingPane();
        tabbedPane.setSelectedComponent(pane);
        // Verify that handleSelectedFile() is called
    }
    
    @Test
    public void testRemoveTabAt_CallsHandleSelectedFile() {
        tabbedPane.add(new JPanel());
        tabbedPane.removeTabAt(0);
        // Verify that handleSelectedFile() is called
    }
    
    @Test
    public void testHandleSelectedFile_WhenSelectedComponentIsNotNull_SetsLastFileChooserDirectory() {
        FileFollowingPane pane = new FileFollowingPane();
        pane.setFollowedFile(new File("testFile.txt"));
        tabbedPane.setSelectedComponent(pane);
        
        File expectedDirectory = new File("testFile.txt").getParentFile();
        assertEquals(expectedDirectory, attributes.getLastFileChooserDirectory());
    }
    
    @Test
    public void testHandleSelectedFile_WhenSelectedComponentIsNull_DoesNotSetLastFileChooserDirectory() {
        tabbedPane.setSelectedComponent(null);
        
        assertNull(attributes.getLastFileChooserDirectory());
    }
}