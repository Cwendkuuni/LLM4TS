import org.junit.Assert;
import org.junit.Test;
import java.awt.Component;
import javax.swing.JTabbedPane;

public class TabbedPaneTest {
    @Test
    public void testFindComponentAt() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());

        // Test when x and y are outside the bounds of the component
        Component result = tabbedPane.findComponentAt(-1, -1);
        Assert.assertNull(result);

        // Test when x and y are inside the bounds of the component
        result = tabbedPane.findComponentAt(1, 1);
        Assert.assertNotNull(result);

        // Test when component is not visible
        tabbedPane.getComponentAt(0).setVisible(false);
        result = tabbedPane.findComponentAt(1, 1);
        Assert.assertNull(result);
    }

    @Test
    public void testSetSelectedIndex() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());

        // Test setting a valid index
        tabbedPane.add(new FileFollowingPane());
        tabbedPane.setSelectedIndex(0);
        Assert.assertEquals(0, tabbedPane.getSelectedIndex());

        // Test setting an invalid index
        tabbedPane.setSelectedIndex(1);
        Assert.assertEquals(0, tabbedPane.getSelectedIndex());
    }

    @Test
    public void testSetSelectedComponent() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        FileFollowingPane pane = new FileFollowingPane();

        // Test setting a valid component
        tabbedPane.add(pane);
        tabbedPane.setSelectedComponent(pane);
        Assert.assertEquals(pane, tabbedPane.getSelectedComponent());

        // Test setting an invalid component
        tabbedPane.setSelectedComponent(new FileFollowingPane());
        Assert.assertEquals(pane, tabbedPane.getSelectedComponent());
    }

    @Test
    public void testRemoveTabAt() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        FileFollowingPane pane = new FileFollowingPane();

        // Test removing a valid tab
        tabbedPane.add(pane);
        tabbedPane.removeTabAt(0);
        Assert.assertEquals(0, tabbedPane.getTabCount());

        // Test removing an invalid index
        tabbedPane.removeTabAt(1);
        Assert.assertEquals(0, tabbedPane.getTabCount());
    }
    
    // Test handleSelectedFile() method
    @Test
    public void testHandleSelectedFile() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        
        // Test with selected component
        FileFollowingPane pane = new FileFollowingPane();
        tabbedPane.add(pane);
        tabbedPane.setSelectedIndex(0);
        tabbedPane.handleSelectedFile();
        Assert.assertNotNull(tabbedPane.getAttributes().getLastFileChooserDirectory());
        
        // Test without selected component
        tabbedPane.setSelectedIndex(-1);
        tabbedPane.handleSelectedFile();
        Assert.assertNull(tabbedPane.getAttributes().getLastFileChooserDirectory());
    }
}