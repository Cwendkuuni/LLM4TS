import org.junit.Test;
import jigl.image.exceptions.InvalidArgumentException;

public class ConnectedComponentsTest {

    @Test
    public void testConstructor() {
        try {
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        } catch (InvalidArgumentException e) {
            fail("Unexpected InvalidArgumentException");
        } catch (Exception e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testApply() {
        BinaryImage image = new BinaryImage(3, 3);
        image.set(0, 0, 1);
        image.set(1, 0, 0);
        image.set(2, 0, 1);
        image.set(0, 1, 0);
        image.set(1, 1, 1);
        image.set(2, 1, 0);
        image.set(0, 2, 1);
        image.set(1, 2, 1);
        image.set(2, 2, 1);
        
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        GrayImage result = cc.apply(image);
        
        assertNotNull(result);
        assertEquals(3, result.X());
        assertEquals(3, result.Y());
        
        // test result values
        assertEquals(0, result.get(0, 0).intValue());
        assertEquals(0, result.get(1, 0).intValue());
        assertEquals(0, result.get(2, 0).intValue());
        assertEquals(0, result.get(0, 1).intValue());
        assertEquals(1, result.get(1, 1).intValue());
        assertEquals(0, result.get(2, 1).intValue());
        assertEquals(0, result.get(0, 2).intValue());
        assertEquals(2, result.get(1, 2).intValue());
        assertEquals(2, result.get(2, 2).intValue());
    }
    
    // Additional test cases for edge cases and exceptions not covered by basic test cases
    
    @Test
    public void testConstructor_invalidArguments() {
        try {
            ConnectedComponents cc = new ConnectedComponents(3, 0);
            fail("Expected InvalidArgumentException");
        } catch (InvalidArgumentException e) {
            // Expected exception
        } catch (Exception e) {
            fail("Unexpected exception");
        }
        try {
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 2);
            fail("Expected InvalidArgumentException");
        } catch (InvalidArgumentException e) {
            // Expected exception
        } catch (Exception e) {
            fail("Unexpected exception");
        }
    }
    
    @Test
    public void testApply_emptyImage() {
        BinaryImage image = new BinaryImage(0, 0);
        
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        GrayImage result = cc.apply(image);
        
        assertNotNull(result);
        assertEquals(0, result.X());
        assertEquals(0, result.Y());
    }
}