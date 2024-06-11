import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ConnectedComponentsTest {

    private ConnectedComponents cc;
    
    @Before
    public void setUp() {
        cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 1);
    }
    
    @Test
    public void testApply_backgroundPixel_grayImage() {
        BinaryImage image = new BinaryImage(new int[][]{{0, 0, 0}, {0, 1, 0}, {0, 0, 0}});
        GrayImage expected = new GrayImage(new int[][]{{1, 1, 1}, {1, 0, 1}, {1, 1, 1}});
        GrayImage result = cc.apply(image);
        assertArrayEquals(expected.getData(), result.getData());
    }
    
    @Test
    public void testApply_singlePixel_grayImage() {
        BinaryImage image = new BinaryImage(new int[][]{{1, 0, 0}, {0, 0, 0}, {0, 0, 0}});
        GrayImage expected = new GrayImage(new int[][]{{2, 0, 0}, {0, 0, 0}, {0, 0, 0}});
        GrayImage result = cc.apply(image);
        assertArrayEquals(expected.getData(), result.getData());
    }
    
    @Test
    public void testApply_multipleConnectedPixels_grayImage() {
        BinaryImage image = new BinaryImage(new int[][]{{1, 1, 0}, {0, 1, 0}, {0, 0, 0}});
        GrayImage expected = new GrayImage(new int[][]{{2, 2, 0}, {0, 2, 0}, {0, 0, 0}});
        GrayImage result = cc.apply(image);
        assertArrayEquals(expected.getData(), result.getData());
    }
    
    @Test
    public void testApply_fourConnected_singlePixel_grayImage() {
        cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 1);
        BinaryImage image = new BinaryImage(new int[][]{{1, 0, 0}, {0, 0, 0}, {0, 0, 0}});
        GrayImage expected = new GrayImage(new int[][]{{2, 2, 2}, {2, 0, 2}, {2, 2, 2}});
        GrayImage result = cc.apply(image);
        assertArrayEquals(expected.getData(), result.getData());
    }
    
    @Test
    public void testApply_fourConnected_backgroundPixel_grayImage() {
        cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 1);
        BinaryImage image = new BinaryImage(new int[][]{{0, 0, 0}, {0, 1, 0}, {0, 0, 0}});
        GrayImage expected = new GrayImage(new int[][]{{0, 0, 0}, {0, 1, 0}, {0, 0, 0}});
        GrayImage result = cc.apply(image);
        assertArrayEquals(expected.getData(), result.getData());
    }
    
    @Test(expected = InvalidArgumentException.class)
    public void testConstructor_invalidNeighborsAndBackgroundColor_throwException() throws InvalidArgumentException {
        cc = new ConnectedComponents(2, 2);
    }
    
    @Test(expected = InvalidArgumentException.class)
    public void testConstructor_invalidNeighbors_throwException() throws InvalidArgumentException {
        cc = new ConnectedComponents(2, 0);
    }
    
    @Test(expected = InvalidArgumentException.class)
    public void testConstructor_invalidBackgroundColor_throwException() throws InvalidArgumentException {
        cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 2);
    }
}