
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Vector;

public class NewzgrabberTest {

    @Test
    public void testNewzgrabber() {
        Newzgrabber ng = new Newzgrabber();
        assertNotNull(ng);
    }

    @Test
    public void testNewzgrabberString() {
        Newzgrabber ng = new Newzgrabber("Newzgrabber");
        assertNotNull(ng);
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
    public void testSetupBasicSystem() {
        Newzgrabber ng = new Newzgrabber();
        ng.setupBasicSystem();
    }

    @Test
    public void testFindDirectory() {
        String classfile = "Newz.jar";
        Newzgrabber ng = new Newzgrabber();
        String directory = ng.findDirectory(classfile);
        assertNotNull(directory);
    }

    @Test
    public void testUpdateIni() {
        Newzgrabber ng = new Newzgrabber();
        ng.updateIni("inifile", "prop", "key", "value");
    }

    @Test
    public void testMain() {
        String[] args = new String[1];
        args[0] = "-verbose";
        Newzgrabber.main(args);
    }
}
