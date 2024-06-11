package jigl.image.ops;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ConnectedComponentsTest {

    @Test
    public void testConstructorValidArguments() {
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        assertEquals(ConnectedComponents.FOUR_CONNECTED, cc.neighbors);
        assertEquals(0, cc.background);
    }
    
    @Test(expected = InvalidArgumentException.class)
    public void testConstructorInvalidArguments() {
        new ConnectedComponents(2, -1);
    }
    
    @Test
    public void testApply() {
        BinaryImage image = new BinaryImage(3, 3);
        image.set(0, 0, 1);
        image.set(0, 1, 0);
        image.set(1, 0, 1);
        image.set(1, 1, 1);
        image.set(2, 2, 1);
        
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);
        GrayImage result = cc.apply(image);
        
        assertEquals(1, result.get(0, 0).intValue());
        assertEquals(1, result.get(0, 1).intValue());
        assertEquals(2, result.get(1, 0).intValue());
        assertEquals(2, result.get(1, 1).intValue());
        assertEquals(3, result.get(2, 2).intValue());
    }
    
    @Test
    public void testApplyEdgeCases() {
        // Test edge cases here
    }
    
    @Test
    public void testApplyExceptions() {
        // Test exception handling scenarios here
    }
}