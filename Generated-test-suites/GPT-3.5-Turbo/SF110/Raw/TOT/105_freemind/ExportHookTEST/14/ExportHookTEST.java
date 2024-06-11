import org.junit.Before;
import org.junit.Test;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class ExportHookTest {

    private ExportHook exportHook;

    @Before
    public void setup() {
        exportHook = new ExportHook();
    }

    @Test
    public void testChooseFile_typicalUseCase() {
        String type = "jpeg";
        String description = "JPEG Image";
        String nameExtension = "_export";
        
        File file = exportHook.chooseFile(type, description, nameExtension);

        assertNotNull(file);
        assertTrue(file.getName().endsWith(type));
    }

    @Test
    public void testChooseFile_nullReturnValue() {
        String type = "png";
        String description = "PNG Image";
        String nameExtension = "_export";
        
        File file = exportHook.chooseFile(type, description, nameExtension);

        assertNull(file);
    }

    @Test
    public void testGetTranslatableResourceString_typicalUseCase() {
        String resourceName = "example_resource_string";
        String expectedValue = "Example Translated Resource";
        
        String actualValue = exportHook.getTranslatableResourceString(resourceName);

        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testGetTranslatableResourceString_noTranslationAvailable() {
        String resourceName = "non_existent_resource";
        
        String actualValue = exportHook.getTranslatableResourceString(resourceName);

        assertEquals(resourceName, actualValue);
    }

    @Test
    public void testCreateBufferedImage_typicalUseCase() {
        JComponent component = new JPanel();
        component.setPreferredSize(new Dimension(500, 500));
        component.setBackground(Color.WHITE);

        exportHook.getController().setView(component);
        BufferedImage image = exportHook.createBufferedImage();

        assertNotNull(image);
        assertEquals(500, image.getWidth());
        assertEquals(500, image.getHeight());
    }

    @Test
    public void testCreateBufferedImage_nullView() {
        BufferedImage image = exportHook.createBufferedImage();

        assertNull(image);
    }

    @Test
    public void testCopyFromResource_fileExists() {
        String prefix = "resources/";
        String fileName = "example_file.txt";
        String destinationDirectory = "destination";

        exportHook.copyFromResource(prefix, fileName, destinationDirectory);

        File copiedFile = new File(destinationDirectory + "/" + fileName);

        assertTrue(copiedFile.exists());
    }

    @Test
    public void testCopyFromResource_fileDoesNotExist() {
        String prefix = "resources/";
        String fileName = "non_existent_file.txt";
        String destinationDirectory = "destination";

        exportHook.copyFromResource(prefix, fileName, destinationDirectory);

        File copiedFile = new File(destinationDirectory + "/" + fileName);

        assertFalse(copiedFile.exists());
    }

    @Test
    public void testCopyFromFile_fileExists() {
        String dir = "resources";
        String fileName = "example_file.txt";
        String destinationDirectory = "destination";

        exportHook.copyFromFile(dir, fileName, destinationDirectory);

        File copiedFile = new File(destinationDirectory + "/" + fileName);

        assertTrue(copiedFile.exists());
    }

    @Test
    public void testCopyFromFile_fileDoesNotExist() {
        String dir = "resources";
        String fileName = "non_existent_file.txt";
        String destinationDirectory = "destination";

        exportHook.copyFromFile(dir, fileName, destinationDirectory);

        File copiedFile = new File(destinationDirectory + "/" + fileName);

        assertFalse(copiedFile.exists());
    }

    @Test
    public void testImageFilter_accept_validFile() {
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("jpeg", "JPEG Image");
        File validFile = new File("example.jpg");

        boolean accept = imageFilter.accept(validFile);

        assertTrue(accept);
    }

    @Test
    public void testImageFilter_accept_invalidFile() {
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("jpeg", "JPEG Image");
        File invalidFile = new File("example.png");

        boolean accept = imageFilter.accept(invalidFile);

        assertFalse(accept);
    }

    @Test
    public void testImageFilter_getDescription_withDescription() {
        String type = "jpeg";
        String description = "JPEG Image";
        
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter(type, description);
        String expectedDescription = description;

        String actualDescription = imageFilter.getDescription();

        assertEquals(expectedDescription, actualDescription);
    }

    @Test
    public void testImageFilter_getDescription_withoutDescription() {
        String type = "jpeg";
        
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter(type, null);
        String expectedDescription = type;

        String actualDescription = imageFilter.getDescription();

        assertEquals(expectedDescription, actualDescription);
    }

}