package jigl.image.ops;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import jigl.image.BinaryImage;
import jigl.image.GrayImage;
import jigl.image.exceptions.InvalidArgumentException;
import jigl.image.ops.ConnectedComponents;

public class ConnectedComponentsTest {
    
    @Test
    public void testConstructor() {
        try {
            ConnectedComponents cc = new ConnectedComponents(0, 0);
            assertNotNull(cc);
        } catch (InvalidArgumentException e) {
            assertNull(e);
        }
    }
    
    @Test
    public void testApply() {
        BinaryImage image = new BinaryImage(2, 2);
        image.set(0, 0, 0);
        image.set(0, 1, 0);
        image.set(1, 0, 1);
        image.set(1, 1, 1);
        
        ConnectedComponents cc = new ConnectedComponents(0, 0);
        GrayImage result = cc.apply(image);
        
        assertEquals(0, result.get(0, 0).intValue());
        assertEquals(0, result.get(0, 1).intValue());
        assertEquals(1, result.get(1, 0).intValue());
        assertEquals(1, result.get(1, 1).intValue());
    }
}