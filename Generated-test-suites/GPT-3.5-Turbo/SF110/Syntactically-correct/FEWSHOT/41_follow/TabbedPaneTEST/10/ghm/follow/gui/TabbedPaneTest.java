
package ghm.follow.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.Component;
import java.awt.Container;
import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTabbedPane;

import org.junit.Before;
import org.junit.Test;

import ghm.follow.config.FollowAppAttributes;

public class TabbedPaneTest {

    private FollowAppAttributes attributes;
    private TabbedPane tabbedPane;
    private FileFollowingPane fileFollowingPane1;
    private FileFollowingPane fileFollowingPane2;
    private FileFollowingPane fileFollowingPane3;

    @Before
    public void setUp() {
        attributes = new FollowAppAttributes();
        tabbedPane = new TabbedPane(attributes);
        fileFollowingPane1 = new FileFollowingPane(new File("/file1.txt"));
        fileFollowingPane2 = new FileFollowingPane(new File("/file2.txt"));
        fileFollowingPane3 = new FileFollowingPane(new File("/file3.txt"));
        tabbedPane.addTab("File 1", fileFollowingPane1);
        tabbedPane.addTab("File 2", fileFollowingPane2);
        tabbedPane.addTab("File 3", fileFollowingPane3);
    }

    @Test
    public void testFindComponentAt() {
        Component component = tabbedPane.findComponentAt(0, 0);
        assertEquals(tabbedPane, component);

        component = tabbedPane.findComponentAt(100, 100);
        assertEquals(fileFollowingPane1, component);

        component = tabbedPane.findComponentAt(200, 200);
        assertEquals(fileFollowingPane2, component);

        component = tabbedPane.findComponentAt(300, 300);
        assertEquals(fileFollowingPane3, component);

        component = tabbedPane.findComponentAt(400, 400);
        assertNull(component);

        component = tabbedPane.findComponentAt(50, 50);
        assertEquals(fileFollowingPane1, component);

        component = tabbedPane.findComponentAt(150, 150);
        assertEquals(fileFollowingPane2, component);

        component = tabbedPane.findComponentAt(250, 250);
        assertEquals(fileFollowingPane3, component);
    }

    @Test
    public void testSetSelectedIndex() {
        assertEquals(0, tabbedPane.getSelectedIndex());

        tabbedPane.setSelectedIndex(1);

        assertEquals(1, tabbedPane.getSelectedIndex());

        tabbedPane.setSelectedIndex(2);

        assertEquals(2, tabbedPane.getSelectedIndex());
    }

    @Test
    public void testSetSelectedComponent() {
        assertEquals(0, tabbedPane.getSelectedIndex());

        tabbedPane.setSelectedComponent(fileFollowingPane2);

        assertEquals(1, tabbedPane.getSelectedIndex());

        tabbedPane.setSelectedComponent(fileFollowingPane3);

        assertEquals(2, tabbedPane.getSelectedIndex());
    }

    @Test
    public void testRemoveTabAt() {
        assertEquals(3, tabbedPane.getTabCount());

        tabbedPane.removeTabAt(1);

        assertEquals(2, tabbedPane.getTabCount());

        tabbedPane.removeTabAt(1);

        assertEquals(1, tabbedPane.getTabCount());

        tabbedPane.removeTabAt(0);

        assertEquals(0, tabbedPane.getTabCount());
    }

    @Test
    public void testHandleSelectedFile() {
        assertFalse(attributes.getLastFileChooserDirectory().isPresent());

        tabbedPane.setSelectedComponent(fileFollowingPane1);

        File parent = fileFollowingPane1.getFollowedFile().getParentFile();
        assertTrue(attributes.getLastFileChooserDirectory().isPresent());
        assertEquals(parent, attributes.getLastFileChooserDirectory().get());

        tabbedPane.setSelectedComponent(fileFollowingPane2);

        parent = fileFollowingPane2.getFollowedFile().getParentFile();
        assertTrue(attributes.getLastFileChooserDirectory().isPresent());
        assertEquals(parent, attributes.getLastFileChooserDirectory().get());

        tabbedPane.setSelectedComponent(fileFollowingPane3);

        parent = fileFollowingPane3.getFollowedFile().getParentFile();
        assertTrue(attributes.getLastFileChooserDirectory().isPresent());
        assertEquals(parent, attributes.getLastFileChooserDirectory().get());
    }
}
