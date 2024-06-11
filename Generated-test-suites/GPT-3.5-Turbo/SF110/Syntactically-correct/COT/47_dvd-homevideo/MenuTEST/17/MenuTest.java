import org.junit.Test;
import static org.junit.Assert.*;

public class MenuTest {

    @Test
    public void testDefaultConstructor() {
        Menu menu = new Menu();
        assertNotNull(menu);
    }
    
    @Test
    public void testParameterizedConstructor1() {
        String title = "TestTitle";
        String textFilePath = "test.txt";
        boolean isPAL = true;
        GUI dvdGui = new GUI();
        Menu menu = new Menu(title, textFilePath, isPAL, dvdGui);
        assertNotNull(menu);
    }
    
    @Test
    public void testParameterizedConstructor2() {
        String title = "TestTitle";
        String picPath = "test.jpg";
        String audioPath = "test.mp3";
        String textFilePath = "test.txt";
        boolean isPAL = true;
        GUI dvdGui = new GUI();
        Menu menu = new Menu(title, picPath, audioPath, textFilePath, isPAL, dvdGui);
        assertNotNull(menu);
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