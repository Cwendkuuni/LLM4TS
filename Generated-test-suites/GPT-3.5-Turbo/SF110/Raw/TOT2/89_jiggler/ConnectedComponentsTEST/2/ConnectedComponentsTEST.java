package jigl.image.ops;

import static org.junit.Assert.*;
import org.junit.Test;

public class ConnectedComponentsTest {

    @Test
    public void testConstructor() {
        try {
            ConnectedComponents cc = new ConnectedComponents(0, 1);
            assertEquals(0, cc.neighbors);
            assertEquals(1, cc.background);
        } catch (InvalidArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testApply() {
        BinaryImage image = new BinaryImage(10, 10);
        image.set(3, 5, 1);
        ConnectedComponents cc = new ConnectedComponents(0, 0);
        GrayImage result = cc.apply(image);
        assertEquals(1, result.get(3, 5).intValue());
    }
}