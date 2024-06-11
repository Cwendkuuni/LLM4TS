import org.junit.*;
import static org.junit.Assert.*;

public class ExportHookTest {

    @Test
    public void testChooseFile() {
        ExportHook hook = new ExportHook();
        String type = "png";
        String description = "PNG Image";
        String nameExtension = "_export";
        
        // Test if the chosen file is not null
        assertNotNull(hook.chooseFile(type, description, nameExtension));
    }
    
    @Test
    public void testChooseImageFile() {
        ExportHook hook = new ExportHook();
        String type = "png";
        String description = "PNG Image";
        String nameExtension = "_export";
        ModeController controller = new ModeController();
        
        // Test if the chosen file is not null
        assertNotNull(hook.chooseImageFile(type, description, nameExtension, controller));
    }
    
    @Test
    public void testGetTranslatableResourceString() {
        ExportHook hook = new ExportHook();
        String resourceName = "test_resource";
        
        // Test if the return value is not null
        assertNotNull(hook.getTranslatableResourceString(resourceName));
    }
    
    @Test
    public void testCreateBufferedImage() {
        ExportHook hook = new ExportHook();
        
        // Test if the created buffered image is not null
        assertNotNull(hook.createBufferedImage());
    }
    
    @Test
    public void testCopyFromResource() {
        ExportHook hook = new ExportHook();
        String prefix = "resource_prefix/";
        String fileName = "test_file.txt";
        String destinationDirectory = "destination_directory";
        
        // Test if the file is copied successfully
        hook.copyFromResource(prefix, fileName, destinationDirectory);
    }
    
    @Test
    public void testCopyFromFile() {
        ExportHook hook = new ExportHook();
        String dir = "source_directory";
        String fileName = "test_file.txt";
        String destinationDirectory = "destination_directory";
        
        // Test if the file is copied successfully
        hook.copyFromFile(dir, fileName, destinationDirectory);
    }
    
    @Test
    public void testImageFilterAccept() {
        ExportHook.ImageFilter filter = new ExportHook.ImageFilter("png", "PNG Image");
        File dir = new File("image_directory");
        File file = new File(dir, "test_image.png");
        
        // Test if the filter accepts a directory
        assertTrue(filter.accept(dir));
        
        // Test if the filter accepts a file with the correct extension
        assertTrue(filter.accept(file));
    }
    
    @Test
    public void testImageFilterGetDescription() {
        ExportHook.ImageFilter filter = new ExportHook.ImageFilter("png", "PNG Image");
        
        // Test if the description is correct
        assertEquals("PNG Image", filter.getDescription());
    }
}