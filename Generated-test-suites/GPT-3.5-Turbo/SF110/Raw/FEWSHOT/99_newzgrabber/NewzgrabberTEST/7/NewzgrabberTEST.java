package Newzgrabber;

import static org.junit.Assert.*;
import org.junit.Test;

public class NewzgrabberTest {

    @Test
    public void testFindDirectory() {
        assertEquals(null, Newzgrabber.findDirectory(""));
        assertEquals(null, Newzgrabber.findDirectory(null));
        assertEquals(null, Newzgrabber.findDirectory("abc.jar"));
        assertEquals(null, Newzgrabber.findDirectory("def.jar"));
        assertEquals(null, Newzgrabber.findDirectory("ghi.jar"));
    }

    @Test
    public void testUpdateIni() {
        Newzgrabber newzgrabber = new Newzgrabber();
        newzgrabber.updateIni("", "", "", "");
        newzgrabber.updateIni(null, null, null, null);
        newzgrabber.updateIni("abc.ini", "prop1", "key1", "value1");
        newzgrabber.updateIni("def.ini", "prop2", "key2", "value2");
        newzgrabber.updateIni("ghi.ini", "prop3", "key3", "value3");
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
    public void testMain() {
        Newzgrabber.main(new String[] {"-verbose"});
        Newzgrabber.main(new String[] {"-batch"});
        Newzgrabber.main(new String[] {"-debug"});
        Newzgrabber.main(new String[] {"-batch"});
        Newzgrabber.main(new String[] {});
    }
}