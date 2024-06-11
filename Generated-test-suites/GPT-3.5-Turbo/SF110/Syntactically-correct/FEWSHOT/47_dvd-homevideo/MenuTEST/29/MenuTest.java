import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

public class MenuTest {
    @Test
    public void testMenuConstructor() {
        Menu menu = new Menu();
        assertEquals("", menu.strTitle);
        assertNull(menu.strPicPath);
        assertNull(menu.strAudioPath);
        assertNull(menu.strTextFilePath);
        assertEquals("dvd-menu", menu.dvd_menu);
        assertEquals(50, menu.titles.length);
        assertEquals("Menu Error - ", menu.baseErr);
        
        Menu menuWithParameters = new Menu("Title", "textfile.txt", true, new GUI());
        assertEquals("Title", menuWithParameters.strTitle);
        assertNull(menuWithParameters.strPicPath);
        assertNull(menuWithParameters.strAudioPath);
        assertEquals("textfile.txt", menuWithParameters.strTextFilePath);
        assertTrue(menuWithParameters.pal_menu);
        assertNotNull(menuWithParameters.m_GUI);
        
        Menu menuWithAllParameters = new Menu("Title", "picpath.png", "audiopath.wav", "textfile.txt", false, new GUI());
        assertEquals("Title", menuWithAllParameters.strTitle);
        assertEquals("picpath.png", menuWithAllParameters.strPicPath);
        assertEquals("audiopath.wav", menuWithAllParameters.strAudioPath);
        assertEquals("textfile.txt", menuWithAllParameters.strTextFilePath);
        assertFalse(menuWithAllParameters.pal_menu);
        assertNotNull(menuWithAllParameters.m_GUI);
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