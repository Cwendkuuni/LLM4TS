
package com.example;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

public class MenuTest {

    @Test
    public void testMenuConstructor() {
        Menu menu = new Menu();
        assertNull(menu.strTitle);
        assertNull(menu.strPicPath);
        assertNull(menu.strAudioPath);
        assertNull(menu.strTextFilePath);
        assertEquals("dvd-menu", menu.dvd_menu);
        assertEquals(50, menu.titles.length);
        assertEquals("Menu Error - ", menu.baseErr);
        assertNull(menu.video_files);
        assertNull(menu.m_GUI);
        assertNull(menu.m_Thread);
        assertFalse(menu.m_Error);

        Menu menuWithParams = new Menu("Title", "TextFilePath", true, new GUI());
        assertEquals("Title", menuWithParams.strTitle);
        assertNull(menuWithParams.strPicPath);
        assertNull(menuWithParams.strAudioPath);
        assertEquals("TextFilePath", menuWithParams.strTextFilePath);
        assertTrue(menuWithParams.pal_menu);
        assertNotNull(menuWithParams.m_GUI);

        Menu menuWithAllParams = new Menu("Title", "PicPath", "AudioPath", "TextFilePath", false, new GUI());
        assertEquals("Title", menuWithAllParams.strTitle);
        assertEquals("PicPath", menuWithAllParams.strPicPath);
        assertEquals("AudioPath", menuWithAllParams.strAudioPath);
        assertEquals("TextFilePath", menuWithAllParams.strTextFilePath);
        assertFalse(menuWithAllParams.pal_menu);
        assertNotNull(menuWithAllParams.m_GUI);
    }

    @Test
    public void testInit() {
        Menu menu = new Menu();
        assertFalse(menu.init());

        Menu menuWithParams = new Menu("Title", "TextFilePath", true, new GUI());
        assertFalse(menuWithParams.init());
    }

    @Test
    public void testRun() {
        Menu menu = new Menu();
        menu.run();
    }

    @Test
    public void testDVDMainMenu() {
        Menu menu = new Menu();
        menu.DVDMainMenu();
    }

    @Test
    public void testCreateXML() {
        Menu menu = new Menu();
        assertFalse(menu.createXML());
    }
}
