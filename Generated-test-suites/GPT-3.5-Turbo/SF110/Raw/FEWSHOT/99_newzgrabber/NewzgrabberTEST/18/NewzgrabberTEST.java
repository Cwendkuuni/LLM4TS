
package Newzgrabber;

import static org.junit.Assert.*;
import org.junit.Test;

public class NewzgrabberTest {
    
    @Test
    public void testConstructor() {
        Newzgrabber n = new Newzgrabber();
        assertNotNull(n);
    }
    
    @Test
    public void testFindDirectory() {
        String classfile = "Newz.jar";
        String directory = Newzgrabber.findDirectory(classfile);
        assertNotNull(directory);
    }
    
    @Test
    public void testSetupBasicSystem() {
        Newzgrabber.setupBasicSystem();
        assertNotNull(Newzgrabber.Newzdirectory);
        assertNotNull(Newzgrabber.nf);
    }
    
    @Test
    public void testUpdateIni() {
        String inifile = "Newzgrabber.ini";
        String prop = "prop";
        String key = "key";
        String value = "value";
        Newzgrabber n = new Newzgrabber();
        n.updateIni(inifile, prop, key, value);
    }
    
    @Test
    public void testWindowClosed() {
        Newzgrabber n = new Newzgrabber();
        n.windowClosed(null);
    }
    
    @Test
    public void testWindowOpened() {
        Newzgrabber n = new Newzgrabber();
        n.windowOpened(null);
    }
    
    @Test
    public void testWindowActivated() {
        Newzgrabber n = new Newzgrabber();
        n.windowActivated(null);
    }
    
    @Test
    public void testWindowDeactivated() {
        Newzgrabber n = new Newzgrabber();
        n.windowDeactivated(null);
    }
    
    @Test
    public void testWindowIconified() {
        Newzgrabber n = new Newzgrabber();
        n.windowIconified(null);
    }
    
    @Test
    public void testWindowDeiconified() {
        Newzgrabber n = new Newzgrabber();
        n.windowDeiconified(null);
    }
    
    @Test
    public void testWindowClosing() {
        Newzgrabber n = new Newzgrabber();
        n.windowClosing(null);
    }
    
    @Test
    public void testMain() {
        String[] args = new String[1];
        args[0] = "-verbose";
        Newzgrabber.main(args);
    }
}
