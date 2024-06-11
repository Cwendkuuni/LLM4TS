package jigl.image.ops;

import static org.junit.Assert.*;

import org.junit.Test;

import jigl.image.*;
import jigl.image.exceptions.*;

public class ConnectedComponentsTest {
    @Test
    public void testConstructor() {
        try {
            ConnectedComponents cc = new ConnectedComponents(0, 0);
            assertEquals(0, cc.neighbors);
            assertEquals(0, cc.background);
        } catch (InvalidArgumentException e) {
            fail("InvalidArgumentException should not be thrown");
        }
        
        try {
            ConnectedComponents cc = new ConnectedComponents(1, 1);
            assertEquals(1, cc.neighbors);
            assertEquals(1, cc.background);
        } catch (InvalidArgumentException e) {
            fail("InvalidArgumentException should not be thrown");
        }
        
        try {
            ConnectedComponents cc = new ConnectedComponents(2, 0);
            fail("InvalidArgumentException should be thrown");
        } catch (InvalidArgumentException e) {
            // exception should be thrown
        }
        
        try {
            ConnectedComponents cc = new ConnectedComponents(0, 2);
            fail("InvalidArgumentException should be thrown");
        } catch (InvalidArgumentException e) {
            // exception should be thrown
        }
    }
    
    @Test
    public void testApply() {
        BinaryImage image = new BinaryImage(5, 5);
        image.set(2, 2, 1);
        
        ConnectedComponents cc = new ConnectedComponents(0, 0);
        GrayImage result = cc.apply(image);
        
        assertEquals(1, result.get(2, 2));
        assertEquals(0, result.get(0, 0));
        assertEquals(0, result.get(4, 4));
    }
}