package freemind.extensions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

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

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import freemind.main.FreeMindFileDialog;
import freemind.main.Resources;
import freemind.modes.ModeController;
import freemind.modes.ModeControllerHookAdapter;
import freemind.view.mindmapview.MapView;
import freemind.view.mindmapview.Tools;

public class ExportHookTest {
    private ExportHook exportHook = new ExportHook();

    @Test
    public void testChooseFile() {
        File chosenFile = exportHook.chooseFile("type", "description", "nameExtension");
        assertNotNull(chosenFile);
        assertEquals("type", chosenFile.getName().substring(chosenFile.getName().lastIndexOf(".") + 1));
    }

    @Test
    public void testChooseImageFile() {
        ModeController controller = new ModeController() {
            @Override
            public Container getContentPane() {
                return new Container();
            }

            @Override
            public File getMapFile() {
                return new File("");
            }

            @Override
            public File getFile() {
                return new File("");
            }

            @Override
            public String getText(String key) {
                return "";
            }

            @Override
            public FreeMindFileDialog getFileChooser(FileFilter filter) {
                return new FreeMindFileDialog(new JFileChooser());
            }

            @Override
            public MapView getView() {
                return new MapView();
            }
        };

        File chosenFile = ExportHook.chooseImageFile("type", "description", "nameExtension", controller);
        assertNotNull(chosenFile);
        assertEquals("type", chosenFile.getName().substring(chosenFile.getName().lastIndexOf(".") + 1));
    }

    @Test
    public void testGetTranslatableResourceString() {
        String result = exportHook.getTranslatableResourceString("resourceName");
        assertNull(result);
    }

    @Test
    public void testCreateBufferedImage() {
        BufferedImage image = exportHook.createBufferedImage();
        assertNull(image);
    }

    @Test
    public void testCopyFromResource() {
        exportHook.copyFromResource("prefix", "fileName", "destinationDirectory");
    }

    @Test
    public void testCopyFromFile() {
        exportHook.copyFromFile("dir", "fileName", "destinationDirectory");
    }

    @Test
    public void testImageFilter() {
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("type", "description");
        File file = new File("test.jpg");
        assertEquals(true, imageFilter.accept(file));
        assertEquals("description", imageFilter.getDescription());
    }
}