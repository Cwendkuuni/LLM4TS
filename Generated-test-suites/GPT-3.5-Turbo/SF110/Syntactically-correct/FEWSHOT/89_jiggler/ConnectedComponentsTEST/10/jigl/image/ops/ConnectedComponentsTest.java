package jigl.image.ops;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class ConnectedComponentsTest {
    
    @Test
    public void testConstructor() {
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        assertNotNull(cc);
    }
    
    @Test(expected = InvalidArgumentException.class)
    public void testInvalidConstructor() {
        ConnectedComponents cc = new ConnectedComponents(2, 3);
    }
    
    @Test
    public void testApply() {
        BinaryImage image = new BinaryImage(10, 10);
        image.set(0, 0, 1);
        image.set(0, 1, 1);
        image.set(0, 2, 0);
        image.set(1, 0, 1);
        image.set(1, 1, 0);
        image.set(1, 2, 0);
        image.set(2, 0, 1);
        
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        GrayImage result = cc.apply(image);
        
        assertEquals(1, result.get(0, 0));
        assertEquals(1, result.get(0, 1));
        assertEquals(0, result.get(0, 2));
        assertEquals(1, result.get(1, 0));
        assertEquals(0, result.get(1, 1));
        assertEquals(0, result.get(1, 2));
        assertEquals(1, result.get(2, 0));
    }
}