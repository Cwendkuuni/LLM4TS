import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;

public class ExportHookTest {
    
    @Test
    public void testChooseFile() {
        ExportHook exportHook = new ExportHook();
        String type = "png";
        String description = "PNG Image";
        String nameExtension = ".png";
        
        File file = exportHook.chooseFile(type, description, nameExtension);
        assertNotNull(file);
    }
    
    @Test
    public void testChooseImageFile() {
        ExportHook exportHook = new ExportHook();
        String type = "png";
        String description = "PNG Image";
        String nameExtension = ".png";
        
        File file = ExportHook.chooseImageFile(type, description, nameExtension, null);
        assertNotNull(file);
    }
    
    @Test
    public void testGetTranslatableResourceString() {
        ExportHook exportHook = new ExportHook();
        String resourceName = "file_name";
        
        String translatableResourceString = exportHook.getTranslatableResourceString(resourceName);
        assertNotNull(translatableResourceString);
    }
    
    @Test
    public void testCreateBufferedImage() {
        ExportHook exportHook = new ExportHook();
        
        BufferedImage image = exportHook.createBufferedImage();
        assertNotNull(image);
    }
    
    @Test
    public void testCopyFromResource() {
        ExportHook exportHook = new ExportHook();
        String prefix = "images/";
        String fileName = "logo.png";
        String destinationDirectory = "resources";
        
        exportHook.copyFromResource(prefix, fileName, destinationDirectory);
        File file = new File(destinationDirectory + "/" + fileName);
        assertTrue(file.exists());
    }
    
    @Test
    public void testCopyFromFile() {
        ExportHook exportHook = new ExportHook();
        String dir = "images";
        String fileName = "logo.png";
        String destinationDirectory = "resources";
        
        exportHook.copyFromFile(dir, fileName, destinationDirectory);
        File file = new File(destinationDirectory + "/" + fileName);
        assertTrue(file.exists());
    }
    
    @Test
    public void testImageFilterAccept() {
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("png", "PNG Image");
        File file = new File("image.png");
        
        assertTrue(imageFilter.accept(file));
    }
    
    @Test
    public void testImageFilterGetDescription() {
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("png", "PNG Image");
        
        assertEquals("PNG Image", imageFilter.getDescription());
    }
}