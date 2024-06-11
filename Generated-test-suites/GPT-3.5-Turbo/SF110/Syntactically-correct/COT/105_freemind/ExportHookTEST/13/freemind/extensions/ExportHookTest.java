import org.junit.Before;
import org.junit.Test;
import javax.swing.filechooser.FileFilter;
import static org.junit.Assert.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.MessageFormat;

public class ExportHookTest {

    private ExportHook exportHook;

    @Before
    public void setUp() {
        exportHook = new ExportHook();
    }

    @Test
    public void testChooseFile() {
        String type = "jpg";
        String description = "JPEG Image";
        String nameExtension = "_test";
        File file = exportHook.chooseFile(type, description, nameExtension);
        assertNotNull(file);
        assertEquals("jpg", Tools.getExtension(file.getName()));
    }

    @Test
    public void testChooseImageFile() {
        String type = "jpg";
        String description = "JPEG Image";
        String nameExtension = "_test";
        ModeController controller = new ModeController();
        Container component = new Container();
        controller.setFrame(new JFrame());
        controller.getFrame().setContentPane(component);
        File file = ExportHook.chooseImageFile(type, description, nameExtension, controller);
        assertNotNull(file);
        assertEquals("jpg", Tools.getExtension(file.getName()));
    }

    @Test
    public void testGetTranslatableResourceString() {
        String resourceName = "resource_name_test";
        String returnValue = exportHook.getTranslatableResourceString(resourceName);
        assertNotNull(returnValue);
        assertTrue(returnValue.startsWith("%"));
        assertEquals("Resource Value Test", exportHook.getController().getText(returnValue.substring(1)));
    }

    @Test
    public void testCreateBufferedImage() {
        BufferedImage image = exportHook.createBufferedImage();
        assertNotNull(image);
        assertEquals(800, image.getWidth());
        assertEquals(600, image.getHeight());
    }

    @Test
    public void testCopyFromResource() {
        String prefix = "prefix/";
        String fileName = "file_test.png";
        String destinationDirectory = "destination";
        exportHook.copyFromResource(prefix, fileName, destinationDirectory);
        File copiedFile = new File(destinationDirectory + "/" + fileName);
        assertTrue(copiedFile.exists());
    }

    @Test
    public void testCopyFromFile() {
        String dir = "directory/";
        String fileName = "file_test.png";
        String destinationDirectory = "destination";
        exportHook.copyFromFile(dir, fileName, destinationDirectory);
        File copiedFile = new File(destinationDirectory + "/" + fileName);
        assertTrue(copiedFile.exists());
    }

    @Test
    public void testImageFilterAccept() {
        FileFilter filter = new ExportHook.ImageFilter("jpg", "JPEG Image");
        File jpgFile = new File("image.jpg");
        assertTrue(filter.accept(jpgFile));
        File pngFile = new File("image.png");
        assertFalse(filter.accept(pngFile));
        File directory = new File("dir");
        assertTrue(filter.accept(directory));
    }

    @Test
    public void testImageFilterGetDescription() {
        FileFilter filter = new ExportHook.ImageFilter("jpg", "JPEG Image");
        assertEquals("JPEG Image", filter.getDescription());
    }
}

