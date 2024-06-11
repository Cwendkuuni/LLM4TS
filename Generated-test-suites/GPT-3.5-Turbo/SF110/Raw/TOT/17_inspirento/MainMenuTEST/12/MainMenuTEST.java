import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MainMenuTest {
    
    private MainMenu mainMenu;
    
    @Before
    public void setUp() {
        mainMenu = new MainMenu(mediator);
    }
    
    @Test
    public void testCreateMenu_WhenMenuWithNoStarSuffix_ExpectMenuWithGivenLabel() {
        JMenu menu = mainMenu.createMenu("File");
        assertNotNull(menu);
        assertEquals("File", menu.getText());
    }
    
    @Test
    public void testCreateMenu_WhenMenuWithStarSuffix_ExpectMenuWithLabelWithoutSuffixAndMnemonic() {
        JMenu menu = mainMenu.createMenu("Edit");
        assertNotNull(menu);
        assertEquals("Edit", menu.getText());
        assertEquals('E', menu.getMnemonic());
    }
    
    @Test
    public void testCreateMenu_WhenMenuWithSubMenus_ExpectMenuWithNestedMenus() {
        JMenu menu = mainMenu.createMenu("View");
        assertNotNull(menu);
        
        JMenu subMenu1 = (JMenu) menu.getMenuComponent(0);
        assertNotNull(subMenu1);
        assertEquals("Show/Hide", subMenu1.getText());
        
        JMenuItem subMenuItem = (JMenuItem) subMenu1.getMenuComponent(0);
        assertNotNull(subMenuItem);
        assertEquals("Toolbar", subMenuItem.getText());
        // Test if toolbar menuItem has shortcut and icon
        
        JMenu subMenu2 = (JMenu) menu.getMenuComponent(1);
        assertNotNull(subMenu2);
        assertEquals("Zoom", subMenu2.getText());
    }
    
    @Test
    public void testCreateMenu_WhenMenuItemPrefixedWithHash_ExpectJCheckBoxMenuItemWithLabelWithoutSuffixAndMnemonic() {
        JMenu menu = mainMenu.createMenu("Edit");
        assertNotNull(menu);
        
        JCheckBoxMenuItem checkBoxMenuItem = (JCheckBoxMenuItem) menu.getMenuComponent(3);
        assertNotNull(checkBoxMenuItem);
        assertEquals("Auto Indent", checkBoxMenuItem.getText());
        assertEquals('A', checkBoxMenuItem.getMnemonic());
        // Test if checkBoxMenuItem has shortcut and icon
    }
    
    @Test
    public void testCreateMenu_WhenMenuItemWithoutHashAndArrowSuffix_ExpectJMenuItemWithLabelWithoutSuffixAndMnemonic() {
        JMenu menu = mainMenu.createMenu("Edit");
        assertNotNull(menu);
        
        JMenuItem menuItem1 = (JMenuItem) menu.getMenuComponent(0);
        assertNotNull(menuItem1);
        assertEquals("Undo", menuItem1.getText());
        assertEquals('U', menuItem1.getMnemonic());
        // Test if menuItem1 has shortcut and icon
        
        JMenuItem menuItem2 = (JMenuItem) menu.getMenuComponent(1);
        assertNotNull(menuItem2);
        assertEquals("Redo", menuItem2.getText());
        assertEquals('R', menuItem2.getMnemonic());
        // Test if menuItem2 has shortcut and icon
    }
    
    @Test
    public void testAddShortcutAndIcon_WhenShortcutAndIconExist_ExpectMenuItemWithShortcutAndIcon() {
        JMenuItem menuItem = new JMenuItem();
        mainMenu.addShortcutAndIcon(menuItem, "menu.item");
        
        assertNotNull(menuItem.getAccelerator());
        assertNotNull(menuItem.getIcon());
    }
    
    @Test
    public void testAddShortcutAndIcon_WhenShortcutDoesNotExist_ExpectMenuItemWithoutShortcut() {
        JMenuItem menuItem = new JMenuItem();
        mainMenu.addShortcutAndIcon(menuItem, "menu.item.noShortcut");
        
        assertNull(menuItem.getAccelerator());
        assertNotNull(menuItem.getIcon());
    }
    
    @Test
    public void testAddShortcutAndIcon_WhenIconDoesNotExist_ExpectMenuItemWithoutIconAndDisplayWarningMessage() {
        JMenuItem menuItem = new JMenuItem();
        mainMenu.addShortcutAndIcon(menuItem, "menu.item.noIcon");
        
        assertNotNull(menuItem.getAccelerator());
        assertNull(menuItem.getIcon());
        // Test if warning message is displayed
    }

}