package freemind.extensions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import freemind.view.mindmapview.ModeController;
import freemind.view.mindmapview.MapView;
import freemind.view.mindmapview.FreeMindFileDialog;
import freemind.extensions.ExportHook.ImageFilter;
import freemind.main.Tools;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.MessageFormat;

public class ExportHookTest {

    private ExportHook exportHook;
    private ModeController mockModeController;
    private MapView mockView;

    @Before
    public void setUp() {
        exportHook = new ExportHook();
        mockModeController = mock(ModeController.class);
        mockView = mock(MapView.class);
        when(mockModeController.getFrame()).thenReturn(new JFrame());
        when(mockModeController.getMap()).thenReturn(mockView);
        when(mockModeController.getFileChooser(any(FileFilter.class))).thenReturn(new FreeMindFileDialog());
        exportHook.setController(mockModeController);
    }

    @Test
    public void testChooseImageFile() {
        File chosenFile = ExportHook.chooseImageFile("jpg", "JPEG Image", ".jpg", mockModeController);
        assertNotNull(chosenFile);
        assertEquals(".jpg", Tools.getExtension(chosenFile.getName()));
        when(mockModeController.getMap().getFile()).thenReturn(new File("file.mm"));
        chosenFile = ExportHook.chooseImageFile("png", "PNG Image", ".png", mockModeController);
        assertNotNull(chosenFile);
        assertEquals(".png", Tools.getExtension(chosenFile.getName()));
        chosenFile = ExportHook.chooseImageFile("bmp", "BMP Image", ".bmp", mockModeController);
        assertNotNull(chosenFile);
        assertEquals(".bmp", Tools.getExtension(chosenFile.getName()));
    }

    @Test
    public void testChooseImageFile_Cancel() {
        when(mockModeController.getFileChooser(any(FileFilter.class))).thenReturn(null);
        File chosenFile = ExportHook.chooseImageFile("jpg", "JPEG Image", ".jpg", mockModeController);
        assertNull(chosenFile);
    }

    @Test
    public void testChooseImageFile_FileAlreadyExists() {
        FreeMindFileDialog mockFileChooser = mock(FreeMindFileDialog.class);
        when(mockModeController.getFileChooser(any(FileFilter.class))).thenReturn(mockFileChooser);
        when(mockFileChooser.showSaveDialog(any(Container.class))).thenReturn(0);
        when(mockFileChooser.getSelectedFile()).thenReturn(new File("existing.jpg"));
        when(mockModeController.getText("file_already_exists")).thenReturn("File already exists");
        int overwriteMap = JOptionPane.YES_OPTION;
        when(mockModeController.getView()).thenReturn(null);
        when(mockModeController.getText(any(String.class))).thenReturn("OK");
        when(mockModeController.getFrame().getContentPane()).thenReturn(new JPanel());
        when(JOptionPane.showConfirmDialog(any(Container.class), any(String.class), any(String.class), anyInt())).thenReturn(overwriteMap);
        File chosenFile = ExportHook.chooseImageFile("jpg", "JPEG Image", ".jpg", mockModeController);
        assertNull(chosenFile);
    }

    @Test
    public void testGetTranslatableResourceString() {
        String translatableString = exportHook.getTranslatableResourceString("%text");
        assertNotNull(translatableString);
        assertEquals("text", translatableString);
        translatableString = exportHook.getTranslatableResourceString(null);
        assertNull(translatableString);
    }

    @Test
    public void testCreateBufferedImage() {
        when(mockModeController.getView()).thenReturn(mockView);
        when(mockView.getWidth()).thenReturn(100);
        when(mockView.getHeight()).thenReturn(100);
        when(mockView.preparePrinting()).thenReturn(null);
        when(mockView.getInnerBounds()).thenReturn(new Rectangle(0, 0, 100, 100));
        BufferedImage bufferedImage = new BufferedImage(100, 100, BufferedImage.TYPE_BYTE_BINARY);
        Graphics graphics = bufferedImage.getGraphics();
        when(mockView.createImage(100, 100)).thenReturn(bufferedImage);
        when(mockView.print(any(Graphics.class))).thenReturn(null);
        BufferedImage myImage = exportHook.createBufferedImage();
        assertNotNull(myImage);
        assertEquals(100, myImage.getWidth());
        assertEquals(100, myImage.getHeight());
    }

    @Test
    public void testCopyFromResource() {
        try {
            URL mockResource = mock(URL.class);
            when(exportHook.getResource(any(String.class))).thenReturn(mockResource);
            InputStream mockInputStream = mock(InputStream.class);
            when(mockResource.openStream()).thenReturn(mockInputStream);
            OutputStream mockOutputStream = mock(OutputStream.class);
            whenNew(FileOutputStream.class).withAnyArguments().thenReturn(mockOutputStream);
            Tools.copyStream(mockInputStream, mockOutputStream, true);
            exportHook.copyFromResource("path/", "file.jpg", "destination");
            verify(exportHook, times(1)).getResource("path/file.jpg");
            verify(exportHook, times(1)).getResourceString("path/file.jpg");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCopyFromResource_ResourceNotFound() {
        exportHook.copyFromResource("path/", "nonexisting.jpg", "destination");
        verify(exportHook, times(1)).getResource("path/nonexisting.jpg");
        verify(exportHook, times(1)).getResourceString("path/nonexisting.jpg");
    }

    @Test
    public void testCopyFromFile() {
        try {
            File mockFile = mock(File.class);
            when(mockFile.getAbsolutePath()).thenReturn("dir/file.txt");
            whenNew(File.class).withAnyArguments().thenReturn(mockFile);
            InputStream mockInputStream = mock(InputStream.class);
            whenNew(FileInputStream.class).withAnyArguments().thenReturn(mockInputStream);
            OutputStream mockOutputStream = mock(OutputStream.class);
            whenNew(FileOutputStream.class).withAnyArguments().thenReturn(mockOutputStream);
            Tools.copyStream(mockInputStream, mockOutputStream, true);
            exportHook.copyFromFile("dir/", "file.txt", "destination");
            verify(exportHook, times(1)).getResourceString("dir/file.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCopyFromFile_ResourceNotFound() {
        exportHook.copyFromFile("dir/", "nonexisting.txt", "destination");
        verify(exportHook, times(1)).getResourceString("dir/nonexisting.txt");
    }

    @Test
    public void testImageFilter_AcceptDirectory() {
        File file = new File("directory/");
        ImageFilter imageFilter = new ImageFilter("jpg", "JPEG Image");
        boolean accepted = imageFilter.accept(file);
        assertEquals(true, accepted);
    }

    @Test
    public void testImageFilter_AcceptJPEGImage() {
        File file = new File("file.jpg");
        ImageFilter imageFilter = new ImageFilter("jpg", "JPEG Image");
        boolean accepted = imageFilter.accept(file);
        assertEquals(true, accepted);
    }

    @Test
    public void testImageFilter_RejectNonJPEGImage() {
        File file = new File("file.png");
        ImageFilter imageFilter = new ImageFilter("jpg", "JPEG Image");
        boolean accepted = imageFilter.accept(file);
        assertEquals(false, accepted);
    }

    @Test
    public void testGetDescription() {
        ImageFilter imageFilterWithDescription = new ImageFilter("jpg", "JPEG Image");
        ImageFilter imageFilterWithoutDescription = new ImageFilter("png", null);
        String descriptionWithDescription = imageFilterWithDescription.getDescription();
        String descriptionWithoutDescription = imageFilterWithoutDescription.getDescription();
        assertEquals("JPEG Image", descriptionWithDescription);
        assertEquals("png", descriptionWithoutDescription);
    }
}