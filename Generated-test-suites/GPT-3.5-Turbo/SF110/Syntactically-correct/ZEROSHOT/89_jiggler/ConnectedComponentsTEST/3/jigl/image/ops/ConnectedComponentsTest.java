import static org.junit.Assert.*;
import org.junit.Test;

public class ConnectedComponentsTest {

    @Test
    public void testApply() {
        BinaryImage image = new BinaryImage(2, 2);
        image.set(0, 0, 0);
        image.set(0, 1, 1);
        image.set(1, 0, 1);
        image.set(1, 1, 0);
        
        ConnectedComponents cc = new ConnectedComponents(0, 0);
        
        GrayImage result = cc.apply(image);
        
        assertEquals(1, result.get(0, 0));
        assertEquals(2, result.get(0, 1));
        assertEquals(2, result.get(1, 0));
        assertEquals(3, result.get(1, 1));
    }
    
    @Test
    public void testConstructorInvalidArguments() {
        try {
            ConnectedComponents cc = new ConnectedComponents(2, 0);
            fail("InvalidArgumentException should have been thrown");
        } catch (InvalidArgumentException e) {
            assertEquals("InvalidArgumentException", e.getMessage());
        }
    }
    
    @Test
    public void testConstructorValidArguments() {
        ConnectedComponents cc = new ConnectedComponents(0, 1);
        
        assertEquals(0, cc.neighbors);
        assertEquals(1, cc.background);
    }
}