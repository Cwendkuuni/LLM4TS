import freemind.view.mindmapview.MapView;
import freemind.modes.ModeController;
import freemind.main.FreeMindFileDialog;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.mockito.Mockito;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.text.MessageFormat;
import java.util.logging.Logger;
import java.util.logging.Level;

public class ExportHookTest {

    private ExportHook exportHook;
    private ModeController modeController;
    private FileFilter fileFilter;
    private FileChooser fileChooser;
    private File file;
    private String resourceName;
    private String prefix;
    private JComponent component;
    private MapView mapView;
    private URL url;
    private InputStream inputStream;
    private OutputStream outputStream;
    private BufferedImage bufferedImage;
    private Graphics graphics;
    private Rectangle rectangle;
    
    @Before
    public void setup() {
        exportHook = new ExportHook();
        modeController = Mockito.mock(ModeController.class);
        fileFilter = Mockito.mock(FileFilter.class);
        fileChooser = Mockito.mock(FileChooser.class);
        file = Mockito.mock(File.class);
        resourceName = "testResource";
        prefix = "test";
        component = Mockito.mock(JComponent.class);
        mapView = Mockito.mock(MapView.class);
        url = Mockito.mock(URL.class);
        inputStream = Mockito.mock(InputStream.class);
        outputStream = Mockito.mock(OutputStream.class);
        bufferedImage = Mockito.mock(BufferedImage.class);
        graphics = Mockito.mock(Graphics.class);
        rectangle = Mockito.mock(Rectangle.class);
    }

    @After
    public void tearDown() {
        exportHook = null;
        modeController = null;
        fileFilter = null;
        fileChooser = null;
        file = null;
        resourceName = null;
        prefix = null;
        component = null;
        mapView = null;
        url = null;
        inputStream = null;
        outputStream = null;
        bufferedImage = null;
        graphics = null;
        rectangle = null;
    }

    @Test
    public void testChooseFile() {
        Mockito.when(modeController.getFrame()).thenReturn(frame);
        Mockito.when(frame.getContentPane()).thenReturn(component);
        Mockito.when(modeController.getFileChooser(fileFilter)).thenReturn(fileChooser);
        Mockito.when(controller.getMap().getFile()).thenReturn(file);
        Mockito.when(file.getAbsolutePath()).thenReturn("testDir/testFile");
        Mockito.when(file.getName()).thenReturn("testFileName");
        Mockito.when(chooser.getSelectedFile()).thenReturn(file);
        File chosenFile = exportHook.chooseFile("jpg", "jpg file", ".jpg");
        Mockito.verify(controller, Mockito.times(1)).getMap().getFile();
        Mockito.verify(frame, Mockito.times(1)).getContentPane();
        Mockito.verify(controller, Mockito.times(1)).getText("file_already_exists");
        Mockito.verify(controller, Mockito.times(1)).showConfirmDialog(component, "file_already_exists", "file_already_exists", JOptionPane.YES_NO_OPTION);
        Mockito.verify(chooser, Mockito.times(1)).setSelectedFile(Mockito.any(File.class));
        Mockito.verify(chooser, Mockito.times(1)).showSaveDialog(component);
        Mockito.verify(chooser, Mockito.times(1)).getSelectedFile();
        Mockito.verify(file, Mockito.times(1)).exists();
        Mockito.verify(file, Mockito.times(1)).getParent();
        Mockito.verify(file, Mockito.times(1)).getName();
    }

    @Test
    public void testChooseImageFile() {
        Mockito.when(modeController.getFrame()).thenReturn(frame);
        Mockito.when(frame.getContentPane()).thenReturn(component);
        Mockito.when(modeController.getFileChooser(fileFilter)).thenReturn(fileChooser);
        Mockito.when(controller.getMap().getFile()).thenReturn(file);
        Mockito.when(file.getAbsolutePath()).thenReturn("testDir/testFile");
        Mockito.when(file.getName()).thenReturn("testFileName");
        Mockito.when(chooser.getSelectedFile()).thenReturn(file);
        File chosenFile = exportHook.chooseImageFile("jpg", "jpg file", ".jpg", controller);
        Mockito.verify(controller, Mockito.times(1)).getMap().getFile();
        Mockito.verify(frame, Mockito.times(1)).getContentPane();
        Mockito.verify(controller, Mockito.times(1)).getText("file_already_exists");
        Mockito.verify(controller, Mockito.times(1)).showConfirmDialog(component, "file_already_exists", "file_already_exists", JOptionPane.YES_NO_OPTION);
        Mockito.verify(chooser, Mockito.times(1)).setSelectedFile(Mockito.any(File.class));
        Mockito.verify(chooser, Mockito.times(1)).showSaveDialog(component);
        Mockito.verify(chooser, Mockito.times(1)).getSelectedFile();
        Mockito.verify(file, Mockito.times(1)).exists();
        Mockito.verify(file, Mockito.times(1)).getParent();
        Mockito.verify(file, Mockito.times(1)).getName();
    }

    @Test
    public void testGetTranslatableResourceString() {
        Mockito.when(exportHook.getResourceString(resourceName)).thenReturn("%testString");
        Mockito.when(controller.getText("testString")).thenReturn("translatableString");
        String result = exportHook.getTranslatableResourceString(resourceName);
        Mockito.verify(controller, Mockito.times(1)).getText("testString");
    }

    @Test
    public void testCreateBufferedImage() {
        Mockito.when(controller.getView()).thenReturn(mapView);
        Mockito.when(mapView.getWidth()).thenReturn(100);
        Mockito.when(mapView.getHeight()).thenReturn(200);
        Mockito.when(mapView.getInnerBounds()).thenReturn(rectangle);
        Mockito.when(mapView.createImage(mapView.getWidth(), mapView.getHeight())).thenReturn(bufferedImage);
        Mockito.when(bufferedImage.getGraphics()).thenReturn(graphics);
        Mockito.when(graphics.create(0, 0, 100, 200)).thenReturn(graphics);
        Mockito.when(mapView.print(graphics)).thenReturn(true);
        BufferedImage image = exportHook.createBufferedImage();
        Mockito.verify(controller, Mockito.times(1)).getView();
        Mockito.verify(mapView, Mockito.times(1)).preparePrinting();
        Mockito.verify(bufferedImage, Mockito.times(1)).getGraphics();
        Mockito.verify(mapView, Mockito.times(1)).print(graphics);
        Mockito.verify(mapView, Mockito.times(1)).endPrinting();
    }

    @Test
    public void testCopyFromResource() throws Exception {
        Mockito.when(exportHook.getResource(prefix + fileName)).thenReturn(url);
        Mockito.when(url.openStream()).thenReturn(inputStream);
        Mockito.when(resource_1.getParent()).thenReturn("parent");
        Mockito.when(fileName.getName()).thenReturn("testFileName");
        FileUtils.copyInputStreamToFile(inputStream, (destinationDirectory + "/" + fileName));
        exportHook.copyFromResource(prefix, fileName, destinationDirectory);
        Mockito.verify(fileOutputStream_1, Mockito.times(1)).close();
    }

    @Test
    public void testCopyFromFile() throws Exception {
        Mockito.when(resource_1).thenReturn(file);
        Mockito.when(file.getAbsolutePath()).thenReturn("testDir/testFileName");
        Mockito.when(fileName.getParent()).thenReturn("parent");
        Mockito.when(fileInputStream_1).thenReturn(inputStream);
        Mockito.when(file.length()).thenReturn(1L);
        FileUtils.copyFile(file, (destinationDirectory + "/" + fileName));
        exportHook.copyFromFile(dir, fileName, destinationDirectory);
        Mockito.verify(fileInputStream_1, Mockito.times(1)).close();
        Mockito.verify(fileOutputStream_1, Mockito.times(1)).close();
    }

    @Test
    public void testImageFilterAccept() {
        File directory = Mockito.mock(File.class);
        Mockito.when(f.isDirectory()).thenReturn(true);
        boolean result = imageFilter.accept(f);
        Mockito.verify(f, Mockito.times(1)).isDirectory();
    }

    @Test
    public void testImageFilterGetDescription() {
        imageFilter = new ImageFilter("jpg", "jpg file");
        String result = imageFilter.getDescription();
    }
}