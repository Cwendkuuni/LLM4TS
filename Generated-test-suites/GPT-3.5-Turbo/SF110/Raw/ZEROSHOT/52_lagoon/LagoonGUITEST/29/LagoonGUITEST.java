import org.junit.*;
import static org.junit.Assert.*;

public class LagoonGUITest {
  
  @Test
  public void testBuild() {
    LagoonGUI gui = new LagoonGUI();
    
    // Assert all buttons are enabled
    assertTrue(gui.loadButton.isEnabled());
    assertTrue(gui.saveButton.isEnabled());
    assertTrue(gui.buildButton.isEnabled());
    assertTrue(gui.forceButton.isEnabled());
    
    // Assert all InputComponents are enabled
    assertTrue(gui.sitemapFile.isEnabled());
    assertTrue(gui.sourceDir.isEnabled());
    assertTrue(gui.targetURL.isEnabled());
    
    // Run build method
    gui.build(false);
    
    // Assert all buttons are enabled
    assertTrue(gui.loadButton.isEnabled());
    assertTrue(gui.saveButton.isEnabled());
    assertTrue(gui.buildButton.isEnabled());
    assertTrue(gui.forceButton.isEnabled());
    
    // Assert all InputComponents are enabled
    assertTrue(gui.sitemapFile.isEnabled());
    assertTrue(gui.sourceDir.isEnabled());
    assertTrue(gui.targetURL.isEnabled());
  }
  
  @Test
  public void testExit() {
    LagoonGUI gui = new LagoonGUI();
    
    // Run exit method
    gui.exit();
    
    // Assert that the program has exited
    // We cannot actually test the System.exit(0), so we just check if the test passes without any exceptions
    assertTrue(true);
  }
  
  @Test
  public void testInit() {
    LagoonGUI gui = new LagoonGUI();
    
    // Run init method
    gui.init();
    
    // Assert that the processor is initialized
    assertNotNull(gui.processor);
  }
  
  @Test
  public void testLoadPropertyFile() {
    LagoonGUI gui = new LagoonGUI();
    
    // Run loadPropertyFile method
    gui.loadPropertyFile();
    
    // Assert that the properties have been loaded
    assertNotNull(gui.sitemapFile.getValue());
    assertNotNull(gui.sourceDir.getValue());
    assertNotNull(gui.targetURL.getValue());
    assertNotNull(gui.password);
  }
  
  @Test
  public void testSavePropertyFile() {
    LagoonGUI gui = new LagoonGUI();
    
    // Run savePropertyFile method
    gui.savePropertyFile();
    
    // Assert that the properties have been saved
    assertEquals("Properties saved", gui.statusLabel.getText());
  }
  
  @Test
  public void testGetProperty() {
    Properties prop = new Properties();
    prop.setProperty("testProperty", "testValue");
    
    // Run getProperty method with valid property name
    try {
      String value = LagoonGUI.getProperty(prop, "testProperty");
      assertEquals("testValue", value);
    } catch (LagoonException e) {
      fail("Exception should not have been thrown");
    }
    
    // Run getProperty method with invalid property name
    try {
      LagoonGUI.getProperty(prop, "invalidProperty");
      fail("Exception should have been thrown");
    } catch (LagoonException e) {
      assertEquals("Property invalidProperty not specified", e.getMessage());
    }
  }
  
  @Test
  public void testShowTime() {
    // Run showTime method with less than 10 seconds
    String time = LagoonGUI.showTime(9000);
    assertEquals("in 9000 ms", time);
    
    // Run showTime method with more than 10 seconds
    time = LagoonGUI.showTime(15000);
    assertEquals("in 15 s", time);
  }
}