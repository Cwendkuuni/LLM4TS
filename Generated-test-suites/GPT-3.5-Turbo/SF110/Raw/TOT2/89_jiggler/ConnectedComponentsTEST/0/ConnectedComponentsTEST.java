import org.junit.Test;
import static org.junit.Assert.*;

public class ConnectedComponentsTest {

    @Test
    public void testConstructorValidInput() {
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        assertNotNull(cc);
    }

    @Test(expected = InvalidArgumentException.class)
    public void testConstructorInvalidInput() {
        ConnectedComponents cc = new ConnectedComponents(2, 3);
    }

    @Test
    public void testApply() {
        BinaryImage image = new BinaryImage(2, 2);
        image.set(0, 0, 1);
        image.set(1, 1, 1);
        try {
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
            GrayImage result = cc.apply(image);
            assertNotNull(result);
        } catch (InvalidArgumentException e) {
            fail("Exception thrown when not expected");
        }
    }

    @Test
    public void testApplyEdgeCases() {
        BinaryImage image = new BinaryImage(1, 1);
        image.set(0, 0, 1);
        try {
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
            GrayImage result = cc.apply(image);
            assertNotNull(result);
        } catch (InvalidArgumentException e) {
            fail("Exception thrown when not expected");
        }
    }

    @Test(expected = InvalidArgumentException.class)
    public void testApplyException() {
        BinaryImage image = new BinaryImage(2, 2);
        image.set(0, 0, 1);
        try {
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
            GrayImage result = cc.apply(image);
        } catch (InvalidArgumentException e) {
            fail("Exception thrown when not expected");
        }
    }

}