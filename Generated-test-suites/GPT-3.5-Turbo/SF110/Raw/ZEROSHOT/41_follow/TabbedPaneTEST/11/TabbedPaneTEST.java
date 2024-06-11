import org.junit.Test;
import static org.junit.Assert.*;

public class TabbedPaneTest {

    @Test
    public void testFindComponentAt_WhenComponentNull_ShouldReturnNull() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        Component comp = tabbedPane.findComponentAt(0, 0);
        assertNull(comp);
    }

    @Test
    public void testFindComponentAt_WhenComponentVisible_ShouldReturnComponent() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        JLabel label = new JLabel();
        label.setVisible(true);
        tabbedPane.add(label, "Label");
        Component comp = tabbedPane.findComponentAt(0, 0);
        assertEquals(label, comp);
    }

    @Test
    public void testFindComponentAt_WhenComponentInvisible_ShouldReturnNull() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        JLabel label = new JLabel();
        label.setVisible(false);
        tabbedPane.add(label, "Label");
        Component comp = tabbedPane.findComponentAt(0, 0);
        assertNull(comp);
    }

    @Test
    public void testSetSelectedIndex_ShouldUpdateSelectedIndex() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        tabbedPane.add(new JLabel(), "Label 1");
        tabbedPane.add(new JLabel(), "Label 2");
        tabbedPane.setSelectedIndex(1);
        assertEquals(1, tabbedPane.getSelectedIndex());
    }

    @Test
    public void testSetSelectedComponent_ShouldUpdateSelectedComponent() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        JLabel label1 = new JLabel();
        JLabel label2 = new JLabel();
        tabbedPane.add(label1, "Label 1");
        tabbedPane.add(label2, "Label 2");
        tabbedPane.setSelectedComponent(label2);
        assertEquals(label2, tabbedPane.getSelectedComponent());
    }

    @Test
    public void testRemoveTabAt_ShouldRemoveTabAtIndex() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        JLabel label1 = new JLabel();
        JLabel label2 = new JLabel();
        tabbedPane.add(label1, "Label 1");
        tabbedPane.add(label2, "Label 2");
        tabbedPane.removeTabAt(1);
        assertEquals(1, tabbedPane.getTabCount());
        assertEquals(label1, tabbedPane.getComponentAt(0));
    }
    
    @Test(expected = ClassCastException.class)
    public void testHandleSelectedFile_WhenSelectedComponentNotFileFollowingPane_ShouldThrowClassCastException() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        JLabel label = new JLabel();
        tabbedPane.add(label, "Label");
        tabbedPane.setSelectedComponent(label);
        tabbedPane.handleSelectedFile();
    }

    @Test
    public void testHandleSelectedFile_WhenSelectedComponentIsFileFollowingPane_ShouldSetLastFileChooserDirectory() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        File file = new File("example/file.txt");
        FileFollowingPane pane = new FileFollowingPane(file);
        tabbedPane.add(pane, "File Pane");
        tabbedPane.setSelectedComponent(pane);
        tabbedPane.handleSelectedFile();
        assertEquals(file.getParentFile(), tabbedPane.getAttributes().getLastFileChooserDirectory());
    }
}