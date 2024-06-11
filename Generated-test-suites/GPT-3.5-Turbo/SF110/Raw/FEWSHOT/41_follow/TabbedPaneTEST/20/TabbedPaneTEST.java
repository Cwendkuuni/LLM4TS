import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import ghm.follow.config.FileFollowingPane;
import ghm.follow.config.FollowAppAttributes;
import org.junit.Before;
import org.junit.Test;

public class TabbedPaneTest {

    private FollowAppAttributes attributes;
    private TabbedPane tabbedPane;
    
    @Before
    public void setup() {
        attributes = new FollowAppAttributes();
        tabbedPane = new TabbedPane(attributes);
    }

    @Test
    public void testFindComponentAt() {
        assertEquals(null, tabbedPane.findComponentAt(0, 0));
        FileFollowingPane pane = mock(FileFollowingPane.class);
        when(pane.isVisible()).thenReturn(true);
        when(pane.getFollowedFile()).thenReturn(new File("path/to/file"));
        tabbedPane.add(pane);
        assertEquals(pane, tabbedPane.findComponentAt(10, 10));
        assertEquals(tabbedPane, tabbedPane.findComponentAt(100, 100));
    }

    @Test
    public void testSetSelectedIndex() {
        tabbedPane.add(new FileFollowingPane(new File("file1")));
        tabbedPane.add(new FileFollowingPane(new File("file2")));
        tabbedPane.setSelectedIndex(1);
        assertEquals(1, tabbedPane.getSelectedIndex());
    }
    
    @Test
    public void testSetSelectedComponent() {
        FileFollowingPane pane1 = new FileFollowingPane(new File("file1"));
        FileFollowingPane pane2 = new FileFollowingPane(new File("file2"));
        tabbedPane.add(pane1);
        tabbedPane.add(pane2);
        tabbedPane.setSelectedComponent(pane2);
        assertEquals(1, tabbedPane.getSelectedIndex());
    }
    
    @Test
    public void testRemoveTabAt() {
        FileFollowingPane pane1 = new FileFollowingPane(new File("file1"));
        FileFollowingPane pane2 = new FileFollowingPane(new File("file2"));
        FileFollowingPane pane3 = new FileFollowingPane(new File("file3"));
        tabbedPane.add(pane1);
        tabbedPane.add(pane2);
        tabbedPane.add(pane3);
        tabbedPane.removeTabAt(1);
        assertEquals(2, tabbedPane.getTabCount());
    }
}