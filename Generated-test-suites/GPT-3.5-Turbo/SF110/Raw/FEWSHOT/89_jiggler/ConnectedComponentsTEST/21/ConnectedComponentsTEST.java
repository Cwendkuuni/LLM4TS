package jigl.image.ops;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import jigl.image.exceptions.InvalidArgumentException;
import jigl.image.types.BinaryImage;
import jigl.image.types.GrayImage;
import org.junit.Test;

public class ConnectedComponentsTest {
    @Test
    public void testConstructor() throws InvalidArgumentException {
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        assertEquals(ConnectedComponents.FOUR_CONNECTED, cc.getNeighbors());
        assertEquals(0, cc.getBackground());
        
        cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 1);
        assertEquals(ConnectedComponents.EIGHT_CONNECTED, cc.getNeighbors());
        assertEquals(1, cc.getBackground());
    }
    
    @Test(expected = InvalidArgumentException.class)
    public void testConstructor_InvalidNeighbors() throws InvalidArgumentException {
        ConnectedComponents cc = new ConnectedComponents(2, 0);
    }
    
    @Test(expected = InvalidArgumentException.class)
    public void testConstructor_InvalidBackground() throws InvalidArgumentException {
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 2);
    }
    
    @Test
    public void testApply() {
        BinaryImage binaryImage = new BinaryImage(10, 10);
        binaryImage.set(0, 0, 1);
        binaryImage.set(1, 0, 1);
        binaryImage.set(0, 1, 1);
        binaryImage.set(1, 1, 1);
        
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        GrayImage result = cc.apply(binaryImage);
        
        assertEquals(1, result.get(0, 0));
        assertEquals(1, result.get(1, 0));
        assertEquals(1, result.get(0, 1));
        assertEquals(1, result.get(1, 1));
        
        binaryImage.set(5, 5, 1);
        
        result = cc.apply(binaryImage);
        
        assertEquals(2, result.get(0, 0));
        assertEquals(2, result.get(1, 0));
        assertEquals(2, result.get(0, 1));
        assertEquals(2, result.get(1, 1));
        assertEquals(0, result.get(5, 5));
        
        cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 1);
        result = cc.apply(binaryImage);
        
        assertEquals(2, result.get(0, 0));
        assertEquals(2, result.get(1, 0));
        assertEquals(2, result.get(0, 1));
        assertEquals(2, result.get(1, 1));
        assertEquals(0, result.get(5, 5));
    }
}