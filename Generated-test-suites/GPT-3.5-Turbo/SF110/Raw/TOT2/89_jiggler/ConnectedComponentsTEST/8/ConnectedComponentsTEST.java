import static org.junit.Assert.*;
import org.junit.Test;

public class ConnectedComponentsTest {

    @Test
    public void testConnectedComponentsConstructorValid() {
        try {
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 1);
            assertNotNull(cc);
        } catch (InvalidArgumentException e) {
            fail("Exception thrown for valid constructor arguments");
        }
    }

    @Test
    public void testConnectedComponentsConstructorInvalidArguments() {
        try {
            ConnectedComponents cc = new ConnectedComponents(2, 1);
            fail("InvalidArgumentException not thrown for invalid constructor arguments");
        } catch (InvalidArgumentException e) {
            // Exception expected
        }
    }

    @Test
    public void testApplyMethod() {
        BinaryImage image = new BinaryImage(10, 10);
        image.set(0, 0, 1); // Set a pixel to background color
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);

        GrayImage result = cc.apply(image);

        assertNotNull(result);
    }
}