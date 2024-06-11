import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.io.File;

import static org.junit.Assert.*;

public class TabbedPaneTest {

    private TabbedPane tabbedPane;
    private FollowAppAttributes attributes;
    private FileFollowingPane fileFollowingPane;

    @Before
    public void setUp() {
        attributes = new FollowAppAttributes();
        tabbedPane = new TabbedPane(attributes);
        fileFollowingPane = new FileFollowingPane(new File("testFile.txt"));
        tabbedPane.addTab("Tab", fileFollowingPane);
    }

    @Test
    public void testFindComponentAt() {
        Component component = tabbedPane.findComponentAt(0, 0);
        assertNotNull(component);
    }

    @Test
    public void testSetSelectedIndex() {
        tabbedPane.setSelectedIndex(0);
        assertEquals(0, tabbedPane.getSelectedIndex());
    }

    @Test
    public void testSetSelectedComponent() {
        tabbedPane.setSelectedComponent(fileFollowingPane);
        assertEquals(fileFollowingPane, tabbedPane.getSelectedComponent());
    }

    @Test
    public void testRemoveTabAt() {
        tabbedPane.removeTabAt(0);
        assertEquals(0, tabbedPane.getTabCount());
    }

    @Test
    public void testHandleSelectedFile() {
        tabbedPane.setSelectedComponent(fileFollowingPane);
        tabbedPane.handleSelectedFile();
        assertEquals(attributes.getLastFileChooserDirectory(), fileFollowingPane.getFollowedFile().getParentFile());
    }
}