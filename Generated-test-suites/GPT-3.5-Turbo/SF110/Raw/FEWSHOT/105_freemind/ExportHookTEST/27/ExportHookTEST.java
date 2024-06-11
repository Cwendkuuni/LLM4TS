package freemind.extensions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.junit.Test;

import freemind.main.Resources;
import freemind.modes.ModeController;
import freemind.modes.Tools;
import freemind.view.mindmapview.FreeMindFileDialog;
import freemind.view.mindmapview.MapView;

public class ExportHookTest {

    @Test
    public void testChooseFile() {
        ExportHook exportHook = new ExportHook();
        ModeController controller = new ModeController();
        exportHook.setController(controller);

        File file = exportHook.chooseFile("png", "PNG file", ".png");
        assertNotNull(file);

        file = exportHook.chooseFile("jpg", "JPEG file", ".jpg");
        assertNull(file);
    }

    @Test
    public void testChooseImageFile() {
        ModeController controller = new ModeController();

        Container container = new Container();
        controller.getFrame().setContentPane(container);

        FileFilter filter = new FileFilter() {
            @Override
            public boolean accept(File f) {
                return true;
            }

            @Override
            public String getDescription() {
                return null;
            }
        };

        FreeMindFileDialog chooser = new FreeMindFileDialog(null, "title", filter);
        controller.setFileChooser(chooser);

        File file = ExportHook.chooseImageFile("png", "PNG file", ".png", controller);
        assertNotNull(file);

        file = ExportHook.chooseImageFile("jpg", "JPEG file", ".jpg", controller);
        assertNull(file);
    }

    @Test
    public void testGetTranslatableResourceString() {
        ExportHook exportHook = new ExportHook();

        String resourceName = "resource";
        String returnValue = exportHook.getTranslatableResourceString(resourceName);

        assertNull(returnValue);
    }

    @Test
    public void testCreateBufferedImage() {
        ExportHook exportHook = new ExportHook();
        ModeController controller = new ModeController();
        MapView view = new MapView(controller);
        controller.setView(view);
        exportHook.setController(controller);

        BufferedImage image = exportHook.createBufferedImage();
        assertNotNull(image);

        Rectangle innerBounds = view.getInnerBounds();
        assertEquals(innerBounds.width, image.getWidth());
        assertEquals(innerBounds.height, image.getHeight());
    }

    @Test
    public void testCopyFromResource() {
        ExportHook exportHook = new ExportHook();
        Resources resources = Resources.getInstance();
        resources.setExceptionLogger(null);
        exportHook.setLogger(null);

        String prefix = "prefix/";
        String fileName = "file.txt";
        String destinationDirectory = "destination";
        exportHook.copyFromResource(prefix, fileName, destinationDirectory);
    }

    @Test
    public void testCopyFromFile() {
        ExportHook exportHook = new ExportHook();
        Resources resources = Resources.getInstance();
        resources.setExceptionLogger(null);
        exportHook.setLogger(null);

        String prefix = "prefix/";
        String fileName = "file.txt";
        String destinationDirectory = "destination";
        exportHook.copyFromFile(prefix, fileName, destinationDirectory);
    }

    @Test
    public void testImageFilter() {
        ExportHook.ImageFilter filter = new ExportHook.ImageFilter("png", "PNG file");

        File file = new File("image.png");
        boolean accept = filter.accept(file);
        assertTrue(accept);

        file = new File("image.jpg");
        accept = filter.accept(file);
        assertFalse(accept);

        String description = filter.getDescription();
        assertEquals("PNG file", description);
    }
}