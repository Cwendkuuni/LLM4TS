package Newzgrabber;

import org.junit.*;
import static org.junit.Assert.*;

public class NewzgrabberTest {

    @Test
    public void testNewzgrabber() {
        Newzgrabber newzgrabber = new Newzgrabber();
        assertNotNull(newzgrabber);
    }

    @Test
    public void testNewzgrabberString() {
        Newzgrabber newzgrabber = new Newzgrabber("Newzgrabber");
        assertNotNull(newzgrabber);
    }

    @Test
    public void testSetupBasicSystem() {
        Newzgrabber.setupBasicSystem();
        // Write your assertions here
    }

    @Test
    public void testWindowClosed() {
        Newzgrabber newzgrabber = new Newzgrabber();
        newzgrabber.windowClosed(null);
        // Write your assertions here
    }

    @Test
    public void testWindowOpened() {
        Newzgrabber newzgrabber = new Newzgrabber();
        newzgrabber.windowOpened(null);
        // Write your assertions here
    }

    @Test
    public void testWindowActivated() {
        Newzgrabber newzgrabber = new Newzgrabber();
        newzgrabber.windowActivated(null);
        // Write your assertions here
    }

    @Test
    public void testWindowDeactivated() {
        Newzgrabber newzgrabber = new Newzgrabber();
        newzgrabber.windowDeactivated(null);
        // Write your assertions here
    }

    @Test
    public void testWindowIconified() {
        Newzgrabber newzgrabber = new Newzgrabber();
        newzgrabber.windowIconified(null);
        // Write your assertions here
    }

    @Test
    public void testWindowDeiconified() {
        Newzgrabber newzgrabber = new Newzgrabber();
        newzgrabber.windowDeiconified(null);
        // Write your assertions here
    }

    @Test
    public void testWindowClosing() {
        Newzgrabber newzgrabber = new Newzgrabber();
        newzgrabber.windowClosing(null);
        // Write your assertions here
    }

    @Test
    public void testFindDirectory() {
        String classfile = "Newzgrabber.jar";
        String result = Newzgrabber.findDirectory(classfile);
        assertNotNull(result);
    }

    @Test
    public void testUpdateIni() {
        Newzgrabber newzgrabber = new Newzgrabber();
        newzgrabber.updateIni("inifile", "prop", "key", "value");
        // Write your assertions here
    }

    @Test
    public void testMain() {
        String[] args = {"-verbose"};
        Newzgrabber.main(args);
        // Write your assertions here
    }
    
}