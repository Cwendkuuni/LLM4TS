
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MenuTest {
    private Menu menu;
    private GUI gui;

    @Before
    public void setUp() {
        gui = new GUI();
        menu = new Menu("Test Menu", "TestPicPath", "TestAudioPath", "TestTextFilePath", true, gui);
    }

    @Test
    public void testInit() {
        assertTrue(menu.init());
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
        assertFalse(menu.createXML()); // Assuming XML creation will not throw an IOException
    }
}

