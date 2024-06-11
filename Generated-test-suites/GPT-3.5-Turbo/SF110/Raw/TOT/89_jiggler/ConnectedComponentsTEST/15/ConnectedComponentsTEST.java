import org.junit.*;
import static org.junit.Assert.*;

public class ConnectedComponentsTest {

    @Test
    public void testConstructorValidValues() throws Exception {
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 1);
        assertEquals(ConnectedComponents.FOUR_CONNECTED, cc.getNumberNeighbors());
        assertEquals(1, cc.getBackgroundColor());
    }

    @Test(expected = InvalidArgumentException.class)
    public void testConstructorInvalidValues() throws Exception {
        ConnectedComponents cc = new ConnectedComponents(2, 2);
        // Should throw InvalidArgumentException
    }

    @Test
    public void testApplyFourConnected() {
        BinaryImage image = new BinaryImage(10, 10);
        image.set(3, 3, 1);
        image.set(6, 6, 1);

        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        GrayImage result = cc.apply(image);

        assertEquals(1, result.get(3, 3));
        assertEquals(2, result.get(6, 6));
        assertEquals(0, result.get(2, 2));
        assertEquals(0, result.get(7, 7));
    }

    @Test
    public void testApplyEightConnected() {
        BinaryImage image = new BinaryImage(10, 10);
        image.set(3, 3, 1);
        image.set(6, 6, 1);
        image.set(7, 7, 1);

        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);
        GrayImage result = cc.apply(image);

        assertEquals(1, result.get(3, 3));
        assertEquals(2, result.get(6, 6));
        assertEquals(2, result.get(7, 7));
        assertEquals(0, result.get(2, 2));
    }

    @Test
    public void testApplyAllBackground() {
        BinaryImage image = new BinaryImage(10, 10); // All pixels are background

        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        GrayImage result = cc.apply(image);

        for (int x = 0; x < result.X(); x++) {
            for (int y = 0; y < result.Y(); y++) {
                assertEquals(0, result.get(x, y));
            }
        }
    }

    @Test
    public void testApplyAllForeground() {
        BinaryImage image = new BinaryImage(10, 10); // All pixels are foreground
        image.fill(1);

        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        GrayImage result = cc.apply(image);

        for (int x = 0; x < result.X(); x++) {
            for (int y = 0; y < result.Y(); y++) {
                assertEquals(1, result.get(x, y));
            }
        }
    }

    @Test
    public void testApplyEmptyImage() {
        BinaryImage image = new BinaryImage(0, 0); // Empty image

        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        GrayImage result = cc.apply(image);

        assertEquals(0, result.X());
        assertEquals(0, result.Y());
    }

    @Test
    public void testApplyEdgeCase() {
        BinaryImage image = new BinaryImage(1, 1);
        image.set(0, 0, 1);

        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        GrayImage result = cc.apply(image);

        assertEquals(1, result.get(0, 0));
    }

}