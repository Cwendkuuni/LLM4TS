import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.awt.*;
import javax.swing.*;

public class ExportHookTest {
    @Test
    public void testChooseFileTypical() {
        ExportHook exportHook = new ExportHook();
        File file = exportHook.chooseFile("png", "PNG Image", "_exported");
        assertNotNull(file);
        // verify the file properties
        assertEquals("png", Tools.getExtension(file.getName()));
        assertTrue(file.exists());
    }
    
    @Test
    public void testChooseFileNullReturnValue() {
        ExportHook exportHook = new ExportHook();
        File file = exportHook.chooseFile("jpg", "JPG Image", null);
        assertNull(file);
    }
    
    @Test
    public void testChooseImageFileTypical() {
        String type = "png";
        String description = "PNG Image";
        String nameExtension = "_exported";
        ModeController controller = new ModeController();
        
        File file = ExportHook.chooseImageFile(type, description, nameExtension, controller);
        assertNotNull(file);
        // verify the file properties
        assertEquals("png", Tools.getExtension(file.getName()));
        assertTrue(file.exists());
    }
    
    @Test
    public void testChooseImageFileNullReturnValue() {
        String type = "jpg";
        String description = "JPG Image";
        String nameExtension = null;
        ModeController controller = new ModeController();
        
        File file = ExportHook.chooseImageFile(type, description, nameExtension, controller);
        assertNull(file);
    }
    
    @Test
    public void testGetTranslatableResourceStringTypical() {
        ExportHook exportHook = new ExportHook();
        String resourceName = "key";
        String expectedValue = "value";
        exportHook.addResourceString(resourceName, "%" + resourceName);
        exportHook.getController().addText(resourceName, expectedValue);
        
        String actualValue = exportHook.getTranslatableResourceString(resourceName);
        assertEquals(expectedValue, actualValue);
    }
    
    @Test
    public void testGetTranslatableResourceStringNoTranslation() {
        ExportHook exportHook = new ExportHook();
        String resourceName = "key";
        String expectedValue = "value";
        exportHook.addResourceString(resourceName, expectedValue);
        
        String actualValue = exportHook.getTranslatableResourceString(resourceName);
        assertEquals(expectedValue, actualValue);
    }
    
    @Test
    public void testCreateBufferedImageTypical() {
        ExportHook exportHook = new ExportHook();
        ModeControllerMock modeController = new ModeControllerMock();
        exportHook.setController(modeController);
        
        BufferedImage image = exportHook.createBufferedImage();
        assertNotNull(image);
        // verify the image properties
        assertEquals(800, image.getWidth());
        assertEquals(600, image.getHeight());
    }
    
    @Test
    public void testCreateBufferedImageNullReturnValue() {
        ExportHook exportHook = new ExportHook();
        ModeControllerMock modeController = new ModeControllerMock();
        modeController.setView(null);
        exportHook.setController(modeController);
        
        BufferedImage image = exportHook.createBufferedImage();
        assertNull(image);
    }
    
    @Test
    public void testCopyFromResourceTypical() {
        ExportHook exportHook = new ExportHook();
        String prefix = "resources/";
        String fileName = "resource.txt";
        String destinationDirectory = "destination";
        exportHook.setLogger(new LoggerMock());
        
        exportHook.copyFromResource(prefix, fileName, destinationDirectory);
        File destinationFile = new File(destinationDirectory, fileName);
        assertTrue(destinationFile.exists());
    }
    
    @Test
    public void testCopyFromResourceFileNotFound() {
        ExportHook exportHook = new ExportHook();
        String prefix = "resources/";
        String fileName = "nonexistent.txt";
        String destinationDirectory = "destination";
        LoggerMock logger = new LoggerMock();
        exportHook.setLogger(logger);
        
        exportHook.copyFromResource(prefix, fileName, destinationDirectory);
        logger.verifySevere("Cannot find resource: " + prefix + fileName);
    }
    
    @Test
    public void testCopyFromFileTypical() {
        ExportHook exportHook = new ExportHook();
        String dir = "source";
        String fileName = "file.txt";
        String destinationDirectory = "destination";
        exportHook.setLogger(new LoggerMock());
        
        exportHook.copyFromFile(dir, fileName, destinationDirectory);
        File destinationFile = new File(destinationDirectory, fileName);
        assertTrue(destinationFile.exists());
    }
    
    @Test
    public void testCopyFromFileFileNotFound() {
        ExportHook exportHook = new ExportHook();
        String dir = "source";
        String fileName = "nonexistent.txt";
        String destinationDirectory = "destination";
        LoggerMock logger = new LoggerMock();
        exportHook.setLogger(logger);
        
        exportHook.copyFromFile(dir, fileName, destinationDirectory);
        logger.verifySevere("Cannot find resource: " + dir + fileName);
    }
    
    @Test
    public void testImageFilterAcceptDirectory() {
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("png", "PNG Image");
        File directory = new File("directory");
        
        boolean accept = imageFilter.accept(directory);
        assertTrue(accept);
    }
    
    @Test
    public void testImageFilterAcceptMatchingExtension() {
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("png", "PNG Image");
        File file = new File("image.png");
        
        boolean accept = imageFilter.accept(file);
        assertTrue(accept);
    }
    
    @Test
    public void testImageFilterNotAcceptingNonMatchingExtension() {
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("png", "PNG Image");
        File file = new File("image.jpg");
        
        boolean accept = imageFilter.accept(file);
        assertFalse(accept);
    }
    
    @Test
    public void testImageFilterGetDescription() {
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("png", "PNG Image");
        
        String description = imageFilter.getDescription();
        assertEquals("PNG Image", description);
    }
    
    @Test
    public void testImageFilterGetDescriptionNoCustomDescription() {
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("png", null);
        
        String description = imageFilter.getDescription();
        assertEquals("png", description);
    }
}