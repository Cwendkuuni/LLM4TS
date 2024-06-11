import org.junit.*;
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

    @Test
    public void testChooseFile() {
        ExportHook exportHook = new ExportHook();
        String type = "png";
        String description = "PNG Image";
        String nameExtension = ".png";
        File file = exportHook.chooseFile(type, description, nameExtension);
        
        assertNotNull(file);
        assertEquals(type, Tools.getExtension(file.getName()));
    }

    @Test
    public void testChooseImageFile() {
        ExportHook exportHook = new ExportHook();
        String type = "png";
        String description = "PNG Image";
        String nameExtension = ".png";
        ModeController controller = new ModeController();
        File file = ExportHook.chooseImageFile(type, description, nameExtension, controller);
        
        assertNotNull(file);
        assertEquals(type, Tools.getExtension(file.getName()));
    }

    @Test
    public void testGetTranslatableResourceString() {
        ExportHook exportHook = new ExportHook();
        String resourceName = "testResource";
        String expectedResult = "Test Resource";
        String result = exportHook.getTranslatableResourceString(resourceName);
        
        assertEquals(expectedResult, result);
    }

    @Test
    public void testCreateBufferedImage() {
        ExportHook exportHook = new ExportHook();
        BufferedImage image = exportHook.createBufferedImage();
        
        assertNotNull(image);
    }

    @Test
    public void testCopyFromResource() throws IOException {
        ExportHook exportHook = new ExportHook();
        String prefix = "resources/";
        String fileName = "testfile.txt";
        String destinationDirectory = "destination/";
        File destinationFile = new File(destinationDirectory + fileName);
        
        exportHook.copyFromResource(prefix, fileName, destinationDirectory);
        
        assertTrue(destinationFile.exists());
        destinationFile.delete();
    }

    @Test
    public void testCopyFromFile() throws IOException {
        ExportHook exportHook = new ExportHook();
        String dir = "source/";
        String fileName = "testfile.txt";
        String destinationDirectory = "destination/";
        File sourceFile = new File(dir + fileName);
        File destinationFile = new File(destinationDirectory + fileName);
        
        exportHook.copyFromFile(dir, fileName, destinationDirectory);
        
        assertTrue(destinationFile.exists());
        destinationFile.delete();
    }

    @Test
    public void testImageFilterAccept() {
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("png", "PNG Image");
        File file = new File("test.png");
        
        assertTrue(imageFilter.accept(file));
    }

    @Test
    public void testImageFilterGetDescription() {
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("png", "PNG Image");
        String expectedDescription = "PNG Image";
        String description = imageFilter.getDescription();
        
        assertEquals(expectedDescription, description);
    }
}