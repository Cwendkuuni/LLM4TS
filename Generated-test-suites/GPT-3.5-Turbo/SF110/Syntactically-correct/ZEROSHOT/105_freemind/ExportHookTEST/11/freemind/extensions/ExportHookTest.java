
package freemind.extensions;

import org.junit.Test;
import static org.junit.Assert.*;
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

public class ExportHookTest {

    @Test
    public void testChooseFile() {
        ExportHook exportHook = new ExportHook();
        ModeController controllerMock = mock(ModeController.class);
        when(exportHook.getController()).thenReturn(controllerMock);
        
        File chosenFile = exportHook.chooseFile("png", "PNG Image", ".png");
        
        assertNotNull(chosenFile);
        verify(controllerMock).getFileChooser(any(ImageFilter.class));
    }

    @Test
    public void testChooseImageFile() {
        ModeController controllerMock = mock(ModeController.class);
        Container containerMock = mock(Container.class);
        JFrame frameMock = mock(JFrame.class);
        MapView mapViewMock = mock(MapView.class);
        File mapFileMock = mock(File.class);
        FreeMindFileDialog dialogMock = mock(FreeMindFileDialog.class);
        
        when(controllerMock.getFrame()).thenReturn(frameMock);
        when(frameMock.getContentPane()).thenReturn(containerMock);
        when(controllerMock.getMap()).thenReturn(mapViewMock);
        when(mapViewMock.getFile()).thenReturn(mapFileMock);
        when(controllerMock.getFileChooser(any(ImageFilter.class))).thenReturn(dialogMock);
        
        ExportHook.chooseImageFile("png", "PNG Image", ".png", controllerMock);
        
        verify(mapFileMock).getAbsolutePath();
        verify(dialogMock).setSelectedFile(any(File.class));
        verify(dialogMock).showSaveDialog(containerMock);
    }

    @Test
    public void testGetTranslatableResourceString() {
        ExportHook exportHook = new ExportHook();
        exportHook.setController(mock(ModeController.class));
        
        String resourceName = "resource";
        String resourceValue = "value";
        
        when(exportHook.getResourceString("resource")).thenReturn("%resource");
        when(exportHook.getController().getText("resource")).thenReturn("value");
        
        String translatedString = exportHook.getTranslatableResourceString(resourceName);
        
        assertEquals(resourceValue, translatedString);
        verify(exportHook.getController()).getText(resourceName);
    }

    @Test
    public void testCreateBufferedImage() {
        ExportHook exportHook = new ExportHook();
        MapView mapViewMock = mock(MapView.class);
        ModeController controllerMock = mock(ModeController.class);
        
        exportHook.setView(mapViewMock);
        when(exportHook.getController()).thenReturn(controllerMock);
        when(mapViewMock.getInnerBounds()).thenReturn(new Rectangle());
        when(mapViewMock.getWidth()).thenReturn(10);
        when(mapViewMock.getHeight()).thenReturn(10);
        when(mapViewMock.createImage(anyInt(), anyInt())).thenReturn(new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB));
        
        BufferedImage image = exportHook.createBufferedImage();
        
        assertNotNull(image);
        verify(mapViewMock).preparePrinting();
        verify(mapViewMock).print(any(Graphics.class));
        verify(mapViewMock).endPrinting();
    }

    @Test
    public void testCopyFromResource() {
        ExportHook exportHook = new ExportHook();
        exportHook.setLogger(mock(Logger.class));
        exportHook.setResourceString(mock(String.class));
        exportHook.setController(mock(ModeController.class));
        
        exportHook.copyFromResource("prefix", "file", "destination");
        
        // verify the expected sequence of method calls and behavior
    }

    @Test
    public void testCopyFromFile() {
        ExportHook exportHook = new ExportHook();
        exportHook.setLogger(mock(Logger.class));
        exportHook.setController(mock(ModeController.class));
        
        exportHook.copyFromFile("dir", "file", "destination");
        
        // verify the expected sequence of method calls and behavior
    }

    @Test
    public void testImageFilterAccept() {
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("png", "PNG Image");
        File fileMock = mock(File.class);
        
        when(fileMock.getName()).thenReturn("image.png");
        
        assertTrue(imageFilter.accept(fileMock));
    }

    @Test
    public void testImageFilterGetDescriptionWithoutCustomDescription() {
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("png", null);
        
        String description = imageFilter.getDescription();
        
        assertEquals("png", description);
    }

    @Test
    public void testImageFilterGetDescriptionWithCustomDescription() {
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("png", "PNG Image");
        
        String description = imageFilter.getDescription();
        
        assertEquals("PNG Image", description);
    }
}
