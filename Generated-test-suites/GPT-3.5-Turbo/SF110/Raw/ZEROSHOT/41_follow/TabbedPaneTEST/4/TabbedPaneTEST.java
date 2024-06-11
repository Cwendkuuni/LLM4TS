import org.junit.*;
import static org.junit.Assert.*;
import org.mockito.*;

public class TabbedPaneTest {

    @Mock
    private FollowAppAttributes mockAttributes;

    private TabbedPane tabbedPane;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        tabbedPane = new TabbedPane(mockAttributes);
    }

    @Test
    public void testFindComponentAt_notContains() {
        Component result = tabbedPane.findComponentAt(100, 100);
        assertNull(result);
    }

    @Test
    public void testFindComponentAt_contains() {
        Component mockComponent = Mockito.mock(Component.class);

        Mockito.when(mockComponent.getX()).thenReturn(50);
        Mockito.when(mockComponent.getY()).thenReturn(50);
        Mockito.when(mockComponent.isVisible()).thenReturn(true);

        Mockito.when(tabbedPane.getComponentCount()).thenReturn(1);
        Mockito.when(tabbedPane.getComponentAt(0)).thenReturn(mockComponent);

        Component result = tabbedPane.findComponentAt(100, 100);

        assertNotNull(result);
        Mockito.verify(mockComponent, Mockito.times(1)).getX();
        Mockito.verify(mockComponent, Mockito.times(1)).getY();
        Mockito.verify(mockComponent, Mockito.times(1)).isVisible();
        Mockito.verify(mockComponent, Mockito.times(1)).getComponentAt(Mockito.anyInt(), Mockito.anyInt());
    }

    @Test
    public void testSetSelectedIndex() {
        int index = 1;
        tabbedPane.setSelectedIndex(index);
        // Assert any additional behavior or changes here
    }

    @Test
    public void testSetSelectedComponent() {
        FileFollowingPane mockPane = Mockito.mock(FileFollowingPane.class);
        tabbedPane.setSelectedComponent(mockPane);
        // Assert any additional behavior or changes here
    }

    @Test
    public void testRemoveTabAt() {
        int index = 1;
        tabbedPane.removeTabAt(index);
        // Assert any additional behavior or changes here
    }

    @Test
    public void testHandleSelectedFile_notNull() {
        FileFollowingPane mockPane = Mockito.mock(FileFollowingPane.class);
        File mockFile = Mockito.mock(File.class);

        Mockito.when(mockPane.getFollowedFile()).thenReturn(mockFile);
        Mockito.when(mockFile.getParentFile()).thenReturn(mockFile);

        tabbedPane.setSelectedComponent(mockPane);

        Mockito.verify(mockPane, Mockito.times(1)).getFollowedFile();
        Mockito.verify(mockFile, Mockito.times(1)).getParentFile();
        Mockito.verify(mockAttributes, Mockito.times(1)).setLastFileChooserDirectory(Mockito.eq(mockFile));
    }

    @Test
    public void testHandleSelectedFile_null() {
        tabbedPane.setSelectedComponent(null);
        // Assert any additional behavior or changes here
    }

    @After
    public void tearDown() {
        mockAttributes = null;
        tabbedPane = null;
    }
}