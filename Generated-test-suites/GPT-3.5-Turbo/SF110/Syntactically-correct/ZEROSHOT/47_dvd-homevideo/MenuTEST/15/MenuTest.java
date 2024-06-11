import org.junit.Before;
import org.junit.Test;
import java.io.File;
import static org.junit.Assert.*;

public class MenuTest {
    private Menu menu;
    private GUI gui;
    private String outputDir;

    @Before
    public void setUp() {
        gui = new GUI();
        outputDir = "testOutputDir";
        gui.strOutputDir = outputDir;
        menu = new Menu("Test Title", "Test Pic Path", "Test Audio Path", "Test Text File Path", true, gui);
    }

    @Test
    public void testMenuConstructor1() {
        assertNotNull(menu);
        assertEquals("dvd-menu", menu.dvd_menu);
        assertArrayEquals(new String[50], menu.titles);
        assertEquals("Menu Error - ", menu.baseErr);
        assertNull(menu.strTitle);
        assertNull(menu.strPicPath);
        assertNull(menu.strAudioPath);
        assertNull(menu.strTextFilePath);
        assertTrue(menu.pal_menu);
        assertEquals(gui, menu.m_GUI);
    }

    @Test
    public void testMenuConstructor2() {
        Menu menu2 = new Menu();
        assertNotNull(menu2);
        assertEquals("dvd-menu", menu2.dvd_menu);
        assertArrayEquals(new String[50], menu2.titles);
        assertEquals("Menu Error - ", menu2.baseErr);
    }

    @Test
    public void testMenuConstructor3() {
        assertNotNull(menu);
        assertEquals("dvd-menu", menu.dvd_menu);
        assertArrayEquals(new String[50], menu.titles);
        assertEquals("Menu Error - ", menu.baseErr);
        assertEquals("Test Title", menu.strTitle);
        assertEquals("Test Text File Path", menu.strTextFilePath);
        assertTrue(menu.pal_menu);
        assertEquals(gui, menu.m_GUI);
    }

    @Test
    public void testMenuConstructor4() {
        assertNotNull(menu);
        assertEquals("dvd-menu", menu.dvd_menu);
        assertArrayEquals(new String[50], menu.titles);
        assertEquals("Menu Error - ", menu.baseErr);
        assertEquals("Test Title", menu.strTitle);
        assertEquals("Test Pic Path", menu.strPicPath);
        assertEquals("Test Audio Path", menu.strAudioPath);
        assertEquals("Test Text File Path", menu.strTextFilePath);
        assertTrue(menu.pal_menu);
        assertEquals(gui, menu.m_GUI);
    }

    @Test
    public void testInit() {
        assertFalse(menu.init());
    }

    @Test
    public void testRun() {
        menu.run();
    }

    @Test
    public void testDVDMainMenu() {
        menu.DVDMainMenu();
    }

    @Test
    public void testCreateXML() {
        File xmlFile = new File(outputDir + "/vmgm.xml");
        assertFalse(menu.createXML());
        assertTrue(xmlFile.exists());
    }
}