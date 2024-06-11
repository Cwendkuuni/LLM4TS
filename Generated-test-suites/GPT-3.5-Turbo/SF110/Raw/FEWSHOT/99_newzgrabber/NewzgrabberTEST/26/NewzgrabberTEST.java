package Newzgrabber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.LinkedList;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

public class NewzgrabberTest {

    private Newzgrabber newzgrabber;

    @Before
    public void setUp() {
        newzgrabber = new Newzgrabber();
    }

    @Test
    public void testNewzgrabber() {
        assertNotNull(newzgrabber);
        assertTrue(Newzgrabber.IsBatch);
        assertNotNull(Newzgrabber.optionspanel);
        assertNotNull(Newzgrabber.groupspanel);
        assertNotNull(Newzgrabber.searchpanel);
        assertNotNull(Newzgrabber.transferpanel);
        assertNotNull(Newzgrabber.nf);
    }

    @Test
    public void testNewzgrabberString() {
        String s = "Newzgrabber Title";
        Newzgrabber ng = new Newzgrabber(s);
        assertNotNull(ng);
        assertFalse(Newzgrabber.IsBatch);
    }

    @Test
    public void testSetupBasicSystem() {
        Newzgrabber.setupBasicSystem();
        assertNotNull(Newzgrabber.Newzdirectory);
    }

    @Test
    public void testWindowClosed() {
        newzgrabber.windowClosed(null);
    }

    @Test
    public void testWindowOpened() {
        newzgrabber.windowOpened(null);
    }

    @Test
    public void testWindowActivated() {
        newzgrabber.windowActivated(null);
    }

    @Test
    public void testWindowDeactivated() {
        newzgrabber.windowDeactivated(null);
    }

    @Test
    public void testWindowIconified() {
        newzgrabber.windowIconified(null);
    }

    @Test
    public void testWindowDeiconified() {
        newzgrabber.windowDeiconified(null);
    }

    @Test
    public void testWindowClosing() {
        newzgrabber.windowClosing(null);
    }

    @Test
    public void testFindDirectory() {
        String classfile = "Newzgrabber.class";
        String result = Newzgrabber.findDirectory(classfile);
        assertNotNull(result);
    }

    @Test
    public void testUpdateIni() {
        String inifile = "filename.ini";
        String prop = "property";
        String key = "key";
        String value = "value";
        newzgrabber.updateIni(inifile, prop, key, value);
    }

    @Test
    public void testMain() {
        String[] args = new String[1];
        args[0] = "-verbose";
        Newzgrabber.main(args);
    }
}