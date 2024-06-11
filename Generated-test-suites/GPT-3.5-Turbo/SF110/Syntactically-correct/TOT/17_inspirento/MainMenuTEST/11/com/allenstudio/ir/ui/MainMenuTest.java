import org.junit.*;
import static org.junit.Assert.*;

public class MainMenuTest {

    private MainMenu mainMenu;
    private InspirentoMediator mockMediator;
    
    @Before
    public void setUp() {
        mockMediator = new InspirentoMediator();
        mainMenu = new MainMenu(mockMediator);
    }
    
    @Test
    public void testCreateMenu_withValidMenuLabel_shouldCreateJMenuWithCorrectLabel() {
        String menu = "file";
        JMenu jMenu = mainMenu.createMenu(menu);
        
        assertEquals(UIResources.getString(menu + "Label"), jMenu.getText());
    }
    
    @Test
    public void testCreateMenu_withMenuLabelContainingAsterisk_shouldCreateJMenuWithLabelWithoutAsterisk() {
        String menu = "file";
        String tempLabel = UIResources.getString(menu + "Label");
        tempLabel = tempLabel.substring(0, tempLabel.length() - 2);
        char mnemonic = tempLabel.charAt(tempLabel.length() - 1);
        
        JMenu jMenu = mainMenu.createMenu(menu);
        
        assertEquals(tempLabel, jMenu.getText());
        assertEquals(mnemonic, jMenu.getMnemonic());
    }
    
    @Test
    public void testCreateMenu_withSubMenu_shouldCreateJMenuWithRecursiveCallToCreateMenu() {
        String menu = "file";
        JMenu jMenu = mainMenu.createMenu(menu);
        
        assertEquals(UIResources.getString(menu + "Label"), jMenu.getText());
        
        // Test if the submenu is created correctly by recursive call
        assertEquals(jMenu, jMenu.getMenuComponent(0));
        assertTrue(jMenu.getMenuComponent(1) instanceof JMenuItem);
        assertEquals(2, jMenu.getMenuComponentCount());
    }
    
    @Test
    public void testCreateMenu_withCheckBoxMenuItem_shouldCreateJCheckBoxMenuItemWithCorrectLabel() {
        String item = "#settings";
        String itemLabel = UIResources.getString(item.substring(1) + "Label");
        itemLabel = itemLabel.substring(0, itemLabel.length() - 2);
        char mnemonic = itemLabel.charAt(itemLabel.length() - 1);
        
        JMenu jMenu = mainMenu.createMenu("file");
        
        assertTrue(jMenu.getMenuComponent(3) instanceof JCheckBoxMenuItem);
        JCheckBoxMenuItem jMenuItem = (JCheckBoxMenuItem) jMenu.getMenuComponent(3);
        
        assertEquals(itemLabel, jMenuItem.getText());
        assertEquals(mnemonic, jMenuItem.getMnemonic());
    }
    
    @Test
    public void testCreateMenu_withMenuItem_shouldCreateJMenuItemWithCorrectLabel() {
        String item = "open";
        String itemLabel = UIResources.getString(item + "Label");
        
        JMenu jMenu = mainMenu.createMenu("file");
        
        assertTrue(jMenu.getMenuComponent(2) instanceof JMenuItem);
        JMenuItem jMenuItem = (JMenuItem) jMenu.getMenuComponent(2);
        
        assertEquals(itemLabel, jMenuItem.getText());
    }
    
    @Test
    public void testCreateMenu_withSeparatorItem_shouldCreateJMenuWithSeparator() {
        JMenu jMenu = mainMenu.createMenu("file");
        
        assertEquals(1, jMenu.getMenuComponentCount());
        
        assertTrue(jMenu.getMenuComponent(0) instanceof JPopupMenu.Separator);
    }
    
    @Test
    public void testAddShortcutAndIcon_withValidShortcut_shouldSetAcceleratorToItem() {
        String key = "open";
        String shortcut = UIResources.getString(key + "Shortcut");
        JMenuItem jMenuItem = new JMenuItem();
        
        mainMenu.addShortcutAndIcon(jMenuItem, key);
        
        assertNotNull(jMenuItem.getAccelerator());
        assertEquals(KeyStroke.getKeyStroke(shortcut), jMenuItem.getAccelerator());
    }
    
    @Test
    public void testAddShortcutAndIcon_withInvalidShortcut_shouldNotSetAcceleratorToItem() {
        String key = "invalidShortcut";
        JMenuItem jMenuItem = new JMenuItem();
        
        mainMenu.addShortcutAndIcon(jMenuItem, key);
        
        assertNull(jMenuItem.getAccelerator());
    }
    
    @Test
    public void testAddShortcutAndIcon_withValidIcon_shouldSetIconToItem() {
        String key = "open";
        String path = UIResources.getString(key + "Icon");
        URL url = this.getClass().getResource(path);
        JMenuItem jMenuItem = new JMenuItem();
        
        mainMenu.addShortcutAndIcon(jMenuItem, key);
        
        assertNotNull(jMenuItem.getIcon());
        assertEquals(new ImageIcon(url), jMenuItem.getIcon());
    }
    
    @Test
    public void testAddShortcutAndIcon_withInvalidIcon_shouldNotSetIconToItem() {
        String key = "invalidIcon";
        JMenuItem jMenuItem = new JMenuItem();
        
        mainMenu.addShortcutAndIcon(jMenuItem, key);
        
        assertNull(jMenuItem.getIcon());
    }
    
    @Test
    public void testChanged_withAnyCondition_shouldDoNothing() {
        mainMenu.changed();
        
        // No assertion as the method is expected to do nothing
    }
}