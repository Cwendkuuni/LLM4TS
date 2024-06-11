
import org.junit.Test;
import static org.junit.Assert.*;

public class ConnectedComponentsTest {

    // Test constructor
    @Test
    public void testConstructor() throws InvalidArgumentException {
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        assertNotNull(cc);
    }

    // Test apply method
    @Test
    public void testApply() {
        // Create a test BinaryImage
        BinaryImage image = new BinaryImage(2, 2);
        image.set(0, 0, 1); // background
        image.set(0, 1, 0);
        image.set(1, 0, 0);
        image.set(1, 1, 1); // background

        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        GrayImage result = cc.apply(image);

        // Assert the result
        assertEquals(0, result.get(0, 0));
        assertEquals(1, result.get(0, 1));
        assertEquals(1, result.get(1, 0));
        assertEquals(0, result.get(1, 1));
    }

    // Test apply method with specific edge cases
    @Test
    public void testApplyEdgeCases() {
        // Create a test BinaryImage
        BinaryImage image1 = new BinaryImage(1, 1);
        image1.set(0, 0, 1); // background

        BinaryImage image2 = new BinaryImage(2, 2);
        image2.set(0, 0, 0); // background
        image2.set(0, 1, 0);
        image2.set(1, 0, 0);
        image2.set(1, 1, 0); // background

        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        GrayImage result1 = cc.apply(image1);
        GrayImage result2 = cc.apply(image2);

        // Assert the result
        assertEquals(0, result1.get(0, 0));
        assertEquals(0, result2.get(0, 0));
        assertEquals(0, result2.get(0, 1));
        assertEquals(0, result2.get(1, 0));
        assertEquals(0, result2.get(1, 1));
    }
}