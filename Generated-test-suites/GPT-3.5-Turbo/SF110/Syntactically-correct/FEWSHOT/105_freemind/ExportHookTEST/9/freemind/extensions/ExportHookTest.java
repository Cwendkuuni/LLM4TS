import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import freemind.extensions.ExportHook;
import freemind.main.ModeController;
import freemind.view.mindmapview.MapView;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.text.MessageFormat;

public class ExportHookTest {

    private ExportHook exportHook;
    private ModeController modeController;
    private MapView mapView;
    private FileDialog fileDialog;
    private File mockFile;

    @Before
    public void setup() {
        exportHook = new ExportHook();
        modeController = mock(ModeController.class);
        mapView = mock(MapView.class);
        fileDialog = mock(FileDialog.class);
        mockFile = mock(File.class);

        when(modeController.getFrame()).thenReturn(new Frame());
        when(modeController.getFrame().getContentPane()).thenReturn(new Container());
        when(modeController.getFileChooser(any())).thenReturn(fileDialog);
        when(modeController.getMap().getFile()).thenReturn(mockFile);

        exportHook.setController(modeController);
    }

    @Test
    public void testChooseImageFile_validParameters() {
        ImageFilter imageFilter = new ImageFilter("png", "PNG Image");

        when(fileDialog.showSaveDialog(any())).thenReturn(0);
        when(fileDialog.getSelectedFile()).thenReturn(mockFile);
        when(mockFile.getName()).thenReturn("image.png");
        when(Tools.getExtension("image.png")).thenReturn("png");
        when(Tools.safeEqualsIgnoreCase("png", "png")).thenReturn(true);

        File result = ExportHook.chooseImageFile("png", "PNG Image", ".png", modeController);

        assertEquals(mockFile, result);
        verify(fileDialog).setSelectedFile(any(File.class));
        verify(mockFile).exists();
        verify(modeController).getText(anyString());
    }

    @Test
    public void testChooseImageFile_invalidParameters() {
        ImageFilter imageFilter = new ImageFilter("jpg", "JPG Image");

        when(fileDialog.showSaveDialog(any())).thenReturn(1);

        File result = ExportHook.chooseImageFile("jpg", "JPG Image", ".jpg", modeController);

        assertNull(result);
        verify(fileDialog, never()).setSelectedFile(any(File.class));
        verify(modeController, never()).getText(anyString());
    }

    @Test
    public void testChooseFile_validParameters() {
        when(modeController.getFrame()).thenReturn(new Frame());
        when(modeController.getFrame().getContentPane()).thenReturn(new Container());
        when(modeController.getFileChooser(any())).thenReturn(fileDialog);
        when(modeController.getMap().getFile()).thenReturn(mockFile);

        File result = exportHook.chooseFile("png", "PNG Image", ".png");

        assertEquals(mockFile, result);
        verify(fileDialog).setSelectedFile(any(File.class));
        verify(mockFile).exists();
        verify(modeController).getText(anyString());
    }

    @Test
    public void testChooseFile_invalidParameters() {
        when(modeController.getFrame()).thenReturn(new Frame());
        when(modeController.getFrame().getContentPane()).thenReturn(new Container());
        when(modeController.getFileChooser(any())).thenReturn(fileDialog);
        when(modeController.getMap().getFile()).thenReturn(null);

        File result = exportHook.chooseFile("png", "PNG Image", ".png");

        assertNull(result);
        verify(fileDialog, never()).setSelectedFile(any(File.class));
        verify(modeController, never()).getText(anyString());
    }

    @Test
    public void testGetTranslatableResourceString_validResourceName() {
        String resourceName = "%resourceName";
        when(exportHook.getResourceString(resourceName)).thenReturn("%translatedValue");
        when(modeController.getText("translatedValue")).thenReturn("Translated Value");

        String result = exportHook.getTranslatableResourceString(resourceName);

        assertEquals("Translated Value", result);
        verify(exportHook).getResourceString(resourceName);
        verify(modeController).getText("translatedValue");
    }

    @Test
    public void testGetTranslatableResourceString_invalidResourceName() {
        String resourceName = "resourceName";
        when(exportHook.getResourceString(resourceName)).thenReturn("Resource Value");

        String result = exportHook.getTranslatableResourceString(resourceName);

        assertEquals("Resource Value", result);
        verify(exportHook).getResourceString(resourceName);
        verify(modeController, never()).getText(anyString());
    }

    @Test
    public void testCreateBufferedImage_validMapView() {
        when(modeController.getView()).thenReturn(mapView);
        when(mapView.getWidth()).thenReturn(100);
        when(mapView.getHeight()).thenReturn(100);
        when(mapView.preparePrinting()).thenReturn(true);
        when(mapView.getInnerBounds()).thenReturn(new Rectangle(0, 0, 100, 100));
        when(mapView.createImage(100, 100)).thenReturn(new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB));

        BufferedImage result = exportHook.createBufferedImage();

        assertEquals(new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB), result);
        verify(mapView).preparePrinting();
        verify(mapView).print(any(Graphics.class));
        verify(mapView).endPrinting();
    }

    @Test
    public void testCreateBufferedImage_invalidMapView() {
        BufferedImage result = exportHook.createBufferedImage();

        assertNull(result);
        verify(modeController).getView();
        verify(modeController, never()).preparePrinting();
    }

    @Test
    public void testCopyFromResource_validParameters() throws IOException {
        String prefix = "prefix/";
        String fileName = "file.txt";
        String destinationDirectory = "destination";

        URL resource = new URL("https://example.com/resource");
        InputStream inputStream = mock(InputStream.class);
        OutputStream outputStream = mock(OutputStream.class);

        when(exportHook.getResource(prefix + fileName)).thenReturn(resource);
        when(resource.openStream()).thenReturn(inputStream);
        when(mockFile.getAbsolutePath()).thenReturn(destinationDirectory + "/" + fileName);
        when(mockFile.getName()).thenReturn(fileName);
        when(mockFile.getParent()).thenReturn(destinationDirectory);
        when(new FileOutputStream(destinationDirectory + "/" + fileName)).thenReturn(outputStream);

        exportHook.copyFromResource(prefix, fileName, destinationDirectory);

        verify(exportHook).getResource(prefix + fileName);
        verify(resource).openStream();
        verify(Tools).copyStream(inputStream, outputStream, true);
    }

    @Test
    public void testCopyFromResource_invalidParameters() throws IOException {
        String prefix = "prefix/";
        String fileName = "file.txt";
        String destinationDirectory = "destination";

        when(exportHook.getResource(prefix + fileName)).thenReturn(null);

        exportHook.copyFromResource(prefix, fileName, destinationDirectory);

        verify(exportHook).getResource(prefix + fileName);
        verify(exportHook.logger).severe("Cannot find resource: prefix/file.txt");
        verify(exportHook.logger, never()).finest("searching for " + prefix + fileName);
        verify(Tools, never()).copyStream(any(InputStream.class), any(OutputStream.class), anyBoolean());
        verify(Resources.getInstance()).logException(any(Exception.class));
    }

    @Test
    public void testCopyFromFile_validParameters() throws IOException {
        String dir = "dir/";
        String fileName = "file.txt";
        String destinationDirectory = "destination";

        File resource = new File(dir, fileName);
        InputStream inputStream = mock(InputStream.class);
        OutputStream outputStream = mock(OutputStream.class);

        when(new FileInputStream(resource)).thenReturn(inputStream);
        when(mockFile.getAbsolutePath()).thenReturn(destinationDirectory + "/" + fileName);
        when(mockFile.getName()).thenReturn(fileName);
        when(mockFile.getParent()).thenReturn(destinationDirectory);
        when(new FileOutputStream(destinationDirectory + "/" + fileName)).thenReturn(outputStream);

        exportHook.copyFromFile(dir, fileName, destinationDirectory);

        verify(new FileInputStream(resource)).close();
        verify(Tools).copyStream(inputStream, outputStream, true);
    }

    @Test
    public void testCopyFromFile_invalidParameters() throws IOException {
        String dir = "dir/";
        String fileName = "file.txt";
        String destinationDirectory = "destination";

        when(new File(dir, fileName)).thenReturn(null);

        exportHook.copyFromFile(dir, fileName, destinationDirectory);

        verify(exportHook.logger).severe("Cannot find resource: dir/file.txt");
        verify(exportHook.logger, never()).finest("searching for " + dir + fileName);
        verify(Tools, never()).copyStream(any(InputStream.class), any(OutputStream.class), anyBoolean());
        verify(Resources.getInstance(), never()).logException(any(Exception.class));
    }

    @Test
    public void testImageFilter_accept() {
        File file = mock(File.class);
        when(file.isDirectory()).thenReturn(true);

        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("png", "PNG Image");
        boolean result = imageFilter.accept(file);

        assertEquals(true, result);
        verify(file).isDirectory();
    }

    @Test
    public void testImageFilter_accept_validExtension() {
        File file = mock(File.class);
        when(file.isDirectory()).thenReturn(false);
        when(file.getName()).thenReturn("image.png");
        when(Tools.getExtension("image.png")).thenReturn("png");
        when(Tools.safeEqualsIgnoreCase("png", "png")).thenReturn(true);

        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("png", "PNG Image");
        boolean result = imageFilter.accept(file);

        assertEquals(true, result);
        verify(file).isDirectory();
        verify(file).getName();
        verify(Tools).getExtension("image.png");
        verify(Tools).safeEqualsIgnoreCase("png", "png");
    }

    @Test
    public void testImageFilter_accept_invalidExtension() {
        File file = mock(File.class);
        when(file.isDirectory()).thenReturn(false);
        when(file.getName()).thenReturn("image.jpg");
        when(Tools.getExtension("image.jpg")).thenReturn("jpg");
        when(Tools.safeEqualsIgnoreCase("jpg", "png")).thenReturn(false);

        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("png", "PNG Image");
        boolean result = imageFilter.accept(file);

        assertEquals(false, result);
        verify(file).isDirectory();
        verify(file).getName();
        verify(Tools).getExtension("image.jpg");
        verify(Tools).safeEqualsIgnoreCase("jpg", "png");
    }

    @Test
    public void testImageFilter_getDescription_withDescription() {
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("png", "PNG Image");
        String result = imageFilter.getDescription();

        assertEquals("PNG Image", result);
    }

    @Test
    public void testImageFilter_getDescription_withoutDescription() {
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("png", null);
        String result = imageFilter.getDescription();

        assertEquals("png", result);
    }
}