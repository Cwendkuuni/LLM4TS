import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

public class TabbedPaneTest {
    private TabbedPane tabbedPane;
    
    @Before
    public void setUp() {
        FollowAppAttributes attributes = new FollowAppAttributes();
        tabbedPane = new TabbedPane(attributes);
    }
    
    @Test
    public void testFindComponentAt() {
        assertNotNull(tabbedPane.findComponentAt(0, 0));
    }
    
    @Test
    public void testSetSelectedIndex() {
        tabbedPane.setSelectedIndex(0);
        assertEquals(0, tabbedPane.getSelectedIndex());
    }
    
    @Test
    public void testSetSelectedComponent() {
        FileFollowingPane pane = new FileFollowingPane();
        tabbedPane.setSelectedComponent(pane);
        assertEquals(pane, tabbedPane.getSelectedComponent());
    }
    
    @Test
    public void testRemoveTabAt() {
        tabbedPane.addTab("Tab 1", new JPanel());
        tabbedPane.addTab("Tab 2", new JPanel());
        tabbedPane.removeTabAt(0);
        assertEquals(1, tabbedPane.getTabCount());
    }
    
    @Test
    public void testHandleSelectedFile() {
        File file = new File("test.txt");
        FileFollowingPane pane = new FileFollowingPane(file);
        tabbedPane.setSelectedComponent(pane);
        tabbedPane.handleSelectedFile();
        assertEquals(file.getParentFile(), tabbedPane.getAttributes().getLastFileChooserDirectory());
    }
    
    private class FileFollowingPane extends JPanel {
        private File followedFile;
        
        public FileFollowingPane() {
            followedFile = null;
        }
        
        public FileFollowingPane(File file) {
            followedFile = file;
        }
        
        public File getFollowedFile() {
            return followedFile;
        }
    }
}