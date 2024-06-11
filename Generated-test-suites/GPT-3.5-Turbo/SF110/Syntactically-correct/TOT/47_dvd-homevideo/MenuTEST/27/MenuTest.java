import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;

public class MenuTest {

    private Menu menu;

    @Before
    public void setUp(){
        menu = new Menu();
    }

    @Test
    public void testInit(){
        assertTrue(menu.init());
    }

    @Test
    public void testRun(){
        menu.run();
    }

    @Test
    public void testDVDMainMenu(){
        menu.DVDMainMenu();
    }

    @Test
    public void testCreateXML(){
        assertTrue(menu.createXML());
    }

    @Test
    public void testMenuConstructor1(){
        Menu menu1 = new Menu();

        assertNotNull(menu1);
    }

    @Test
    public void testMenuConstructor2(){
        String title = "Test";
        String textFilePath = "text.txt";
        boolean isPAL = true;
        GUI DVD_GUI = new GUI();

        Menu menu2 = new Menu(title, textFilePath, isPAL, DVD_GUI);

        assertNotNull(menu2);
        assertEquals(title, menu2.strTitle);
        assertEquals(textFilePath, menu2.strTextFilePath);
        assertEquals(isPAL, menu2.pal_menu);
        assertEquals(DVD_GUI, menu2.m_GUI);
    }

    @Test
    public void testMenuConstructor3(){
        String title = "Test";
        String picPath = "pic.jpg";
        String audioPath = "audio.wav";
        String textFilePath = "text.txt";
        boolean isPAL = true;
        GUI DVD_GUI = new GUI();

        Menu menu3 = new Menu(title, picPath, audioPath, textFilePath, isPAL, DVD_GUI);

        assertNotNull(menu3);
        assertEquals(title, menu3.strTitle);
        assertEquals(picPath, menu3.strPicPath);
        assertEquals(audioPath, menu3.strAudioPath);
        assertEquals(textFilePath, menu3.strTextFilePath);
        assertEquals(isPAL, menu3.pal_menu);
        assertEquals(DVD_GUI, menu3.m_GUI);
    }

    @Test
    public void testAcceptMethodWithVobFile() {
        FilenameFilter filter = menu.new FilenameFilter();

        File dir1 = new File("dir1");
        File dir2 = new File("dir2");

        assertTrue(filter.accept(dir1, "file.vob"));
        assertTrue(filter.accept(dir2, "movie.vob"));
    }

    @Test
    public void testAcceptMethodWithMenuVobFile() {
        FilenameFilter filter = menu.new FilenameFilter();

        File dir1 = new File("dir1");

        assertFalse(filter.accept(dir1, "menu.vob"));
    }

    @Test
    public void testAcceptMethodWithNonVobFile() {
        FilenameFilter filter = menu.new FilenameFilter();

        File dir1 = new File("dir1");
        File dir2 = new File("dir2");

        assertFalse(filter.accept(dir1, "file.txt"));
        assertFalse(filter.accept(dir2, "movie.doc"));
    }

    @Test
    public void testRunMethod() {
        menu.run(); // No assertion can be made as the method does not return anything
    }

    @Test
    public void testDVDMainMenuMethod() {
        menu.DVDMainMenu(); // No assertion can be made as the method does not return anything
    }

    @Test
    public void testCreateXMLMethodWithValidInput() {
        GUI DVD_GUI = new GUI();
        menu.m_GUI = DVD_GUI;

        assertFalse(menu.createXML());
    }

    @Test
    public void testCreateXMLMethodWithIOException() {
        GUI DVD_GUI = new GUI();
        menu.m_GUI = DVD_GUI;
        menu.m_GUI.strOutputDir = "non_existing_directory";

        assertTrue(menu.createXML());
    }
}