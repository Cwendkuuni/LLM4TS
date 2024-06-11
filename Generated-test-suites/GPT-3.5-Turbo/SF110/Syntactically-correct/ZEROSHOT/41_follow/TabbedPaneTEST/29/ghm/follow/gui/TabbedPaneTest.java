package ghm.follow.gui.test;

import ghm.follow.config.FollowAppAttributes;
import ghm.follow.gui.TabbedPane;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.io.File;

import static org.junit.Assert.*;

public class TabbedPaneTest {

    private TabbedPane tabbedPane;

    @Before
    public void setUp() {
        FollowAppAttributes attributes = new FollowAppAttributes();
        tabbedPane = new TabbedPane(attributes);
    }

    @Test
    public void testFindComponentAt_WithValidCoordinates_ReturnsComponent() {
        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 100, 100);
        tabbedPane.add(panel);

        Component component = tabbedPane.findComponentAt(50, 50);

        assertNotNull(component);
    }

    @Test
    public void testFindComponentAt_WithInvalidCoordinates_ReturnsNull() {
        Component component = tabbedPane.findComponentAt(100, 100);

        assertNull(component);
    }

    @Test
    public void testSetSelectedIndex_CallsHandleSelectedFile() {
        FileFollowingPane pane = new FileFollowingPane();
        tabbedPane.add(pane);

        tabbedPane.setSelectedIndex(0);

        assertTrue(pane.isHandleSelectedFileCalled());
    }

    @Test
    public void testSetSelectedComponent_CallsHandleSelectedFile() {
        FileFollowingPane pane = new FileFollowingPane();
        tabbedPane.add(pane);

        tabbedPane.setSelectedComponent(pane);

        assertTrue(pane.isHandleSelectedFileCalled());
    }

    @Test
    public void testRemoveTabAt_CallsHandleSelectedFile() {
        FileFollowingPane pane = new FileFollowingPane();
        tabbedPane.add(pane);

        tabbedPane.removeTabAt(0);

        assertTrue(pane.isHandleSelectedFileCalled());
    }

    private static class FileFollowingPane extends JPanel {
        private boolean handleSelectedFileCalled;

        public FileFollowingPane() {
            handleSelectedFileCalled = false;
        }

        public boolean isHandleSelectedFileCalled() {
            return handleSelectedFileCalled;
        }

        @Override
        public void setVisible(boolean visible) {
            super.setVisible(visible);
            handleSelectedFileCalled = true;
        }

        public File getFollowedFile() {
            return new File("dummyFile");
        }
    }
}