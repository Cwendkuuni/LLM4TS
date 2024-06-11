import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class TabbedPaneTest {

    private TabbedPane tabbedPane;
    private FileFollowingPane fileFollowingPane;

    @Before
    public void setUp() {
        FollowAppAttributes attributes = new FollowAppAttributes();
        tabbedPane = new TabbedPane(attributes);
        fileFollowingPane = new FileFollowingPane(attributes);
    }

    @Test
    public void testFindComponentAt_containsValidCoordinates_returnsComponent() {
        JPanel panel = new JPanel();
        tabbedPane.add(panel, "Panel");

        Component result = tabbedPane.findComponentAt(10, 10);

        assertEquals(panel, result);
    }

    @Test
    public void testFindComponentAt_containsInvalidCoordinates_returnsNull() {
        JPanel panel = new JPanel();
        tabbedPane.add(panel, "Panel");

        Component result = tabbedPane.findComponentAt(100, 100);

        assertNull(result);
    }

    @Test
    public void testFindComponentAt_containsNestedComponent_returnsNestedComponent() {
        JPanel panel = new JPanel();
        JPanel nestedPanel = new JPanel();
        panel.add(nestedPanel);
        tabbedPane.add(panel, "Panel");

        Component result = tabbedPane.findComponentAt(10, 10);

        assertEquals(nestedPanel, result);
    }

    @Test
    public void testFindComponentAt_containsHiddenComponent_returnsNull() {
        JPanel panel = new JPanel();
        panel.setVisible(false);
        tabbedPane.add(panel, "Panel");

        Component result = tabbedPane.findComponentAt(10, 10);

        assertNull(result);
    }

    @Test
    public void testSetSelectedIndex_validIndex_callsHandleSelectedFile() {
        tabbedPane.setSelectedIndex(1);

        assertNotNull(tabbedPane.getSelectedComponent());
        assertTrue(fileFollowingPane.isHandleSelectedFileCalled());
    }

    @Test
    public void testSetSelectedComponent_validComponent_callsHandleSelectedFile() {
        tabbedPane.add(fileFollowingPane, "FileFollowingPane");
        tabbedPane.setSelectedComponent(fileFollowingPane);

        assertTrue(fileFollowingPane.isHandleSelectedFileCalled());
    }

    @Test(expected = IndexOutOfBoundsException.class) 
    public void testRemoveTabAt_invalidIndex_throwsIndexOutOfBoundsException() {
        tabbedPane.removeTabAt(0);
    }

    @Test
    public void testRemoveTabAt_validIndex_callsHandleSelectedFile() {
        tabbedPane.add(fileFollowingPane, "FileFollowingPane");
        tabbedPane.removeTabAt(0);

        assertTrue(fileFollowingPane.isHandleSelectedFileCalled());
    }

    @Test
    public void testHandleSelectedFile_validComponent_setsLastFileChooserDirectory() throws IOException {
        File tempFile = File.createTempFile("test", ".txt");
        File tempFileParent = tempFile.getParentFile();
        fileFollowingPane.setFollowedFile(tempFile);
        tabbedPane.add(fileFollowingPane, "FileFollowingPane");
        tabbedPane.setSelectedComponent(fileFollowingPane);

        assertEquals(tempFileParent, tabbedPane.getAttributes().getLastFileChooserDirectory());
    }

    @Test
    public void testHandleSelectedFile_nullComponent_doesNothing() {
        tabbedPane.handleSelectedFile();

        assertNull(tabbedPane.getAttributes().getLastFileChooserDirectory());
    }

}