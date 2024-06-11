import org.junit.Test;
import static org.junit.Assert.*;
import freemind.extensions.ExportHook;
import java.io.File;

public class ExportHookTest {
    
    @Test
    public void testChooseFile() {
        ExportHook exportHook = new ExportHook();
        File file = exportHook.chooseFile("jpg", "JPEG Image", ".jpg");
        assertNotNull(file);
    }

    @Test
    public void testChooseImageFile() {
        ExportHook exportHook = new ExportHook();
        File file = ExportHook.chooseImageFile("jpg", "JPEG Image", ".jpg", null);
        assertNotNull(file);
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
        assertNotNull(exportHook.createBufferedImage());
    }

    @Test
    public void testCopyFromResource() {
        ExportHook exportHook = new ExportHook();
        exportHook.copyFromResource("prefix", "file.txt", "destination");
        // Add assertion for file copy success
    }

    @Test
    public void testCopyFromFile() {
        ExportHook exportHook = new ExportHook();
        exportHook.copyFromFile("directory", "file.txt", "destination");
        // Add assertion for file copy success
    }

    @Test
    public void testImageFilterAccept() {
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("jpg", "JPEG Image");
        assertTrue(imageFilter.accept(new File("test.jpg")));
        assertFalse(imageFilter.accept(new File("test.txt")));
    }

    @Test
    public void testImageFilterDescription() {
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("jpg", "JPEG Image");
        assertEquals("JPEG Image", imageFilter.getDescription());
    }
}