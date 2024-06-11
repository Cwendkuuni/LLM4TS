import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.awt.image.BufferedImage;

public class ExportHookTest {

    @Test
    public void testChooseFile() {
        ExportHook exportHook = new ExportHook();
        File file = exportHook.chooseFile("jpg", "Image file", ".jpg");
        assertNotNull(file);
    }

    @Test
    public void testChooseImageFile() {
        ModeController controller = new ModeController();
        File file = ExportHook.chooseImageFile("png", "Image file", ".png", controller);
        assertNotNull(file);
    }

    @Test
    public void testGetTranslatableResourceString() {
        ExportHook exportHook = new ExportHook();
        String resourceName = "testResource";
        String result = exportHook.getTranslatableResourceString(resourceName);
        assertNotNull(result);
    }

    @Test
    public void testCreateBufferedImage() {
        ExportHook exportHook = new ExportHook();
        BufferedImage image = exportHook.createBufferedImage();
        assertNotNull(image);
    }

    @Test
    public void testCopyFromResource() {
        ExportHook exportHook = new ExportHook();
        exportHook.copyFromResource("images/", "testImage.png", "output/");
        // Check if file is copied successfully
        File file = new File("output/testImage.png");
        assertTrue(file.exists());
    }

    @Test
    public void testCopyFromFile() {
        ExportHook exportHook = new ExportHook();
        exportHook.copyFromFile("resources/", "testFile.txt", "output/");
        // Check if file is copied successfully
        File file = new File("output/testFile.txt");
        assertTrue(file.exists());
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
        String description = imageFilter.getDescription();
        assertEquals("PNG Image", description);
    }
}