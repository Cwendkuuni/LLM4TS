package freemind.extensions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import org.junit.Before;
import org.junit.Test;

public class ExportHookTest {
    private ExportHook exportHook;
    private ModeController mockController;
    private MapView mockView;
    private JFrame frame;
    private Container container;
    
    @Before
    public void setUp() {
        exportHook = new ExportHook();
        mockController = mock(ModeController.class);
        mockView = mock(MapView.class);
        frame = new JFrame();
        container = new Container();
        
        frame.setContentPane(container);
        when(mockController.getFrame()).thenReturn(frame);
        when(mockController.getMap()).thenReturn(new MindMap());
        when(mockController.getView()).thenReturn(mockView);
        exportHook.setController(mockController);
    }
    
    @Test
    public void testChooseFile() {
        String type = "png";
        String description = "PNG Image";
        String nameExtension = "export";
        
        FileChooser mockFileChooser = mockFileChooserWithType(type, description);
        when(mockController.getFileChooser(any(FileFilter.class))).thenReturn(mockFileChooser);
        
        File expectedFile = new File("path/to/file.png");
        when(mockFileChooser.showSaveDialog(container)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(mockFileChooser.getSelectedFile()).thenReturn(expectedFile);
        
        File chosenFile = exportHook.chooseFile(type, description, nameExtension);
        assertEquals(expectedFile, chosenFile);
    }
    
    @Test
    public void testChooseFile_NoFileSelected() {
        String type = "png";
        String description = "PNG Image";
        String nameExtension = "export";
        
        FileChooser mockFileChooser = mockFileChooserWithType(type, description);
        when(mockController.getFileChooser(any(FileFilter.class))).thenReturn(mockFileChooser);
        
        when(mockFileChooser.showSaveDialog(container)).thenReturn(JFileChooser.CANCEL_OPTION);
        
        File chosenFile = exportHook.chooseFile(type, description, nameExtension);
        assertNull(chosenFile);
    }
    
    @Test
    public void testChooseFile_ProposedName() {
        String type = "png";
        String description = "PNG Image";
        String nameExtension = "export";
        
        FileChooser mockFileChooser = mockFileChooserWithType(type, description);
        when(mockController.getFileChooser(any(FileFilter.class))).thenReturn(mockFileChooser);
        
        File mmFile = new File("path/to/map.mm");
        when(mockController.getMap().getFile()).thenReturn(mmFile);
        
        when(mockFileChooser.showSaveDialog(container)).thenReturn(JFileChooser.APPROVE_OPTION);
        
        File chosenFile = exportHook.chooseFile(type, description, nameExtension);
        File expectedFile = new File("path/to/map.export.png");
        assertEquals(expectedFile, chosenFile);
    }
    
    @Test
    public void testChooseFile_TypeMismatch() {
        String type = "png";
        String description = "PNG Image";
        String nameExtension = "export";
        
        FileChooser mockFileChooser = mockFileChooserWithType(type, description);
        when(mockController.getFileChooser(any(FileFilter.class))).thenReturn(mockFileChooser);
        
        File expectedFile = new File("path/to/file.jpg");
        when(mockFileChooser.showSaveDialog(container)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(mockFileChooser.getSelectedFile()).thenReturn(expectedFile);
        
        File chosenFile = exportHook.chooseFile(type, description, nameExtension);
        File expectedChosenFile = new File("path/to/file.jpg.png");
        assertEquals(expectedChosenFile, chosenFile);
    }
    
    @Test
    public void testChooseFile_FileExists_OverwriteMapConfirmed() {
        String type = "png";
        String description = "PNG Image";
        String nameExtension = "export";
        
        FileChooser mockFileChooser = mockFileChooserWithType(type, description);
        when(mockController.getFileChooser(any(FileFilter.class))).thenReturn(mockFileChooser);
        
        File expectedFile = new File("path/to/file.png");
        when(mockFileChooser.showSaveDialog(container)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(mockFileChooser.getSelectedFile()).thenReturn(expectedFile);
        
        File existingFile = new File("path/to/file.png");
        when(expectedFile.exists()).thenReturn(true);
        when(JOptionPane.showConfirmDialog(container, "File already exists: " + existingFile.toString(), "File already exists: " + existingFile.toString(), JOptionPane.YES_NO_OPTION)).thenReturn(JOptionPane.YES_OPTION);
        
        File chosenFile = exportHook.chooseFile(type, description, nameExtension);
        assertEquals(expectedFile, chosenFile);
    }
    
    @Test
    public void testChooseFile_FileExists_OverwriteMapCancelled() {
        String type = "png";
        String description = "PNG Image";
        String nameExtension = "export";
        
        FileChooser mockFileChooser = mockFileChooserWithType(type, description);
        when(mockController.getFileChooser(any(FileFilter.class))).thenReturn(mockFileChooser);
        
        File expectedFile = new File("path/to/file.png");
        when(mockFileChooser.showSaveDialog(container)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(mockFileChooser.getSelectedFile()).thenReturn(expectedFile);
        
        File existingFile = new File("path/to/file.png");
        when(expectedFile.exists()).thenReturn(true);
        when(JOptionPane.showConfirmDialog(container, "File already exists: " + existingFile.toString(), "File already exists: " + existingFile.toString(), JOptionPane.YES_NO_OPTION)).thenReturn(JOptionPane.NO_OPTION);
        
        File chosenFile = exportHook.chooseFile(type, description, nameExtension);
        assertNull(chosenFile);
    }
    
    @Test
    public void testGetTranslatableResourceString_ExistingResource() {
        String resourceName = "resource.name";
        String expectedValue = "Translated Resource";
        
        when(exportHook.getResourceString(resourceName)).thenReturn("%" + resourceName);
        when(mockController.getText(resourceName)).thenReturn(expectedValue);
        
        String translatableResourceString = exportHook.getTranslatableResourceString(resourceName);
        assertEquals(expectedValue, translatableResourceString);
    }
    
    @Test
    public void testGetTranslatableResourceString_NonExistingResource() {
        String resourceName = "resource.name";
        String expectedValue = "Non-translated Resource";
        
        when(exportHook.getResourceString(resourceName)).thenReturn(expectedValue);
        
        String translatableResourceString = exportHook.getTranslatableResourceString(resourceName);
        assertEquals(expectedValue, translatableResourceString);
    }
    
    @Test
    public void testCreateBufferedImage() {
        int width = 800;
        int height = 600;
        
        Rectangle innerBounds = new Rectangle(50, 50, 700, 500);
        BufferedImage expectedImage = new BufferedImage(innerBounds.width, innerBounds.height, BufferedImage.TYPE_INT_RGB);
        
        when(mockView.getInnerBounds()).thenReturn(innerBounds);
        when(mockView.getWidth()).thenReturn(width);
        when(mockView.getHeight()).thenReturn(height);
        when(mockView.createImage(width, height)).thenReturn(expectedImage);
        
        BufferedImage createdImage = exportHook.createBufferedImage();
        verify(mockView).preparePrinting();
        verify(mockView).print(any(Graphics.class));
        verify(mockView).endPrinting();
        assertEquals(expectedImage, createdImage);
    }
    
    @Test
    public void testCopyFromResource_FileNotFound() {
        String prefix = "prefix/";
        String fileName = "resource.txt";
        String destinationDirectory = "path/to/destination";
        
        exportHook.copyFromResource(prefix, fileName, destinationDirectory);
        
        verify(exportHook.getLogger()).severe("Cannot find resource: " + prefix + fileName);
        verify(exportHook.getResources()).logException(any(Exception.class));
    }
    
    @Test
    public void testCopyFromResource_FileFoundAndCopied() throws Exception {
        String prefix = "prefix/";
        String fileName = "resource.txt";
        String destinationDirectory = "path/to/destination";
        
        File resourceFile = new File("path/to/resource.txt");
        
        URL mockResourceURL = mock(URL.class);
        when(exportHook.getResource(prefix + fileName)).thenReturn(mockResourceURL);
        
        InputStream mockInputStream = mock(InputStream.class);
        when(mockResourceURL.openStream()).thenReturn(mockInputStream);
        
        File destinationFile = new File(destinationDirectory + "/" + fileName);
        OutputStream mockOutputStream = mock(OutputStream.class);
        whenNew(FileOutputStream.class).withArguments(destinationFile).thenReturn(mockOutputStream);
        
        exportHook.copyFromResource(prefix, fileName, destinationDirectory);
        
        verify(exportHook.getLogger()).finest("searching for " + prefix + fileName);
        verify(exportHook).getResource(prefix + fileName);
        verify(mockResourceURL).openStream();
        verifyNew(FileOutputStream.class).withArguments(destinationFile);
        verify(mockInputStream).close();
        verify(mockOutputStream).close();
    }
    
    @Test
    public void testCopyFromFile_FileNotFound() {
        String dir = "path/to/dir";
        String fileName = "resource.txt";
        String destinationDirectory = "path/to/destination";
        
        exportHook.copyFromFile(dir, fileName, destinationDirectory);
        
        verify(exportHook.getLogger()).severe("Cannot find resource: " + dir + fileName);
        verify(exportHook.getResources()).logException(any(Exception.class));
    }
    
    @Test
    public void testCopyFromFile_FileFoundAndCopied() throws Exception {
        String dir = "path/to/dir";
        String fileName = "resource.txt";
        String destinationDirectory = "path/to/destination";
        
        File resourceFile = new File(dir, fileName);
        
        whenNew(File.class).withArguments(dir, fileName).thenReturn(resourceFile);
        
        InputStream mockInputStream = mock(InputStream.class);
        whenNew(FileInputStream.class).withArguments(resourceFile).thenReturn(mockInputStream);
        
        File destinationFile = new File(destinationDirectory + "/" + fileName);
        OutputStream mockOutputStream = mock(OutputStream.class);
        whenNew(FileOutputStream.class).withArguments(destinationFile).thenReturn(mockOutputStream);
        
        exportHook.copyFromFile(dir, fileName, destinationDirectory);
        
        verify(exportHook.getLogger()).finest("searching for " + dir + fileName);
        verifyNew(File.class).withArguments(dir, fileName);
        verifyNew(FileInputStream.class).withArguments(resourceFile);
        verifyNew(FileOutputStream.class).withArguments(destinationFile);
        verify(mockInputStream).close();
        verify(mockOutputStream).close();
    }
    
    private FileChooser mockFileChooserWithType(String type, String description) {
        FileChooser mockFileChooser = mock(FileChooser.class);
        FileFilter mockFileFilter = mock(FileFilter.class);
        when(mockFileFilter.getDescription()).thenReturn(description);
        when(mockFileChooser.getFileFilter()).thenReturn(mockFileFilter);
        
        File mockSelectedFile = mock(File.class);
        when(mockSelectedFile.getName()).thenReturn("file." + type);
        
        when(mockFileChooser.getSelectedFile()).thenReturn(mockSelectedFile);
        return mockFileChooser;
    }
}