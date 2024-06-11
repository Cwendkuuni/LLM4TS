import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ConnectedComponentsTest {

    private ConnectedComponents cc;
    
    @Before
    public void setUp() {
        cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
    }
    
    @Test
    public void testConstructor() {
        assertEquals(ConnectedComponents.FOUR_CONNECTED, cc.neighbors);
        assertEquals(0, cc.background);
    }
    
    @Test
    public void testApply() {
        BinaryImage image = new BinaryImage(10, 10);
        GrayImage result = cc.apply(image);
        
        // Check if the result has the same dimensions as the input image
        assertEquals(image.X(), result.X());
        assertEquals(image.Y(), result.Y());
    }
    
    @Test
    public void testConstructorWithInvalidArguments() {
        try {
            ConnectedComponents invalidCC = new ConnectedComponents(2, 0);
            fail("InvalidArgumentException should have been thrown");
        } catch (InvalidArgumentException e) {
            // Exception is expected
        }
    }
    
    @Test
    public void testApplyWithDifferentNumberOfNeighbors() {
        ConnectedComponents ccEightConnected = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);
        BinaryImage image = new BinaryImage(10, 10);
        GrayImage result = ccEightConnected.apply(image);
        
        // Check if the result has the same dimensions as the input image
        assertEquals(image.X(), result.X());
        assertEquals(image.Y(), result.Y());
    }
    
    @Test
    public void testApplyWithDifferentBackgroundColor() {
        ConnectedComponents ccWhiteBackground = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 1);
        BinaryImage image = new BinaryImage(10, 10);
        GrayImage result = ccWhiteBackground.apply(image);
        
        // Check if the result has the same dimensions as the input image
        assertEquals(image.X(), result.X());
        assertEquals(image.Y(), result.Y());
    }
    
    @Test
    public void testApplyWithSmallerImage() {
        BinaryImage image = new BinaryImage(5, 5);
        GrayImage result = cc.apply(image);
        
        // Check if the result has the same dimensions as the input image
        assertEquals(image.X(), result.X());
        assertEquals(image.Y(), result.Y());
    }
    
    @Test
    public void testApplyWithLargerImage() {
        BinaryImage image = new BinaryImage(20, 20);
        GrayImage result = cc.apply(image);
        
        // Check if the result has the same dimensions as the input image
        assertEquals(image.X(), result.X());
        assertEquals(image.Y(), result.Y());
    }
    
    @Test
    public void testApplyWithAllBackgroundPixels() {
        BinaryImage image = new BinaryImage(10, 10);
        image.setAll(0);
        GrayImage result = cc.apply(image);
        
        // Check if the result has the same dimensions as the input image
        assertEquals(image.X(), result.X());
        assertEquals(image.Y(), result.Y());
    }
    
    @Test
    public void testApplyWithAllNonBackgroundPixels() {
        BinaryImage image = new BinaryImage(10, 10);
        image.setAll(1);
        GrayImage result = cc.apply(image);
        
        // Check if the result has the same dimensions as the input image
        assertEquals(image.X(), result.X());
        assertEquals(image.Y(), result.Y());
    }
}