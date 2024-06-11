
import org.junit.Assert;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import nu.staldal.lagoon.core.LagoonException;
import nu.staldal.xtree.Element;


public class LagoonGUITest {

    @Test
    public void testMain() throws Exception {
      // Test 1: test main() with no arguments
      LagoonGUI.main(new String[0]);

      // Test 2: test main() with a property file argument
      LagoonGUI.main(new String[]{"test.properties"});
    
      // Verify the output file was generated
      Assert.assertTrue(Files.exists(Paths.get("output.html")));
    }
    
    @Test
    public void testWindowEvents() {
      LagoonGUI lagoonGUI = new LagoonGUI();

      // Test 1: test windowOpened() method
      lagoonGUI.windowOpened(null);

      // Test 2: test windowClosing() method
      lagoonGUI.windowClosing(null);

      // Test 3: test windowClosed() method
      lagoonGUI.windowClosed(null);

      // Test 4: test windowIconified() method
      lagoonGUI.windowIconified(null);

      // Test 5: test windowDeiconified() method
      lagoonGUI.windowDeiconified(null);

      // Test 6: test windowActivated() method
      lagoonGUI.windowActivated(null);

      // Test 7: test windowDeactivated() method
      lagoonGUI.windowDeactivated(null);
    }
    
    @Test
    public void testBuild() {
      LagoonGUI lagoonGUI = new LagoonGUI();

      // Test 1: test build() method with force set to false
      lagoonGUI.build(false);

      // Test 2: test build() method with force set to true
      lagoonGUI.build(true);
    }

    @Test
    public void testExit() {
      LagoonGUI lagoonGUI = new LagoonGUI();

      // Test 1: test exit() method
      lagoonGUI.exit();
    }

    @Test
    public void testInit() {
      LagoonGUI lagoonGUI = new LagoonGUI();

      // Test 1: test init() method
      lagoonGUI.init();
    }

    @Test
    public void testLoadPropertyFile() {
      LagoonGUI lagoonGUI = new LagoonGUI();

      // Test 1: test loadPropertyFile() method
      lagoonGUI.loadPropertyFile();
    }

    @Test
    public void testLoadProperties() throws IOException, LagoonException {
      LagoonGUI lagoonGUI = new LagoonGUI();

      // Create a temporary property file for testing
      File tempFile = File.createTempFile("test", ".properties");
      PrintWriter writer = new PrintWriter(tempFile);
      writer.println("sitemapFile=file.xml");
      writer.println("sourceDir=source");
      writer.println("targetURL=http://example.com");
      writer.close();

      // Test loadProperties() method
      lagoonGUI.loadProperties(tempFile);

      // Verify the values are correctly loaded
      Assert.assertEquals("file.xml", lagoonGUI.sitemapFile.getValue());
      Assert.assertEquals("source", lagoonGUI.sourceDir.getValue());
      Assert.assertEquals("http://example.com", lagoonGUI.targetURL.getValue());

      // Delete the temporary file
      tempFile.delete();
    }

    @Test
    public void testSavePropertyFile() throws IOException {
      LagoonGUI lagoonGUI = new LagoonGUI();

      // Create a temporary property file for testing
      File tempFile = File.createTempFile("test", ".properties");

      // Set the property values
      lagoonGUI.sitemapFile.setValue("file.xml");
      lagoonGUI.sourceDir.setValue("source");
      lagoonGUI.targetURL.setValue("http://example.com");

      // Test savePropertyFile() method
      lagoonGUI.savePropertyFile();

      // Verify the property file was created
      Assert.assertTrue(Files.exists(Paths.get(tempFile.getPath())));

      // Delete the temporary file
      tempFile.delete();
    }

    @Test
    public void testGetProperty() throws LagoonException {
      LagoonGUI lagoonGUI = new LagoonGUI();

      // Test 1: test getProperty() method for an existing property
      Assert.assertEquals("value", lagoonGUI.getProperty("property=value", "property"));

      // Test 2: test getProperty() method for a missing property
      try {
        lagoonGUI.getProperty("property=value", "missing");
        Assert.fail("Expected LagoonException to be thrown");
      } catch (LagoonException e) {
        Assert.assertEquals("Property missing not specified", e.getMessage());
      }
    }

    @Test
    public void testShowTime() {
      LagoonGUI lagoonGUI = new LagoonGUI();

      // Test 1: test showTime() method for milliseconds less than 10,000
      Assert.assertEquals("in 5000 ms", lagoonGUI.showTime(5000));

      // Test 2: test showTime() method for milliseconds greater than 10,000
      Assert.assertEquals("in 20 s", lagoonGUI.showTime(20000));
    }
}

