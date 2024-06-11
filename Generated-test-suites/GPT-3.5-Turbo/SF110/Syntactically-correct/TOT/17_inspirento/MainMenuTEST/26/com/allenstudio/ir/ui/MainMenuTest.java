import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;
import javax.swing.*;
import java.net.*;

public class MainMenuTest {
  
    @Test
    public void testCreateMenu_WhenMenuExists_ReturnsJMenuObject() {
        // Arrange
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        String menu = "File";
        
        // Act
        JMenu result = mainMenu.createMenu(menu);
        
        // Assert
        assertNotNull(result);
    }
    
    @Test
    public void testCreateMenu_WhenMenuDoesNotExist_ReturnsNull() {
        // Arrange
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        String menu = "InvalidMenu";
        
        // Act
        JMenu result = mainMenu.createMenu(menu);
        
        // Assert
        assertNull(result);
    }
    
    @Test
    public void testAddShortcutAndIcon_WhenShortcutExists_AddsAcceleratorToMenuItem() {
        // Arrange
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        JMenuItem item = new JMenuItem();
        String key = "New";
        String shortcut = "Ctrl+N";
        
        // Act
        mainMenu.addShortcutAndIcon(item, key);
        
        // Assert
        assertEquals(shortcut, item.getAccelerator().toString());
    }
    
    @Test
    public void testAddShortcutAndIcon_WhenShortcutDoesNotExist_DoesNotAddAcceleratorToMenuItem() {
        // Arrange
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        JMenuItem item = new JMenuItem();
        String key = "InvalidShortcut";
        
        // Act
        mainMenu.addShortcutAndIcon(item, key);
        
        // Assert
        assertNull(item.getAccelerator());
    }
    
    @Test
    public void testAddShortcutAndIcon_WhenIconExists_SetsIconToMenuItem() {
        // Arrange
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        JMenuItem item = new JMenuItem();
        String key = "New";
        String path = "/icons/new.png";
        
        // Act
        mainMenu.addShortcutAndIcon(item, key);
        
        // Assert
        URL expectedUrl = this.getClass().getResource(path);
        ImageIcon expectedIcon = new ImageIcon(expectedUrl);
        assertEquals(expectedIcon.getImage(), item.getIcon().getImage());
    }
    
    @Test
    public void testAddShortcutAndIcon_WhenIconDoesNotExist_DoesNotSetIconToMenuItem() {
        // Arrange
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        JMenuItem item = new JMenuItem();
        String key = "InvalidIcon";
        
        // Act
        mainMenu.addShortcutAndIcon(item, key);
        
        // Assert
        assertNull(item.getIcon());
    }
    
    @Test
    public void testChanged_DoesNotThrowException() {
        // Arrange
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        
        // Act & Assert
        assertDoesNotThrow(() -> mainMenu.changed());
    }
}