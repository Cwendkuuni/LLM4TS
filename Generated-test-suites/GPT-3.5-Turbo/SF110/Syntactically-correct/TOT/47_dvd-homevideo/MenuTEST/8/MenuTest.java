import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MenuTest {
  
  private Menu menu;
  private GUI gui;
  
  @Before
  public void setUp() {
    gui = new GUI();
    menu = new Menu("Title", "PicPath", "AudioPath", "TextFilePath", false, gui);
  }
  
  @Test
  public void testInit() {
    Assert.assertTrue(menu.init());
  }
  
  @Test
  public void testRun() {
    menu.run();
    // Add assertions here
  }
  
  @Test
  public void testDVDMainMenu() {
    menu.DVDMainMenu();
    // Add assertions here
  }
  
  @Test
  public void testCreateXML() {
    Assert.assertFalse(menu.createXML());
  }
}