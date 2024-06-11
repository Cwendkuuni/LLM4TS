
import org.junit.Before;
import org.junit.Test;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import static org.junit.Assert.*;

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
        String nameExtension = ".jpg";
        File chosenFile = exportHook.chooseFile(type, description, nameExtension);
        assertNotNull(chosenFile);
        assertEquals(type, Tools.getExtension(chosenFile.getName()));
    }

    @Test
    public void testGetTranslatableResourceString() {
        String resourceName = "resource_name";
        String expected = exportHook.getController().getText(resourceName);
        String result = exportHook.getTranslatableResourceString(resourceName);
        assertEquals(expected, result);
    }

    @Test
    public void testCreateBufferedImage() {
        BufferedImage bufferedImage = exportHook.createBufferedImage();
        assertNotNull(bufferedImage);
        assertEquals(exportHook.getController().getView().getInnerBounds().width, bufferedImage.getWidth());
        assertEquals(exportHook.getController().getView().getInnerBounds().height, bufferedImage.getHeight());
    }

    @Test
    public void testCopyFromResource() {
        String prefix = "prefix/";
        String fileName = "file.txt";
        String destinationDirectory = "destination";
        exportHook.copyFromResource(prefix, fileName, destinationDirectory);
        File copiedFile = new File(destinationDirectory + "/" + fileName);
        assertTrue(copiedFile.exists());
    }

    @Test
    public void testCopyFromFile() {
        String dir = "dir";
        String fileName = "file.txt";
        String destinationDirectory = "destination";
        exportHook.copyFromFile(dir, fileName, destinationDirectory);
        File copiedFile = new File(destinationDirectory + "/" + fileName);
        assertTrue(copiedFile.exists());
    }

    @Test
    public void testImageFilterAccept() {
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("jpg", "JPEG Image");
        File directory = new File(".");
        assertTrue(imageFilter.accept(directory));
        assertTrue(imageFilter.accept(new File("image.jpg")));
        assertFalse(imageFilter.accept(new File("image.png")));
    }

    @Test
    public void testImageFilterGetDescription() {
        String type = "jpg";
        String description = "JPEG Image";
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter(type, description);
        assertEquals(description, imageFilter.getDescription());
    }
}
