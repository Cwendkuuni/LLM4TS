
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import javax.swing.filechooser.FileFilter;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.MessageFormat;
import javax.swing.JOptionPane;
import freemind.modes.ModeController;
import freemind.extensions.ExportHook;
import freemind.resources.Resources;
import freemind.tools.Tools;

public class ExportHookTest {
  
  private ExportHook exportHook;
  
  @Before
  public void setup() {
    exportHook = new ExportHook();
  }
  
  @Test
  public void testChooseFile() {
    String type = "jpg";
    String description = "JPEG Image";
    String nameExtension = "-export";
    
    File chosenFile = exportHook.chooseFile(type, description, nameExtension);
    Assert.assertNotNull(chosenFile);
  }
  
  @Test
  public void testChooseFile_Cancel() {
    String type = "jpg";
    String description = "JPEG Image";
    String nameExtension = "-export";
    
    // Mocking the JOptionPane.showSaveDialog method to return '1' which represents 'CANCEL_OPTION'
    JOptionPane.showSaveDialog(null);
    File chosenFile = exportHook.chooseFile(type, description, nameExtension);
    Assert.assertNull(chosenFile);
  }
  
  @Test
  public void testChooseImageFile() {
    String type = "jpg";
    String description = "JPEG Image";
    String nameExtension = "-export";
    ModeController controller = new ModeController();
    
    Container component = new Container();
    BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
    Graphics graphics = image.getGraphics();
    
    controller.getFrame().getContentPane().add(component);
    controller.setMap(new File("mindmap.mm"));
    controller.setFileChooser(new FreeMindFileDialog());
    controller.getMap().setFile(new File("mindmap.mm"));
    
    File chosenFile = exportHook.chooseImageFile(type, description, nameExtension, controller);
    Assert.assertNotNull(chosenFile);
  }
  
  @Test
  public void testChooseImageFile_MockFileChooser() {
    String type = "jpg";
    String description = "JPEG Image";
    String nameExtension = "-export";
    ModeController controller = new ModeController();
    
    Container component = new Container();
    BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
    Graphics graphics = image.getGraphics();
    
    controller.getFrame().getContentPane().add(component);
    controller.setMap(new File("mindmap.mm"));
    
    File chosenFile = exportHook.chooseImageFile(type, description, nameExtension, controller);
    Assert.assertNotNull(chosenFile);
  }
  
  @Test
  public void testChooseImageFile_Cancel() {
    String type = "jpg";
    String description = "JPEG Image";
    String nameExtension = "-export";
    ModeController controller = new ModeController();
    
    Container component = new Container();
    BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
    Graphics graphics = image.getGraphics();
    
    controller.getFrame().getContentPane().add(component);
    controller.setMap(new File("mindmap.mm"));
    controller.setFileChooser(new FreeMindFileDialog());
    controller.getMap().setFile(new File("mindmap.mm"));
    
    // Mocking the JOptionPane.showConfirmDialog method to return '1' which represents 'CANCEL_OPTION'
    JOptionPane.showConfirmDialog(component, "Overwrite?", "Overwrite", 0);
    File chosenFile = exportHook.chooseImageFile(type, description, nameExtension, controller);
    Assert.assertNull(chosenFile);
  }
  
  @Test
  public void testChooseImageFile_ExtensionNotMatch() {
    String type = "png";
    String description = "PNG Image";
    String nameExtension = "-export";
    ModeController controller = new ModeController();
    
    Container component = new Container();
    BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
    Graphics graphics = image.getGraphics();
    
    controller.getFrame().getContentPane().add(component);
    controller.setMap(new File("mindmap.mm"));
    controller.setFileChooser(new FreeMindFileDialog());
    controller.getMap().setFile(new File("mindmap.mm"));
    
    File chosenFile = exportHook.chooseImageFile(type, description, nameExtension, controller);
    Assert.assertNotNull(chosenFile);
    Assert.assertTrue(chosenFile.getPath().endsWith(".png"));
  }
  
  @Test
  public void testGetTranslatableResourceString() {
    String resourceName = "test_resource";
    
    String result = exportHook.getTranslatableResourceString(resourceName);
    Assert.assertEquals(result, "Translated Test Resource");
  }
  
  @Test
  public void testCreateBufferedImage() {
    BufferedImage image = exportHook.createBufferedImage();
    Assert.assertNotNull(image);
  }
  
  @Test
  public void testCopyFromResource() {
    String prefix = "resources/";
    String fileName = "testfile.txt";
    String destinationDirectory = "destination";
    
    exportHook.copyFromResource(prefix, fileName, destinationDirectory);
    File copiedFile = new File(destinationDirectory + "/" + fileName);
    Assert.assertTrue(copiedFile.exists());
    boolean deleteSuccess = copiedFile.delete();
    Assert.assertTrue(deleteSuccess);
  }
  
  @Test
  public void testCopyFromFile() {
    String dir = "resources";
    String fileName = "testfile.txt";
    String destinationDirectory = "destination";
    
    exportHook.copyFromFile(dir, fileName, destinationDirectory);
    File copiedFile = new File(destinationDirectory + "/" + fileName);
    Assert.assertTrue(copiedFile.exists());
    boolean deleteSuccess = copiedFile.delete();
    Assert.assertTrue(deleteSuccess);
  }
  
  @Test
  public void testImageFilterAccept() {
    File pngFile = new File("image.png");
    File jpgFile = new File("image.jpg");
    
    ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("png", "PNG Image");
    boolean acceptPNG = imageFilter.accept(pngFile);
    boolean acceptJPG = imageFilter.accept(jpgFile);
    
    Assert.assertTrue(acceptPNG);
    Assert.assertFalse(acceptJPG);
  }
  
  @Test
  public void testImageFilterGetDescription() {
    ExportHook.ImageFilter imageFilter = new ExportHook.ImageFilter("png", "PNG Image");
    String description = imageFilter.getDescription();
    
    Assert.assertEquals(description, "PNG Image");
  }
}
