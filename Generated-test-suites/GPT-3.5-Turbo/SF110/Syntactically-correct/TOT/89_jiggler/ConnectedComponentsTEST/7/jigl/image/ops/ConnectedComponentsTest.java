import static org.junit.Assert.*;
import org.junit.Test;
import jigl.image.*;
import jigl.image.exceptions.*;
import jigl.image.ops.*;

public class ConnectedComponentsTest {

    @Test
    public void testConnectedComponents_constructor_validInputs() {
        try {
            ConnectedComponents cc1 = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
            ConnectedComponents cc2 = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 1);
            assertNotNull(cc1);
            assertNotNull(cc2);
        } catch (InvalidArgumentException e) {
            fail("Exception thrown for valid inputs");
        }
    }

    @Test(expected = InvalidArgumentException.class)
    public void testConnectedComponents_constructor_invalidNumNeighbors() throws InvalidArgumentException {
        ConnectedComponents cc = new ConnectedComponents(2, 0);
    }

    @Test(expected = InvalidArgumentException.class)
    public void testConnectedComponents_constructor_invalidBackgroundColor() throws InvalidArgumentException {
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 2);
    }

    @Test
    public void testConnectedComponents_apply_fourConnectedTypical() {
        BinaryImage image = new BinaryImage(4, 4);
        image.set(0, 0, 0);
        image.set(0, 1, 1);
        image.set(0, 2, 1);
        image.set(0, 3, 0);
        image.set(1, 0, 1);
        image.set(1, 1, 0);
        image.set(1, 2, 1);
        image.set(1, 3, 1);
        image.set(2, 0, 1);
        image.set(2, 1, 0);
        image.set(2, 2, 1);
        image.set(2, 3, 1);
        image.set(3, 0, 0);
        image.set(3, 1, 0);
        image.set(3, 2, 1);
        image.set(3, 3, 0);

        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        GrayImage result = cc.apply(image);

        assertEquals(1, result.get(0, 1));
        assertEquals(1, result.get(0, 2));
        assertEquals(2, result.get(1, 0));
        assertEquals(1, result.get(1, 2));
        assertEquals(1, result.get(1, 3));
        assertEquals(3, result.get(2, 0));
        assertEquals(1, result.get(2, 2));
        assertEquals(1, result.get(2, 3));
        assertEquals(4, result.get(3, 2));
    }

    @Test
    public void testConnectedComponents_apply_fourConnectedEdge() {
        BinaryImage image = new BinaryImage(4, 4);
        image.set(0, 0, 0);
        image.set(0, 1, 0);
        image.set(0, 2, 1);
        image.set(0, 3, 1);
        image.set(1, 0, 1);
        image.set(1, 1, 1);
        image.set(1, 2, 0);
        image.set(1, 3, 1);
        image.set(2, 0, 0);
        image.set(2, 1, 1);
        image.set(2, 2, 1);
        image.set(2, 3, 0);
        image.set(3, 0, 0);
        image.set(3, 1, 1);
        image.set(3, 2, 1);
        image.set(3, 3, 1);

        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        GrayImage result = cc.apply(image);

        assertEquals(0, result.get(0, 2));
        assertEquals(0, result.get(0, 3));
        assertEquals(1, result.get(1, 0));
        assertEquals(1, result.get(1, 1));
        assertEquals(1, result.get(1, 3));
        assertEquals(2, result.get(2, 1));
        assertEquals(2, result.get(2, 2));
        assertEquals(3, result.get(2, 3));
        assertEquals(4, result.get(3, 1));
        assertEquals(4, result.get(3, 2));
        assertEquals(4, result.get(3, 3));
    }

    @Test
    public void testConnectedComponents_apply_eightConnectedTypical() {
        BinaryImage image = new BinaryImage(4, 4);
        image.set(0, 0, 0);
        image.set(0, 1, 1);
        image.set(0, 2, 1);
        image.set(0, 3, 0);
        image.set(1, 0, 1);
        image.set(1, 1, 0);
        image.set(1, 2, 1);
        image.set(1, 3, 1);
        image.set(2, 0, 1);
        image.set(2, 1, 0);
        image.set(2, 2, 1);
        image.set(2, 3, 1);
        image.set(3, 0, 0);
        image.set(3, 1, 0);
        image.set(3, 2, 1);
        image.set(3, 3, 0);

        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);
        GrayImage result = cc.apply(image);

        assertEquals(1, result.get(0, 1));
        assertEquals(1, result.get(0, 2));
        assertEquals(2, result.get(1, 0));
        assertEquals(1, result.get(1, 2));
        assertEquals(1, result.get(1, 3));
        assertEquals(3, result.get(2, 0));
        assertEquals(1, result.get(2, 2));
        assertEquals(1, result.get(2, 3));
        assertEquals(4, result.get(3, 2));
    }

    @Test
    public void testConnectedComponents_apply_eightConnectedEdge() {
        BinaryImage image = new BinaryImage(4, 4);
        image.set(0, 0, 0);
        image.set(0, 1, 0);
        image.set(0, 2, 1);
        image.set(0, 3, 1);
        image.set(1, 0, 1);
        image.set(1, 1, 1);
        image.set(1, 2, 0);
        image.set(1, 3, 1);
        image.set(2, 0, 0);
        image.set(2, 1, 1);
        image.set(2, 2, 1);
        image.set(2, 3, 0);
        image.set(3, 0, 0);
        image.set(3, 1, 1);
        image.set(3, 2, 1);
        image.set(3, 3, 1);

        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);
        GrayImage result = cc.apply(image);

        assertEquals(0, result.get(0, 2));
        assertEquals(0, result.get(0, 3));
        assertEquals(1, result.get(1, 0));
        assertEquals(1, result.get(1, 1));
        assertEquals(1, result.get(1, 3));
        assertEquals(2, result.get(2, 1));
        assertEquals(2, result.get(2, 2));
        assertEquals(3, result.get(2, 3));
        assertEquals(4, result.get(3, 1));
        assertEquals(4, result.get(3, 2));
        assertEquals(4, result.get(3, 3));
    }

}