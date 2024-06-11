import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ConnectedComponentsTest {
    
    private ConnectedComponents connectedComponents;
    
    @Before
    public void setup() throws InvalidArgumentException {
        connectedComponents = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 1);
    }
    
    @Test
    public void testApply_validBinaryImage_returnGrayImage() {
        BinaryImage binaryImage = new BinaryImage(10, 10);
        binaryImage.set(0, 0, 1);
        binaryImage.set(0, 1, 1);
        binaryImage.set(1, 0, 1);
        binaryImage.set(1, 1, 1);
        
        GrayImage expected = new GrayImage(10, 10);
        expected.set(0, 0, 1);
        expected.set(0, 1, 1);
        expected.set(1, 0, 1);
        expected.set(1, 1, 1);
        
        GrayImage result = connectedComponents.apply(binaryImage);
        
        assertEquals(expected, result);
    }
    
    // Edge cases and exception handling scenarios
    
    @Test(expected = InvalidArgumentException.class)
    public void testConstructor_invalidNumberNeighbors_throwsInvalidArgumentException() throws InvalidArgumentException {
        new ConnectedComponents(2, 0);
    }
    
    @Test(expected = InvalidArgumentException.class)
    public void testConstructor_invalidBackgroundColor_throwsInvalidArgumentException() throws InvalidArgumentException {
        new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 2);
    }
}