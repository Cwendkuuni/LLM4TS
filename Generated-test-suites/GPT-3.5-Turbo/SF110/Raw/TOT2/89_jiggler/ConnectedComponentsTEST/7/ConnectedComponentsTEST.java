import static org.junit.Assert.*;
import org.junit.Test;

public class ConnectedComponentsTest {
    
    @Test
    public void testConnectedComponentsConstructor() {
        try {
            ConnectedComponents cc = new ConnectedComponents(0, 0);
            assertEquals(0, cc.neighbors);
            assertEquals(0, cc.background);
        } catch (InvalidArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testApplyMethod_withFourConnected() {
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        BinaryImage image = new BinaryImage(4, 4);
        image.set(0, 0, 1);
        image.set(1, 1, 1);
        image.set(2, 2, 1);
        image.set(3, 3, 1);

        GrayImage result = cc.apply(image);
        assertEquals(1, result.get(0, 0).intValue());
        assertEquals(2, result.get(1, 1).intValue());
        assertEquals(3, result.get(2, 2).intValue());
        assertEquals(4, result.get(3, 3).intValue());
    }
    
    @Test
    public void testApplyMethod_withEightConnected() {
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);
        BinaryImage image = new BinaryImage(4, 4);
        image.set(0, 0, 1);
        image.set(0, 1, 1);
        image.set(1, 1, 1);
        image.set(1, 2, 1);
        image.set(2, 2, 1);
        image.set(2, 3, 1);
        image.set(3, 3, 1);

        GrayImage result = cc.apply(image);
        assertEquals(1, result.get(0, 0).intValue());
        assertEquals(1, result.get(0, 1).intValue());
        assertEquals(2, result.get(1, 1).intValue());
        assertEquals(2, result.get(1, 2).intValue());
        assertEquals(3, result.get(2, 2).intValue());
        assertEquals(3, result.get(2, 3).intValue());
        assertEquals(4, result.get(3, 3).intValue());
    }
}