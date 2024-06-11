import static org.junit.Assert.*;
import org.junit.Test;

public class MenuTest {

    Menu menu1 = new Menu();
    Menu menu2 = new Menu("Title", "TextFilePath", true, new GUI());
    Menu menu3 = new Menu("Title", "PicPath", "AudioPath", "TextFilePath", true, new GUI());
    
    @Test
    public void testInit() {
        assertTrue(menu1.init());
        assertTrue(menu2.init());
        assertTrue(menu3.init());
    }
    
    @Test
    public void testRun() {
        menu1.run();
        menu2.run();
        menu3.run();
    }
    
    @Test
    public void testDVDMainMenu() {
        menu1.DVDMainMenu();
        menu2.DVDMainMenu();
        menu3.DVDMainMenu();
    }
    
    @Test
    public void testCreateXML() {
        assertTrue(menu1.createXML());
        assertTrue(menu2.createXML());
        assertTrue(menu3.createXML());
    }
}