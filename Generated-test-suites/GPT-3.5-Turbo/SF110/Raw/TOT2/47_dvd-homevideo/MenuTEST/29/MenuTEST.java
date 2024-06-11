import org.junit.Test;
import static org.junit.Assert.*;

public class MenuTest {

    @Test
    public void testMenuConstructor1() {
        Menu menu = new Menu();
        assertNotNull(menu);
        assertEquals(menu.dvd_menu, "dvd-menu");
        assertEquals(menu.titles.length, 50);
        assertEquals(menu.baseErr, "Menu Error - ");
    }
    
    @Test
    public void testMenuConstructor2() {
        String title = "Test Title";
        String textFilePath = "TestFilePath";
        boolean isPal = true;
        GUI gui = new GUI();
        Menu menu = new Menu(title, textFilePath, isPal, gui);
        assertNotNull(menu);
        assertEquals(menu.strTitle, title);
        assertEquals(menu.strTextFilePath, textFilePath);
        assertEquals(menu.pal_menu, isPal);
        assertEquals(menu.m_GUI, gui);
    }
    
    @Test
    public void testMenuConstructor3() {
        String title = "Test Title";
        String picPath = "TestPicPath";
        String audioPath = "TestAudioPath";
        String textFilePath = "TestFilePath";
        boolean isPal = true;
        GUI gui = new GUI();
        Menu menu = new Menu(title, picPath, audioPath, textFilePath, isPal, gui);
        assertNotNull(menu);
        assertEquals(menu.strTitle, title);
        assertEquals(menu.strPicPath, picPath);
        assertEquals(menu.strAudioPath, audioPath);
        assertEquals(menu.strTextFilePath, textFilePath);
        assertEquals(menu.pal_menu, isPal);
        assertEquals(menu.m_GUI, gui);
    }

    // Add more test cases for other methods as needed

}