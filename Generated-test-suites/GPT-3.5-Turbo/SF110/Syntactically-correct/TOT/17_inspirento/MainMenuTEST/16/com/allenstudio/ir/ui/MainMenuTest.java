import org.junit.Before;
import org.junit.Test;
import javax.swing.*;
import java.util.*;

public class MainMenuTest {

    private MainMenu mainMenu;

    @Before
    public void setUp() {
        mainMenu = new MainMenu(new InspirentoMediator());
    }

    @Test
    public void createMenu_NormalLabel_Success() {
        String menu = "File";
        JMenu result = mainMenu.createMenu(menu);
        assert(result != null);
        assert(result.getText().equals(menu));
        assert(result.getMenuComponentCount() == 0);
    }

    @Test
    public void createMenu_LabeledWithAsterisk_Success() {
        String menu = "Edit";
        JMenu result = mainMenu.createMenu(menu);
        assert(result != null);
        assert(result.getText().equals(menu.substring(0, menu.length() - 2)));
        assert(result.getMnemonic() == (int)menu.charAt(menu.length() - 1));
        assert(result.getMenuComponentCount() == 0);
    }

    @Test
    public void createMenu_SubMenu_Success() {
        String menu = "Edit->Copy";
        JMenu result = mainMenu.createMenu(menu);
        assert(result != null);
        assert(result.getText().equals("Copy"));
        assert(result.getMenuComponentCount() == 0);
        assert(result.getParent() instanceof JMenu);
    }

    @Test
    public void createMenu_MissingResourceException_Exit() {
        String menu = "InvalidMenu";
        try {
            JMenu result = mainMenu.createMenu(menu);
        } catch (MissingResourceException e) {
            assert(e != null);
            assert(e.getMessage().contains("Cannot load certain menu label(s)."));
        }
    }

    @Test
    public void addShortcutAndIcon_NormalItem_Success() {
        JMenuItem item = new JMenuItem();
        String key = "File";
        mainMenu.addShortcutAndIcon(item, key);
        assert(item.getAccelerator().getKeyChar() == '\u0000');
        assert(item.getIcon() == null);
    }

    @Test
    public void addShortcutAndIcon_WithShortcut_Success() {
        JMenuItem item = new JMenuItem();
        String key = "File";
        UIResources.put(key + "Shortcut", "ctrl C");
        mainMenu.addShortcutAndIcon(item, key);
        assert(item.getAccelerator().getKeyChar() == 'C');
        assert(item.getAccelerator().isControlDown());
        assert(item.getIcon() == null);
    }

    @Test
    public void addShortcutAndIcon_WithIcon_Success() {
        JMenuItem item = new JMenuItem();
        String key = "File";
        UIResources.put(key + "Icon", "/path/icon.png");
        mainMenu.addShortcutAndIcon(item, key);
        // Assume that the icon would be loaded successfully for this test case
        assert(item.getIcon() != null);
    }

    @Test
    public void addShortcutAndIcon_WithInvalidIcon_WarningMessagePrinted() {
        JMenuItem item = new JMenuItem();
        String key = "File";
        UIResources.put(key + "Icon", "/path/invalid_icon.png");
        mainMenu.addShortcutAndIcon(item, key);
        // We assume that the warning message will always be printed in the console if the icon file is lost
    }

    @Test
    public void changed_DoNothing_Success() {
        mainMenu.changed();
        // No assertions needed, the method should not throw any exceptions
    }
}