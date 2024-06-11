package Newzgrabber;

import static org.junit.Assert.*;
import org.junit.Test;

public class NewzgrabberTest {
    
    @Test
    public void testSetupBasicSystem() {
        // Test 1
        assertEquals("C:/Program Files/Newzgrabber", Newzgrabber.findDirectory("Newz.jar"));
        // Test 2
        assertNull(Newzgrabber.findDirectory("File.jar"));
    }
    
    @Test
    public void testWindowClosing() {
        Newzgrabber newzgrabber = new Newzgrabber();
        newzgrabber.windowClosing(null);
        // ADD ASSERTION(S) HERE
        
    }
    
    // Add more test cases for other methods
    
}