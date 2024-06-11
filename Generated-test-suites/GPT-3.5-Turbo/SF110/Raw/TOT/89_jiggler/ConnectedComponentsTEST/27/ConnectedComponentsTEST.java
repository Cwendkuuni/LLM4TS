import static org.junit.Assert.*;
import org.junit.*;

public class ConnectedComponentsTest {

    private ConnectedComponents cc;

    @Before
    public void setUp() throws InvalidArgumentException {
        cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
    }

    // Test case for constructor with valid input
    @Test
    public void testConstructorValidInput() throws InvalidArgumentException {
        assertEquals(0, cc.neighbors);
        assertEquals(0, cc.background);
    }

    // Test case for constructor with invalid input
    @Test(expected = InvalidArgumentException.class)
    public void testConstructorInvalidInput() throws InvalidArgumentException {
        cc = new ConnectedComponents(2, 2);
    }

    // Test case for apply method with valid input
    @Test
    public void testApplyValidInput() {
        BinaryImage image = new BinaryImage(2, 2);
        image.set(0, 0, 1);
        image.set(1, 0, 1);
        image.set(0, 1, 0);
        image.set(1, 1, 0);
        
        GrayImage result = cc.apply(image);
        assertEquals(2, result.X());
        assertEquals(2, result.Y());
        assertEquals(1, result.get(0, 0).intValue());
        assertEquals(1, result.get(1, 0).intValue());
        assertEquals(0, result.get(0, 1).intValue());
        assertEquals(0, result.get(1, 1).intValue());
    }

    // Test case for apply method with empty image
    @Test
    public void testApplyEmptyImage() {
        BinaryImage image = new BinaryImage(0, 0);
        GrayImage result = cc.apply(image);
        assertEquals(0, result.X());
        assertEquals(0, result.Y());
    }

    // Test case for apply method with single pixel image
    @Test
    public void testApplySinglePixelImage() {
        BinaryImage image = new BinaryImage(1, 1);
        image.set(0, 0, 1);
        GrayImage result = cc.apply(image);
        assertEquals(1, result.X());
        assertEquals(1, result.Y());
        assertEquals(1, result.get(0, 0).intValue());
    }

    // Test case for apply method with all background pixels
    @Test
    public void testApplyAllBackgroundPixels() {
        BinaryImage image = new BinaryImage(2, 2);
        GrayImage result = cc.apply(image);
        assertEquals(2, result.X());
        assertEquals(2, result.Y());
        assertEquals(0, result.get(0, 0).intValue());
        assertEquals(0, result.get(1, 0).intValue());
        assertEquals(0, result.get(0, 1).intValue());
        assertEquals(0, result.get(1, 1).intValue());
    }

    // Test case for apply method with disconnected pixels
    @Test
    public void testApplyDisconnectedPixels() {
        BinaryImage image = new BinaryImage(2, 2);
        image.set(0, 0, 1);
        image.set(1, 1, 1);
        GrayImage result = cc.apply(image);
        assertEquals(2, result.X());
        assertEquals(2, result.Y());
        assertEquals(1, result.get(0, 0).intValue());
        assertEquals(0, result.get(1, 0).intValue());
        assertEquals(0, result.get(0, 1).intValue());
        assertEquals(2, result.get(1, 1).intValue());
    }

    // Test case for apply method with edge case of maximum image size
    @Test
    public void testApplyMaximumImageSize() {
        BinaryImage image = new BinaryImage(1000, 1000);
        // Set all pixels to background
        for (int x = 0; x < image.X(); x++) {
            for (int y = 0; y < image.Y(); y++) {
                image.set(x, y, 0);
            }
        }
        GrayImage result = cc.apply(image);
        assertEquals(1000, result.X());
        assertEquals(1000, result.Y());
        for (int x = 0; x < result.X(); x++) {
            for (int y = 0; y < result.Y(); y++) {
                assertEquals(0, result.get(x, y).intValue());
            }
        }
    }

    // Test case for apply method with error scenario of null image
    @Test(expected = NullPointerException.class)
    public void testApplyNullImage() {
        BinaryImage image = null;
        cc.apply(image);
    }

}