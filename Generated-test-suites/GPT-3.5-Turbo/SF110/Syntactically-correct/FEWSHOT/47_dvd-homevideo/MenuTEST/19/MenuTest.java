
package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class MenuTest {
    @Test
    public void testInit() {
        Menu menu = new Menu();
        assertEquals(false, menu.init());
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
        assertEquals(false, menu.createXML());
    }
}
