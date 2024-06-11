import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;
import java.awt.*;
import java.awt.image.*;
import org.mockito.*;
import static org.mockito.Mockito.*;

public class ExportHookTest {

  @Test
  public void testChooseFile_typical() {
    ExportHook exportHook = new ExportHook();
    String type = "jpg";
    String description = "JPEG Image";
    String nameExtension = ".jpg";
    ModeController mockController = mock(ModeController.class);
    FileNameExtensionFilter mockFilter = mock(FileNameExtensionFilter.class);
    FreeMindFileDialog mockChooser = mock(FreeMindFileDialog.class);
    Container mockComponent = mock(Container.class);
    File mockMapFile = mock(File.class);
    File mockChosenFile = mock(File.class);
    
    when(exportHook.getController()).thenReturn(mockController);
    when(mockController.getFrame().getContentPane()).thenReturn(mockComponent);
    when(mockController.getFileChooser(mockFilter)).thenReturn(mockChooser);
    when(mockController.getMap().getFile()).thenReturn(mockMapFile);
    when(mockMapFile.getAbsolutePath()).thenReturn("/path/to/map.mm");
    when(mockChooser.showSaveDialog(mockComponent)).thenReturn(0);
    when(mockChooser.getSelectedFile()).thenReturn(mockChosenFile);
    when(mockChosenFile.getName()).thenReturn("chosenFile.jpg");
    when(mockChosenFile.getParent()).thenReturn("/path/to/save");

    File result = exportHook.chooseFile(type, description, nameExtension);

    assertEquals(mockChosenFile, result);
    verify(mockController).getFileChooser(mockFilter);
    verify(mockController).getMap();
    verify(mockController.getFrame().getContentPane());
    verify(mockChooser).setSelectedFile(any(File.class));
    verify(mockChooser).showSaveDialog(mockComponent);
    verify(mockChosenFile).exists();
  }

  @Test
  public void testChooseFile_noFileSelected() {
    ExportHook exportHook = new ExportHook();
    String type = "jpg";
    String description = "JPEG Image";
    String nameExtension = ".jpg";
    ModeController mockController = mock(ModeController.class);
    FileNameExtensionFilter mockFilter = mock(FileNameExtensionFilter.class);
    FreeMindFileDialog mockChooser = mock(FreeMindFileDialog.class);
    Container mockComponent = mock(Container.class);
    File mockMapFile = mock(File.class);
    
    when(exportHook.getController()).thenReturn(mockController);
    when(mockController.getFrame().getContentPane()).thenReturn(mockComponent);
    when(mockController.getFileChooser(mockFilter)).thenReturn(mockChooser);
    when(mockController.getMap().getFile()).thenReturn(mockMapFile);
    when(mockMapFile.getAbsolutePath()).thenReturn("/path/to/map.mm");
    when(mockChooser.showSaveDialog(mockComponent)).thenReturn(1);

    File result = exportHook.chooseFile(type, description, nameExtension);

    assertNull(result);
  }

  @Test
  public void testChooseFile_fileExtensionMismatch() {
    ExportHook exportHook = new ExportHook();
    String type = "jpg";
    String description = "JPEG Image";
    String nameExtension = ".jpg";
    ModeController mockController = mock(ModeController.class);
    FileNameExtensionFilter mockFilter = mock(FileNameExtensionFilter.class);
    FreeMindFileDialog mockChooser = mock(FreeMindFileDialog.class);
    Container mockComponent = mock(Container.class);
    File mockMapFile = mock(File.class);
    File mockChosenFile = mock(File.class);
    
    when(exportHook.getController()).thenReturn(mockController);
    when(mockController.getFrame().getContentPane()).thenReturn(mockComponent);
    when(mockController.getFileChooser(mockFilter)).thenReturn(mockChooser);
    when(mockController.getMap().getFile()).thenReturn(mockMapFile);
    when(mockMapFile.getAbsolutePath()).thenReturn("/path/to/map.mm");
    when(mockChooser.showSaveDialog(mockComponent)).thenReturn(0);
    when(mockChooser.getSelectedFile()).thenReturn(mockChosenFile);
    when(mockChosenFile.getName()).thenReturn("chosenFile.png");
    when(mockChosenFile.getParent()).thenReturn("/path/to/save");
    when(mockChosenFile.exists()).thenReturn(true);

    File result = exportHook.chooseFile(type, description, nameExtension);

    assertNotNull(result);
    verify(mockChosenFile).getParent();
    verify(mockChosenFile, times(2)).exists();
  }

  @Test
  public void testGetTranslatableResourceString_typical() {
    ExportHook exportHook = new ExportHook();
    String resourceName = "%resource";

    when(exportHook.getController().getText("resource")).thenReturn("Translated Resource");

    String result = exportHook.getTranslatableResourceString(resourceName);

    assertEquals("Translated Resource", result);
    verify(exportHook.getController()).getText("resource");
  }

  @Test
  public void testGetTranslatableResourceString_noTranslationAvailable() {
    ExportHook exportHook = new ExportHook();
    String resourceName = "resource";

    when(exportHook.getResourceString(resourceName)).thenReturn("%resource");

    String result = exportHook.getTranslatableResourceString(resourceName);

    assertEquals("%resource", result);
    verify(exportHook.getResourceString("resource"));
  }

  @Test
  public void testCreateBufferedImage_typical() {
    ExportHook exportHook = new ExportHook();
    ModeController mockController = mock(ModeController.class);
    MapView mockView = mock(MapView.class);
    Graphics mockGraphics = mock(Graphics.class);
    BufferedImage mockImage = mock(BufferedImage.class);
    
    when(exportHook.getController()).thenReturn(mockController);
    when(mockController.getView()).thenReturn(mockView);
    when(mockView.getInnerBounds()).thenReturn(new Rectangle(0, 0, 100, 100));
    when(mockView.getWidth()).thenReturn(200);
    when(mockView.getHeight()).thenReturn(200);
    when(mockView.createImage(200, 200)).thenReturn(mockImage);
    when(mockImage.getGraphics()).thenReturn(mockGraphics);
    when(mockGraphics.create()).thenReturn(mockGraphics);

    BufferedImage result = exportHook.createBufferedImage();

    assertEquals(mockImage, result);
    verify(mockView).preparePrinting();
    verify(mockView).print(mockGraphics);
    verify(mockGraphics).clipRect(0, 0, 100, 100);
    verify(mockView).endPrinting();
    verify(mockImage).getSubimage(0, 0, 100, 100);
  }

  @Test
  public void testCreateBufferedImage_nullView() {
    ExportHook exportHook = new ExportHook();
    ModeController mockController = mock(ModeController.class);
    
    when(exportHook.getController()).thenReturn(mockController);
    when(mockController.getView()).thenReturn(null);

    BufferedImage result = exportHook.createBufferedImage();

    assertNull(result);
  }

  @Test
  public void testCopyFromResource_typical() throws Exception {
    ExportHook exportHook = new ExportHook();
    String prefix = "/path/to/";
    String fileName = "resourceFile.txt";
    String destinationDirectory = "/path/to/save";
    String resourcePath = "/path/to/resourceFile.txt";
    URL mockResource = mock(URL.class);
    InputStream mockInputStream = mock(InputStream.class);
    OutputStream mockOutputStream = mock(OutputStream.class);
    File mockDestinationFile = mock(File.class);
    
    when(exportHook.getResource(prefix + fileName)).thenReturn(mockResource);
    when(mockResource.openStream()).thenReturn(mockInputStream);
   	when(mockResource.toString()).thenReturn(resourcePath);
    when(mockInputStream.available()).thenReturn(10);
    when(exportHook.getResourceString(anyString())).thenReturn("File already exists");
    doNothing().when(exportHook.logger).finest(anyString());
    when(mockDestinationFile.getAbsolutePath()).thenReturn("/path/to/save/resourceFile.txt");
    when(mockOutputStream.toString()).thenReturn("/path/to/save/resourceFile.txt");
    doNothing().when(Tools).copyStream(mockInputStream, mockOutputStream, true);

    exportHook.copyFromResource(prefix, fileName, destinationDirectory);

    verify(exportHook).getResource(prefix + fileName);
    verify(mockResource).openStream();
    verify(mockInputStream).available();
    verify(exportHook.logger).finest("searching for " + prefix + fileName);
    verify(exportHook.logger).severe("Cannot find resource: " + prefix + fileName);
    verify(mockOutputStream).flush();
    verify(mockDestinationFile).getAbsolutePath();
    verify(mockOutputStream).close();
    verify(Tools).copyStream(mockInputStream, mockOutputStream, true);
  }

  @Test(expected=Exception.class)
  public void testCopyFromResource_resourceNotFound() throws Exception {
    ExportHook exportHook = new ExportHook();
    String prefix = "/path/to/";
    String fileName = "resourceFile.txt";
    String destinationDirectory = "/path/to/save";
    String resourcePath = "/path/to/resourceFile.txt";
    URL mockResource = mock(URL.class);
    File mockDestinationFile = mock(File.class);

    when(exportHook.getResource(prefix + fileName)).thenReturn(null);
    when(mockDestinationFile.getAbsolutePath()).thenReturn("/path/to/save/resourceFile.txt");
    when(mockOutputStream.toString()).thenReturn("/path/to/save/resourceFile.txt");
    
    try {
      exportHook.copyFromResource(prefix, fileName, destinationDirectory);
    } catch (Exception e) {
      assertEquals("Cannot find resource: /path/to/resourceFile.txt", e.getMessage());
      throw e;
    }    
  }

  @Test(expected=Exception.class)
  public void testCopyFromResource_fileNotFound() throws Exception {
    ExportHook exportHook = new ExportHook();
    String prefix = "/path/to/";
    String fileName = "resourceFile.txt";
    String destinationDirectory = "/path/to/save";
    String resourcePath = "/path/to/resourceFile.txt";
    URL mockResource = mock(URL.class);
    InputStream mockInputStream = mock(InputStream.class);
    OutputStream mockOutputStream = mock(OutputStream.class);
    File mockDestinationFile = mock(File.class);

    when(exportHook.getResource(prefix + fileName)).thenReturn(mockResource);
    when(mockResource.openStream()).thenReturn(null);
    when(mockDestinationFile.getAbsolutePath()).thenReturn("/path/to/save/resourceFile.txt");
    when(mockOutputStream.toString()).thenReturn("/path/to/save/resourceFile.txt");

    try {
      exportHook.copyFromResource(prefix, fileName, destinationDirectory);
    } catch (Exception e) {
      assertEquals("File not found or could not be copied. Was earching for /path/to/resourceFile.txt and should go to /path/to/save", e.getMessage());
      throw e;
    }    
  }

  @Test
  public void testCopyFromFile_typical() throws Exception {
    ExportHook exportHook = new ExportHook();
    String dir = "/path/to/";
    String fileName = "file.txt";
    String destinationDirectory = "/path/to/save";
    File mockFile = mock(File.class);
    File mockDestinationFile = mock(File.class);
    InputStream mockInputStream = mock(InputStream.class);
    OutputStream mockOutputStream = mock(OutputStream.class);

    when(exportHook.logger).finest("searching for " + dir + fileName);
    when(Tools.getExtension(fileName)).thenReturn("txt");
    when(mockFile.exists()).thenReturn(true);
    when(mockDestinationFile.getAbsolutePath()).thenReturn("/path/to/save/file.txt");
    when(mockOutputStream.toString()).thenReturn("/path/to/save/file.txt");
    when(mockDestinationFile.getParent()).thenReturn("/path/to/save");
    doNothing().when(Tools).copyStream(mockInputStream, mockOutputStream, true);
    
    exportHook.copyFromFile(dir, fileName, destinationDirectory);

    verify(exportHook.logger).finest("searching for " + dir + fileName);
    verify(Tools).getExtension(fileName);
    verify(mockOutputStream).toString();
    verify(mockDestinationFile).getAbsolutePath();
    verify(mockOutputStream).close();
    verify(Tools).copyStream(mockInputStream, mockOutputStream, true);
  }

  @Test(expected=Exception.class)
  public void testCopyFromFile_fileNotFound() throws Exception {
    ExportHook exportHook = new ExportHook();
    String dir = "/path/to/";
    String fileName = "file.txt";
    String destinationDirectory = "/path/to/save";
    File mockFile = mock(File.class);
    File mockDestinationFile = mock(File.class);

    when(exportHook.logger).finest("searching for " + dir + fileName);
    when(Tools.getExtension(fileName)).thenReturn("txt");
    when(mockFile.exists()).thenReturn(false);
    
    try {
      exportHook.copyFromFile(dir, fileName, destinationDirectory);
    } catch (Exception e) {
      assertEquals("Cannot find resource: /path/to/file.txt", e.getMessage());
      throw e;
    }    
  }

  @After
  public void tearDown() {
    reset(exportHook, exportHook.logger);
  }
}