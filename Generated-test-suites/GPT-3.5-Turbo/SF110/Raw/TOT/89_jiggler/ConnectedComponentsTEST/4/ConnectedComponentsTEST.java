import static org.junit.Assert.*;
import org.junit.Test;
import jigl.image.*;
import jigl.image.exceptions.*;

public class ConnectedComponentsTest {

    @Test
    public void testConstructor() {
        try {
            ConnectedComponents cc = new ConnectedComponents(0, 1);
            assertNotNull(cc);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test(expected = InvalidArgumentException.class)
    public void testConstructorInvalidArguments() throws InvalidArgumentException {
        ConnectedComponents cc = new ConnectedComponents(2, 1);
    }

    @Test
    public void testApplyFourConnected() {
        BinaryImage image = new BinaryImage(4, 4);
        image.clear();
        image.set(0, 0, true);
        image.set(0, 2, true);
        image.set(2, 0, true);
        image.set(2, 2, true);
        ConnectedComponents cc = new ConnectedComponents(0, 0);
        GrayImage result = cc.apply(image);
        assertEquals(result.X(), 4);
        assertEquals(result.Y(), 4);
        assertEquals(result.get(0, 0), Integer.valueOf(1));
        assertEquals(result.get(0, 1), Integer.valueOf(0));
        assertEquals(result.get(0, 2), Integer.valueOf(2));
        assertEquals(result.get(0, 3), Integer.valueOf(0));
        assertEquals(result.get(1, 0), Integer.valueOf(0));
        assertEquals(result.get(1, 1), Integer.valueOf(0));
        assertEquals(result.get(1, 2), Integer.valueOf(0));
        assertEquals(result.get(1, 3), Integer.valueOf(0));
        assertEquals(result.get(2, 0), Integer.valueOf(3));
        assertEquals(result.get(2, 1), Integer.valueOf(0));
        assertEquals(result.get(2, 2), Integer.valueOf(4));
        assertEquals(result.get(2, 3), Integer.valueOf(0));
        assertEquals(result.get(3, 0), Integer.valueOf(0));
        assertEquals(result.get(3, 1), Integer.valueOf(0));
        assertEquals(result.get(3, 2), Integer.valueOf(0));
        assertEquals(result.get(3, 3), Integer.valueOf(0));
    }

    @Test
    public void testApplyEightConnected() {
        BinaryImage image = new BinaryImage(4, 4);
        image.clear();
        image.set(0, 0, true);
        image.set(0, 2, true);
        image.set(2, 0, true);
        image.set(2, 2, true);
        ConnectedComponents cc = new ConnectedComponents(1, 0);
        GrayImage result = cc.apply(image);
        assertEquals(result.X(), 4);
        assertEquals(result.Y(), 4);
        assertEquals(result.get(0, 0), Integer.valueOf(1));
        assertEquals(result.get(0, 1), Integer.valueOf(0));
        assertEquals(result.get(0, 2), Integer.valueOf(1));
        assertEquals(result.get(0, 3), Integer.valueOf(0));
        assertEquals(result.get(1, 0), Integer.valueOf(0));
        assertEquals(result.get(1, 1), Integer.valueOf(0));
        assertEquals(result.get(1, 2), Integer.valueOf(0));
        assertEquals(result.get(1, 3), Integer.valueOf(0));
        assertEquals(result.get(2, 0), Integer.valueOf(1));
        assertEquals(result.get(2, 1), Integer.valueOf(0));
        assertEquals(result.get(2, 2), Integer.valueOf(1));
        assertEquals(result.get(2, 3), Integer.valueOf(0));
        assertEquals(result.get(3, 0), Integer.valueOf(0));
        assertEquals(result.get(3, 1), Integer.valueOf(0));
        assertEquals(result.get(3, 2), Integer.valueOf(0));
        assertEquals(result.get(3, 3), Integer.valueOf(0));
    }

}