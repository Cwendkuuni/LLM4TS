import org.junit.*;
import static org.junit.Assert.*;
import org.mockito.*;

public class ExportHookTest {
    
    private ExportHook hook;
    
    @Before
    public void setUp() {
        hook = new ExportHook();
    }
    
    @Test
    public void testChooseFile() {
        // Test case for chooseFile method
        String type = "jpg";
        String description = "JPEG image";
        String nameExtension = "_export";
        
        // Set up mocked objects
        ModeController controller = Mockito.mock(ModeController.class);
        
        FileChooser fileChooser = Mockito.mock(FileChooser.class);
        Mockito.when(controller.getFileChooser(Mockito.any(ImageFilter.class))).thenReturn(fileChooser);
        
        File mmFile = Mockito.mock(File.class);
        Mockito.when(controller.getMap().getFile()).thenReturn(mmFile);
        
        Container component = Mockito.mock(Container.class);
        JFrame frame = Mockito.mock(JFrame.class);
        Mockito.when(controller.getFrame()).thenReturn(frame);
        Mockito.when(frame.getContentPane()).thenReturn(component);
        
        File selectedFile = Mockito.mock(File.class);
        Mockito.when(fileChooser.getSelectedFile()).thenReturn(selectedFile);
        
        // Call the method to be tested
        File result = hook.chooseFile(type, description, nameExtension);
        
        // Verify the interactions
        Mockito.verify(controller).getFileChooser(Mockito.any(ImageFilter.class));
        Mockito.verify(controller.getMap()).getFile();
        Mockito.verify(fileChooser).setSelectedFile(Mockito.any(File.class));
        Mockito.verify(fileChooser).showSaveDialog(component);
        
        // Verify the result
        assertEquals(selectedFile, result);
    }
    
    @Test
    public void testChooseImageFile() {
        // Test case for chooseImageFile method
        String type = "jpg";
        String description = "JPEG image";
        String nameExtension = "_export";
        
        // Set up mocked objects
        Container component = Mockito.mock(Container.class);
        ModeController controller = Mockito.mock(ModeController.class);
        JFrame frame = Mockito.mock(JFrame.class);
        Mockito.when(controller.getFrame()).thenReturn(frame);
        Mockito.when(frame.getContentPane()).thenReturn(component);
        
        FileChooser fileChooser = Mockito.mock(FileChooser.class);
        Mockito.when(controller.getFileChooser(Mockito.any(ImageFilter.class))).thenReturn(fileChooser);
        
        File mmFile = Mockito.mock(File.class);
        Mockito.when(controller.getMap().getFile()).thenReturn(mmFile);
        
        File selectedFile = Mockito.mock(File.class);
        Mockito.when(fileChooser.getSelectedFile()).thenReturn(selectedFile);
        
        // Call the method to be tested
        File result = ExportHook.chooseImageFile(type, description, nameExtension, controller);
        
        // Verify the interactions
        Mockito.verify(controller).getFileChooser(Mockito.any(ImageFilter.class));
        Mockito.verify(controller.getMap()).getFile();
        Mockito.verify(fileChooser).setSelectedFile(Mockito.any(File.class));
        Mockito.verify(fileChooser).showSaveDialog(component);
        
        // Verify the result
        assertEquals(selectedFile, result);
    }
    
    @Test
    public void testGetTranslatableResourceString() {
        // Test case for getTranslatableResourceString method
        String resourceName = "example_resource";
        
        // Set up mocked objects
        ModeController controller = Mockito.mock(ModeController.class);
        
        // Call the method to be tested
        String result = hook.getTranslatableResourceString(resourceName);
        
        // Verify the interactions
        Mockito.verify(controller).getText(Mockito.anyString());
        
        // Verify the result
        assertNotNull(result);
    }
    
    @Test
    public void testCreateBufferedImage() {
        // Test case for createBufferedImage method
        
        // Set up mocked objects
        ModeController controller = Mockito.mock(ModeController.class);
        MapView view = Mockito.mock(MapView.class);
        Mockito.when(controller.getView()).thenReturn(view);
        
        BufferedImage image = Mockito.mock(BufferedImage.class);
        Mockito.when(view.getWidth()).thenReturn(100);
        Mockito.when(view.getHeight()).thenReturn(100);
        Mockito.when(view.createImage(Mockito.anyInt(), Mockito.anyInt())).thenReturn(image);
        
        Graphics graphics = Mockito.mock(Graphics.class);
        Mockito.when(image.getGraphics()).thenReturn(graphics);
        
        Rectangle innerBounds = Mockito.mock(Rectangle.class);
        Mockito.when(view.getInnerBounds()).thenReturn(innerBounds);
        
        // Call the method to be tested
        BufferedImage result = hook.createBufferedImage();
        
        // Verify the interactions
        Mockito.verify(controller).getView();
        Mockito.verify(view).preparePrinting();
        Mockito.verify(view).print(graphics);
        Mockito.verify(view).endPrinting();
        
        // Verify the result
        assertNotNull(result);
    }
    
    @Test
    public void testCopyFromResource() {
        // Test case for copyFromResource method
        String prefix = "resources/";
        String fileName = "example.txt";
        String destinationDirectory = "destination";
        
        // Set up mocked objects
        Logger logger = Mockito.mock(Logger.class);
        Mockito.when(hook.logger.finest(Mockito.anyString())).thenReturn(logger);
        Mockito.when(hook.logger.severe(Mockito.anyString())).thenReturn(logger);
        
        URL resource = this.getClass().getResource("/" + prefix + fileName);
        Mockito.when(hook.getResource(prefix + fileName)).thenReturn(resource);
        
        InputStream in = Mockito.mock(InputStream.class);
        OutputStream out = Mockito.mock(OutputStream.class);
        
        try {
            Mockito.when(resource.openStream()).thenReturn(in);
            Mockito.when(hook.getResourceString(Mockito.anyString())).thenReturn("test string");
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // Call the method to be tested
        hook.copyFromResource(prefix, fileName, destinationDirectory);
        
        // Verify the interactions
        Mockito.verify(hook.logger).finest(Mockito.anyString());
        Mockito.verify(hook.getResource(prefix + fileName)).thenReturn(resource);
        
        try {
            Mockito.verify(in).close();
            Mockito.verify(out).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testCopyFromFile() {
        // Test case for copyFromFile method
        String dir = "resources";
        String fileName = "example.txt";
        String destinationDirectory = "destination";
        
        // Set up mocked objects
        Logger logger = Mockito.mock(Logger.class);
        Mockito.when(hook.logger.finest(Mockito.anyString())).thenReturn(logger);
        Mockito.when(hook.logger.severe(Mockito.anyString())).thenReturn(logger);
        
        File resource = Mockito.mock(File.class);
        Mockito.when(hook.getFile(dir, fileName)).thenReturn(resource);
        
        InputStream in = Mockito.mock(InputStream.class);
        OutputStream out = Mockito.mock(OutputStream.class);
        
        try {
            Mockito.when(resource.openStream()).thenReturn(in);
            Mockito.when(hook.getResourceString(Mockito.anyString())).thenReturn("test string");
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // Call the method to be tested
        hook.copyFromFile(dir, fileName, destinationDirectory);
        
        // Verify the interactions
        Mockito.verify(hook.logger).finest(Mockito.anyString());
        Mockito.verify(hook.getFile(dir, fileName)).thenReturn(resource);
        
        try {
            Mockito.verify(in).close();
            Mockito.verify(out).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testImageFilterAccept() {
        // Test case for ImageFilter accept method
        String filePath = "example.jpg";
        
        // Set up Input
        File file = Mockito.mock(File.class);
        Mockito.when(file.isDirectory()).thenReturn(false);
        Mockito.when(file.getName()).thenReturn(filePath);
        
        // Create object to be tested
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("jpg", "JPEG image");
        
        // Call the method to be tested
        boolean result = imageFilter.accept(file);
        
        // Verify the result
        assertTrue(result);
    }
    
    @Test
    public void testImageFilterGetDescription() {
        // Test case for ImageFilter getDescription method
        String description = "JPEG image";
        
        // Create object to be tested
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("jpg", description);
        
        // Call the method to be tested
        String result = imageFilter.getDescription();
        
        // Verify the result
        assertEquals(description, result);
    }
}