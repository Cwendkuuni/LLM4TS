package jigl.image.ops;

import static org.junit.Assert.*;
import org.junit.Test;
import jigl.image.exceptions.InvalidArgumentException;

public class ConnectedComponentsTest {
    
    @Test
    public void testConstructor() {
        // Test valid constructor with FOUR_CONNECTED and background 0
        try {
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
            assertEquals(ConnectedComponents.FOUR_CONNECTED, cc.neighbors);
            assertEquals(0, cc.background);
        } catch (InvalidArgumentException e) {
            fail("Should not throw InvalidArgumentException");
        }
        
        // Test valid constructor with FOUR_CONNECTED and background 1
        try {
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 1);
            assertEquals(ConnectedComponents.FOUR_CONNECTED, cc.neighbors);
            assertEquals(1, cc.background);
        } catch (InvalidArgumentException e) {
            fail("Should not throw InvalidArgumentException");
        }
        
        // Test valid constructor with EIGHT_CONNECTED and background 0
        try {
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);
            assertEquals(ConnectedComponents.EIGHT_CONNECTED, cc.neighbors);
            assertEquals(0, cc.background);
        } catch (InvalidArgumentException e) {
            fail("Should not throw InvalidArgumentException");
        }
        
        // Test valid constructor with EIGHT_CONNECTED and background 1
        try {
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 1);
            assertEquals(ConnectedComponents.EIGHT_CONNECTED, cc.neighbors);
            assertEquals(1, cc.background);
        } catch (InvalidArgumentException e) {
            fail("Should not throw InvalidArgumentException");
        }
        
        // Test invalid constructor with invalid numberNeighbors and valid background
        try {
            ConnectedComponents cc = new ConnectedComponents(2, 1);
            fail("Should throw InvalidArgumentException");
        } catch (InvalidArgumentException e) {
            // expected
        }
        
        // Test invalid constructor with valid numberNeighbors and invalid background
        try {
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 2);
            fail("Should throw InvalidArgumentException");
        } catch (InvalidArgumentException e) {
            // expected
        }
    }
    
    @Test
    public void testApply() {
        // Test apply() method with BinaryImage
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        BinaryImage image = new BinaryImage(10, 10);
        image.set(0, 0, 1);
        image.set(1, 1, 1);
        GrayImage result = cc.apply(image);
        
        // Verify result
        assertEquals(1, result.get(0, 0));
        assertEquals(2, result.get(1, 1));
    }

}