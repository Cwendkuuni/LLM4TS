import junit.framework.TestCase;

public class ConnectedComponentsTest extends TestCase {

    public void testConstructor() {
        try {
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
            assertEquals(ConnectedComponents.FOUR_CONNECTED, cc.neighbors);
            assertEquals(0, cc.background);
        } catch (InvalidArgumentException e) {
            fail("Failed to create ConnectedComponents object");
        }
    }

    public void testConstructorInvalidArgs() {
        try {
            ConnectedComponents cc = new ConnectedComponents(3, 1);
            fail("Should throw InvalidArgumentException");
        } catch (InvalidArgumentException e) {
            assertEquals("Invalid arguments", e.getMessage());
        }
    }

    public void testApply() {
        try {
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
            BinaryImage image = new BinaryImage(3, 3);
            image.set(0, 0, 0);
            image.set(0, 1, 1);
            image.set(0, 2, 1);
            image.set(1, 0, 0);
            image.set(1, 1, 0);
            image.set(1, 2, 1);
            image.set(2, 0, 0);
            image.set(2, 1, 1);
            image.set(2, 2, 0);
            GrayImage result = cc.apply(image);
            assertEquals(2, result.get(0, 0));
            assertEquals(0, result.get(0, 1));
            assertEquals(0, result.get(0, 2));
            assertEquals(1, result.get(1, 0));
            assertEquals(1, result.get(1, 1));
            assertEquals(0, result.get(1, 2));
            assertEquals(3, result.get(2, 0));
            assertEquals(0, result.get(2, 1));
            assertEquals(4, result.get(2, 2));
        } catch (InvalidArgumentException e) {
            fail("Failed to create ConnectedComponents object");
        }
    }
}