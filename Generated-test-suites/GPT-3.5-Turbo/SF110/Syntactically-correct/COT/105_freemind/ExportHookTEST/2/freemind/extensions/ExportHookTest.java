import org.junit.Test;
import static org.junit.Assert.*;

public class ExportHookTest {
    
    @Test
    public void testChooseFile() {
        ExportHook exportHook = new ExportHook();
        String type = "jpg";
        String description = "JPEG Image";
        String nameExtension = "img";
        File file = exportHook.chooseFile(type, description, nameExtension);
        assertNotNull(file);
        assertTrue(file.exists());
    }
    
    @Test
    public void testChooseImageFile() {
        String type = "jpg";
        String description = "JPEG Image";
        String nameExtension = "img";
        ModeController modeController = new ModeController();
        File file = ExportHook.chooseImageFile(type, description, nameExtension, modeController);
        assertNotNull(file);
        assertTrue(file.exists());
    }
    
    @Test
    public void testGetTranslatableResourceString() {
        ExportHook exportHook = new ExportHook();
        String resourceName = "resource_name";
        String translatableResource = exportHook.getTranslatableResourceString(resourceName);
        assertNotNull(translatableResource);
        assertFalse(translatableResource.startsWith("%"));
    }
    
    @Test
    public void testCreateBufferedImage() {
        ExportHook exportHook = new ExportHook();
        BufferedImage image = exportHook.createBufferedImage();
        assertNotNull(image);
        assertEquals(ExportHook.view.getInnerBounds().width, image.getWidth());
        assertEquals(ExportHook.view.getInnerBounds().height, image.getHeight());
    }
    
    @Test
    public void testCopyFromResource() {
        ExportHook exportHook = new ExportHook();
        String prefix = "prefix/";
        String fileName = "file.txt";
        String destinationDirectory = "destination";
        exportHook.copyFromResource(prefix, fileName, destinationDirectory);
        File copiedFile = new File(destinationDirectory + "/" + fileName);
        assertTrue(copiedFile.exists());
    }
    
    @Test
    public void testCopyFromFile() {
        ExportHook exportHook = new ExportHook();
        String dir = "directory";
        String fileName = "file.txt";
        String destinationDirectory = "destination";
        exportHook.copyFromFile(dir, fileName, destinationDirectory);
        File copiedFile = new File(destinationDirectory + "/" + fileName);
        assertTrue(copiedFile.exists());
    }
    
    @Test
    public void testImageFilter_accept() {
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("jpg", "JPEG Image");
        File jpgFile = new File("image.jpg");
        assertTrue(imageFilter.accept(jpgFile));
        File pngFile = new File("image.png");
        assertFalse(imageFilter.accept(pngFile));
        File directory = new File("dir");
        assertTrue(imageFilter.accept(directory));
    }
    
    @Test
    public void testImageFilter_getDescription() {
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("jpg", "JPEG Image");
        String description = imageFilter.getDescription();
        assertNotNull(description);
        assertEquals("JPEG Image", description);
    }
}