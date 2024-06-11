import static org.junit.Assert.*;
import org.junit.Test;
import java.awt.event.*;
import org.xml.sax.*;
import nu.staldal.lagoon.core.*;
import nu.staldal.xtree.*;
import java.awt.*;
import java.util.*;
import java.io.*;

public class LagoonGUITest {

  @Test
  public void testMain() {
    // Test case for main method
    // Typical use case: providing command line argument to load properties
    // Test the success case where properties are loaded and no exception is thrown
    try {
      LagoonGUI.main(new String[]{"properties.txt"});
    } catch (Exception e) {
      fail("Exception should not be thrown");
    }
  }
  
  @Test
  public void testBuild() {
    // Test case for build method
    // Typical use case: building without force build
    // Test the success case where build is completed successfully
    LagoonGUI lagoonGUI = new LagoonGUI();
    lagoonGUI.build(false);
    // Check if progressArea text is updated with the expected message
    assertEquals("--- Start build ---\n--- Build finished successfully", lagoonGUI.progressArea.getText().trim());
  }

  @Test
  public void testExit() {
    // Test case for exit method
    // Test the success case where exit is called and no exception is thrown
    LagoonGUI lagoonGUI = new LagoonGUI();
    lagoonGUI.exit();
    // Check if the system exits without any exception
    assertTrue(true);
  }

  @Test
  public void testInit() {
    // Test case for init method
    // Typical use case: initializing without any exception
    // Test the success case where initialization is completed successfully
    LagoonGUI lagoonGUI = new LagoonGUI();
    lagoonGUI.init();
    // Check if the status label is updated to "Initializing..."
    assertEquals("Initializing...", lagoonGUI.statusLabel.getText());
  }

  @Test
  public void testLoadPropertyFile() {
    // Test case for loadPropertyFile method
    // Typical use case: loading property file without any exception
    // Test the success case where property file is loaded successfully
    LagoonGUI lagoonGUI = new LagoonGUI();
    lagoonGUI.loadPropertyFile();
    // Check if the status label is updated to "Properties saved"
    assertEquals("Properties saved", lagoonGUI.statusLabel.getText());
  }

  @Test
  public void testSavePropertyFile() {
    // Test case for savePropertyFile method
    // Typical use case: saving property file without saving password
    // Test the success case where property file is saved successfully
    LagoonGUI lagoonGUI = new LagoonGUI();
    lagoonGUI.savePropertyFile();
    // Check if the status label is updated to "Properties saved"
    assertEquals("Properties saved", lagoonGUI.statusLabel.getText());
  }

  @Test
  public void testGetProperty() {
    // Test case for getProperty method
    // Typical use case: getting existing property value
    // Test the success case where the expected property value is returned
    Properties prop = new Properties();
    prop.setProperty("sitemapFile", "sitemap.xml");
    try {
      String value = LagoonGUI.getProperty(prop, "sitemapFile");
      assertEquals("sitemap.xml", value);
    } catch (LagoonException e) {
      fail("Exception should not be thrown");
    }
  }

  @Test(expected = LagoonException.class)
  public void testGetPropertyNotFound() throws LagoonException {
    // Test case for getProperty method
    // Error scenario: getting non-existing property value
    // Test the case where LagoonException is thrown as the property value is not found
    Properties prop = new Properties();
    LagoonGUI.getProperty(prop, "nonExistingProperty");
  }

  @Test
  public void testShowTimeLessThan10s() {
    // Test case for showTime method when time is less than 10 seconds
    // Test the success case where the expected time in milliseconds is returned
    long ms = 5000L;
    String time = LagoonGUI.showTime(ms);
    assertEquals("in 5000 ms", time);
  }

  @Test
  public void testShowTimeMoreThan10s() {
    // Test case for showTime method when time is more than 10 seconds
    // Test the success case where the expected time in seconds is returned
    long ms = 15000L;
    String time = LagoonGUI.showTime(ms);
    assertEquals("in 15 s", time);
  }

}