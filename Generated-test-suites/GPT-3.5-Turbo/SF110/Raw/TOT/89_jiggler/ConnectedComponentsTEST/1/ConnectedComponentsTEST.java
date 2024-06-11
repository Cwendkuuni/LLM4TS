import org.junit.Test;
import junit.framework.TestCase;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class ConnectedComponentsTest extends TestCase {

    @Test
    public void testConstructorValidNeighbors() {
        // Test that the constructor initializes neighbors properly for valid input
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        assertEquals(ConnectedComponents.FOUR_CONNECTED, cc.getNeighbors());
    }
    
    @Test
    public void testConstructorValidBackgroundColor() {
        // Test that the constructor initializes background properly for valid input
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 1);
        assertEquals(1, cc.getBackground());
    }
    
    @Test(expected = InvalidArgumentException.class)
    public void testConstructorInvalidNeighbors() {
        // Test that the constructor throws exception for invalid neighbors input
        ConnectedComponents cc = new ConnectedComponents(2, 0);
    }
    
    @Test(expected = InvalidArgumentException.class)
    public void testConstructorInvalidBackgroundColor() {
        // Test that the constructor throws exception for invalid background input
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 2);
    }
    
    @Test
    public void testApplyFourConnected() {
        // Test that apply method applies four-connected algorithm correctly
        BinaryImage image = new BinaryImage(5, 5);
        image.set(0, 0, 0);
        image.set(0, 1, 1);
        image.set(1, 0, 1);
        image.set(1, 1, 0);
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        GrayImage result = cc.apply(image);
        assertEquals(1, result.get(0, 0));
        assertEquals(2, result.get(0, 1));
        assertEquals(2, result.get(1, 0));
        assertEquals(3, result.get(1, 1));
    }
    
    @Test
    public void testApplyEightConnected() {
        // Test that apply method applies eight-connected algorithm correctly
        BinaryImage image = new BinaryImage(5, 5);
        image.set(0, 0, 0);
        image.set(0, 1, 1);
        image.set(1, 0, 1);
        image.set(1, 1, 0);
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);
        GrayImage result = cc.apply(image);
        assertEquals(1, result.get(0, 0));
        assertEquals(1, result.get(0, 1));
        assertEquals(1, result.get(1, 0));
        assertEquals(2, result.get(1, 1));
    }
    
    @Test
    public void testApplyBackgroundValue() {
        // Test that apply method sets background value correctly
        BinaryImage image = new BinaryImage(3, 3);
        image.set(0, 0, 1);
        image.set(0, 1, 0);
        image.set(0, 2, 1);
        image.set(1, 0, 0);
        image.set(1, 1, 0);
        image.set(1, 2, 0);
        image.set(2, 0, 1);
        image.set(2, 1, 1);
        image.set(2, 2, 1);
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        GrayImage result = cc.apply(image);
        assertEquals(0, result.get(0, 0));
        assertEquals(0, result.get(0, 1));
        assertEquals(0, result.get(0, 2));
        assertEquals(0, result.get(1, 0));
        assertEquals(0, result.get(1, 1));
        assertEquals(0, result.get(1, 2));
        assertEquals(0, result.get(2, 0));
        assertEquals(0, result.get(2, 1));
        assertEquals(0, result.get(2, 2));
    }
    
    @Test
    public void testApplyEdgeCase() {
        // Test that apply method handles edge case correctly
        BinaryImage image = new BinaryImage(1, 1);
        image.set(0, 0, 0);
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        GrayImage result = cc.apply(image);
        assertEquals(1, result.get(0, 0));
    }
}