package Newzgrabber;

import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;

public class NewzgrabberTest {

    @Test
    public void testSetupBasicSystem() {
        Newzgrabber.setupBasicSystem();
        assertNotNull(Newzgrabber.Newzdirectory);
        assertFalse(Newzgrabber.Newzdirectory.isEmpty());
        assertNotNull(Newzgrabber.nf);
    }

    @Test
    public void testFindDirectory() {
        String directory = Newzgrabber.findDirectory("Newz.jar");
        assertNotNull(directory);
        assertFalse(directory.isEmpty());
    }

    @Test
    public void testUpdateIni() {
        Newzgrabber ng = new Newzgrabber();
        String inifile = "filename";
        String prop = "prop";
        String key = "key";
        String value = "value";
        ng.updateIni(inifile, prop, key, value);

        try {
            BufferedReader reader = new BufferedReader(new FileReader(inifile));
            String line;
            boolean foundValue = false;
            while ((line = reader.readLine()) != null) {
                if (line.contains(key + " = " + value)) {
                    foundValue = true;
                    break;
                }
            }
            reader.close();

            assertTrue(foundValue);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testWindowClosing() {
        Newzgrabber ng = new Newzgrabber();
        ng.windowClosing(null);
        assertFalse(ng.isVisible());
    }

}