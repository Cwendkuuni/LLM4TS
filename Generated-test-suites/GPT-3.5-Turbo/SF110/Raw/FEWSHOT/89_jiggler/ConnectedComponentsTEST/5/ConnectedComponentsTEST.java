
package jigl.image.ops;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import jigl.image.BinaryImage;
import jigl.image.GrayImage;
import jigl.image.exceptions.InvalidArgumentException;
import jigl.image.types.ImageTypeException;
import org.junit.Before;
import org.junit.Test;

public class ConnectedComponentsTest {
    private ConnectedComponents connectedComponents;

    @Before
    public void setUp() throws InvalidArgumentException {
        connectedComponents = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);
    }

    @Test(expected = InvalidArgumentException.class)
    public void testInvalidConstructorParameters() throws InvalidArgumentException {
        new ConnectedComponents(2, 1);
    }

    @Test
    public void testConnectedComponentsFourConnected() throws ImageTypeException {
        BinaryImage image = new BinaryImage(3, 3);
        image.set(0, 0, 0);
        image.set(0, 1, 1);
        image.set(0, 2, 0);
        image.set(1, 0, 1);
        image.set(1, 1, 1);
        image.set(1, 2, 0);
        image.set(2, 0, 0);
        image.set(2, 1, 0);
        image.set(2, 2, 1);

        GrayImage result = connectedComponents.apply(image);

        assertNotNull(result);
        assertEquals(1, result.get(0, 0));
        assertEquals(2, result.get(0, 1));
        assertEquals(3, result.get(0, 2));
        assertEquals(4, result.get(1, 0));
        assertEquals(4, result.get(1, 1));
        assertEquals(3, result.get(1, 2));
        assertEquals(5, result.get(2, 0));
        assertEquals(5, result.get(2, 1));
        assertEquals(6, result.get(2, 2));
    }

    @Test
    public void testConnectedComponentsEightConnected() throws ImageTypeException {
        BinaryImage image = new BinaryImage(3, 3);
        image.set(0, 0, 0);
        image.set(0, 1, 1);
        image.set(0, 2, 0);
        image.set(1, 0, 1);
        image.set(1, 1, 1);
        image.set(1, 2, 0);
        image.set(2, 0, 0);
        image.set(2, 1, 0);
        image.set(2, 2, 1);

        ConnectedComponents connectedComponentsEight = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);
        GrayImage result = connectedComponentsEight.apply(image);

        assertNotNull(result);
        assertEquals(1, result.get(0, 0));
        assertEquals(2, result.get(0, 1));
        assertEquals(3, result.get(0, 2));
        assertEquals(4, result.get(1, 0));
        assertEquals(4, result.get(1, 1));
        assertEquals(3, result.get(1, 2));
        assertEquals(5, result.get(2, 0));
        assertEquals(5, result.get(2, 1));
        assertEquals(6, result.get(2, 2));
    }
}
