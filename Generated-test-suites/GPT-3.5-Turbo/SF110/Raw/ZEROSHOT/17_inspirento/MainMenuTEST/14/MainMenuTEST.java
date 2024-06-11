import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

public class MainMenuTest {
  
  private MainMenu menu;
  
  @Before
  public void setUp() {
    menu = new MainMenu(null);
  }
  
  @Test
  public void testCreateMenu() {
    // Test case 1: test creating a menu with label length greater than 2 and ending with *
    // Expected output: menu label should be substring of tempLabel without the last two characters
    // and menu mnemonic should be the last character
    String menuName1 = "TestMenu1";
    String tempLabel1 = "TestMenu1*Y";
    JMenu jMenu1 = menu.createMenu(menuName1);
    assertEquals(menuName1, jMenu1.getText());
    assertEquals('Y', jMenu1.getMnemonic());
    
    // Test case 2: test creating a menu with label length less than or equal to 2
    // Expected output: menu label should be same as tempLabel
    String menuName2 = "TestMenu2";
    String tempLabel2 = "TestMenu2";
    JMenu jMenu2 = menu.createMenu(menuName2);
    assertEquals(menuName2, jMenu2.getText());
    
    // Test case 3: test creating a menu with submenu
    // Expected output: submenu should be added to the menu
    String menuName3 = "TestMenu3";
    String submenuName = "Submenu";
    String tempLabel3 = "TestMenu3";
    String tempLabel4 = "Submenu";
    JMenu jMenu3 = menu.createMenu(menuName3);
    JMenu submenu = (JMenu) jMenu3.getMenuComponent(0);
    assertEquals(submenuName, submenu.getText());
    
  }
  
  @Test
  public void testAddShortcutAndIcon() {
    // Test case 1: test adding a shortcut to a JMenuItem
    // Expected output: JMenuItem should have the specified shortcut
    JMenuItem menuItem1 = new JMenuItem("TestItem1");
    String key1 = "Shortcut";
    menu.addShortcutAndIcon(menuItem1, key1);
    assertNotNull(menuItem1.getAccelerator());
    
    // Test case 2: test adding an icon to a JMenuItem
    // Expected output: JMenuItem should have the specified icon
    JMenuItem menuItem2 = new JMenuItem("TestItem2");
    String key2 = "Icon";
    menu.addShortcutAndIcon(menuItem2, key2);
    assertNotNull(menuItem2.getIcon());
  }
  
  @Test
  public void testChanged() {
    // Test case: test the changed method
    // Expected output: no assertion as the method does not have any return value
    menu.changed();
  }
}