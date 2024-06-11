
import org.junit.Test;
import static org.junit.Assert.*;

public class ExportHookTest {

    @Test
    public void testChooseFile() {
        ExportHook exportHook = new ExportHook();
        File file = exportHook.chooseFile("png", "PNG Image", ".png");
        assertNotNull(file);
        // Perform additional assertions as needed
    }

    @Test
    public void testChooseImageFile() {
        ModeController controller = new ModeController(); // Create a mock controller
        File file = ExportHook.chooseImageFile("png", "PNG Image", ".png", controller);
        assertNotNull(file);
        // Perform additional assertions as needed
    }

    @Test
    public void testGetTranslatableResourceString() {
        ExportHook exportHook = new ExportHook();
        String resourceName = "resourceName";
        String result = exportHook.getTranslatableResourceString(resourceName);
        assertNotNull(result);
        // Perform additional assertions as needed
    }

    @Test
    public void testCreateBufferedImage() {
        ExportHook exportHook = new ExportHook();
        BufferedImage image = exportHook.createBufferedImage();
        assertNotNull(image);
        // Perform additional assertions as needed
    }

    @Test
    public void testCopyFromResource() {
        ExportHook exportHook = new ExportHook();
        String prefix = "prefix";
        String fileName = "fileName";
        String destinationDirectory = "destinationDirectory";
        
        // Create necessary files/directories for testing
        FileIO.createDirectory(destinationDirectory);
        FileIO.createFile(prefix + fileName);
        
        exportHook.copyFromResource(prefix, fileName, destinationDirectory);
        
        // Perform assertions to check if the file was copied successfully
        File copiedFile = new File(destinationDirectory, fileName);
        assertTrue(copiedFile.exists());
        
        // Clean up the files/directories created for testing
        FileIO.deleteDirectory(destinationDirectory);
        FileIO.deleteFile(prefix + fileName);
    }

    @Test
    public void testCopyFromFile() {
        ExportHook exportHook = new ExportHook();
        String dir = "dir";
        String fileName = "fileName";
        String destinationDirectory = "destinationDirectory";
        
        // Create necessary files/directories for testing
        FileIO.createDirectory(destinationDirectory);
        FileIO.createFile(dir + File.separator + fileName);
        
        exportHook.copyFromFile(dir, fileName, destinationDirectory);
        
        // Perform assertions to check if the file was copied successfully
        File copiedFile = new File(destinationDirectory, fileName);
        assertTrue(copiedFile.exists());
        
        // Clean up the files/directories created for testing
        FileIO.deleteDirectory(destinationDirectory);
        FileIO.deleteFile(dir + File.separator + fileName);
    }

    @Test
    public void testImageFilterAccept() {
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("png", "PNG Image");
        File pngFile = new File("image.png");
        File textFile = new File("text.txt");
        
        assertTrue(imageFilter.accept(pngFile));
        assertFalse(imageFilter.accept(textFile));
    }

    @Test
    public void testImageFilterGetDescription() {
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("png", "PNG Image");
        String expectedDescription = "PNG Image";
        String actualDescription = imageFilter.getDescription();
        
        assertEquals(expectedDescription, actualDescription);
    }
}

