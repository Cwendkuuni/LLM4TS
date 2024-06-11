import freemind.main.FreeMindFileDialog;
import freemind.modes.ModeController;
import freemind.view.mindmapview.MapView;
import org.junit.Before;
import org.junit.Test;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.text.MessageFormat;

import static org.junit.Assert.*;

public class ExportHookTest {

    private ExportHook exportHook;

    @Before
    public void setUp() {
        exportHook = new ExportHook();
    }

    @Test
    public void testChooseFile() {
        ModeController controller = new ModeController();
        File chosenFile = exportHook.chooseFile("jpg", "JPEG Image", ".jpg");
        assertNotNull(chosenFile);
        assertEquals(".jpg", chosenFile.getName().substring(chosenFile.getName().lastIndexOf(".")));
    }

    @Test
    public void testChooseImageFile() {
        ModeController controller = new ModeController();
        Container component = new Container();
        File chosenFile = exportHook.chooseImageFile("png", "PNG Image", ".png", controller);
        assertNotNull(chosenFile);
        assertEquals(".png", chosenFile.getName().substring(chosenFile.getName().lastIndexOf(".")));
    }

    @Test
    public void testGetTranslatableResourceString() {
        String resourceName = "testResource";
        String returnValue = exportHook.getTranslatableResourceString(resourceName);
        assertEquals(resourceName, returnValue);
    }

    @Test
    public void testCreateBufferedImage() {
        ModeController controller = new ModeController();
        MapView view = new MapView(controller);
        controller.setView(view);
        BufferedImage image = exportHook.createBufferedImage();
        assertNotNull(image);
    }

    @Test
    public void testCopyFromResource() {
        String prefix = "resources/";
        String fileName = "test.txt";
        String destinationDirectory = "dest";
        exportHook.copyFromResource(prefix, fileName, destinationDirectory);
        File copiedFile = new File(destinationDirectory + "/" + fileName);
        assertTrue(copiedFile.exists());
    }

    @Test
    public void testCopyFromFile() {
        String dir = "resources";
        String fileName = "test.txt";
        String destinationDirectory = "dest";
        exportHook.copyFromFile(dir, fileName, destinationDirectory);
        File copiedFile = new File(destinationDirectory + "/" + fileName);
        assertTrue(copiedFile.exists());
    }

    @Test
    public void testImageFilterAccept() {
        ExportHook.ImageFilter imageFilter = exportHook.new ImageFilter("jpg", "JPEG Image");
        File file = new File("image.jpg");
        assertTrue(imageFilter.accept(file));
    }

    @Test
    public void testImageFilterGetDescription() {
        String type = "jpg";
        String description = "JPEG Image";
        ExportHook.ImageFilter imageFilter = exportHook.new ImageFilter(type, description);
        String expected = description;
        String actual = imageFilter.getDescription();
        assertEquals(expected, actual);
    }
}