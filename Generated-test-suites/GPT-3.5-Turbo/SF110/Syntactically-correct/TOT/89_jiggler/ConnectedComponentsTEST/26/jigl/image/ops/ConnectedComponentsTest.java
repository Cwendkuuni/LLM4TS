import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import jigl.image.BinaryImage;
import jigl.image.GrayImage;

public class ConnectedComponentsTest {

    ConnectedComponents instance;

    @Before
    public void setUp() {
        instance = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
    }

    @Test
    public void testConnectedComponents_constructor_validInput() {
        try {
            ConnectedComponents instance = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 1);
            assertNotNull(instance);
        } catch (InvalidArgumentException e) {
            fail("Failed to create an instance of ConnectedComponents with valid input");
        }
    }

    @Test(expected = InvalidArgumentException.class)
    public void testConnectedComponents_constructor_invalidInput() throws InvalidArgumentException {
        ConnectedComponents instance = new ConnectedComponents(3, 1);
    }

    @Test
    public void testApply_allBackgroundPixels_returnGrayImageWithAllZeros() {
        BinaryImage image = new BinaryImage(10, 10);
        GrayImage expected = new GrayImage(10, 10);
        GrayImage result = instance.apply(image);
        assertEquals(expected, result);
    }

    @Test
    public void testApply_allForegroundPixels_returnGrayImageWithAllNonZeroValues() {
        BinaryImage image = new BinaryImage(10, 10);
        image.setToValue(1);
        GrayImage result = instance.apply(image);
        for (int x = 0; x < result.X(); x++) {
            for (int y = 0; y < result.Y(); y++) {
                assertNotEquals(0, result.get(x, y));
            }
        }
    }

    @Test
    public void testApply_mixedBackgroundAndForegroundPixels_returnCorrectGrayImage() {
        BinaryImage image = new BinaryImage(10, 10);
        // set pixels (0,0), (0,1) as foreground
        image.set(0, 0, 1);
        image.set(0, 1, 1);
        GrayImage expected = new GrayImage(10, 10);
        expected.set(0, 0, 1);
        expected.set(0, 1, 1);
        GrayImage result = instance.apply(image);
        assertEquals(expected, result);
    }

    @Test
    public void testApply_fourConnectedNeighbors_returnCorrectGrayImage() {
        BinaryImage image = new BinaryImage(10, 10);
        // set pixels (0,0), (1,0) as foreground
        image.set(0, 0, 1);
        image.set(1, 0, 1);
        GrayImage expected = new GrayImage(10, 10);
        expected.set(0, 0, 1);
        expected.set(1, 0, 1);
        GrayImage result = instance.apply(image);
        assertEquals(expected, result);
    }

    @Test
    public void testApply_eightConnectedNeighbors_returnCorrectGrayImage() {
        instance = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);
        BinaryImage image = new BinaryImage(10, 10);
        // set pixels (0,0), (1,0), (0,1), (1,1) as foreground
        image.set(0, 0, 1);
        image.set(1, 0, 1);
        image.set(0, 1, 1);
        image.set(1, 1, 1);
        GrayImage expected = new GrayImage(10, 10);
        expected.set(0, 0, 1);
        expected.set(1, 0, 1);
        expected.set(0, 1, 1);
        expected.set(1, 1, 1);
        GrayImage result = instance.apply(image);
        assertEquals(expected, result);
    }
}