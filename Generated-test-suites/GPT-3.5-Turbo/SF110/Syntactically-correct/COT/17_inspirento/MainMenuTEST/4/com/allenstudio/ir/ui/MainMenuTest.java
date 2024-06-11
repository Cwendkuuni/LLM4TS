
import org.junit.Test;
import javax.swing.*;
import java.util.*;
import java.net.*;

public class MainMenuTest {
    @Test
    public void testCreateMenu() {
        MainMenu mainMenu = new MainMenu(null);
        
        // Test case 1: Test creation of JMenu with label having length greater than 2 and ending with '*'
        String tempLabel1 = "TestMenu*";
        JMenu jMenu1 = mainMenu.createMenu(tempLabel1);
        assertEquals(tempLabel1.substring(0, tempLabel1.length() - 2), jMenu1.getText());
        assertEquals(tempLabel1.charAt(tempLabel1.length() - 1), jMenu1.getMnemonic());
        
        
        // Test case 2: Test creation of JMenu with label not ending with '*'
        String tempLabel2 = "TestMenu";
        JMenu jMenu2 = mainMenu.createMenu(tempLabel2);
        assertEquals(tempLabel2, jMenu2.getText());
        
        
        // Test case 3: Test creation of JCheckBoxMenuItem with label having length greater than 2 and ending with '*'
        String itemLabel1 = "TestItem*";
        String item1 = "#" + itemLabel1;
        JCheckBoxMenuItem jMenuItem1 = mainMenu.createCheckBoxMenuItem(item1);
        assertEquals(itemLabel1.substring(0, itemLabel1.length() - 2), jMenuItem1.getText());
        assertEquals(itemLabel1.charAt(itemLabel1.length() - 1), jMenuItem1.getMnemonic());
        
        
        // Test case 4: Test creation of JCheckBoxMenuItem with label not ending with '*'
        String itemLabel2 = "TestItem";
        String item2 = "#" + itemLabel2;
        JCheckBoxMenuItem jMenuItem2 = mainMenu.createCheckBoxMenuItem(item2);
        assertEquals(itemLabel2, jMenuItem2.getText());
        
        
        // Test case 5: Test creation of JMenuItem with label having length greater than 2 and ending with '*'
        String itemLabel3 = "TestItem*";
        JMenuItem jMenuItem3 = mainMenu.createMenuItem(itemLabel3);
        assertEquals(itemLabel3.substring(0, itemLabel3.length() - 2), jMenuItem3.getText());
        assertEquals(itemLabel3.charAt(itemLabel3.length() - 1), jMenuItem3.getMnemonic());
        
        
        // Test case 6: Test creation of JMenuItem with label not ending with '*'
        String itemLabel4 = "TestItem";
        JMenuItem jMenuItem4 = mainMenu.createMenuItem(itemLabel4);
        assertEquals(itemLabel4, jMenuItem4.getText());
    }
    
    @Test
    public void testAddShortcutAndIcon() {
        MainMenu mainMenu = new MainMenu(null);
        JMenuItem jMenuItem = new JMenuItem();
        
        // Test case 1: Testing adding shortcut for JMenuItem
        String key1 = "TestKey";
        String shortcut1 = "ctrl T";
        UIResources.setString(key1 + "Shortcut", shortcut1);
        mainMenu.addShortcutAndIcon(jMenuItem, key1);
        assertEquals(KeyStroke.getKeyStroke(shortcut1), jMenuItem.getAccelerator());
        
        
        // Test case 2: Testing adding icon for JMenuItem
        String key2 = "TestKey";
        String iconPath2 = "test_icon_path.png";
        UIResources.setString(key2 + "Icon", iconPath2);
        mainMenu.addShortcutAndIcon(jMenuItem, key2);
        assertNotNull(jMenuItem.getIcon());
    }
}
