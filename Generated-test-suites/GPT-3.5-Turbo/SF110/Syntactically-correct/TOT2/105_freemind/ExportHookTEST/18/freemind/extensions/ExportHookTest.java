package freemind.extensions;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.awt.image.BufferedImage;

public class ExportHookTest {
    
    @Test
    public void testChooseFile() {
        ExportHook exportHook = new ExportHook();
        File chosenFile = exportHook.chooseFile("jpg", "JPEG Image", ".export");
        assertNotNull(chosenFile);
    }
    
    @Test
    public void testImageFilter() {
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("jpg", "JPEG Image");
        File file = new File("test.jpg");
        assertTrue(imageFilter.accept(file));
    }
    
    @Test
    public void testGetTranslatableResourceString() {
        ExportHook exportHook = new ExportHook();
        String resourceString = exportHook.getTranslatableResourceString("testResource");
        assertNotNull(resourceString);
    }
    
    @Test
    public void testCreateBufferedImage() {
        ExportHook exportHook = new ExportHook();
        BufferedImage image = exportHook.createBufferedImage();
        assertNotNull(image);
    }
    
    @Test
    public void testCopyFromResource() {
        ExportHook exportHook = new ExportHook();
        exportHook.copyFromResource("images/", "test.jpg", "destination/");
        File copiedFile = new File("destination/test.jpg");
        assertTrue(copiedFile.exists());
    }
    
    @Test
    public void testCopyFromFile() {
        ExportHook exportHook = new ExportHook();
        exportHook.copyFromFile("src/", "test.txt", "destination/");
        File copiedFile = new File("destination/test.txt");
        assertTrue(copiedFile.exists());
    }
}