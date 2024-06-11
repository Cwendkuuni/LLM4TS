import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MenuTest {

    private Menu menu;

    @Before
    public void setUp() {
        menu = new Menu();
    }

    // Test cases for constructor Menu()
    @Test
    public void testDefaultConstructor() {
        assertNotNull(menu.titles);
        assertEquals(50, menu.titles.length);
        assertEquals("dvd-menu", menu.dvd_menu);
        assertEquals("Menu Error - ", menu.baseErr);
    }

    // Test cases for constructor Menu(String title, String TextFilePath, boolean isPAL, GUI DVD_GUI)
    @Test
    public void testConstructorWithTitleTextFilePathIsPALDVDGUI() {
        String title = "Test";
        String textFilePath = "test.txt";
        boolean isPAL = true;
        GUI dvdGUI = new GUI();
        menu = new Menu(title, textFilePath, isPAL, dvdGUI);

        assertEquals(title, menu.strTitle);
        assertEquals(textFilePath, menu.strTextFilePath);
        assertEquals(isPAL, menu.pal_menu);
        assertEquals(dvdGUI, menu.m_GUI);
    }

    // Test cases for constructor Menu(String title, String picPath, String audioPath, String TextFilePath, boolean isPAL, GUI DVD_GUI)
    @Test
    public void testConstructorWithTitlePicPathAudioPathTextFilePathIsPALDVDGUI() {
        String title = "Test";
        String picPath = "pic.png";
        String audioPath = "audio.mp3";
        String textFilePath = "test.txt";
        boolean isPAL = true;
        GUI dvdGUI = new GUI();
        menu = new Menu(title, picPath, audioPath, textFilePath, isPAL, dvdGUI);

        assertEquals(title, menu.strTitle);
        assertEquals(picPath, menu.strPicPath);
        assertEquals(audioPath, menu.strAudioPath);
        assertEquals(textFilePath, menu.strTextFilePath);
        assertEquals(isPAL, menu.pal_menu);
        assertEquals(dvdGUI, menu.m_GUI);
    }

    // Test cases for init() method
    @Test
    public void testInit() {
        assertFalse(menu.init());
    }

    // Test cases for run() method
    @Test
    public void testRun() {
        menu.run();
    }

    // Test cases for DVDMainMenu() method
    @Test
    public void testDVDMainMenu() {
        // Test case 1: strPicPath and strAudioPath are null
        menu.strPicPath = null;
        menu.strAudioPath = null;
        menu.strTextFilePath = null;
        menu.m_GUI = new GUI();
        menu.DVDMainMenu();

        // Test case 2: strPicPath is empty and strAudioPath is not null
        menu.strPicPath = "";
        menu.strAudioPath = "audio.mp3";
        menu.DVDMainMenu();

        // Test case 3: strPicPath is null and strAudioPath is empty
        menu.strPicPath = null;
        menu.strAudioPath = "";
        menu.DVDMainMenu();

        // Test case 4: strPicPath and strAudioPath are not null
        menu.strPicPath = "pic.png";
        menu.strAudioPath = "audio.mp3";
        menu.DVDMainMenu();
    }

    // Test cases for createXML() method
    @Test
    public void testCreateXML() {
        assertFalse(menu.createXML());
    }

    // Test cases for FilenameFilter accept() method
    @Test
    public void testFilenameFilterAccept() {
        Menu.FilenameFilter filter = menu.new FilenameFilter();

        // Test case 1: name ends with "vob" and is not equal to "menu.vob"
        assertTrue(filter.accept(null, "test.vob"));

        // Test case 2: name ends with "vob" and is equal to "menu.vob"
        assertFalse(filter.accept(null, "menu.vob"));

        // Test case 3: name does not end with "vob"
        assertFalse(filter.accept(null, "test.txt"));
    }

}