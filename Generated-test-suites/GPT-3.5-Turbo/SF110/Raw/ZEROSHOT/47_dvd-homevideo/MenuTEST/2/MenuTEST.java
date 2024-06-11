import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.util.Random;

public class MenuTest {

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

    @Test
    public void testConstructor1() {
        String title = "Test Title";
        String textFilePath = "test.txt";
        boolean isPAL = true;
        GUI dvdGUI = new GUI();
        
        Menu menu = new Menu(title, textFilePath, isPAL, dvdGUI);
        
        assertNotNull(menu);
        assertEquals(title, menu.strTitle);
        assertEquals(textFilePath, menu.strTextFilePath);
        assertTrue(menu.pal_menu);
        assertEquals(dvdGUI, menu.m_GUI);
    }

    @Test
    public void testConstructor2() {
        String title = "Test Title";
        String picPath = "picture.jpg";
        String audioPath = "audio.mp3";
        String textFilePath = "test.txt";
        boolean isPAL = true;
        GUI dvdGUI = new GUI();
        
        Menu menu = new Menu(title, picPath, audioPath, textFilePath, isPAL, dvdGUI);
        
        assertNotNull(menu);
        assertEquals(title, menu.strTitle);
        assertEquals(picPath, menu.strPicPath);
        assertEquals(audioPath, menu.strAudioPath);
        assertEquals(textFilePath, menu.strTextFilePath);
        assertTrue(menu.pal_menu);
        assertEquals(dvdGUI, menu.m_GUI);
    }
}