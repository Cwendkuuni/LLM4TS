import org.junit.*;
import javax.swing.*;
import java.net.*;
import java.util.*;

public class MainMenuTest {

   @Test
   public void testConstructor() {
      InspirentoMediator mediator = new InspirentoMediator();
      MainMenu mainMenu = new MainMenu(mediator);
      assertEquals(mediator, mainMenu.getMediator());
   }

   @Test
   public void testCreateMenu() {
      MainMenu mainMenu = new MainMenu(new InspirentoMediator());
      String menu = "File";
      JMenu createdMenu = mainMenu.createMenu(menu);
      assertNotNull(createdMenu);
      assertEquals(menu, createdMenu.getText());
   }

   @Test
   public void testAddShortcutAndIcon() {
      MainMenu mainMenu = new MainMenu(new InspirentoMediator());

      JMenuItem item = new JMenuItem();
      String key = "Item";

      mainMenu.addShortcutAndIcon(item, key);

      assertEquals(KeyStroke.getKeyStroke(UIResources.getString(key + "Shortcut")), item.getAccelerator());

      URL expectedURL = mainMenu.getClass().getResource(UIResources.getString(key + "Icon"));
      assertEquals(expectedURL, ((ImageIcon)item.getIcon()).getImage().getSource());
   }

   @Test
   public void testChanged() {
      MainMenu mainMenu = new MainMenu(new InspirentoMediator());

      // we cannot test the actual behavior of this method as it does nothing,
      // so we can only assert that it executes without throwing any exceptions
      mainMenu.changed();
   }
}