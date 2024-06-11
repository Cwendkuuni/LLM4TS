package jigl.image.ops;

import jigl.image.exceptions.InvalidArgumentException;
import jigl.image.types.BinaryImage;
import jigl.image.types.GrayImage;
import junit.framework.TestCase;
import org.junit.Test;

public class ConnectedComponentsTest extends TestCase {

    @Test
    public void testConstructorValidInput() {
        try {
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
            assertEquals(0, cc.neighbors);
            assertEquals(0, cc.background);

            ConnectedComponents cc2 = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 1);
            assertEquals(1, cc2.neighbors);
            assertEquals(1, cc2.background);
        } catch (InvalidArgumentException e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testConstructorInvalidInput() {
        try {
            ConnectedComponents cc = new ConnectedComponents(2, 0);
            fail("Expected InvalidArgumentException");
        } catch (InvalidArgumentException e) {
            // expected exception
        }

        try {
            ConnectedComponents cc2 = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 2);
            fail("Expected InvalidArgumentException");
        } catch (InvalidArgumentException e) {
            // expected exception
        }
    }

    @Test
    public void testApply() {
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        BinaryImage image = new BinaryImage(2, 2);
        image.set(0, 0, 0);
        image.set(0, 1, 1);
        image.set(1, 0, 1);
        image.set(1, 1, 0);

        GrayImage gimage = cc.apply(image);

        assertEquals(1, gimage.get(0, 0));
        assertEquals(0, gimage.get(0, 1));
        assertEquals(0, gimage.get(1, 0));
        assertEquals(2, gimage.get(1, 1));
    }
}