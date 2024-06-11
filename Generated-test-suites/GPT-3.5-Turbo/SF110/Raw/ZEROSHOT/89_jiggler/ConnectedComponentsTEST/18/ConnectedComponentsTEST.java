
package jigl.image.ops;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import jigl.image.GrayImage;
import jigl.image.BinaryImage;
import jigl.image.exceptions.InvalidArgumentException;

public class ConnectedComponentsTest {

    ConnectedComponents connectedComponents;
    BinaryImage binaryImage;
    
    @Before
    public void setUp() throws Exception {
        connectedComponents = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        binaryImage = new BinaryImage(10, 10);
        // Set up binary image here
    }
    
    @Test
    public void testApply() {
        GrayImage result = connectedComponents.apply(binaryImage);
        // Perform assertion on the result
    }
    
    // Add more test methods to cover all methods in the ConnectedComponents class
    
}
