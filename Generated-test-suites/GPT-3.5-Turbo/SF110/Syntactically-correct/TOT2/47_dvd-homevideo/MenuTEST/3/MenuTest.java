import static org.junit.Assert.*;
import org.junit.Test;

public class MenuTest {

    @Test
    public void testMenuConstructor1() {
        Menu menu = new Menu();
        assertNotNull(menu);  
    }
    
    @Test
    public void testMenuConstructor2() {
        Menu menu = new Menu("Test", "textFile.txt", true, new GUI());
        assertNotNull(menu);  
    }
    
    @Test
    public void testMenuConstructor3() {
        Menu menu = new Menu("Test", "pic.jpg", "audio.mp3", "textFile.txt", true, new GUI());
        assertNotNull(menu);  
    }

    @Test
    public void testInit() {
        Menu menu = new Menu();
        assertFalse(menu.init());
    }

    @Test
    public void testRun() {
        Menu menu = new Menu();
        menu.run();
        // Test the run method indirectly through the DVDMainMenu method
    }

    @Test
    public void testDVDMainMenu() {
        Menu menu = new Menu("Test", "pic.jpg", "audio.mp3", "textFile.txt", true, new GUI());
        menu.DVDMainMenu();
        // Test the DVDMainMenu method indirectly through the initialization of a Menu object
    }

    @Test
    public void testCreateXML() {
        Menu menu = new Menu("Test", "pic.jpg", "audio.mp3", "textFile.txt", true, new GUI());
        assertTrue(menu.createXML());
    }

}