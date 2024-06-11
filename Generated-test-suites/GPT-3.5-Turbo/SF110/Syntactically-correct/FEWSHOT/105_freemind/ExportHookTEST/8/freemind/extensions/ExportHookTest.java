import static org.junit.Assert.*;
import org.junit.Test;
import javax.swing.*;
import freemind.view.mindmapview.*;
import freemind.main.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.net.*;
import java.text.*;
import javax.swing.filechooser.*;

public class ExportHookTest {
    private ExportHook exportHook;
    
    @Before
    public void setUp() {
        exportHook = new ExportHook();
    }
    
    @Test
    public void testChooseFile() {
        ModeController controller = new ModeController();
        controller.setFrame(new JFrame());
        
        String type = "png";
        String description = "PNG Image";
        String nameExtension = ".png";
        
        File chosenFile = exportHook.chooseFile(type, description, nameExtension);
        
        assertNotNull(chosenFile);
        assertEquals(nameExtension, chosenFile.getName().substring(nameExtension.length()-4));
    }
    
    @Test
    public void testChooseImageFile() {
        ModeController controller = new ModeController();
        controller.setFrame(new JFrame());
        
        String type = "png";
        String description = "PNG Image";
        String nameExtension = ".png";
        
        File chosenFile = ExportHook.chooseImageFile(type, description, nameExtension, controller);
        
        assertNotNull(chosenFile);
        assertEquals(description, chosenFile.getName().substring(description.length()));
    }
    
    @Test
    public void testGetTranslatableResourceString() {
        String resourceName = "resource";
        String returnValue = exportHook.getTranslatableResourceString(resourceName);
        
        assertNotNull(returnValue);
        assertFalse(returnValue.startsWith("%"));
    }
    
    @Test
    public void testCreateBufferedImage() {
        ModeController controller = new ModeController();
        MindMapPanel panel = new MindMapPanel(controller);
        BufferedImage image = new BufferedImage(500, 500, BufferedImage.TYPE_INT_RGB);
        
        controller.setView(panel);
        controller.addViewObserver(panel);
        
        BufferedImage myImage = exportHook.createBufferedImage();
        
        assertNotNull(myImage);
        assertEquals(image.getWidth(), myImage.getWidth());
        assertEquals(image.getHeight(), myImage.getHeight());
    }
    
    @Test
    public void testCopyFromResource() throws IOException {
        String prefix = "prefix/";
        String fileName = "file.txt";
        String destinationDirectory = "destination";
        
        URL resource = new URL("http://example.com/file.txt");
        File file = new File(fileName);
        InputStream in = resource.openStream();
        OutputStream out = new FileOutputStream(file);
        
        exportHook.copyFromResource(prefix, fileName, destinationDirectory);
        
        assertTrue(file.exists());
        assertTrue(file.isFile());
        assertEquals(file.getName(), fileName);
        assertEquals(file.getParent(), destinationDirectory);
    }
    
    @Test
    public void testCopyFromFile() throws IOException {
        String dir = "dir";
        String fileName = "file.txt";
        String destinationDirectory = "destination";
        
        File resource = new File(dir, fileName);
        File file = new File(destinationDirectory, fileName);
        InputStream in = new FileInputStream(resource);
        OutputStream out = new FileOutputStream(file);
        
        exportHook.copyFromFile(dir, fileName, destinationDirectory);
        
        assertTrue(file.exists());
        assertTrue(file.isFile());
        assertEquals(file.getName(), fileName);
        assertEquals(file.getParent(), destinationDirectory);
    }
    
    @Test
    public void testImageFilter() {
        String type = "png";
        String description = "PNG Image";
        
        ExportHook.ImageFilter filter = new ExportHook.ImageFilter(type, description);
        
        File pngFile = new File("image.png");
        assertTrue(filter.accept(pngFile));
        assertEquals(description, filter.getDescription());
        
        File txtFile = new File("text.txt");
        assertFalse(filter.accept(txtFile));
    }
}