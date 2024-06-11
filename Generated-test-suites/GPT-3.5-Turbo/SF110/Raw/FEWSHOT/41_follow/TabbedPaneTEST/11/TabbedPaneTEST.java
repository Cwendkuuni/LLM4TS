package ghm.follow.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Test;

public class TabbedPaneTest {
    
    @Test
    public void testFindComponentAt_returnNullWhenNotContains() {
        TabbedPane tabbedPane = new TabbedPane(null);
        
        assertNull(tabbedPane.findComponentAt(10, 10));
    }
    
    @Test
    public void testFindComponentAt_returnNullWhenNotVisible() {
        TabbedPane tabbedPane = new TabbedPane(null);
        JPanel panel1 = mock(JPanel.class);
        JPanel panel2 = mock(JPanel.class);
        
        when(panel1.isVisible()).thenReturn(false);
        when(panel2.isVisible()).thenReturn(false);
        
        tabbedPane.add(panel1);
        tabbedPane.add(panel2);
        
        assertNull(tabbedPane.findComponentAt(10, 10));
    }
    
    @Test
    public void testFindComponentAt_returnNullWhenComponentIsNull() {
        TabbedPane tabbedPane = new TabbedPane(null);
        JPanel panel1 = mock(JPanel.class);
        JPanel panel2 = null;
        
        when(panel1.isVisible()).thenReturn(true);
        
        tabbedPane.add(panel1);
        tabbedPane.add(panel2);
        
        assertNull(tabbedPane.findComponentAt(10, 10));
    }
    
    @Test
    public void testFindComponentAt_returnComponentWhenVisible() {
        TabbedPane tabbedPane = new TabbedPane(null);
        JPanel panel1 = mock(JPanel.class);
        JPanel panel2 = mock(JPanel.class);
        JPanel panel3 = mock(JPanel.class);
        
        when(panel1.isVisible()).thenReturn(true);
        when(panel2.isVisible()).thenReturn(true);
        when(panel3.isVisible()).thenReturn(true);
        
        when(panel1.getX()).thenReturn(0);
        when(panel1.getY()).thenReturn(0);
        when(panel2.getX()).thenReturn(100);
        when(panel2.getY()).thenReturn(100);
        when(panel3.getX()).thenReturn(200);
        when(panel3.getY()).thenReturn(200);
        
        when(panel1.getComponentAt(10, 10)).thenReturn(null);
        when(panel2.getComponentAt(90, 90)).thenReturn(panel3);
        when(panel3.getComponentAt(90, 90)).thenReturn(panel3);
        
        tabbedPane.add(panel1);
        tabbedPane.add(panel2);
        
        assertEquals(panel3, tabbedPane.findComponentAt(200, 200));
    }
    
    @Test
    public void testSetSelectedIndex_callsHandleSelectedFile() {
        TabbedPane tabbedPane = new TabbedPane(null);
        tabbedPane.setSelectedIndex(0);
        FollowAppAttributes attributes = mock(FollowAppAttributes.class);
        
        tabbedPane.setSelectedIndex(1);
        
        verify(attributes, times(1)).setLastFileChooserDirectory(any());
    }
    
    @Test
    public void testSetSelectedComponent_callsHandleSelectedFile() {
        TabbedPane tabbedPane = new TabbedPane(null);
        tabbedPane.setSelectedComponent(mock(FileFollowingPane.class));
        FollowAppAttributes attributes = mock(FollowAppAttributes.class);
        
        tabbedPane.setSelectedComponent(mock(FileFollowingPane.class));
        
        verify(attributes, times(1)).setLastFileChooserDirectory(any());
    }
    
    @Test
    public void testRemoveTabAt_callsHandleSelectedFile() {
        TabbedPane tabbedPane = new TabbedPane(null);
        tabbedPane.addTab("Tab 1", mock(Component.class));
        tabbedPane.addTab("Tab 2", mock(Component.class));
        FollowAppAttributes attributes = mock(FollowAppAttributes.class);
        
        tabbedPane.removeTabAt(1);
        
        verify(attributes, times(1)).setLastFileChooserDirectory(any());
    }

}