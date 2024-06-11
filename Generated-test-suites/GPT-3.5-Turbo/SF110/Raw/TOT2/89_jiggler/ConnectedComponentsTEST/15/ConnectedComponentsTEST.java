import static org.junit.Assert.*;
import org.junit.Test;

public class ConnectedComponentsTest {

    @Test
    public void testConstructorValidArguments() {
        try {
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
            assertEquals(0, cc.neighbors);
            assertEquals(0, cc.background);
        } catch (InvalidArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testConstructorInvalidArguments() {
        try {
            ConnectedComponents cc = new ConnectedComponents(2, 2);
            fail("InvalidArgumentException not thrown");
        } catch (InvalidArgumentException e) {
            assertEquals("Invalid arguments", e.getMessage());
        }
    }

    @Test
    public void testApply() {
        BinaryImage image = new BinaryImage(2, 2);
        image.set(0, 0, 1);
        image.set(1, 0, 0);
        image.set(0, 1, 1);
        image.set(1, 1, 0);
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);
        GrayImage result = cc.apply(image);
        assertEquals(Integer.valueOf(1), result.get(0, 0));
        assertEquals(Integer.valueOf(1), result.get(1, 0));
        assertEquals(Integer.valueOf(2), result.get(0, 1));
        assertEquals(Integer.valueOf(2), result.get(1, 1));
    }

    @Test
    public void testApplyEdgeCases() {
        // Test when input BinaryImage is null
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        try {
            cc.apply(null);
            fail("Expected IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Input BinaryImage is null", e.getMessage());
        }
    }
}