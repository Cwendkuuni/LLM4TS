
package Newzgrabber;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Vector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class NewzgrabberTest {

    @Mock
    JFrame jFrameMock;

    @Before
    public void setUp() {
        Newzgrabber.newzdirectory = "C:\\Newz";
        Newzgrabber.JarFilename = "Newz.jar";
        Newzgrabber.BatchFilename = "batch.ini";
        Newzgrabber.NewzInifilename = "Newzgrabber.ini";
    }

    @Test
    public void testNewzgrabber() {
        Newzgrabber newzgrabber = new Newzgrabber();
        Mockito.verify(jFrameMock, Mockito.times(1)).addWindowListener(newzgrabber);
    }

    @Test
    public void testNewzgrabberWithTitle() {
        String title = "Newzgrabber";
        Newzgrabber newzgrabber = new Newzgrabber(title);
        assertEquals(title, newzgrabber.getTitle());
        Mockito.verify(jFrameMock, Mockito.times(1)).addWindowListener(newzgrabber);
    }

    @Test
    public void testSetupBasicSystem() {
        Newzgrabber.setupBasicSystem();
        assertNull(System.getProperty("java.library.path"));
    }

    @Test
    public void testWindowClosed() {
        Newzgrabber newzgrabber = new Newzgrabber();
        newzgrabber.windowClosed(new WindowEvent(newzgrabber, WindowEvent.WINDOW_CLOSED));
    }

    @Test
    public void testWindowOpened() {
        Newzgrabber newzgrabber = new Newzgrabber();
        newzgrabber.windowOpened(new WindowEvent(newzgrabber, WindowEvent.WINDOW_OPENED));
    }

    @Test
    public void testWindowActivated() {
        Newzgrabber newzgrabber = new Newzgrabber();
        newzgrabber.windowActivated(new WindowEvent(newzgrabber, WindowEvent.WINDOW_ACTIVATED));
    }

    @Test
    public void testWindowDeactivated() {
        Newzgrabber newzgrabber = new Newzgrabber();
        newzgrabber.windowDeactivated(new WindowEvent(newzgrabber, WindowEvent.WINDOW_DEACTIVATED));
    }

    @Test
    public void testWindowIconified() {
        Newzgrabber newzgrabber = new Newzgrabber();
        newzgrabber.windowIconified(new WindowEvent(newzgrabber, WindowEvent.WINDOW_ICONIFIED));
    }

    @Test
    public void testWindowDeiconified() {
        Newzgrabber newzgrabber = new Newzgrabber();
        newzgrabber.windowDeiconified(new WindowEvent(newzgrabber, WindowEvent.WINDOW_DEICONIFIED));
    }

    @Test
    public void testWindowClosing() {
        Newzgrabber newzgrabber = new Newzgrabber();
        newzgrabber.windowClosing(new WindowEvent(newzgrabber, WindowEvent.WINDOW_CLOSING));
        Mockito.verify(jFrameMock, Mockito.times(1)).addWindowListener(newzgrabber);
    }

    @Test
    public void testFindDirectory() {
        String classfile = "Newz.jar";
        String expectedDirectory = "C:\\Newz";
        String directory = Newzgrabber.findDirectory(classfile);
        assertEquals(expectedDirectory, directory);
    }

    @Test
    public void testUpdateIni() {
        String inifile = "Newzgrabber.ini";
        String prop = "Messageids";
        String key = "MessageId";
        String value = "12345";
        Newzgrabber newzgrabber = new Newzgrabber();
        newzgrabber.updateIni(inifile, prop, key, value);
        Newzgrabber.Ini iniMock = Mockito.mock(Newzgrabber.Ini.class);
        Mockito.when(iniMock.getIniVector()).thenReturn(new Vector<>());
        Mockito.when(iniMock.writeIni(Mockito.any(LinkedList.class))).thenReturn(null);
        Mockito.when(newzgrabber.updateIni(inifile, prop, key, value)).thenReturn(null);
        Mockito.verify(iniMock, Mockito.times(1)).writeIni(Mockito.any(LinkedList.class));
    }

    @Test
    public void testMain() {
        Newzgrabber.main(new String[]{});
    }
}
