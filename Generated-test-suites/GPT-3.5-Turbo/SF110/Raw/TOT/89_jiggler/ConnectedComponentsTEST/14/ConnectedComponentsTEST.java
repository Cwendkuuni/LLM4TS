import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ConnectedComponentsTest {

    private ConnectedComponents cc;

    @Before
    public void setUp() throws InvalidArgumentException {
        cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
    }

    @Test
    public void testApply_backgroundPixel_returnZero() {
        BinaryImage image = new BinaryImage(1, 1);
        image.set(0, 0, 0);
        GrayImage result = cc.apply(image);
        assertEquals(0, result.get(0, 0));
    }

    @Test
    public void testApply_topLeftCorner_returnOne() {
        BinaryImage image = new BinaryImage(2, 2);
        image.set(0, 0, 1);
        GrayImage result = cc.apply(image);
        assertEquals(1, result.get(0, 0));
    }

    @Test
    public void testApply_fourConnectedNeighbors_returnTwo() {
        BinaryImage image = new BinaryImage(2, 2);
        image.set(0, 0, 1);
        image.set(1, 0, 1);
        GrayImage result = cc.apply(image);
        assertEquals(2, result.get(1, 0));
    }

    @Test
    public void testApply_eightConnectedNeighbors_returnTwo() {
        cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);
        BinaryImage image = new BinaryImage(3, 3);
        image.set(0, 0, 1);
        image.set(1, 0, 1);
        image.set(0, 1, 1);
        GrayImage result = cc.apply(image);
        assertEquals(2, result.get(1, 0));
    }

    @Test(expected = InvalidArgumentException.class)
    public void testConstructor_invalidNumberNeighbors_throwInvalidArgumentException() throws InvalidArgumentException {
        new ConnectedComponents(2, 0);
    }

    @Test(expected = InvalidArgumentException.class)
    public void testConstructor_invalidBackgroundColor_throwInvalidArgumentException() throws InvalidArgumentException {
        new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 2);
    }

}