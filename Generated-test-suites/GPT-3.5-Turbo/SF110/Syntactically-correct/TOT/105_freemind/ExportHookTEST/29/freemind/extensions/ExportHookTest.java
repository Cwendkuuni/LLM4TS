import org.junit.Test;
import static org.junit.Assert.*;
import javax.swing.JFrame;
import java.io.File;
import java.awt.image.BufferedImage;
import java.nio.file.Files;

public class ExportHookTest {

    @Test
    public void testChooseFile_typical() {
        ExportHook exportHook = new ExportHook();
        JFrame frame = new JFrame();
        frame.setContentPane(new JPanel());
        ModeController controller = new ModeController();
        controller.setFrame(frame);
        exportHook.setController(controller);
        File file = exportHook.chooseFile("png", "PNG File", "_export");
        assertNotNull(file);
    }

    @Test
    public void testChooseFile_nullReturn() {
        ExportHook exportHook = new ExportHook();
        JFrame frame = new JFrame();
        frame.setContentPane(new JPanel());
        ModeController controller = new ModeController();
        controller.setFrame(frame);
        exportHook.setController(controller);
        File file = exportHook.chooseFile("png", "PNG File", "_export");
        assertNull(file);
    }

    @Test
    public void testChooseImageFile_typical() {
        ExportHook exportHook = new ExportHook();
        JFrame frame = new JFrame();
        frame.setContentPane(new JPanel());
        ModeController controller = new ModeController();
        controller.setFrame(frame);
        File mmFile = new File("test.mm");
        controller.getMap().setFile(mmFile);
        File file = ExportHook.chooseImageFile("png", "PNG File", "_export", controller);
        assertNotNull(file);
    }

    @Test
    public void testChooseImageFile_nullReturn() {
        ExportHook exportHook = new ExportHook();
        JFrame frame = new JFrame();
        frame.setContentPane(new JPanel());
        ModeController controller = new ModeController();
        controller.setFrame(frame);
        File mmFile = new File("test.mm");
        controller.getMap().setFile(mmFile);
        File file = ExportHook.chooseImageFile("png", "PNG File", "_export", controller);
        assertNull(file);
    }

    @Test
    public void testGetTranslatableResourceString_typical() {
        ExportHook exportHook = new ExportHook();
        String resourceName = "test_resource";
        String returnValue = exportHook.getTranslatableResourceString(resourceName);
        assertEquals("Test Resource", returnValue);
    }

    @Test
    public void testGetTranslatableResourceString_emptyReturn() {
        ExportHook exportHook = new ExportHook();
        String resourceName = "test_resource_empty";
        String returnValue = exportHook.getTranslatableResourceString(resourceName);
        assertEquals("", returnValue);
    }

    @Test
    public void testCreateBufferedImage_typical() {
        ExportHook exportHook = new ExportHook();
        assertTrue(exportHook.createBufferedImage() instanceof BufferedImage);
    }

    @Test
    public void testCreateBufferedImage_nullReturn() {
        ExportHook exportHook = new ExportHook();
        JFrame frame = new JFrame();
        ModeController controller = new ModeController();
        controller.setFrame(frame);
        exportHook.setController(controller);
        assertNull(exportHook.createBufferedImage());
    }

    @Test
    public void testCopyFromResource_typical() {
        ExportHook exportHook = new ExportHook();
        exportHook.copyFromResource("images/", "test_image.png", "destination");
        File file = new File("destination/test_image.png");
        assertTrue(file.exists());
        assertTrue(file.isFile());
    }

    @Test
    public void testCopyFromResource_resourceNotFound() {
        ExportHook exportHook = new ExportHook();
        exportHook.copyFromResource("images/", "non_existing_image.png", "destination");
        File file = new File("destination/non_existing_image.png");
        assertFalse(file.exists());
    }

    @Test
    public void testCopyFromFile_typical() {
        ExportHook exportHook = new ExportHook();
        File sourceFile = new File("source/test_file.txt");
        File destinationDirectory = new File("destination");
        exportHook.copyFromFile("source", "test_file.txt", "destination");
        File file = new File(destinationDirectory, "test_file.txt");
        assertTrue(file.exists());
        assertTrue(file.isFile());
        assertTrue(Files.isSameFile(sourceFile.toPath(), file.toPath()));
    }

    @Test
    public void testCopyFromFile_fileNotFound() {
        ExportHook exportHook = new ExportHook();
        File destinationDirectory = new File("destination");
        exportHook.copyFromFile("source", "non_existing_file.txt", "destination");
        File file = new File(destinationDirectory, "non_existing_file.txt");
        assertFalse(file.exists());
    }

    @Test
    public void testImageFilter_accept_true() {
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("png", "PNG File");
        File file = new File("test_image.png");
        assertTrue(imageFilter.accept(file));
    }

    @Test
    public void testImageFilter_accept_false() {
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("png", "PNG File");
        File file = new File("test_image.jpg");
        assertFalse(imageFilter.accept(file));
    }

    @Test
    public void testImageFilter_getDescription_noDescription() {
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("png", null);
        assertEquals("png", imageFilter.getDescription());
    }

    @Test
    public void testImageFilter_getDescription_withDescription() {
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("png", "PNG File");
        assertEquals("PNG File", imageFilter.getDescription());
    }
}