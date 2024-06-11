import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class MainMenuTest {
    
    // Test MainMenu constructor
    @Test
    public void testMainMenuConstructor() {
        InspirentoMediator mediator = new InspirentoMediator();
        MainMenu mainMenu = new MainMenu(mediator);
        assertNotNull(mainMenu);
    }
    
    // Test createMenu method
    @Test
    public void testCreateMenu() {
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        assertNotNull(mainMenu.createMenu("File"));
        assertNotNull(mainMenu.createMenu("Edit"));
        assertNotNull(mainMenu.createMenu("Help"));
    }
    
    // Test addShortcutAndIcon method
    @Test
    public void testAddShortcutAndIcon() {
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        JMenuItem item = new JMenuItem();
        mainMenu.addShortcutAndIcon(item, "newFile");
        assertNotNull(item.getAccelerator());
    }
    
    // Test changed method
    @Test
    public void testChanged() {
        // No functionality to test
    }
    
    // Test exception handling when resource is missing
    @Test
    public void testMissingResourceExceptionHandling() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        JMenu menu = mainMenu.createMenu("InvalidMenuKey");
        
        assertTrue(outContent.toString().contains("Cannot load certain menu label(s)."));
        
        System.setOut(System.out);
    }
    
    // Test exception handling when icon file is lost
    @Test
    public void testIconFileLostHandling() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        MainMenu mainMenu = new MainMenu(new InspirentoMediator());
        JMenuItem item = new JMenuItem();
        mainMenu.addShortcutAndIcon(item, "invalidIconKey");
        
        assertTrue(outContent.toString().contains("Warning: icon file lost!"));
        
        System.setOut(System.out);
    }
}