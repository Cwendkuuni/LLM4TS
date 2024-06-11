package jigl.image.ops;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ConnectedComponentsTest {

    @Test
    public void testConstructor() {
        try {
            ConnectedComponents cc = new ConnectedComponents(0, 0);
            assertEquals(0, cc.neighbors);
            assertEquals(0, cc.background);
            
            cc = new ConnectedComponents(1, 1);
            assertEquals(1, cc.neighbors);
            assertEquals(1, cc.background);
            
            cc = new ConnectedComponents(0, 1);
            assertEquals(0, cc.neighbors);
            assertEquals(1, cc.background);
            
            cc = new ConnectedComponents(1, 0);
            assertEquals(1, cc.neighbors);
            assertEquals(0, cc.background);
            
        } catch (InvalidArgumentException e) {
            assertNull(e);
        }
    }

    @Test(expected = InvalidArgumentException.class)
    public void testInvalidConstructor() throws InvalidArgumentException {
        ConnectedComponents cc = new ConnectedComponents(2, 1);
    }

    @Test
    public void testApplyFourConnected() {
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);

        BinaryImage image = new BinaryImage(3, 3);
        image.set(0, 0, 1);
        image.set(0, 1, 0);
        image.set(0, 2, 1);
        image.set(1, 0, 0);
        image.set(1, 1, 1);
        image.set(1, 2, 0);
        image.set(2, 0, 1);
        image.set(2, 1, 0);
        image.set(2, 2, 1);

        GrayImage result = cc.apply(image);

        GrayImage expected = new GrayImage(3, 3);
        expected.set(0, 0, 0);
        expected.set(0, 1, 1);
        expected.set(0, 2, 2);
        expected.set(1, 0, 3);
        expected.set(1, 1, 4);
        expected.set(1, 2, 5);
        expected.set(2, 0, 6);
        expected.set(2, 1, 7);
        expected.set(2, 2, 8);

        assertEquals(expected, result);
    }

    @Test
    public void testApplyEightConnected() {
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);

        BinaryImage image = new BinaryImage(3, 3);
        image.set(0, 0, 1);
        image.set(0, 1, 0);
        image.set(0, 2, 1);
        image.set(1, 0, 0);
        image.set(1, 1, 1);
        image.set(1, 2, 0);
        image.set(2, 0, 1);
        image.set(2, 1, 0);
        image.set(2, 2, 1);

        GrayImage result = cc.apply(image);

        GrayImage expected = new GrayImage(3, 3);
        expected.set(0, 0, 0);
        expected.set(0, 1, 1);
        expected.set(0, 2, 2);
        expected.set(1, 0, 3);
        expected.set(1, 1, 1);
        expected.set(1, 2, 4);
        expected.set(2, 0, 5);
        expected.set(2, 1, 6);
        expected.set(2, 2, 7);

        assertEquals(expected, result);
    }
}