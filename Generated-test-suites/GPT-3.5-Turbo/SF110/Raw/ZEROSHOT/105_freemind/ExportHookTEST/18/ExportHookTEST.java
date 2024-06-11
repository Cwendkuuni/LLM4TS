package freemind.extensions;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExportHookTest {
    
    private ExportHook exportHook;
    
    @Before
    public void setUp() {
        exportHook = new ExportHook();
    }
    
    @Test
    public void testChooseFile() {
        // Test case for chooseFile method
        // Test if the returned file is not null
        assertNotNull(exportHook.chooseFile("jpg", "JPEG Image File", ".jpg"));
        
        // Test if the returned file has the correct extension
        String extension = exportHook.chooseFile("jpg", "JPEG Image File", ".jpg").getName().substring(exportHook.chooseFile("jpg", "JPEG Image File", ".jpg").getName().lastIndexOf(".")+1);
        assertEquals("jpg", extension);
        
        // Test if the returned file exists
        assertTrue(exportHook.chooseFile("jpg", "JPEG Image File", ".jpg").exists());
    }
    
    @Test
    public void testChooseImageFile() {
        // Test case for chooseImageFile method
        // Test if the returned file is not null
        assertNotNull(ExportHook.chooseImageFile("png", "PNG Image File", ".png", new ModeController()));
        
        // Test if the returned file has the correct extension
        String extension = ExportHook.chooseImageFile("png", "PNG Image File", ".png", new ModeController()).getName().substring(ExportHook.chooseImageFile("png", "PNG Image File", ".png", new ModeController()).getName().lastIndexOf(".")+1);
        assertEquals("png", extension);
        
        // Test if the returned file exists
        assertTrue(ExportHook.chooseImageFile("png", "PNG Image File", ".png", new ModeController()).exists());
    }
    
    @Test
    public void testGetTranslatableResourceString() {
        // Test case for getTranslatableResourceString method
        // Test if the returned value is not null
        assertNotNull(exportHook.getTranslatableResourceString("resourceName"));
    }
    
    @Test
    public void testCreateBufferedImage() {
        // Test case for createBufferedImage method
        // Test if the returned image is not null
        assertNotNull(exportHook.createBufferedImage());
    }
    
    @Test
    public void testCopyFromResource() {
        // Test case for copyFromResource method
        // Test if the resource can be found and copied successfully
        exportHook.copyFromResource("prefix", "fileName", "destinationDirectory");
        // Check if the copied file exists
        assertTrue(new File("destinationDirectory/fileName").exists());
    }
    
    @Test
    public void testCopyFromFile() {
        // Test case for copyFromFile method
        // Test if the file can be found and copied successfully
        exportHook.copyFromFile("dir", "fileName", "destinationDirectory");
        // Check if the copied file exists
        assertTrue(new File("destinationDirectory/fileName").exists());
    }
    
    @Test
    public void testImageFilter() {
        // Test case for ImageFilter class
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("png", "PNG Image File");
        
        // Test if the accept method returns true for a valid file
        assertTrue(imageFilter.accept(new File("test.png")));
        
        // Test if the accept method returns false for a directory
        assertFalse(imageFilter.accept(new File("test")));
        
        // Test if the accept method returns false for an invalid file
        assertFalse(imageFilter.accept(new File("test.jpg")));
        
        // Test if the getDescription method returns the correct description
        assertEquals("PNG Image File", imageFilter.getDescription());
    }
}