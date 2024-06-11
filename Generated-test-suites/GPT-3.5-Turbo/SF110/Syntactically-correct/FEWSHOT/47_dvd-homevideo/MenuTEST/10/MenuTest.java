import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

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
    public void testMenuWithTitleTextFilePathIsPALAndDVD_GUI() {
        Menu menuWithTitleTextFilePathIsPALAndDVD_GUI = new Menu("Title", "TextFilePath", true, new GUI());
        assertNotNull(menuWithTitleTextFilePathIsPALAndDVD_GUI);
        assertEquals("dvd-menu", menuWithTitleTextFilePathIsPALAndDVD_GUI.dvd_menu);
        assertEquals("Title", menuWithTitleTextFilePathIsPALAndDVD_GUI.strTitle);
        assertEquals("TextFilePath", menuWithTitleTextFilePathIsPALAndDVD_GUI.strTextFilePath);
        assertTrue(menuWithTitleTextFilePathIsPALAndDVD_GUI.pal_menu);
        assertNotNull(menuWithTitleTextFilePathIsPALAndDVD_GUI.m_GUI);
    }
    
    @Test
    public void testMenuWithTitlePicPathAudioPathTextFilePathIsPALAndDVD_GUI() {
        Menu menuWithTitlePicPathAudioPathTextFilePathIsPALAndDVD_GUI = new Menu("Title", "PicPath", "AudioPath", "TextFilePath", true, new GUI());
        assertNotNull(menuWithTitlePicPathAudioPathTextFilePathIsPALAndDVD_GUI);
        assertEquals("dvd-menu", menuWithTitlePicPathAudioPathTextFilePathIsPALAndDVD_GUI.dvd_menu);
        assertEquals("Title", menuWithTitlePicPathAudioPathTextFilePathIsPALAndDVD_GUI.strTitle);
        assertEquals("PicPath", menuWithTitlePicPathAudioPathTextFilePathIsPALAndDVD_GUI.strPicPath);
        assertEquals("AudioPath", menuWithTitlePicPathAudioPathTextFilePathIsPALAndDVD_GUI.strAudioPath);
        assertEquals("TextFilePath", menuWithTitlePicPathAudioPathTextFilePathIsPALAndDVD_GUI.strTextFilePath);
        assertTrue(menuWithTitlePicPathAudioPathTextFilePathIsPALAndDVD_GUI.pal_menu);
        assertNotNull(menuWithTitlePicPathAudioPathTextFilePathIsPALAndDVD_GUI.m_GUI);
    }
    
    @Test
    public void testInit() {
        boolean result = menu.init();
        assertFalse(result);
    }
    
    @Test
    public void testCreateXML() {
        boolean result = menu.createXML();
        assertFalse(result);
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
    public void testStripLeadingAndTrailingQuotes() {
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("\"foo\""));
        assertEquals("foo \"bar\"", Util.stripLeadingAndTrailingQuotes("foo \"bar\""));
        assertEquals("\"foo\" bar", Util.stripLeadingAndTrailingQuotes("\"foo\" bar"));
        assertEquals("\"foo\" and \"bar\"", Util.stripLeadingAndTrailingQuotes("\"foo\" and \"bar\""));
        assertEquals("", Util.stripLeadingAndTrailingQuotes("\""));
    }

    @Test
    public void testStripLeadingHyphens() {
        assertEquals("f", Util.stripLeadingHyphens("-f"));
        assertEquals("foo", Util.stripLeadingHyphens("--foo"));
        assertEquals("-foo", Util.stripLeadingHyphens("---foo"));
        assertNull(Util.stripLeadingHyphens(null));
    }
}