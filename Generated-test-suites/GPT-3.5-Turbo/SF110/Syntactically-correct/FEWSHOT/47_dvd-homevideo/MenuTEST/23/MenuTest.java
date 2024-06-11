import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class MenuTest {
    
    @Test
    public void testConstructorMenu() {
        Menu menu = new Menu();
        assertNull(menu.strTitle);
        assertNull(menu.strPicPath);
        assertNull(menu.strAudioPath);
        assertNull(menu.strTextFilePath);
        assertEquals("dvd-menu", menu.dvd_menu);
        assertEquals(50, menu.titles.length);
        assertEquals("Menu Error - ", menu.baseErr);
        
        String title = "Title";
        String picPath = "pic/path";
        String audioPath = "audio/path";
        String textFilePath = "text/file/path";
        boolean isPAL = true;
        GUI dvdGUI = new GUI();
        
        menu = new Menu(title, textFilePath, isPAL, dvdGUI);
        assertEquals(title, menu.strTitle);
        assertNull(menu.strPicPath);
        assertNull(menu.strAudioPath);
        assertEquals(textFilePath, menu.strTextFilePath);
        assertTrue(menu.pal_menu);
        assertEquals(dvdGUI, menu.m_GUI);
        
        menu = new Menu(title, picPath, audioPath, textFilePath, isPAL, dvdGUI);
        assertEquals(title, menu.strTitle);
        assertEquals(picPath, menu.strPicPath);
        assertEquals(audioPath, menu.strAudioPath);
        assertEquals(textFilePath, menu.strTextFilePath);
        assertTrue(menu.pal_menu);
        assertEquals(dvdGUI, menu.m_GUI);
    }
    
    @Test
    public void testInit() {
        Menu menu = new Menu();
        assertTrue(menu.init());
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
        
        // Add more test cases if needed
    }
    
    @Test
    public void testCreateXML() {
        Menu menu = new Menu();
        assertTrue(menu.createXML());
    }
}