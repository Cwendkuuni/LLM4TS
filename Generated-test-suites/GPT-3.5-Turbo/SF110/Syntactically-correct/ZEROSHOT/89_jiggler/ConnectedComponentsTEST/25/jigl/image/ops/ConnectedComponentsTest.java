import org.junit.Test;
import static org.junit.Assert.*;

public class ConnectedComponentsTest {

    @Test
    public void testConstructor() {
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        assertEquals(0, cc.neighbors);
        assertEquals(0, cc.background);
        
        cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 1);
        assertEquals(1, cc.neighbors);
        assertEquals(1, cc.background);
        
        try {
            cc = new ConnectedComponents(2, 0);
        } catch (InvalidArgumentException e) {
            // Expected exception
        }
        
        try {
            cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 2);
        } catch (InvalidArgumentException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testApply() {
        BinaryImage image = new BinaryImage(4, 4);
        image.set(0, 0, 0);
        image.set(0, 1, 1);
        image.set(0, 2, 0);
        image.set(0, 3, 1);
        image.set(1, 0, 0);
        image.set(1, 1, 0);
        image.set(1, 2, 1);
        image.set(1, 3, 0);
        image.set(2, 0, 1);
        image.set(2, 1, 1);
        image.set(2, 2, 0);
        image.set(2, 3, 1);
        image.set(3, 0, 1);
        image.set(3, 1, 0);
        image.set(3, 2, 1);
        image.set(3, 3, 0);
        
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        
        GrayImage result = cc.apply(image);
        
        assertEquals(1, result.get(0, 0));
        assertEquals(0, result.get(0, 1));
        assertEquals(2, result.get(0, 2));
        assertEquals(0, result.get(0, 3));

        assertEquals(1, result.get(1, 0));
        assertEquals(1, result.get(1, 1));
        assertEquals(0, result.get(1, 2));
        assertEquals(3, result.get(1, 3));

        assertEquals(4, result.get(2, 0));
        assertEquals(4, result.get(2, 1));
        assertEquals(0, result.get(2, 2));
        assertEquals(5, result.get(2, 3));

        assertEquals(6, result.get(3, 0));
        assertEquals(0, result.get(3, 1));
        assertEquals(7, result.get(3, 2));
        assertEquals(0, result.get(3, 3));
    }
}