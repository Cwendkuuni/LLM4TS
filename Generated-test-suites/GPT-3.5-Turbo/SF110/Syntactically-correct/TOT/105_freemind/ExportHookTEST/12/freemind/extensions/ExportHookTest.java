import org.junit.*;
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
    public void chooseFile_typicalUseCase() {
        // Setup
        ExportHook exportHook = new ExportHook();
        String type = "png";
        String description = "PNG Image";
        String nameExtension = "_image";
        
        // Execute
        File file = exportHook.chooseFile(type, description, nameExtension);
        
        // Verify
        assertNotNull(file);
        assertEquals(file.getName(), "image.png");
    }
    
    @Test
    public void chooseFile_extensionMismatch() {
        // Setup
        ExportHook exportHook = new ExportHook();
        String type = "png";
        String description = "PNG Image";
        String nameExtension = "_image";
        ModeController mockController = mock(ModeController.class);   // Mock ModeController to return incorrect file extension
        when(exportHook.getController()).thenReturn(mockController);
        File mmFile = new File("example.mm");
        when(mockController.getMap().getFile()).thenReturn(mmFile);
        
        // Execute
        File file = exportHook.chooseFile(type, description, nameExtension);
        
        // Verify
        assertNotNull(file);
        assertEquals(file.getName(), "image.png");  // Name extension not appended
    }
    
    @Test
    public void chooseFile_existingFileOverwritten() {
        // Setup
        ExportHook exportHook = new ExportHook();
        String type = "png";
        String description = "PNG Image";
        String nameExtension = "_image";
        ModeController mockController = mock(ModeController.class);   // Mock ModeController to return existing file
        when(exportHook.getController()).thenReturn(mockController);
        File mmFile = new File("example.mm");
        when(mockController.getMap().getFile()).thenReturn(mmFile);
        FreeMindFileDialog mockChooser = mock(FreeMindFileDialog.class);
        when(mockController.getFileChooser(any(ImageFilter.class))).thenReturn(mockChooser);
        when(mockChooser.showSaveDialog(any(Container.class))).thenReturn(0); // User selects to overwrite existing file
        File existingFile = new File("existing.png");
        when(mockChooser.getSelectedFile()).thenReturn(existingFile);
        
        // Execute
        File file = exportHook.chooseFile(type, description, nameExtension);
        
        // Verify
        assertNotNull(file);
        assertEquals(file.getName(), "existing.png");  // Existing file is used
    }
    
    @Test
    public void chooseFile_existingFileNotOverwritten() {
        // Setup
        ExportHook exportHook = new ExportHook();
        String type = "png";
        String description = "PNG Image";
        String nameExtension = "_image";
        ModeController mockController = mock(ModeController.class);   // Mock ModeController to return existing file
        when(exportHook.getController()).thenReturn(mockController);
        File mmFile = new File("example.mm");
        when(mockController.getMap().getFile()).thenReturn(mmFile);
        FreeMindFileDialog mockChooser = mock(FreeMindFileDialog.class);
        when(mockController.getFileChooser(any(ImageFilter.class))).thenReturn(mockChooser);
        when(mockChooser.showSaveDialog(any(Container.class))).thenReturn(1); // User cancels overwrite
        File existingFile = new File("existing.png");
        when(mockChooser.getSelectedFile()).thenReturn(existingFile);
        
        // Execute
        File file = exportHook.chooseFile(type, description, nameExtension);
        
        // Verify
        assertNull(file);
    }

    @Test
    public void getTranslatableResourceString_translatableResourcePresent() {
        // Setup
        ExportHook exportHook = new ExportHook();
        String resourceName = "%resourceName";  // Translatable resource
        
        // Execute
        String resourceString = exportHook.getTranslatableResourceString(resourceName);
        
        // Verify
        assertNotNull(resourceString);
        assertEquals(resourceString, "Translated Resource Name");
    }
    
    @Test
    public void getTranslatableResourceString_translatableResourceNotPresent() {
        // Setup
        ExportHook exportHook = new ExportHook();
        String resourceName = "resourceName";  // No translation needed
        
        // Execute
        String resourceString = exportHook.getTranslatableResourceString(resourceName);
        
        // Verify
        assertNotNull(resourceString);
        assertEquals(resourceString, "resourceName");
    }
    
    @Test
    public void createBufferedImage_noViewPresent() {
        // Setup
        ExportHook exportHook = new ExportHook();
        ModeController mockController = mock(ModeController.class);   // Mock ModeController to return null as view
        when(exportHook.getController()).thenReturn(mockController);
        
        // Execute
        BufferedImage image = exportHook.createBufferedImage();
        
        // Verify
        assertNull(image);
    }
    
    @Test
    public void createBufferedImage_withView() {
        // Setup
        ExportHook exportHook = new ExportHook();
        ModeController mockController = mock(ModeController.class);   // Mock ModeController to return non-null as view
        when(exportHook.getController()).thenReturn(mockController);
        MapView mockView = mock(MapView.class);
        when(mockController.getView()).thenReturn(mockView);
        
        // Execute
        BufferedImage image = exportHook.createBufferedImage();
        
        // Verify
        assertNotNull(image);
    }
    
    @Test
    public void copyFromResource_resourcePresent() throws Exception {
        // Setup
        ExportHook exportHook = new ExportHook();
        String prefix = "/resources/";
        String fileName = "example.txt";
        String destinationDirectory = "destination";
        URL mockResource = mock(URL.class);
        when(exportHook.getResource(prefix + fileName)).thenReturn(mockResource);
        InputStream mockInputStream = mock(InputStream.class);
        when(mockResource.openStream()).thenReturn(mockInputStream);
        OutputStream mockOutputStream = mock(OutputStream.class);
        FileOutputStream mockFileOutputStream = mock(FileOutputStream.class);
        whenNew(FileOutputStream.class).withParameterTypes(String.class).withArguments(anyString()).thenReturn(mockFileOutputStream);
        
        // Execute
        exportHook.copyFromResource(prefix, fileName, destinationDirectory);
        
        // Verify
        verify(mockInputStream).close();
        verify(mockFileOutputStream).close();
    }
    
    @Test
    public void copyFromResource_resourceNotPresent() throws Exception {
        // Setup
        ExportHook exportHook = new ExportHook();
        String prefix = "/resources/";
        String fileName = "example.txt";
        String destinationDirectory = "destination";
        URL mockResource = null;
        when(exportHook.getResource(prefix + fileName)).thenReturn(mockResource);
        
        // Execute
        exportHook.copyFromResource(prefix, fileName, destinationDirectory);
        
        // Verify
        // Exception is logged, no other actions are taken
    }
    
    @Test
    public void copyFromFile_filePresent() throws Exception {
        // Setup
        ExportHook exportHook = new ExportHook();
        String dir = "dir";
        String fileName = "example.txt";
        String destinationDirectory = "destination";
        File mockFile = mock(File.class);
        whenNew(File.class).withParameterTypes(String.class, String.class).withArguments(dir, fileName).thenReturn(mockFile);
        FileInputStream mockFileInputStream = mock(FileInputStream.class);
        whenNew(FileInputStream.class).withArguments(mockFile).thenReturn(mockFileInputStream);
        OutputStream mockOutputStream = mock(OutputStream.class);
        FileOutputStream mockFileOutputStream = mock(FileOutputStream.class);
        whenNew(FileOutputStream.class).withParameterTypes(String.class).withArguments(anyString()).thenReturn(mockFileOutputStream);
        
        // Execute
        exportHook.copyFromFile(dir, fileName, destinationDirectory);
        
        // Verify
        verify(mockFileInputStream).close();
        verify(mockFileOutputStream).close();
    }
    
    @Test
    public void copyFromFile_fileNotPresent() throws Exception {
        // Setup
        ExportHook exportHook = new ExportHook();
        String dir = "dir";
        String fileName = "example.txt";
        String destinationDirectory = "destination";
        File mockFile = null;
        whenNew(File.class).withParameterTypes(String.class, String.class).withArguments(dir, fileName).thenReturn(mockFile);
        
        // Execute
        exportHook.copyFromFile(dir, fileName, destinationDirectory);
        
        // Verify
        // Exception is logged, no other actions are taken
    }
    
    @Test
    public void ImageFilter_accept_fileIsDirectory() {
        // Setup
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("png", "PNG Image");
        File mockFile = mock(File.class);
        when(mockFile.isDirectory()).thenReturn(true);
        
        // Execute
        boolean isAccepted = imageFilter.accept(mockFile);
        
        // Verify
        assertTrue(isAccepted);
    }
    
    @Test
    public void ImageFilter_accept_extensionMatches() {
        // Setup
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("png", "PNG Image");
        File mockFile = mock(File.class);
        when(mockFile.isDirectory()).thenReturn(false);
        when(mockFile.getName()).thenReturn("example.png");
        
        // Execute
        boolean isAccepted = imageFilter.accept(mockFile);
        
        // Verify
        assertTrue(isAccepted);
    }
    
    @Test
    public void ImageFilter_accept_extensionDoesNotMatch() {
        // Setup
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("png", "PNG Image");
        File mockFile = mock(File.class);
        when(mockFile.isDirectory()).thenReturn(false);
        when(mockFile.getName()).thenReturn("example.jpg");
        
        // Execute
        boolean isAccepted = imageFilter.accept(mockFile);
        
        // Verify
        assertFalse(isAccepted);
    }
    
    @Test
    public void ImageFilter_getDescription_descriptionPresent() {
        // Setup
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("png", "PNG Image");
        
        // Execute
        String description = imageFilter.getDescription();
        
        // Verify
        assertNotNull(description);
        assertEquals(description, "PNG Image");
    }
    
    @Test
    public void ImageFilter_getDescription_descriptionNotPresent() {
        // Setup
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("png", null);
        
        // Execute
        String description = imageFilter.getDescription();
        
        // Verify
        assertNotNull(description);
        assertEquals(description, "png");
    }
}