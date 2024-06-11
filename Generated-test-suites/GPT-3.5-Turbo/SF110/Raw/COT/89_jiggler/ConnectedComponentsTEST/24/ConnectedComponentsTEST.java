import org.junit.*;
import static org.junit.Assert.*;

public class ConnectedComponentsTest {
    
    @Test
    public void testApply() {
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        BinaryImage image = new BinaryImage(3, 3);
        image.set(0, 0, 0);
        image.set(0, 1, 1);
        image.set(0, 2, 0);
        image.set(1, 0, 1);
        image.set(1, 1, 0);
        image.set(1, 2, 1);
        image.set(2, 0, 0);
        image.set(2, 1, 1);
        image.set(2, 2, 0);
        
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
    
    @Test
    public void testConstructorValidArgs() {
        try {
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
            assertNotNull(cc);
        } catch (InvalidArgumentException e) {
            fail();
        }
    }
    
    @Test(expected=InvalidArgumentException.class)
    public void testConstructorInvalidArgs() throws InvalidArgumentException {
        ConnectedComponents cc = new ConnectedComponents(2, 2);
    }
}