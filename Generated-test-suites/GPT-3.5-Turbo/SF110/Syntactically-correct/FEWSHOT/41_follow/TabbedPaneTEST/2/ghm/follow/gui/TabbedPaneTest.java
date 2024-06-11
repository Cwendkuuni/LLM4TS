import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import ghm.follow.config.FollowAppAttributes;
import java.awt.Component;
import java.io.File;


public class TabbedPaneTest {
    
    private FollowAppAttributes attributes;
    private TabbedPane tabbedPane;
    
    @Before
    public void setup() {
        attributes = mock(FollowAppAttributes.class);
        tabbedPane = new TabbedPane(attributes);
    }
    
    @Test
    public void testFindComponentAt() {
        Component component = tabbedPane.findComponentAt(10, 10);
        assertNull(component);
        
        component = tabbedPane.findComponentAt(0, 0);
        assertNotNull(component);
    }
    
    @Test
    public void testSetSelectedIndex() {
        tabbedPane.setSelectedIndex(0);
        verify(attributes, times(1)).setLastFileChooserDirectory(any(File.class));
    }
    
    @Test
    public void testSetSelectedComponent() {
        FileFollowingPane pane = mock(FileFollowingPane.class);
        tabbedPane.setSelectedComponent(pane);
        verify(attributes, times(1)).setLastFileChooserDirectory(any(File.class));
    }
    
    @Test
    public void testRemoveTabAt() {
        tabbedPane.removeTabAt(0);
        verify(attributes, times(1)).setLastFileChooserDirectory(any(File.class));
    }
    
    @Test
    public void testHandleSelectedFile() {
        FileFollowingPane pane = mock(FileFollowingPane.class);
        File file = mock(File.class);
        when(pane.getFollowedFile()).thenReturn(file);
        when(file.getParentFile()).thenReturn(file);
        
        tabbedPane.setSelectedComponent(pane);
        verify(attributes, times(1)).setLastFileChooserDirectory(file);
    }
}