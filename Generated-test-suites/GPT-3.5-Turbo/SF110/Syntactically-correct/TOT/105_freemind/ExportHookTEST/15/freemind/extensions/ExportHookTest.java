import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;

public class ExportHookTest {

    @Test
    public void testChooseFileTypical() {
        // Typical use case
        ExportHook exportHook = new ExportHook();
        File result = exportHook.chooseFile("png", "Image file", "_export");
        assertNotNull(result);
        assertTrue(result.isFile());
        assertEquals("_export.png", result.getName());
    }

    @Test
    public void testChooseFileNull() {
        // Error scenario: mmFile is null
        ExportHook exportHook = new ExportHook();
        exportHook.getController().getMap().setFile(null);
        File result = exportHook.chooseFile("jpg", "Image file", "_export");
        assertNull(result);
    }

    @Test
    public void testChooseFileDirectory() {
        // Error scenario: chosen file is a directory
        ExportHook exportHook = new ExportHook();
        File result = exportHook.chooseFile("gif", "Image file", "_export");
        assertNull(result);
    }

    @Test
    public void testChooseImageFileTypical() {
        // Typical use case
        ExportHook exportHook = new ExportHook();
        ModeController controller = exportHook.getController();
        Container container = Mockito.mock(Container.class);
        Frame frame = Mockito.mock(Frame.class);
        Mockito.when(controller.getFrame()).thenReturn(frame);
        Mockito.when(frame.getContentPane()).thenReturn(container);
        File result = ExportHook.chooseImageFile("svg", "Scalable Vector Graphics", "_export", controller);
        assertNotNull(result);
        assertTrue(result.isFile());
        assertEquals("_export.svg", result.getName());
    }

    @Test
    public void testChooseImageFileNoSelectedFile() {
        // Error scenario: returnVal is not 0
        ExportHook exportHook = new ExportHook();
        ModeController controller = exportHook.getController();
        Container container = Mockito.mock(Container.class);
        Frame frame = Mockito.mock(Frame.class);
        Mockito.when(controller.getFrame()).thenReturn(frame);
        Mockito.when(frame.getContentPane()).thenReturn(container);
        FreeMindFileDialog chooser = Mockito.mock(FreeMindFileDialog.class);
        Mockito.when(controller.getFileChooser(Mockito.any(ImageFilter.class))).thenReturn(chooser);
        Mockito.when(chooser.showSaveDialog(container)).thenReturn(1);
        File result = ExportHook.chooseImageFile("bmp", "Bitmap Image", "_export", controller);
        assertNull(result);
    }

    @Test
    public void testChooseImageFileInvalidExtension() {
        // Typical use case: ext is different from type
        ExportHook exportHook = new ExportHook();
        ModeController controller = exportHook.getController();
        Container container = Mockito.mock(Container.class);
        Frame frame = Mockito.mock(Frame.class);
        Mockito.when(controller.getFrame()).thenReturn(frame);
        Mockito.when(frame.getContentPane()).thenReturn(container);
        FreeMindFileDialog chooser = Mockito.mock(FreeMindFileDialog.class);
        Mockito.when(controller.getFileChooser(Mockito.any(ImageFilter.class))).thenReturn(chooser);
        Mockito.when(chooser.showSaveDialog(container)).thenReturn(0);
        Mockito.when(chooser.getSelectedFile()).thenReturn(new File("output.txt"));
        File result = ExportHook.chooseImageFile("png", "Image file", "_export", controller);
        assertNotNull(result);
        assertTrue(result.isFile());
        assertEquals("output.txt.png", result.getName());
    }

    @Test
    public void testChooseImageFileExistsOverwrite() {
        // Typical use case: chosen file already exists and user chooses to overwrite
        ExportHook exportHook = new ExportHook();
        ModeController controller = exportHook.getController();
        Container container = Mockito.mock(Container.class);
        Frame frame = Mockito.mock(Frame.class);
        Mockito.when(controller.getFrame()).thenReturn(frame);
        Mockito.when(frame.getContentPane()).thenReturn(container);
        FreeMindFileDialog chooser = Mockito.mock(FreeMindFileDialog.class);
        Mockito.when(controller.getFileChooser(Mockito.any(ImageFilter.class))).thenReturn(chooser);
        Mockito.when(chooser.showSaveDialog(container)).thenReturn(0);
        Mockito.when(chooser.getSelectedFile()).thenReturn(new File("output.png"));
        Mockito.when(controller.getText("file_already_exists")).thenReturn("The file already exists");
        JOptionPane optionPane = Mockito.mock(JOptionPane.class);
        Mockito.when(optionPane.showConfirmDialog(container, "The file already exists", "The file already exists", 0)).thenReturn(0);
        File result = ExportHook.chooseImageFile("png", "Image file", "_export", controller);
        assertNotNull(result);
        assertTrue(result.isFile());
        assertEquals("output.png", result.getName());
    }

    @Test
    public void testChooseImageFileExistsCancel() {
        // Error scenario: chosen file already exists and user cancels
        ExportHook exportHook = new ExportHook();
        ModeController controller = exportHook.getController();
        Container container = Mockito.mock(Container.class);
        Frame frame = Mockito.mock(Frame.class);
        Mockito.when(controller.getFrame()).thenReturn(frame);
        Mockito.when(frame.getContentPane()).thenReturn(container);
        FreeMindFileDialog chooser = Mockito.mock(FreeMindFileDialog.class);
        Mockito.when(controller.getFileChooser(Mockito.any(ImageFilter.class))).thenReturn(chooser);
        Mockito.when(chooser.showSaveDialog(container)).thenReturn(0);
        Mockito.when(chooser.getSelectedFile()).thenReturn(new File("output.png"));
        Mockito.when(controller.getText("file_already_exists")).thenReturn("The file already exists");
        JOptionPane optionPane = Mockito.mock(JOptionPane.class);
        Mockito.when(optionPane.showConfirmDialog(container, "The file already exists", "The file already exists", 0)).thenReturn(1);
        File result = ExportHook.chooseImageFile("png", "Image file", "_export", controller);
        assertNull(result);
    }

    @Test
    public void testGetTranslatableResourceStringTypical() {
        // Typical use case
        ExportHook exportHook = new ExportHook();
        exportHook.getController().setText("resourceKey", "Translated Text");
        String result = exportHook.getTranslatableResourceString("%resourceKey");
        assertEquals("Translated Text", result);
    }

    @Test
    public void testGetTranslatableResourceStringInvalidKey() {
        // Edge case: resource name starts with % but is not a valid key
        ExportHook exportHook = new ExportHook();
        String result = exportHook.getTranslatableResourceString("%invalidKey");
        assertEquals("%invalidKey", result);
    }

    @Test
    public void testCreateBufferedImageTypical() {
        // Typical use case
        ExportHook exportHook = new ExportHook();
        ModeController controller = Mockito.mock(ModeController.class);
        MapView view = Mockito.mock(MapView.class);
        Rectangle innerBounds = new Rectangle(10, 10, 100, 100);
        Mockito.when(exportHook.getController()).thenReturn(controller);
        Mockito.when(controller.getView()).thenReturn(view);
        Mockito.when(view.getInnerBounds()).thenReturn(innerBounds);
        Mockito.when(view.getWidth()).thenReturn(200);
        Mockito.when(view.getHeight()).thenReturn(200);
        Mockito.when(view.createImage(200, 200)).thenReturn(new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB));
        BufferedImage result = exportHook.createBufferedImage();
        assertNotNull(result);
        assertEquals(innerBounds.width, result.getWidth());
        assertEquals(innerBounds.height, result.getHeight());
    }

    @Test
    public void testCreateBufferedImageNullView() {
        // Error scenario: view is null
        ExportHook exportHook = new ExportHook();
        BufferedImage result = exportHook.createBufferedImage();
        assertNull(result);
    }

    @Test
    public void testCopyFromResourceTypical() {
        // Typical use case
        ExportHook exportHook = new ExportHook();
        exportHook.setLogger(Mockito.mock(Logger.class));
        exportHook.setResourceString("resourceKey", "Resource Content");
        exportHook.setController(Mockito.mock(ModeController.class));
        Mockito.when(exportHook.getResource("prefixfileName")).thenReturn(new URL("http://example.com"));
        Mockito.when(exportHook.getResource("prefixfileName")).thenReturn(new URL("http://example.com"));
        File destination = new File("destinationDir");
        exportHook.copyFromResource("prefix", "fileName.txt", destination.getPath());
        File copiedFile = new File(destination, "fileName.txt");
        assertTrue(copiedFile.exists());
    }

    @Test
    public void testCopyFromResourceNotFound() {
        // Error scenario: resource not found
        ExportHook exportHook = new ExportHook();
        exportHook.setLogger(Mockito.mock(Logger.class));
        exportHook.setResourceString("resourceKey", "Resource Content");
        exportHook.setController(Mockito.mock(ModeController.class));
        Mockito.when(exportHook.getResource("prefixfileName")).thenReturn(null);
        File destination = new File("destinationDir");
        exportHook.copyFromResource("prefix", "fileName.txt", destination.getPath());
        File copiedFile = new File(destination, "fileName.txt");
        assertFalse(copiedFile.exists());
    }

    @Test
    public void testCopyFromFileTypical() {
        // Typical use case
        ExportHook exportHook = new ExportHook();
        exportHook.setLogger(Mockito.mock(Logger.class));
        exportHook.setController(Mockito.mock(ModeController.class));
        File resource = new File("dir", "fileName.txt");
        Mockito.when(exportHook.getResource("dirfileName")).thenReturn(resource);
        File destination = new File("destinationDir");
        exportHook.copyFromFile("dir", "fileName.txt", destination.getPath());
        File copiedFile = new File(destination, "fileName.txt");
        assertTrue(copiedFile.exists());
    }

    @Test
    public void testCopyFromFileNotFound() {
        // Error scenario: file not found
        ExportHook exportHook = new ExportHook();
        exportHook.setLogger(Mockito.mock(Logger.class));
        exportHook.setController(Mockito.mock(ModeController.class));
        Mockito.when(exportHook.getResource("dirfileName")).thenReturn(null);
        File destination = new File("destinationDir");
        exportHook.copyFromFile("dir", "fileName.txt", destination.getPath());
        File copiedFile = new File(destination, "fileName.txt");
        assertFalse(copiedFile.exists());
    }
}