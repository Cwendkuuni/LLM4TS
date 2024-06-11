package Newzgrabber;

import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;
import java.util.*;

public class NewzgrabberTest {

    @Test
    public void testNewzgrabberConstructor() {
        Newzgrabber grabber = new Newzgrabber();
        assertNotNull(grabber);
    }

    @Test
    public void testWindowClosed() {
        Newzgrabber grabber = new Newzgrabber();
        WindowEvent event = new WindowEvent(new JFrame(), WindowEvent.WINDOW_CLOSED);
        grabber.windowClosed(event);
    }

    @Test
    public void testWindowOpened() {
        Newzgrabber grabber = new Newzgrabber();
        WindowEvent event = new WindowEvent(new JFrame(), WindowEvent.WINDOW_OPENED);
        grabber.windowOpened(event);
    }

    @Test
    public void testWindowActivated() {
        Newzgrabber grabber = new Newzgrabber();
        WindowEvent event = new WindowEvent(new JFrame(), WindowEvent.WINDOW_ACTIVATED);
        grabber.windowActivated(event);
    }

    @Test
    public void testWindowDeactivated() {
        Newzgrabber grabber = new Newzgrabber();
        WindowEvent event = new WindowEvent(new JFrame(), WindowEvent.WINDOW_DEACTIVATED);
        grabber.windowDeactivated(event);
    }

    @Test
    public void testWindowIconified() {
        Newzgrabber grabber = new Newzgrabber();
        WindowEvent event = new WindowEvent(new JFrame(), WindowEvent.WINDOW_ICONIFIED);
        grabber.windowIconified(event);
    }

    @Test
    public void testWindowDeiconified() {
        Newzgrabber grabber = new Newzgrabber();
        WindowEvent event = new WindowEvent(new JFrame(), WindowEvent.WINDOW_DEICONIFIED);
        grabber.windowDeiconified(event);
    }

    @Test
    public void testWindowClosing() {
        Newzgrabber grabber = new Newzgrabber();
        WindowEvent event = new WindowEvent(new JFrame(), WindowEvent.WINDOW_CLOSING);
        grabber.windowClosing(event);
    }

    @Test
    public void testFindDirectory() {
        String classfile = "Newz.jar";
        String directory = Newzgrabber.findDirectory(classfile);
        assertNotNull(directory);
    }

    @Test
    public void testUpdateIni() {
        Newzgrabber grabber = new Newzgrabber();
        String inifile = "Newzgrabber.ini";
        String prop = "prop";
        String key = "key";
        String value = "value";
        grabber.updateIni(inifile, prop, key, value);
    }

    @Test
    public void testMain() {
        String[] args = {};
        Newzgrabber.main(args);
    }
}