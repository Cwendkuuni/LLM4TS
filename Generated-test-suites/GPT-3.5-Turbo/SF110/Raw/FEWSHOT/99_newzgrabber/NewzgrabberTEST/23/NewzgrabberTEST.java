package Newzgrabber;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class NewzgrabberTest {

    private Newzgrabber newzgrabber;

    @Before
    public void setUp() {
        newzgrabber = new Newzgrabber();
    }

    @Test
    public void testSetupBasicSystem() {
        // Test case for Windows
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("windows")) {
            try {
                System.setProperty("java.library.path", Newzgrabber.Newzdirectory + System.getProperty("path.separator") + System.getProperty("java.library.path"));
            } catch (Exception e) {
                fail("There was a problem setting the lib path!");
            }
            if (Newzgrabber.debug || Newzgrabber.verbose) {
                assertNotNull(System.getProperty("java.library.path"));
            }
        }

        // Test the newz dir
        if (Newzgrabber.verbose) {
            assertNotNull(Newzgrabber.Newzdirectory);
        }
    }

    @Test
    public void testFindDirectory() {
        String classfile = Newzgrabber.JarFilename;
        String directory = Newzgrabber.findDirectory(classfile);
        assertNotNull(directory);
    }

    @Test
    public void testUpdateIni() {
        String inifile = "Newzgrabber.ini";
        String prop = "Messageids";
        String key = "TestKey";
        String value = "TestValue";
        newzgrabber.updateIni(inifile, prop, key, value);

        // Verify that the key-value pair is added to the ini file
        // and the value is updated
        Ini ini = new Ini(inifile, prop);
        LinkedList<String[]> iniVector = ini.getIniVector();
        boolean foundKey = false;
        for (String[] iniKey : iniVector) {
            if (iniKey[0].equalsIgnoreCase(key)) {
                assertEquals(value, iniKey[1]);
                foundKey = true;
                break;
            }
        }
        assertTrue(foundKey);
    }
}