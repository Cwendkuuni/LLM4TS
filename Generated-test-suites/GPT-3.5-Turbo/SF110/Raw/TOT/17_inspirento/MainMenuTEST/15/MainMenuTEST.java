import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MainMenuTest {

  private MainMenu mainMenu;

  @Before
  public void setUp() {
    mainMenu = new MainMenu(new InspirentoMediator());
  }

  @Test
  public void testCreateMenu_WithValidMenu_ReturnsJMenuObject() {
    String menu = "FILE";
    JMenu result = mainMenu.createMenu(menu);
    Assert.assertNotNull(result);
  }

  @Test
  public void testCreateMenu_WithInvalidMenu_ReturnsNull() {
    String menu = "INVALID_MENU";
    JMenu result = mainMenu.createMenu(menu);
    Assert.assertNull(result);
  }

  @Test
  public void testAddShortcutAndIcon_WithValidKey_AddsShortcutAndIcon() {
    JMenuItem item = new JMenuItem();
    String key = "FILE";
    mainMenu.addShortcutAndIcon(item, key);
    // Assert that the item has a valid accelerator and icon
    Assert.assertNotNull(item.getAccelerator());
    Assert.assertNotNull(item.getIcon());
  }

  @Test
  public void testAddShortcutAndIcon_WithInvalidKey_DoesNotAddShortcutAndIcon() {
    JMenuItem item = new JMenuItem();
    String key = "INVALID_KEY";
    mainMenu.addShortcutAndIcon(item, key);
    // Assert that the item does not have an accelerator or icon
    Assert.assertNull(item.getAccelerator());
    Assert.assertNull(item.getIcon());
  }

  @Test
  public void testChanged_WhenCalled_DoesNothing() {
    // Since changed() method has an empty implementation, we do not need to test it further.
    // This test case is included for completeness.
    mainMenu.changed();
  }
}