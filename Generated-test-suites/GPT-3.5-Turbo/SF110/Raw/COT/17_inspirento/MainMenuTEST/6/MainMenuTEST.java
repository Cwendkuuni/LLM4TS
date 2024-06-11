import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class MainMenuTest {
    
    private MainMenu mainMenu;
    
    @Before
    public void setUp() {
        mainMenu = new MainMenu(null);
    }
    
    @Test
    public void testCreateMenu() {
        String menu = "File";
        JMenu jMenu = mainMenu.createMenu(menu);
        Assert.assertEquals(menu, jMenu.getText());
        
        String menuWithMnemonic = "Edit";
        JMenu jMenuWithMnemonic = mainMenu.createMenu(menuWithMnemonic);
        Assert.assertEquals(menuWithMnemonic.substring(0, menuWithMnemonic.length() - 2), 
            jMenuWithMnemonic.getText());
        Assert.assertEquals(menuWithMnemonic.charAt(menuWithMnemonic.length() - 1), 
            jMenuWithMnemonic.getMnemonic());
    }
    
    @Test
    public void testAddShortcutAndIcon() {
        JMenuItem jMenuItemWithShortcut = new JMenuItem();
        String keyWithShortcut = "FileMenu";
        mainMenu.addShortcutAndIcon(jMenuItemWithShortcut, keyWithShortcut);
        Assert.assertNotNull(jMenuItemWithShortcut.getAccelerator());
        Assert.assertNull(jMenuItemWithShortcut.getIcon());
        
        JMenuItem jMenuItemWithIcon = new JMenuItem();
        String keyWithIcon = "ViewMenu";
        mainMenu.addShortcutAndIcon(jMenuItemWithIcon, keyWithIcon);
        Assert.assertNull(jMenuItemWithIcon.getAccelerator());
        Assert.assertNotNull(jMenuItemWithIcon.getIcon());
    }
    
    @Test
    public void testChanged() {
        mainMenu.changed();
        // No assertions, just test for code coverage
    }
    
    @Test(expected = MissingResourceException.class)
    public void testCreateMenuMissingLabel() {
        String missingMenu = "MissingMenu";
        JMenu jMenu = mainMenu.createMenu(missingMenu);
    }
    
    @Test(expected = MissingResourceException.class)
    public void testCreateMenuItemMissingLabel() {
        String missingItem = "MissingItem";
        JMenuItem jMenuItem = mainMenu.createMenuItem(missingItem);
    }
    
    @Test(expected = MissingResourceException.class)
    public void testAddShortcutAndIconMissingShortcut() {
        JMenuItem jMenuItem = new JMenuItem();
        String missingShortcut = "MissingShortcut";
        mainMenu.addShortcutAndIcon(jMenuItem, missingShortcut);
    }
    
    @Test(expected = MissingResourceException.class)
    public void testAddShortcutAndIconMissingIcon() {
        JMenuItem jMenuItem = new JMenuItem();
        String missingIcon = "MissingIcon";
        mainMenu.addShortcutAndIcon(jMenuItem, missingIcon);
    }
}