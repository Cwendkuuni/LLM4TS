package Newzgrabber;

import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class NewzgrabberTest {

    private static Newzgrabber ng;

    @BeforeClass
    public static void setUp() {
        ng = new Newzgrabber();
    }

    @AfterClass
    public static void tearDown() {
        ng = null;
    }

    @Test
    public void testSetupBasicSystem() {
        Newzgrabber.setupBasicSystem();
        // Assert statements
    }

    @Test
    public void testFindDirectory() {
        String classfile = "Newz.jar";
        String directory = Newzgrabber.findDirectory(classfile);
        assertNotNull(directory);
    }

    @Test
    public void testUpdateIni() {
        String inifile = "Newzgrabber.ini";
        String prop = "prop";
        String key = "key";
        String value = "value";
        ng.updateIni(inifile, prop, key, value);
    }

    @Test
    public void testWindowClosing() {
        Newzgrabber ng = new Newzgrabber();
        ng.windowClosing(null);
        // Assert statements
    }

    @Test
    public void testNewzgrabberString() {
        Newzgrabber ng = new Newzgrabber("Newzgrabber");
        // Assert statements
    }
}