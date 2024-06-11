import jigl.image.*;
import jigl.image.types.*;
import jigl.image.exceptions.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class ConnectedComponentsTest {

    @Test
    public void testConstructorValidArgs() {
        try {
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 1);
            assertNotNull(cc);
        } catch (InvalidArgumentException e) {
            fail("InvalidArgumentException should not be thrown");
        }
    }

    @Test(expected = InvalidArgumentException.class)
    public void testConstructorInvalidNeighbor() {
        ConnectedComponents cc = new ConnectedComponents(2, 1);
    }

    @Test(expected = InvalidArgumentException.class)
    public void testConstructorInvalidBackground() {
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 2);
    }

    @Test
    public void testApplyFourConnected() {
        BinaryImage image = new BinaryImage(10, 10);
        image.set(1, 1, 1);
        image.set(1, 2, 1);
        image.set(2, 1, 1);
        image.set(9, 9, 1);
        image.set(9, 8, 1);
        image.set(8, 9, 1);
        
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        GrayImage result = cc.apply(image);
        
        assertEquals(1, result.get(1, 1));
        assertEquals(1, result.get(1, 2));
        assertEquals(1, result.get(2, 1));
        assertEquals(2, result.get(9, 9));
        assertEquals(2, result.get(9, 8));
        assertEquals(2, result.get(8, 9));
    }

    @Test
    public void testApplyEightConnected() {
        BinaryImage image = new BinaryImage(10, 10);
        image.set(4, 4, 1);
        image.set(4, 5, 1);
        image.set(5, 4, 1);
        image.set(5, 5, 1);
        
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);
        GrayImage result = cc.apply(image);
        
        assertEquals(1, result.get(4, 4));
        assertEquals(1, result.get(4, 5));
        assertEquals(1, result.get(5, 4));
        assertEquals(1, result.get(5, 5));
    }

}