package jigl.image.ops;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import jigl.image.BinaryImage;
import jigl.image.GrayImage;
import jigl.image.types.*;
import jigl.image.exceptions.InvalidArgumentException;

import org.junit.Test;

public class ConnectedComponentsTest {
    
    @Test
    public void testConstructor() throws InvalidArgumentException {
        ConnectedComponents cc1 = new ConnectedComponents(0, 0);
        assertEquals(0, cc1.neighbors);
        assertEquals(0, cc1.background);
        
        ConnectedComponents cc2 = new ConnectedComponents(1, 1);
        assertEquals(1, cc2.neighbors);
        assertEquals(1, cc2.background);
        
        ConnectedComponents cc3 = new ConnectedComponents(2, 0);
        assertEquals(2, cc3.neighbors);
        assertEquals(0, cc3.background);
        
        ConnectedComponents cc4 = new ConnectedComponents(0, 1);
        assertEquals(0, cc4.neighbors);
        assertEquals(1, cc4.background);
    }
    
    @Test(expected = InvalidArgumentException.class)
    public void testConstructorInvalid() throws InvalidArgumentException {
        ConnectedComponents cc = new ConnectedComponents(3, 2);
    }
    
    @Test
    public void testApply() {
        BinaryImage image = new BinaryImage(5, 5);
        image.set(0, 0, 1);
        image.set(1, 0, 0);
        image.set(2, 0, 1);
        image.set(3, 0, 0);
        image.set(4, 0, 1);
        
        image.set(0, 1, 0);
        image.set(1, 1, 0);
        image.set(2, 1, 0);
        image.set(3, 1, 0);
        image.set(4, 1, 0);
        
        image.set(0, 2, 1);
        image.set(1, 2, 0);
        image.set(2, 2, 1);
        image.set(3, 2, 0);
        image.set(4, 2, 1);
        
        image.set(0, 3, 0);
        image.set(1, 3, 0);
        image.set(2, 3, 0);
        image.set(3, 3, 0);
        image.set(4, 3, 0);
        
        image.set(0, 4, 1);
        image.set(1, 4, 0);
        image.set(2, 4, 1);
        image.set(3, 4, 0);
        image.set(4, 4, 1);
        
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        GrayImage result = cc.apply(image);
        
        assertNotNull(result);
        assertEquals(5, result.X());
        assertEquals(5, result.Y());
        
        assertEquals(1, result.get(0, 0));
        assertEquals(2, result.get(1, 0));
        assertEquals(1, result.get(2, 0));
        assertEquals(3, result.get(3, 0));
        assertEquals(4, result.get(4, 0));
        
        assertEquals(0, result.get(0, 1));
        assertEquals(0, result.get(1, 1));
        assertEquals(0, result.get(2, 1));
        assertEquals(0, result.get(3, 1));
        assertEquals(0, result.get(4, 1));
        
        assertEquals(5, result.get(0, 2));
        assertEquals(0, result.get(1, 2));
        assertEquals(6, result.get(2, 2));
        assertEquals(0, result.get(3, 2));
        assertEquals(7, result.get(4, 2));
        
        assertEquals(0, result.get(0, 3));
        assertEquals(0, result.get(1, 3));
        assertEquals(0, result.get(2, 3));
        assertEquals(0, result.get(3, 3));
        assertEquals(0, result.get(4, 3));
        
        assertEquals(8, result.get(0, 4));
        assertEquals(0, result.get(1, 4));
        assertEquals(9, result.get(2, 4));
        assertEquals(0, result.get(3, 4));
        assertEquals(10, result.get(4, 4));
    }
}