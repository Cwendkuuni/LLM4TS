
import static org.junit.Assert.*;
import org.junit.Test;
import jigl.image.*;
import jigl.image.exceptions.InvalidArgumentException;

public class ConnectedComponentsTest {
    
    @Test
    public void testConstructor() {
        try {
            ConnectedComponents cc = new ConnectedComponents(0, 1);
            assertTrue(cc != null);
        } catch (InvalidArgumentException e) {
            fail("Constructor threw exception");
        }
    }
    
    @Test
    public void testConstructorWithInvalidNeighbors() {
        try {
            ConnectedComponents cc = new ConnectedComponents(2, 1);
            fail("Expected exception not thrown");
        } catch (InvalidArgumentException e) {
            assertEquals(e.getClass(), InvalidArgumentException.class);
        }
    }
    
    @Test
    public void testConstructorWithInvalidBackground() {
        try {
            ConnectedComponents cc = new ConnectedComponents(0, 2);
            fail("Expected exception not thrown");
        } catch (InvalidArgumentException e) {
            assertEquals(e.getClass(), InvalidArgumentException.class);
        }
    }
    
    @Test
    public void testApply() {
        BinaryImage image = new BinaryImage(10, 10);
        image.set(0, 0, 1);
        image.set(1, 0, 1);
        image.set(0, 1, 1);
        image.set(1, 1, 1);
        
        ConnectedComponents cc = new ConnectedComponents(FOUR_CONNECTED, 0);
        GrayImage result = cc.apply(image);
        
        assertEquals(result.get(0, 0), 1);
        assertEquals(result.get(1, 0), 1);
        assertEquals(result.get(0, 1), 1);
        assertEquals(result.get(1, 1), 1);
    }
    
    @Test
    public void testApplyWithEightConnected() {
        BinaryImage image = new BinaryImage(10, 10);
        image.set(0, 0, 1);
        image.set(1, 0, 1);
        image.set(0, 1, 1);
        image.set(1, 1, 1);
        
        ConnectedComponents cc = new ConnectedComponents(EIGHT_CONNECTED, 0);
        GrayImage result = cc.apply(image);
        
        assertEquals(result.get(0, 0), 1);
        assertEquals(result.get(1, 0), 1);
        assertEquals(result.get(0, 1), 1);
        assertEquals(result.get(1, 1), 1);
    }
    
}
