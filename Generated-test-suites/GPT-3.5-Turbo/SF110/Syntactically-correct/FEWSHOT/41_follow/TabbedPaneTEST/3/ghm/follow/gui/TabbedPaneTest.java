package ghm.follow.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import ghm.follow.config.FollowAppAttributes;

public class TabbedPaneTest {
    private FollowAppAttributes attributes;
    private TabbedPane tabbedPane;
    
    @Before
    public void setUp() {
        attributes = new FollowAppAttributes();
        tabbedPane = new TabbedPane(attributes);
    }
    
    @Test
    public void testFindComponentAt() {
        assertEquals(null, tabbedPane.findComponentAt(0, 0));
        // TODO: test other cases here
    }
    
    @Test
    public void testSetSelectedIndex() {
        tabbedPane.setSelectedIndex(0);
        assertEquals(0, tabbedPane.getSelectedIndex());
        // TODO: test other cases here
    }
    
    @Test
    public void testSetSelectedComponent() {
        FileFollowingPane pane = new FileFollowingPane();
        tabbedPane.setSelectedComponent(pane);
        assertEquals(pane, tabbedPane.getSelectedComponent());
        // TODO: test other cases here
    }
    
    @Test
    public void testRemoveTabAt() {
        tabbedPane.addTab("Tab 1", new JPanel());
        tabbedPane.addTab("Tab 2", new JPanel());
        tabbedPane.removeTabAt(1);
        assertEquals(1, tabbedPane.getTabCount());
        // TODO: test other cases here
    }
    
    @Test
    public void testHandleSelectedFile() {
        FileFollowingPane pane = new FileFollowingPane();
        File file = new File("test.txt");
        pane.setFollowedFile(file);
        tabbedPane.setSelectedComponent(pane);
        tabbedPane.handleSelectedFile();
        assertEquals(file.getParentFile(), attributes.getLastFileChooserDirectory());
        // TODO: test other cases here
    }
}