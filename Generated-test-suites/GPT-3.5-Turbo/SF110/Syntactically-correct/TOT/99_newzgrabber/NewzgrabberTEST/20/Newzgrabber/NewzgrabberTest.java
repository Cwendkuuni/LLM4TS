import static org.junit.Assert.*;
import org.junit.*;
import java.io.*;
import java.util.*;

public class NewzgrabberTest {

    @Test
    // Test for the constructor Newzgrabber()
    public void testConstructor() {
        Newzgrabber newz = new Newzgrabber();
        assertNotNull(newz);
        assertEquals(true, Newzgrabber.IsBatch);
    }

    @Test
    // Test for the constructor Newzgrabber(String s)
    public void testConstructorWithArg() {
        Newzgrabber newz = new Newzgrabber("Newzgrabber");
        assertNotNull(newz);
        assertEquals(false, Newzgrabber.IsBatch);
    }

    @Test
    // Test for the method setupBasicSystem()
    public void testSetupBasicSystem() {
        Newzgrabber newz = new Newzgrabber();
        newz.setupBasicSystem();
        assertEquals(true, Newzgrabber.verbose || Newzgrabber.debug);
        assertNotNull(Newzgrabber.Newzdirectory);
        assertNotNull(Newzgrabber.nf);
    }

    @Test
    // Test for the method findDirectory(String classfile)
    public void testFindDirectory() {
        Newzgrabber newz = new Newzgrabber();
        String directory = newz.findDirectory("Newz.jar");
        assertNotNull(directory);
    }

    @Test
    // Test for the method windowClosed(WindowEvent we)
    public void testWindowClosed() {
        Newzgrabber newz = new Newzgrabber();
        newz.windowClosed(null);
        // unable to assert anything since the method does nothing
    }

    @Test
    // Test for the method windowOpened(WindowEvent we)
    public void testWindowOpened() {
        Newzgrabber newz = new Newzgrabber();
        newz.windowOpened(null);
        // unable to assert anything since the method does nothing
    }

    @Test
    // Test for the method windowActivated(WindowEvent we)
    public void testWindowActivated() {
        Newzgrabber newz = new Newzgrabber();
        newz.windowActivated(null);
        // unable to assert anything since the method does nothing
    }

    @Test
    // Test for the method windowDeactivated(WindowEvent we)
    public void testWindowDeactivated() {
        Newzgrabber newz = new Newzgrabber();
        newz.windowDeactivated(null);
        // unable to assert anything since the method does nothing
    }

    @Test
    // Test for the method windowIconified(WindowEvent we)
    public void testWindowIconified() {
        Newzgrabber newz = new Newzgrabber();
        newz.windowIconified(null);
        // unable to assert anything since the method does nothing
    }

    @Test
    // Test for the method windowDeiconified(WindowEvent we)
    public void testWindowDeiconified() {
        Newzgrabber newz = new Newzgrabber();
        newz.windowDeiconified(null);
        // unable to assert anything since the method does nothing
    }

    @Test
    // Test for the method windowClosing(WindowEvent we)
    public void testWindowClosing() {
        Newzgrabber newz = new Newzgrabber();
        OutputStream os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(os);
        System.setErr(ps);
        newz.windowClosing(null);
        assertEquals("", os.toString());
        // unable to assert anything since the method System.exit(0)
    }

    @Test
    // Test for the method updateIni(String inifile, String prop, String key, String value)
    public void testUpdateIni() {
        Newzgrabber newz = new Newzgrabber();
        newz.updateIni("Test.ini", "prop", "key", "value");
        File file = new File("Test.ini");
        assertTrue(file.exists());
    }

    @Test
    // Test for the method main(String[] args)
    public void testMain() {
        OutputStream os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(os);
        System.setOut(ps);
        Newzgrabber.main(new String[] {});        
        assertEquals("The user directory is:\n" + System.getProperty("user.dir") + "\n", os.toString());
    }

}