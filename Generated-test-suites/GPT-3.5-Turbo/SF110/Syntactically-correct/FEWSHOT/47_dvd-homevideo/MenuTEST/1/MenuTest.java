package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class MenuTest {
    
    private Menu menu;
    private GUI gui;
    
    @Before
    public void setUp() {
        menu = new Menu();
        gui = new GUI();
    }
    
    @Test
    public void testMenuConstructor() {
        assertNotNull(menu.getDvdMenu());
        assertEquals(50, menu.getTitles().length);
        assertEquals("Menu Error - ", menu.getBaseErr());
    }
    
    @Test
    public void testMenuConstructorWithTitleAndTextFilePathAndIsPALAndGUI() {
        menu = new Menu("Title", "TextFilePath", true, gui);
        
        assertEquals("dvd-menu", menu.getDvdMenu());
        assertEquals(50, menu.getTitles().length);
        assertEquals("Menu Error - ", menu.getBaseErr());
        assertEquals("Title", menu.getStrTitle());
        assertEquals("TextFilePath", menu.getStrTextFilePath());
        assertTrue(menu.isPalMenu());
        assertEquals(gui, menu.getM_GUI());
    }
    
    @Test
    public void testMenuConstructorWithTitleAndPicPathAndAudioPathAndTextFilePathAndIsPALAndGUI() {
        menu = new Menu("Title", "PicPath", "AudioPath", "TextFilePath", true, gui);
        
        assertEquals("dvd-menu", menu.getDvdMenu());
        assertEquals(50, menu.getTitles().length);
        assertEquals("Menu Error - ", menu.getBaseErr());
        assertEquals("Title", menu.getStrTitle());
        assertEquals("PicPath", menu.getStrPicPath());
        assertEquals("AudioPath", menu.getStrAudioPath());
        assertEquals("TextFilePath", menu.getStrTextFilePath());
        assertTrue(menu.isPalMenu());
        assertEquals(gui, menu.getM_GUI());
    }
    
    @Test
    public void testInit() {
        assertTrue(menu.init());
    }
    
    @Test
    public void testRun() {
        menu.run();
        
        // No assertions to be made, as it is difficult to test a run method which runs indefinitely until interrupted
    }
    
    @Test
    public void testDVDMainMenu() {
        menu.setStrPicPath("PicPath");
        menu.setStrAudioPath("AudioPath");
        menu.setStrTextFilePath("TextFilePath");
        menu.setPalMenu(true);
        menu.setM_GUI(gui);
        
        menu.DVDMainMenu();
        
        // No assertions to be made, as it is difficult to test a method which executes external processes and manipulates files
    }
    
    @Test
    public void testCreateXML() {
        assertFalse(menu.createXML());
    }
}