package ghm.follow.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.awt.Component;
import java.io.File;

import org.junit.Before;
import org.junit.Test;

import ghm.follow.config.FollowAppAttributes;

public class TabbedPaneTest {

    private FollowAppAttributes attributes;
    private TabbedPane tabbedPane;

    @Before
    public void setUp() {
        attributes = mock(FollowAppAttributes.class);
        tabbedPane = new TabbedPane(attributes);
    }

    @Test
    public void testFindComponentAt_shouldReturnNull() {
        assertEquals(null, tabbedPane.findComponentAt(0, 0)); // when x and y are outside the pane
    }

    @Test
    public void testFindComponentAt_shouldReturnComponent() {
        Component comp = mock(Component.class);
        when(comp.isVisible()).thenReturn(true);
        when(comp.getX()).thenReturn(10);
        when(comp.getY()).thenReturn(10);
        when(comp.getComponentAt(anyInt(), anyInt())).thenReturn(comp);

        tabbedPane.add(comp);
        assertEquals(comp, tabbedPane.findComponentAt(20, 20));
    }

    @Test
    public void testSetSelectedIndex() {
        tabbedPane.setSelectedIndex(1);
        verify(attributes).setLastFileChooserDirectory(any(File.class));
    }

    @Test
    public void testSetSelectedComponent() {
        FileFollowingPane pane = mock(FileFollowingPane.class);
        when(pane.getFollowedFile()).thenReturn(new File("test.txt"));

        tabbedPane.setSelectedComponent(pane);
        verify(attributes).setLastFileChooserDirectory(any(File.class));
    }

    @Test
    public void testRemoveTabAt() {
        tabbedPane.removeTabAt(1);
        verify(attributes).setLastFileChooserDirectory(any(File.class));
    }
    
}