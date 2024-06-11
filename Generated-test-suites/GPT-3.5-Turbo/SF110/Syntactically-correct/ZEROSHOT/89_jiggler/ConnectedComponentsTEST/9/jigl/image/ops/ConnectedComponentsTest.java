import static org.junit.Assert.*;
import org.junit.Test;
import jigl.image.*;
import jigl.image.exceptions.*;

public class ConnectedComponentsTest {

    @Test
    public void testConstructor() {
        try {
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, BinaryImage.BLACK);
            // assert statements
            assertEquals(ConnectedComponents.FOUR_CONNECTED, cc.getNeighbors());
            assertEquals(BinaryImage.BLACK, cc.getBackground());
        } catch (InvalidArgumentException e) {
            fail("Exception thrown when constructing ConnectedComponents");
        }
    }
    
    @Test
    public void testConstructorInvalidArguments() {
        try {
            ConnectedComponents cc = new ConnectedComponents(2, 0);
            fail("InvalidArgumentException not thrown when constructing ConnectedComponents with invalid arguments");
        } catch (InvalidArgumentException e) {
            // assert statement
            assertEquals("InvalidArgumentException thrown with incorrect message", "Invalid argument", e.getMessage());
        }
    }
    
    @Test
    public void testApply() {
        BinaryImage image = new BinaryImage(4, 4);
        image.set(0, 0, BinaryImage.WHITE);
        image.set(1, 0, BinaryImage.BLACK);
        image.set(2, 0, BinaryImage.BLACK);
        image.set(3, 0, BinaryImage.WHITE);
        image.set(0, 1, BinaryImage.WHITE);
        image.set(1, 1, BinaryImage.BLACK);
        image.set(2, 1, BinaryImage.BLACK);
        image.set(3, 1, BinaryImage.WHITE);
        image.set(0, 2, BinaryImage.WHITE);
        image.set(1, 2, BinaryImage.BLACK);
        image.set(2, 2, BinaryImage.BLACK);
        image.set(3, 2, BinaryImage.WHITE);
        image.set(0, 3, BinaryImage.WHITE);
        image.set(1, 3, BinaryImage.BLACK);
        image.set(2, 3, BinaryImage.BLACK);
        image.set(3, 3, BinaryImage.WHITE);
        
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, BinaryImage.WHITE);
        GrayImage result = cc.apply(image);
        
        assertEquals(1, result.get(0, 0));
        assertEquals(2, result.get(1, 0));
        assertEquals(2, result.get(2, 0));
        assertEquals(3, result.get(3, 0));
        assertEquals(1, result.get(0, 1));
        assertEquals(0, result.get(1, 1));
        assertEquals(0, result.get(2, 1));
        assertEquals(4, result.get(3, 1));
        assertEquals(1, result.get(0, 2));
        assertEquals(0, result.get(1, 2));
        assertEquals(0, result.get(2, 2));
        assertEquals(5, result.get(3, 2));
        assertEquals(1, result.get(0, 3));
        assertEquals(0, result.get(1, 3));
        assertEquals(0, result.get(2, 3));
        assertEquals(6, result.get(3, 3));
    }
}