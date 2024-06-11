import static org.junit.Assert.*;
import org.junit.Test;
import jigl.image.*;
import jigl.image.ops.ConnectedComponents;
import jigl.image.exceptions.InvalidArgumentException;

public class ConnectedComponentsTest {

    @Test
    public void testConstructorThrowsArgumentException() {
        // Test case where numberNeighbors and backgroundColor are both invalid
        try {
            ConnectedComponents cc = new ConnectedComponents(2, 2);
            fail("Expected InvalidArgumentException to be thrown");
        } catch (InvalidArgumentException e) {
            assertEquals("Expected exception message: Invalid arguments", e.getMessage());
        }
    }
    
    @Test
    public void testApplyMethodWithFourConnectedAndBackgroundColorZero() {
        // Test case where numberNeighbors is FOUR_CONNECTED and backgroundColor is 0
        try {
            // Create a binary image with 5x5 dimensions
            BinaryImage image = new BinaryImage(5, 5);
            
            // Set some pixels to the background color (0)
            image.set(0, 0, 0);
            image.set(2, 0, 0);
            image.set(0, 2, 0);
            image.set(4, 4, 0);
            
            // Set some pixels to the foreground color (1)
            image.set(1, 0, 1);
            image.set(3, 0, 1);
            image.set(1, 2, 1);
            image.set(2, 4, 1);
            
            // Create ConnectedComponents object
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
            
            // Apply the connected components operation on the image
            GrayImage result = cc.apply(image);
            
            // Check the resulting gray image
            assertEquals(0, result.get(0, 0));
            assertEquals(1, result.get(1, 0));
            assertEquals(1, result.get(2, 0));
            assertEquals(2, result.get(0, 2));
            assertEquals(3, result.get(1, 2));
            assertEquals(4, result.get(2, 4));
            assertEquals(0, result.get(4, 4));
        } catch (InvalidArgumentException e) {
            fail("Caught unexpected InvalidArgumentException: " + e.getMessage());
        }
    }
    
    @Test
    public void testApplyMethodWithEightConnectedAndBackgroundColorOne() {
        // Test case where numberNeighbors is EIGHT_CONNECTED and backgroundColor is 1
        try {
            // Create a binary image with 3x3 dimensions
            BinaryImage image = new BinaryImage(3, 3);
            
            // Set some pixels to the background color (1)
            image.set(0, 1, 1);
            image.set(1, 0, 1);
            image.set(2, 1, 1);
            
            // Set some pixels to the foreground color (0)
            image.set(0, 0, 0);
            image.set(1, 1, 0);
            image.set(2, 2, 0);
            
            // Create ConnectedComponents object
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 1);
            
            // Apply the connected components operation on the image
            GrayImage result = cc.apply(image);
            
            // Check the resulting gray image
            assertEquals(0, result.get(0, 0));
            assertEquals(1, result.get(1, 1));
            assertEquals(1, result.get(0, 1));
            assertEquals(1, result.get(2, 1));
            assertEquals(2, result.get(1, 0));
            assertEquals(3, result.get(2, 2));
        } catch (InvalidArgumentException e) {
            fail("Caught unexpected InvalidArgumentException: " + e.getMessage());
        }
    }
    
    @Test
    public void testApplyMethodWithEdgeCases() {
        // Test cases with edge inputs
        
        try {
            // Create a binary image with minimum dimensions (1x1)
            BinaryImage minImage = new BinaryImage(1, 1);
            minImage.set(0, 0, 0);
            
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
            GrayImage minResult = cc.apply(minImage);
            assertEquals(0, minResult.get(0, 0));
            
            cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 1);
            minResult = cc.apply(minImage);
            assertEquals(1, minResult.get(0, 0));
            
            // Create a binary image with maximum dimensions (1000x1000)
            BinaryImage maxImage = new BinaryImage(1000, 1000);
            
            cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 1);
            GrayImage maxResult = cc.apply(maxImage);
            assertEquals(0, maxResult.get(0, 0));
            assertEquals(0, maxResult.get(999, 999));
            
        } catch (InvalidArgumentException e) {
            fail("Caught unexpected InvalidArgumentException: " + e.getMessage());
        }
    }
    
    @Test
    public void testApplyMethodWithErrorScenarios() {
        // Test cases with error scenarios
        
        try {
            // Create a binary image with null dimensions
            BinaryImage nullImage = null;
            
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
            GrayImage result = cc.apply(nullImage);
            assertNull(result);
            
            cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 1);
            result = cc.apply(nullImage);
            assertNull(result);
            
            // Create a binary image with dimensions exceeding the limit
            BinaryImage largeImage = new BinaryImage(1000000, 1000000);
            
            cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
            result = cc.apply(largeImage);
            assertNull(result);
            
            cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 1);
            result = cc.apply(largeImage);
            assertNull(result);
            
        } catch (InvalidArgumentException e) {
            fail("Caught unexpected InvalidArgumentException: " + e.getMessage());
        }
    }
}