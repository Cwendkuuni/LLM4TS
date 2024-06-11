import org.junit.Test;
import static org.junit.Assert.*;

public class ConnectedComponentsTest {

    @Test
    public void testConstructorValidInputs() {
        try {
            // Test constructor with valid inputs
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
            assertNotNull(cc);
        } catch (InvalidArgumentException e) {
            fail("Constructor threw an exception for valid inputs");
        }
    }
    
    @Test(expected = InvalidArgumentException.class)
    public void testConstructorInvalidInputs() throws InvalidArgumentException {
        // Test constructor with invalid inputs
        ConnectedComponents cc = new ConnectedComponents(2, 3);
    }
    
    // Add more test cases for the constructor
    
    @Test
    public void testApplyAllBackground() {
        // Test apply method with a BinaryImage consisting of all background pixels
        
        // Create a BinaryImage consisting of all background pixels
        BinaryImage image = new BinaryImage(10, 10);
        for (int x = 0; x < image.X(); x++) {
            for (int y = 0; y < image.Y(); y++) {
                image.set(x, y, 0);
            }
        }
        
        // Apply the ConnectedComponents operator
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        GrayImage result = cc.apply(image);
        
        // Assert the result
        // ...
    }

    // Add more test cases for the apply method
    
    // More test methods for other methods
    
    // 
    
    // Test methods
    
    // 
}