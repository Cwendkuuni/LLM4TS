import org.junit.*;
import static org.junit.Assert.*;

public class ExportHookTest {

    ExportHook exportHook;
    
    @Before
    public void setUp() {
        exportHook = new ExportHook();
    }
    
    @Test
    public void testChooseFile_typical() {
        assertNotNull(exportHook.chooseFile("png", "PNG Image", ".png"));
    }
    
    @Test
    public void testChooseFile_invalidType() {
        assertNull(exportHook.chooseFile("gif", "GIF Image", ".gif"));
    }
    
    @Test
    public void testChooseImageFile_typical() {
        assertNotNull(ExportHook.chooseImageFile("png", "PNG Image", ".png", null));
    }
    
    @Test
    public void testChooseImageFile_invalidType() {
        assertNull(ExportHook.chooseImageFile("gif", "GIF Image", ".gif", null));
    }
    
    @Test
    public void testGetTranslatableResourceString_typical() {
        assertEquals("resource value", exportHook.getTranslatableResourceString("resourceName"));
    }
    
    @Test
    public void testGetTranslatableResourceString_missingKey() {
        assertNull(exportHook.getTranslatableResourceString("missingKey"));
    }
    
    @Test
    public void testCreateBufferedImage_viewNotNull() {
        exportHook.createBufferedImage();
        assertNotNull(exportHook.view);
    }
    
    @Test
    public void testCreateBufferedImage_viewNull() {
        exportHook.view = null;
        assertNull(exportHook.createBufferedImage());
    }
    
    @Test
    public void testCopyFromResource_resourceExists() {
        exportHook.copyFromResource("prefix/", "file.txt", "destination");
        // Check if the file is copied successfully
        assertTrue(fileExists("destination/file.txt"));
    }
    
    @Test
    public void testCopyFromResource_resourceNotFound() {
        exportHook.copyFromResource("prefix/", "nonexistent.txt", "destination");
        // Check if the file is not copied
        assertFalse(fileExists("destination/nonexistent.txt"));
    }
    
    @Test
    public void testCopyFromFile_fileExists() {
        exportHook.copyFromFile("dir/", "file.txt", "destination");
        // Check if the file is copied successfully
        assertTrue(fileExists("destination/file.txt"));
    }
    
    @Test
    public void testCopyFromFile_fileNotFound() {
        exportHook.copyFromFile("dir/", "nonexistent.txt", "destination");
        // Check if the file is not copied
        assertFalse(fileExists("destination/nonexistent.txt"));
    }
    
    private boolean fileExists(String path) {
        File file = new File(path);
        return file.exists() && file.isFile();
    }
}