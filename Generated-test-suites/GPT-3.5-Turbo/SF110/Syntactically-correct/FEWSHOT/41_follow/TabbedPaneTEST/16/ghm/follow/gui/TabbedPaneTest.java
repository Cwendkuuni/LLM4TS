
package ghm.follow.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class TabbedPaneTest {
    
    private FollowAppAttributes mockAttributes;
    private TabbedPane tabbedPane;
    
    @Before
    public void setUp() {
        mockAttributes = mock(FollowAppAttributes.class);
        tabbedPane = new TabbedPane(mockAttributes);
    }
    
    @Test
    public void testFindComponentAt_WhenContainsXY_ThenReturnComponent() {
        // Arrange
        Component mockComponent = mock(Component.class);
        when(tabbedPane.contains(anyInt(), anyInt())).thenReturn(true);
        when(tabbedPane.getComponentCount()).thenReturn(1);
        when(tabbedPane.getComponentAt(0)).thenReturn(mockComponent);
        when(mockComponent.isVisible()).thenReturn(true);
        when(mockComponent.getComponentAt(anyInt(), anyInt())).thenReturn(mockComponent);
        
        // Act
        Component result = tabbedPane.findComponentAt(0, 0);
        
        // Assert
        assertEquals(mockComponent, result);
        verify(mockComponent).getX();
        verify(mockComponent).getY();
        verify(mockComponent).isVisible();
        verify(mockComponent).getComponentAt(0, 0);
    }
    
    @Test
    public void testFindComponentAt_WhenNotContainsXY_ThenReturnNull() {
        // Arrange
        when(tabbedPane.contains(anyInt(), anyInt())).thenReturn(false);
        
        // Act
        Component result = tabbedPane.findComponentAt(0, 0);
        
        // Assert
        assertNull(result);
    }
    
    @Test
    public void testSetSelectedIndex_ThenHandleSelectedFile() {
        // Arrange
        int index = 0;
        
        // Act
        tabbedPane.setSelectedIndex(index);
        
        // Assert
        verify(tabbedPane).handleSelectedFile();
    }
    
    @Test
    public void testSetSelectedComponent_ThenHandleSelectedFile() {
        // Arrange
        FileFollowingPane mockPane = mock(FileFollowingPane.class);
        
        // Act
        tabbedPane.setSelectedComponent(mockPane);
        
        // Assert
        verify(tabbedPane).handleSelectedFile();
    }
    
    @Test
    public void testRemoveTabAt_ThenHandleSelectedFile() {
        // Arrange
        int index = 0;
        
        // Act
        tabbedPane.removeTabAt(index);
        
        // Assert
        verify(tabbedPane).handleSelectedFile();
    }
    
    @Test
    public void testHandleSelectedFile_WhenPaneNotNull_ThenSetLastFileChooserDirectory() {
        // Arrange
        FileFollowingPane mockPane = mock(FileFollowingPane.class);
        File mockFile = mock(File.class);
        File mockParent = mock(File.class);
        when(mockPane.getFollowedFile()).thenReturn(mockFile);
        when(mockFile.getParentFile()).thenReturn(mockParent);
        
        // Act
        tabbedPane.setSelectedComponent(mockPane);
        tabbedPane.handleSelectedFile();
        
        // Assert
        verify(mockPane).getFollowedFile();
        verify(mockFile).getParentFile();
        verify(mockAttributes).setLastFileChooserDirectory(mockParent);
    }
    
    @Test
    public void testHandleSelectedFile_WhenPaneNull_ThenNotSetLastFileChooserDirectory() {
        // Arrange
        FileFollowingPane mockPane = null;
        
        // Act
        tabbedPane.setSelectedComponent(mockPane);
        tabbedPane.handleSelectedFile();
        
        // Assert
        verify(mockAttributes, never()).setLastFileChooserDirectory(any(File.class));
    }
}
