
import freemind.extensions.*;
import freemind.main.*;
import freemind.modes.*;
import freemind.view.mindmapview.*;
import org.junit.*;
import javax.swing.*;
import javax.swing.filechooser.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.net.*;
import java.text.*;
import java.util.logging.*;

public class ExportHookTest {
    
    private ExportHook exportHook;
    private ModeController modeController;
    
    @Before
    public void setUp() {
        exportHook = new ExportHook();
        modeController = new ModeController();
    }
    
    @Test
    public void testChooseFile() {
        File file = exportHook.chooseFile("jpg", "JPEG Image", ".jpg");
        assertNotNull(file);
    }
    
    @Test
    public void testChooseImageFile() {
        File file = exportHook.chooseImageFile("jpg", "JPEG Image", ".jpg", modeController);
        assertNotNull(file);
    }
    
    @Test
    public void testGetTranslatableResourceString() {
        String resourceString = exportHook.getTranslatableResourceString("resourceName");
        assertNotNull(resourceString);
    }
    
    @Test
    public void testCreateBufferedImage() {
        BufferedImage image = exportHook.createBufferedImage();
        assertNotNull(image);
    }
    
    @Test
    public void testCopyFromResource() {
        exportHook.copyFromResource("prefix", "fileName", "destinationDirectory");
    }
    
    @Test
    public void testCopyFromFile() {
        exportHook.copyFromFile("dir", "fileName", "destinationDirectory");
    }
    
    @Test
    public void testImageFilterAccept() {
        FileFilter filter = new ExportHook.ImageFilter("jpg", "JPEG Image");
        File file = new File("test.jpg");
        assertTrue(filter.accept(file));
    }
    
    @Test
    public void testImageFilterGetDescription() {
        FileFilter filter = new ExportHook.ImageFilter("jpg", "JPEG Image");
        String description = filter.getDescription();
        assertEquals("JPEG Image", description);
    }
}
