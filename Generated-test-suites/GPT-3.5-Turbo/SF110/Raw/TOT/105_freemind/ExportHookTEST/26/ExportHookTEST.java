import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.image.*;
import javax.swing.filechooser.*;
import java.io.*;
import java.util.*;
import java.text.*;

public class ExportHookTest {
    
    private ExportHook exportHook;
    private ModeController modeController;
    private MapView mapView;
    private ModeControllerHookAdapter modeControllerHookAdapter;
    
    @Before
    public void setUp() {
        exportHook = new ExportHook();
        modeController = new ModeController();
        mapView = new MapView();
        modeControllerHookAdapter = new ModeControllerHookAdapter();
    }
    
    @Test
    public void testChooseFile() {
        // Typical use case
        String fileType = "png";
        String fileDescription = "PNG Image File";
        String fileExtension = ".png";
        File chosenFile = exportHook.chooseFile(fileType, fileDescription, fileExtension);
        assertNotNull(chosenFile);
        assertEquals(fileType, Tools.getExtension(chosenFile.getName()));
        
        // Edge case: No file extension provided
        chosenFile = exportHook.chooseFile(fileType, fileDescription, null);
        assertNotNull(chosenFile);
        assertEquals(fileType, Tools.getExtension(chosenFile.getName()));
        
        // Error scenario: File not selected
        chosenFile = exportHook.chooseFile(fileType, fileDescription, fileExtension);
        assertNull(chosenFile);
    }
    
    @Test
    public void testChooseImageFile() {
        // Typical use case
        String fileType = "jpg";
        String fileDescription = "JPG Image File";
        String fileExtension = ".jpg";
        File chosenFile = ExportHook.chooseImageFile(fileType, fileDescription,
                                                     fileExtension, modeController);
        assertNotNull(chosenFile);
        assertEquals(fileType, Tools.getExtension(chosenFile.getName()));
        
        // Edge case: No file extension provided
        chosenFile = ExportHook.chooseImageFile(fileType, fileDescription,
                                                null, modeController);
        assertNotNull(chosenFile);
        assertEquals(fileType, Tools.getExtension(chosenFile.getName()));
        
        // Error scenario: File not selected
        chosenFile = ExportHook.chooseImageFile(fileType, fileDescription,
                                                fileExtension, modeController);
        assertNull(chosenFile);
    }
    
    @Test
    public void testGetTranslatableResourceString() {
        // Typical use case: Resource name starts with %
        String resourceName = "%resourceString";
        String expectedOutput = "Translated Resource String";
        String actualOutput = exportHook.getTranslatableResourceString(resourceName);
        assertEquals(expectedOutput, actualOutput);
        
        // Typical use case: Resource name does not start with %
        resourceName = "resourceString";
        expectedOutput = resourceName;
        actualOutput = exportHook.getTranslatableResourceString(resourceName);
        assertEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void testCreateBufferedImage() {
        // Typical use case
        mapView = modeControllerHookAdapter.getView();
        assertNotNull(mapView);
        BufferedImage bufferedImage = exportHook.createBufferedImage();
        assertNotNull(bufferedImage);
        
        // Error scenario: View is null
        mapView = null;
        bufferedImage = exportHook.createBufferedImage();
        assertNull(bufferedImage);
    }
    
    @Test
    public void testCopyFromResource() {
        // Typical use case
        String prefix = "prefix/";
        String fileName = "file.txt";
        String destinationDirectory = "dir";
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("txt", "Text File");
        File outputFile = new File(destinationDirectory, fileName);
        try {
            exportHook.copyFromResource(prefix, fileName, destinationDirectory);
            assertTrue(outputFile.exists());
            assertTrue(Tools.safeEqualsIgnoreCase(Tools.getExtension(outputFile.getName()), "txt"));
            outputFile.delete();
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
        
        // Error scenario: Resource not found
        prefix = "wrong/";
        try {
            exportHook.copyFromResource(prefix, fileName, destinationDirectory);
            assertFalse(outputFile.exists());
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testCopyFromFile() {
        // Typical use case
        String dir = "dir";
        String fileName = "file.txt";
        String destinationDirectory = "dest";
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("txt", "Text File");
        File inputFile = new File(dir, fileName);
        File outputFile = new File(destinationDirectory, fileName);
        try {
            exportHook.copyFromFile(dir, fileName, destinationDirectory);
            assertTrue(outputFile.exists());
            assertTrue(Tools.safeEqualsIgnoreCase(Tools.getExtension(outputFile.getName()), "txt"));
            outputFile.delete();
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
        
        // Error scenario: File not found
        dir = "wrongDir";
        try {
            exportHook.copyFromFile(dir, fileName, destinationDirectory);
            assertFalse(outputFile.exists());
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testImageFilter() {
        // Typical use case
        String fileType = "png";
        String fileDescription = "PNG Image File";
        ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter(fileType, fileDescription);
        File file = new File("image.png");
        assertTrue(imageFilter.accept(file));
        assertEquals(fileDescription, imageFilter.getDescription());
        
        // Edge case: File is a directory
        file = new File("dir/");
        assertTrue(imageFilter.accept(file));
        
        // Error scenario: File has different extension
        file = new File("image.jpg");
        assertFalse(imageFilter.accept(file));
    }
}