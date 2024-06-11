package Newzgrabber;

import org.junit.*;
import static org.junit.Assert.*;
import java.io.File;

public class NewzgrabberTest {
    
    @Test
    public void testFindDirectory() {
        String classfile = "Newz.jar";
        File file = new File(classfile);
        assertTrue(file.isFile());
        assertTrue(file.getName().equals(classfile));
        File directory = file.getParentFile();
        assertNotNull(directory);
        assertEquals(directory.getAbsolutePath(), Newzgrabber.findDirectory(classfile));
    }
    
    @Test
    public void testUpdateIni() {
        String inifile = "test.ini";
        String prop = "prop";
        String key = "key";
        String value = "value";
        
        Newzgrabber ng = new Newzgrabber();
        ng.updateIni(inifile, prop, key, value);
        
        File file = new File(inifile);
        assertTrue(file.exists());
        
        // Code to verify the content of the ini file
    }
    
    @Test
    public void testWindowClosing() {
        Newzgrabber ng = new Newzgrabber("Newzgrabber");
        WindowEvent we = new WindowEvent(ng, WindowEvent.WINDOW_CLOSING);
        ng.windowClosing(we);
        
        assertFalse(ng.isDisplayable());
    }
}