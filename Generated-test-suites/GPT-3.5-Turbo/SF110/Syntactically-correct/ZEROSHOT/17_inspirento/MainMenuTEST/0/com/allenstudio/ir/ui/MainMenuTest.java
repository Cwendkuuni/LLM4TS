import org.junit.Test;
import static org.junit.Assert.*;

public class MainMenuTest {

@Test
public void testCreateMenu() {
    MainMenu menu = new MainMenu(null);
    JMenu result = menu.createMenu("menu");
    
    assertNotNull(result);
    assertEquals("Menu", result.getText());
}

@Test
public void testAddShortcutAndIcon() {
    MainMenu menu = new MainMenu(null);
    JMenuItem item = new JMenuItem();
    
    menu.addShortcutAndIcon(item, "key");
    
    assertNull(item.getAccelerator());
    
    item = new JMenuItem();
    menu.addShortcutAndIcon(item, "keyShortcut");
    
    assertNotNull(item.getAccelerator());
    
    item = new JMenuItem();
    menu.addShortcutAndIcon(item, "keyIcon");
    
    assertNull(item.getIcon());
}

}