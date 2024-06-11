import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ConnectedComponentsTest {
    
    private ConnectedComponents connectedComponents;

    @Before
    public void setUp() {
        connectedComponents = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
    }

    @Test
    public void testConstructor() {
        assertEquals(ConnectedComponents.FOUR_CONNECTED, connectedComponents.getNeighbors());
        assertEquals(0, connectedComponents.getBackground());
    }

    @Test(expected = InvalidArgumentException.class)
    public void testInvalidConstructor() {
        ConnectedComponents invalidConnectedComponents = new ConnectedComponents(3, 1);
    }

    @Test
    public void testApplyBackground() {
        BinaryImage image = new BinaryImage(3, 3);
        image.set(0, 0, 0);
        image.set(0, 1, 0);
        image.set(0, 2, 0);
        image.set(1, 0, 0);
        image.set(1, 1, 1);
        image.set(1, 2, 1);
        image.set(2, 0, 0);
        image.set(2, 1, 0);
        image.set(2, 2, 0);
        
        GrayImage result = connectedComponents.apply(image);
        
        assertEquals(0, result.get(0, 0));
        assertEquals(0, result.get(0, 1));
        assertEquals(0, result.get(0, 2));
        assertEquals(0, result.get(1, 0));
        assertEquals(1, result.get(1, 1));
        assertEquals(1, result.get(1, 2));
        assertEquals(0, result.get(2, 0));
        assertEquals(0, result.get(2, 1));
        assertEquals(0, result.get(2, 2));
    }

    @Test
    public void testApplyFourConnected() {
        BinaryImage image = new BinaryImage(3, 3);
        image.set(0, 0, 1);
        image.set(0, 1, 0);
        image.set(0, 2, 0);
        image.set(1, 0, 0);
        image.set(1, 1, 1);
        image.set(1, 2, 1);
        image.set(2, 0, 0);
        image.set(2, 1, 0);
        image.set(2, 2, 0);
        
        GrayImage result = connectedComponents.apply(image);
        
        assertEquals(1, result.get(0, 0));
        assertEquals(0, result.get(0, 1));
        assertEquals(0, result.get(0, 2));
        assertEquals(0, result.get(1, 0));
        assertEquals(2, result.get(1, 1));
        assertEquals(2, result.get(1, 2));
        assertEquals(0, result.get(2, 0));
        assertEquals(0, result.get(2, 1));
        assertEquals(0, result.get(2, 2));
    }

    @Test
    public void testApplyEightConnected() {
        connectedComponents = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);
        
        BinaryImage image = new BinaryImage(3, 3);
        image.set(0, 0, 1);
        image.set(0, 1, 0);
        image.set(0, 2, 0);
        image.set(1, 0, 0);
        image.set(1, 1, 1);
        image.set(1, 2, 1);
        image.set(2, 0, 0);
        image.set(2, 1, 0);
        image.set(2, 2, 0);
        
        GrayImage result = connectedComponents.apply(image);
        
        assertEquals(1, result.get(0, 0));
        assertEquals(0, result.get(0, 1));
        assertEquals(0, result.get(0, 2));
        assertEquals(0, result.get(1, 0));
        assertEquals(2, result.get(1, 1));
        assertEquals(2, result.get(1, 2));
        assertEquals(0, result.get(2, 0));
        assertEquals(0, result.get(2, 1));
        assertEquals(0, result.get(2, 2));
    }

}