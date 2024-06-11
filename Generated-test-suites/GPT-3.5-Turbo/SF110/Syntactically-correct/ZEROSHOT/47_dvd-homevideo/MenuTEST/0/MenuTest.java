import org.junit.*;
import static org.junit.Assert.*;

public class MenuTest {

    @Test
    public void testInit() {
        Menu menu = new Menu();
        boolean result = menu.init();
        assertFalse(result);
    }

    @Test
    public void testRun(){
        Menu menu = new Menu();
        menu.run();
        // test the functionality of run method 
    }

    @Test
    public void testDVDMainMenu() {
        Menu menu = new Menu();
        menu.DVDMainMenu();
        // test the functionality of DVDMainMenu method 
    }

    @Test
    public void testCreateXML() {
        Menu menu = new Menu();
        boolean result = menu.createXML();
        assertFalse(result);
    }
}