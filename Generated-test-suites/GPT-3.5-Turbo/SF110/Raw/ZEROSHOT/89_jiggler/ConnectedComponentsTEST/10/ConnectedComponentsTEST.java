package jigl.image.ops;

import jigl.image.BinaryImage;
import jigl.image.GrayImage;
import jigl.image.exceptions.InvalidArgumentException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ConnectedComponentsTest {

    private ConnectedComponents cc;

    @Before
    public void setup() throws InvalidArgumentException {
        cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
    }

    @Test
    public void testApply() {
        //Expected result
        GrayImage expected = new GrayImage(2, 2);
        expected.set(0, 0, 1);
        expected.set(1, 0, 1);
        expected.set(0, 1, 1);
        expected.set(1, 1, 1);

        BinaryImage input = new BinaryImage(2, 2);
        input.set(0, 0, 0);
        input.set(1, 0, 0);
        input.set(0, 1, 0);
        input.set(1, 1, 0);

        GrayImage result = cc.apply(input);

        Assert.assertEquals(expected, result);
    }

    @Test(expected = InvalidArgumentException.class)
    public void testInvalidNeighbors() throws InvalidArgumentException {
        cc = new ConnectedComponents(2, 0);
    }

    @Test(expected = InvalidArgumentException.class)
    public void testInvalidBackground() throws InvalidArgumentException {
        cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 2);
    }

}