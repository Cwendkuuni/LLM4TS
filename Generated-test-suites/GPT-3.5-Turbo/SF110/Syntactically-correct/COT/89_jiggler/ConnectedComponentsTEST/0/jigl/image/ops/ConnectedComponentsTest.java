import org.junit.*;
import static org.junit.Assert.*;

public class ConnectedComponentsTest {

    @Test
    public void testConstructor() {
        try {
            ConnectedComponents cc = new ConnectedComponents(0, 0);
            assertEquals(0, cc.neighbors);
            assertEquals(0, cc.background);
        } catch (InvalidArgumentException e) {
            fail("Exception thrown");
        }
    }

    @Test(expected = InvalidArgumentException.class)
    public void testConstructorInvalidArgs() throws InvalidArgumentException {
        ConnectedComponents cc = new ConnectedComponents(2, 0);
    }

    @Test
    public void testApply() {
        BinaryImage image = new BinaryImage(3, 3);
        image.set(0, 0, 1);
        image.set(1, 1, 1);
        image.set(2, 2, 1);
        
        ConnectedComponents cc = new ConnectedComponents(0, 0);
        GrayImage gimage = cc.apply(image);
        
        assertEquals(Integer.valueOf(0), gimage.get(0, 0));
        assertEquals(Integer.valueOf(1), gimage.get(1, 1));
        assertEquals(Integer.valueOf(2), gimage.get(2, 2));
    }

    @Test
    public void testApplyWithDifferentBackground() {
        BinaryImage image = new BinaryImage(3, 3);
        image.set(0, 0, 0);
        image.set(1, 1, 0);
        image.set(2, 2, 0);
        
        ConnectedComponents cc = new ConnectedComponents(0, 1);
        GrayImage gimage = cc.apply(image);
        
        assertEquals(Integer.valueOf(0), gimage.get(0, 0));
        assertEquals(Integer.valueOf(1), gimage.get(1, 1));
        assertEquals(Integer.valueOf(2), gimage.get(2, 2));
    }

    @Test
    public void testApplyWithFourConnected() {
        BinaryImage image = new BinaryImage(3, 3);
        image.set(0, 0, 1);
        image.set(0, 1, 1);
        image.set(1, 0, 1);
        
        ConnectedComponents cc = new ConnectedComponents(0, 0);
        GrayImage gimage = cc.apply(image);
        
        assertEquals(Integer.valueOf(0), gimage.get(0, 0));
        assertEquals(Integer.valueOf(0), gimage.get(0, 1));
        assertEquals(Integer.valueOf(0), gimage.get(1, 0));
    }

    @Test
    public void testApplyWithEightConnected() {
        BinaryImage image = new BinaryImage(3, 3);
        image.set(0, 0, 1);
        image.set(0, 1, 1);
        image.set(1, 0, 1);
        
        ConnectedComponents cc = new ConnectedComponents(1, 0);
        GrayImage gimage = cc.apply(image);
        
        assertEquals(Integer.valueOf(0), gimage.get(0, 0));
        assertEquals(Integer.valueOf(0), gimage.get(0, 1));
        assertEquals(Integer.valueOf(0), gimage.get(1, 0));
    }

    @Test
    public void testApplyWithIsolatedForeground() {
        BinaryImage image = new BinaryImage(3, 3);
        image.set(0, 0, 1);
        image.set(2, 2, 1);
        
        ConnectedComponents cc = new ConnectedComponents(1, 0);
        GrayImage gimage = cc.apply(image);
        
        assertEquals(Integer.valueOf(0), gimage.get(0, 0));
        assertEquals(Integer.valueOf(1), gimage.get(2, 2));
    }

    @Test
    public void testApplyWithMultipleConnectedComponents() {
        BinaryImage image = new BinaryImage(5, 5);
        image.set(0, 0, 1);
        image.set(1, 0, 1);
        image.set(0, 1, 1);
        image.set(3, 3, 1);
        image.set(4, 3, 1);
        image.set(3, 4, 1);
        
        ConnectedComponents cc = new ConnectedComponents(0, 0);
        GrayImage gimage = cc.apply(image);
        
        assertEquals(Integer.valueOf(0), gimage.get(0, 0));
        assertEquals(Integer.valueOf(0), gimage.get(0, 1));
        assertEquals(Integer.valueOf(0), gimage.get(1, 0));
        assertEquals(Integer.valueOf(1), gimage.get(3, 3));
        assertEquals(Integer.valueOf(1), gimage.get(4, 3));
        assertEquals(Integer.valueOf(1), gimage.get(3, 4));
    }

}