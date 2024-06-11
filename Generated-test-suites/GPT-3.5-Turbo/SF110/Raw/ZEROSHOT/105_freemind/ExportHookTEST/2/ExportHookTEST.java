import static org.junit.Assert.*;
import org.junit.Test;
import freemind.extensions.ExportHook;
import freemind.view.mindmapview.*;
import freemind.modes.*;
import javax.swing.*;
import javax.swing.filechooser.*;
import java.io.*;
import java.awt.*;
import java.awt.image.*;
import java.text.*;
import java.net.*;

public class ExportHookTest {
  
  @Test
  public void testChooseFile() {
    ExportHook exportHook = new ExportHook();
    ModeController controller = new ModeController();
    controller.setFrame(new JFrame());
    controller.setView(new MapView());
    
    File file = exportHook.chooseFile("png", "PNG Image", ".png");
    
    assertNotNull(file);
    assertTrue(file.getName().endsWith(".png"));
  }
  
  @Test
  public void testChooseImageFile() {
    ExportHook exportHook = new ExportHook();
    ModeController controller = new ModeController();
    controller.setFrame(new JFrame());
    controller.setView(new MapView());
    
    File file = exportHook.chooseImageFile("png", "PNG Image", ".png", controller);
    
    assertNotNull(file);
    assertTrue(file.getName().endsWith(".png"));
  }
  
  @Test
  public void testGetTranslatableResourceString() {
    ExportHook exportHook = new ExportHook();
    String resource = exportHook.getTranslatableResourceString("test_resource");
    
    assertNotNull(resource);
    assertTrue(resource.startsWith("%"));
  }
  
  @Test
  public void testCreateBufferedImage() {
    ExportHook exportHook = new ExportHook();
    ModeController controller = new ModeController();
    controller.setView(new MapView());
    
    BufferedImage image = exportHook.createBufferedImage();
    
    assertNotNull(image);
    assertEquals(controller.getView().getWidth(), image.getWidth());
    assertEquals(controller.getView().getHeight(), image.getHeight());
  }
  
  @Test
  public void testCopyFromResource() {
    ExportHook exportHook = new ExportHook();
    exportHook.logger = Logger.getLogger(ExportHook.class.getName());
    String prefix = "test_prefix/";
    String fileName = "test_file";
    String destinationDirectory = "test_directory";
    
    exportHook.copyFromResource(prefix, fileName, destinationDirectory);
    
    File file = new File(destinationDirectory + "/" + fileName);
    assertTrue(file.exists());
  }
  
  @Test
  public void testCopyFromFile() {
    ExportHook exportHook = new ExportHook();
    exportHook.logger = Logger.getLogger(ExportHook.class.getName());
    String dir = "test_dir";
    String fileName = "test_file";
    String destinationDirectory = "test_directory";
    
    exportHook.copyFromFile(dir, fileName, destinationDirectory);
    
    File file = new File(destinationDirectory + "/" + fileName);
    assertTrue(file.exists());
  }
  
  @Test
  public void testImageFilterAccept() {
    ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("png", "PNG Image");
    File directory = new File("test_directory");
    File fileWithExtension = new File("test_file.png");
    File fileWithoutExtension = new File("test_file");
    
    assertTrue(imageFilter.accept(directory));
    assertTrue(imageFilter.accept(fileWithExtension));
    assertFalse(imageFilter.accept(fileWithoutExtension));
  }
  
  @Test
  public void testImageFilterGetDescription() {
    ExportHook.ImageFilter imageFilter1 = new ExportHook.ImageFilter("png", null);
    ExportHook.ImageFilter imageFilter2 = new ExportHook.ImageFilter("png", "PNG Image");
    
    assertEquals("png", imageFilter1.getDescription());
    assertEquals("PNG Image", imageFilter2.getDescription());
  }
}