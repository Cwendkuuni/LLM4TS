import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

public class MenuTest {
    @Test
    public void testMenuConstructor1() {
        Menu menu = new Menu();
        
        assertNotNull(menu.dvd_menu);
        assertEquals(50, menu.titles.length);
        assertNotNull(menu.baseErr);
    }
    
    @Test
    public void testMenuConstructor2() {
        String title = "Title";
        String textFilePath = "TextFilePath";
        boolean isPAL = true;
        GUI dvdGUI = new GUI();
        
        Menu menu = new Menu(title, textFilePath, isPAL, dvdGUI);
        
        assertNotNull(menu.dvd_menu);
        assertEquals(50, menu.titles.length);
        assertNotNull(menu.baseErr);
        assertEquals(title, menu.strTitle);
        assertEquals(textFilePath, menu.strTextFilePath);
        assertTrue(menu.pal_menu);
        assertEquals(dvdGUI, menu.m_GUI);
    }
    
    @Test
    public void testMenuConstructor3() {
        String title = "Title";
        String picPath = "PicPath";
        String audioPath = "AudioPath";
        String textFilePath = "TextFilePath";
        boolean isPAL = true;
        GUI dvdGUI = new GUI();
        
        Menu menu = new Menu(title, picPath, audioPath, textFilePath, isPAL, dvdGUI);
        
        assertNotNull(menu.dvd_menu);
        assertEquals(50, menu.titles.length);
        assertNotNull(menu.baseErr);
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
        
        boolean result = menu.init();
        
        assertFalse(result);
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
        
        boolean result = menu.createXML();
        
        assertFalse(result);
    }
}