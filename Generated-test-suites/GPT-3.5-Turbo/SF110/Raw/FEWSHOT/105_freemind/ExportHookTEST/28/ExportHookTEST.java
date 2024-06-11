package freemind.extensions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.MessageFormat;

import javax.swing.JOptionPane;

import org.junit.Test;
import org.mockito.Mockito;

import freemind.main.ModeController;
import freemind.main.Tools;
import freemind.main.Resources;
import freemind.view.mindmapview.FreeMindFileDialog;
import freemind.view.mindmapview.MapView;

public class ExportHookTest {

    @Test
    public void testChooseFile() {
        ModeController controller = mock(ModeController.class);
        ExportHook exportHook = Mockito.spy(ExportHook.class);
        Mockito.doReturn(controller).when(exportHook).getController();
        
        String type = "png";
        String description = "PNG Image";
        String nameExtension = "mindmap";
        
        File result = exportHook.chooseFile(type, description, nameExtension);
        
        verify(controller).getFrame();
        verify(controller).getFileChooser(any());
        verify(controller).getMap();
        verify(controller).getText(any());
        
        assertNull(result);
    }

    @Test
    public void testChooseImageFile() {
        ModeController controller = mock(ModeController.class);
        ExportHook exportHook = new ExportHook();
        
        String type = "png";
        String description = "PNG Image";
        String nameExtension = "mindmap";
        
        Container component = mock(Container.class);
        ImageFilter filter = new ImageFilter(type, description);
        FreeMindFileDialog chooser = mock(FreeMindFileDialog.class);
        File mmFile = mock(File.class);
        
        when(controller.getFrame()).thenReturn(component);
        when(controller.getFileChooser(filter)).thenReturn(chooser);
        when(controller.getMap().getFile()).thenReturn(mmFile);
        when(controller.getText(any())).thenReturn("Text");
        when(chooser.showSaveDialog(component)).thenReturn(0);
        when(chooser.getSelectedFile()).thenReturn(mock(File.class));
        when(mmFile.getAbsolutePath()).thenReturn("file/path");
        when(mmFile.getName()).thenReturn("file.name");
        
        File result = ExportHook.chooseImageFile(type, description, nameExtension, controller);
        
        verify(controller).getFrame();
        verify(controller).getFileChooser(filter);
        verify(controller).getMap();
        verify(controller).getText(any());
        verify(chooser).setSelectedFile(any());
        verify(mmFile).getAbsolutePath();
        verify(mmFile).getName();
        verify(chooser).showSaveDialog(component);
        verify(chooser).getSelectedFile();
        verify(Tools).getExtension(anyString());
        verify(component).getComponent(0);
        verify(controller).getText(any());
        
        assertNull(result);
    }
    
    @Test
    public void testGetTranslatableResourceString() {
        ExportHook exportHook = new ExportHook();
        
        String resourceName = "resource";
        String returnValue = "returnValue";
        
        exportHook.getController() = mock(ModeController.class);
        
        when(exportHook.getController().getText(resource)).thenReturn(returnValue);
        
        String result = exportHook.getTranslatableResourceString(resourceName);
        
        verify(exportHook.getController()).getText(resourceName.substring(1));
        
        assertEquals(returnValue, result);
    }
    
    @Test
    public void testCreateBufferedImage() {
        ExportHook exportHook = new ExportHook();
        exportHook.getController() = mock(ModeController.class);
        exportHook.getController().getView() = mock(MapView.class);
        BufferedImage image = mock(BufferedImage.class);
        Graphics graphics = mock(Graphics.class);
        Rectangle innerBounds = mock(Rectangle.class);
        when(exportHook.getController().getView().getInnerBounds()).thenReturn(innerBounds);
        when(exportHook.getController().getView()).thenReturn(null, image);
        when(exportHook.getController().getWidth()).thenReturn(10);
        when(exportHook.getController().getHeight()).thenReturn(10);
        when(exportHook.getController().getView().createImage(10, 10)).thenReturn(image);
        when(exportHook.getController().getView().getWidth()).thenReturn(10);
        when(exportHook.getController().getView().getHeight()).thenReturn(10);
        when(image.getGraphics()).thenReturn(graphics);
        when(innerBounds.x).thenReturn(0);
        when(innerBounds.y).thenReturn(0);
        when(innerBounds.width).thenReturn(10);
        when(innerBounds.height).thenReturn(10);
        when(image.getSubimage(0, 0, 10, 10)).thenReturn(image);
        
        BufferedImage result = exportHook.createBufferedImage();
        
        verify(exportHook.getController(), Mockito.times(2)).getView();
        verify(exportHook.getController().getWidth());
        verify(exportHook.getController().getHeight());
        verify(exportHook.getController().getView().createImage(10, 10));
        verify(exportHook.getController().getView().getWidth());
        verify(exportHook.getController().getView().getHeight());
        verify(image.getGraphics());
        verify(graphics).clipRect(0, 0, 10, 10);
        verify(exportHook.getController().getView()).print(graphics);
        verify(image, Mockito.times(2)).getSubimage(0, 0, 10, 10);
        
        assertEquals(image, result);
    }
    
    @Test
    public void testCopyFromResource() {
        ExportHook exportHook = new ExportHook();
        exportHook.logger = mock(Logger.class);
        exportHook.getResourceString() = mock(String.class);
        exportHook.getController() = mock(ModeController.class);
        exportHook.getResource() = mock(URL.class);
        InputStream in = mock(InputStream.class);
        OutputStream out = mock(OutputStream.class);
        FileOutputStream fileOutputStream = mock(FileOutputStream.class);
        when(exportHook.getResourceString()).thenReturn("");
        when(exportHook.logger.finest(any())).thenReturn(null);
        when(exportHook.getResource(any())).thenReturn(null);
        when(new FileOutputStream(anyString())).thenReturn(fileOutputStream);
        
        exportHook.copyFromResource("", "", "");
        
        verify(exportHook.logger).finest(any());
        verify(exportHook.getResource(any()));
        verify(fileOutputStream).close();
    }
    
    @Test
    public void testCopyFromFile() {
        ExportHook exportHook = new ExportHook();
        exportHook.logger = mock(Logger.class);
        exportHook.getResourceString()= mock(String.class);
        File resource = mock(File.class);
        InputStream in = mock(InputStream.class);
        OutputStream out = mock(OutputStream.class);
        FileInputStream fileInputStream = mock(FileInputStream.class);
        when(exportHook.logger.finest(any())).thenReturn(null);
        when(exportHook.getResource(any(), any())).thenReturn(null);
        when(new FileInputStream(any(File.class))).thenReturn(fileInputStream);
        when(new FileOutputStream(anyString())).thenReturn(fileOutputStream);
        
        exportHook.copyFromFile("", "", "");
        
        verify(exportHook.logger).finest(any());
        verify(exportHook.getResource(any(), any()));
        verify(fileInputStream).close();
        verify(fileOutputStream).close();
    }
    
    @Test
    public void testImageFilter() {
        ImageFilter imageFilter = new ImageFilter("png", "PNG Image");
        File f = mock(File.class);
        when(f.isDirectory()).thenReturn(true);
        when(f.getName()).thenReturn("fileName.png");
        
        assertTrue(imageFilter.accept(f));
        assertEquals("PNG Image", imageFilter.getDescription());
    }
}