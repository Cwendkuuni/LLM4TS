
import org.junit.Test;
import static org.junit.Assert.*;

public class ConnectedComponentsTest {

    @Test
    public void testConnectedComponents() {
        // Test case for ConnectedComponents constructor
        try {
            ConnectedComponents cc = new ConnectedComponents(0, 0);
        } catch (InvalidArgumentException e) {
            fail("InvalidArgumentException should not be thrown.");
        }
    }

    @Test
    public void testApply() {
        // Test case for apply method
        BinaryImage image = new BinaryImage(3, 3);
        image.set(1, 1, 1);
        
        ConnectedComponents cc = new ConnectedComponents(0, 0);
        GrayImage result = cc.apply(image);
        
        assertNotNull(result);
        assertEquals(1, result.get(1, 1).intValue());
    }

    @Test
    public void testApplyEdgeCases() {
        // Test case for apply method edge cases
        BinaryImage image = new BinaryImage(1, 1);
        image.set(0, 0, 1);

        ConnectedComponents cc = new ConnectedComponents(0, 0);
        GrayImage result = cc.apply(image);

        assertNotNull(result);
        assertEquals(1, result.get(0, 0).intValue());
    }

    @Test
    public void testApplyExceptionHandling() {
        // Test case for invalid arguments in constructor
        try {
            ConnectedComponents cc = new ConnectedComponents(2, 2);
            fail("InvalidArgumentException should be thrown for invalid arguments.");
        } catch (InvalidArgumentException e) {
            // exception expected
        }
    }
}
