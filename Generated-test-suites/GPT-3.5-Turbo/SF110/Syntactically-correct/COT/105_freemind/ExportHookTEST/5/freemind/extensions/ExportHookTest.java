import static org.junit.Assert.*;
import org.junit.Test;

public class ExportHookTest {

    @Test
    public void testChooseFile() {
        ExportHook exportHook = new ExportHook();
        String type = "jpg";
        String description = "JPEG File";
        String nameExtension = "_image";
        
        // Test basic functionality
        File result = exportHook.chooseFile(type, description, nameExtension);
        assertNotNull(result);
    }

    @Test
    public void testChooseImageFile() {
        ExportHook exportHook = new ExportHook();
        ModeController controller = new ModeController();
        String	type = "jpg";
        String description = "JPEG File";
        String nameExtension = "_image";
        
        // Test basic functionality
        File result = exportHook.chooseImageFile(type, description, nameExtension, controller);
        assertNotNull(result);
    }

    @Test
    public void testGetTranslatableResourceString() {
        ExportHook exportHook = new ExportHook();
        String resourceName = "test_resource";
        
        // Test basic functionality
        String result = exportHook.getTranslatableResourceString(resourceName);
        assertNotNull(result);
    }

    @Test
    public void testCreateBufferedImage() {
        ExportHook exportHook = new ExportHook();
        
        // Test basic functionality
        BufferedImage result = exportHook.createBufferedImage();
        assertNotNull(result);
    }

    @Test
    public void testCopyFromResource() {
        ExportHook exportHook = new ExportHook();
        String prefix = "resources/";
        String fileName = "test_file.txt";
        String destinationDirectory = "destination/";
        
        // Test basic functionality
        exportHook.copyFromResource(prefix, fileName, destinationDirectory);
        // Check if the file was copied successfully
        File copiedFile = new File(destinationDirectory + fileName);
        assertTrue(copiedFile.exists());
    }

    @Test
    public void testCopyFromFile() {
        ExportHook exportHook = new ExportHook();
        String dir = "source/";
        String fileName = "test_file.txt";
        String destinationDirectory = "destination/";
        
        // Test basic functionality
        exportHook.copyFromFile(dir, fileName, destinationDirectory);
        // Check if the file was copied successfully
        File copiedFile = new File(destinationDirectory + fileName);
        assertTrue(copiedFile.exists());
    }

    @Test
    public void testImageFilter_accept() {
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("jpg", "JPEG File");
        File jpegFile = new File("image.jpg");
        File txtFile = new File("text.txt");
        
        // Test JPEG file acceptance
        boolean result1 = imageFilter.accept(jpegFile);
        assertTrue(result1);
        
        // Test non-JPEG file rejection
        boolean result2 = imageFilter.accept(txtFile);
        assertFalse(result2);
    }

    @Test
    public void testImageFilter_getDescription() {
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("jpg", "JPEG File");
        
        // Test getDescription() function
        String result = imageFilter.getDescription();
        assertNotNull(result);
    }
}