package ghm.follow.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import ghm.follow.config.FileFollowingPane;
import ghm.follow.config.FollowAppAttributes;

public class TabbedPaneTest {

    private FollowAppAttributes attributes;
    private TabbedPane tabbedPane;
    private FileFollowingPane fileFollowingPane;

    @Before
    public void setUp() {
        attributes = mock(FollowAppAttributes.class);
        tabbedPane = new TabbedPane(attributes);
        fileFollowingPane = mock(FileFollowingPane.class);
    }

    @Test
    public void testFindComponentAt_returnsNullWhenDoesNotContainPoint() {
        assertEquals(null, tabbedPane.findComponentAt(-1, -1));
    }

    @Test
    public void testFindComponentAt_returnsComponentWhenContainsPoint() {
        tabbedPane.add(fileFollowingPane);
        when(fileFollowingPane.isVisible()).thenReturn(true);
        when(fileFollowingPane.getComponentAt(anyInt(), anyInt())).thenReturn(fileFollowingPane);
        when(fileFollowingPane.getX()).thenReturn(0);
        when(fileFollowingPane.getY()).thenReturn(0);

        assertEquals(fileFollowingPane, tabbedPane.findComponentAt(1, 1));
    }

    @Test
    public void testSetSelectedIndex_callsHandleSelectedFile() {
        tabbedPane.setSelectedIndex(0);

        verify(tabbedPane).handleSelectedFile();
    }

    @Test
    public void testSetSelectedComponent_callsHandleSelectedFile() {
        tabbedPane.setSelectedComponent(fileFollowingPane);

        verify(tabbedPane).handleSelectedFile();
    }

    @Test
    public void testRemoveTabAt_callsHandleSelectedFile() {
        tabbedPane.removeTabAt(0);

        verify(tabbedPane).handleSelectedFile();
    }

    @Test
    public void testHandleSelectedFile_setsLastFileChooserDirectoryWithParentFileWhenPaneNotNull() {
        File parentFile = mock(File.class);
        when(fileFollowingPane.getFollowedFile()).thenReturn(parentFile);
        
        tabbedPane.setSelectedComponent(fileFollowingPane);

        verify(attributes).setLastFileChooserDirectory(parentFile);
    }

    @Test
    public void testHandleSelectedFile_doesNotSetLastFileChooserDirectoryWhenPaneIsNull() {
        tabbedPane.setSelectedComponent(null);

        verify(attributes, never()).setLastFileChooserDirectory(any());
    }
}