package jigl.image.ops;

import jigl.image.BinaryImage;
import jigl.image.GrayImage;
import jigl.image.exceptions.InvalidArgumentException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ConnectedComponentsTest {

    @Test
    public void testConnectedComponentsConstructor() {
        try {
            ConnectedComponents connectedComponents = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
            assertNotNull(connectedComponents);
        } catch (InvalidArgumentException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    public void testConnectedComponentsApply() {
        ConnectedComponents connectedComponents = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        BinaryImage binaryImage = new BinaryImage(2, 2);
        binaryImage.set(0, 0, 1);
        binaryImage.set(0, 1, 0);
        binaryImage.set(1, 0, 0);
        binaryImage.set(1, 1, 1);

        GrayImage result = connectedComponents.apply(binaryImage);

        assertNotNull(result);
        assertEquals(1, result.get(0, 0).intValue());
        assertEquals(0, result.get(0, 1).intValue());
        assertEquals(0, result.get(1, 0).intValue());
        assertEquals(2, result.get(1, 1).intValue());
    }
}