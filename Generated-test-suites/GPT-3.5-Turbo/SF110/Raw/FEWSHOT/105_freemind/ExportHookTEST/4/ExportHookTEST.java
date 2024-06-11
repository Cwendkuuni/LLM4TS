package freemind.extensions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import freemind.view.mindmapview.*;
import javax.swing.filechooser.*;
import java.text.*;
import javax.swing.*;
import freemind.modes.*;
import java.awt.image.*;
import java.awt.*;
import freemind.main.*;
import java.net.*;
import java.io.*;

import org.junit.Before;
import org.junit.Test;

public class ExportHookTest {
    private ExportHook exportHook;
    private ModeController modeController;
    private Frame frame;
    private Container container;
    private MapView mapView;

    @Before
    public void setUp() {
        exportHook = new ExportHook();
        modeController = mock(ModeController.class);
        frame = mock(Frame.class);
        container = mock(Container.class);
        mapView = mock(MapView.class);

        when(modeController.getFrame()).thenReturn(frame);
        when(frame.getContentPane()).thenReturn(container);
        when(modeController.getView()).thenReturn(mapView);
    }

    @Test
    public void testChooseFile() {
        File chosenFile = mock(File.class);
        File selectedFile = mock(File.class);
        File mmFile = mock(File.class);

        when(modeController.getMap().getFile()).thenReturn(mmFile);
        when(mmFile.getAbsolutePath()).thenReturn("testpath");
        when(mmFile.getName()).thenReturn("testfile");

        FreeMindFileDialog chooser = mock(FreeMindFileDialog.class);
        when(chooser.getSelectedFile()).thenReturn(selectedFile);
        when(chooser.showSaveDialog(container)).thenReturn(0);

        when(modeController.getFileChooser(any(ImageFilter.class))).thenReturn(chooser);
        when(selectedFile.getName()).thenReturn("testname");

        File result = exportHook.chooseFile("type", "description", "ext");
        String expectedFilePath = "testpath.testext.type";

        assertEquals(expectedFilePath, result.getAbsolutePath());
    }

    @Test
    public void testChooseFile_nullReturnVal() {
        FreeMindFileDialog chooser = mock(FreeMindFileDialog.class);
        when(chooser.showSaveDialog(container)).thenReturn(1);

        when(modeController.getFileChooser(any(ImageFilter.class))).thenReturn(chooser);

        File result = exportHook.chooseFile("type", "description", "ext");
        
        assertNull(result);
    }

    @Test
    public void testChooseImageFile() {
        File chosenFile = mock(File.class);
        File selectedFile = mock(File.class);
        File mmFile = mock(File.class);

        when(modeController.getMap().getFile()).thenReturn(mmFile);
        when(mmFile.getAbsolutePath()).thenReturn("testpath");
        when(mmFile.getName()).thenReturn("testfile");

        FreeMindFileDialog chooser = mock(FreeMindFileDialog.class);
        when(chooser.getSelectedFile()).thenReturn(selectedFile);
        when(chooser.showSaveDialog(container)).thenReturn(0);

        when(modeController.getFileChooser(any(ImageFilter.class))).thenReturn(chooser);
        when(selectedFile.getName()).thenReturn("testname");

        File result = ExportHook.chooseImageFile("type", "description", "ext", modeController);
        String expectedFilePath = "testpath.testext.type";

        assertEquals(expectedFilePath, result.getAbsolutePath());
    }

    @Test
    public void testChooseImageFile_nullReturnVal() {
        FreeMindFileDialog chooser = mock(FreeMindFileDialog.class);
        when(chooser.showSaveDialog(container)).thenReturn(1);

        when(modeController.getFileChooser(any(ImageFilter.class))).thenReturn(chooser);

        File result = ExportHook.chooseImageFile("type", "description", "ext", modeController);
        
        assertNull(result);
    }

    @Test
    public void testGetTranslatableResourceString() {
        ModeController controller = mock(ModeController.class);
        String text = "test text";

        when(exportHook.getResourceString("resourceName")).thenReturn("%" + text);
        when(controller.getText(text)).thenReturn("translated text");

        String result = exportHook.getTranslatableResourceString("resourceName");

        assertEquals("translated text", result);
    }

    @Test
    public void testGetTranslatableResourceString_noTranslationNeeded() {
        when(exportHook.getResourceString("resourceName")).thenReturn("test text");

        String result = exportHook.getTranslatableResourceString("resourceName");

        assertEquals("test text", result);
    }

    @Test
    public void testCreateBufferedImage() {
        Rectangle innerBounds = mock(Rectangle.class);
        BufferedImage myImage = mock(BufferedImage.class);

        when(mapView.getInnerBounds()).thenReturn(innerBounds);
        when(mapView.getWidth()).thenReturn(100);
        when(mapView.getHeight()).thenReturn(100);
        when(mapView.createImage(100, 100)).thenReturn(myImage);

        BufferedImage result = exportHook.createBufferedImage();

        assertEquals(myImage, result);
    }

    @Test
    public void testCreateBufferedImage_viewNull() {
        when(modeController.getView()).thenReturn(null);

        BufferedImage result = exportHook.createBufferedImage();

        assertNull(result);
    }

    @Test
    public void testCopyFromResource() throws Exception {
        URL resource = new URL("http://example.com/resource");

        when(exportHook.getResource("prefixfileName")).thenReturn(resource);

        InputStream in = mock(InputStream.class);
        when(resource.openStream()).thenReturn(in);

        OutputStream out = mock(OutputStream.class);
        whenNew(FileOutputStream.class).withArguments("destinationDirectory/fileName").thenReturn(out);

        ExportHook.Tools.copyStream(in, out, true);

        exportHook.copyFromResource("prefix", "fileName", "destinationDirectory");

        verify(logger).finest("searching for prefixfileName");
        verify(logger, never()).severe(anyString());
    }

    @Test
    public void testCopyFromResource_resourceNotFound() throws Exception {
        when(exportHook.getResource("prefixfileName")).thenReturn(null);

        exportHook.copyFromResource("prefix", "fileName", "destinationDirectory");

        verify(logger).finest("searching for prefixfileName");
        verify(logger).severe("Cannot find resource: prefixfileName");
    }

    @Test
    public void testCopyFromResource_fileNotFound() throws Exception {
        URL resource = new URL("http://example.com/resource");

        when(exportHook.getResource("prefixfileName")).thenReturn(resource);
        whenNew(FileOutputStream.class).withArguments("destinationDirectory/fileName").thenThrow(IOException.class);

        Resources resources = mock(Resources.class);
        PowerMockito.mockStatic(Resources.class);
        when(Resources.getInstance()).thenReturn(resources);

        exportHook.copyFromResource("prefix", "fileName", "destinationDirectory");

        verify(logger).finest("searching for prefixfileName");
        verify(logger).severe("File not found or could not be copied. Was searching for prefixfileName and should go to destinationDirectory");
        verify(resources).logException(any(IOException.class));
    }

    @Test
    public void testCopyFromFile() throws Exception {
        File resource = mock(File.class);
        whenNew(File.class).withArguments("dir", "fileName").thenReturn(resource);

        InputStream in = mock(InputStream.class);
        whenNew(FileInputStream.class).withArguments(resource).thenReturn(in);

        OutputStream out = mock(OutputStream.class);
        whenNew(FileOutputStream.class).withArguments("destinationDirectory/fileName").thenReturn(out);

        ExportHook.Tools.copyStream(in, out, true);

        exportHook.copyFromFile("dir", "fileName", "destinationDirectory");

        verify(logger).finest("searching for dirfileName");
        verify(logger, never()).severe(anyString());
    }

    @Test
    public void testCopyFromFile_resourceNotFound() throws Exception {
        whenNew(File.class).withArguments("dir", "fileName").thenReturn(null);

        exportHook.copyFromFile("dir", "fileName", "destinationDirectory");

        verify(logger).finest("searching for dirfileName");
        verify(logger).severe("Cannot find resource: dirfileName");
    }

    @Test
    public void testCopyFromFile_fileNotFound() throws Exception {
        File resource = mock(File.class);
        whenNew(File.class).withArguments("dir", "fileName").thenReturn(resource);
        whenNew(FileOutputStream.class).withArguments("destinationDirectory/fileName").thenThrow(IOException.class);

        Resources resources = mock(Resources.class);
        PowerMockito.mockStatic(Resources.class);
        when(Resources.getInstance()).thenReturn(resources);

        exportHook.copyFromFile("dir", "fileName", "destinationDirectory");

        verify(logger).finest("searching for dirfileName");
        verify(logger).severe("File not found or could not be copied. Was searching for dirfileName and should go to destinationDirectory");
        verify(resources).logException(any(IOException.class));
    }

    @Test
    public void testImageFilter_accept() {
        File file = mock(File.class);
        when(file.isDirectory()).thenReturn(false);
        when(file.getName()).thenReturn("test.name");

        ImageFilter imageFilter = new ExportHook.ImageFilter("type", "description");

        boolean result = imageFilter.accept(file);

        assertTrue(result);
    }

    @Test
    public void testImageFilter_accept_directory() {
        File file = mock(File.class);
        when(file.isDirectory()).thenReturn(true);

        ImageFilter imageFilter = new ExportHook.ImageFilter("type", "description");

        boolean result = imageFilter.accept(file);

        assertTrue(result);
    }

    @Test
    public void testImageFilter_reject() {
        File file = mock(File.class);
        when(file.isDirectory()).thenReturn(false);
        when(file.getName()).thenReturn("test.test");

        ImageFilter imageFilter = new ExportHook.ImageFilter("type", "description");

        boolean result = imageFilter.accept(file);

        assertFalse(result);
    }

    @Test
    public void testImageFilter_getDescription() {
        ImageFilter imageFilter = new ExportHook.ImageFilter("type", "description");

        String result = imageFilter.getDescription();

        assertEquals("description", result);
    }

    @Test
    public void testImageFilter_getDescription_noDescription() {
        ImageFilter imageFilter = new ExportHook.ImageFilter("type", null);

        String result = imageFilter.getDescription();

        assertEquals("type", result);
    }
}