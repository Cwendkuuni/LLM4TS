package jigl.image.ops;

import jigl.image.exceptions.InvalidArgumentException;
import jigl.image.BinaryImage;
import jigl.image.GrayImage;

import org.junit.Test;
import static org.junit.Assert.*;

public class ConnectedComponentsTest {

    @Test
    public void testApply_fourConnected() throws InvalidArgumentException {
        ConnectedComponents op = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        BinaryImage image = new BinaryImage(10, 10);
        image.set(0, 0, 1);
        image.set(0, 1, 1);
        image.set(1, 0, 1);
        
        GrayImage result = op.apply(image);
        
        assertEquals(1, result.get(0, 0));
        assertEquals(1, result.get(0, 1));
        assertEquals(1, result.get(1, 0));
        assertEquals(1, result.get(1, 1));
    }
    
    @Test
    public void testApply_eightConnected() throws InvalidArgumentException {
        ConnectedComponents op = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);
        BinaryImage image = new BinaryImage(10, 10);
        image.set(0, 0, 1);
        image.set(0, 1, 1);
        image.set(1, 0, 1);
        
        GrayImage result = op.apply(image);
        
        assertEquals(1, result.get(0, 0));
        assertEquals(1, result.get(0, 1));
        assertEquals(1, result.get(1, 0));
        assertEquals(1, result.get(1, 1));
    }
}