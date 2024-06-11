import static org.junit.Assert.*;
import org.junit.Test;

public class ConnectedComponentsTest {

    @Test
    public void testConnectedComponentsConstructorValidArguments() {
        try {
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
            assertEquals(ConnectedComponents.FOUR_CONNECTED, cc.neighbors);
            assertEquals(0, cc.background);
        } catch (InvalidArgumentException e) {
            fail("Exception thrown for valid arguments");
        }
    }

    @Test
    public void testConnectedComponentsConstructorInvalidArguments() {
        try {
            ConnectedComponents cc = new ConnectedComponents(2, 2);
            fail("InvalidArgumentException not thrown for invalid arguments");
        } catch (InvalidArgumentException e) {
            // This is expected
        }
    }

    @Test
    public void testConnectedComponentsApply() {
        BinaryImage image = new BinaryImage(5, 5);
        image.set(2, 2, 1);

        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        GrayImage result = cc.apply(image);

        assertEquals(1, result.get(2, 2).intValue());
    }

    @Test
    public void testConnectedComponentsApplyEdgeCases() {
        BinaryImage image = new BinaryImage(1, 1);
        image.set(0, 0, 0);

        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 1);
        GrayImage result = cc.apply(image);

        assertEquals(1, result.get(0, 0).intValue());
    }

    @Test
    public void testConnectedComponentsApplyExceptions() {
        BinaryImage image = null;

        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        try {
            cc.apply(image);
            fail("NullPointerException not thrown for null image");
        } catch (NullPointerException e) {
            // This is expected
        }
    }
}