
import org.junit.Test;
import static org.junit.Assert.*;

public class ConnectedComponentsTest {
    @Test
    public void testConnectedComponentsConstructor() {
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        assertEquals(ConnectedComponents.FOUR_CONNECTED, cc.getNeighbors());
        assertEquals(0, cc.getBackground());
    }
    
    @Test(expected = InvalidArgumentException.class)
    public void testConnectedComponentsInvalidConstructor() {
        ConnectedComponents cc = new ConnectedComponents(2, 1);
    }
    
    @Test
    public void testApply() {
        BinaryImage image = new BinaryImage(3, 3);
        image.set(0, 0, 1);
        image.set(0, 1, 0);
        image.set(0, 2, 1);
        image.set(1, 0, 0);
        image.set(1, 1, 1);
        image.set(1, 2, 0);
        image.set(2, 0, 1);
        image.set(2, 1, 0);
        image.set(2, 2, 1);
        
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        GrayImage result = cc.apply(image);
        
        assertEquals(1, result.get(0, 0));
        assertEquals(0, result.get(0, 1));
        assertEquals(2, result.get(0, 2));
        assertEquals(0, result.get(1, 0));
        assertEquals(3, result.get(1, 1));
        assertEquals(0, result.get(1, 2));
        assertEquals(4, result.get(2, 0));
        assertEquals(0, result.get(2, 1));
        assertEquals(5, result.get(2, 2));
    }
    
    // Add additional test cases for edge cases and exceptions
    
}

