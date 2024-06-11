import org.junit.Assert.*;
import org.junit.Test;

public class MainMenuTest {
    
    @Test
    public void testCreateMenu() {
        MainMenu mainMenu = new MainMenu(null);
        
        // Test case 1
        JMenu result1 = mainMenu.createMenu("file");
        assertEquals("File", result1.getText());
        assertEquals(2, result1.getItemCount());
        assertEquals("New", result1.getItem(0).getText());
        assertEquals("Open", result1.getItem(1).getText());
        
        // Test case 2
        JMenu result2 = mainMenu.createMenu("edit");
        assertEquals("Edit", result2.getText());
        assertEquals(3, result2.getItemCount());
        assertEquals("Cut", result2.getItem(0).getText());
        assertEquals("Copy", result2.getItem(1).getText());
        assertEquals("Paste", result2.getItem(2).getText());
    }
    
    @Test
    public void testAddShortcutAndIcon() {
        MainMenu mainMenu = new MainMenu(null);
        
        JMenuItem item1 = new JMenuItem();
        mainMenu.addShortcutAndIcon(item1, "cut");
        assertEquals("Ctrl+X", item1.getAccelerator().toString());
        
        JMenuItem item2 = new JMenuItem();
        mainMenu.addShortcutAndIcon(item2, "copy");
        assertEquals("Ctrl+C", item2.getAccelerator().toString());
        
        JMenuItem item3 = new JMenuItem();
        mainMenu.addShortcutAndIcon(item3, "paste");
        assertEquals("Ctrl+V", item3.getAccelerator().toString());
    }
    
    @Test
    public void testChanged() {
        MainMenu mainMenu = new MainMenu(null);
        
        // Test case 1
        mainMenu.changed();
        // No specific change is expected, so no assertions needed
        
        // Test case 2
        mainMenu.changed();
        // No specific change is expected, so no assertions needed
    }
}