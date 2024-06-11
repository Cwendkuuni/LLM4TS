package freemind.extensions;

import org.junit.Test;
import javax.swing.*;
import java.io.File;
import java.awt.image.BufferedImage;

public class ExportHookTest {

    @Test
    public void testChooseFile() {
        ExportHook exportHook = new ExportHook();
        
        // Typical use case
        String type = "PNG";
        String description = "PNG Image";
        String nameExtension = "_export";
        File chosenFile = exportHook.chooseFile(type, description, nameExtension);
        assertNotNull(chosenFile);
        
        // Edge case - type is null
        type = null;
        description = "PNG Image";
        nameExtension = "_export";
        chosenFile = exportHook.chooseFile(type, description, nameExtension);
        assertNull(chosenFile);
        
        // Error scenario - no chosen file
        type = "PDF";
        description = "PDF Document";
        nameExtension = "_export";
        chosenFile = exportHook.chooseFile(type, description, nameExtension);
        assertNull(chosenFile);
    }
    
    @Test
    public void testChooseImageFile() {
        ExportHook exportHook = new ExportHook();
        ModeController controller = new ModeController();
        
        // Typical use case
        String type = "JPEG";
        String description = "JPEG Image";
        String nameExtension = "_export";
        File chosenFile = ExportHook.chooseImageFile(type, description, nameExtension, controller);
        assertNotNull(chosenFile);
        
        // Edge case - type is null
        type = null;
        description = "JPEG Image";
        nameExtension = "_export";
        chosenFile = ExportHook.chooseImageFile(type, description, nameExtension, controller);
        assertNull(chosenFile);
        
        // Error scenario - no chosen file
        type = "GIF";
        description = "GIF Image";
        nameExtension = "_export";
        chosenFile = ExportHook.chooseImageFile(type, description, nameExtension, controller);
        assertNull(chosenFile);
    }
    
    @Test
    public void testGetTranslatableResourceString() {
        ExportHook exportHook = new ExportHook();
        
        // Typical use case
        String resourceName = "resource_1";
        String translatableString = exportHook.getTranslatableResourceString(resourceName);
        assertNotNull(translatableString);
        
        // Edge case - resourceName is null
        resourceName = null;
        translatableString = exportHook.getTranslatableResourceString(resourceName);
        assertNull(translatableString);
        
        // Error scenario - resource not found
        resourceName = "resource_3";
        translatableString = exportHook.getTranslatableResourceString(resourceName);
        assertNotNull(translatableString);
    }
    
    @Test
    public void testCreateBufferedImage() {
        ExportHook exportHook = new ExportHook();
        
        // Typical use case
        BufferedImage bufferedImage = exportHook.createBufferedImage();
        assertNotNull(bufferedImage);
        
        // Error scenario - view is null
        exportHook.view = null;
        bufferedImage = exportHook.createBufferedImage();
        assertNull(bufferedImage);
    }
    
    @Test
    public void testCopyFromResource() {
        ExportHook exportHook = new ExportHook();
        
        // Typical use case
        String prefix = "resources/";
        String fileName = "file_1.txt";
        String destinationDirectory = "destination";
        exportHook.copyFromResource(prefix, fileName, destinationDirectory);
        
        // Edge case - resource not found
        prefix = "resources/";
        fileName = "file_3.txt";
        destinationDirectory = "destination";
        exportHook.copyFromResource(prefix, fileName, destinationDirectory);
        
        // Error scenario - file not found or could not be copied
        prefix = "resources/";
        fileName = "file_2.txt";
        destinationDirectory = "destination";
        exportHook.copyFromResource(prefix, fileName, destinationDirectory);
    }
    
    @Test
    public void testCopyFromFile() {
        ExportHook exportHook = new ExportHook();
        
        // Typical use case
        String dir = "resources";
        String fileName = "file_1.pdf";
        String destinationDirectory = "destination";
        exportHook.copyFromFile(dir, fileName, destinationDirectory);
        
        // Edge case - file not found
        dir = "resources";
        fileName = "file_3.pdf";
        destinationDirectory = "destination";
        exportHook.copyFromFile(dir, fileName, destinationDirectory);
        
        // Error scenario - file not found or could not be copied
        dir = "resources";
        fileName = "file_2.pdf";
        destinationDirectory = "destination";
        exportHook.copyFromFile(dir, fileName, destinationDirectory);
    }
    
    @Test
    public void testImageFilter() {
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("PNG", "PNG Image");
        
        // Typical use case
        File file = new File("image.png");
        assertTrue(imageFilter.accept(file));
        
        // Edge case - file is directory
        file = new File("resources");
        assertTrue(imageFilter.accept(file));
        
        // Error scenario - invalid extension
        file = new File("image.jpg");
        assertFalse(imageFilter.accept(file));
    }
}