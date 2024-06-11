
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class MenuTest {
    private Menu menu;

    @Before
    public void setUp() {
        menu = new Menu();
    }

    @Test
    public void testMenu() {
        assertNotNull(menu);
    }

    @Test
    public void testMenuWithTitleTextFilePathIsPALAndDVDGUI() {
        Menu menu = new Menu("title", "TextFilePath", true, new GUI());
        assertNotNull(menu);
        assertEquals("title", menu.strTitle);
        assertEquals("TextFilePath", menu.strTextFilePath);
        assertTrue(menu.pal_menu);
        assertNotNull(menu.m_GUI);
    }

    @Test
    public void testMenuWithTitlePicPathAudioPathTextFilePathIsPALAndDVDGUI() {
        Menu menu = new Menu("title", "picPath", "audioPath", "TextFilePath", true, new GUI());
        assertNotNull(menu);
        assertEquals("title", menu.strTitle);
        assertEquals("picPath", menu.strPicPath);
        assertEquals("audioPath", menu.strAudioPath);
        assertEquals("TextFilePath", menu.strTextFilePath);
        assertTrue(menu.pal_menu);
        assertNotNull(menu.m_GUI);
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
        assertFalse(menu.createXML());
    }
}
