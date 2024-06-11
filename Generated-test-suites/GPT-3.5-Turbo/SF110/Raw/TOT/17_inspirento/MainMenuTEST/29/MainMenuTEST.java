import org.junit.Test;
import static org.junit.Assert.*;

public class MainMenuTest {

  @Test
  public void testCreateMenu_withValidMenuName_returnsJMenuObject() {
      // Arrange
      MainMenu mainMenu = new MainMenu(new InspirentoMediator());

      // Act
      JMenu menu = mainMenu.createMenu("File");

      // Assert
      assertNotNull(menu);
      assertEquals(JMenu.class, menu.getClass());
  }

  @Test
  public void testCreateMenu_withInvalidMenuName_throwsMissingResourceException() {
      // Arrange
      MainMenu mainMenu = new MainMenu(new InspirentoMediator());

      // Act and Assert
      assertThrows(MissingResourceException.class, () -> {
          mainMenu.createMenu("InvalidMenu");
      });
  }

  @Test
  public void testAddShortcutAndIcon_withValidKeyAndExistingShortcut_setsAcceleratorToItem() {
      // Arrange
      MainMenu mainMenu = new MainMenu(new InspirentoMediator());
      JMenuItem item = new JMenuItem();

      // Act
      mainMenu.addShortcutAndIcon(item, "ShortcutKey");

      // Assert
      assertNotNull(item.getAccelerator());
  }

  @Test
  public void testAddShortcutAndIcon_withInvalidKeyAndMissingShortcut_doesNotThrowException() {
      // Arrange
      MainMenu mainMenu = new MainMenu(new InspirentoMediator());
      JMenuItem item = new JMenuItem();

      // Acnt and Assert
      assertDoesNotThrow(() -> {
          mainMenu.addShortcutAndIcon(item, "InvalidKey");
      });
  }

  @Test
  public void testAddShortcutAndIcon_withValidKeyAndExistingIcon_setsIconToItem() {
      // Arrange
      MainMenu mainMenu = new MainMenu(new InspirentoMediator());
      JMenuItem item = new JMenuItem();

      // Act
      mainMenu.addShortcutAndIcon(item, "IconKey");

      // Assert
      assertNotNull(item.getIcon());
  }

  @Test
  public void testAddShortcutAndIcon_withInvalidKeyAndMissingIcon_doesNotThrowException() {
      // Arrange
      MainMenu mainMenu = new MainMenu(new InspirentoMediator());
      JMenuItem item = new JMenuItem();

      // Act and Assert
      assertDoesNotThrow(() -> {
          mainMenu.addShortcutAndIcon(item, "InvalidKey");
      });
  }
}