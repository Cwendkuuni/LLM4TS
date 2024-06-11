package jigl.image.ops;

import static org.junit.Assert.*;
import org.junit.Test;
import jigl.image.types.BinaryImage;
import jigl.image.types.GrayImage;;

public class ConnectedComponentsTest {
    
    @Test
    public void testApply() {
        // Create a test binary image
        BinaryImage image = new BinaryImage(3, 3);
        image.set(0, 0, 1);
        image.set(0, 1, 0);
        image.set(0, 2, 0);
        image.set(1, 0, 1);
        image.set(1, 1, 0);
        image.set(1, 2, 1);
        image.set(2, 0, 1);
        image.set(2, 1, 1);
        image.set(2, 2, 0);
        
        // Create the expected output gray image
        GrayImage expected = new GrayImage(3, 3);
        expected.set(0, 0, 1);
        expected.set(0, 1, 0);
        expected.set(0, 2, 0);
        expected.set(1, 0, 1);
        expected.set(1, 1, 0);
        expected.set(1, 2, 2);
        expected.set(2, 0, 1);
        expected.set(2, 1, 1);
        expected.set(2, 2, 0);
        
        // Create the ConnectedComponents object
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);
        
        // Apply the operator and check for the expected output
        GrayImage output = cc.apply(image);
        assertEquals(expected, output);
    }
    
    @Test(expected = InvalidArgumentException.class)
    public void testInvalidArguments() {
        // Test for invalid arguments in the constructor
        new ConnectedComponents(2, 0);
    }
    
    @Test
    public void testOtherMethods() {
        // Test other methods in the class
        
        // Test getters
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 1);
        assertEquals(ConnectedComponents.FOUR_CONNECTED, cc.getNeighbors());
        assertEquals(1, cc.getBackground());
        
        // Test setters
        cc.setNeighbors(ConnectedComponents.EIGHT_CONNECTED);
        cc.setBackground(0);
        assertEquals(ConnectedComponents.EIGHT_CONNECTED, cc.getNeighbors());
        assertEquals(0, cc.getBackground());
    }
}