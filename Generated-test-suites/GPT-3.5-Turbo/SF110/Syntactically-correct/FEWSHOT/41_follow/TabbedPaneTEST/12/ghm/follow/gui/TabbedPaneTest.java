package ghm.follow.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class TabbedPaneTest {

    private TabbedPane tabbedPane;
    private FollowAppAttributes mockAttributes;

    @Before
    public void setUp() {
        mockAttributes = mock(FollowAppAttributes.class);
        tabbedPane = new TabbedPane(mockAttributes);
    }

    @Test
    public void testFindComponentAt_withInvalidCoordinates_returnsNull() {
        assertNull(tabbedPane.findComponentAt(-1, -1));
        assertNull(tabbedPane.findComponentAt(tabbedPane.getWidth() + 1, tabbedPane.getHeight() + 1));
    }

    @Test
    public void testFindComponentAt_withValidCoordinates_returnsComponent() {
        // Test when the selected component is not null and visible
        FileFollowingPane mockPane = mock(FileFollowingPane.class);
        tabbedPane.addTab("tab", mockPane);
        when(mockPane.isVisible()).thenReturn(true);
        when(mockPane.getX()).thenReturn(0);
        when(mockPane.getY()).thenReturn(0);
        when(mockPane.findComponentAt(anyInt(), anyInt())).thenReturn(mock(Component.class));

        assertEquals(mockPane.findComponentAt(5, 5), tabbedPane.findComponentAt(5, 5));
        
        // Test when the selected component is not null but not visible
        when(mockPane.isVisible()).thenReturn(false);
        assertNull(tabbedPane.findComponentAt(5, 5));
        
        // Test when the selected component is null
        tabbedPane.setSelectedComponent(null);
        assertNull(tabbedPane.findComponentAt(5, 5));
    }

    @Test
    public void testSetSelectedIndex_callsHandleSelectedFile() {
        int index = 0;
        tabbedPane.setSelectedIndex(index);
        verify(tabbedPane).handleSelectedFile();
    }

    @Test
    public void testSetSelectedComponent_callsHandleSelectedFile() {
        FileFollowingPane mockPane = mock(FileFollowingPane.class);
        tabbedPane.setSelectedComponent(mockPane);
        verify(tabbedPane).handleSelectedFile();
    }

    @Test
    public void testRemoveTabAt_callsHandleSelectedFile() {
        int index = 0;
        tabbedPane.removeTabAt(index);
        verify(tabbedPane).handleSelectedFile();
    }

    @Test
    public void testHandleSelectedFile_setsLastFileChooserDirectory() {
        FileFollowingPane mockPane = mock(FileFollowingPane.class);
        File mockFile = mock(File.class);
        File mockParent = mock(File.class);
        when(mockPane.getFollowedFile()).thenReturn(mockFile);
        when(mockFile.getParentFile()).thenReturn(mockParent);

        tabbedPane.setSelectedComponent(mockPane);

        verify(mockAttributes).setLastFileChooserDirectory(mockParent);
    }

}