
package Menu;

import static org.junit.Assert.*;
import org.junit.Test;

public class MenuTest {
    
    @Test
    public void testInit() {
        Menu menu = new Menu();
        assertTrue(menu.init());
        
        Menu menuWithTitle = new Menu("Title", "TextFilePath", true, new GUI());
        assertFalse(menuWithTitle.init());
        
        Menu menuWithPaths = new Menu("Title", "picPath", "audioPath", "TextFilePath", false, new GUI());
        assertTrue(menuWithPaths.init());
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
        
        Menu menuWithTitle = new Menu("Title", "TextFilePath", true, new GUI());
        menuWithTitle.DVDMainMenu();
        
        Menu menuWithPaths = new Menu("Title", "picPath", "audioPath", "TextFilePath", false, new GUI());
        menuWithPaths.DVDMainMenu();
    }
    
    @Test
    public void testCreateXML() {
        Menu menu = new Menu();
        assertFalse(menu.createXML());
        
        Menu menuWithTitle = new Menu("Title", "TextFilePath", true, new GUI());
        assertFalse(menuWithTitle.createXML());
        
        Menu menuWithPaths = new Menu("Title", "picPath", "audioPath", "TextFilePath", false, new GUI());
        assertFalse(menuWithPaths.createXML());
    }
}
