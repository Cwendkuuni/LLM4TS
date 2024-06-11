import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

public class MenuTest {
    private Menu menu;

    @Before
    public void setUp() {
        menu = new Menu();
    }

    @Test
    public void testConstructor() {
        assertEquals(menu.dvd_menu, "dvd-menu");
        assertEquals(menu.titles.length, 50);
        assertEquals(menu.baseErr, "Menu Error - ");
    }

    @Test
    public void testInit() {
        assertFalse(menu.init());
    }

    @Test
    public void testDVDMainMenu() {
        menu.DVDMainMenu();
    }

    @Test
    public void testCreateXML() {
        assertFalse(menu.createXML());
    }
}