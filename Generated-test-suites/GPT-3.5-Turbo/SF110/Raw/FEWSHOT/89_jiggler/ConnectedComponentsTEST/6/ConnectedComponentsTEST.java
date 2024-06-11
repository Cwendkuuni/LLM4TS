package jigl.image.ops;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ConnectedComponentsTest {
    
    @Test
    public void testConstructor() {
        try {
            ConnectedComponents cc = new ConnectedComponents(0, 1);
        } catch (InvalidArgumentException e) {
            // Test passed
        }
        
        try {
            ConnectedComponents cc = new ConnectedComponents(1, 2);
        } catch (InvalidArgumentException e) {
            // Test passed
        }
        
        try {
            ConnectedComponents cc = new ConnectedComponents(1, 0);
        } catch (InvalidArgumentException e) {
            // Test passed
        }
        
        // Test passed
        ConnectedComponents cc = new ConnectedComponents(0, 0);
    }
    
    @Test
    public void testApply() {
        ConnectedComponents cc1 = new ConnectedComponents(0, 0);
        BinaryImage image = new BinaryImage(3, 3);
        image.set(0, 0, 1);
        image.set(0, 1, 1);
        image.set(1, 0, 0);
        image.set(1, 1, 0);
        image.set(2, 0, 0);
        image.set(2, 1, 0);
        image.set(2, 2, 1);
        
        GrayImage result = cc1.apply(image);
        assertEquals(1, result.get(0, 0));
        assertEquals(1, result.get(0, 1));
        assertEquals(2, result.get(2, 2));
    }
    
}