import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import jigl.image.BinaryImage;
import jigl.image.GrayImage;
import jigl.image.exceptions.InvalidArgumentException;
import jigl.image.ops.ConnectedComponents;

public class ConnectedComponentsTest {

    private ConnectedComponents cc;

    @Before
    public void setUp() throws Exception {
        cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
    }

    @Test
    public void testApply_fourConnected() {
        BinaryImage image = new BinaryImage(3, 3);
        image.set(0, 0, 1);
        image.set(1, 1, 1);

        GrayImage result = cc.apply(image);

        assertEquals(1, result.get(0, 0));
        assertEquals(2, result.get(1, 1));
    }

    @Test
    public void testApply_eightConnected() {
        cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);

        BinaryImage image = new BinaryImage(3, 3);
        image.set(0, 0, 1);
        image.set(1, 1, 1);

        GrayImage result = cc.apply(image);

        assertEquals(1, result.get(0, 0));
        assertEquals(1, result.get(1, 1));
    }

    @Test(expected = InvalidArgumentException.class)
    public void testConstructor_invalidInput() throws InvalidArgumentException {
        cc = new ConnectedComponents(2, 2);
    }
}