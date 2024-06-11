import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;
import java.awt.*;
import java.awt.image.*;
import java.text.*;
import javax.swing.*;
import javax.swing.filechooser.*;

public class ExportHookTest {

    private ExportHook exportHook; 

    @Before
    public void setUp() {
        exportHook = new ExportHook();
    }

    @After
    public void tearDown() {
        exportHook = null;
    }

    @Test
    public void testChooseFile() {
        File file = exportHook.chooseFile("jpg", "JPEG Image", ".jpg");
        assertNotNull(file);
    }

    @Test
    public void testChooseImageFile() {
        ModeController modeController = new ModeController();
        File file = ExportHook.chooseImageFile("jpg", "JPEG Image", ".jpg", modeController);
        assertNotNull(file);
    }

    @Test
    public void testGetTranslatableResourceString() {
        String resourceName = "resource";
        String translatableResource = exportHook.getTranslatableResourceString(resourceName);
        assertNotNull(translatableResource);
    }

    @Test
    public void testCreateBufferedImage() {
        BufferedImage image = exportHook.createBufferedImage();
        assertNotNull(image);
        assertEquals(image.getWidth(), 0);
        assertEquals(image.getHeight(), 0);
    }

    @Test
    public void testCopyFromResource() {
        String prefix = "";
        String fileName = "file.txt";
        String destinationDirectory = "destination";
        exportHook.copyFromResource(prefix, fileName, destinationDirectory);
        File file = new File(destinationDirectory + "/" + fileName);
        assertTrue(file.exists());
    }

    @Test
    public void testCopyFromFile() {
        String dir = "source";
        String fileName = "file.txt";
        String destinationDirectory = "destination";
        exportHook.copyFromFile(dir, fileName, destinationDirectory);
        File file = new File(destinationDirectory + "/" + fileName);
        assertTrue(file.exists());
    }

    @Test
    public void testImageFilterAccept() {
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("jpg", "JPEG Image");
        File file = new File("image.jpg");
        assertTrue(imageFilter.accept(file));
    }

    @Test
    public void testImageFilterGetDescription() {
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("jpg", "JPEG Image");
        String description = imageFilter.getDescription();
        assertNotNull(description);
        assertEquals(description, "JPEG Image");
    }

}