import org.junit.*;
import static org.junit.Assert.*;

public class MenuTest {
    
    private Menu menu;
    private GUI mockGUI;
    
    // Test Menu() constructor
    @Test
    public void testMenuConstructor() {
        menu = new Menu();
        assertNotNull(menu);
    }
    
    // Test Menu(String, String, boolean, GUI) constructor
    @Test
    public void testMenuConstructorWithTitleTextFilePathIsPALAndGUI() {
        mockGUI = new GUI();
        menu = new Menu("Title", "TextFilePath", true, mockGUI);
        assertNotNull(menu);
        assertEquals("Title", menu.strTitle);
        assertEquals("TextFilePath", menu.strTextFilePath);
        assertTrue(menu.pal_menu);
        assertEquals(mockGUI, menu.m_GUI);
    }
    
    // Test Menu(String, String, String, String, boolean, GUI) constructor
    @Test
    public void testMenuConstructorWithTitlePicPathAudioPathTextFilePathIsPALAndGUI() {
        mockGUI = new GUI();
        menu = new Menu("Title", "PicPath", "AudioPath", "TextFilePath", true, mockGUI);
        assertNotNull(menu);
        assertEquals("Title", menu.strTitle);
        assertEquals("PicPath", menu.strPicPath);
        assertEquals("AudioPath", menu.strAudioPath);
        assertEquals("TextFilePath", menu.strTextFilePath);
        assertTrue(menu.pal_menu);
        assertEquals(mockGUI, menu.m_GUI);
    }
    
    // Test init() method
    @Test
    public void testInit() {
        mockGUI = new GUI();
        menu = new Menu("Title", "TextFilePath", true, mockGUI);
        assertTrue(menu.init());
    }
    
    // Test run() method
    @Test
    public void testRun() {
        mockGUI = new GUI();
        menu = new Menu("Title", "TextFilePath", true, mockGUI);
        menu.run();
        // We can't perform any assertion on the run method as it is void
    }
    
    // Test DVDMainMenu() method
    @Test
    public void testDVDMainMenu() {
        mockGUI = new GUI();
        menu = new Menu("Title", "PicPath", "AudioPath", "TextFilePath", true, mockGUI);
        menu.DVDMainMenu();
        // We can't perform any assertion on the DVDMainMenu method as it is void
    }
    
    // Test createXML() method
    @Test
    public void testCreateXML() {
        mockGUI = new GUI();
        menu = new Menu("Title", "PicPath", "AudioPath", "TextFilePath", true, mockGUI);
        assertFalse(menu.createXML());
    }
}


