import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ExportHookTest {

    @Test
    public void testChooseFile() {
        ExportHook exportHook = new ExportHook();
        ModeController mockController = mock(ModeController.class);
        exportHook.setController(mockController);
        
        String type = "jpg";
        String description = "JPEG Image";
        String nameExtension = "_export";
        
        File result = exportHook.chooseFile(type, description, nameExtension);
        
        assertNotNull(result);
        assertTrue(result.exists());
        assertTrue(result.isDirectory());
        assertEquals("jpg", Tools.getExtension(result.getName()));
    }
    
    @Test
    public void testChooseFile_NoFileSelected() {
        ExportHook exportHook = new ExportHook();
        ModeController mockController = mock(ModeController.class);
        exportHook.setController(mockController);
        
        String type = "png";
        String description = "PNG Image";
        String nameExtension = "";
        
        File result = exportHook.chooseFile(type, description, nameExtension);
        
        assertNull(result);
    }
    
    @Test
    public void testChooseImageFile() {
        ExportHook exportHook = new ExportHook();
        ModeController mockController = mock(ModeController.class);
        Container mockComponent = mock(Container.class);
        Frame mockFrame = mock(Frame.class);
        Map mockMap = mock(Map.class);
        File mockFile = mock(File.class);
        ImageFilter mockFilter = mock(ImageFilter.class);
        FreeMindFileDialog mockChooser = mock(FreeMindFileDialog.class);
        JOptionPane mockOptionPane = mock(JOptionPane.class);
        
        when(mockController.getFrame()).thenReturn(mockFrame);
        when(mockController.getFrame().getContentPane()).thenReturn(mockComponent);
        when(mockController.getMap()).thenReturn(mockMap);
        when(mockController.getMap().getFile()).thenReturn(mockFile);
        when(mockFile.getAbsolutePath()).thenReturn("/path/to/file.mm");
        when(mockFile.getName()).thenReturn("file.mm");
        when(mockChooser.showSaveDialog(any(Container.class))).thenReturn(0);
        when(mockChooser.getSelectedFile()).thenReturn(mockFile);
        when(mockFilter.accept(any(File.class))).thenReturn(true);
        when(mockOptionPane.showConfirmDialog(any(Container.class), anyString(), anyString(), anyInt())).thenReturn(0);
        
        File result = exportHook.chooseImageFile("jpg", "JPEG Image", "_export", mockController);
        
        assertNotNull(result);
        assertTrue(result.exists());
        assertEquals("/path/to/file_export.jpg", result.getAbsolutePath());
    }
    
    @Test
    public void testChooseImageFile_NoFileSelected() {
        ExportHook exportHook = new ExportHook();
        ModeController mockController = mock(ModeController.class);
        Container mockComponent = mock(Container.class);
        Frame mockFrame = mock(Frame.class);
        Map mockMap = mock(Map.class);
        File mockFile = mock(File.class);
        FreeMindFileDialog mockChooser = mock(FreeMindFileDialog.class);
        
        when(mockController.getFrame()).thenReturn(mockFrame);
        when(mockController.getFrame().getContentPane()).thenReturn(mockComponent);
        when(mockController.getMap()).thenReturn(mockMap);
        when(mockController.getMap().getFile()).thenReturn(mockFile);
        when(mockFile.getAbsolutePath()).thenReturn("/path/to/file.mm");
        when(mockChooser.showSaveDialog(any(Container.class))).thenReturn(1);
        
        File result = exportHook.chooseImageFile("png", "PNG Image", "", mockController);
        
        assertNull(result);
    }
    
    @Test
    public void testGetTranslatableResourceString() {
        ExportHook exportHook = new ExportHook();
        exportHook.setController(mock(ModeController.class));
        
        String result = exportHook.getTranslatableResourceString("resource_name");
        
        assertNull(result);
    }
    
    @Test
    public void testGetTranslatableResourceString_WithReturnValue() {
        ExportHook exportHook = new ExportHook();
        exportHook.setController(mock(ModeController.class));
        exportHook.setResourceString("%resource_key");
        ModeController mockController = mock(ModeController.class);
        
        when(mockController.getText("resource_key")).thenReturn("Translated Resource Value");
        
        exportHook.setController(mockController);
        
        String result = exportHook.getTranslatableResourceString("resource_name");
        
        assertEquals("Translated Resource Value", result);
    }
    
    @Test
    public void testCreateBufferedImage() {
        ExportHook exportHook = new ExportHook();
        ModeController mockController = mock(ModeController.class);
        MapView mockView = mock(MapView.class);
        BufferedImage mockImage = mock(BufferedImage.class);
        Graphics mockGraphics = mock(Graphics.class);
        
        when(mockController.getView()).thenReturn(mockView);
        when(mockView.createImage(mockView.getWidth(), mockView.getHeight())).thenReturn(mockImage);
        when(mockImage.getGraphics()).thenReturn(mockGraphics);
        
        BufferedImage result = exportHook.createBufferedImage();
        
        assertNotNull(result);
        verify(mockView).preparePrinting();
        verify(mockGraphics).clipRect(anyInt(), anyInt(), anyInt(), anyInt());
        verify(mockView).print(mockGraphics);
        verify(mockImage).getSubimage(anyInt(), anyInt(), anyInt(), anyInt());
        verify(mockView).endPrinting();
    }
    
    @Test
    public void testCreateBufferedImage_NoView() {
        ExportHook exportHook = new ExportHook();
        ModeController mockController = mock(ModeController.class);
        
        when(mockController.getView()).thenReturn(null);
        
        BufferedImage result = exportHook.createBufferedImage();
        
        assertNull(result);
    }
    
    @Test
    public void testCopyFromResource_ResourceNotFound() {
        ExportHook exportHook = new ExportHook();
        exportHook.setLogger(mock(Logger.class));
        exportHook.setResourceString("resource_key");
        exportHook.setController(mock(ModeController.class));
        URL mockURL = mock(URL.class);
        
        when(mockURL.openStream()).thenReturn(null);
        when(mockResource.getResource("resource_keyresource_name")).thenReturn(mockURL);
        
        exportHook.copyFromResource("resource_key", "resource_name", "destination_directory");
        
        verify(mockLogger).severe("Cannot find resource: resource_keyresource_name");
    }
    
    @Test
    public void testCopyFromResource_FileNotFound() {
        ExportHook exportHook = new ExportHook();
        exportHook.setLogger(mock(Logger.class));
        Resources.getInstance().setLogger(mock(Logger.class));
        exportHook.setController(mock(ModeController.class));
        URL mockURL = mock(URL.class);
        InputStream mockIn = mock(InputStream.class);
        OutputStream mockOut = mock(OutputStream.class);
        
        when(mockURL.openStream()).thenReturn(mockIn);
        when(mockResource.getResource("resource_keyresource_name")).thenReturn(mockURL);
        when(mockIn.available()).thenReturn(0);
        when(mockIn.read()).thenReturn(-1);
        when(mockController.getResourceString("file_not_found")).thenReturn("File not found or could not be copied. Was searching for %resource_keyresource_name% and should go to destination_directory");
        
        exportHook.copyFromResource("resource_key", "resource_name", "destination_directory");
        
        verify(mockLogger).severe("File not found or could not be copied. Was searching for resource_keyresource_name and should go to destination_directory");
        verify(mockResources.getInstance()).logException(any(Exception.class));
    }
    
    @Test
    public void testCopyFromFile_FileNotFound() {
        ExportHook exportHook = new ExportHook();
        exportHook.setLogger(mock(Logger.class));
        Resources.getInstance().setLogger(mock(Logger.class));
        String dir = "/path/to/dir";
        String fileName = "file.txt";
        String destinationDirectory = "destination_directory";
        
        File mockFile = mock(File.class);
        FileInputStream mockIn = mock(FileInputStream.class);
        FileOutputStream mockOut = mock(FileOutputStream.class);
        
        when(mockFile.exists()).thenReturn(false);
        when(mockController.getResourceString("file_not_found")).thenReturn("File not found or could not be copied. Was searching for %resource_keyresource_name% and should go to destination_directory");
        
        exportHook.copyFromFile(dir, fileName, destinationDirectory);
        
        verify(mockLogger).severe("Cannot find resource: /path/to/dirfile.txt");
    }
    
    @Test
    public void testCopyFromFile_FileFound() {
        ExportHook exportHook = new ExportHook();
        exportHook.setLogger(mock(Logger.class));
        Resources.getInstance().setLogger(mock(Logger.class));
        String dir = "/path/to/dir";
        String fileName = "file.txt";
        String destinationDirectory = "destination_directory";
        
        File mockFile = mock(File.class);
        FileInputStream mockIn = mock(FileInputStream.class);
        FileOutputStream mockOut = mock(FileOutputStream.class);
        
        when(mockFile.exists()).thenReturn(true);
        when(mockFile.isDirectory()).thenReturn(true);
        when(mockController.getResourceString("file_already_exists")).thenReturn("file_already_exists");
        when(mockOptionPane.showConfirmDialog(any(Container.class), anyString(), anyString(), anyInt())).thenReturn(0);
        
        exportHook.copyFromFile(dir, fileName, destinationDirectory);
        
        verify(mockOut).write(any(byte[].class));
    }
    
    @Test
    public void testImageFilter_Accept() {
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("jpg", "JPEG Image");
        
        File mockFile = mock(File.class);
        
        when(mockFile.isDirectory()).thenReturn(false);
        
        boolean result = imageFilter.accept(mockFile);
        
        assertTrue(result);
    }
    
    @Test
    public void testImageFilter_Reject() {
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("jpg", "JPEG Image");
        
        File mockDirectory = mock(File.class);
        
        when(mockDirectory.isDirectory()).thenReturn(true);
        
        boolean result = imageFilter.accept(mockDirectory);
        
        assertFalse(result);
    }
    
    @Test
    public void testImageFilter_GetDescription() {
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("jpg", "JPEG Image");
        
        String result = imageFilter.getDescription();
        
        assertEquals("JPEG Image", result);
    }
    
    @Test
    public void testImageFilter_GetDescription_Null() {
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("gif", null);
        
        String result = imageFilter.getDescription();
        
        assertEquals("gif", result);
    }
}