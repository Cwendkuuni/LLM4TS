import static org.junit.Assert.*;
import org.junit.Test;

public class ConnectedComponentsTest {
    
    @Test
    public void testConstructor_validInputs() {
        try {
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 1);
        } catch (InvalidArgumentException e) {
            fail("Valid inputs should not throw an exception");
        }
    }
    
    @Test(expected = InvalidArgumentException.class)
    public void testConstructor_invalidInputs() {
        ConnectedComponents cc = new ConnectedComponents(2, 3);
    }
    
    @Test
    public void testApply_fourConnected() {
        BinaryImage image = new BinaryImage(2, 2);
        image.set(0, 0, 1);
        image.set(0, 1, 1);
        image.set(1, 0, 0);
        image.set(1, 1, 1);
        
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        GrayImage result = cc.apply(image);
        
        assertEquals(Integer.valueOf(1), result.get(0, 0));
        assertEquals(Integer.valueOf(1), result.get(0, 1));
        assertEquals(Integer.valueOf(0), result.get(1, 0));
        assertEquals(Integer.valueOf(2), result.get(1, 1));
    }
    
    @Test
    public void testApply_eightConnected() {
        BinaryImage image = new BinaryImage(2, 2);
        image.set(0, 0, 1);
        image.set(0, 1, 1);
        image.set(1, 0, 0);
        image.set(1, 1, 1);
        
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);
        GrayImage result = cc.apply(image);
        
        assertEquals(Integer.valueOf(1), result.get(0, 0));
        assertEquals(Integer.valueOf(1), result.get(0, 1));
        assertEquals(Integer.valueOf(0), result.get(1, 0));
        assertEquals(Integer.valueOf(1), result.get(1, 1));
    }
    
    @Test
    public void testApply_edgeCases() {
        // Add edge cases test scenarios here
    }
    
    @Test
    public void testApply_exceptionHandling() {
        // Add exception handling test scenarios here
    }
}