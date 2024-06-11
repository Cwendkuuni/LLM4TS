import static org.junit.Assert.assertEquals;
import org.junit.Test;
import jigl.image.*;
import jigl.image.exceptions.InvalidArgumentException;

public class ConnectedComponentsTest {

    // Test case 1 for constructor
    @Test(expected = InvalidArgumentException.class)
    public void testConstructorInvalidNeighbors() throws InvalidArgumentException {
        ConnectedComponents cc = new ConnectedComponents(2, 0);
    }

    // Test case 2 for constructor
    @Test(expected = InvalidArgumentException.class)
    public void testConstructorInvalidBackground() throws InvalidArgumentException {
        ConnectedComponents cc = new ConnectedComponents(0, 2);
    }

    // Test case 3 for constructor
    @Test
    public void testConstructorValidValues() throws InvalidArgumentException {
        ConnectedComponents cc = new ConnectedComponents(1, 0);
        assertEquals(1, cc.getNeighbors());
        assertEquals(0, cc.getBackground());
    }

    // Test case 1 for apply method
    @Test
    public void testApplyFourConnectedEmptyImage() throws InvalidArgumentException {
        BinaryImage image = new BinaryImage(10, 10);
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        GrayImage result = cc.apply(image);
        GrayImage expected = new GrayImage(10, 10);
        assertEquals(expected, result);
    }

    // Test case 2 for apply method
    @Test
    public void testApplyEightConnectedEmptyImage() throws InvalidArgumentException {
        BinaryImage image = new BinaryImage(10, 10);
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);
        GrayImage result = cc.apply(image);
        GrayImage expected = new GrayImage(10, 10);
        assertEquals(expected, result);
    }

    // Test case 3 for apply method
    @Test
    public void testApplyFourConnectedSingleConnectedComponent() throws InvalidArgumentException {
        BinaryImage image = new BinaryImage(10, 10);
        image.set(5, 5, 1);
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        GrayImage result = cc.apply(image);
        GrayImage expected = new GrayImage(10, 10);
        expected.set(5, 5, 1);
        assertEquals(expected, result);
    }

    // Test case 4 for apply method
    @Test
    public void testApplyEightConnectedSingleConnectedComponent() throws InvalidArgumentException {
        BinaryImage image = new BinaryImage(10, 10);
        image.set(5, 5, 1);
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);
        GrayImage result = cc.apply(image);
        GrayImage expected = new GrayImage(10, 10);
        expected.set(5, 5, 1);
        assertEquals(expected, result);
    }

    // Test case 5 for apply method
    @Test
    public void testApplyFourConnectedMultipleConnectedComponents() throws InvalidArgumentException {
        BinaryImage image = new BinaryImage(10, 10);
        image.set(2, 2, 1);
        image.set(7, 7, 1);
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        GrayImage result = cc.apply(image);
        GrayImage expected = new GrayImage(10, 10);
        expected.set(2, 2, 1);
        expected.set(7, 7, 2);
        assertEquals(expected, result);
    }

    // Test case 6 for apply method
    @Test
    public void testApplyEightConnectedMultipleConnectedComponents() throws InvalidArgumentException {
        BinaryImage image = new BinaryImage(10, 10);
        image.set(2, 2, 1);
        image.set(7, 7, 1);
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);
        GrayImage result = cc.apply(image);
        GrayImage expected = new GrayImage(10, 10);
        expected.set(2, 2, 1);
        expected.set(7, 7, 2);
        assertEquals(expected, result);
    }
}