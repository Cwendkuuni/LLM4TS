import org.junit.Test;
import static org.junit.Assert.*;

public class MainMenuTest {

   MainMenu mainMenu = new MainMenu(new InspirentoMediator());

   @Test
   public void testCreateMenu_withValidMenuLabelAndValidItemLabel_shouldCreateMenu() {
       String menu = "File";
       String item = "Open";
       JMenu jMenu = mainMenu.createMenu(menu);
       assertNotNull(jMenu);
       assertEquals(menu, jMenu.getText());
       JMenuItem jMenuItem = (JMenuItem) jMenu.getMenuComponent(0);
       assertNotNull(jMenuItem);
       assertEquals(item, jMenuItem.getText());
   }

   @Test
   public void testCreateMenu_withInvalidMenuLabel_shouldCreateMenuWithDefaultLabel() {
       String menu = "InvalidMenu";
       JMenu jMenu = mainMenu.createMenu(menu);
       assertNotNull(jMenu);
       assertEquals(menu, jMenu.getText());
   }

   @Test
   public void testAddShortcutAndIcon_withValidKey_shouldSetShortcutAndIcon() {
       String key = "File";
       JMenuItem jMenuItem = new JMenuItem();
       mainMenu.addShortcutAndIcon(jMenuItem, key);
       assertNotNull(jMenuItem.getAccelerator());
       assertNotNull(jMenuItem.getIcon());
   }

   @Test
   public void testAddShortcutAndIcon_withInvalidKey_shouldNotSetShortcutAndIcon() {
       String key = "InvalidKey";
       JMenuItem jMenuItem = new JMenuItem();
       mainMenu.addShortcutAndIcon(jMenuItem, key);
       assertNull(jMenuItem.getAccelerator());
       assertNull(jMenuItem.getIcon());
   }

   @Test
   public void testChanged_shouldNotThrowException() {
       assertDoesNotThrow(() -> mainMenu.changed());
   }
}