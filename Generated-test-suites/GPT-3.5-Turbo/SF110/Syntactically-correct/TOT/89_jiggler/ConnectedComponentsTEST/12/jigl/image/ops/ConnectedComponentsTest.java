import org.junit.*;
import static org.junit.Assert.*;
import jigl.image.*;
import jigl.image.exceptions.*;

public class ConnectedComponentsTest {

    ConnectedComponents cc;

    @Before
    public void setUp() {
        try {
            cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        } catch (InvalidArgumentException e) {
            fail("InvalidArgumentException thrown");
        }
    }

    // Test case for constructing ConnectedComponents class with valid input
    @Test
    public void testConstructor_ValidInput() {
        try {
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        } catch (InvalidArgumentException e) {
            fail("InvalidArgumentException thrown");
        }
    }

    // Test case for constructing ConnectedComponents class with invalid neighbors value
    @Test(expected = InvalidArgumentException.class)
    public void testConstructor_InvalidNeighborsValue() throws InvalidArgumentException {
        ConnectedComponents cc = new ConnectedComponents(2, 0);
    }

    // Test case for constructing ConnectedComponents class with invalid background value
    @Test(expected = InvalidArgumentException.class)
    public void testConstructor_InvalidBackgroundValue() throws InvalidArgumentException {
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 2);
    }

    // Test case for applying operation with valid input
    @Test
    public void testApply_ValidInput() {
        BinaryImage image = new BinaryImage(10, 10);
        GrayImage expected = new GrayImage(10, 10);

        // Set the image pixel values
        image.set(0, 0, 1);
        image.set(1, 1, 1);
        image.set(2, 2, 1);

        expected.set(0, 0, 1);
        expected.set(1, 1, 2);
        expected.set(2, 2, 3);

        GrayImage result = cc.apply(image);

        assertEquals(expected, result);
    }

    // Test case for applying operation with image of size 0
    @Test
    public void testApply_ZeroSizeImage() {
        BinaryImage image = new BinaryImage(0, 0);
        GrayImage expected = new GrayImage(0, 0);

        GrayImage result = cc.apply(image);

        assertEquals(expected, result);
    }

    // Test case for applying operation with null input
    @Test(expected = NullPointerException.class)
    public void testApply_NullInput() {
        BinaryImage image = null;

        GrayImage result = cc.apply(image);  
    }

    // Test case for applying operation with invalid input (values other than 0 and 1)
    @Test
    public void testApply_InvalidInputValue() {
        BinaryImage image = new BinaryImage(5, 5);

        // Set the image pixel values
        image.set(0, 0, 2);
        image.set(1, 1, -1);

        GrayImage result = cc.apply(image);

        // Verify that the output remains unchanged
        assertEquals(image, result);
    }

    // Test case for applying operation with invalid image dimensions
    @Test
    public void testApply_InvalidImageDimensions() {
        BinaryImage image = new BinaryImage(5, 5);

        // Set the image pixel values
        image.set(0, 0, 1);
        image.set(1, 1, 1);

        // Create expected result image with invalid dimensions
        GrayImage expected = new GrayImage(6, 6);
        expected.set(0, 0, 1);
        expected.set(1, 1, 2);

        GrayImage result = cc.apply(image);

        // Verify that the output has correct dimensions
        assertEquals(expected.X(), result.X());
        assertEquals(expected.Y(), result.Y());

        // Verify that the output remains unchanged
        assertEquals(image, result);
    }

}