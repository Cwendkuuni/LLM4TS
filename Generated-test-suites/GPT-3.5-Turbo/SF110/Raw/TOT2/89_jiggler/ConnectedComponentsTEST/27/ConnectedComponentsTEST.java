package jigl.image.ops;

import jigl.image.types.*;
import jigl.image.*;
import jigl.image.exceptions.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ConnectedComponentsTest {

    @Test
    public void testConnectedComponentsConstructor() {
        try {
            ConnectedComponents cc = new ConnectedComponents(1, 0);
        } catch (InvalidArgumentException e) {
            fail("Invalid argument exception thrown");
        }
    }
    
    @Test
    public void testConnectedComponentsApply() {
        BinaryImage image = new BinaryImage(3, 3);
        image.set(0, 0, 1);
        image.set(0, 1, 0);
        image.set(0, 2, 0);
        image.set(1, 0, 0);
        image.set(1, 1, 1);
        image.set(1, 2, 0);
        image.set(2, 0, 0);
        image.set(2, 1, 0);
        image.set(2, 2, 1);
        
        ConnectedComponents cc = new ConnectedComponents(1, 0);
        GrayImage result = cc.apply(image);
        
        // Add assertions for the result
    }
}