import org.junit.*;
import static org.junit.Assert.*;

public class TabbedPaneTest {

    @Test
    public void testFindComponentAt_successful() {
        // Typical use case: coordinates inside a visible component
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        
        // Add a component with a known location inside the tabbed pane
        JPanel panel = new JPanel();
        panel.setBounds(10, 10, 100, 100);
        tabbedPane.addTab("Tab 1", panel);
        
        // Test coordinates inside the added component
        Component result = tabbedPane.findComponentAt(20, 20);
        
        // Check that the returned component is the added panel
        assertEquals(panel, result);
    }
    
    @Test
    public void testFindComponentAt_nullResult() {
        // Edge case: coordinates outside the tabbed pane
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        
        // Test coordinates outside the tabbed pane
        Component result = tabbedPane.findComponentAt(200, 200);
        
        // Check that the result is null
        assertNull(result);
    }
    
    @Test
    public void testSetSelectedIndex() {
        // Typical use case: selecting an existing tab
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        
        // Add two tabs to the tabbed pane
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        tabbedPane.addTab("Tab 1", panel1);
        tabbedPane.addTab("Tab 2", panel2);
        
        // Select the second tab by index
        tabbedPane.setSelectedIndex(1);
        
        // Check that the selected component is now the second panel
        assertEquals(panel2, tabbedPane.getSelectedComponent());
    }
    
    @Test
    public void testSetSelectedComponent() {
        // Typical use case: selecting an existing tab by component
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        
        // Add two tabs to the tabbed pane
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        tabbedPane.addTab("Tab 1", panel1);
        tabbedPane.addTab("Tab 2", panel2);
        
        // Select the second tab by component
        tabbedPane.setSelectedComponent(panel2);
        
        // Check that the selected component is now the second panel
        assertEquals(panel2, tabbedPane.getSelectedComponent());
    }
    
    @Test
    public void testRemoveTabAt() {
        // Typical use case: removing an existing tab by index
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        
        // Add two tabs to the tabbed pane
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        tabbedPane.addTab("Tab 1", panel1);
        tabbedPane.addTab("Tab 2", panel2);
        
        // Remove the first tab by index
        tabbedPane.removeTabAt(0);
        
        // Check that the first tab is removed and the second tab is now selected
        assertEquals(panel2, tabbedPane.getSelectedComponent());
        assertEquals(1, tabbedPane.getTabCount());
    }
    
    @Test
    public void testHandleSelectedFile() {
        // Typical use case: selecting a file-following pane
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        
        // Create a file-following pane and set it as the selected component
        FileFollowingPane pane = new FileFollowingPane(new File("test.txt"));
        tabbedPane.setSelectedComponent(pane);
        
        // Call the handleSelectedFile method
        tabbedPane.handleSelectedFile();
        
        // Check that the last file chooser directory is set to the parent directory of the selected file
        assertEquals(new File("."), tabbedPane.getAttributes().getLastFileChooserDirectory());
    }
}