import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MenuTest {
    
    private Menu menu;
    
    @Before
    public void setUp() {
        menu = new Menu("Title", "PicPath", "AudioPath", "TextFilePath", true, new GUI());
    }
    
    @Test
    public void testInit() {
        assertTrue(menu.init());
    }
    
    @Test
    public void testDVDMainMenu() {
        menu.DVDMainMenu();
        assertNotNull(menu.video_files);
    }
    
    @Test
    public void testCreateXML() {
        assertFalse(menu.createXML());
    }
}