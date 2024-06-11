import org.junit.*;
import static org.junit.Assert.*;
import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.filechooser.*;
import java.io.*;
import java.net.*;
import java.text.*;
import freemind.main.*;
import freemind.modes.*;
import freemind.view.mindmapview.*;

public class ExportHookTest {
  
  @Test
  public void testChooseFile() {
    ExportHook exportHook = new ExportHook();
    String type = ".png";
    String description = "PNG Image";
    String nameExtension = "_exported";
    
    File file = exportHook.chooseFile(type, description, nameExtension);
    
    assertNotNull(file);
    assertTrue(file.exists());
    assertTrue(file.getName().endsWith(type));
  }
  
  @Test
  public void testChooseImageFile() {
    ExportHook exportHook = new ExportHook();
    String type = ".jpg";
    String description = "JPEG Image";
    String nameExtension = "_exported";
    ModeController controller = exportHook.getController();
    
    File file = ExportHook.chooseImageFile(type, description, nameExtension, controller);
    
    assertNotNull(file);
    assertTrue(file.exists());
    assertTrue(file.getName().endsWith(type));
  }
  
  @Test
  public void testGetTranslatableResourceString() {
    ExportHook exportHook = new ExportHook();
    String resourceName = "test_resource";
    
    String result = exportHook.getTranslatableResourceString(resourceName);
    
    assertNotNull(result);
    assertFalse(result.startsWith("%"));
  }
  
  @Test
  public void testCreateBufferedImage() {
    ExportHook exportHook = new ExportHook();
    ModeController controller = exportHook.getController();
    
    BufferedImage image = exportHook.createBufferedImage();
    
    assertNotNull(image);
    assertEquals(controller.getView().getWidth(), image.getWidth());
    assertEquals(controller.getView().getHeight(), image.getHeight());
  }
  
  @Test
  public void testCopyFromResource() {
    ExportHook exportHook = new ExportHook();
    String prefix = "images/";
    String fileName = "test_image.jpg";
    String destinationDirectory = "output/";
    
    exportHook.copyFromResource(prefix, fileName, destinationDirectory);
    
    File file = new File(destinationDirectory + fileName);
    
    assertTrue(file.exists());
    assertEquals(fileName, file.getName());
  }
  
  @Test
  public void testCopyFromFile() {
    ExportHook exportHook = new ExportHook();
    String dir = "resources/";
    String fileName = "test_file.txt";
    String destinationDirectory = "output/";
    
    exportHook.copyFromFile(dir, fileName, destinationDirectory);
    
    File file = new File(destinationDirectory + fileName);
    
    assertTrue(file.exists());
    assertEquals(fileName, file.getName());
  }
  
  @Test
  public void testImageFilterAccept() {
    ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter(".jpeg", "JPEG Image");
    File directory = new File("images/");
    File imageFile = new File("image.jpg");
    File textFile = new File("document.txt");
    
    assertTrue(imageFilter.accept(imageFile));
    assertFalse(imageFilter.accept(textFile));
    assertTrue(imageFilter.accept(directory));
  }
  
  @Test
  public void testImageFilterGetDescription() {
    ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter(".png", "PNG Image");
    
    String result = imageFilter.getDescription();
    
    assertNotNull(result);
    assertEquals("PNG Image", result);
  }
}