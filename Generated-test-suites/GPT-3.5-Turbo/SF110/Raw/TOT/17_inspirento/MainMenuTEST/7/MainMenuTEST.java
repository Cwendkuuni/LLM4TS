import org.junit.*;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class MainMenuTest {

  private MainMenu menu;

  @Before
  public void setup() {
    menu = new MainMenu(null);
  }

  @Test
  public void testCreateMenu_withValidMenu_shouldReturnJMenuObject() {
    String menuName = "File";
    JMenu result = menu.createMenu(menuName);

    assertNotNull(result);
    assertEquals(menuName, result.getText());
  }

  @Test
  public void testCreateMenu_withMenuHavingStarredLabel_shouldReturnJMenuObjectWithCorrectLabelAndMnemonic() {
    String menuName = "File";
    String menuLabel = "File*";
    JMenu expected = new JMenu("File");

    expected.setMnemonic('e');

    JMenu result = menu.createMenu(menuLabel);

    assertNotNull(result);
    assertEquals(expected.getText(), result.getText());
    assertEquals(expected.getMnemonic(), result.getMnemonic());
  }

  @Test
  public void testCreateMenu_withMenuAndItems_shouldReturnJMenuObjectWithCorrectStructure() {
    String menuName = "File";
    String menuItem1 = "New";
    String menuItem2 = "Open";
    String menuItem3 = "-";
    String menuItem4 = "Save";
    String menuItem5 = "Save As...";

    JMenu result = menu.createMenu(menuName);

    assertNotNull(result);

    // Test for New menu item
    assertTrue(result.getItem(0) instanceof JMenuItem);
    assertEquals(menuItem1, result.getItem(0).getText());

    // Test for Open menu item
    assertTrue(result.getItem(1) instanceof JMenuItem);
    assertEquals(menuItem2, result.getItem(1).getText());

    // Test for Separator
    assertTrue(result.getItem(2) instanceof JSeparator);

    // Test for Save menu item
    assertTrue(result.getItem(3) instanceof JMenuItem);
    assertEquals(menuItem4, result.getItem(3).getText());

    // Test for Save As... menu item
    assertTrue(result.getItem(4) instanceof JMenuItem);
    assertEquals(menuItem5, result.getItem(4).getText());
  }

  @Test
  public void testCreateMenu_withNestedMenu_shouldReturnJMenuObjectWithCorrectStructure() {
    String menuName = "File";
    String nestedMenuName = "New->Java Class";
    String nestedMenuLabel = "Java Class";

    JMenu result = menu.createMenu(menuName);

    assertNotNull(result);

    // Test for nested menu
    assertTrue(result.getItem(0) instanceof JMenu);
    JMenu nestedMenu = (JMenu) result.getItem(0);
    assertEquals(nestedMenuLabel, nestedMenu.getText());

    // Test for nested menu item
    assertTrue(nestedMenu.getItem(0) instanceof JMenuItem);
    JMenuItem nestedMenuItem = (JMenuItem) nestedMenu.getItem(0);
    assertEquals(nestedMenuName, nestedMenuItem.getText());
  }

  @Test
  public void testCreateMenu_withCheckedMenuItem_shouldReturnJMenuObjectWithCorrectStructure() {
    String menuName = "File";
    String checkedMenuItem = "#Edit->Copy";
    String checkedMenuItemLabel = "Copy";

    JMenu result = menu.createMenu(menuName);

    assertNotNull(result);

    // Test for checked menu item
    assertTrue(result.getItem(0) instanceof JCheckBoxMenuItem);
    JCheckBoxMenuItem checkedItem = (JCheckBoxMenuItem) result.getItem(0);
    assertEquals(checkedMenuItemLabel, checkedItem.getText());
  }

  @Test
  public void testAddShortcutAndIcon_withExistingShortcutAndIcon_shouldSetItemWithCorrectShortcutAndIcon() {
    JMenuItem item = new JMenuItem();
    String key = "File";

    menu.addShortcutAndIcon(item, key);

    assertNotNull(item.getAccelerator());
    assertNotNull(item.getIcon());
  }

  @Test
  public void testAddShortcutAndIcon_withoutShortcut_shouldSetItemWithoutShortcut() {
    JMenuItem item = new JMenuItem();
    String key = "View";

    menu.addShortcutAndIcon(item, key);

    assertNull(item.getAccelerator());
  }

  @Test
  public void testAddShortcutAndIcon_withoutIcon_shouldNotSetItemIcon() {
    JMenuItem item = new JMenuItem();
    String key = "Help";

    menu.addShortcutAndIcon(item, key);

    assertNull(item.getIcon());
  }

}