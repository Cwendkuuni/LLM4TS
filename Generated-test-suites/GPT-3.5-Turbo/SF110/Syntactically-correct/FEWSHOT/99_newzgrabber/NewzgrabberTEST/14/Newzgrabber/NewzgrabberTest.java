
package Newzgrabber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.Test;

public class NewzgrabberTest {

	@Test
    public void testNewzgrabber() {
		Newzgrabber n = new Newzgrabber();
		assertNotNull(n.Newzgrabber.IsBatch);
	}

    @Test
    public void testWindowClosed() {
        Newzgrabber n = new Newzgrabber();
        n.windowClosed(mock(WindowEvent.class));
    }

    @Test
    public void testWindowOpened() {
        Newzgrabber n = new Newzgrabber();
        n.windowOpened(mock(WindowEvent.class));
    }

    @Test
    public void testWindowActivated() {
        Newzgrabber n = new Newzgrabber();
        n.windowActivated(mock(WindowEvent.class));
    }

    @Test
    public void testWindowDeactivated() {
        Newzgrabber n = new Newzgrabber();
        n.windowDeactivated(mock(WindowEvent.class));
    }

    @Test
    public void testWindowIconified() {
        Newzgrabber n = new Newzgrabber();
        n.windowIconified(mock(WindowEvent.class));
    }

    @Test
    public void testWindowDeiconified() {
        Newzgrabber n = new Newzgrabber();
        n.windowDeiconified(mock(WindowEvent.class));
    }

    @Test
    public void testWindowClosing() {
        Newzgrabber n = new Newzgrabber();
        n.windowClosing(mock(WindowEvent.class));
    }

    @Test
    public void testFindDirectory() {
        String result = Newzgrabber.findDirectory("Newz.jar");
        assertNull(result);

        result = Newzgrabber.findDirectory("test.jar");
        assertNull(result);
    }

    @Test
    public void testUpdateIni() {
        Newzgrabber n = new Newzgrabber();
        n.updateIni("test.ini", "prop", "key", "value");
    }

    @Test
    public void testMain() {
        Newzgrabber.main(new String[]{});
        Newzgrabber.main(new String[]{"-verbose"});
        Newzgrabber.main(new String[]{"-batch"});
        Newzgrabber.main(new String[]{"-debug"});
    }
}
