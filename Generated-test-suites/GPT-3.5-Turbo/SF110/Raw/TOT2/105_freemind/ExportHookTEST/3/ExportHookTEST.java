import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;

public class ExportHookTest {
    
    ExportHook exportHook;
    
    @Before
    public void setUp() {
        exportHook = new ExportHook();
    }
    
    @Test
    public void testChooseFile() {
        File file = exportHook.chooseFile("png", "PNG Image", ".png");
        assertNotNull(file);
    }
    
    @Test
    public void testChooseImageFile() {
        File imageFile = ExportHook.chooseImageFile("png", "PNG Image", ".png", new ModeController());
        assertNotNull(imageFile);
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
        exportHook.copyFromResource("resources/", "testResource.txt", "destination/");
        File copiedFile = new File("destination/testResource.txt");
        assertTrue(copiedFile.exists());
    }
    
    @Test
    public void testCopyFromFile() {
        exportHook.copyFromFile("source/", "testFile.txt", "destination/");
        File copiedFile = new File("destination/testFile.txt");
        assertTrue(copiedFile.exists());
    }
    
    @Test
    public void testImageFilterAccept() {
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("jpg", "JPG Image");
        File directory = new File("testDir");
        assertTrue(imageFilter.accept(directory));
    }
    
    @Test
    public void testImageFilterDescription() {
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("jpg", "JPG Image");
        assertEquals("JPG Image", imageFilter.getDescription());
    }
}