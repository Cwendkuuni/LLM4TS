
package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class MenuTest {
    @Test
    public void testMenuConstructor() {
        Menu menu = new Menu();
        assertNull(menu.strTitle);
        assertNull(menu.strPicPath);
        assertNull(menu.strAudioPath);
        assertNull(menu.strTextFilePath);
        assertEquals("dvd-menu", menu.dvd_menu);
        assertNull(menu.titles);
        assertEquals("Menu Error - ", menu.baseErr);

        menu = new Menu("title", "TextFilePath", true, new GUI());
        assertEquals("title", menu.strTitle);
        assertEquals("TextFilePath", menu.strTextFilePath);
        assertTrue(menu.pal_menu);
        assertNotNull(menu.m_GUI);

        menu = new Menu("title", "picPath", "audioPath", "TextFilePath", true, new GUI());
        assertEquals("title", menu.strTitle);
        assertEquals("picPath", menu.strPicPath);
        assertEquals("audioPath", menu.strAudioPath);
        assertEquals("TextFilePath", menu.strTextFilePath);
        assertTrue(menu.pal_menu);
        assertNotNull(menu.m_GUI);
    }
    
    @Test
    public void testInit() {
        Menu menu = new Menu();
        assertFalse(menu.init());
        
        menu = new Menu("title", "TextFilePath", true, new GUI());
        assertFalse(menu.init());
        
        menu = new Menu("title", "picPath", "audioPath", "TextFilePath", true, new GUI());
        assertFalse(menu.init());
    }
    
    @Test
    public void testRun() {
        Menu menu = new Menu();
        menu.run();
        // no assertions can be made as the method does not return anything
    }
    
    @Test
    public void testDVDMainMenu() {
        Menu menu = new Menu();
        menu.DVDMainMenu();
        // no assertions can be made as the method does not return anything
    }
    
    @Test
    public void testCreateXML() {
        Menu menu = new Menu();
        assertFalse(menu.createXML());
    }
}
