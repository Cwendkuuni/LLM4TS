package freemind.extensions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import freemind.main.Controller;
import freemind.view.mindmapview.MapView;
import freemind.view.modes.ModeController;
import org.freeplane.core.util.Resources;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class ExportHookTest {
    
    private ExportHook exportHook;
    private ModeController modeController;
    private JFrame frame;
    private MapView view;

    @Before
    public void setUp() {
        exportHook = new ExportHook();
        modeController = mock(ModeController.class);
        frame = new JFrame();
        view = new MapView();
        frame.setContentPane(view);
        view.setModeController(modeController);
    }

    @Test
    public void testChooseFile() {
        when(modeController.getFrame()).thenReturn(frame);
        when(modeController.getFileChooser(isA(ExportHook.ImageFilter.class))).thenReturn(new FreeMindFileDialog());
        File file = exportHook.chooseFile("png", "PNG Image", null);
        assertNull(file);
    }

    @Test
    public void testChooseImageFile() {
        when(modeController.getFrame()).thenReturn(frame);
        when(modeController.getFileChooser(isA(ExportHook.ImageFilter.class))).thenReturn(new FreeMindFileDialog());
        File file = ExportHook.chooseImageFile("png", "PNG Image", null, modeController);
        assertNull(file);
    }

    @Test
    public void testGetTranslatableResourceString() {
        Resources.getInstance().init();
        modeController.init();
        Resources.getInstance().localize("./lib/freemind.properties");
        String translatedString = exportHook.getTranslatableResourceString("file_already_exists");
        assertEquals("This file already exists. Do you want to overwrite it?", translatedString);
    }

    @Test
    public void testCreateBufferedImage() {
        exportHook.setController(modeController);
        modeController.setView(view);
        BufferedImage image = exportHook.createBufferedImage();
        assertNull(image);
    }

    @Test
    public void testCopyFromResource() {
        exportHook.setController(modeController);
        ExportHook.logger.setLevel(ExportHook.logger.getLevel().parse("ALL"));
        exportHook.copyFromResource("/", "logo.png", "./test");
        File file = new File("./test/logo.png");
        boolean exists = file.exists();
        file.delete();
        assertEquals(true, exists);
    }

    @Test
    public void testCopyFromFile() {
        exportHook.setController(modeController);
        ExportHook.logger.setLevel(ExportHook.logger.getLevel().parse("ALL"));
        exportHook.copyFromFile("./test", "logo.png", "./test2");
        File file = new File("./test2/logo.png");
        boolean exists = file.exists();
        file.delete();
        assertEquals(true, exists);
    }
}