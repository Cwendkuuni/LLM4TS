import org.junit.Test;
import static org.junit.Assert.*;
import jigl.image.types.*;
import jigl.image.*;
import jigl.image.exceptions.*;

public class ConnectedComponentsTest {

    // Test case for constructor IllegalArgumentException
    @Test(expected = InvalidArgumentException.class)
    public void testConstructorIllegalArgumentException() throws InvalidArgumentException {
        ConnectedComponents cc = new ConnectedComponents(2, 0);
    }

    // Test cases for apply() method

    // Typical use case - apply on regular BinaryImage
    @Test
    public void testApplyTypical() throws InvalidArgumentException {
        BinaryImage image = new BinaryImage(3, 3);
        image.set(0, 0, 1);
        image.set(0, 1, 0);
        image.set(0, 2, 1);
        image.set(1, 0, 0);
        image.set(1, 1, 0);
        image.set(1, 2, 0);
        image.set(2, 0, 1);
        image.set(2, 1, 0);
        image.set(2, 2, 1);
        
        ConnectedComponents cc = new ConnectedComponents(0, 0);
        GrayImage result = cc.apply(image);
        
        GrayImage expected = new GrayImage(3, 3);
        expected.set(0, 0, 1);
        expected.set(0, 1, 0);
        expected.set(0, 2, 2);
        expected.set(1, 0, 0);
        expected.set(1, 1, 0);
        expected.set(1, 2, 0);
        expected.set(2, 0, 3);
        expected.set(2, 1, 0);
        expected.set(2, 2, 4);

        assertEquals(expected, result);
    }

    // Edge case - apply on empty BinaryImage
    @Test
    public void testApplyEmptyImage() throws InvalidArgumentException {
        BinaryImage image = new BinaryImage(0, 0);

        ConnectedComponents cc = new ConnectedComponents(0, 0);
        GrayImage result = cc.apply(image);
        
        GrayImage expected = new GrayImage(0, 0);

        assertEquals(expected, result);

    }

    // Error scenario - apply on null BinaryImage
    @Test(expected = NullPointerException.class)
    public void testApplyNullImage() throws InvalidArgumentException {
        BinaryImage image = null;

        ConnectedComponents cc = new ConnectedComponents(0, 0);
        GrayImage result = cc.apply(image);
    }

    // Test case for FOUR_CONNECTED constant
    @Test
    public void testFourConnectedConstant() throws InvalidArgumentException {
        assertEquals(0, ConnectedComponents.FOUR_CONNECTED);
    }

    // Test case for EIGHT_CONNECTED constant
    @Test
    public void testEightConnectedConstant() throws InvalidArgumentException {
        assertEquals(1, ConnectedComponents.EIGHT_CONNECTED);
    }

}