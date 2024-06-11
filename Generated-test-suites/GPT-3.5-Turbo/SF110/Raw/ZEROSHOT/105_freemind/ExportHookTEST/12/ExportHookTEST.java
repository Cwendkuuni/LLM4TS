import org.junit.*;
import static org.junit.Assert.*;
import java.awt.image.*;
import java.io.*;

public class ExportHookTest {
   
    private ExportHook exportHook;
    
    @Before
    public void setUp() {
        exportHook = new ExportHook();
    }
    
    @Test
    public void testChooseImageFile() {
        File chosenFile = exportHook.chooseImageFile("png", "PNG Image", ".png", null);
        
        assertNotNull(chosenFile);
        assertEquals(".png", chosenFile.getAbsolutePath().substring(chosenFile.getAbsolutePath().lastIndexOf('.')));
    }
    
    @Test
    public void testGetTranslatableResourceString() {
        String resourceName = "resourceName";
        String returnValue = exportHook.getTranslatableResourceString(resourceName);
        
        assertNotNull(returnValue);
    }
    
    @Test
    public void testCreateBufferedImage() {
        BufferedImage bufferedImage = exportHook.createBufferedImage();
        
        assertNotNull(bufferedImage);
        assertTrue(bufferedImage instanceof BufferedImage);
    }
    
    @Test
    public void testCopyFromResource() {
        String prefix = "prefix";
        String fileName = "fileName";
        String destinationDirectory = "destinationDirectory";
        
        exportHook.copyFromResource(prefix, fileName, destinationDirectory);
        
        File copiedFile = new File(destinationDirectory + "/" + fileName);
        assertTrue(copiedFile.exists());
    }
    
    @Test
    public void testCopyFromFile() {
        String dir = "dir";
        String fileName = "fileName";
        String destinationDirectory = "destinationDirectory";
        
        exportHook.copyFromFile(dir, fileName, destinationDirectory);
        
        File copiedFile = new File(destinationDirectory + "/" + fileName);
        assertTrue(copiedFile.exists());
    }
    
    @Test
    public void testImageFilterAccept() {
        ExportHook.ImageFilter imageFilter = exportHook.new ImageFilter("png", "PNG Image");
        
        assertTrue(imageFilter.accept(new File("test.png")));
        assertFalse(imageFilter.accept(new File("test.jpg")));
        assertFalse(imageFilter.accept(new File("test.txt")));
    }
    
    @Test
    public void testImageFilterGetDescription() {
        ExportHook.ImageFilter imageFilter = exportHook.new ImageFilter("png", "PNG Image");
        
        assertEquals("PNG Image", imageFilter.getDescription());
    }
}