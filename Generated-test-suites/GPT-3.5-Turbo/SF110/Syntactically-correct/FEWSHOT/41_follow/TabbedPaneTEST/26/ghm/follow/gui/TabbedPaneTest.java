import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import ghm.follow.config.FollowAppAttributes;
import ghm.follow.gui.FileFollowingPane;

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
    public void testFindComponentAt_NotContains() {
        assertNull(tabbedPane.findComponentAt(0, 0));
    }
    
    @Test
    public void testFindComponentAt_Contains_Container_NotVisible() {
        tabbedPane.add(fileFollowingPane);
        when(fileFollowingPane.isVisible()).thenReturn(false);
        
        assertEquals(tabbedPane, tabbedPane.findComponentAt(0, 0));
    }
    
    @Test
    public void testFindComponentAt_Contains_Container_Visible() {
        tabbedPane.add(fileFollowingPane);
        when(fileFollowingPane.isVisible()).thenReturn(true);
        when(fileFollowingPane.getX()).thenReturn(0);
        when(fileFollowingPane.getY()).thenReturn(0);
        when(fileFollowingPane.findComponentAt(0, 0)).thenReturn(fileFollowingPane);
        
        assertEquals(fileFollowingPane, tabbedPane.findComponentAt(0, 0));
    }
    
    @Test
    public void testFindComponentAt_Contains_NotContainer() {
        tabbedPane.add(fileFollowingPane);
        when(fileFollowingPane.getX()).thenReturn(0);
        when(fileFollowingPane.getY()).thenReturn(0);
        
        assertEquals(fileFollowingPane, tabbedPane.findComponentAt(0, 0));
    }
    
    @Test
    public void testSetSelectedIndex() {
        tabbedPane.setSelectedIndex(0);
        
        verify(attributes, times(1)).setLastFileChooserDirectory(any(File.class));
    }
    
    @Test
    public void testSetSelectedComponent() {
        tabbedPane.setSelectedComponent(fileFollowingPane);
        
        verify(attributes, times(1)).setLastFileChooserDirectory(any(File.class));
    }
    
    @Test
    public void testRemoveTabAt() {
        tabbedPane.removeTabAt(0);
        
        verify(attributes, times(1)).setLastFileChooserDirectory(any(File.class));
    }
    
    @Test
    public void testHandleSelectedFile_NotNull() {
        when(tabbedPane.getSelectedComponent()).thenReturn(fileFollowingPane);
        File file = new File("test file");
        when(fileFollowingPane.getFollowedFile()).thenReturn(file);
        File parent = mock(File.class);
        when(file.getParentFile()).thenReturn(parent);
        
        tabbedPane.handleSelectedFile();
        
        verify(attributes, times(1)).setLastFileChooserDirectory(parent);
    }
    
    @Test
    public void testHandleSelectedFile_Null() {
        when(tabbedPane.getSelectedComponent()).thenReturn(null);
        
        tabbedPane.handleSelectedFile();
        
        verify(attributes, never()).setLastFileChooserDirectory(any(File.class));
    }
}