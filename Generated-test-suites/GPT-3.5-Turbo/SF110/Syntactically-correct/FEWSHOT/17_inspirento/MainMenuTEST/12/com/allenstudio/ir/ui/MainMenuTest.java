import static org.junit.Assert.*;
import org.junit.Test;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.KeyStroke;
import javax.swing.ImageIcon;
import java.util.MissingResourceException;
import java.net.URL;
import com.allenstudio.ir.util.InspirentoUtilities;
import com.allenstudio.ir.util.UIResources;
import com.allenstudio.ir.core.InspirentoMediator;

public class MainMenuTest {
    @Test
    public void testCreateMenu() {
        MainMenu menu = new MainMenu(new InspirentoMediator());
        
        // Test when menu is not present
        assertNull(menu.createMenu("unexistingMenu"));
        
        // Test when menu label is marked as sub-menu
        JMenu subMenu = menu.createMenu("subMenu->");
        assertEquals("", subMenu.getText());
        assertEquals(0, subMenu.getMenuComponentCount());
        
        // Test when menu label does not have sub-menu
        JMenu normalMenu = menu.createMenu("normalMenu");
        assertEquals("normalMenu", normalMenu.getText());
        assertEquals(0, normalMenu.getMenuComponentCount());
        
        // Test when menu item label is marked as check box
        JMenu checkBoxMenu = menu.createMenu("#checkBox");
        assertEquals("", checkBoxMenu.getText());
        assertEquals(1, checkBoxMenu.getMenuComponentCount());
        assertTrue(checkBoxMenu.getMenuComponent(0) instanceof JCheckBoxMenuItem);
        
        // Test when menu item label is not marked as check box
        JMenu normalMenuItem = menu.createMenu("normalMenuItem");
        assertEquals("", normalMenuItem.getText());
        assertEquals(1, normalMenuItem.getMenuComponentCount());
        assertTrue(normalMenuItem.getMenuComponent(0) instanceof JMenuItem);
    }
    
    @Test
    public void testAddShortcutAndIcon() {
        MainMenu menu = new MainMenu(new InspirentoMediator());
        JMenu testMenu = new JMenu();
        JMenuItem menuItem = new JMenuItem();
        JCheckBoxMenuItem checkBoxItem = new JCheckBoxMenuItem();
        
        // Test when shortcut is present
        menu.addShortcutAndIcon(testMenu, "shortcutMenu");
        assertNotNull(testMenu.getAccelerator());
        
        menu.addShortcutAndIcon(menuItem, "shortcutMenuItem");
        assertNotNull(menuItem.getAccelerator());
        
        menu.addShortcutAndIcon(checkBoxItem, "shortcutCheckBoxItem");
        assertNotNull(checkBoxItem.getAccelerator());
        
        // Test when shortcut is not present
        menu.addShortcutAndIcon(testMenu, "noShortcutMenu");
        assertNull(testMenu.getAccelerator());
        
        menu.addShortcutAndIcon(menuItem, "noShortcutMenuItem");
        assertNull(menuItem.getAccelerator());
        
        menu.addShortcutAndIcon(checkBoxItem, "noShortcutCheckBoxItem");
        assertNull(checkBoxItem.getAccelerator());
        
        // Test when icon is present
        menu.addShortcutAndIcon(testMenu, "iconMenu");
        assertNotNull(testMenu.getIcon());
        
        menu.addShortcutAndIcon(menuItem, "iconMenuItem");
        assertNotNull(menuItem.getIcon());
        
        menu.addShortcutAndIcon(checkBoxItem, "iconCheckBoxItem");
        assertNotNull(checkBoxItem.getIcon());
        
        // Test when icon is not present
        menu.addShortcutAndIcon(testMenu, "noIconMenu");
        assertNull(testMenu.getIcon());
        
        menu.addShortcutAndIcon(menuItem, "noIconMenuItem");
        assertNull(menuItem.getIcon());
        
        menu.addShortcutAndIcon(checkBoxItem, "noIconCheckBoxItem");
        assertNull(checkBoxItem.getIcon());
    }
    
    @Test
    public void testChanged() {
        MainMenu menu = new MainMenu(new InspirentoMediator());
        // No assertions needed, method does not return anything and does not modify any state
        menu.changed();
    }
}