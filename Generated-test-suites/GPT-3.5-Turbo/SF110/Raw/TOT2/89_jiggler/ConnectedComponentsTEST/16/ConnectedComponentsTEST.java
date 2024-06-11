import org.junit.Test;
import static org.junit.Assert.*;
import jigl.image.ops.ConnectedComponents;
import jigl.image.types.BinaryImage;
import jigl.image.exceptions.InvalidArgumentException;

public class ConnectedComponentsTest {

    @Test
    public void testConstructorValidArguments() {
        try {
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, BinaryImage.BLACK);
        } catch (InvalidArgumentException e) {
            fail("Valid arguments should not throw an exception");
        }
    }

    @Test
    public void testConstructorInvalidArguments() {
        try {
            ConnectedComponents cc = new ConnectedComponents(2, 1);
            fail("Invalid arguments should throw an InvalidArgumentException");
        } catch (InvalidArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testApply() {
        BinaryImage image = new BinaryImage(10, 10);
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, BinaryImage.BLACK);

        // Test basic functionality
        GrayImage result = cc.apply(image);
        assertNotNull(result);

        // Additional tests for edge cases

        // Test X=0, Y=0
        image.set(0, 0, BinaryImage.BLACK);
        result = cc.apply(image);
        assertEquals(1, result.get(0, 0).intValue());

        // Test X=10, Y=10
        image = new BinaryImage(10, 10);
        image.set(5, 5, BinaryImage.BLACK);
        result = cc.apply(image);
        assertNotNull(result);

    }
}