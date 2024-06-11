import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;

import javax.swing.filechooser.FileFilter;

import java.io.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.MessageFormat;
import java.net.URL;

public class ExportHookTest {

    private ExportHook exportHook;
    private ModeController controller;
    private MapView view;
    private FreeMindFileDialog fileDialog;

    @Before
    public void setUp() {
        exportHook = Mockito.spy(new ExportHook());
        controller = Mockito.mock(ModeController.class);
        view = Mockito.mock(MapView.class);
        fileDialog = Mockito.mock(FreeMindFileDialog.class);

        Mockito.when(controller.getFrame()).thenReturn(new JFrame());
        Mockito.when(controller.getFrame().getContentPane()).thenReturn(new JPanel());
        Mockito.when(exportHook.getController()).thenReturn(controller);
        Mockito.when(exportHook.getController().getView()).thenReturn(view);
        Mockito.when(exportHook.getFileChooser(Mockito.any(FileFilter.class))).thenReturn(fileDialog);
    }

    @Test
    public void chooseFile_ValidTypeAndDescriptionAndNameExtension_ReturnsSelectedFile() {
        File expectedFile = new File("test.png");
        Mockito.when(fileDialog.showSaveDialog(Mockito.any(Container.class))).thenReturn(0);
        Mockito.when(fileDialog.getSelectedFile()).thenReturn(expectedFile);

        File actualFile = exportHook.chooseFile("png", "PNG Image", null);

        assertEquals(expectedFile, actualFile);
    }

    @Test
    public void chooseFile_CancelDialog_ReturnsNull() {
        Mockito.when(fileDialog.showSaveDialog(Mockito.any(Container.class))).thenReturn(1);

        File actualFile = exportHook.chooseFile("png", "PNG Image", null);

        assertNull(actualFile);
    }

    @Test
    public void chooseImageFile_ValidTypeAndDescriptionAndNameExtension_ReturnsSelectedFileWithCorrectExtension() {
        File expectedFile = new File("test.png");
        Mockito.when(fileDialog.showSaveDialog(Mockito.any(Container.class))).thenReturn(0);
        Mockito.when(fileDialog.getSelectedFile()).thenReturn(new File("test"));
        Mockito.when(exportHook.getController().getText(Mockito.anyString())).thenReturn("file_already_exists");
        Mockito.when(exportHook.getResourceString(Mockito.anyString())).thenReturn("filenames.png");

        File actualFile = exportHook.chooseImageFile("png", "PNG Image", null, controller);

        assertEquals(expectedFile, actualFile);
    }

    @Test
    public void chooseImageFile_ExistingFileConfirmationDialogCancelled_ReturnsNull() {
        File expectedFile = new File("test.png");
        File selectedFile = new File("test");
        Mockito.when(fileDialog.showSaveDialog(Mockito.any(Container.class))).thenReturn(0);
        Mockito.when(fileDialog.getSelectedFile()).thenReturn(selectedFile);
        Mockito.when(selectedFile.getName()).thenReturn("test.png");
        Mockito.when(selectedFile.getParent()).thenReturn(".");
        Mockito.when(selectedFile.exists()).thenReturn(true);
        Mockito.when(controller.getText(Mockito.anyString())).thenReturn("file_already_exists");
        Mockito.when(JOptionPane.showConfirmDialog(Mockito.any(Component.class), Mockito.anyString(), Mockito.anyString(), Mockito.anyInt()))
                .thenReturn(1);

        File actualFile = exportHook.chooseImageFile("png", "PNG Image", null, controller);

        assertNull(actualFile);
    }

    @Test
    public void chooseImageFile_ExistingFileConfirmationDialogConfirmed_ReturnsFileWithCorrectExtension() {
        File expectedFile = new File("test.png");
        File selectedFile = new File("test");
        Mockito.when(fileDialog.showSaveDialog(Mockito.any(Container.class))).thenReturn(0);
        Mockito.when(fileDialog.getSelectedFile()).thenReturn(selectedFile);
        Mockito.when(selectedFile.getName()).thenReturn("test");
        Mockito.when(selectedFile.getParent()).thenReturn(".");
        Mockito.when(selectedFile.exists()).thenReturn(true);
        Mockito.when(controller.getText(Mockito.anyString())).thenReturn("file_already_exists");
        Mockito.when(JOptionPane.showConfirmDialog(Mockito.any(Component.class), Mockito.anyString(), Mockito.anyString(), Mockito.anyInt()))
                .thenReturn(0);

        File actualFile = exportHook.chooseImageFile("png", "PNG Image", null, controller);

        assertEquals(expectedFile, actualFile);
    }

    @Test
    public void chooseImageFile_InvalidExtension_ReturnsFileWithCorrectExtension() {
        File expectedFile = new File("test.png");
        Mockito.when(fileDialog.showSaveDialog(Mockito.any(Container.class))).thenReturn(0);
        Mockito.when(fileDialog.getSelectedFile()).thenReturn(new File("test"));
        Mockito.when(exportHook.getController().getText(Mockito.anyString())).thenReturn("file_already_exists");

        File actualFile = exportHook.chooseImageFile("jpg", "JPEG Image", null, controller);

        assertEquals(expectedFile, actualFile);
    }

    @Test
    public void chooseImageFile_NullSelectedFile_ReturnsNull() {
        Mockito.when(fileDialog.showSaveDialog(Mockito.any(Container.class))).thenReturn(0);
        Mockito.when(fileDialog.getSelectedFile()).thenReturn(null);

        File actualFile = exportHook.chooseImageFile("png", "PNG Image", null, controller);

        assertNull(actualFile);
    }

    @Test
    public void getTranslatableResourceString_TranslatableResource_ReturnsTranslatedString() {
        String resourceName = "%file_already_exists";
        String expectedString = "File Already Exists";
        Mockito.when(exportHook.getResourceString(Mockito.anyString())).thenReturn(resourceName);
        Mockito.when(exportHook.getController().getText(resourceName.substring(1))).thenReturn(expectedString);

        String actualString = exportHook.getTranslatableResourceString(resourceName);

        assertEquals(expectedString, actualString);
    }

    @Test
    public void getTranslatableResourceString_NonTranslatableResource_ReturnsString() {
        String resourceName = "file_already_exists";
        String expectedString = "file_already_exists";
        Mockito.when(exportHook.getResourceString(Mockito.anyString())).thenReturn(resourceName);

        String actualString = exportHook.getTranslatableResourceString(resourceName);

        assertEquals(expectedString, actualString);
    }

    @Test
    public void createBufferedImage_ViewNotNull_CreatesBufferedImage() {
        Mockito.when(view.preparePrinting()).thenReturn(true);
        Mockito.when(view.getInnerBounds()).thenReturn(new Rectangle(0, 0, 100, 100));
        Mockito.when(view.createImage(Mockito.anyInt(), Mockito.anyInt())).thenReturn(Mockito.mock(BufferedImage.class));

        BufferedImage image = exportHook.createBufferedImage();

        assertNotNull(image);
    }

    @Test
    public void createBufferedImage_ViewNull_ReturnsNull() {
        Mockito.when(exportHook.getController().getView()).thenReturn(null);

        BufferedImage image = exportHook.createBufferedImage();

        assertNull(image);
    }

    @Test
    public void copyFromResource_ResourceFoundAndCopied_SuccessfullyCopied() throws Exception {
        String prefix = "images/";
        String fileName = "logo.png";
        String destinationDirectory = "dest";
        URL resource = new URL("http://example.com/logo.png");
        InputStream inputStream = Mockito.mock(InputStream.class);
        OutputStream outputStream = Mockito.mock(OutputStream.class);

        exportHook.copyFromResource(prefix, fileName, destinationDirectory);

        Mockito.verify(exportHook, Mockito.times(1)).logger.finest("searching for " + prefix + fileName);
        Mockito.when(exportHook.getResource(prefix + fileName)).thenReturn(resource);
        Mockito.when(resource.openStream()).thenReturn(inputStream);
        Mockito.when(new FileOutputStream(destinationDirectory + "/" + fileName)).thenReturn(outputStream);
        Mockito.verify(inputStream, Mockito.times(1)).close();
        Mockito.verify(outputStream, Mockito.times(1)).close();
    }

    @Test
    public void copyFromResource_ResourceNotFound_LogsError() {
        String prefix = "images/";
        String fileName = "logo.png";
        String destinationDirectory = "dest";

        exportHook.copyFromResource(prefix, fileName, destinationDirectory);

        Mockito.verify(exportHook, Mockito.times(1)).logger.finest("searching for " + prefix + fileName);
        Mockito.when(exportHook.getResource(prefix + fileName)).thenReturn(null);
        Mockito.verify(exportHook, Mockito.times(1)).logger.severe("Cannot find resource: " + prefix + fileName);
    }

    @Test
    public void copyFromFile_ResourceFoundAndCopied_SuccessfullyCopied() throws Exception {
        String dir = "src";
        String fileName = "test.txt";
        String destinationDirectory = "dest";
        File resource = new File(dir, fileName);
        InputStream inputStream = Mockito.mock(InputStream.class);
        OutputStream outputStream = Mockito.mock(OutputStream.class);

        exportHook.copyFromFile(dir, fileName, destinationDirectory);

        Mockito.verify(exportHook, Mockito.times(1)).logger.finest("searching for " + dir + fileName);
        Mockito.when(exportHook.getResource(dir + fileName)).thenReturn(resource);
        Mockito.when(new FileInputStream(resource)).thenReturn(inputStream);
        Mockito.when(new FileOutputStream(destinationDirectory + "/" + fileName)).thenReturn(outputStream);
        Mockito.verify(inputStream, Mockito.times(1)).close();
        Mockito.verify(outputStream, Mockito.times(1)).close();
    }

    @Test
    public void copyFromFile_ResourceNotFound_LogsError() {
        String dir = "src";
        String fileName = "test.txt";
        String destinationDirectory = "dest";

        exportHook.copyFromFile(dir, fileName, destinationDirectory);

        Mockito.verify(exportHook, Mockito.times(1)).logger.finest("searching for " + dir + fileName);
        Mockito.when(exportHook.getResource(dir + fileName)).thenReturn(null);
        Mockito.verify(exportHook, Mockito.times(1)).logger.severe("Cannot find resource: " + dir + fileName);
    }

    @Test
    public void ImageFilter_AcceptDirectory_ReturnsTrue() {
        File directory = Mockito.mock(File.class);
        Mockito.when(directory.isDirectory()).thenReturn(true);

        ExportHook.ImageFilter filter = new ExportHook.ImageFilter("png", null);
        boolean result = filter.accept(directory);

        assertTrue(result);
    }

    @Test
    public void ImageFilter_AcceptFileWithMatchingExtension_ReturnsTrue() {
        File file = Mockito.mock(File.class);
        Mockito.when(file.isDirectory()).thenReturn(false);
        Mockito.when(file.getName()).thenReturn("test.png");
        Mockito.when(Tools.getExtension("test.png")).thenReturn("png");

        ExportHook.ImageFilter filter = new ExportHook.ImageFilter("png", null);
        boolean result = filter.accept(file);

        assertTrue(result);
    }

    @Test
    public void ImageFilter_AcceptFileWithNonMatchingExtension_ReturnsFalse() {
        File file = Mockito.mock(File.class);
        Mockito.when(file.isDirectory()).thenReturn(false);
        Mockito.when(file.getName()).thenReturn("test.png");
        Mockito.when(Tools.getExtension("test.png")).thenReturn("jpg");

        ExportHook.ImageFilter filter = new ExportHook.ImageFilter("png", null);
        boolean result = filter.accept(file);

        assertFalse(result);
    }

    @Test
    public void ImageFilter_GetDescription_DescriptionNotNull_ReturnsDescription() {
        String type = "png";
        String description = "PNG Image";

        ExportHook.ImageFilter filter = new ExportHook.ImageFilter(type, description);
        String result = filter.getDescription();

        assertEquals(description, result);
    }

    @Test
    public void ImageFilter_GetDescription_DescriptionNull_ReturnsType() {
        String type = "png";

        ExportHook.ImageFilter filter = new ExportHook.ImageFilter(type, null);
        String result = filter.getDescription();

        assertEquals(type, result);
    }

}