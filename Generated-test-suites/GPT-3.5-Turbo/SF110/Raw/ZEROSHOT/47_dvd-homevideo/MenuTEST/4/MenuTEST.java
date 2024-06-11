import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;

public class MenuTest {
    
    private Menu menu;
    private GUI gui;
    
    @Before
    public void setUp() {
        gui = new GUI();
        menu = new Menu("Test Menu", "Test Pic Path", "Test Audio Path", "Test Text File Path", true, gui);
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
        assertFalse(menu.createXML());
    }
}