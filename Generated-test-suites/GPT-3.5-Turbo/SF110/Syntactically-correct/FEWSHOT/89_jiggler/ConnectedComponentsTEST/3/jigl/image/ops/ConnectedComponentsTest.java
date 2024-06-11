package jigl.image.ops;

import static org.junit.Assert.*;
import org.junit.Test;
import jigl.image.SimpleOperator;
import jigl.image.BinaryImage;
import jigl.image.GrayImage;

public class ConnectedComponentsTest {
    @Test
    public void testConstructor() {
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        assertEquals(ConnectedComponents.FOUR_CONNECTED, cc.neighbors);
        assertEquals(0, cc.background);
        
        cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 1);
        assertEquals(ConnectedComponents.EIGHT_CONNECTED, cc.neighbors);
        assertEquals(1, cc.background);
    }
    
    @Test(expected = InvalidArgumentException.class)
    public void testInvalidConstructorParams() {
        ConnectedComponents cc = new ConnectedComponents(2, 0);
    }
    
    @Test
    public void testApplyFourConnected() {
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        
        // create a 4x4 binary image with two connected components separated by background
        BinaryImage image = new BinaryImage(4, 4);
        image.set(0, 0, 1); // connected component 1
        image.set(1, 0, 1); // connected component 1
        image.set(2, 0, 0); // background
        image.set(3, 0, 0); // background
        
        image.set(0, 1, 1); // connected component 1
        image.set(1, 1, 1); // connected component 1
        image.set(2, 1, 0); // background
        image.set(3, 1, 0); // background
        
        image.set(0, 2, 0); // background
        image.set(1, 2, 0); // background
        image.set(2, 2, 1); // connected component 2
        image.set(3, 2, 1); // connected component 2
        
        image.set(0, 3, 0); // background
        image.set(1, 3, 0); // background
        image.set(2, 3, 1); // connected component 2
        image.set(3, 3, 1); // connected component 2
        
        GrayImage result = cc.apply(image);
        
        assertEquals(1, result.get(0,0));
        assertEquals(1, result.get(1,0));
        assertEquals(0, result.get(2,0));
        assertEquals(0, result.get(3,0));
        
        assertEquals(1, result.get(0,1));
        assertEquals(1, result.get(1,1));
        assertEquals(0, result.get(2,1));
        assertEquals(0, result.get(3,1));
        
        assertEquals(0, result.get(0,2));
        assertEquals(0, result.get(1,2));
        assertEquals(2, result.get(2,2));
        assertEquals(2, result.get(3,2));
        
        assertEquals(0, result.get(0,3));
        assertEquals(0, result.get(1,3));
        assertEquals(2, result.get(2,3));
        assertEquals(2, result.get(3,3));
    }
    
    @Test
    public void testApplyEightConnected() {
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 1);
        
        // create a 4x4 binary image with two connected components separated by background
        BinaryImage image = new BinaryImage(4, 4);
        image.set(0, 0, 1); // connected component 1
        image.set(1, 0, 1); // connected component 1
        image.set(2, 0, 0); // background
        image.set(3, 0, 0); // background
        
        image.set(0, 1, 1); // connected component 1
        image.set(1, 1, 1); // connected component 1
        image.set(2, 1, 0); // background
        image.set(3, 1, 0); // background
        
        image.set(0, 2, 0); // background
        image.set(1, 2, 0); // background
        image.set(2, 2, 1); // connected component 2
        image.set(3, 2, 1); // connected component 2
        
        image.set(0, 3, 0); // background
        image.set(1, 3, 0); // background
        image.set(2, 3, 1); // connected component 2
        image.set(3, 3, 1); // connected component 2
        
        GrayImage result = cc.apply(image);
        
        assertEquals(1, result.get(0,0));
        assertEquals(1, result.get(1,0));
        assertEquals(0, result.get(2,0));
        assertEquals(0, result.get(3,0));
        
        assertEquals(1, result.get(0,1));
        assertEquals(1, result.get(1,1));
        assertEquals(0, result.get(2,1));
        assertEquals(0, result.get(3,1));
        
        assertEquals(0, result.get(0,2));
        assertEquals(0, result.get(1,2));
        assertEquals(1, result.get(2,2));
        assertEquals(1, result.get(3,2));
        
        assertEquals(0, result.get(0,3));
        assertEquals(0, result.get(1,3));
        assertEquals(1, result.get(2,3));
        assertEquals(1, result.get(3,3));
    }
}