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
    private ModeController modeControllerMock;
    private Container containerMock;
    private JFrame frameMock;
    private File fileMock;
    private MapView mapViewMock;
    private FreeMindFileDialog fileDialogMock;
    private ImageFilter imageFilterMock;
    private FreeMindLogger loggerMock;
    private Resources resourcesMock;
    private InputStream inputStreamMock;
    private OutputStream outputStreamMock;
    private BufferedImage bufferedImageMock;
    private Graphics graphicsMock;
    private Rectangle rectangleMock;
    private Tools toolsMock;
    private URL urlMock;

    @Before
    public void setUp() {
        exportHook = new ExportHook();
        
        modeControllerMock = mock(ModeController.class);
        containerMock = mock(Container.class);
        frameMock = mock(JFrame.class);
        fileMock = mock(File.class);
        mapViewMock = mock(MapView.class);
        fileDialogMock = mock(FreeMindFileDialog.class);
        imageFilterMock = mock(ImageFilter.class);
        loggerMock = mock(FreeMindLogger.class);
        resourcesMock = mock(Resources.class);
        inputStreamMock = mock(InputStream.class);
        outputStreamMock = mock(OutputStream.class);
        bufferedImageMock = mock(BufferedImage.class);
        graphicsMock = mock(Graphics.class);
        rectangleMock = mock(Rectangle.class);
        toolsMock = mock(Tools.class);
        urlMock = mock(URL.class);

        when(modeControllerMock.getFrame()).thenReturn(frameMock);
        when(frameMock.getContentPane()).thenReturn(containerMock);
        when(mapViewMock.getInnerBounds()).thenReturn(rectangleMock);
        when(mapViewMock.createImage(anyInt(), anyInt())).thenReturn(bufferedImageMock);
        when(bufferedImageMock.getGraphics()).thenReturn(graphicsMock);
    }

    @Test
    public void testChooseFile() {
        when(exportHook.getController()).thenReturn(modeControllerMock);
        when(modeControllerMock.getFileChooser(imageFilterMock)).thenReturn(fileDialogMock);
        when(modeControllerMock.getMap().getFile()).thenReturn(fileMock);
        when(fileMock.getAbsolutePath()).thenReturn("path/to/file.mm");
        when(imageFilterMock.getType()).thenReturn("png");
        
        File result = exportHook.chooseFile("png", "PNG Image", "export");
        
        assertEquals("path/to/file.export.png", result.getAbsolutePath());
    }
    
    @Test
    public void testChooseFile_mmFileNull() {
        when(exportHook.getController()).thenReturn(modeControllerMock);
        when(modeControllerMock.getFileChooser(imageFilterMock)).thenReturn(fileDialogMock);
        when(modeControllerMock.getMap().getFile()).thenReturn(null);
        when(imageFilterMock.getType()).thenReturn("png");
        
        File result = exportHook.chooseFile("png", "PNG Image", "export");
        
        assertNull(result);
    }
    
    @Test
    public void testChooseImageFile() {
        when(exportHook.getController()).thenReturn(modeControllerMock);
        when(modeControllerMock.getFileChooser(imageFilterMock)).thenReturn(fileDialogMock);
        when(modeControllerMock.getMap().getFile()).thenReturn(fileMock);
        when(fileMock.getAbsolutePath()).thenReturn("path/to/file.mm");
        when(imageFilterMock.getType()).thenReturn("png");
        when(chooser.showSaveDialog(containerMock)).thenReturn(0);
        when(chooser.getSelectedFile()).thenReturn(fileMock);
        when(fileMock.getName()).thenReturn("file.export.png");
        when(toolsMock.getExtension("file.export.png")).thenReturn("png");
        
        File result = exportHook.chooseFile("png", "PNG Image", "export");
        
        assertEquals("path/to/file.export.png", result.getAbsolutePath());
        verify(chooser).setSelectedFile(fileMock);
        verify(fileMock).getParent();
        verify(fileMock).exists();
    }
    
    @Test
    public void testChooseImageFile_cancelDialog() {
        when(exportHook.getController()).thenReturn(modeControllerMock);
        when(modeControllerMock.getFileChooser(imageFilterMock)).thenReturn(fileDialogMock);
        when(modeControllerMock.getMap().getFile()).thenReturn(fileMock);
        when(fileMock.getAbsolutePath()).thenReturn("path/to/file.mm");
        when(imageFilterMock.getType()).thenReturn("png");
        when(chooser.showSaveDialog(containerMock)).thenReturn(1);
        
        File result = exportHook.chooseFile("png", "PNG Image", "export");
        
        assertNull(result);
        verify(chooser, never()).getSelectedFile();
    }
    
    @Test
    public void testChooseImageFile_extMismatch() {
        when(exportHook.getController()).thenReturn(modeControllerMock);
        when(modeControllerMock.getFileChooser(imageFilterMock)).thenReturn(fileDialogMock);
        when(modeControllerMock.getMap().getFile()).thenReturn(fileMock);
        when(fileMock.getAbsolutePath()).thenReturn("path/to/file.mm");
        when(imageFilterMock.getType()).thenReturn("png");
        when(chooser.showSaveDialog(containerMock)).thenReturn(0);
        when(chooser.getSelectedFile()).thenReturn(fileMock);
        when(fileMock.getName()).thenReturn("file.export.jpg");
        when(toolsMock.getExtension("file.export.jpg")).thenReturn("jpg");
        
        File result = exportHook.chooseFile("png", "PNG Image", "export");
        
        assertEquals("path/to/file.export.png", result.getAbsolutePath());
        verify(chooser).setSelectedFile(fileMock);
        verify(fileMock).getParent();
        verify(fileMock).exists();
    }
    
    @Test
    public void testCreateBufferedImage() {
        when(exportHook.getController()).thenReturn(modeControllerMock);
        when(modeControllerMock.getView()).thenReturn(mapViewMock);
        when(mapViewMock.getWidth()).thenReturn(800);
        when(mapViewMock.getHeight()).thenReturn(600);
        when(bufferedImageMock.getSubimage(0, 0, 800, 600)).thenReturn(bufferedImageMock);
        
        BufferedImage result = exportHook.createBufferedImage();
        
        assertEquals(bufferedImageMock, result);
        verify(mapViewMock).preparePrinting();
        verify(graphicsMock).clipRect(0, 0, 800, 600);
        verify(mapViewMock).print(graphicsMock);
        verify(mapViewMock).endPrinting();
    }
    
    @Test
    public void testGetTranslatableResourceString() {
        when(exportHook.getResourceString("%stringName")).thenReturn("translatedString");
        when(exportHook.getController()).thenReturn(modeControllerMock);
        when(modeControllerMock.getText("stringName")).thenReturn("translatedString");
        
        String result = exportHook.getTranslatableResourceString("%stringName");
        
        assertEquals("translatedString", result);
        verify(exportHook, never()).getController();
        verify(modeControllerMock).getText("stringName");
    }
    
    @Test
    public void testGetTranslatableResourceString_noTranslation() {
        when(exportHook.getResourceString("%stringName")).thenReturn("untranslatedString");
        when(exportHook.getController()).thenReturn(modeControllerMock);
        when(modeControllerMock.getText("stringName")).thenReturn(null);
        
        String result = exportHook.getTranslatableResourceString("%stringName");
        
        assertEquals("untranslatedString", result);
        verify(exportHook, never()).getController();
        verify(modeControllerMock).getText("stringName");
    }
    
    @Test
    public void testCopyFromResource() throws Exception {
        when(exportHook.getResource("prefixfileName")).thenReturn(urlMock);
        when(exportHook.getLogger()).thenReturn(loggerMock);
        when(loggerMock.finest("searching for prefixfileName")).thenReturn(mock(Logger.class));
        when(loggerMock.severe("Cannot find resource: prefixfileName")).thenReturn(mock(Logger.class));
        when(urlMock.openStream()).thenReturn(inputStreamMock);
        when(toolsMock.copyStream(inputStreamMock, outputStreamMock, true)).thenReturn(mock(String.class));
        
        exportHook.copyFromResource("prefix", "fileName", "destinationDirectory");
        
        verify(loggerMock).finest("searching for prefixfileName");
        verify(exportHook).getLogger();
        verify(loggerMock).severe("Cannot find resource: prefixfileName");
        verify(urlMock).openStream();
        verify(toolsMock).copyStream(inputStreamMock, outputStreamMock, true);
    }
    
    @Test
    public void testCopyFromResource_resourceNull() throws Exception {
        when(exportHook.getResource("prefixfileName")).thenReturn(null);
        when(exportHook.getLogger()).thenReturn(loggerMock);
        when(loggerMock.finest("searching for prefixfileName")).thenReturn(mock(Logger.class));
        when(loggerMock.severe("Cannot find resource: prefixfileName")).thenReturn(mock(Logger.class));
        
        exportHook.copyFromResource("prefix", "fileName", "destinationDirectory");
        
        verify(loggerMock).finest("searching for prefixfileName");
        verify(exportHook).getLogger();
        verify(loggerMock).severe("Cannot find resource: prefixfileName");
        verify(urlMock, never()).openStream();
        verify(toolsMock, never()).copyStream(inputStreamMock, outputStreamMock, true);
    }
    
    @Test
    public void testCopyFromFile() throws Exception {
        when(exportHook.getLogger()).thenReturn(loggerMock);
        when(loggerMock.finest("searching for dirfileName")).thenReturn(mock(Logger.class));
        when(loggerMock.severe("Cannot find resource: dirfileName")).thenReturn(mock(Logger.class));
        when(fileMock.getAbsolutePath()).thenReturn("dir");
        when(fileMock.exists()).thenReturn(true);
        when(fileMock.isDirectory()).thenReturn(false);
        when(fileInputStreamMock.equals(new FileInputStream(fileMock))).thenReturn(inputStreamMock);
        when(fileOutputStreamMock.equals(new FileOutputStream("destinationDirectory/fileName"))).thenReturn(outputStreamMock);
        when(toolsMock.copyStream(inputStreamMock, outputStreamMock, true)).thenReturn(mock(String.class));
        
        exportHook.copyFromFile("dir", "fileName", "destinationDirectory");
        
        verify(loggerMock).finest("searching for dirfileName");
        verify(loggerMock, never()).severe("Cannot find resource: dirfileName");
        verify(fileInputStreamMock).equals(new FileInputStream(fileMock));
        verify(fileOutputStreamMock).equals(new FileOutputStream("destinationDirectory/fileName"));
        verify(toolsMock).copyStream(inputStreamMock, outputStreamMock, true);
    }
    
    @Test
    public void testCopyFromFile_resourceNull() throws Exception {
        when(exportHook.getLogger()).thenReturn(loggerMock);
        when(loggerMock.finest("searching for dirfileName")).thenReturn(mock(Logger.class));
        when(loggerMock.severe("Cannot find resource: dirfileName")).thenReturn(mock(Logger.class));

        exportHook.copyFromFile("dir", "fileName", "destinationDirectory");
        
        verify(loggerMock).finest("searching for dirfileName");
        verify(loggerMock).severe("Cannot find resource: dirfileName");
        verify(fileInputStreamMock, never()).equals(new FileInputStream(fileMock));
        verify(fileOutputStreamMock, never()).equals(new FileOutputStream("destinationDirectory/fileName"));
        verify(toolsMock, never()).copyStream(inputStreamMock, outputStreamMock, true);
    }

}