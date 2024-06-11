import org.junit.Test;
import static org.junit.Assert.*;
import jigl.image.ops.ConnectedComponents;
import jigl.image.binary.*;

public class ConnectedComponentsTest {

    @Test
    public void testConstructorValidInputs() {
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        assertEquals(ConnectedComponents.FOUR_CONNECTED, cc.getNeighbors());
        assertEquals(0, cc.getBackground());
    }
    
    @Test(expected = InvalidArgumentException.class)
    public void testConstructorInvalidInputs() {
        ConnectedComponents cc = new ConnectedComponents(2, 3);
    }
    
    @Test
    public void testApplyFourConnected() {
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        BinaryImage image = new BinaryImage(3, 3);
        
        image.set(0, 0, 1); // foreground
        image.set(1, 1, 1);
        image.set(2, 2, 1);
        
        GrayImage result = cc.apply(image);
        assertEquals(3, result.get(0, 0));
        assertEquals(3, result.get(1, 1));
        assertEquals(4, result.get(2, 2));
    }
    
    @Test
    public void testApplyEightConnected() {
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);
        BinaryImage image = new BinaryImage(3, 3);
        
        image.set(0, 0, 1); // foreground
        image.set(2, 2, 1);
        
        GrayImage result = cc.apply(image);
        assertEquals(3, result.get(0, 0));
        assertEquals(3, result.get(2, 2));
    }
    
    @Test
    public void testApplyEmptyImage() {
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        BinaryImage image = new BinaryImage(3, 3);
        
        GrayImage result = cc.apply(image);
        for (int x = 0; x < image.X(); x++) {
            for (int y = 0; y < image.Y(); y++) {
                assertEquals(0, result.get(x, y));
            }
        }
    }
    
    @Test
    public void testApplySinglePixel() {
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        BinaryImage image = new BinaryImage(1, 1);
        image.set(0, 0, 1); // foreground
        
        GrayImage result = cc.apply(image);
        assertEquals(1, result.get(0, 0));
    }

}