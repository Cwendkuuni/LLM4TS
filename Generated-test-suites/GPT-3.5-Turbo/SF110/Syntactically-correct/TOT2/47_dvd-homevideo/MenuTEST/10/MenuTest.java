import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;

public class MenuTest {

    private Menu menu;

    @Before
    public void setUp() {
        menu = new Menu("Test Title", "testPicPath", "testAudioPath", "testTextFilePath", true, new GUI());
    }

    @Test
    public void testMenuInit() {
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

    @Test
    public void testRun() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        menu.run();
        assertTrue(outContent.toString().contains("DVDMainMenu"));
    }
}