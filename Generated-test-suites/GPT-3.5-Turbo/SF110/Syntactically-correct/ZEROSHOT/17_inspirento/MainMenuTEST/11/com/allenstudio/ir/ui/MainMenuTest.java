import org.junit.*;
import static org.junit.Assert.*;

public class MainMenuTest {
  
  @Test
  public void testCreateMenu() {
    MainMenu mainMenu = new MainMenu(null);
    assertNotNull(mainMenu.createMenu("menu1"));
  }
  
  @Test
  public void testAddShortcutAndIcon() {
    MainMenu mainMenu = new MainMenu(null);
    mainMenu.addShortcutAndIcon(new JMenuItem(), "item1");
  }
  
  @Test
  public void testChanged() {
    MainMenu mainMenu = new MainMenu(null);
    mainMenu.changed();
  }
}