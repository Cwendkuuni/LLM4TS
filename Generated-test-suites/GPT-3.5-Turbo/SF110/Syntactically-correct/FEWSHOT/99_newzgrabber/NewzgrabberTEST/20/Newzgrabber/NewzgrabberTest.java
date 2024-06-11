package Newzgrabber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class NewzgrabberTest {

    @Test
    public void testFindDirectory() {
        assertEquals("Directory1", Newzgrabber.findDirectory("Class1"));
        assertEquals("Directory2", Newzgrabber.findDirectory("Class2"));
        assertNull(Newzgrabber.findDirectory(""));
    }

    @Test
    public void testUpdateIni() {
        Newzgrabber newzgrabber = new Newzgrabber();

        newzgrabber.updateIni("iniFile1", "prop", "key", "value");
        newzgrabber.updateIni("iniFile2", "prop", "key", "value");
        newzgrabber.updateIni("iniFile3", "prop", "key", "value");
    }

    @Test
    public void testWindowClosed() {
        Newzgrabber newzgrabber = new Newzgrabber();

        newzgrabber.windowClosed(null);
    }

    @Test
    public void testWindowOpened() {
        Newzgrabber newzgrabber = new Newzgrabber();

        newzgrabber.windowOpened(null);
    }

    @Test
    public void testWindowActivated() {
        Newzgrabber newzgrabber = new Newzgrabber();

        newzgrabber.windowActivated(null);
    }

    @Test
    public void testWindowDeactivated() {
        Newzgrabber newzgrabber = new Newzgrabber();

        newzgrabber.windowDeactivated(null);
    }

    @Test
    public void testWindowIconified() {
        Newzgrabber newzgrabber = new Newzgrabber();

        newzgrabber.windowIconified(null);
    }

    @Test
    public void testWindowDeiconified() {
        Newzgrabber newzgrabber = new Newzgrabber();

        newzgrabber.windowDeiconified(null);
    }

    @Test
    public void testWindowClosing() {
        Newzgrabber newzgrabber = new Newzgrabber();

        newzgrabber.windowClosing(null);
    }

    @Test
    public void testSetupBasicSystem() {
        Newzgrabber.setupBasicSystem();
    }

    @Test
    public void testNewzgrabberString() {
        Newzgrabber newzgrabber = new Newzgrabber("Title");
    }

    @Test
    public void testMain() {
        Newzgrabber.main(new String[0]);
    }
}