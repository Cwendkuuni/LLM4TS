import org.junit.*;
import static org.junit.Assert.*;

public class MenuTest {

    @Test
    public void testConstructors() {
        Menu menu1 = new Menu();
        assertEquals("dvd-menu", menu1.dvd_menu);
        assertArrayEquals(new String[50], menu1.titles);
        assertEquals("Menu Error - ", menu1.baseErr);

        Menu menu2 = new Menu("Title", "TextFilePath", true, new GUI());
        assertEquals("dvd-menu", menu2.dvd_menu);
        assertArrayEquals(new String[50], menu2.titles);
        assertEquals("Menu Error - ", menu2.baseErr);
        assertEquals("Title", menu2.strTitle);
        assertEquals("TextFilePath", menu2.strTextFilePath);
        assertTrue(menu2.pal_menu);
        assertNotNull(menu2.m_GUI);

        Menu menu3 = new Menu("Title", "PicPath", "AudioPath", "TextFilePath", false, new GUI());
        assertEquals("dvd-menu", menu3.dvd_menu);
        assertArrayEquals(new String[50], menu3.titles);
        assertEquals("Menu Error - ", menu3.baseErr);
        assertEquals("Title", menu3.strTitle);
        assertEquals("PicPath", menu3.strPicPath);
        assertEquals("AudioPath", menu3.strAudioPath);
        assertEquals("TextFilePath", menu3.strTextFilePath);
        assertFalse(menu3.pal_menu);
        assertNotNull(menu3.m_GUI);
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