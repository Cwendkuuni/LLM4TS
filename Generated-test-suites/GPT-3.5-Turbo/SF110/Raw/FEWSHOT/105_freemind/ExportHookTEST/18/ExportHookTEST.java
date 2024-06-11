package freemind.extensions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.io.File;

public class ExportHookTest {
    private ExportHook exportHook;

    @Before
    public void setUp() {
        exportHook = new ExportHook();
    }

    @Test
    public void testChooseFile() {
        String type = "png";
        String description = "PNG Image";
        String nameExtension = "_export";

        File file = exportHook.chooseFile(type, description, nameExtension);

        assertNotNull(file);
        assertTrue(file.getName().endsWith(".png"));
    }

    @Test
    public void testChooseImageFile() {
        String type = "jpg";
        String description = "JPEG Image";
        String nameExtension = "_export";
        ModeController controller = null; // Provide a valid instance of ModeController

        File file = ExportHook.chooseImageFile(type, description, nameExtension, controller);

        assertNotNull(file);
        assertTrue(file.getName().endsWith(".jpg"));
    }

    @Test
    public void testGetTranslatableResourceString() {
        String resourceName = "%export_text";

        String translatableResourceString = exportHook.getTranslatableResourceString(resourceName);

        assertNotNull(translatableResourceString);
        assertTrue(translatableResourceString.startsWith("export_text")); // Verify that the returned string is translated
    }

    @Test
    public void testCreateBufferedImage() {
        BufferedImage bufferedImage = exportHook.createBufferedImage();

        assertNotNull(bufferedImage);
        assertEquals(0, bufferedImage.getWidth());
        assertEquals(0, bufferedImage.getHeight());
    }

    @Test
    public void testCopyFromResource() {
        String prefix = "resources/";
        String fileName = "logo.png";
        String destinationDirectory = "images/";

        exportHook.copyFromResource(prefix, fileName, destinationDirectory);

        File copiedFile = new File(destinationDirectory, fileName);

        assertTrue(copiedFile.exists());
        assertTrue(copiedFile.isFile());
    }

    @Test
    public void testCopyFromFile() {
        String dir = "src/test/resources/";
        String fileName = "test.txt";
        String destinationDirectory = "files/";

        exportHook.copyFromFile(dir, fileName, destinationDirectory);

        File copiedFile = new File(destinationDirectory, fileName);

        assertTrue(copiedFile.exists());
        assertTrue(copiedFile.isFile());
    }
}