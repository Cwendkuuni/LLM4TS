import static org.junit.Assert.*;
import org.junit.*;

public class MenuTest {

    @Test
    public void testMenuConstructor() {
        Menu menu = new Menu();
        assertEquals(menu.dvd_menu, "dvd-menu");
        assertNotNull(menu.titles);
        assertNotNull(menu.baseErr);
    }
    
    @Test
    public void testMenuConstructorWithTitleTextFilePathIsPALAndGUI() {
        String title = "Test Title";
        String textFilePath = "path/to/text/file";
        boolean isPAL = true;
        GUI gui = new GUI();
        
        Menu menu = new Menu(title, textFilePath, isPAL, gui);
        assertEquals(menu.dvd_menu, "dvd-menu");
        assertNotNull(menu.titles);
        assertNotNull(menu.baseErr);
        assertEquals(menu.strTitle, title);
        assertEquals(menu.strTextFilePath, textFilePath);
        assertTrue(menu.pal_menu);
        assertEquals(menu.m_GUI, gui);
    }
    
    @Test
    public void testMenuConstructorWithTitlePicPathAudioPathTextFilePathIsPALAndGUI() {
        String title = "Test Title";
        String picPath = "path/to/pic/file";
        String audioPath = "path/to/audio/file";
        String textFilePath = "path/to/text/file";
        boolean isPAL = true;
        GUI gui = new GUI();
        
        Menu menu = new Menu(title, picPath, audioPath, textFilePath, isPAL, gui);
        assertEquals(menu.dvd_menu, "dvd-menu");
        assertNotNull(menu.titles);
        assertNotNull(menu.baseErr);
        assertEquals(menu.strTitle, title);
        assertEquals(menu.strPicPath, picPath);
        assertEquals(menu.strAudioPath, audioPath);
        assertEquals(menu.strTextFilePath, textFilePath);
        assertTrue(menu.pal_menu);
        assertEquals(menu.m_GUI, gui);
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
        GUI gui = new GUI();
        Menu menu = new Menu("Test Title", "/path/to/pic", "/path/to/audio", "/path/to/text", true, gui);
        menu.DVDMainMenu();
        assertNotNull(menu.video_files);
        assertNotNull(menu.m_GUI.lblAuthor);
        assertNotNull(menu.m_GUI.lblAuthorProg);
        assertNotNull(menu.m_GUI.prgAuthor);
        assertTrue(menu.m_GUI.prgAuthor.isIndeterminate());
        assertNotNull(menu.m_Thread);
    }
    
    @Test
    public void testCreateXML() {
        GUI gui = new GUI();
        Menu menu = new Menu("Test Title", "/path/to/pic", "/path/to/audio", "/path/to/text", true, gui);
        assertFalse(menu.createXML());
    }
}