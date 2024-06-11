package jigl.image.ops;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ConnectedComponentsTest {

    @Test
    public void testApply() {
        BinaryImage image = new BinaryImage(3, 3);
        
        image.set(0, 0, true);
        image.set(0, 1, true);
        image.set(0, 2, true);
        image.set(1, 0, true);
        image.set(1, 1, false);
        image.set(1, 2, true);
        image.set(2, 0, true);
        image.set(2, 1, true);
        image.set(2, 2, true);
        
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        GrayImage result = cc.apply(image);
        
        // Check the size of the resulting image
        assertEquals(3, result.X());
        assertEquals(3, result.Y());
        
        // Check the values of the resulting image
        assertEquals(1, result.get(0, 0));
        assertEquals(1, result.get(0, 1));
        assertEquals(1, result.get(0, 2));
        assertEquals(2, result.get(1, 0));
        assertEquals(0, result.get(1, 1));
        assertEquals(3, result.get(1, 2));
        assertEquals(4, result.get(2, 0));
        assertEquals(4, result.get(2, 1));
        assertEquals(4, result.get(2, 2));
    }
}