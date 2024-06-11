package Newzgrabber;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NewzgrabberTest {

    private Newzgrabber newzgrabber;

    @Before
    public void setUp() {
        newzgrabber = new Newzgrabber();
    }

    @Test
    public void testSetupBasicSystem() {
        assertNotNull(Newzgrabber.findDirectory(Newzgrabber.JarFilename));
    }

    @Test
    public void testFindDirectory() {
        assertNotNull(newzgrabber.findDirectory(Newzgrabber.JarFilename));
    }

    @Test
    public void testUpdateIni() {
        newzgrabber.updateIni("test.ini", "prop", "key", "value");
    }

    @Test
    public void testWindowClosing() {
        newzgrabber.windowClosing(null);
    }

    // Add more test cases for each method here

}