import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import javax.swing.filechooser.*;
import java.text.*;
import javax.swing.*;
import freemind.modes.*;
import java.awt.image.*;
import java.awt.*;
import freemind.main.*;
import java.net.*;
import java.io.*;

public class ExportHookTest {
    
    private ExportHook exportHook;
    
    @Before
    public void setUp() {
        exportHook = new ExportHook();
    }
    
    @Test
    public void testChooseFile() {
        ModeController controller = new ModeController();
        String type = "png";
        String description = "PNG Image";
        String nameExtension = ".png";
        
        File file = exportHook.chooseFile(type, description, nameExtension);
        
        assertNotNull(file);
        assertTrue(file.exists());
    }
    
    @Test
    public void testChooseImageFile() {
        ModeController controller = new ModeController();
        String type = "png";
        String description = "PNG Image";
        String nameExtension = ".png";
        
        File file = ExportHook.chooseImageFile(type, description, nameExtension, controller);
        
        assertNotNull(file);
        assertTrue(file.exists());
    }
    
    @Test
    public void testGetTranslatableResourceString() {
        String resourceName = "resourceName";
        
        String result = exportHook.getTranslatableResourceString(resourceName);
        
        assertNotNull(result);
        assertFalse(result.startsWith("%"));
    }
    
    @Test
    public void testCreateBufferedImage() {
        BufferedImage image = exportHook.createBufferedImage();
        
        assertNotNull(image);
        assertEquals(0, image.getWidth());
        assertEquals(0, image.getHeight());
    }
    
    @Test
    public void testCopyFromResource() {
        String prefix = "prefix/";
        String fileName = "file.txt";
        String destinationDirectory = "destination";
        
        exportHook.copyFromResource(prefix, fileName, destinationDirectory);
        
        File file = new File(destinationDirectory, fileName);
        assertTrue(file.exists());
    }
    
    @Test
    public void testCopyFromFile() {
        String dir = "directory";
        String fileName = "file.txt";
        String destinationDirectory = "destination";
        
        exportHook.copyFromFile(dir, fileName, destinationDirectory);
        
        File file = new File(destinationDirectory, fileName);
        assertTrue(file.exists());
    }
    
    @Test
    public void testImageFilterAccept() {
        File file = new File("image.png");
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("png", "PNG Image");
        
        assertTrue(imageFilter.accept(file));
    }
    
    @Test
    public void testImageFilterAcceptInvalidExtension() {
        File file = new File("image.jpg");
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("png", "PNG Image");
        
        assertFalse(imageFilter.accept(file));
    }
    
    @Test
    public void testImageFilterGetDescription() {
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("png", "PNG Image");
        
        String description = imageFilter.getDescription();
        
        assertNotNull(description);
        assertEquals("PNG Image", description);
    }
    
    @Test
    public void testImageFilterGetDescriptionNoCustomDescription() {
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("png", null);
        
        String description = imageFilter.getDescription();
        
        assertNotNull(description);
        assertEquals("png", description);
    }
}