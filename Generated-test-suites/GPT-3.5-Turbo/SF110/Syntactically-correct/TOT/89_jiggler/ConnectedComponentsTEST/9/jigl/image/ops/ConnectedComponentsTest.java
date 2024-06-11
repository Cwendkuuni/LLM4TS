import org.junit.Test;
import static org.junit.Assert.*;

public class ConnectedComponentsTest {

    @Test
    public void testConstructor_validInputs() {
        try {
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
            assertEquals(ConnectedComponents.FOUR_CONNECTED, cc.getNeighbors());
            assertEquals(0, cc.getBackground());
        } catch (InvalidArgumentException e) {
            fail("InvalidArgumentException should not be thrown");
        }
    }

    @Test(expected = InvalidArgumentException.class)
    public void testConstructor_invalidInputs() throws InvalidArgumentException {
        ConnectedComponents cc = new ConnectedComponents(2, 0);
    }
    
    @Test
    public void testApply_backgroundPixel() {
        BinaryImage image = new BinaryImage(5, 5);
        for (int x = 0; x < image.X(); x++) {
            for (int y = 0; y < image.Y(); y++) {
                image.set(x, y, 0);
            }
        }
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        GrayImage result = cc.apply(image);
        for (int x = 0; x < result.X(); x++) {
            for (int y = 0; y < result.Y(); y++) {
                assertEquals(0, result.get(x, y));
            }
        }
    }
    
    @Test
    public void testApply_firstPixel() {
        BinaryImage image = new BinaryImage(5, 5);
        image.set(0, 0, 1);
        for (int x = 1; x < image.X(); x++) {
            for (int y = 1; y < image.Y(); y++) {
                image.set(x, y, 0);
            }
        }
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        GrayImage result = cc.apply(image);
        assertEquals(1, result.get(0, 0));
        for (int x = 1; x < result.X(); x++) {
            for (int y = 1; y < result.Y(); y++) {
                assertEquals(0, result.get(x, y));
            }
        }
    }
    
    @Test
    public void testApply_fourConnectedNeighbors() {
        BinaryImage image = new BinaryImage(3, 3);
        image.set(0, 0, 1);
        image.set(1, 0, 0);
        image.set(2, 0, 1);
        image.set(0, 1, 1);
        image.set(1, 1, 0);
        image.set(2, 1, 1);
        image.set(0, 2, 1);
        image.set(1, 2, 0);
        image.set(2, 2, 1);
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        GrayImage result = cc.apply(image);
        assertEquals(1, result.get(0, 0));
        assertEquals(0, result.get(1, 0));
        assertEquals(2, result.get(2, 0));
        assertEquals(1, result.get(0, 1));
        assertEquals(0, result.get(1, 1));
        assertEquals(3, result.get(2, 1));
        assertEquals(4, result.get(0, 2));
        assertEquals(0, result.get(1, 2));
        assertEquals(5, result.get(2, 2));
    }
    
    @Test
    public void testApply_eightConnectedNeighbors() {
        BinaryImage image = new BinaryImage(3, 3);
        image.set(0, 0, 1);
        image.set(1, 0, 0);
        image.set(2, 0, 1);
        image.set(0, 1, 1);
        image.set(1, 1, 0);
        image.set(2, 1, 1);
        image.set(0, 2, 1);
        image.set(1, 2, 0);
        image.set(2, 2, 1);
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);
        GrayImage result = cc.apply(image);
        assertEquals(1, result.get(0, 0));
        assertEquals(0, result.get(1, 0));
        assertEquals(2, result.get(2, 0));
        assertEquals(1, result.get(0, 1));
        assertEquals(0, result.get(1, 1));
        assertEquals(2, result.get(2, 1));
        assertEquals(3, result.get(0, 2));
        assertEquals(0, result.get(1, 2));
        assertEquals(4, result.get(2, 2));
    }
}