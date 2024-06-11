import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import freemind.extensions.ExportHook;
import freemind.view.mindmapview.ModeController;
import freemind.view.mindmapview.FreeMindFileDialog;
import javax.swing.filechooser.FileFilter;

import org.junit.Before;
import org.junit.Test;

public class ExportHookTest {
    private ExportHook exportHook;
    private ModeController mockController;
    private FreeMindFileDialog mockChooser;

    @Before
    public void setup() {
        exportHook = new ExportHook();
        mockController = mock(ModeController.class);
        mockChooser = mock(FreeMindFileDialog.class);
        when(mockController.getFrame().getContentPane()).thenReturn(mock(Container.class));
        when(mockController.getFileChooser(any(FileFilter.class))).thenReturn(mockChooser);
        exportHook.setController(mockController);
    }

    @Test
    public void testChooseFile() {
        // Test implementation when mmFile is null
        File expectedFile = null;
        when(mockController.getMap().getFile()).thenReturn(null);
        when(mockChooser.showSaveDialog(any(Container.class))).thenReturn(1);
        File chosenFile = exportHook.chooseFile("png", "PNG Image", ".png");
        assertEquals(expectedFile, chosenFile);

        // Test implementation when mmFile is not null
        File mmFile = new File("test.mm");
        expectedFile = new File("test.png");
        when(mockController.getMap().getFile()).thenReturn(mmFile);
        when(mockChooser.getSelectedFile()).thenReturn(expectedFile);
        when(mockChooser.showSaveDialog(any(Container.class))).thenReturn(0);
        chosenFile = exportHook.chooseFile("png", "PNG Image", ".png");
        assertEquals(expectedFile, chosenFile);
    }

    @Test
    public void testChooseImageFile() {
        // Test implementation when ext matches type
        File chosenFile = new File("test.jpg");
        String type = "jpg";
        String description = "JPEG Image";
        String nameExtension = ".jpg";
        File expectedFile = new File(chosenFile.getParent(), chosenFile.getName() + "." + type);
        ModeController mockController = mock(ModeController.class);
        when(mockChooser.getSelectedFile()).thenReturn(chosenFile);
        when(mockController.getText(anyString())).thenReturn("File already exists");
        when(mockChooser.showConfirmDialog(any(Container.class), anyString(), anyString(), anyInt())).thenReturn(0);
        File resultFile = ExportHook.chooseImageFile(type, description, nameExtension, mockController);
        assertEquals(expectedFile, resultFile);

        // Test implementation when ext does not match type
        chosenFile = new File("test.png");
        type = "jpg";
        description = "JPEG Image";
        nameExtension = ".jpg";
        expectedFile = new File(chosenFile.getParent(), chosenFile.getName() + "." + type);
        mockController = mock(ModeController.class);
        when(mockChooser.getSelectedFile()).thenReturn(chosenFile);
        when(mockController.getText(anyString())).thenReturn("File already exists");
        when(mockChooser.showConfirmDialog(any(Container.class), anyString(), anyString(), anyInt())).thenReturn(0);
        resultFile = ExportHook.chooseImageFile(type, description, nameExtension, mockController);
        assertEquals(expectedFile, resultFile);

        // Test implementation when file already exists
        chosenFile = new File("test.jpg");
        type = "jpg";
        description = "JPEG Image";
        nameExtension = ".jpg";
        expectedFile = null;
        mockController = mock(ModeController.class);
        when(mockChooser.getSelectedFile()).thenReturn(chosenFile);
        when(mockController.getText(anyString())).thenReturn("File already exists");
        when(mockChooser.showConfirmDialog(any(Container.class), anyString(), anyString(), anyInt())).thenReturn(1);
        resultFile = ExportHook.chooseImageFile(type, description, nameExtension, mockController);
        assertEquals(expectedFile, resultFile);
    }

    @Test
    public void testGetTranslatableResourceString() {
        String resourceName = "%file_name_label";
        String expectedValue = "File Name";
        ModeController mockController = mock(ModeController.class);
        when(mockController.getText(anyString())).thenReturn(expectedValue);
        String resultValue = exportHook.getTranslatableResourceString(resourceName);
        assertEquals(expectedValue, resultValue);

        resourceName = "invalid_resource_name";
        expectedValue = "Invalid Resource";
        mockController = mock(ModeController.class);
        when(mockController.getText(anyString())).thenReturn(expectedValue);
        resultValue = exportHook.getTranslatableResourceString(resourceName);
        assertEquals(expectedValue, resultValue);
    }

    @Test
    public void testCreateBufferedImage() {
        BufferedImage expectedImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        ModeController mockController = mock(ModeController.class);
        MapView mockView = mock(MapView.class);
        when(mockController.getView()).thenReturn(mockView);
        when(mockView.getWidth()).thenReturn(100);
        when(mockView.getHeight()).thenReturn(100);
        when(mockView.getInnerBounds()).thenReturn(new Rectangle(0, 0, 100, 100));
        when(mockView.createImage(anyInt(), anyInt())).thenReturn(expectedImage);
        BufferedImage resultImage = exportHook.createBufferedImage();
        assertEquals(expectedImage, resultImage);
    }

    @Test
    public void testCopyFromResource() {
        String prefix = "/resources/";
        String fileName = "test.jpg";
        String destinationDirectory = "/destination";
        String absolutePath = destinationDirectory + "/" + fileName;

        URL mockResource = mock(URL.class);
        InputStream mockInputStream = mock(InputStream.class);
        OutputStream mockOutputStream = mock(OutputStream.class);
        when(mockResource.openStream()).thenReturn(mockInputStream);
        when(mockResource.getFile()).thenReturn(absolutePath);
        when(exportHook.getResource(prefix + fileName)).thenReturn(mockResource);
        when(mockController.getText(anyString())).thenReturn("File not found");
        try {
            doNothing().when(Tools.class);
            Tools.copyStream(mockInputStream, mockOutputStream, true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        exportHook.copyFromResource(prefix, fileName, destinationDirectory);
        verify(exportHook).getResource(prefix + fileName);
        verify(mockResource).openStream();
        verify(Tools.class);
        Tools.copyStream(mockInputStream, mockOutputStream, true);
    }

    @Test
    public void testCopyFromFile() {
        String dir = "/source";
        String fileName = "test.jpg";
        String destinationDirectory = "/destination";
        String absolutePath = dir + "/" + fileName;

        File mockResource = mock(File.class);
        InputStream mockInputStream = mock(InputStream.class);
        OutputStream mockOutputStream = mock(OutputStream.class);
        when(mockResource.getPath()).thenReturn(absolutePath);
        try {
            whenNew(File.class).withArguments(dir, fileName).thenReturn(mockResource);
        } catch (Exception e) {
            e.printStackTrace();
        }
        when(mockResource.exists()).thenReturn(true);
        try {
            whenNew(FileInputStream.class).withArguments(mockResource).thenReturn(mockInputStream);
            whenNew(FileOutputStream.class).withArguments(destinationDirectory + "/" + fileName).thenReturn(mockOutputStream);
            doNothing().when(Tools.class);
            Tools.copyStream(mockInputStream, mockOutputStream, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        exportHook.copyFromFile(dir, fileName, destinationDirectory);
        try {
            verifyNew(FileInputStream.class).withArguments(mockResource);
            verifyNew(FileOutputStream.class).withArguments(destinationDirectory + "/" + fileName);
            verify(Tools.class);
            Tools.copyStream(mockInputStream, mockOutputStream, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}