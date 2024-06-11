package freemind.extensions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

import freemind.main.ModeController;
import freemind.modes.ModeControllerHookAdapter;
import freemind.view.mindmapview.MapView;

import org.junit.Test;

public class ExportHookTest {
    @Test
    public void testChooseFile() {
        ExportHook exportHook = new ExportHook();
        File file = exportHook.chooseFile("type", "description", "nameExtension");
        assertNull(file);
    }

    @Test
    public void testChooseImageFile() {
        ExportHook exportHook = new ExportHook();
        ModeController modeController = new ModeController();
        modeController.setFrame(new JFrame());
        File file = ExportHook.chooseImageFile("type", "description", "nameExtension", modeController);
        assertNull(file);
    }

    @Test
    public void testGetTranslatableResourceString() {
        ExportHook exportHook = new ExportHook();
        String translatableResourceString = exportHook.getTranslatableResourceString("resourceName");
        assertNull(translatableResourceString);
    }

    @Test
    public void testCreateBufferedImage() {
        ExportHook exportHook = new ExportHook();
        ModeController modeController = new ModeController();
        MapView mapView = new MapView();
        modeController.setView(mapView);
        exportHook.setController(modeController);
        BufferedImage image = exportHook.createBufferedImage();
        assertNull(image);
    }

    @Test
    public void testCopyFromResource() {
        ExportHook exportHook = new ExportHook();
        exportHook.setLogger(Logger.getLogger(ExportHook.class.getName()));
        exportHook.setResource("prefix");
        exportHook.copyFromResource("prefix", "fileName", "destinationDirectory");
        // Add assertions if needed
    }

    @Test
    public void testCopyFromFile() {
        ExportHook exportHook = new ExportHook();
        exportHook.setLogger(Logger.getLogger(ExportHook.class.getName()));
        exportHook.copyFromFile("dir", "fileName", "destinationDirectory");
        // Add assertions if needed
    }

    @Test
    public void testImageFilterAccept() {
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("type", "description");
        File file = new File("test.png");
        boolean accept = imageFilter.accept(file);
        assertEquals(true, accept);
    }

    @Test
    public void testImageFilterGetDescription() {
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("type", "description");
        String description = imageFilter.getDescription();
        assertEquals("description", description);
    }
}