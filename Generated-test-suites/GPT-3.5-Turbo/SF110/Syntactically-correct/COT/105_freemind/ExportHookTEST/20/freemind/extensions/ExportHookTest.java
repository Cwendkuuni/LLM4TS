package freemind.extensions;

import static org.junit.Assert.*;
import org.junit.Test;

public class ExportHookTest {

    @Test
    public void testChooseFile() {
        ExportHook hook = new ExportHook();
        String type = "txt";
        String description = "Text Files";
        String nameExtension = "file";
        File file = hook.chooseFile(type, description, nameExtension);
        assertNotNull(file);
        assertEquals(type, Tools.getExtension(file.getName()));
    }

    @Test
    public void testChooseImageFile() {
        ModeController controller = new ModeController();
        String type = "png";
        String description = "PNG Image";
        String nameExtension = "image";
        File file = ExportHook.chooseImageFile(type, description, nameExtension, controller);
        assertNotNull(file);
        assertEquals(type, Tools.getExtension(file.getName()));
    }

    @Test
    public void testGetTranslatableResourceString() {
        ExportHook hook = new ExportHook();
        String resourceName = "test_resource";
        String resourceValue = hook.getTranslatableResourceString(resourceName);
        assertEquals("Translated Resource Value", resourceValue);
    }

    @Test
    public void testCreateBufferedImage() {
        ExportHook hook = new ExportHook();
        BufferedImage image = hook.createBufferedImage();
        assertNotNull(image);
        // Validate image properties
        assertEquals(BufferedImage.TYPE_INT_ARGB, image.getType());
        assertEquals(hook.getController().getView().getWidth(), image.getWidth());
        assertEquals(hook.getController().getView().getHeight(), image.getHeight());
    }

    @Test
    public void testCopyFromResource() {
        ExportHook hook = new ExportHook();
        String prefix = "resources/";
        String fileName = "test.txt";
        String destinationDirectory = "destination/";
        hook.copyFromResource(prefix, fileName, destinationDirectory);
        File copiedFile = new File(destinationDirectory + fileName);
        assertTrue(copiedFile.exists());
    }

    @Test
    public void testCopyFromFile() {
        ExportHook hook = new ExportHook();
        String dir = "source/";
        String fileName = "test.txt";
        String destinationDirectory = "destination/";
        hook.copyFromFile(dir, fileName, destinationDirectory);
        File copiedFile = new File(destinationDirectory + fileName);
        assertTrue(copiedFile.exists());
    }

    @Test
    public void testImageFilterAccept() {
        ExportHook.ImageFilter filter = new ExportHook.ImageFilter("png", "PNG Image");
        File pngFile = new File("image.png");
        File txtFile = new File("document.txt");
        assertTrue(filter.accept(pngFile));
        assertFalse(filter.accept(txtFile));
    }

    @Test
    public void testImageFilterGetDescription() {
        String type = "png";
        String description = "PNG Image";
        ExportHook.ImageFilter filter = new ExportHook.ImageFilter(type, description);
        assertEquals(description, filter.getDescription());
    }

}