import org.junit.*;
import static org.junit.Assert.*;

public class ExportHookTest {

    // Test case for chooseFile method
    @Test
    public void testChooseFile() {
        ExportHook exportHook = new ExportHook();
        String type = "png";
        String description = "PNG Image";
        String nameExtension = ".png";
        File chosenFile = exportHook.chooseFile(type, description, nameExtension);
        // Assert that the chosen file is not null
        assertNotNull(chosenFile);
        // Assert that the chosen file has the correct extension
        assertEquals("png", Tools.getExtension(chosenFile.getName()));
    }
  
    // Test case for chooseImageFile method
    @Test
    public void testChooseImageFile() {
        ExportHook exportHook = new ExportHook();
        String type = "png";
        String description = "PNG Image";
        String nameExtension = ".png";
        ModeController controller = new ModeController();
        controller.getFrame().getContentPane();
        File chosenFile = exportHook.chooseImageFile(type, description, nameExtension, controller);
        // Assert that the chosen file is not null
        assertNotNull(chosenFile);
        // Assert that the chosen file has the correct extension
        assertEquals("png", Tools.getExtension(chosenFile.getName()));
    }
  
    // Test case for getTranslatableResourceString method
    @Test
    public void testGetTranslatableResourceString() {
        ExportHook exportHook = new ExportHook();
        String resourceName = "resourceName";
        String returnValue = exportHook.getTranslatableResourceString(resourceName);
        // Assert that the return value is not null
        assertNotNull(returnValue);
        // Assert that the return value is equal to the expected value
        assertEquals("Expected Value", returnValue);
    }
  
    // Test case for createBufferedImage method
    @Test
    public void testCreateBufferedImage() {
        ExportHook exportHook = new ExportHook();
        BufferedImage bufferedImage = exportHook.createBufferedImage();
        // Assert that the buffered image is not null
        assertNotNull(bufferedImage);
        // Assert that the buffered image has the correct width and height
        assertEquals(800, bufferedImage.getWidth());
        assertEquals(600, bufferedImage.getHeight());
    }
  
    // Test case for copyFromResource method
    @Test
    public void testCopyFromResource() {
        ExportHook exportHook = new ExportHook();
        String prefix = "prefix/";
        String fileName = "file.txt";
        String destinationDirectory = "destination/";
        exportHook.copyFromResource(prefix, fileName, destinationDirectory);
        // Assert that the file is copied to the destination directory
        File copiedFile = new File(destinationDirectory + fileName);
        assertTrue(copiedFile.exists());
        // Assert that the copied file has the same content as the resource file
        String copiedFileContent = Tools.readFile(copiedFile);
        String resourceFileContent = Tools.readFile(new File(prefix + fileName));
        assertEquals(resourceFileContent, copiedFileContent);
    }
  
    // Test case for copyFromFile method
    @Test
    public void testCopyFromFile() {
        ExportHook exportHook = new ExportHook();
        String dir = "directory/";
        String fileName = "file.txt";
        String destinationDirectory = "destination/";
        exportHook.copyFromFile(dir, fileName, destinationDirectory);
        // Assert that the file is copied to the destination directory
        File copiedFile = new File(destinationDirectory + fileName);
        assertTrue(copiedFile.exists());
        // Assert that the copied file has the same content as the resource file
        String copiedFileContent = Tools.readFile(copiedFile);
        String resourceFileContent = Tools.readFile(new File(dir + fileName));
        assertEquals(resourceFileContent, copiedFileContent);
    }
  
    // Test case for ImageFilter accept method
    @Test
    public void testImageFilterAccept() {
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("png", "PNG Image");
        File file = new File("image.png");
        // Assert that the file is accepted by the image filter
        assertTrue(imageFilter.accept(file));
    }
  
    // Test case for ImageFilter getDescription method
    @Test
    public void testImageFilterGetDescription() {
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("png", "PNG Image");
        // Assert that the description of the image filter is correct
        assertEquals("PNG Image", imageFilter.getDescription());
    }
}