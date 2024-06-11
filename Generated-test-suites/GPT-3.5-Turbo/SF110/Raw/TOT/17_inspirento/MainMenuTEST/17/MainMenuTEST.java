import org.junit.*;
import static org.junit.Assert.*;

public class MainMenuTest {
    
    @Test
    public void testCreateMenu_typicalUseCase() {
        // Arrange
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        String menu = "File";
        
        // Act
        JMenuItem result = mainMenu.createMenu(menu);
        
        // Assert
        assertNotNull(result);
        assertEquals("File", result.getText());
        assertEquals('F', result.getMnemonic());
    }
    
    @Test
    public void testCreateMenu_nestedMenu() {
        // Arrange
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        String menu = "File>Open";
        
        // Act
        JMenuItem result = mainMenu.createMenu(menu);
        
        // Assert
        assertNotNull(result);
        assertEquals("Open", result.getText());
        assertEquals('O', result.getMnemonic());
    }
    
    @Test
    public void testCreateMenu_checkboxMenuItem_typicalUseCase() {
        // Arrange
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        String menu = "View#FullScreen";
        
        // Act
        JCheckBoxMenuItem result = (JCheckBoxMenuItem) mainMenu.createMenu(menu);
        
        // Assert
        assertNotNull(result);
        assertEquals("FullScreen", result.getText());
        assertEquals('F', result.getMnemonic());
        assertFalse(result.isSelected());
    }
    
    @Test
    public void testCreateMenu_checkboxMenuItem_invalidKey() {
        // Arrange
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        String menu = "View#InvalidMenuItem";
        
        // Act
        JCheckBoxMenuItem result = (JCheckBoxMenuItem) mainMenu.createMenu(menu);
        
        // Assert
        assertNull(result);
    }
    
    @Test
    public void testCreateMenu_menuItem_typicalUseCase() {
        // Arrange
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        String menu = "Edit>Copy";
        
        // Act
        JMenuItem result = mainMenu.createMenu(menu);
        
        // Assert
        assertNotNull(result);
        assertEquals("Copy", result.getText());
        assertEquals('C', result.getMnemonic());
    }
    
    @Test
    public void testCreateMenu_menuItem_invalidKey() {
        // Arrange
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        String menu = "Edit>InvalidMenuItem";
        
        // Act
        JMenuItem result = mainMenu.createMenu(menu);
        
        // Assert
        assertNull(result);
    }
    
    @Test
    public void testCreateMenu_separator() {
        // Arrange
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        String menu = "-";
        
        // Act
        JMenuItem result = mainMenu.createMenu(menu);
        
        // Assert
        assertNotNull(result);
        assertTrue(result instanceof JSeparator);
    }
    
    @Test
    public void testAddShortcutAndIcon_typicalUseCase_shortcutAndIconExist() {
        // Arrange
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        JMenuItem item = new JMenuItem();
        String key = "File>New";
        
        // Act
        mainMenu.addShortcutAndIcon(item, key);
        
        // Assert
        assertEquals(KeyStroke.getKeyStroke("ctrl+N"), item.getAccelerator());
        assertNotNull(item.getIcon());
    }
    
    @Test
    public void testAddShortcutAndIcon_typicalUseCase_shortcutExistsIconMissing() {
        // Arrange
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        JMenuItem item = new JMenuItem();
        String key = "File>InvalidIcon";
        
        // Act
        mainMenu.addShortcutAndIcon(item, key);
        
        // Assert
        assertEquals(KeyStroke.getKeyStroke("ctrl+I"), item.getAccelerator());
        assertNull(item.getIcon());
    }
    
    @Test
    public void testAddShortcutAndIcon_invalidKey() {
        // Arrange
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        JMenuItem item = new JMenuItem();
        String key = "File>InvalidShortcut";
        
        // Act
        mainMenu.addShortcutAndIcon(item, key);
        
        // Assert
        assertNull(item.getAccelerator());
        assertNull(item.getIcon());
    }

    // Other test cases for MainMenu methods can be added here
    
}