import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import freemind.modes.ModeController;
import freemind.main.FreeMindFrame;

public class ExportHookTest {
    
    private ExportHook exportHook;
    private ModeController modeController;
    
    @Before
    public void setUp() {
        exportHook = new ExportHook();
        modeController = new ModeController();
        FreeMindFrame frame = new FreeMindFrame();
        modeController.setFrame(frame);
    }
    
    @Test
    public void testChooseFile_typicalCase() {
        String type = "jpg";
        String description = "Image Files";
        String nameExtension = "_export";
        
        File result = exportHook.chooseFile(type, description, nameExtension);
        
        assertNotNull(result);
        assertEquals(".jpg", Tools.getExtension(result.getName()));
    }
    
    @Test
    public void testChooseFile_mmFileNull() {
        String type = "png";
        String description = "Image Files";
        String nameExtension = null;
        
        File result = exportHook.chooseFile(type, description, nameExtension);
        
        assertNotNull(result);
        assertEquals(".png", Tools.getExtension(result.getName()));
    }
    
    @Test
    public void testChooseFile_returnValNotZero() {
        String type = "bmp";
        String description = "Image Files";
        String nameExtension = "_export";
        
        File result = exportHook.chooseFile(type, description, nameExtension);
        
        assertNull(result);
    }
    
    @Test
    public void testChooseImageFile_safeEqualsIgnoreCaseFalse() {
        String type = "jpeg";
        String description = "Image Files";
        String nameExtension = "_export";
        
        File result = ExportHook.chooseImageFile(type, description, nameExtension, modeController);
        
        assertNotNull(result);
        assertEquals(".jpeg", Tools.getExtension(result.getName()));
    }
    
    @Test
    public void testChooseImageFile_safeEqualsIgnoreCaseTrue() {
        String type = "png";
        String description = "Image Files";
        String nameExtension = "_export";
        
        File result = ExportHook.chooseImageFile(type, description, nameExtension, modeController);
        
        assertNotNull(result);
        assertEquals(".png", Tools.getExtension(result.getName()));
    }
    
    @Test
    public void testChooseImageFile_returnValNotZero() {
        String type = "bmp";
        String description = "Image Files";
        String nameExtension = "_export";
        
        File result = ExportHook.chooseImageFile(type, description, nameExtension, modeController);
        
        assertNull(result);
    }
    
    @Test
    public void testGetTranslatableResourceString_typicalCase() {
        String resourceName = "resource_name";
        
        String result = exportHook.getTranslatableResourceString(resourceName);
        
        assertNotNull(result);
        assertFalse(result.startsWith("%"));
    }
    
    @Test
    public void testGetTranslatableResourceString_resourceNull() {
        String resourceName = "non_existent_resource";
        
        String result = exportHook.getTranslatableResourceString(resourceName);
        
        assertNull(result);
    }
    
    @Test
    public void testCreateBufferedImage_viewNull() {
        BufferedImage result = exportHook.createBufferedImage();
        
        assertNull(result);
    }
    
    @Test
    public void testCreateBufferedImage_viewNotNull() {
        modeController.createView();
        
        BufferedImage result = exportHook.createBufferedImage();
        
        assertNotNull(result);
        assertEquals(modeController.getView().getWidth(), result.getWidth());
        assertEquals(modeController.getView().getHeight(), result.getHeight());
    }
    
    @Test
    public void testCopyFromResource_resourceExists() {
        String prefix = "prefix/";
        String fileName = "file.txt";
        String destinationDirectory = "destination_directory";
        
        exportHook.copyFromResource(prefix, fileName, destinationDirectory);
        
        File destinationFile = new File(destinationDirectory + "/" + fileName);
        assertTrue(destinationFile.exists());
    }
    
    @Test
    public void testCopyFromResource_resourceNotExists() {
        String prefix = "prefix/";
        String fileName = "non_existent_file.txt";
        String destinationDirectory = "destination_directory";
        
        exportHook.copyFromResource(prefix, fileName, destinationDirectory);
        
        File destinationFile = new File(destinationDirectory + "/" + fileName);
        assertFalse(destinationFile.exists());
    }
    
    @Test
    public void testCopyFromFile_resourceExists() {
        String dir = "directory";
        String fileName = "file.txt";
        String destinationDirectory = "destination_directory";
        
        exportHook.copyFromFile(dir, fileName, destinationDirectory);
        
        File destinationFile = new File(destinationDirectory + "/" + fileName);
        assertTrue(destinationFile.exists());
    }
    
    @Test
    public void testCopyFromFile_resourceNotExists() {
        String dir = "non_existent_directory";
        String fileName = "file.txt";
        String destinationDirectory = "destination_directory";
        
        exportHook.copyFromFile(dir, fileName, destinationDirectory);
        
        File destinationFile = new File(destinationDirectory + "/" + fileName);
        assertFalse(destinationFile.exists());
    }
    
    @Test
    public void testImageFilter_acceptDirectory() {
        File file = new File("directory");
        ImageFilter imageFilter = new ImageFilter("jpg", null);
        
        boolean result = imageFilter.accept(file);
        
        assertTrue(result);
    }
    
    @Test
    public void testImageFilter_acceptMatchingExtension() {
        File file = new File("file.jpg");
        ImageFilter imageFilter = new ImageFilter("jpg", null);
        
        boolean result = imageFilter.accept(file);
        
        assertTrue(result);
    }
    
    @Test
    public void testImageFilter_acceptNonMatchingExtension() {
        File file = new File("file.png");
        ImageFilter imageFilter = new ImageFilter("jpg", null);
        
        boolean result = imageFilter.accept(file);
        
        assertFalse(result);
    }
    
    @Test
    public void testImageFilter_getDescriptionNonNull() {
        String type = "jpg";
        String description = "Image Files";
        ImageFilter imageFilter = new ImageFilter(type, description);
        
        String result = imageFilter.getDescription();
        
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(description, result);
    }
    
    @Test
    public void testImageFilter_getDescriptionNull() {
        String type = "jpg";
        ImageFilter imageFilter = new ImageFilter(type, null);
        
        String result = imageFilter.getDescription();
        
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(type, result);
    }
}