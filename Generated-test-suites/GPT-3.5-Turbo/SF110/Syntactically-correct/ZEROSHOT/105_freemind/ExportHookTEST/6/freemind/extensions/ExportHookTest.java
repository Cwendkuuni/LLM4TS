package freemind.extensions;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

import freemind.view.mindmapview.MapView;
import freemind.modes.ModeController;
import freemind.main.FreeMindFileDialog;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.*;
import java.io.File;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ExportHookTest {

    @Mock
    private ModeController mockController;

    @Mock
    private MapView mockView;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(mockController.getView()).thenReturn(mockView);
    }

    @Test
    public void testChooseFile() {
        ExportHook exportHook = new ExportHook();
        File result = exportHook.chooseFile("type", "description", "extension");
        assertNull(result);
        verify(mockController).getFileChooser(any(FileNameExtensionFilter.class));
    }

    @Test
    public void testChooseImageFile() {
        ExportHook exportHook = new ExportHook();
        File result = exportHook.chooseImageFile("type", "description", "extension", mockController);
        assertNull(result);
        verify(mockController).getFrame();
        verify(mockController).getFileChooser(any(FileNameExtensionFilter.class));
        verify(mockController.getMap()).getFile();
    }

    @Test
    public void testGetTranslatableResourceString() {
        ExportHook exportHook = new ExportHook();
        String result = exportHook.getTranslatableResourceString("resourceName");
        assertNull(result);
        verify(mockController).getText("resourceName");
    }

    @Test
    public void testCreateBufferedImage() {
        ExportHook exportHook = new ExportHook();
        when(mockView.getWidth()).thenReturn(100);
        when(mockView.getHeight()).thenReturn(100);
        Rectangle innerBounds = new Rectangle(0, 0, 100, 100);
        BufferedImage mockImage = mock(BufferedImage.class);
        Graphics mockGraphics = mock(Graphics.class);
        when(mockView.getInnerBounds()).thenReturn(innerBounds);
        when(mockView.createImage(100, 100)).thenReturn(mockImage);
        when(mockImage.getGraphics()).thenReturn(mockGraphics);
        BufferedImage result = exportHook.createBufferedImage();
        assertNotNull(result);
        verify(mockView).preparePrinting();
        verify(mockGraphics).clipRect(0, 0, 100, 100);
        verify(mockView).print(mockGraphics);
        verify(mockImage).getSubimage(0, 0, 100, 100);
        verify(mockView).endPrinting();
    }

    @Test
    public void testCopyFromResource() throws Exception {
        ExportHook exportHook = new ExportHook();
        exportHook.logger = mock(java.util.logging.Logger.class);
        exportHook.copyFromResource("prefix", "fileName", "destinationDirectory");
        verify(exportHook.logger).finest("searching for prefixfileName");
        verify(exportHook.logger).severe("Cannot find resource: prefixfileName");
    }

    @Test
    public void testCopyFromFile() throws Exception {
        ExportHook exportHook = new ExportHook();
        exportHook.logger = mock(java.util.logging.Logger.class);
        exportHook.copyFromFile("dir", "fileName", "destinationDirectory");
        verify(exportHook.logger).finest("searching for dirfileName");
        verify(exportHook.logger).severe("Cannot find resource: dirfileName");
    }

    @Test
    public void testImageFilterAccept() {
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("type", "description");
        File mockFile = mock(File.class);
        when(mockFile.isDirectory()).thenReturn(false);
        when(mockFile.getName()).thenReturn("file.type");
        assertTrue(imageFilter.accept(mockFile));
    }

    @Test
    public void testImageFilterAccept_InvalidFile() {
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("type", "description");
        File mockFile = mock(File.class);
        when(mockFile.isDirectory()).thenReturn(false);
        when(mockFile.getName()).thenReturn("file.invalid");
        assertFalse(imageFilter.accept(mockFile));
    }

    @Test
    public void testImageFilterAccept_Directory() {
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("type", "description");
        File mockDirectory = mock(File.class);
        when(mockDirectory.isDirectory()).thenReturn(true);
        assertTrue(imageFilter.accept(mockDirectory));
    }

    @Test
    public void testImageFilterGetDescription() {
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("type", "description");
        assertEquals("description", imageFilter.getDescription());
    }

    @Test
    public void testImageFilterGetDescription_NullDescription() {
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("type", null);
        assertEquals("type", imageFilter.getDescription());
    }
}