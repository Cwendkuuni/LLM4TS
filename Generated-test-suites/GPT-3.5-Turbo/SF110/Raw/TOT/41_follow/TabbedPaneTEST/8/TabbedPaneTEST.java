
import org.junit.Test;
import static org.junit.Assert.*;

public class TabbedPaneTest {

    @Test
    public void testFindComponentAt_Contains() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        assertNotNull(tabbedPane.findComponentAt(0, 0));
    }

    @Test
    public void testFindComponentAt_NotContains() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        assertNull(tabbedPane.findComponentAt(-1, -1));
    }

    @Test
    public void testFindComponentAt_ContainerVisible() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        tabbedPane.add(new Container());
        assertNotNull(tabbedPane.findComponentAt(0, 0));
    }

    @Test
    public void testFindComponentAt_ContainerInvisible() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        Container container = new Container();
        container.setVisible(false);
        tabbedPane.add(container);
        assertNull(tabbedPane.findComponentAt(0, 0));
    }

    @Test
    public void testSetSelectedIndex() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        tabbedPane.add(new FileFollowingPane());
        tabbedPane.setSelectedIndex(0);
        // Assert selected index is 0
        assertEquals(0, tabbedPane.getSelectedIndex());
    }

    @Test
    public void testSetSelectedComponent() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        FileFollowingPane fileFollowingPane = new FileFollowingPane();
        tabbedPane.add(fileFollowingPane);
        tabbedPane.setSelectedComponent(fileFollowingPane);
        // Assert selected component is the FileFollowingPane
        assertEquals(fileFollowingPane, tabbedPane.getSelectedComponent());
    }

    @Test
    public void testRemoveTabAt() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        FileFollowingPane fileFollowingPane1 = new FileFollowingPane();
        FileFollowingPane fileFollowingPane2 = new FileFollowingPane();
        tabbedPane.add(fileFollowingPane1);
        tabbedPane.add(fileFollowingPane2);
        tabbedPane.removeTabAt(0);
        // Assert the size of tabbedPane is 1
        assertEquals(1, tabbedPane.getTabCount());
    }

    @Test
    public void testHandleSelectedFile() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        FileFollowingPane fileFollowingPane = new FileFollowingPane();
        File testFile = new File("test.txt");
        fileFollowingPane.setFollowedFile(testFile);
        tabbedPane.add(fileFollowingPane);
        tabbedPane.setSelectedComponent(fileFollowingPane);
        // Assert that the last file chooser directory is set to the parent directory of testFile
        assertEquals(testFile.getParentFile(), tabbedPane.attributes.getLastFileChooserDirectory());
    }
}

