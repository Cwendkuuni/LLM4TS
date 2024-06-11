import org.junit.*;
import static org.junit.Assert.*;
import freemind.extensions.ExportHook;
import freemind.view.mindmapview.MapView;
import freemind.modes.ModeController;

public class ExportHookTest {
    
    private ExportHook exportHook;
    private ModeController modeController;
    
    @Before
    public void setUp() {
        exportHook = new ExportHook();
        modeController = new ModeController();
        MapView view = new MapView();
        modeController.setView(view);
        exportHook.setController(modeController);
    }
    
    @Test
    public void testChooseFile() {
        String type = "png";
        String description = "Image file";
        String nameExtension = "_export";
        
        assertNotNull(exportHook.chooseFile(type, description, nameExtension));
    }
    
    @Test
    public void testChooseImageFile() {
        String type = "png";
        String description = "Image file";
        String nameExtension = "_export";
        
        assertNotNull(exportHook.chooseImageFile(type, description, nameExtension, modeController));
    }
    
    @Test
    public void testGetTranslatableResourceString() {
        String resourceName = "test_resource";
        
        assertEquals("Resource String", exportHook.getTranslatableResourceString(resourceName));
    }
    
    @Test
    public void testCreateBufferedImage() {
        assertNotNull(exportHook.createBufferedImage());
    }
    
    @Test
    public void testCopyFromResource() {
        String prefix = "/resources/";
        String fileName = "test_file.jpg";
        String destinationDirectory = "test_directory";
        
        exportHook.copyFromResource(prefix, fileName, destinationDirectory);
    }
    
    @Test
    public void testCopyFromFile() {
        String dir = "test_directory";
        String fileName = "test_file.jpg";
        String destinationDirectory = "test_destination";
        
        exportHook.copyFromFile(dir, fileName, destinationDirectory);
    }
    
    @Test
    public void testImageFilterAccept() {
        ExportHook.ImageFilter imageFilter = exportHook.new ImageFilter("png", "Image file");
        
        assertTrue(imageFilter.accept(new File("image.png")));
    }
    
    @Test
    public void testImageFilterGetDescription() {
        ExportHook.ImageFilter imageFilter = exportHook.new ImageFilter("png", "Image file");
        
        assertEquals("Image file", imageFilter.getDescription());
    }
}