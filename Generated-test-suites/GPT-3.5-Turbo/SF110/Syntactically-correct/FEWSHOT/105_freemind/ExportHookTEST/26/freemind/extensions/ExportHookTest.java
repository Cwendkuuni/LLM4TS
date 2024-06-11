package freemind.extensions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ExportHookTest {
    @Test
    public void testChooseFile() {
        ExportHook exportHook = new ExportHook();
        String type = "png";
        String description = "Image";
        String nameExtension = "export";
        assertEquals(null, exportHook.chooseFile(type, description, nameExtension));
    }
    
    @Test
    public void testChooseImageFile() {
        ExportHook exportHook = new ExportHook();
        String type = "png";
        String description = "Image";
        String nameExtension = "export";
        ModeController controller = new ModeController();
        assertEquals(null, exportHook.chooseImageFile(type, description, nameExtension, controller));
    }
    
    @Test
    public void testGetTranslatableResourceString() {
        ExportHook exportHook = new ExportHook();
        String resourceName = "resource";
        assertEquals(resourceName, exportHook.getTranslatableResourceString(resourceName));
    }

    @Test
    public void testCreateBufferedImage() {
        ExportHook exportHook = new ExportHook();
        assertEquals(null, exportHook.createBufferedImage());
    }
    
    @Test
    public void testCopyFromResource() {
        ExportHook exportHook = new ExportHook();
        String prefix = "prefix/";
        String fileName = "file.txt";
        String destinationDirectory = "directory";
        exportHook.copyFromResource(prefix, fileName, destinationDirectory);
    }
    
    @Test
    public void testCopyFromFile() {
        ExportHook exportHook = new ExportHook();
        String dir = "dir";
        String fileName = "file.txt";
        String destinationDirectory = "directory";
        exportHook.copyFromFile(dir, fileName, destinationDirectory);
    }
    
    @Test
    public void testImageFilterAccept() {
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("png", "Image");
        assertEquals(false, imageFilter.accept(new File("file.txt")));
    }
    
    @Test
    public void testImageFilterGetDescription() {
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("png", "Image");
        assertEquals("Image", imageFilter.getDescription());
    }
}