import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.io.FilenameFilter;
import java.io.InputStreamReader;
import java.io.BufferedReader;

public class MenuTest {
    private Menu menu;
    private GUI gui;
    
    @Test
    public void testInit() {
        menu = new Menu();
        assertTrue(menu.init());
    }
    
    @Test
    public void testRun() {
        menu = new Menu();
        menu.run();
    }
    
    @Test
    public void testDVDMainMenu() {
        menu = new Menu();
        menu.DVDMainMenu();
    }
    
    @Test
    public void testCreateXML() {
        menu = new Menu();
        gui = new GUI();
        gui.strOutputDir = "/output/dir";
        assertTrue(menu.createXML());
    }
}