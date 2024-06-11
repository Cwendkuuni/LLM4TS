import jigl.image.*;
import jigl.image.exceptions.*;
import jigl.image.ops.*;
import jigl.image.types.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ConnectedComponentsTest {

    @Test
    public void testConstructor() {
        try {
            ConnectedComponents connectedComponents = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
            assertNotNull(connectedComponents);
        } catch (InvalidArgumentException e) {
            fail("Should not throw exception");
        }
    }

    @Test
    public void testConstructorInvalidArgs() {
        try {
            ConnectedComponents connectedComponents = new ConnectedComponents(2, 0);
            fail("Should throw exception");
        } catch (InvalidArgumentException e) {
            // success
        }
    }

    @Test
    public void testApply() {
        try {
            BinaryImage image = new BinaryImage(5, 5);
            image.set(0, 0, 1);
            image.set(2, 2, 1);
            image.set(4, 4, 1);

            ConnectedComponents connectedComponents = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
            GrayImage result = connectedComponents.apply(image);

            assertEquals(1, result.get(0, 0));
            assertEquals(2, result.get(2, 2));
            assertEquals(3, result.get(4, 4));
        } catch (InvalidArgumentException e) {
            fail("Should not throw exception");
        }
    }
}