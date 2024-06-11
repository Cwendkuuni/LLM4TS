
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import freemind.extensions.ExportHook;
import freemind.extensions.ExportHook.ImageFilter;
import freemind.modes.FreeMindFileDialog;
import freemind.main.ModeControllerHookAdapter;
import freemind.view.mindmapview.MapView;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Rectangle;

public class ExportHookTest {
  
  private ExportHook exportHook;
  
  @Before
  public void setUp() {
    exportHook = new ExportHook();
  }
  
  @Test
  public void testChooseFile_typicalCase_returnsFile() {
    // Arrange
    String type = "jpg";
    String description = "JPEG Image";
    String nameExtension = "_export";
    
    // Act
    File result = exportHook.chooseFile(type, description, nameExtension);
    
    // Assert
    assertNotNull(result);
    assertEquals(".jpg", result.getName().substring(result.getName().lastIndexOf(".")));
  }
  
  @Test
  public void testChooseFile_mmFileIsNull_returnsNull() {
    // Arrange
    ExportHook.ModeController modeController = exportHook.new ModeController();
    modeController.setMap(null);
    exportHook.setController(modeController);
    String type = "jpg";
    String description = "JPEG Image";
    String nameExtension = "_export";
    
    // Act
    File result = exportHook.chooseFile(type, description, nameExtension);
    
    // Assert
    assertNull(result);
  }
  
  @Test
  public void testChooseImageFile_typicalCase_returnsFile() {
    // Arrange
    ExportHook.ModeController modeController = exportHook.new ModeController();
    JFileChooser fileChooser = new JFileChooser();
    ImageFilter imageFilter = new ImageFilter("jpg", "JPEG Image");
    File selectedFile = new File("test.jpg");
    Container component = new Container();
    modeController.setFileChooser(fileChooser);
    modeController.setMap(exportHook.new Map());
    fileChooser.setSelectedFile(selectedFile);
    modeController.setFrame(new JFrame());
    modeController.getFrame().setContentPane(component);
    component.add(fileChooser);
    
    // Act
    File result = exportHook.chooseImageFile("jpg", "JPEG Image", null, modeController);
    
    // Assert
    assertNotNull(result);
    assertEquals(selectedFile, result);
  }
  
  @Test
  public void testChooseImageFile_mmFileIsNull_returnsNull() {
    // Arrange
    ExportHook.ModeController modeController = exportHook.new ModeController();
    modeController.setMap(null);
    
    // Act
    File result = exportHook.chooseImageFile("jpg", "JPEG Image", null, modeController);
    
    // Assert
    assertNull(result);
  }
  
  @Test
  public void testGetTranslatableResourceString_returnsResourceString() {
    // Arrange
    String resourceName = "%message.test";
    
    // Act
    String result = exportHook.getTranslatableResourceString(resourceName);
    
    // Assert
    assertEquals("test message", result);
  }
  
  @Test
  public void testCreateBufferedImage_typicalCase_returnsBufferedImage() {
    // Arrange
    String mmFileAbsolutePath = "C:\\test.mm";
    MapView view = exportHook.new MapView();
    view.setFile(new File(mmFileAbsolutePath));
    ExportHook.ModeController modeController = exportHook.new ModeController();
    modeController.setView(view);
    exportHook.setController(modeController);
    
    // Act
    BufferedImage result = exportHook.createBufferedImage();
    
    // Assert
    assertNotNull(result);
    assertEquals(view.getInnerBounds().x, 0);
    assertEquals(view.getInnerBounds().y, 0);
    assertEquals(view.getInnerBounds().width, result.getWidth());
    assertEquals(view.getInnerBounds().height, result.getHeight());
  }
  
  @Test
  public void testCopyFromResource_validResource_copiesFile() {
    // Arrange
    String prefix = "/resources/";
    String fileName = "data.txt";
    String destinationDirectory = "C:\\temp";
    String resourcePath = prefix + fileName;
    InputStream inputStream = this.getClass().getResourceAsStream(resourcePath);
    File outputFile = new File(destinationDirectory + "/" + fileName);
    
    // Act
    exportHook.copyFromResource(prefix, fileName, destinationDirectory);
    
    // Assert
    try {
      BufferedReader reader = new BufferedReader(new FileReader(outputFile));
      String line = reader.readLine();
      
      while (line != null) {
        assertTrue(line.contains("data"));
        line = reader.readLine(); 
      }
      reader.close();
    } 
    catch (IOException e) {
      fail(e.getMessage());
    }
    finally {
      outputFile.delete();
    }
  }
  
  @Test
  public void testCopyFromResource_invalidResource_returnsNull() {
    // Arrange
    String prefix = "/invalidResources/";
    String fileName = "invalidData.txt";
    String destinationDirectory = "C:\\temp";
    
    // Act
    exportHook.copyFromResource(prefix, fileName, destinationDirectory);
    
    // Assert
    assertNull(exportHook.getResource(prefix + fileName));
  }
  
  @Test
  public void testCopyFromFile_validFile_copiesFile() {
    // Arrange
    String dir = "C:\\dir";
    String fileName = "data.txt";
    String destinationDirectory = "C:\\temp";
    File inputFile = new File(dir, fileName);
    File outputFile = new File(destinationDirectory + "/" + fileName);
    
    // Act
    exportHook.copyFromFile(dir, fileName, destinationDirectory);
    
    // Assert
    try {
      BufferedReader reader = new BufferedReader(new FileReader(outputFile));
      String line = reader.readLine();
      
      while (line != null) {
        assertTrue(line.contains("data"));
        line = reader.readLine(); 
      }
      reader.close();
    } 
    catch (IOException e) {
      fail(e.getMessage());
    }
    finally {
      outputFile.delete();
    }
  }
  
  @Test
  public void testCopyFromFile_invalidFile_returnsNull() {
    // Arrange
    String dir = "C:\\invalidDir";
    String fileName = "invalidData.txt";
    String destinationDirectory = "C:\\temp";
    
    // Act
    exportHook.copyFromFile(dir, fileName, destinationDirectory);
    
    // Assert
    File outputFile = new File(destinationDirectory + "/" + fileName);
    assertFalse(outputFile.exists());
  }
  
  @Test
  public void testImageFilter_acceptsImageFile_returnsTrue() {
    // Arrange
    File file = new File("image.jpg");
    ImageFilter filter = exportHook.new ImageFilter("jpg", "JPEG Image");
    
    // Act
    boolean result = filter.accept(file);
    
    // Assert
    assertTrue(result);
  }
  
  @Test
  public void testImageFilter_acceptsNonImageFile_returnsFalse() {
    // Arrange
    File file = new File("document.pdf");
    ImageFilter filter = exportHook.new ImageFilter("jpg", "JPEG Image");
    
    // Act
    boolean result = filter.accept(file);
    
    // Assert
    assertFalse(result);
  }
  
  @Test
  public void testImageFilter_acceptsDirectory_returnsTrue() {
    // Arrange
    File file = new File("C:\\dir");
    ImageFilter filter = exportHook.new ImageFilter("jpg", "JPEG Image");
    
    // Act
    boolean result = filter.accept(file);
    
    // Assert
    assertTrue(result);
  }
  
  @Test
  public void testImageFilter_getDescription_fileHasDescription_returnsDescription() {
    // Arrange
    String type = "jpg";
    String description = "JPEG Image";
    ImageFilter filter = exportHook.new ImageFilter(type, description);
    
    // Act
    String result = filter.getDescription();
    
    // Assert
    assertEquals(description, result);
  }
  
  @Test
  public void testImageFilter_getDescription_fileDoesNotHaveDescription_returnsType() {
    // Arrange
    String type = "jpg";
    String description = null;
    ImageFilter filter = exportHook.new ImageFilter(type, description);
    
    // Act
    String result = filter.getDescription();
    
    // Assert
    assertEquals(type, result);
  }
}
