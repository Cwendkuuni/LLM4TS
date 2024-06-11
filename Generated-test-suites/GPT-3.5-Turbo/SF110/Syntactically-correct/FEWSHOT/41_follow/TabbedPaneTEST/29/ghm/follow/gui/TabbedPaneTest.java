import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import org.mockito.Mockito;

public class TabbedPaneTest {

    private FollowAppAttributes attributesMock = Mockito.mock(FollowAppAttributes.class);

    @Test
    public void testTabbedPane() {
        TabbedPane tabbedPane = new TabbedPane(attributesMock);
        assertEquals(attributesMock.getTabPlacement(), tabbedPane.getTabPlacement());
        assertNull(tabbedPane.getComponentAt(0, 0));
        tabbedPane.setSelectedIndex(0);
        Mockito.verify(attributesMock, Mockito.times(1)).setLastFileChooserDirectory(null);
    }

    @Test
    public void testFindComponentAt() {
        TabbedPane tabbedPane = new TabbedPane(attributesMock);
        assertNull(tabbedPane.findComponentAt(0, 0));
    }

    @Test
    public void testSetSelectedIndex() {
        TabbedPane tabbedPane = new TabbedPane(attributesMock);
        tabbedPane.setSelectedIndex(0);
        Mockito.verify(attributesMock, Mockito.times(1)).setLastFileChooserDirectory(null);
    }

    @Test
    public void testSetSelectedComponent() {
        FileFollowingPane paneMock = Mockito.mock(FileFollowingPane.class);
        TabbedPane tabbedPane = new TabbedPane(attributesMock);
        tabbedPane.setSelectedComponent(paneMock);
        Mockito.verify(attributesMock, Mockito.times(1)).setLastFileChooserDirectory(null);
    }

    @Test
    public void testRemoveTabAt() {
        TabbedPane tabbedPane = new TabbedPane(attributesMock);
        tabbedPane.removeTabAt(0);
        Mockito.verify(attributesMock, Mockito.times(1)).setLastFileChooserDirectory(null);
    }

    @Test
    public void testHandleSelectedFile() {
        FileFollowingPane paneMock = Mockito.mock(FileFollowingPane.class);
        File fileMock = Mockito.mock(File.class);
        File parentMock = Mockito.mock(File.class);
        Mockito.when(paneMock.getFollowedFile()).thenReturn(fileMock);
        Mockito.when(fileMock.getParentFile()).thenReturn(parentMock);

        TabbedPane tabbedPane = new TabbedPane(attributesMock);
        tabbedPane.setSelectedComponent(paneMock);
        Mockito.verify(attributesMock, Mockito.times(1)).setLastFileChooserDirectory(parentMock);
    }
}