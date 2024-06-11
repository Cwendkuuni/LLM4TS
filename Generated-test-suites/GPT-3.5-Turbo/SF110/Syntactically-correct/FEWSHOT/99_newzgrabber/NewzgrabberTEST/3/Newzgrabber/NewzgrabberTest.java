package Newzgrabber;

import static org.junit.Assert.*;
import org.junit.Test;

public class NewzgrabberTest {
    
    @Test
    public void testFindDirectory() {
        assertEquals(null, Newzgrabber.findDirectory(""));
        assertEquals(null, Newzgrabber.findDirectory("test.txt"));
    }
    
    @Test
    public void testUpdateIni() {
        Newzgrabber ng = new Newzgrabber();
        ng.updateIni("test.ini", "prop", "key", "value");
    }
    
    @Test
    public void testWindowClosing() {
        Newzgrabber ng = new Newzgrabber();
        ng.windowClosing(null);
    }
}