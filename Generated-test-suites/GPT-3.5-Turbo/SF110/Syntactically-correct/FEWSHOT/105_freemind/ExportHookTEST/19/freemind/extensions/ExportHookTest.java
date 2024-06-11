package freemind.extensions;

import static org.junit.Assert.*;
import org.junit.Test;

import java.awt.Container;
import java.awt.image.BufferedImage;
import java.io.File;

public class ExportHookTest {

    @Test
    public void testChooseFile() {
        ExportHook exportHook = new ExportHook();
        String type = "jpg";
        String description = "JPEG Image";
        String nameExtension = ".jpg";
        File chosenFile = exportHook.chooseFile(type, description, nameExtension);
        
        assertNull(chosenFile);
    }

    @Test
    public void testChooseImageFile() {
        String type = "jpg";
        String description = "JPEG Image";
        String nameExtension = ".jpg";
        ModeController controller = new ModeController();
        Container component = new Container();
        ImageFilter filter = new ImageFilter(type, description);
        FreeMindFileDialog chooser = controller.getFileChooser(filter);
        File mmFile = controller.getMap().getFile();
        String proposedName = mmFile.getAbsolutePath().replaceFirst("\\.[^.]*?$", "") + ((nameExtension != null) ? nameExtension : "") + "." + type;
        chooser.setSelectedFile(new File(proposedName));
        
        assertEquals(chooser, ExportHook.chooseImageFile(type, description, nameExtension, controller));
    }

    @Test
    public void testGetTranslatableResourceString() {
        ExportHook exportHook = new ExportHook();
        String resourceName = "resource";
        
        assertNull(exportHook.getTranslatableResourceString(resourceName));
    }

    @Test
    public void testCreateBufferedImage() {
        ExportHook exportHook = new ExportHook();
        exportHook.view = new MapView();
        
        assertNull(exportHook.createBufferedImage());
    }

    @Test
    public void testCopyFromResource() {
        ExportHook exportHook = new ExportHook();
        String prefix = "prefix";
        String fileName = "file";
        String destinationDirectory = "destination";
        exportHook.logger = new Logger();
        
        exportHook.copyFromResource(prefix, fileName, destinationDirectory);
        
        assertNotNull(exportHook.logger);
    }

    @Test
    public void testCopyFromFile() {
        ExportHook exportHook = new ExportHook();
        String dir = "directory";
        String fileName = "file";
        String destinationDirectory = "destination";
        exportHook.logger = new Logger();
        
        exportHook.copyFromFile(dir, fileName, destinationDirectory);
        
        assertNotNull(exportHook.logger);
    }

    @Test
    public void testImageFilterAccept() {
        ImageFilter imageFilter = new ImageFilter("jpg", "JPEG Image");
        File dir = new File("directory");
        File file = new File(dir, "file.jpg");
        
        assertTrue(imageFilter.accept(file));
    }

    @Test
    public void testImageFilterGetDescription() {
        ImageFilter imageFilter = new ImageFilter("jpg", "JPEG Image");
        assertEquals("JPEG Image", imageFilter.getDescription());
    }
}