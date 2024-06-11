package Newzgrabber;

import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;

public class NewzgrabberTest {

    @Test
    public void testNewzgrabberConstructor() {
        Newzgrabber newz = new Newzgrabber();
        assertNotNull(newz);
    }

    @Test
    public void testNewzgrabberStringConstructor() {
        Newzgrabber newz = new Newzgrabber("Newzgrabber");
        assertNotNull(newz);
    }

    @Test
    public void testSetupBasicSystem() {
        Newzgrabber.setupBasicSystem();
        // Add test case here
    }

    @Test
    public void testFindDirectory() {
        String directory = Newzgrabber.findDirectory("Newz.jar");
        assertNotNull(directory);
    }

    @Test
    public void testUpdateIni() {
        Newzgrabber newz = new Newzgrabber();
        newz.updateIni("iniFile", "prop", "key", "value");
        // Add test case here
    }

    @Test
    public void testMain() {
        String[] args = {"-verbose"};
        Newzgrabber.main(args);
        // Add test case here
    }
}