import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class MainMenuTest {
    
    private MainMenu mainmenu;
    private InspirentoMediator mediator;
    
    @Before
    public void setUp() {
        mediator = mock(InspirentoMediator.class);
        mainmenu = new MainMenu(mediator);
    }

    @Test
    public void testCreateMenu_WhenGivenMenuExists_ReturnJMenuObjectWithProperTextAndMnemonic() {
        // Arrange
        String menu = "file";
        
        // Act
        JMenu result = mainmenu.createMenu(menu);
        
        // Assert
        assertEquals(JMenu.class, result.getClass());
        assertEquals("File", result.getText());
        assertEquals('F', result.getMnemonic());
    }
    
    @Test
    public void testCreateMenu_WhenGivenMenuDoesNotExist_ThrowMissingResourceException() {
        // Arrange
        String menu = "nonexistentMenu";
        
        // Assert
        assertThrows(MissingResourceException.class, () -> {
            // Act
            mainmenu.createMenu(menu);
        });
    }
    
    @Test
    public void testCreateMenu_WhenGivenMenuHasSubMenu_ReturnJMenuObjectWithSubMenuAdded() {
        // Arrange
        String menu = "file";
        
        // Act
        JMenu result = mainmenu.createMenu(menu);
        
        // Assert
        assertEquals(JMenu.class, result.getClass());
        assertEquals(2, result.getMenuComponentCount());
        assertEquals(JMenu.class, result.getMenuComponent(1).getClass());
    }
    
    @Test
    public void testAddShortcutAndIcon_WhenGivenItemWithShortcut_AddShortcutToItem() {
        // Arrange
        JMenuItem item = new JMenuItem();
        String key = "file";
        
        // Act
        mainmenu.addShortcutAndIcon(item, key);
        
        // Assert
        assertEquals(KeyStroke.class, item.getAccelerator().getClass());
    }
    
    @Test
    public void testAddShortcutAndIcon_WhenGivenItemWithIcon_AddIconToItem() {
        // Arrange
        JMenuItem item = new JMenuItem();
        String key = "file";
        
        // Act
        mainmenu.addShortcutAndIcon(item, key);
        
        // Assert
        assertNotNull(item.getIcon());
    }

    @Test
    public void testChanged_WhenCalled_ShouldNotDoAnything() {
        // Arrange

        // Act
        mainmenu.changed();

        // Assert
        // No exceptions should be thrown
    }

}