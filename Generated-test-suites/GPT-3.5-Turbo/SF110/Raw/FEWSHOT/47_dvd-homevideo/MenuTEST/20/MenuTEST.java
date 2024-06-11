import static org.junit.Assert.*;
import org.junit.Test;

public class MenuTest {
    
    @Test
    public void testInit() {
        Menu menu = new Menu();
        assertTrue(menu.init());
        
        Menu menuWithTitle = new Menu("Title", "", true, new GUI());
        assertTrue(menuWithTitle.init());
        
        Menu menuWithAllParams = new Menu("Title", "picPath", "audioPath", "TextFilePath", false, new GUI());
        assertTrue(menuWithAllParams.init());
    }
    
    @Test
    public void testRun() {
        Menu menu = new Menu();
        menu.run();
        // No assertion can be made as run method doesn't return anything
    }
    
    @Test
    public void testDVDMainMenu() {
        Menu menu = new Menu();
        menu.DVDMainMenu();
        // No assertion can be made, as this method doesn't return anything
    }
    
    @Test
    public void testCreateXML() {
        Menu menu = new Menu();
        assertFalse(menu.createXML());
    }
    
}