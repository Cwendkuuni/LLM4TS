import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import freemind.extensions.ExportHook;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.filechooser.FileFilter;

public class ExportHookTest {
    
    private ExportHook exportHook;
    
    @Before
    public void setUp() {
        exportHook = new ExportHook();
    }
    
    @Test
    public void testChooseFile() {
        String type = "png";
        String description = "PNG Image";
        String nameExtension = "-export";
        
        File chosenFile = exportHook.chooseFile(type, description, nameExtension);
        
        assertNotNull(chosenFile);
        assertTrue(chosenFile.exists());
    }
    
    @Test
    public void testChooseImageFile() {
        String type = "png";
        String description = "PNG Image";
        String nameExtension = "-export";
        
        File chosenFile = ExportHook.chooseImageFile(type, description, nameExtension, null);
        
        assertNotNull(chosenFile);
        assertTrue(chosenFile.exists());
    }
    
    @Test
    public void testChooseImageFileWithNullController() {
        String type = "png";
        String description = "PNG Image";
        String nameExtension = "-export";
        
        File chosenFile = ExportHook.chooseImageFile(type, description, nameExtension, null);
        
        assertNull(chosenFile);
    }
    
    @Test
    public void testGetTranslatableResourceString() {
        String resourceName = "testResource";
        
        String returnValue = exportHook.getTranslatableResourceString(resourceName);
        
        assertNotNull(returnValue);
        assertFalse(returnValue.isEmpty());
    }
    
    @Test
    public void testCreateBufferedImage() {
        BufferedImage bufferedImage = exportHook.createBufferedImage();
        
        assertNotNull(bufferedImage);
        assertEquals(800, bufferedImage.getWidth());
        assertEquals(600, bufferedImage.getHeight());
    }
    
    @Test
    public void testCopyFromResource() {
        String prefix = "images/";
        String fileName = "testImage.png";
        String destinationDirectory = "/tmp";
        
        exportHook.copyFromResource(prefix, fileName, destinationDirectory);
        
        File copiedFile = new File(destinationDirectory + "/" + fileName);
        
        assertTrue(copiedFile.exists());
        assertTrue(copiedFile.isFile());
    }
    
    @Test
    public void testCopyFromNonExistingResource() {
        String prefix = "images/";
        String fileName = "nonExistingImage.png";
        String destinationDirectory = "/tmp";
        
        exportHook.copyFromResource(prefix, fileName, destinationDirectory);
        
        File copiedFile = new File(destinationDirectory + "/" + fileName);
        
        assertFalse(copiedFile.exists());
    }
    
    @Test
    public void testCopyFromFile() {
        String dir = "images";
        String fileName = "testImage.png";
        String destinationDirectory = "/tmp";
        
        exportHook.copyFromFile(dir, fileName, destinationDirectory);
        
        File copiedFile = new File(destinationDirectory + "/" + fileName);
        
        assertTrue(copiedFile.exists());
        assertTrue(copiedFile.isFile());
    }
    
    @Test
    public void testCopyFromNonExistingFile() {
        String dir = "images";
        String fileName = "nonExistingImage.png";
        String destinationDirectory = "/tmp";
        
        exportHook.copyFromFile(dir, fileName, destinationDirectory);
        
        File copiedFile = new File(destinationDirectory + "/" + fileName);
        
        assertFalse(copiedFile.exists());
    }
    
    @Test
    public void testImageFilterAccept() {
        File directory = new File(".");
        FileFilter filter = new ExportHook.ImageFilter("png", "PNG Image");
        
        assertTrue(filter.accept(directory));
        
        File imageFile = new File("testImage.png");
        
        assertTrue(filter.accept(imageFile));
        
        File txtFile = new File("testFile.txt");
        
        assertFalse(filter.accept(txtFile));
    }
    
    @Test
    public void testImageFilterGetDescription() {
        String type = "png";
        String description = "PNG Image";
        ExportHook.ImageFilter filter = new ExportHook.ImageFilter(type, description);
        
        String filterDescription = filter.getDescription();
        
        assertNotNull(filterDescription);
        assertFalse(filterDescription.isEmpty());
        assertEquals(description, filterDescription);
    }
}


// JUnit 4 test cases for the ExportHook class
public class ExportHookTest {
    
    private ExportHook exportHook;
    
    // Set up the ExportHook object before each test
    @Before
    public void setUp() {
        exportHook = new ExportHook();
    }
    
    // Test the chooseFile method
    @Test
    public void testChooseFile() {
        String type = "png";
        String description = "PNG Image";
        String nameExtension = "-export";
        
        // Check if the chosen file is not null and exists
        File chosenFile = exportHook.chooseFile(type, description, nameExtension);
        assertNotNull(chosenFile);
        assertTrue(chosenFile.exists());
    }
    
    // Test the chooseImageFile method
    @Test
    public void testChooseImageFile() {
        String type = "png";
        String description = "PNG Image";
        String nameExtension = "-export";
        
        // Check if the chosen file is not null and exists
        File chosenFile = ExportHook.chooseImageFile(type, description, nameExtension, null);
        assertNotNull(chosenFile);
        assertTrue(chosenFile.exists());
    }
    
    // Test the chooseImageFile method with a null controller
    @Test
    public void testChooseImageFileWithNullController() {
        String type = "png";
        String description = "PNG Image";
        String nameExtension = "-export";
        
        // Check if the chosen file is null
        File chosenFile = ExportHook.chooseImageFile(type, description, nameExtension, null);
        assertNull(chosenFile);
    }
    
    // Test the getTranslatableResourceString method
    @Test
    public void testGetTranslatableResourceString() {
        String resourceName = "testResource";
        
        // Check if the returned value is not null and not empty
        String returnValue = exportHook.getTranslatableResourceString(resourceName);
        assertNotNull(returnValue);
        assertFalse(returnValue.isEmpty());
    }
    
    // Test the createBufferedImage method
    @Test
    public void testCreateBufferedImage() {
        // Check if the created buffered image is not null and has the expected width and height
        BufferedImage bufferedImage = exportHook.createBufferedImage();
        assertNotNull(bufferedImage);
        assertEquals(800, bufferedImage.getWidth());
        assertEquals(600, bufferedImage.getHeight());
    }
    
    // Test the copyFromResource method
    @Test
    public void testCopyFromResource() {
        String prefix = "images/";
        String fileName = "testImage.png";
        String destinationDirectory = "/tmp";
        
        // Check if the file is successfully copied from the resource to the destination directory
        exportHook.copyFromResource(prefix, fileName, destinationDirectory);
        File copiedFile = new File(destinationDirectory + "/" + fileName);
        assertTrue(copiedFile.exists());
        assertTrue(copiedFile.isFile());
    }
    
    // Test the copyFromResource method with a non-existing resource
    @Test
    public void testCopyFromNonExistingResource() {
        String prefix = "images/";
        String fileName = "nonExistingImage.png";
        String destinationDirectory = "/tmp";
        
        // Check if the file is not copied from the non-existing resource to the destination directory
        exportHook.copyFromResource(prefix, fileName, destinationDirectory);
        File copiedFile = new File(destinationDirectory + "/" + fileName);
        assertFalse(copiedFile.exists());
    }
    
    // Test the copyFromFile method
    @Test
    public void testCopyFromFile() {
        String dir = "images";
        String fileName = "testImage.png";
        String destinationDirectory = "/tmp";
        
        // Check if the file is successfully copied from the file in the directory to the destination directory
        exportHook.copyFromFile(dir, fileName, destinationDirectory);
        File copiedFile = new File(destinationDirectory + "/" + fileName);
        assertTrue(copiedFile.exists());
        assertTrue(copiedFile.isFile());
    }
    
    // Test the copyFromFile method with a non-existing file
    @Test
    public void testCopyFromNonExistingFile() {
        String dir = "images";
        String fileName = "nonExistingImage.png";
        String destinationDirectory = "/tmp";
        
        // Check if the file is not copied from the non-existing file in the directory to the destination directory
        exportHook.copyFromFile(dir, fileName, destinationDirectory);
        File copiedFile = new File(destinationDirectory + "/" + fileName);
        assertFalse(copiedFile.exists());
    }
    
    // Test the accept method of the ImageFilter class
    @Test
    public void testImageFilterAccept() {
        File directory = new File(".");
        ExportHook.ImageFilter filter = new ExportHook.ImageFilter("png", "PNG Image");
        
        // Check if the filter accepts the directory
        assertTrue(filter.accept(directory));
        
        File imageFile = new File("testImage.png");
        
        // Check if the filter accepts the image file
        assertTrue(filter.accept(imageFile));
        
        File txtFile = new File("testFile.txt");
        
        // Check if the filter does not accept the text file
        assertFalse(filter.accept(txtFile));
    }
    
    // Test the getDescription method of the ImageFilter class
    @Test
    public void testImageFilterGetDescription() {
        String type = "png";
        String description = "PNG Image";
        ExportHook.ImageFilter filter = new ExportHook.ImageFilter(type, description);
        
        // Check if the filter description is not null, not empty, and equal to the expected description
        String filterDescription = filter.getDescription();
        assertNotNull(filterDescription);
        assertFalse(filterDescription.isEmpty());
        assertEquals(description, filterDescription);
    }
}

