import static org.junit.Assert.*;
import org.junit.Test;

public class ConnectedComponentsTest {

    @Test
    public void testFourConnected() {
        try {
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
            BinaryImage image = new BinaryImage(4, 4);
            image.set(0, 0, 1);
            image.set(0, 1, 0);
            image.set(0, 2, 0);
            image.set(0, 3, 1);
            image.set(1, 0, 0);
            image.set(1, 1, 1);
            image.set(1, 2, 1);
            image.set(1, 3, 0);
            image.set(2, 0, 0);
            image.set(2, 1, 0);
            image.set(2, 2, 0);
            image.set(2, 3, 0);
            image.set(3, 0, 1);
            image.set(3, 1, 0);
            image.set(3, 2, 0);
            image.set(3, 3, 1);
            GrayImage expected = new GrayImage(4, 4);
            expected.set(0, 0, 1);
            expected.set(0, 1, 0);
            expected.set(0, 2, 0);
            expected.set(0, 3, 2);
            expected.set(1, 0, 0);
            expected.set(1, 1, 3);
            expected.set(1, 2, 3);
            expected.set(1, 3, 0);
            expected.set(2, 0, 0);
            expected.set(2, 1, 0);
            expected.set(2, 2, 0);
            expected.set(2, 3, 0);
            expected.set(3, 0, 4);
            expected.set(3, 1, 0);
            expected.set(3, 2, 0);
            expected.set(3, 3, 5);

            GrayImage result = cc.apply(image);

            assertEquals(expected, result);
        } catch (Exception e) {
            fail("Exception not expected");
        }
    }

    @Test
    public void testEightConnected() {
        try {
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 1);
            BinaryImage image = new BinaryImage(4, 4);
            image.set(0, 0, 1);
            image.set(0, 1, 0);
            image.set(0, 2, 0);
            image.set(0, 3, 1);
            image.set(1, 0, 0);
            image.set(1, 1, 1);
            image.set(1, 2, 1);
            image.set(1, 3, 0);
            image.set(2, 0, 0);
            image.set(2, 1, 0);
            image.set(2, 2, 0);
            image.set(2, 3, 0);
            image.set(3, 0, 1);
            image.set(3, 1, 0);
            image.set(3, 2, 0);
            image.set(3, 3, 1);
            GrayImage expected = new GrayImage(4, 4);
            expected.set(0, 0, 1);
            expected.set(0, 1, 0);
            expected.set(0, 2, 0);
            expected.set(0, 3, 1);
            expected.set(1, 0, 0);
            expected.set(1, 1, 4);
            expected.set(1, 2, 4);
            expected.set(1, 3, 0);
            expected.set(2, 0, 0);
            expected.set(2, 1, 0);
            expected.set(2, 2, 0);
            expected.set(2, 3, 0);
            expected.set(3, 0, 2);
            expected.set(3, 1, 0);
            expected.set(3, 2, 0);
            expected.set(3, 3, 3);

            GrayImage result = cc.apply(image);

            assertEquals(expected, result);
        } catch (Exception e) {
            fail("Exception not expected");
        }
    }

    @Test(expected = InvalidArgumentException.class)
    public void testInvalidNumberNeighbors() throws InvalidArgumentException {
        new ConnectedComponents(3, 0);
    }

    @Test(expected = InvalidArgumentException.class)
    public void testInvalidBackgroundColor() throws InvalidArgumentException {
        new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 2);
    }

}