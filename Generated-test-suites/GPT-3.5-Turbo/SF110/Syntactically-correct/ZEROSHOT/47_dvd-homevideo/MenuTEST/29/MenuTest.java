import org.junit.Test;

import static org.junit.Assert.*;

public class MenuTest {

    @Test
    public void testInit() {
        Menu menu = new Menu();
        assertFalse(menu.init());
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