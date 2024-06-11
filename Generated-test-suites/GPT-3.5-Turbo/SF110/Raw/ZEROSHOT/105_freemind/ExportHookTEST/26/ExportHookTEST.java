import freemind.extensions.ExportHook;
import freemind.modes.ModeController;
import freemind.view.mindmapview.MapView;
import org.junit.Before;
import org.junit.Test;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.MessageFormat;
import java.util.logging.Logger;
import static org.junit.Assert.*;

public class ExportHookTest {
    private ExportHook exportHook;
    
    @Before
    public void setUp() {
        exportHook = new ExportHook();
    }
    
    @Test
    public void testChooseFile() {
        String type = "png";
        String description = "PNG File";
        String nameExtension = ".png";
        
        File result = exportHook.chooseFile(type, description, nameExtension);
        
        assertNotNull(result);
        assertEquals(nameExtension, result.getName().substring(result.getName().lastIndexOf(".")));
        
        type = "jpg";
        description = "JPG File";
        nameExtension = ".jpg";
        
        result = exportHook.chooseFile(type, description, nameExtension);
        
        assertNotNull(result);
        assertEquals(nameExtension, result.getName().substring(result.getName().lastIndexOf(".")));
    }
    
    @Test
    public void testChooseImageFile() {
        ModeController controller = new ModeController();
        controller.getFrame().setContentPane(new Container());
        String type = "png";
        String description = "PNG File";
        String nameExtension = ".png";
        
        File result = ExportHook.chooseImageFile(type, description, nameExtension, controller);
        
        assertNotNull(result);
        assertEquals(nameExtension, result.getName().substring(result.getName().lastIndexOf(".")));
        
        type = "jpg";
        description = "JPG File";
        nameExtension = ".jpg";
        
        result = ExportHook.chooseImageFile(type, description, nameExtension, controller);
        
        assertNotNull(result);
        assertEquals(nameExtension, result.getName().substring(result.getName().lastIndexOf(".")));
    }
    
    @Test
    public void testGetTranslatableResourceString() {
        String resourceName = "test";
        
        String result = exportHook.getTranslatableResourceString(resourceName);
        
        assertNull(result);
    }
    
    @Test
    public void testCreateBufferedImage() {
        ModeController controller = new ModeController();
        MapView mapView = new MapView(controller);
        controller.setView(mapView);
        
        BufferedImage result = exportHook.createBufferedImage();
        
        assertNotNull(result);
    }
    
    @Test
    public void testCopyFromResource() {
        String prefix = "images/";
        String fileName = "image.jpg";
        String destinationDirectory = "C:/temp";
        
        exportHook.copyFromResource(prefix, fileName, destinationDirectory);
        
        File copiedFile = new File(destinationDirectory + "/" + fileName);
        
        assertTrue(copiedFile.exists());
    }
    
    @Test
    public void testCopyFromFile() {
        String dir = "C:/temp";
        String fileName = "image.jpg";
        String destinationDirectory = "C:/temp2";
        
        exportHook.copyFromFile(dir, fileName, destinationDirectory);
        
        File copiedFile = new File(destinationDirectory + "/" + fileName);
        
        assertTrue(copiedFile.exists());
    }
    
    @Test
    public void testImageFilter() {
        String type = "png";
        String description = "PNG File";
        
        ExportHook.ImageFilter imageFilter = exportHook.new ImageFilter(type, description);
        
        File pngFile = new File("C:/temp/image.png");
        File jpgFile = new File("C:/temp/image.jpg");
        File dir = new File("C:/temp");
        
        assertTrue(imageFilter.accept(pngFile));
        assertFalse(imageFilter.accept(jpgFile));
        assertTrue(imageFilter.accept(dir));
        
        assertEquals(description, imageFilter.getDescription());
    }
}