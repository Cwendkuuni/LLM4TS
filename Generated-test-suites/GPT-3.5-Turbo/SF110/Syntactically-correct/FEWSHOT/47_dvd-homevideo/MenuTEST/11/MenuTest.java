import static org.junit.Assert.*;
import org.junit.Test;

public class MenuTest {
    
    @Test
    public void testInit() {
        Menu menu = new Menu();
        assertTrue(menu.init());
        
        Menu menu2 = new Menu("Title", "TextFilePath", true, new GUI());
        assertTrue(menu2.init());
        
        Menu menu3 = new Menu("Title", "picPath", "audioPath", "TextFilePath", true, new GUI());
        assertTrue(menu3.init());
    }
    
    @Test
    public void testRun() {
        Menu menu = new Menu();
        menu.run();
    }
    
    @Test
    public void testDVDMainMenu() {
        Menu menu = new Menu();
        menu.DVDMainMenu();
    }
    
    @Test
    public void testCreateXML() {
        Menu menu = new Menu();
        assertFalse(menu.createXML());
    }
}