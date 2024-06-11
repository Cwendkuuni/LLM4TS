package ghm.follow.gui;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import ghm.follow.config.FollowAppAttributes;
import java.awt.Component;
import java.awt.Container;
import java.io.File;

public class TabbedPaneTest {

    private TabbedPane tabbedPane;
    private FollowAppAttributes attributes;

    @Before
    public void setUp() {
        attributes = new FollowAppAttributes();
        tabbedPane = new TabbedPane(attributes);
    }

    @Test
    public void testFindComponentAt_WithInvalidCoordinates_ReturnsNull() {
        Component component = tabbedPane.findComponentAt(0, 0);

        assertNull(component);
    }

    @Test
    public void testFindComponentAt_WhenComponentIsContainer_ReturnsCorrectComponent() {
        Container container = new Container();
        tabbedPane.add(container, "Container");

        Component component = tabbedPane.findComponentAt(0, 0);

        assertEquals(container, component);
    }

    @Test
    public void testFindComponentAt_WhenComponentIsNotVisible_ReturnsNull() {
        Component component = new Component() {
            @Override
            public boolean isVisible() {
                return false;
            }
        };
        tabbedPane.add(component, "HiddenComponent");

        Component result = tabbedPane.findComponentAt(0, 0);

        assertNull(result);
    }

    @Test
    public void testFindComponentAt_WithValidCoordinates_ReturnsComponent() {
        Component component = new Component() {
            @Override
            public boolean isVisible() {
                return true;
            }
        };
        tabbedPane.add(component, "VisibleComponent");

        Component result = tabbedPane.findComponentAt(0, 0);

        assertEquals(component, result);
    }

    @Test
    public void testSetSelectedIndex_CallsHandleSelectedFile() {
        tabbedPane.setSelectedIndex(0);

        // assert that handleSelectedFile was called
        // add assertions here
    }

    @Test
    public void testSetSelectedComponent_CallsHandleSelectedFile() {
        FileFollowingPane pane = new FileFollowingPane();
        tabbedPane.setSelectedComponent(pane);

        // assert that handleSelectedFile was called
        // add assertions here
    }

    @Test
    public void testRemoveTabAt_CallsHandleSelectedFile() {
        tabbedPane.removeTabAt(0);

        // assert that handleSelectedFile was called
        // add assertions here
    }

    @Test
    public void testHandleSelectedFile_WithNonNullPane_SetsLastFileChooserDirectory() {
        FileFollowingPane pane = new FileFollowingPane();
        File followedFile = new File("testfile.txt");
        pane.setFollowedFile(followedFile);
        tabbedPane.setSelectedComponent(pane);

        File parent = followedFile.getParentFile();
        File result = attributes.getLastFileChooserDirectory();

        assertEquals(parent, result);
    }

    @Test
    public void testHandleSelectedFile_WithNullPane_DoesNotSetLastFileChooserDirectory() {
        tabbedPane.setSelectedIndex(0);

        File result = attributes.getLastFileChooserDirectory();

        assertNull(result);
    }

}