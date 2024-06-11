import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;

public class ExportHookTest {
    
    private ExportHook exportHook;
    
    @Before
    public void setUp() {
        exportHook = new ExportHook();
    }
    
    @Test
    public void testChooseFile() {
        File file = exportHook.chooseFile("jpg", "Image File", ".jpg");
        assertNotNull(file);
    }
    
    @Test
    public void testChooseImageFile() {
        File file = ExportHook.chooseImageFile("png", "Image File", ".png", null);
        assertNull(file);
    }
    
    @Test
    public void testGetTranslatableResourceString() {
        String resourceString = exportHook.getTranslatableResourceString("testResource");
        assertNotNull(resourceString);
    }
    
    @Test
    public void testCreateBufferedImage() {
        BufferedImage image = exportHook.createBufferedImage();
        assertNotNull(image);
    }
    
    @Test
    public void testCopyFromResource() {
        exportHook.copyFromResource("test/", "test.txt", "destination");
        File file = new File("destination", "test.txt");
        assertTrue(file.exists());
    }
    
    @Test
    public void testCopyFromFile() {
        exportHook.copyFromFile("source", "test.txt", "destination");
        File file = new File("destination", "test.txt");
        assertTrue(file.exists());
    }
    
    @Test
    public void testImageFilterAccept() {
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("jpg", "JPEG Image");
        File jpgFile = new File("test.jpg");
        assertTrue(imageFilter.accept(jpgFile));
        
        File txtFile = new File("test.txt");
        assertFalse(imageFilter.accept(txtFile));
    }
    
    @Test
    public void testImageFilterGetDescription() {
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("jpeg", "JPEG Image");
        String description = imageFilter.getDescription();
        assertEquals("JPEG Image", description);
    }
}