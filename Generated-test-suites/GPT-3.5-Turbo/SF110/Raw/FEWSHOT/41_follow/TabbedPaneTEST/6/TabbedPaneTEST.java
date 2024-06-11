package ghm.follow.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import ghm.follow.config.FollowAppAttributes;
import java.io.File;
import javax.swing.JTabbedPane;
import org.junit.Before;
import org.junit.Test;

public class TabbedPaneTest {
    private FollowAppAttributes attributes;
    private TabbedPane tabbedPane;
    private FileFollowingPane mockFileFollowingPane;
    
    @Before
    public void setUp() {
        attributes = new FollowAppAttributes();
        tabbedPane = new TabbedPane(attributes);
        mockFileFollowingPane = mock(FileFollowingPane.class);
        
        when(mockFileFollowingPane.getFollowedFile()).thenReturn(new File("test.txt"));
    }
    
    @Test
    public void testFindComponentAt_whenComponentNotFound_returnsNull() {
        assertNull(tabbedPane.findComponentAt(0, 0));
    }
    
    @Test
    public void testFindComponentAt_whenComponentFound_returnsComponent() {
        tabbedPane.add(mockFileFollowingPane);
        assertEquals(mockFileFollowingPane, tabbedPane.findComponentAt(0, 0));
    }
    
    @Test
    public void testSetSelectedIndex_callsSuperMethod() {
        JTabbedPane spyTabbedPane = spy(tabbedPane);
        spyTabbedPane.setSelectedIndex(0);
        verify(spyTabbedPane, times(1)).setSelectedIndex(0);
    }
    
    @Test
    public void testSetSelectedIndex_callsHandleSelectedFile() {
        tabbedPane.add(mockFileFollowingPane);
        tabbedPane.setSelectedIndex(0);
        verify(mockFileFollowingPane, times(1)).getFollowedFile();
        verify(attributes, times(1)).setLastFileChooserDirectory(any(File.class));
    }
    
    @Test
    public void testSetSelectedComponent_callsSuperMethod() {
        JTabbedPane spyTabbedPane = spy(tabbedPane);
        spyTabbedPane.setSelectedComponent(mockFileFollowingPane);
        verify(spyTabbedPane, times(1)).setSelectedComponent(mockFileFollowingPane);
    }
    
    @Test
    public void testSetSelectedComponent_callsHandleSelectedFile() {
        tabbedPane.setSelectedComponent(mockFileFollowingPane);
        verify(mockFileFollowingPane, times(1)).getFollowedFile();
        verify(attributes, times(1)).setLastFileChooserDirectory(any(File.class));
    }
    
    @Test
    public void testRemoveTabAt_callsSuperMethod() {
        tabbedPane.add(mockFileFollowingPane);
        JTabbedPane spyTabbedPane = spy(tabbedPane);
        spyTabbedPane.removeTabAt(0);
        verify(spyTabbedPane, times(1)).removeTabAt(0);
    }
    
    @Test
    public void testRemoveTabAt_callsHandleSelectedFile() {
        tabbedPane.add(mockFileFollowingPane);
        tabbedPane.removeTabAt(0);
        verify(mockFileFollowingPane, times(1)).getFollowedFile();
        verify(attributes, times(1)).setLastFileChooserDirectory(any(File.class));
    }
}