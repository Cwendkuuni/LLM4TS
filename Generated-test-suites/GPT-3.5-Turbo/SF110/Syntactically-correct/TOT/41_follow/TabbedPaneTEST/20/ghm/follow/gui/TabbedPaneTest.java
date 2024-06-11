import org.junit.Test;
import static org.junit.Assert.*;

public class TabbedPaneTest {

    @Test
    public void testFindComponentAt_typical() {
        // Typical use case: The specified point is within the component
        // Should return the component at the specified point
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        tabbedPane.setPreferredSize(new Dimension(200, 200));
        JPanel panel = new JPanel();
        tabbedPane.addTab("Tab 1", panel);
        Component result = tabbedPane.findComponentAt(100, 100);
        assertEquals(panel, result);
    }

    @Test
    public void testFindComponentAt_edge() {
        // Edge case: The specified point is on the edge of the component
        // Should return the component at the specified point
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        tabbedPane.setPreferredSize(new Dimension(200, 200));
        JPanel panel = new JPanel();
        tabbedPane.addTab("Tab 1", panel);
        Component result = tabbedPane.findComponentAt(200, 0);
        assertEquals(tabbedPane, result);
    }

    @Test
    public void testFindComponentAt_null() {
        // Edge case: The specified point is outside the component
        // Should return null
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        tabbedPane.setPreferredSize(new Dimension(200, 200));
        Component result = tabbedPane.findComponentAt(300, 300);
        assertNull(result);
    }

    @Test
    public void testSetSelectedIndex_typical() {
        // Typical use case: Setting the selected index to a valid index
        // Should set the selected index and call handleSelectedFile()
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        tabbedPane.addTab("Tab 1", panel1);
        tabbedPane.addTab("Tab 2", panel2);
        
        tabbedPane.setSelectedIndex(1);
        assertEquals(1, tabbedPane.getSelectedIndex());
        // Verify handleSelectedFile() is called
    }

    @Test
    public void testSetSelectedComponent_typical() {
        // Typical use case: Setting the selected component to a valid component
        // Should set the selected component and call handleSelectedFile()
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        tabbedPane.addTab("Tab 1", panel1);
        tabbedPane.addTab("Tab 2", panel2);
        
        tabbedPane.setSelectedComponent(panel2);
        assertEquals(panel2, tabbedPane.getSelectedComponent());
        // Verify handleSelectedFile() is called
    }

    @Test
    public void testRemoveTabAt_typical() {
        // Typical use case: Removing a tab at a valid index
        // Should remove the tab and call handleSelectedFile()
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        tabbedPane.addTab("Tab 1", panel1);
        tabbedPane.addTab("Tab 2", panel2);
        
        tabbedPane.removeTabAt(1);
        assertEquals(1, tabbedPane.getTabCount());
        // Verify handleSelectedFile() is called
    }

    @Test
    public void testHandleSelectedFile_typical() {
        // Typical use case: Selected component is a FileFollowingPane with a valid file
        // Should set the LastFileChooserDirectory attribute to the parent directory of the file
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        FileFollowingPane pane = new FileFollowingPane(new File("path/to/file.txt"));
        tabbedPane.add(pane);
        
        tabbedPane.handleSelectedFile();
        assertEquals(new File("path/to"), tabbedPane.getAttributes().getLastFileChooserDirectory());
    }
}