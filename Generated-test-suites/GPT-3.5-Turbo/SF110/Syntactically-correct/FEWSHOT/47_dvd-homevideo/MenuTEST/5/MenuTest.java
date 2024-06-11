import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class MenuTest {
	
    @Test
    public void testInit() {
        Menu menu = new Menu();
        assertEquals(true, menu.init());
        
        Menu menu1 = new Menu("Title", "TextFilePath", true, new GUI());
        assertEquals(true, menu1.init());
        
        Menu menu2 = new Menu("Title", "picPath", "audioPath", "TextFilePath", true, new GUI());
        assertEquals(true, menu2.init());
    }
    
    @Test
    public void testDVDMainMenu() {
        Menu menu = new Menu();
        menu.DVDMainMenu();
        
        Menu menu1 = new Menu("Title", "TextFilePath", true, new GUI());
        menu1.DVDMainMenu();
        
        Menu menu2 = new Menu("Title", "picPath", "audioPath", "TextFilePath", true, new GUI());
        menu2.DVDMainMenu();
    }
    
    @Test
    public void testCreateXML() {
        Menu menu = new Menu();
        assertEquals(false, menu.createXML());

        Menu menu1 = new Menu("Title", "TextFilePath", true, new GUI());
        assertEquals(false, menu1.createXML());
        
        Menu menu2 = new Menu("Title", "picPath", "audioPath", "TextFilePath", true, new GUI());
        assertEquals(false, menu2.createXML());
    }
}