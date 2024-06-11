package freemind.extensions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ExportHookTest {
    @Test
    public void testChooseFile() {
        // Test with valid inputs
        final ExportHook exportHook = new ExportHook();
        final String type = "png";
        final String description = "PNG Image";
        final String nameExtension = "export";
        final File chosenFile = exportHook.chooseFile(type, description, nameExtension);
        assertNotNull(chosenFile);
        
        // Test with null inputs
        assertNull(exportHook.chooseFile(null, null, null));
    }

    @Test
    public void testChooseImageFile() {
        // Test with valid inputs
        final ExportHook exportHook = new ExportHook();
        final ModeController controller = new ModeController();
        controller.setFrame(new JFrame());
        final File chosenFile = exportHook.chooseImageFile("png", "PNG Image", "export", controller);
        assertNotNull(chosenFile);
        
        // Test with null inputs
        assertNull(exportHook.chooseImageFile(null, null, null, null));
    }

    @Test
    public void testGetTranslatableResourceString() {
        final ExportHook exportHook = new ExportHook();
        
        // Test with valid resource name
        assertEquals("FreeMind", exportHook.getTranslatableResourceString("application.name"));
        
        // Test with resource name starting with %
        assertEquals("", exportHook.getTranslatableResourceString("%invalid.name"));
        
        // Test with null resource name
        assertNull(exportHook.getTranslatableResourceString(null));
    }

    @Test
    public void testCreateBufferedImage() {
        final ExportHook exportHook = new ExportHook();
        
        // Test with valid view object
        final BufferedImage image = exportHook.createBufferedImage();
        assertNotNull(image);
        
        // Test with null view object
        assertNull(exportHook.createBufferedImage());
    }

    @Test
    public void testCopyFromResource() {
        final ExportHook exportHook = new ExportHook();
        
        // Test with valid inputs
        exportHook.copyFromResource("prefix", "fileName", "destinationDirectory");
        
        // Test with invalid inputs
        exportHook.copyFromResource("prefix", "nonExistingFileName", "destinationDirectory");
    }

    @Test
    public void testCopyFromFile() {
        final ExportHook exportHook = new ExportHook();
        
        // Test with valid inputs
        exportHook.copyFromFile("dir", "fileName", "destinationDirectory");
        
        // Test with invalid inputs
        exportHook.copyFromFile("nonExistingDir", "fileName", "destinationDirectory");
    }

    @Test
    public void testImageFilter() {
        final ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("png", "PNG Image");
        
        // Test accept() with valid png file
        final File pngFile = new File("image.png");
        assertTrue(imageFilter.accept(pngFile));
        
        // Test accept() with valid jpg file
        final File jpgFile = new File("image.jpg");
        assertFalse(imageFilter.accept(jpgFile));
        
        // Test accept() with directory
        final File directory = new File("path/to/directory");
        assertTrue(imageFilter.accept(directory));
        
        // Test getDescription()
        assertEquals("PNG Image", imageFilter.getDescription());
    }
}