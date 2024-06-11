import org.junit.Test;
import static org.junit.Assert.*;

public class ConnectedComponentsTest {

    @Test
    public void testConstructor() {
        try {
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
            assertNotNull(cc);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testApply() {
        try {
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
            BinaryImage image = new BinaryImage(5, 5);
            // Set up image with connected components
            image.set(0, 0, 1);
            image.set(1, 0, 1);
            image.set(0, 1, 1);
            image.set(1, 1, 1);
            image.set(3, 2, 1);
            image.set(4, 2, 1);
            image.set(3, 3, 1);
            image.set(4, 3, 1);

            GrayImage result = cc.apply(image);
            // Check the result
            assertEquals(result.get(0, 0), 1);
            assertEquals(result.get(1, 0), 1);
            assertEquals(result.get(0, 1), 1);
            assertEquals(result.get(1, 1), 1);
            assertEquals(result.get(3, 2), 2);
            assertEquals(result.get(4, 2), 2);
            assertEquals(result.get(3, 3), 2);
            assertEquals(result.get(4, 3), 2);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test(expected = InvalidArgumentException.class)
    public void testConstructorInvalidNeighbors() throws InvalidArgumentException {
        ConnectedComponents cc = new ConnectedComponents(2, 0);
    }

    @Test(expected = InvalidArgumentException.class)
    public void testConstructorInvalidBackgroundColor() throws InvalidArgumentException {
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 2);
    }

    // Add more test cases for edge cases and exception handling scenarios

}

