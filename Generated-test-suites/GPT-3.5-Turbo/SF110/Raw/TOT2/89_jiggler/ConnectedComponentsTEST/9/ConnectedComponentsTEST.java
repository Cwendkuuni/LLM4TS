package jigl.image.ops;

import static org.junit.Assert.*;
import org.junit.Test;
import jigl.image.*;
import jigl.image.exceptions.InvalidArgumentException;

public class ConnectedComponentsTest {

    @Test
    public void testConnectedComponentsConstructor() {
        ConnectedComponents cc = null;
        try {
            cc = new ConnectedComponents(0, 0);
            assertEquals(0, cc.neighbors);
            assertEquals(0, cc.background);
        } catch (InvalidArgumentException e) {
            fail("Constructor test failed");
        }
    }

    @Test
    public void testConnectedComponentsConstructorInvalidArguments() {
        try {
            ConnectedComponents cc = new ConnectedComponents(2, 2);
            fail("Invalid arguments test failed");
        } catch (InvalidArgumentException e) {
            // Exception expected
        }
    }

    @Test
    public void testConnectedComponentsApply() {
        // Test the apply method functionality
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        BinaryImage image = new BinaryImage(3, 3);
        image.set(0, 0, 1);
        image.set(1, 1, 1);
        image.set(2, 2, 1);

        GrayImage result = cc.apply(image);

        // Check if the output image has the expected connected components
        assertEquals(Integer.valueOf(1), result.get(0, 0));
        assertEquals(Integer.valueOf(2), result.get(1, 1));
        assertEquals(Integer.valueOf(3), result.get(2, 2));
    }

    @Test
    public void testConnectedComponentsApplyEdgeCases() {
        // Test edge cases where the input image has boundaries
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);
        BinaryImage image = new BinaryImage(3, 3);
        // Setting the top left and bottom right pixels as foreground
        image.set(0, 0, 1);
        image.set(2, 2, 1);

        // Apply connected components algorithm
        GrayImage result = cc.apply(image);

        // Check if the output image has the expected connected components
        assertEquals(Integer.valueOf(1), result.get(0, 0)); // Top left pixel
        assertEquals(Integer.valueOf(2), result.get(2, 2)); // Bottom right pixel
    }

    @Test
    public void testConnectedComponentsApplyExceptionHandling() {
        // Test exception handling when applying connected components algorithm
        try {
            ConnectedComponents cc = new ConnectedComponents(1, 0);
            BinaryImage image = new BinaryImage(3, 3);
            cc.apply(image); // This should throw an exception since the background value is invalid
            fail("Exception handling test failed");
        } catch (InvalidArgumentException e) {
            // Exception expected
        }
    }
}