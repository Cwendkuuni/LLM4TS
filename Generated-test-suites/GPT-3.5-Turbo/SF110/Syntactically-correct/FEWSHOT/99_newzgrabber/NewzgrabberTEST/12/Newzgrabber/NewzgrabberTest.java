package Newzgrabber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NewzgrabberTest {
    
    @Test
    public void testSetupBasicSystem() {
        Newzgrabber.setupBasicSystem();
    }
    
    @Test
    public void testWindowClosed() {
        Newzgrabber ng = new Newzgrabber();
        ng.windowClosed(null);
    }
    
    @Test
    public void testWindowOpened() {
        Newzgrabber ng = new Newzgrabber();
        ng.windowOpened(null);
    }
    
    @Test
    public void testWindowActivated() {
        Newzgrabber ng = new Newzgrabber();
        ng.windowActivated(null);
    }
    
    @Test
    public void testWindowDeactivated() {
        Newzgrabber ng = new Newzgrabber();
        ng.windowDeactivated(null);
    }
    
    @Test
    public void testWindowIconified() {
        Newzgrabber ng = new Newzgrabber();
        ng.windowIconified(null);
    }
    
    @Test
    public void testWindowDeiconified() {
        Newzgrabber ng = new Newzgrabber();
        ng.windowDeiconified(null);
    }
    
    @Test
    public void testWindowClosing() {
        Newzgrabber ng = new Newzgrabber();
        ng.windowClosing(null);
    }
    
    @Test
    public void testFindDirectory() {
        Newzgrabber.findDirectory("Newz.jar");
    }
    
    @Test
    public void testUpdateIni() {
        Newzgrabber ng = new Newzgrabber();
        ng.updateIni("newzgrabber.ini", "testProp", "testKey", "testValue");
    }
    
    @Test
    public void testMain() {
        Newzgrabber.main(new String[] {});
        Newzgrabber.main(new String[] {"-verbose"});
        Newzgrabber.main(new String[] {"-batch"});
        Newzgrabber.main(new String[] {"-debug"});
        Newzgrabber.main(new String[] {"-verbose", "-batch", "-debug"});
    }
}