import org.junit.Test;
import static org.junit.Assert.*;

public class ConnectedComponentsTest {

    @Test
    public void testConstructor() {
        // typical use case
        try {
            ConnectedComponents cc = new ConnectedComponents(4, 0);
            assertEquals(4, cc.neighbors);
            assertEquals(0, cc.background);
        } catch (InvalidArgumentException e) {
            fail("Unexpected exception thrown");
        }
        
        // edge case: minimum values
        try {
            ConnectedComponents cc = new ConnectedComponents(0, 0);
            assertEquals(0, cc.neighbors);
            assertEquals(0, cc.background);
        } catch (InvalidArgumentException e) {
            fail("Unexpected exception thrown");
        }
        
        // edge case: maximum values
        try {
            ConnectedComponents cc = new ConnectedComponents(1, 1);
            assertEquals(1, cc.neighbors);
            assertEquals(1, cc.background);
        } catch (InvalidArgumentException e) {
            fail("Unexpected exception thrown");
        }
        
        // error scenario: invalid numberNeighbors
        try {
            ConnectedComponents cc = new ConnectedComponents(2, 1);
            fail("Expected exception not thrown");
        } catch (InvalidArgumentException e) {
            // expected exception thrown
        }
        
        // error scenario: invalid backgroundColor
        try {
            ConnectedComponents cc = new ConnectedComponents(1, 2);
            fail("Expected exception not thrown");
        } catch (InvalidArgumentException e) {
            // expected exception thrown
        }
    }

    @Test
    public void testApply() {
        BinaryImage image = new BinaryImage(3, 3);
        image.set(0, 0, 1);
        image.set(0, 1, 1);
        image.set(1, 0, 1);
        image.set(1, 2, 1);
        image.set(2, 1, 1);
        
        ConnectedComponents cc = new ConnectedComponents(4, 0);
        GrayImage result = cc.apply(image);
        
        // typical use case: all pixels are connected
        assertEquals(1, result.get(0, 0));
        assertEquals(1, result.get(0, 1));
        assertEquals(1, result.get(1, 0));
        assertEquals(2, result.get(1, 2));
        assertEquals(3, result.get(2, 1));
        
        // edge case: empty image
        image = new BinaryImage(1, 1);
        image.set(0, 0, 0);
        
        result = cc.apply(image);
        
        assertEquals(0, result.get(0, 0));
        
        // edge case: image with only background pixels
        image = new BinaryImage(3, 3);
        image.set(0, 0, 0);
        image.set(0, 1, 0);
        image.set(0, 2, 0);
        image.set(1, 0, 0);
        image.set(1, 1, 0);
        image.set(1, 2, 0);
        image.set(2, 0, 0);
        image.set(2, 1, 0);
        image.set(2, 2, 0);
        
        result = cc.apply(image);
        
        assertEquals(0, result.get(0, 0));
        assertEquals(0, result.get(0, 1));
        assertEquals(0, result.get(0, 2));
        assertEquals(0, result.get(1, 0));
        assertEquals(0, result.get(1, 1));
        assertEquals(0, result.get(1, 2));
        assertEquals(0, result.get(2, 0));
        assertEquals(0, result.get(2, 1));
        assertEquals(0, result.get(2, 2));
        
        // error scenario: invalid number of neighbors
        image = new BinaryImage(3, 3);
        image.set(0, 0, 1);
        image.set(0, 1, 1);
        image.set(1, 0, 1);
        image.set(1, 2, 1);
        image.set(2, 1, 1);
        
        cc = new ConnectedComponents(2, 0);
        result = cc.apply(image);
        
        assertEquals(0, result.get(0, 0));
        assertEquals(0, result.get(0, 1));
        assertEquals(0, result.get(1, 0));
        assertEquals(0, result.get(1, 2));
        assertEquals(0, result.get(2, 1));
        
        // error scenario: invalid background color
        image = new BinaryImage(3, 3);
        image.set(0, 0, 1);
        image.set(0, 1, 1);
        image.set(1, 0, 1);
        image.set(1, 2, 1);
        image.set(2, 1, 1);
        
        cc = new ConnectedComponents(4, 1);
        result = cc.apply(image);
        
        assertEquals(0, result.get(0, 0));
        assertEquals(0, result.get(0, 1));
        assertEquals(0, result.get(1, 0));
        assertEquals(2, result.get(1, 2));
        assertEquals(3, result.get(2, 1));
    }
}