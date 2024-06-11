
package freemind.extensions;

import freemind.view.mindmapview.ModeController;
import freemind.view.mindmapview.FreeMindFileDialog;
import freemind.modes.Tools;
import freemind.resources.Resources;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.text.MessageFormat;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class ExportHookTest {

    private ExportHook exportHook;

    @Before
    public void setUp() {
        exportHook = new ExportHook();
    }

    @Test
    public void testChooseFile() {
        final File file = exportHook.chooseFile("png", "PNG Image", "image");
        assertNotNull(file);
        assertEquals("file.png", file.getName());
    }

    @Test
    public void testChooseImageFile() {
        final ModeController controller = mock(ModeController.class);
        final Container container = new Container();
        final FreeMindFileDialog fileDialog = mock(FreeMindFileDialog.class);
        final File mmFile = new File("example.mm");
        final String type = "png";
        final String description = "PNG Image";
        final String nameExtension = "image";

        controller.setFrame(new JFrame());
        controller.getMap().setFile(mmFile);

        JFrame frame = controller.getFrame();
        frame.setContentPane(container);

        container.add(fileDialog);

        fileDialog.setType(type);
        fileDialog.setDescription(description);
        fileDialog.setNameExtension(nameExtension);

        fileDialog.setSelectedFile(new File("example_image.png"));

        final File chosenFile = exportHook.chooseImageFile(type, description, nameExtension, controller);
        assertNotNull(chosenFile);
        assertEquals("example_image.png", chosenFile.getName());
    }

    @Test
    public void testGetTranslatableResourceString() {
        final String resourceName = "/path/to/resource";
        final String returnValue = exportHook.getTranslatableResourceString(resourceName);
        //assertEquals("Expected value", returnValue);
    }

    @Test
    public void testCreateBufferedImage() {
        exportHook.getController().setView(new MapView());
        final BufferedImage bufferedImage = exportHook.createBufferedImage();
        //assertNotNull(bufferedImage);
    }

    @Test
    public void testCopyFromResource() {
        exportHook.logger = mock(Logger.class);
        exportHook.copyFromResource("prefix", "file", "/destination");

        // Verify the file is copied properly
    }

    @Test
    public void testCopyFromFile() {
        exportHook.logger = mock(Logger.class);
        exportHook.copyFromFile("/dir", "file", "/destination");

        // Verify the file is copied properly
    }

    @Test
    public void testImageFilterAccept() {
        final ExportHook.ImageFilter filter = exportHook.new ImageFilter("png", "PNG Image");
        final File directory = new File(".");
        final File file = new File("example.png");

        assertTrue(filter.accept(directory));
        assertTrue(filter.accept(file));
    }

    @Test
    public void testImageFilterGetDescription() {
        final ExportHook.ImageFilter filter = exportHook.new ImageFilter("png", "PNG Image");
        assertEquals("PNG Image", filter.getDescription());
    }
}
