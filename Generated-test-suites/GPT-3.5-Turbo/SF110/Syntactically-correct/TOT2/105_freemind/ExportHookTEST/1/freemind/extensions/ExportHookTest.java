package freemind.extensions;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.image.BufferedImage;
import javax.swing.filechooser.FileFilter;
import java.io.File;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ExportHookTest {
    
    private ExportHook exportHook;
    
    @Before
    public void setUp() {
        exportHook = new ExportHook();
    }
    
    @Test
    public void testChooseFile() {
        File file = exportHook.chooseFile("jpg", "JPEG Image", ".jpg");
        assertNotNull(file);
    }
    
    @Test
    public void testChooseImageFile() {
        FileFilter filter = new FileNameExtensionFilter("JPEG Image", "jpg");
        File imageFile = ExportHook.chooseImageFile("jpg", "JPEG Image", ".jpg", exportHook.getController());
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
        exportHook.copyFromResource("testPrefix", "testFile.txt", "/destination/directory");
        // Check if file was copied successfully
    }
    
    @Test
    public void testCopyFromFile() {
        exportHook.copyFromFile("/source/directory", "testFile.txt", "/destination/directory");
        // Check if file was copied successfully
    }
    
    @Test
    public void testImageFilterAccept() {
        File imageFile = new File("testImage.jpg");
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("jpg", "JPEG Image");
        assertTrue(imageFilter.accept(imageFile));
    }
    
    @Test
    public void testImageFilterDescription() {
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("jpg", "JPEG Image");
        assertEquals("JPEG Image", imageFilter.getDescription());
    }
}