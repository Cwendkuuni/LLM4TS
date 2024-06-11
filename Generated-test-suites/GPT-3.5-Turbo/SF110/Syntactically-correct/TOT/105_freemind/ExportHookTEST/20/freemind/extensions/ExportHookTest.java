import org.junit.*;
import static org.junit.Assert.*;
import java.awt.Container;
import javax.swing.*;
import java.io.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.*;
import javax.swing.filechooser.FileFilter;
import java.text.MessageFormat;
import freemind.main.FreeMindFileDialog;
import freemind.view.mindmapview.MapView;
import freemind.modes.ModeController;

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
        File chosenFile = exportHook.chooseFile(type, description, nameExtension);
        assertNotNull(chosenFile);
        assertEquals(".png_export.png", chosenFile.getName());
    }

    @Test
    public void testChooseImageFile() {
        String type = "jpeg";
        String description = "JPEG Image";
        String nameExtension = "_export";
        ModeController modeController = new ModeController();
        Container container = new Container();
        modeController.setFrame(container);
        File chosenFile = ExportHook.chooseImageFile(type, description, nameExtension, modeController);
        assertNotNull(chosenFile);
        assertEquals(".jpeg_export.jpeg", chosenFile.getName());
    }

    @Test
    public void testGetString() {
        String resourceName = "test_resource";
        String expected = "Test Resource";
        String actual = exportHook.getTranslatableResourceString(resourceName);
        assertEquals(expected, actual);
    }

    @Test
    public void testCreateBufferedImage() {
        MapView mapView = new MapView();
        ModeController modeController = new ModeController();
        modeController.setView(mapView);
        exportHook.setController(modeController);
        BufferedImage bufferedImage = exportHook.createBufferedImage();
        assertNotNull(bufferedImage);
        assertEquals(mapView.getWidth(), bufferedImage.getWidth());
        assertEquals(mapView.getHeight(), bufferedImage.getHeight());
    }

    @Test
    public void testCopyFromResource() {
        String prefix = "resources/";
        String fileName = "test.txt";
        String destinationDirectory = System.getProperty("user.dir");
        exportHook.copyFromResource(prefix, fileName, destinationDirectory);
        File copiedFile = new File(destinationDirectory + "/" + fileName);
        assertTrue(copiedFile.exists());
    }

    @Test
    public void testCopyFromFile() {
        String dir = System.getProperty("user.dir");
        String fileName = "test.txt";
        String destinationDirectory = System.getProperty("user.dir");
        exportHook.copyFromFile(dir, fileName, destinationDirectory);
        File copiedFile = new File(destinationDirectory + "/" + fileName);
        assertTrue(copiedFile.exists());
    }

    @Test
    public void testImageFileFilterAccept() {
        File directory = new File(System.getProperty("user.dir"));
        FileFilter imageFilter = new ExportHook.ImageFilter("png", "PNG Image");
        File pngFile = new File(directory, "sample.png");
        assertTrue(imageFilter.accept(pngFile));
        File txtFile = new File(directory, "sample.txt");
        assertFalse(imageFilter.accept(txtFile));
    }

    @Test
    public void testImageFileFilterDescription() {
        FileFilter imageFilter = new ExportHook.ImageFilter("jpeg", "JPEG Image");
        String description = imageFilter.getDescription();
        assertEquals("JPEG Image", description);
    }

}