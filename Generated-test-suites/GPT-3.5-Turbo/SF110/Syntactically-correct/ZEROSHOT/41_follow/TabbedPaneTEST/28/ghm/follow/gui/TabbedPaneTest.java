import org.junit.Test;

import javax.swing.*;
import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class TabbedPaneTest {

    @Test
    public void testFindComponentAt_WhenComponentIsNull_ReturnNull() {
        Component comp = mock(Component.class);
        when(comp.isVisible()).thenReturn(true);
        when(comp.getComponentAt(anyInt(), anyInt())).thenReturn(comp);

        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        tabbedPane.add(comp);
        Component result = tabbedPane.findComponentAt(0, 0);

        assertEquals(comp, result);
    }

    @Test
    public void testFindComponentAt_WhenComponentIsContainerAndVisible_ReturnNotNullComponent() {
        Container container = mock(Container.class);
        Component comp = mock(Component.class);
        when(container.isVisible()).thenReturn(true);
        when(container.findComponentAt(anyInt(), anyInt())).thenReturn(comp);

        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        tabbedPane.add(container);
        Component result = tabbedPane.findComponentAt(0, 0);

        assertEquals(comp, result);
    }

    @Test
    public void testFindComponentAt_WhenComponentIsContainerButNotVisible_ReturnNull() {
        Container container = mock(Container.class);
        when(container.isVisible()).thenReturn(false);

        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        tabbedPane.add(container);
        Component result = tabbedPane.findComponentAt(0, 0);

        assertEquals(null, result);
    }

    @Test
    public void testFindComponentAt_WhenComponentIsNotContainerButHasVisibleComponent_ReturnNotNullComponent() {
        JPanel panel = mock(JPanel.class);
        Container container = mock(Container.class);
        Component comp = mock(Component.class);
        when(comp.isVisible()).thenReturn(true);
        when(comp.getComponentAt(anyInt(), anyInt())).thenReturn(comp);
        when(panel.getComponentAt(anyInt(), anyInt())).thenReturn(comp);

        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        tabbedPane.add(panel);
        Component result = tabbedPane.findComponentAt(0, 0);

        assertEquals(comp, result);
    }

    @Test
    public void testFindComponentAt_WhenComponentIsNotContainerAndDoesNotHaveVisibleComponent_ReturnNotNullComponent() {
        JPanel panel = mock(JPanel.class);
        Container container = mock(Container.class);
        Component comp = mock(Component.class);
        when(comp.isVisible()).thenReturn(false);
        when(comp.getComponentAt(anyInt(), anyInt())).thenReturn(null);
        when(panel.getComponentAt(anyInt(), anyInt())).thenReturn(comp);

        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        tabbedPane.add(panel);
        Component result = tabbedPane.findComponentAt(0, 0);

        assertEquals(tabbedPane, result);
    }

    @Test
    public void testSetSelectedIndex_CallsSuperMethodAndHandleSelectedFile() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        tabbedPane.handleSelectedFile = mock(Runnable.class);

        tabbedPane.setSelectedIndex(0);

        verify(tabbedPane).super.setSelectedIndex(0);
        verify(tabbedPane.handleSelectedFile).run();
    }

    @Test
    public void testSetSelectedComponent_CallsSuperMethodAndHandleSelectedFile() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        FileFollowingPane pane = mock(FileFollowingPane.class);
        tabbedPane.handleSelectedFile = mock(Runnable.class);

        tabbedPane.setSelectedComponent(pane);

        verify(tabbedPane).super.setSelectedComponent(pane);
        verify(tabbedPane.handleSelectedFile).run();
    }

    @Test
    public void testRemoveTabAt_CallsSuperMethodAndHandleSelectedFile() {
        TabbedPane tabbedPane = new TabbedPane(new FollowAppAttributes());
        tabbedPane.handleSelectedFile = mock(Runnable.class);
        
        tabbedPane.removeTabAt(0);

        verify(tabbedPane).super.removeTabAt(0);
        verify(tabbedPane.handleSelectedFile).run();
    }

    private static class FollowAppAttributes extends ghm.follow.config.FollowAppAttributes {
        public FollowAppAttributes() {
            super();
        }

        @Override
        public int getTabPlacement() {
            return JTabbedPane.TOP;
        }
    }
}