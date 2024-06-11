import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;

public class ExportHookTest {
    
    ExportHook exportHook = new ExportHook();
    
    @Test
    public void testChooseFile() {
        File chosenFile = exportHook.chooseFile("png", "Image File", "_export");
        assertNotNull(chosenFile);
    }
    
    @Test
    public void testChooseImageFile() {
        File mmFile = new File("test.mm");
        ModeController controller = new ModeController();
        File chosenFile = ExportHook.chooseImageFile("png", "Image File", "_export", controller);
        assertNotNull(chosenFile);
    }
    
    @Test
    public void testGetTranslatableResourceString() {
        String resourceName = "test_resource";
        String returnValue = exportHook.getTranslatableResourceString(resourceName);
        assertNotNull(returnValue);
    }
    
    @Test
    public void testCreateBufferedImage() {
        BufferedImage image = exportHook.createBufferedImage();
        assertNotNull(image);
    }
    
    @Test
    public void testCopyFromResource() {
        String prefix = "test_prefix";
        String fileName = "test_file.txt";
        String destinationDirectory = "dest_dir";
        exportHook.copyFromResource(prefix, fileName, destinationDirectory);
        
        File copiedFile = new File(destinationDirectory + "/" + fileName);
        assertTrue(copiedFile.exists());
    }
    
    @Test
    public void testCopyFromFile() {
        String dir = "test_dir";
        String fileName = "test_file.txt";
        String destinationDirectory = "dest_dir";
        exportHook.copyFromFile(dir, fileName, destinationDirectory);
        
        File copiedFile = new File(destinationDirectory + "/" + fileName);
        assertTrue(copiedFile.exists());
    }
    
    @Test
    public void testImageFilterAccept() {
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("jpg", "JPEG Image");
        File file = new File("test.jpg");
        assertTrue(imageFilter.accept(file));
        
        File directory = new File("testDir");
        assertTrue(imageFilter.accept(directory));
    }
    
    @Test
    public void testImageFilterDescription() {
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("jpg", "JPEG Image");
        assertEquals("JPEG Image", imageFilter.getDescription());
    }
}