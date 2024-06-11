import org.junit.Test;
import org.mockito.Mockito;

public class TabbedPaneTest {

    private FollowAppAttributes mockAttributes = Mockito.mock(FollowAppAttributes.class);
    private TabbedPane tabbedPane = new TabbedPane(mockAttributes);

    @Test
    public void testFindComponentAt_WhenXAndYAreOutsideOfComponent_ReturnsNull() {
        Component result = tabbedPane.findComponentAt(100, 100);
        assertNull(result);
    }

    @Test
    public void testFindComponentAt_WhenXAndYAreInsideOfComponent_ReturnsComponent() {
        // Create and add a mock component to the tabbedPane
        Component mockComponent = Mockito.mock(Component.class);
        Mockito.when(mockComponent.isVisible()).thenReturn(true);
        Mockito.when(mockComponent.getX()).thenReturn(10);
        Mockito.when(mockComponent.getY()).thenReturn(10);
        Mockito.when(mockComponent.getComponentAt(5, 5)).thenReturn(mockComponent);
        tabbedPane.add(mockComponent);

        Component result = tabbedPane.findComponentAt(15, 15);
        assertEquals(mockComponent, result);
    }

    @Test
    public void testFindComponentAt_WhenXAndYAreInsideOfNestedContainer_ReturnsComponent() {
        // Create and add a mock container to the tabbedPane with a child component
        Container mockContainer = Mockito.mock(Container.class);
        Component mockComponent = Mockito.mock(Component.class);
        Mockito.when(mockContainer.isVisible()).thenReturn(true);
        Mockito.when(mockContainer.getX()).thenReturn(10);
        Mockito.when(mockContainer.getY()).thenReturn(10);
        Mockito.when(mockContainer.findComponentAt(5, 5)).thenReturn(mockComponent);
        Mockito.when(mockComponent.isVisible()).thenReturn(true);
        Mockito.when(mockComponent.getX()).thenReturn(5);
        Mockito.when(mockComponent.getY()).thenReturn(5);
        Mockito.when(mockComponent.getComponentAt(0, 0)).thenReturn(mockComponent);
        tabbedPane.add(mockContainer);

        Component result = tabbedPane.findComponentAt(15, 15);
        assertEquals(mockComponent, result);
    }

    @Test
    public void testSetSelectedIndex_InvokesSuperClassMethodAndHandleSelectedFile() {
        FileFollowingPane mockPane = Mockito.mock(FileFollowingPane.class);
        tabbedPane.add(mockPane);

        tabbedPane.setSelectedIndex(0);

        Mockito.verify(mockPane, Mockito.times(1)).getFollowedFile();
        Mockito.verify(mockAttributes, Mockito.times(1)).setLastFileChooserDirectory(Mockito.any(File.class));
    }

    @Test
    public void testSetSelectedComponent_InvokesSuperClassMethodAndHandleSelectedFile() {
        FileFollowingPane mockPane = Mockito.mock(FileFollowingPane.class);
        tabbedPane.add(mockPane);

        tabbedPane.setSelectedComponent(mockPane);

        Mockito.verify(mockPane, Mockito.times(1)).getFollowedFile();
        Mockito.verify(mockAttributes, Mockito.times(1)).setLastFileChooserDirectory(Mockito.any(File.class));
    }

    @Test
    public void testRemoveTabAt_InvokesSuperClassMethodAndHandleSelectedFile() {
        FileFollowingPane mockPane = Mockito.mock(FileFollowingPane.class);
        tabbedPane.add(mockPane);

        tabbedPane.removeTabAt(0);

        Mockito.verify(mockPane, Mockito.times(1)).getFollowedFile();
        Mockito.verify(mockAttributes, Mockito.times(1)).setLastFileChooserDirectory(Mockito.any(File.class));
    }
}