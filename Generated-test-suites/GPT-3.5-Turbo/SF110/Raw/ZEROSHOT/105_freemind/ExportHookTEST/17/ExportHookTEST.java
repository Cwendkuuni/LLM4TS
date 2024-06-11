import static org.junit.Assert.*;
import org.junit.Test;
import javax.swing.JFileChooser;

public class ExportHookTest {

    // Test chooseFile method
    @Test
    public void testChooseFile() {
        ExportHook exportHook = new ExportHook();
        File file = exportHook.chooseFile("png", "PNG Image", "_exported");
        assertNotNull(file);
    }

    // Test chooseImageFile method
    @Test
    public void testChooseImageFile() {
        ExportHook exportHook = new ExportHook();
        ModeController controller = new ModeController();
        File file = exportHook.chooseImageFile("png", "PNG Image", "_exported", controller);
        assertNotNull(file);
    }

    // Test getTranslatableResourceString method
    @Test
    public void testGetTranslatableResourceString() {
        ExportHook exportHook = new ExportHook();
        String resourceName = "test_resource";
        String result = exportHook.getTranslatableResourceString(resourceName);
        assertNotNull(result);
        assertEquals("Test Resource", result);
    }

    // Test createBufferedImage method
    @Test
    public void testCreateBufferedImage() {
        ExportHook exportHook = new ExportHook();
        ModeController controller = new ModeController();
        exportHook.setController(controller);
        BufferedImage image = exportHook.createBufferedImage();
        assertNotNull(image);
    }

    // Test copyFromResource method
    @Test
    public void testCopyFromResource() {
        ExportHook exportHook = new ExportHook();
        exportHook.copyFromResource("images/", "test_image.png", "test_directory");
        File file = new File("test_directory/test_image.png");
        assertTrue(file.exists());
    }

    // Test copyFromFile method
    @Test
    public void testCopyFromFile() {
        ExportHook exportHook = new ExportHook();
        exportHook.copyFromFile("resources/images/", "test_image.png", "test_directory");
        File file = new File("test_directory/test_image.png");
        assertTrue(file.exists());
    }

    // Test ImageFilter accept method
    @Test
    public void testImageFilterAccept() {
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("png", "PNG Image");
        File testFile = new File("test_image.png");
        assertTrue(imageFilter.accept(testFile));
        File directory = new File("test_directory");
        assertTrue(imageFilter.accept(directory));
    }

    // Test ImageFilter getDescription method
    @Test
    public void testImageFilterGetDescription() {
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("png", "PNG Image");
        String description = imageFilter.getDescription();
        assertEquals("PNG Image", description);
    }

}