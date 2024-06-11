import org.junit.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import freemind.view.mindmapview.MapView;
import freemind.modes.ModeController;
import freemind.main.Tools;
import freemind.extensions.ExportHook;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;

public class ExportHookTest {

    private ExportHook exportHook;
    private ModeController mockedController;

    @Before
    public void setUp() {
        exportHook = new ExportHook();
        mockedController = mock(ModeController.class);
    }

    @Test
    public void testChooseFile() {
        String type = "jpg";
        String description = "JPEG Image";
        String nameExtension = ".jpg";

        assertNotNull(exportHook.chooseFile(type, description, nameExtension));
    }

    @Test
    public void testChooseImageFile() {
        String type = "png";
        String description = "PNG Image";

        assertNotNull(ExportHook.chooseImageFile(type, description, null, mockedController));
    }

    @Test
    public void testGetTranslatableResourceString() {
        String resourceName = "test.resource";
        assertEquals("Test Resource Value", exportHook.getTranslatableResourceString(resourceName));
    }

    @Test
    public void testCreateBufferedImage() {
        MapView mockedView = mock(MapView.class);
        when(mockedController.getView()).thenReturn(mockedView);
        BufferedImage image = exportHook.createBufferedImage();

        assertNotNull(image);
    }

    @Test
    public void testCopyFromResource() {
        String prefix = "images/";
        String fileName = "test.jpg";
        String destinationDirectory = "temp/";

        exportHook.copyFromResource(prefix, fileName, destinationDirectory);

        // Add assertion here to check if the file is copied successfully
    }

    @Test
    public void testCopyFromFile() {
        String dir = "resources/";
        String fileName = "data.txt";
        String destinationDirectory = "temp/";

        exportHook.copyFromFile(dir, fileName, destinationDirectory);

        // Add assertion here to check if the file is copied successfully
    }

    @Test
    public void testImageFilterAccept() {
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("jpg", "JPEG Image");

        File file = new File("test.jpg");

        assertTrue(imageFilter.accept(file));
    }

    @Test
    public void testImageFilterDescription() {
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("png", "PNG Image");

        assertEquals("PNG Image", imageFilter.getDescription());
    }

}