import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TabbedPaneTest {

    private TabbedPane tabbedPane;
    private FollowAppAttributes attributes;

    @Before
    public void setUp() {
        attributes = new FollowAppAttributes();
        tabbedPane = new TabbedPane(attributes);
    }

    @Test
    public void testFindComponentAt_NullResult() {
        Component result = tabbedPane.findComponentAt(100, 100);
        assertNull(result);
    }

    @Test
    public void testFindComponentAt_ComponentFound() {
        JPanel panel = new JPanel();
        tabbedPane.add(panel, "Panel");
        Component result = tabbedPane.findComponentAt(panel.getX() + 10, panel.getY() + 10);
        assertEquals(panel, result);
    }

    @Test
    public void testSetSelectedIndex() {
        tabbedPane.add(new FileFollowingPane(), "FilePane1");
        tabbedPane.add(new FileFollowingPane(), "FilePane2");
        tabbedPane.add(new FileFollowingPane(), "FilePane3");

        tabbedPane.setSelectedIndex(2);

        FileFollowingPane selectedPane = (FileFollowingPane) tabbedPane.getSelectedComponent();
        assertNotNull(selectedPane);
        assertEquals("FilePane3", tabbedPane.getTitleAt(2));

        // Test handling when no selected component is present
        tabbedPane.removeTabAt(2);

        tabbedPane.setSelectedIndex(2);

        assertNull(tabbedPane.getSelectedComponent());
    }

    @Test
    public void testSetSelectedComponent() {
        FileFollowingPane pane1 = new FileFollowingPane();
        tabbedPane.add(pane1, "FilePane1");
        FileFollowingPane pane2 = new FileFollowingPane();
        tabbedPane.add(pane2, "FilePane2");

        tabbedPane.setSelectedComponent(pane2);

        assertTrue(pane2.isShowing());
        assertEquals(1, tabbedPane.getSelectedIndex());

        // Test handling when null component is passed
        tabbedPane.setSelectedComponent(null);

        assertNull(tabbedPane.getSelectedComponent());
    }

    @Test
    public void testRemoveTabAt() {
        FileFollowingPane pane1 = new FileFollowingPane();
        tabbedPane.add(pane1, "FilePane1");
        FileFollowingPane pane2 = new FileFollowingPane();
        tabbedPane.add(pane2, "FilePane2");
        FileFollowingPane pane3 = new FileFollowingPane();
        tabbedPane.add(pane3, "FilePane3");

        tabbedPane.removeTabAt(1);

        assertEquals(2, tabbedPane.getTabCount());
        assertEquals(pane3, tabbedPane.getSelectedComponent());

        // Test handling when index is out of bounds
        tabbedPane.removeTabAt(5);

        assertEquals(2, tabbedPane.getTabCount());
        assertEquals(pane3, tabbedPane.getSelectedComponent());
    }

    @Test
    public void testHandleSelectedFile() {
        FileFollowingPane pane = new FileFollowingPane();
        File file = new File("test.txt");
        pane.setFollowedFile(file);
        tabbedPane.add(pane);

        tabbedPane.setSelectedComponent(pane);
        tabbedPane.handleSelectedFile();

        assertEquals(file.getParent(), attributes.getLastFileChooserDirectory().getPath());

        // Test handling when no selected component is present
        tabbedPane.remove(pane);
        tabbedPane.handleSelectedFile();

        assertNull(attributes.getLastFileChooserDirectory());
    }
}