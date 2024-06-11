import static org.junit.Assert.*;
import org.junit.Test;

public class ConnectedComponentsTest {

    @Test
    public void testConstructor() {
        try {
            ConnectedComponents cc = new ConnectedComponents(0, 0);
            assertTrue(true);
        } catch (Exception e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testApply() {
        BinaryImage image = new BinaryImage(3, 3);
        image.set(0, 0, 1);
        image.set(0, 1, 0);
        image.set(0, 2, 1);
        image.set(1, 0, 1);
        image.set(1, 1, 0);
        image.set(1, 2, 1);
        image.set(2, 0, 0);
        image.set(2, 1, 0);
        image.set(2, 2, 1);
        
        ConnectedComponents cc = new ConnectedComponents(4, 0);
        GrayImage result = cc.apply(image);
        
        assertEquals(1, result.get(0, 0));
        assertEquals(1, result.get(0, 1));
        assertEquals(2, result.get(0, 2));
        assertEquals(1, result.get(1, 0));
        assertEquals(1, result.get(1, 1));
        assertEquals(2, result.get(1, 2));
        assertEquals(0, result.get(2, 0));
        assertEquals(0, result.get(2, 1));
        assertEquals(3, result.get(2, 2));
    }

    @Test
    public void testApplyWithException() {
        BinaryImage image = new BinaryImage(3, 3);
        image.set(0, 0, 1);
        image.set(0, 1, 0);
        image.set(0, 2, 1);
        image.set(1, 0, 1);
        image.set(1, 1, 2);
        image.set(1, 2, 1);
        image.set(2, 0, 0);
        image.set(2, 1, 0);
        image.set(2, 2, 1);
        
        try {
            ConnectedComponents cc = new ConnectedComponents(4, 0);
            GrayImage result = cc.apply(image);
            fail("No exception thrown");
        } catch (Exception e) {
            assertTrue(true);
        }
    }

}