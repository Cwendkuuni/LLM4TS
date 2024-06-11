
package jigl.image.ops;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

import jigl.image.BinaryImage;
import jigl.image.GrayImage;
import jigl.image.exceptions.InvalidArgumentException;

public class ConnectedComponentsTest {
    
    private ConnectedComponents cc;
    
    @Before
    public void setUp() throws InvalidArgumentException {
        cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
    }
    
    @Test
    public void testApply_fourConnected() {
        BinaryImage image = new BinaryImage(3, 3);
        image.set(0, 0, 1);
        image.set(0, 1, 0);
        image.set(0, 2, 1);
        image.set(1, 0, 0);
        image.set(1, 1, 1);
        image.set(1, 2, 1);
        image.set(2, 0, 1);
        image.set(2, 1, 1);
        image.set(2, 2, 0);
        
        GrayImage result = cc.apply(image);
        
        assertEquals(1, result.get(0, 0));
        assertEquals(0, result.get(0, 1));
        assertEquals(2, result.get(0, 2));
        assertEquals(0, result.get(1, 0));
        assertEquals(3, result.get(1, 1));
        assertEquals(3, result.get(1, 2));
        assertEquals(4, result.get(2, 0));
        assertEquals(4, result.get(2, 1));
        assertEquals(0, result.get(2, 2));
    }
    
    @Test
    public void testApply_eightConnected() {
        cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 1);
        
        BinaryImage image = new BinaryImage(3, 3);
        image.set(0, 0, 1);
        image.set(0, 1, 0);
        image.set(0, 2, 1);
        image.set(1, 0, 0);
        image.set(1, 1, 1);
        image.set(1, 2, 1);
        image.set(2, 0, 1);
        image.set(2, 1, 1);
        image.set(2, 2, 0);
        
        GrayImage result = cc.apply(image);
        
        assertEquals(1, result.get(0, 0));
        assertEquals(0, result.get(0, 1));
        assertEquals(2, result.get(0, 2));
        assertEquals(0, result.get(1, 0));
        assertEquals(2, result.get(1, 1));
        assertEquals(2, result.get(1, 2));
        assertEquals(3, result.get(2, 0));
        assertEquals(3, result.get(2, 1));
        assertEquals(0, result.get(2, 2));
    }
}
