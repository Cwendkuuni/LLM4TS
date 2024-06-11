package jigl.image.ops;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jigl.image.exceptions.InvalidArgumentException;
import jigl.image.types.BinaryImage;
import jigl.image.types.GrayImage;

public class ConnectedComponentsTest {

    @Test(expected = InvalidArgumentException.class)
    public void testConstructorInvalidArgs() throws InvalidArgumentException {
        new ConnectedComponents(3, 1);
    }

    @Test
    public void testConstructorValidArgs() throws InvalidArgumentException {
        ConnectedComponents cc = new ConnectedComponents(0, 1);
        assertEquals(0, cc.neighbors);
        assertEquals(1, cc.background);

        cc = new ConnectedComponents(1, 0);
        assertEquals(1, cc.neighbors);
        assertEquals(0, cc.background);
    }

    @Test
    public void testApplyFourConnected() {
        ConnectedComponents cc = new ConnectedComponents(0, 1);
        BinaryImage binaryImage = new BinaryImage(3, 3);
        binaryImage.set(0, 0, 1);
        binaryImage.set(0, 2, 1);
        binaryImage.set(2, 0, 1);
        binaryImage.set(2, 2, 1);
        GrayImage expected = new GrayImage(3, 3);
        expected.set(0, 0, 0);
        expected.set(0, 2, 1);
        expected.set(2, 0, 2);
        expected.set(2, 2, 3);
        GrayImage result = cc.apply(binaryImage);
        assertEquals(expected, result);
    }

    @Test
    public void testApplyEightConnected() {
        ConnectedComponents cc = new ConnectedComponents(1, 0);
        BinaryImage binaryImage = new BinaryImage(3, 3);
        binaryImage.set(0, 0, 1);
        binaryImage.set(0, 2, 1);
        binaryImage.set(2, 0, 1);
        binaryImage.set(2, 2, 1);
        GrayImage expected = new GrayImage(3, 3);
        expected.set(0, 0, 1);
        expected.set(0, 2, 2);
        expected.set(2, 0, 3);
        expected.set(2, 2, 4);
        GrayImage result = cc.apply(binaryImage);
        assertEquals(expected, result);
    }
}