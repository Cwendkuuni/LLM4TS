
package ghm.follow.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.Component;
import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ghm.follow.config.FollowAppAttributes;


public class TabbedPaneTest {

    @Mock
    private FollowAppAttributes mockAttributes;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testFindComponentAt_NotContained() {
        TabbedPane tabbedPane = new TabbedPane(mockAttributes);
        
        assertEquals(null, tabbedPane.findComponentAt(0, 0));
    }
    
    @Test
    public void testFindComponentAt_ContainerComp_ContainedVisible() {
        TabbedPane tabbedPane = new TabbedPane(mockAttributes);
        
        Component mockContainer = mock(Component.class);
        when(mockContainer.isVisible()).thenReturn(true);
        when(mockContainer.getX()).thenReturn(0);
        when(mockContainer.getY()).thenReturn(0);
        when(mockContainer.findComponentAt(0, 0)).thenReturn(mockContainer);
        
        tabbedPane.add(mockContainer);
        
        assertEquals(mockContainer, tabbedPane.findComponentAt(0, 0));
    }
    
    @Test
    public void testFindComponentAt_LeafComp_ContainedVisible() {
        TabbedPane tabbedPane = new TabbedPane(mockAttributes);
        
        Component mockLeaf = mock(Component.class);
        when(mockLeaf.getX()).thenReturn(0);
        when(mockLeaf.getY()).thenReturn(0);
        when(mockLeaf.getComponentAt(0, 0)).thenReturn(mockLeaf);
        
        tabbedPane.add(mockLeaf);
        
        assertEquals(mockLeaf, tabbedPane.findComponentAt(0, 0));
    }
    
    @Test
    public void testFindComponentAt_ContainedInvisible() {
        TabbedPane tabbedPane = new TabbedPane(mockAttributes);
        
        Component mockContainer = mock(Component.class);
        when(mockContainer.isVisible()).thenReturn(false);
        when(mockContainer.getX()).thenReturn(0);
        when(mockContainer.getY()).thenReturn(0);
        when(mockContainer.findComponentAt(0, 0)).thenReturn(mockContainer);
        
        tabbedPane.add(mockContainer);
        
        assertEquals(null, tabbedPane.findComponentAt(0, 0));
    }
    
    @Test
    public void testFindComponentAt_ContainerComp_ContainedNotVisible() {
        TabbedPane tabbedPane = new TabbedPane(mockAttributes);
        
        Component mockContainer = mock(Component.class);
        when(mockContainer.isVisible()).thenReturn(false);
        when(mockContainer.getX()).thenReturn(0);
        when(mockContainer.getY()).thenReturn(0);
        when(mockContainer.findComponentAt(0, 0)).thenReturn(mockContainer);
        
        tabbedPane.add(mockContainer);
        
        assertEquals(null, tabbedPane.findComponentAt(0, 0));
    }
    
    @Test
    public void testSetSelectedIndex() {
        TabbedPane tabbedPane = new TabbedPane(mockAttributes);
        
        tabbedPane.setSelectedIndex(0);
        
        // Assert implementation-specific behavior, if any
        
    }
    
    @Test
    public void testSetSelectedComponent() {
        TabbedPane tabbedPane = new TabbedPane(mockAttributes);
        
        FileFollowingPane mockPane = mock(FileFollowingPane.class);
        
        tabbedPane.setSelectedComponent(mockPane);
        
        // Assert implementation-specific behavior, if any
        
    }
    
    @Test
    public void testRemoveTabAt() {
        TabbedPane tabbedPane = new TabbedPane(mockAttributes);
        
        tabbedPane.removeTabAt(0);
        
        // Assert implementation-specific behavior, if any
        
    }
    
    @Test
    public void testHandleSelectedFile() {
        TabbedPane tabbedPane = new TabbedPane(mockAttributes);
        
        FileFollowingPane mockPane = mock(FileFollowingPane.class);
        File mockFile = mock(File.class);
        when(mockPane.getFollowedFile()).thenReturn(mockFile);
        when(mockFile.getParentFile()).thenReturn(mockFile);
        
        tabbedPane.setSelectedComponent(mockPane);
        
        // Assert implementation-specific behavior, if any
        
    }

}
