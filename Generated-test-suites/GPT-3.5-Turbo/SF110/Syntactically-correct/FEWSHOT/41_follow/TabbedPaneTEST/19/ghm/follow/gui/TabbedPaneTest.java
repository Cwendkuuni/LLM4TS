package ghm.follow.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import javax.swing.JTabbedPane;

import ghm.follow.config.FollowAppAttributes;

import org.junit.Before;
import org.junit.Test;

public class TabbedPaneTest {
    
    private TabbedPane tabbedPane;
    private FollowAppAttributes attributesMock;
    
    @Before
    public void setUp() {
        attributesMock = mock(FollowAppAttributes.class);
        tabbedPane = new TabbedPane(attributesMock);
    }
    
    @Test
    public void testConstructor() {
        assertEquals(attributesMock.getTabPlacement(), tabbedPane.getTabPlacement());
        verify(attributesMock, times(1)).getTabPlacement();
    }
    
    @Test
    public void testFindComponentAt_WithValidCoordinates_ShouldReturnValidComponent() {
        assertNull(tabbedPane.findComponentAt(-1, -1));
        assertNull(tabbedPane.findComponentAt(1000, 1000));
        
        // Additional test cases can be added to test different coordinates
    }
    
    @Test
    public void testSetSelectedIndex() {
        int index = 0;
        tabbedPane.setSelectedIndex(index);
        verify(attributesMock, times(1)).setLastFileChooserDirectory(any());
        assertEquals(index, tabbedPane.getSelectedIndex());
    }
    
    @Test
    public void testSetSelectedComponent() {
        FileFollowingPane paneMock = mock(FileFollowingPane.class);
        tabbedPane.setSelectedComponent(paneMock);
        verify(attributesMock, times(1)).setLastFileChooserDirectory(any());
        assertEquals(paneMock, tabbedPane.getSelectedComponent());
    }
    
    @Test
    public void testRemoveTabAt() {
        int index = 0;
        tabbedPane.removeTabAt(index);
        verify(attributesMock, times(1)).setLastFileChooserDirectory(any());
        // Additional test cases can be added to test different indexes
    }
}