import org.junit.Test;
import static org.junit.Assert.*;

public class LagoonGUITest {

  @Test
  public void testInit() {
    // Test case 1: Initialize LagoonGUI without any exceptions
    // Expected output: LagoonGUI initialized successfully
    LagoonGUI gui = new LagoonGUI();
    gui.init();
    assertNotNull(gui.processor);
    assertEquals("Not initialized", gui.statusLabel.getText());
    
    // Test case 2: Initialize LagoonGUI with IOException
    // Expected output: Error initializing LagoonGUI
    LagoonGUI gui2 = new LagoonGUI();
    gui2.sitemapFile.setValue("path/to/nonexistingfile");
    gui2.init();
    assertNull(gui2.processor);
    assertEquals("Initializing error", gui2.statusLabel.getText());
  }

  @Test
  public void testBuild() {
    // Test case 1: Build with force=false and successful build
    // Expected output: Build finished successfully
    LagoonGUI gui = new LagoonGUI();
    gui.sitemapFile.setValue("path/to/sitemapfile");
    gui.sourceDir.setValue("path/to/sourcedir");
    gui.targetURL.setValue("path/to/targeturl");
    gui.init();
    gui.build(false);
    assertNotEquals("Not initialized", gui.statusLabel.getText());
    assertTrue(gui.progressArea.getText().contains("Build finished successfully"));
    
    // Test case 2: Build with force=true and build with errors
    // Expected output: Build finished with errors
    LagoonGUI gui2 = new LagoonGUI();
    gui2.sitemapFile.setValue("path/to/sitemapfile");
    gui2.sourceDir.setValue("path/to/sourcedir");
    gui2.targetURL.setValue("path/to/targeturl");
    gui2.init();
    gui2.build(true);
    assertNotEquals("Not initialized", gui2.statusLabel.getText());
    assertTrue(gui2.progressArea.getText().contains("Build finished with error(s)"));
    
    // Test case 3: Build with uninitialized LagoonGUI
    // Expected output: Not initialized
    LagoonGUI gui3 = new LagoonGUI();
    gui3.build(false);
    assertEquals("Not initialized", gui3.statusLabel.getText());
  }

  @Test
  public void testExit() {
    // Test case: Exit application
    // Expected output: Application exits without any exception
    LagoonGUI gui = new LagoonGUI();
    gui.exit();
  }

  @Test
  public void testLoadPropertyFile() {
    // Test case 1: Load existing property file
    // Expected output: Properties loaded successfully
    LagoonGUI gui = new LagoonGUI();
    gui.loadProperties(new File("path/to/propertyfile"));
    assertEquals("path/to/sitemapfile", gui.sitemapFile.getValue());
    assertEquals("path/to/sourcedir", gui.sourceDir.getValue());
    assertEquals("path/to/targeturl", gui.targetURL.getValue());
    assertNotNull(gui.password);
    
    // Test case 2: Load non-existing property file
    // Expected output: Error reading property file
    LagoonGUI gui2 = new LagoonGUI();
    gui2.loadProperties(new File("path/to/nonexistingfile"));
    assertNull(gui2.sitemapFile.getValue());
    assertNull(gui2.sourceDir.getValue());
    assertNull(gui2.targetURL.getValue());
    assertNull(gui2.password);
  }

  @Test
  public void testSavePropertyFile() {
    // Test case 1: Save property file without saving password
    // Expected output: Property file saved successfully
    LagoonGUI gui = new LagoonGUI();
    gui.sitemapFile.setValue("path/to/sitemapfile");
    gui.sourceDir.setValue("path/to/sourcedir");
    gui.targetURL.setValue("path/to/targeturl");
    gui.savePropertyFile();
    assertEquals("Properties saved", gui.statusLabel.getText());
    
    // Test case 2: Save property file with saving password
    // Expected output: Property file saved successfully with password
    LagoonGUI gui2 = new LagoonGUI();
    gui2.sitemapFile.setValue("path/to/sitemapfile");
    gui2.sourceDir.setValue("path/to/sourcedir");
    gui2.targetURL.setValue("path/to/targeturl");
    gui2.password = "password123";
    gui2.savePropertyFile();
    assertEquals("Properties saved", gui2.statusLabel.getText());
  }
}