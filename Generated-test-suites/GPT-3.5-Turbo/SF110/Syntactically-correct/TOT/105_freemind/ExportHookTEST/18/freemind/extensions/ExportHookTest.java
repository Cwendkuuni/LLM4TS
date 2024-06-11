import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import freemind.view.mindmapview.MapView;
import freemind.modes.ModeController;
import freemind.extensions.ExportHook;

import javax.swing.*;
import javax.swing.filechooser.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;

public class ExportHookTest {

    private ExportHook exportHook;
    private ModeController modeController;
    private MapView mapView;
    private Container container;
    private ImageFilter imageFilter;
    private FreeMindFileDialog fileDialog;
    private File mockInputFile;
    private File mockSelectedFile;
    private String mockSelectedFileName;
    
    @Before
    public void setUp() {
        exportHook = new ExportHook();
        modeController = mock(ModeController.class);
        mapView = mock(MapView.class);
        container = mock(Container.class);
        imageFilter = mock(ImageFilter.class);
        fileDialog = mock(FreeMindFileDialog.class);
        mockInputFile = mock(File.class);
        mockSelectedFile = mock(File.class);
        mockSelectedFileName = "test.png";
        
        when(modeController.getFrame()).thenReturn(new JFrame());
        when(modeController.getFrame().getContentPane()).thenReturn(container);
        when(modeController.getMap().getFile()).thenReturn(mockInputFile);
        when(fileDialog.showSaveDialog(container)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(fileDialog.getSelectedFile()).thenReturn(mockSelectedFile);
        when(mockSelectedFile.getName()).thenReturn(mockSelectedFileName);
    }
    
    @Test
    public void testChooseFile() {
        when(modeController.getFileChooser(imageFilter)).thenReturn(fileDialog);
        
        File result = exportHook.chooseFile("png", "PNG Image", "_export");
        
        assertEquals(mockSelectedFile, result);
        verify(fileDialog).setSelectedFile(new File(mockInputFile.getAbsolutePath().replaceFirst("\\.[^.]*?$", "") + "_export.png"));
    }
    
    @Test
    public void testChooseFile_Cancelled() {
        when(fileDialog.showSaveDialog(container)).thenReturn(JFileChooser.CANCEL_OPTION);
        
        File result = exportHook.chooseFile("png", "PNG Image", "_export");
        
        assertNull(result);
    }
    
    @Test
    public void testChooseImageFile() {
        when(modeController.getFileChooser(imageFilter)).thenReturn(fileDialog);
        
        File result = ExportHook.chooseImageFile("png", "PNG Image", "_export", modeController);
        
        assertEquals(mockSelectedFile, result);
        verify(fileDialog).setSelectedFile(new File(mockInputFile.getAbsolutePath().replaceFirst("\\.[^.]*?$", "") + "_export.png"));
    }
    
    @Test
    public void testChooseImageFile_Cancelled() {
        when(fileDialog.showSaveDialog(container)).thenReturn(JFileChooser.CANCEL_OPTION);
        
        File result = ExportHook.chooseImageFile("png", "PNG Image", "_export", modeController);
        
        assertNull(result);
    }
    
    @Test
    public void testChooseImageFile_ExtensionMismatch() {
        when(modeController.getFileChooser(imageFilter)).thenReturn(fileDialog);
        when(mockSelectedFile.getName()).thenReturn("test.jpg");
        
        File result = ExportHook.chooseImageFile("png", "PNG Image", "_export", modeController);
        
        assertNotNull(result);
        assertEquals("test.png", result.getName());
    }
    
    @Test
    public void testChooseImageFile_ExistingFile_Overwrite() {
        when(modeController.getFileChooser(imageFilter)).thenReturn(fileDialog);
        when(mockSelectedFile.exists()).thenReturn(true);
        when(mockSelectedFile.toString()).thenReturn("test.png");
        
        File result = ExportHook.chooseImageFile("png", "PNG Image", "_export", modeController);
        
        assertEquals(mockSelectedFile, result);
    }
    
    @Test
    public void testChooseImageFile_ExistingFile_Cancel() {
        when(modeController.getFileChooser(imageFilter)).thenReturn(fileDialog);
        when(mockSelectedFile.exists()).thenReturn(true);
        when(mockSelectedFile.toString()).thenReturn("test.png");
        when(JOptionPane.showConfirmDialog(container, "file_already_exists", "file_already_exists", JOptionPane.YES_NO_OPTION)).thenReturn(JOptionPane.NO_OPTION);
        
        File result = ExportHook.chooseImageFile("png", "PNG Image", "_export", modeController);
        
        assertNull(result);
    }
    
    @Test
    public void testGetTranslatableResourceString_WithText() {
        String expectedResult = "Translated Resource";
        String resourceName = "%resource";
        when(exportHook.getResourceString(resourceName)).thenReturn(resourceName);
        when(modeController.getText(resourceName.substring(1))).thenReturn(expectedResult);
        
        String result = exportHook.getTranslatableResourceString(resourceName);
        
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testGetTranslatableResourceString_NoText() {
        String expectedResult = "Not Translated";
        String resourceName = "resource";
        when(exportHook.getResourceString(resourceName)).thenReturn(expectedResult);
        
        String result = exportHook.getTranslatableResourceString(resourceName);
        
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testCreateBufferedImage() {
        when(exportHook.getController()).thenReturn(modeController);
        when(modeController.getView()).thenReturn(mapView);
        when(mapView.getInnerBounds()).thenReturn(new Rectangle(0, 0, 100, 100));
        when(mapView.getWidth()).thenReturn(100);
        when(mapView.getHeight()).thenReturn(100);
        BufferedImage mockImage = mock(BufferedImage.class);
        when(mapView.createImage(100, 100)).thenReturn(mockImage);
        Graphics mockGraphics = mock(Graphics.class);
        when(mockImage.getGraphics()).thenReturn(mockGraphics);
        BufferedImage subImage = mock(BufferedImage.class);
        when(mockImage.getSubimage(0, 0, 100, 100)).thenReturn(subImage);
        
        BufferedImage result = exportHook.createBufferedImage();
        
        assertEquals(subImage, result);
        verify(mockGraphics).clipRect(0, 0, 100, 100);
        verify(mapView).preparePrinting();
        verify(mockImage).getGraphics();
        verify(mapView).print(mockGraphics);
        verify(mapView).endPrinting();
    }
    
    @Test
    public void testCopyFromResource_ResourceFound() throws Exception {
        String prefix = "prefix/";
        String fileName = "test.txt";
        String destinationDirectory = "destination";
        String resourcePath = prefix + fileName;
        URL resourceURL = new URL("file:///path/to/resource");
        InputStream mockInputStream = mock(InputStream.class);
        OutputStream mockOutputStream = mock(OutputStream.class);
        
        when(exportHook.getResource(resourcePath)).thenReturn(resourceURL);
        when(exportHook.logger.finest("searching for " + resourcePath)).thenReturn(null);
        when(exportHook.getResource(prefix + fileName)).thenReturn(resourceURL);
        when(resourceURL.openStream()).thenReturn(mockInputStream);
        when(mockOutputStream).thenReturn(mock(FileOutputStream.class));
        
        exportHook.copyFromResource(prefix, fileName, destinationDirectory);
        
        verify(exportHook.logger).finest("searching for " + resourcePath);
        verify(exportHook).getResource(resourcePath);
        verify(mockInputStream).close();
        verify(mockOutputStream).close();
    }
    
    @Test
    public void testCopyFromFile_ResourceFound() throws Exception {
        String dir = "dir";
        String fileName = "test.txt";
        String destinationDirectory = "destination";
        File resource = new File(dir, fileName);
        InputStream mockInputStream = mock(InputStream.class);
        OutputStream mockOutputStream = mock(OutputStream.class);
        
        when(exportHook.logger.finest("searching for " + dir + fileName)).thenReturn(null);
        when(mockInputStream).thenReturn(mock(FileInputStream.class));
        when(mockOutputStream).thenReturn(mock(FileOutputStream.class));
        
        exportHook.copyFromFile(dir, fileName, destinationDirectory);
        
        verify(exportHook.logger).finest("searching for " + dir + fileName);
        verify(mockInputStream).close();
        verify(mockOutputStream).close();
    }
    
    @Test
    public void testImageFilter_Accept() {
        File mockFile = mock(File.class);
        when(mockFile.isDirectory()).thenReturn(false);
        when(mockFile.getName()).thenReturn("test.png");
        when(Tools.getExtension(mockFile.getName())).thenReturn("png");
        
        assertTrue(imageFilter.accept(mockFile));
    }
    
    @Test
    public void testImageFilter_Reject() {
        File mockDirectory = mock(File.class);
        when(mockDirectory.isDirectory()).thenReturn(true);
        when(mockDirectory.getName()).thenReturn("testDirectory");
        
        assertFalse(imageFilter.accept(mockDirectory));
    }
    
    @Test
    public void testImageFilter_GetDescription_WithDescription() {
        String description = "PNG Image";
        when(imageFilter.getDescription()).thenReturn(description);
        
        assertEquals(description, imageFilter.getDescription());
    }
    
    @Test
    public void testImageFilter_GetDescription_Null() {
        when(imageFilter.getDescription()).thenReturn(null);
        
        assertNull(imageFilter.getDescription());
    }
}