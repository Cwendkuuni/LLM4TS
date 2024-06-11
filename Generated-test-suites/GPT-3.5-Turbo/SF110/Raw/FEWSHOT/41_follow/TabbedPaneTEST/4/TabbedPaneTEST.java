
package ghm.follow.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.Mockito.*;

import java.awt.Component;
import java.io.File;

import org.junit.Before;
import org.junit.Test;

import ghm.follow.config.FollowAppAttributes;

public class TabbedPaneTest {
    
    private TabbedPane tabbedPane;
    private FollowAppAttributes attributes;
    
    @Before
    public void setUp() {
        attributes = mock(FollowAppAttributes.class);
        when(attributes.getTabPlacement()).thenReturn(JTabbedPane.TOP);
        tabbedPane = new TabbedPane(attributes);
    }

    @Test
    public void testFindComponentAt_WithValidCoordinates_ReturnsComponent() {
        Component component = mock(Component.class);
        when(component.isVisible()).thenReturn(true);
        when(component.getX()).thenReturn(0);
        when(component.getY()).thenReturn(0);
        when(component.getComponentAt(anyInt(), anyInt())).thenReturn(component);

        tabbedPane.add(component);
        
        Component result = tabbedPane.findComponentAt(10, 10);
        
        assertEquals(component, result);
    }
    
    @Test
    public void testFindComponentAt_WithInvalidCoordinates_ReturnsNull() {
        Component component = mock(Component.class);
        when(component.isVisible()).thenReturn(true);
        when(component.getX()).thenReturn(20);
        when(component.getY()).thenReturn(20);
        when(component.getComponentAt(anyInt(), anyInt())).thenReturn(component);

        tabbedPane.add(component);
        
        Component result = tabbedPane.findComponentAt(10, 10);
        
        assertNull(result);
    }
    
    @Test
    public void testSetSelectedIndex_CallsSuperMethodAndHandleSelectedFile() {
        FileFollowingPane pane = mock(FileFollowingPane.class);
        when(tabbedPane.getSelectedComponent()).thenReturn(pane);

        tabbedPane.setSelectedIndex(1);

        verify(tabbedPane).setSelectedIndex(1);
        verify(tabbedPane).handleSelectedFile();
    }
    
    @Test
    public void testSetSelectedComponent_CallsSuperMethodAndHandleSelectedFile() {
        FileFollowingPane pane = mock(FileFollowingPane.class);

        tabbedPane.setSelectedComponent(pane);

        verify(tabbedPane).setSelectedComponent(pane);
        verify(tabbedPane).handleSelectedFile();
    }
    
    @Test
    public void testRemoveTabAt_CallsSuperMethodAndHandleSelectedFile() {
        FileFollowingPane pane = mock(FileFollowingPane.class);
        FileFollowingPane selectedPane = mock(FileFollowingPane.class);
        when(tabbedPane.getSelectedComponent()).thenReturn(selectedPane);

        tabbedPane.removeTabAt(1);

        verify(tabbedPane).removeTabAt(1);
        verify(tabbedPane).handleSelectedFile();
    }
    
    @Test
    public void testHandleSelectedFile_WithNonNullSelectedComponent_SetLastFileChooserDirectory() {
        FileFollowingPane pane = mock(FileFollowingPane.class);
        File file = mock(File.class);
        File parent = mock(File.class);
        when(pane.getFollowedFile()).thenReturn(file);
        when(file.getParentFile()).thenReturn(parent);
        when(tabbedPane.getSelectedComponent()).thenReturn(pane);

        tabbedPane.handleSelectedFile();

        verify(attributes).setLastFileChooserDirectory(parent);
    }

    @Test
    public void testHandleSelectedFile_WithNullSelectedComponent_DoesNotSetLastFileChooserDirectory() {
        when(tabbedPane.getSelectedComponent()).thenReturn(null);

        tabbedPane.handleSelectedFile();

        verify(attributes, never()).setLastFileChooserDirectory(anyObject());
    }
}
