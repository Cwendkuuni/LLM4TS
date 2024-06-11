import org.junit.Before;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.*;

public class MenuTest {
    private Menu menu;

    @Before
    public void setUp() {
        menu = new Menu();
    }

    @Test
    public void testInit() {
        assertFalse(menu.init());
    }

    @Test
    public void testRun() {
        menu.run();
        // TODO: Add assertions for the run method
    }

    @Test
    public void testDVDMainMenu() {
        menu.DVDMainMenu();
        // TODO: Add assertions for the DVDMainMenu method
    }

    @Test
    public void testCreateXML() {
        assertFalse(menu.createXML());
    }
}