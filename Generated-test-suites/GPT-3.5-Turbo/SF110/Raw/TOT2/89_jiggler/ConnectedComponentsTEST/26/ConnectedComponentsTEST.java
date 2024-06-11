import org.junit.Test;
import static org.junit.Assert.*;
import jigl.image.ops.ConnectedComponents;
import jigl.image.types.BinaryImage;
import jigl.image.types.GrayImage;
import jigl.image.exceptions.InvalidArgumentException;

public class ConnectedComponentsTest {

    @Test
    public void testConstructorValidInput() {
        try {
            ConnectedComponents cc = new ConnectedComponents(0, 0);
            assertNotNull(cc);
        } catch (InvalidArgumentException e) {
            fail("Unexpected exception thrown");
        }
    }

    @Test
    public void testConstructorInvalidInput() {
        try {
            ConnectedComponents cc = new ConnectedComponents(3, 2);
            fail("InvalidArgumentException not thrown");
        } catch (InvalidArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testApplyMethod() {
        ConnectedComponents cc = new ConnectedComponents(0, 0);
        BinaryImage image = new BinaryImage(2, 2);
        image.set(0, 0, 1);
        image.set(1, 1, 1);
        GrayImage result = cc.apply(image);
        assertEquals(1, result.get(0, 0).intValue());
        assertEquals(2, result.get(1, 1).intValue());
    }

    // Add more test cases for edge cases and error scenarios

}