package freemind.extensions;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import java.io.File;

public class ExportHookTest {
    
    private ExportHook exportHook;

    @Before
    public void setUp() {
        exportHook = new ExportHook();
    }

    @Test
    public void testChooseFile() {
        File chosenFile = exportHook.chooseFile("png", "PNG Image File", ".png");
        assertNotNull(chosenFile);
    }

    @Test
    public void testChooseImageFile() {
        File chosenFile = ExportHook.chooseImageFile("png", "PNG Image File", ".png", null);
        assertNull(chosenFile);
    }

    @Test
    public void testGetTranslatableResourceString() {
        String translatableResourceString = exportHook.getTranslatableResourceString("testResource");
        assertEquals("Translated testResource", translatableResourceString);
    }

    @Test
    public void testCreateBufferedImage() {
        assertNotNull(exportHook.createBufferedImage());
    }

    @Test
    public void testCopyFromResource() {
        exportHook.copyFromResource("images/", "test.png", "/tmp");
        File copiedFile = new File("/tmp/test.png");
        assertTrue(copiedFile.exists());
    }

    @Test
    public void testCopyFromFile() {
        exportHook.copyFromFile("/tmp", "test.txt", "/tmp");
        File copiedFile = new File("/tmp/test.txt");
        assertTrue(copiedFile.exists());
    }

    @Test
    public void testImageFilter() {
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("png", "PNG Image");
        File testFile = new File("test.png");
        assertTrue(imageFilter.accept(testFile));
        assertEquals("PNG Image", imageFilter.getDescription());
    }
}