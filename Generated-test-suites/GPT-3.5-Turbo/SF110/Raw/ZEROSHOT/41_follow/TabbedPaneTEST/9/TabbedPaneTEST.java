import org.junit.*;
import static org.junit.Assert.*;
import org.mockito.Mockito.*;

public class TabbedPaneTest {

    private FollowAppAttributes mockAttributes;
    private JTabbedPane tabbedPane;

    @Before
    public void setUp() {
        mockAttributes = mock(FollowAppAttributes.class);
        tabbedPane = new TabbedPane(mockAttributes);
    }

    @Test
    public void testFindComponentAt_WithValidCoordinates_ShouldReturnComponent() {
        // Arrange
        Component component = new Component();
        TabbedPane spyTabbedPane = spy(tabbedPane);
        doReturn(true).when(spyTabbedPane).contains(anyInt(), anyInt());
        doReturn(component).when(spyTabbedPane).getComponentAt(anyInt());

        // Act
        Component result = spyTabbedPane.findComponentAt(10, 10);

        // Assert
        assertNotNull(result);
        assertEquals(component, result);
        verify(spyTabbedPane, times(1)).contains(10, 10);
        verify(spyTabbedPane, times(1)).getComponentAt(10);
    }

    @Test
    public void testFindComponentAt_WithInvalidCoordinates_ShouldReturnNull() {
        // Arrange
        TabbedPane spyTabbedPane = spy(tabbedPane);
        doReturn(false).when(spyTabbedPane).contains(anyInt(), anyInt());

        // Act
        Component result = spyTabbedPane.findComponentAt(100, 100);

        // Assert
        assertNull(result);
        verify(spyTabbedPane, times(1)).contains(100, 100);
    }

    @Test
    public void testSetSelectedIndex_ShouldCallSuperMethod() {
        // Arrange
        int index = 1;

        // Act
        tabbedPane.setSelectedIndex(index);

        // Assert
        verify(tabbedPane, times(1)).setSelectedIndex(index);
        verify(tabbedPane, times(1)).handleSelectedFile();
    }

    @Test
    public void testSetSelectedComponent_ShouldCallSuperMethod() {
        // Arrange
        FileFollowingPane pane = mock(FileFollowingPane.class);

        // Act
        tabbedPane.setSelectedComponent(pane);

        // Assert
        verify(tabbedPane, times(1)).setSelectedComponent(pane);
        verify(tabbedPane, times(1)).handleSelectedFile();
    }

    @Test
    public void testRemoveTabAt_ShouldCallSuperMethod() {
        // Arrange
        int index = 0;

        // Act
        tabbedPane.removeTabAt(index);

        // Assert
        verify(tabbedPane, times(1)).removeTabAt(index);
        verify(tabbedPane, times(1)).handleSelectedFile();
    }

    @Test
    public void testHandleSelectedFile_WithNonNullPane_ShouldSetLastFileChooserDirectory() {
        // Arrange
        FileFollowingPane pane = mock(FileFollowingPane.class);
        File parentDirectory = mock(File.class);
        when(pane.getFollowedFile()).thenReturn(parentDirectory);

        // Act
        tabbedPane.setSelectedComponent(pane);

        // Assert
        verify(mockAttributes, times(1)).setLastFileChooserDirectory(parentDirectory);
    }

    @Test
    public void testHandleSelectedFile_WithNullPane_ShouldNotSetLastFileChooserDirectory() {
      // Arrange
      FileFollowingPane pane = null;

      // Act
      tabbedPane.setSelectedComponent(pane);

      // Assert
      verify(mockAttributes, times(0)).setLastFileChooserDirectory(any());
    }

}