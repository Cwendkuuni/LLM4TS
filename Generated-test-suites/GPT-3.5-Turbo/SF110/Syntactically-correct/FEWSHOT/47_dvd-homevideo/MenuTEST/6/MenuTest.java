// Test cases for the Menu class

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import java.io.File;

public class MenuTest {

    private Menu menu;

    @Before
    public void setUp() {
        menu = new Menu();
    }

    @Test
    public void testInit() {
        assertTrue(menu.init());
    }

    @Test
    public void testRun() {
        menu.run();
        assertNotNull(menu);
    }

    @Test
    public void testDVDMainMenu() {
        menu.DVDMainMenu();
        assertNotNull(menu);
    }

    @Test
    public void testCreateXML() {
        assertFalse(menu.createXML());
    }

    @Test
    public void testMenuConstructor() {
        assertNotNull(menu);
    }

    @Test
    public void testMenuConstructorWithParameters() {
        Menu menu = new Menu("Title", "TextFilePath", true, new GUI());
        assertNotNull(menu);
    }

    @Test
    public void testMenuConstructorWithAllParameters() {
        Menu menu = new Menu("Title", "PicPath", "AudioPath", "TextFilePath", true, new GUI());
        assertNotNull(menu);
    }
}