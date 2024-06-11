import freemind.extensions.ExportHook;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.logging.Logger;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ExportHookTest {

    private ExportHook exportHook;
    private ModeControllerHookAdapter.ModeController controller;
    private Container container;

    @Before
    public void setup() {
        exportHook = new ExportHook();
        controller = Mockito.mock(ModeControllerHookAdapter.ModeController.class);
        container = Mockito.mock(Container.class);
        JFrame frame = Mockito.mock(JFrame.class);
        when(controller.getFrame()).thenReturn(frame);
        when(frame.getContentPane()).thenReturn(container);
    }

    @Test
    public void testChooseFile() {
        File expectedFile = new File("test.jpg");
        File result = exportHook.chooseFile("jpg", "JPEG Image", null);
        assertEquals(expectedFile, result);
    }

    @Test
    public void testChooseImageFile() {
        File expectedFile = new File("test.jpg");
        File result = exportHook.chooseImageFile("jpg", "JPEG Image", null, controller);
        assertEquals(expectedFile, result);
    }

    @Test
    public void testChooseImageFileWithExistingFilePrompt() {
        File chosenFile = Mockito.mock(File.class);
        when(chosenFile.exists()).thenReturn(true);
        when(chooser.getSelectedFile()).thenReturn(chosenFile);
        when(controller.getText(anyString())).thenReturn("File Already Exists");
        int overwriteMap = 0;
        when(JOptionPane.showConfirmDialog(container, "File Already Exists", "File Already Exists", 0)).thenReturn(overwriteMap);
        File result = exportHook.chooseImageFile("jpg", "JPEG Image", null, controller);
        assertNull(result);
    }

    @Test
    public void testGetTranslatableResourceString() {
        String resourceName = "resourceName";
        String expectedValue = "Translated Resource";
        when(exportHook.getResourceString(resourceName)).thenReturn("%Translated Resource");
        when(controller.getText("Translated Resource")).thenReturn(expectedValue);
        String result = exportHook.getTranslatableResourceString(resourceName);
        assertEquals(expectedValue, result);
    }

    @Test
    public void testGetTranslatableResourceStringWithoutTranslation() {
        String resourceName = "resourceName";
        String expectedValue = "Resource Value";
        when(exportHook.getResourceString(resourceName)).thenReturn("Resource Value");
        String result = exportHook.getTranslatableResourceString(resourceName);
        assertEquals(expectedValue, result);
    }

    @Test
    public void testCreateBufferedImage() {
        Graphics graphics = Mockito.mock(Graphics.class);
        Image image = Mockito.mock(Image.class);
        BufferedImage bufferedImage = Mockito.mock(BufferedImage.class);
        Rectangle innerBounds = Mockito.mock(Rectangle.class);
        MapView view = Mockito.mock(MapView.class);
        when(controller.getView()).thenReturn(view);
        when(view.preparePrinting()).thenReturn(innerBounds);
        when(view.getInnerBounds()).thenReturn(innerBounds);
        when(view.createImage(view.getWidth(), view.getHeight())).thenReturn(image);
        when(image.getGraphics()).thenReturn(graphics);
        when(graphics.getClipBounds()).thenReturn(innerBounds);
        BufferedImage result = exportHook.createBufferedImage();
        assertEquals(bufferedImage, result);
    }

    @Test
    public void testCopyFromResource() throws IOException {
        String prefix = "resources/";
        String fileName = "test.txt";
        String destinationDirectory = "destination";
        String expectedFilePath = destinationDirectory + "/" + fileName;

        OutputStream outputStream = Mockito.mock(OutputStream.class);
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(prefix + fileName);
        File file = new File(expectedFilePath);

        ExportHook exportHookSpy = Mockito.spy(exportHook);
        Logger logger = Mockito.mock(Logger.class);
        doReturn(logger).when(exportHookSpy).getLogger();
        doReturn(inputStream).when(exportHookSpy).getResource(prefix + fileName);
        doReturn(outputStream).when(exportHookSpy).getOutputStream(expectedFilePath);

        exportHookSpy.copyFromResource(prefix, fileName, destinationDirectory);

        Mockito.verify(Files.class, times(1))
                .copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    public void testCopyFromFile() throws IOException {
        String dir = "resources/";
        String fileName = "test.txt";
        String destinationDirectory = "destination";
        String expectedFilePath = destinationDirectory + "/" + fileName;

        OutputStream outputStream = Mockito.mock(OutputStream.class);
        File inputFile = new File(dir, fileName);
        File outputFile = new File(expectedFilePath);

        ExportHook exportHookSpy = Mockito.spy(exportHook);
        Logger logger = Mockito.mock(Logger.class);
        doReturn(logger).when(exportHookSpy).getLogger();
        doReturn(new FileInputStream(inputFile)).when(exportHookSpy).getInputStream(inputFile);
        doReturn(outputStream).when(exportHookSpy).getOutputStream(expectedFilePath);

        exportHookSpy.copyFromFile(dir, fileName, destinationDirectory);

        Mockito.verify(Files.class, times(1))
                .copy(inputFile.toPath(), outputFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }
}