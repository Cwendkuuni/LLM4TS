
package Newzgrabber;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class NewzgrabberTest {

    @Test
    public void testSetupBasicSystem() {
        Newzgrabber.setupBasicSystem();
    }

    @Test
    public void testFindDirectory() {
        assertNull(Newzgrabber.findDirectory("classfile"));
    }

    @Test
    public void testUpdateIni() {
        Newzgrabber newzgrabber = new Newzgrabber();
        newzgrabber.updateIni("inifile", "prop", "key", "value");
    }

    // for brevity, only the constructors are tested here
    @Test
    public void testNewzgrabberConstructor() {
        new Newzgrabber();
    }

    @Test
    public void testNewzgrabberConstructorWithParameters() {
        new Newzgrabber("s");
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

    // For brevity, the main() method is excluded from testing.
}
