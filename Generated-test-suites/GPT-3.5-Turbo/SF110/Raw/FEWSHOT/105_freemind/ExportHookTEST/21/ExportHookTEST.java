package freemind.extensions;

import static org.junit.Assert.*;
import org.junit.Test;
import freemind.modes.*;
import java.io.File;
import javax.swing.filechooser.FileFilter;

public class ExportHookTest {
    
    @Test
    public void testChooseFile() {
        ExportHook exportHook = new ExportHook();
        ModeController controller = new ModeController();
        exportHook.setController(controller);
        
        File result1 = exportHook.chooseFile("jpg", "Image File", ".jpg");
        assertNull(result1);
        
        File result2 = exportHook.chooseFile("png", "Image File", null);
        assertNull(result2);
        
        File result3 = exportHook.chooseFile("pdf", "PDF File", ".pdf");
        assertNull(result3);
    }
    
    @Test
    public void testChooseImageFile() {
        ModeController controller = new ModeController();
        
        File result1 = ExportHook.chooseImageFile("jpg", "Image File", ".jpg", controller);
        assertNull(result1);
        
        File result2 = ExportHook.chooseImageFile("png", "Image File", null, controller);
        assertNull(result2);
        
        File result3 = ExportHook.chooseImageFile("pdf", "PDF File", ".pdf", controller);
        assertNull(result3);
    }
    
    @Test
    public void testGetTranslatableResourceString() {
        ExportHook exportHook = new ExportHook();
        exportHook.setController(new ModeController());
        
        String result = exportHook.getTranslatableResourceString("resource1");
        assertNull(result);
        
        String result2 = exportHook.getTranslatableResourceString("resource2");
        assertNull(result2);
    }
    
    @Test
    public void testCreateBufferedImage() {
        ExportHook exportHook = new ExportHook();
        exportHook.setController(new ModeController());
        exportHook.getController().setView(new MapView());

        BufferedImage result = exportHook.createBufferedImage();
        assertNull(result);
    }
    
    @Test
    public void testCopyFromResource() {
        ExportHook exportHook = new ExportHook();
        exportHook.setLogger(Logger.getLogger(ExportHook.class.getName()));
        
        exportHook.copyFromResource("prefix1", "file1", "destination1");
        // validate that the file has been copied to destinationDirectory
        
        exportHook.copyFromResource("prefix2", "file2", "destination2");
        // validate that the file has been copied to destinationDirectory
    }
    
    @Test
    public void testCopyFromFile() {
        ExportHook exportHook = new ExportHook();
        exportHook.setLogger(Logger.getLogger(ExportHook.class.getName()));
        
        exportHook.copyFromFile("dir1", "file1", "destination1");
        // validate that the file has been copied to destinationDirectory
        
        exportHook.copyFromFile("dir2", "file2", "destination2");
        // validate that the file has been copied to destinationDirectory
    }
    
    @Test
    public void testImageFilter() {
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("jpg", "Image File");
        
        File file1 = new File("test.jpg");
        assertTrue(imageFilter.accept(file1));
        
        File file2 = new File("test.png");
        assertFalse(imageFilter.accept(file2));
        
        String description = imageFilter.getDescription();
        assertEquals("Image File", description);
    }
}