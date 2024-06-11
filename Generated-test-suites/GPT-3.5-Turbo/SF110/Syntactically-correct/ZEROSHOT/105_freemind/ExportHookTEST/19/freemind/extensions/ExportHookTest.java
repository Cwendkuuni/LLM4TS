import org.junit.*;
import static org.junit.Assert.*;
import org.mockito.*;

public class ExportHookTest {
    private ExportHook exportHook;
    private ModeController mockController;
    private MapView mockView;
    
    @Before
    public void setUp() {
        exportHook = new ExportHook();
        mockController = Mockito.mock(ModeController.class);
        mockView = Mockito.mock(MapView.class);
        Mockito.when(mockController.getView()).thenReturn(mockView);
        exportHook.setController(mockController);
    }
    
    @Test
    public void testChooseImageFile() {
        // test case 1: mmFile is null
        File result1 = ExportHook.chooseImageFile("png", "PNG file", null, mockController);
        assertNull(result1);
        
        // test case 2: mmFile is not null
        File mmFile = Mockito.mock(File.class);
        Mockito.when(mmFile.getAbsolutePath()).thenReturn("C:\\path\\to\\file.mm");
        Mockito.when(controller.getMap().getFile()).thenReturn(mmFile);
        File result2 = ExportHook.chooseImageFile("jpg", "JPG file", null, mockController);
        assertNotNull(result2);
        assertEquals("C:\\path\\to\\file.jpg", result2.getAbsolutePath());
        
        // test case 3: chosenFile extension is different from type
        File chosenFile = Mockito.mock(File.class);
        Mockito.when(chosenFile.exists()).thenReturn(true);
        Mockito.when(chosenFile.getParent()).thenReturn("C:\\path\\to\\folder");
        Mockito.when(chosenFile.getName()).thenReturn("image.bmp");
        Mockito.when(controller.getText("file_already_exists")).thenReturn("The file already exists");
        Mockito.when(JOptionPane.showConfirmDialog(component, "The file already exists", "The file already exists", 0)).thenReturn(0);
        File result3 = ExportHook.chooseImageFile("png", "PNG file", null, mockController);
        assertNotNull(result3);
        assertEquals("C:\\path\\to\\folder\\image.png", result3.getAbsolutePath());
        
        // test case 4: chosenFile extension is same as type
        File chosenFile2 = Mockito.mock(File.class);
        Mockito.when(chosenFile2.exists()).thenReturn(true);
        Mockito.when(chosenFile2.getParent()).thenReturn("C:\\path\\to\\folder");
        Mockito.when(chosenFile2.getName()).thenReturn("image.jpg");
        File result4 = ExportHook.chooseImageFile("jpg", "JPG file", null, mockController);
        assertNotNull(result4);
        assertEquals("C:\\path\\to\\folder\\image.jpg", result4.getAbsolutePath());
    }
    
    @Test
    public void testGetTranslatableResourceString() {
        // test case 1: returnValue starts with "%"
        Mockito.when(exportHook.getResourceString("resourceName")).thenReturn("%translatableString");
        Mockito.when(mockController.getText("translatableString")).thenReturn("Translated String");
        String result1 = exportHook.getTranslatableResourceString("resourceName");
        assertEquals("Translated String", result1);
        
        // test case 2: returnValue does not start with "%"
        Mockito.when(exportHook.getResourceString("resourceName")).thenReturn("nonTranslatableString");
        String result2 = exportHook.getTranslatableResourceString("resourceName");
        assertEquals("nonTranslatableString", result2);
    }
    
    @Test
    public void testCreateBufferedImage() {
        // test case 1: view is null
        BufferedImage result1 = exportHook.createBufferedImage();
        assertNull(result1);
        
        // test case 2: view is not null
        Mockito.when(mockView.getWidth()).thenReturn(800);
        Mockito.when(mockView.getHeight()).thenReturn(600);
        Rectangle innerBoundsMock = Mockito.mock(Rectangle.class);
        BufferedImage bufferedImageMock = Mockito.mock(BufferedImage.class);
        Graphics graphicsMock = Mockito.mock(Graphics.class);
        Mockito.when(mockView.getInnerBounds()).thenReturn(innerBoundsMock);
        Mockito.when(mockView.createImage(800, 600)).thenReturn(bufferedImageMock);
        Mockito.when(bufferedImageMock.getGraphics()).thenReturn(graphicsMock);
        BufferedImage result2 = exportHook.createBufferedImage();
        assertNotNull(result2);
        // additional assertions based on the implementation of createBufferedImage method
    }
    
    @Test
    public void testCopyFromResource() {
        // additional configurations and assertions can be added
        // based on the implementation of copyFromResource method
    }
    
    @Test
    public void testCopyFromFile() {
        // additional configurations and assertions can be added
        // based on the implementation of copyFromFile method
    }
}