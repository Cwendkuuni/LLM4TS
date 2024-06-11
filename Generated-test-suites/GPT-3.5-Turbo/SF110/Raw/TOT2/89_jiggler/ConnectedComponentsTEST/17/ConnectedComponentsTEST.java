package jigl.image.ops;

import org.junit.Test;
import static org.junit.Assert.*;

public class ConnectedComponentsTest {

    @Test
    public void testConnectedComponentsConstructorValid() {
        try {
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
            assertEquals(ConnectedComponents.FOUR_CONNECTED, cc.neighbors);
            assertEquals(0, cc.background);
        } catch (InvalidArgumentException e) {
            fail("Exception thrown when it shouldn't have");
        }
    }

    @Test
    public void testConnectedComponentsConstructorInvalid() {
        try {
            ConnectedComponents cc = new ConnectedComponents(2, 0);
            fail("Invalid argument exception not thrown");
        } catch (InvalidArgumentException e) {
            // Exception should be thrown
        }
    }

    @Test
    public void testApplyFourConnected() {
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        BinaryImage imageIn = new BinaryImage(3, 3);
        imageIn.set(1, 1, 1);
        imageIn.set(0, 2, 1);
        imageIn.set(2, 2, 1);
        GrayImage result = cc.apply(imageIn);
        assertEquals(1, result.get(1, 1));
        assertEquals(2, result.get(0, 2));
        assertEquals(3, result.get(2, 2));
    }
    
    @Test
    public void testApplyEightConnected() {
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);
        BinaryImage imageIn = new BinaryImage(3, 3);
        imageIn.set(1, 1, 1);
        imageIn.set(0, 2, 1);
        imageIn.set(2, 2, 1);
        GrayImage result = cc.apply(imageIn);
        assertEquals(1, result.get(1, 1));
        assertEquals(2, result.get(0, 2));
        assertEquals(3, result.get(2, 2));
    }

    // Add more test cases as needed for edge cases and error scenarios
    
}