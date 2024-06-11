import static org.junit.Assert.*;
import org.junit.Test;
import jigl.image.types.*;
import jigl.image.exceptions.*;

public class ConnectedComponentsTest {
    
    @Test
    public void testConstructor() {
        try {
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
            assertEquals(0, cc.neighbors);
            assertEquals(0, cc.background);
            
            cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 1);
            assertEquals(1, cc.neighbors);
            assertEquals(1, cc.background);
        } catch (InvalidArgumentException e) {
            fail("Exception thrown");
        }
    }
    
    @Test(expected = InvalidArgumentException.class)
    public void testConstructorInvalidNeighbors() throws InvalidArgumentException {
        ConnectedComponents cc = new ConnectedComponents(2, 0);
    }
    
    @Test(expected = InvalidArgumentException.class)
    public void testConstructorInvalidBackground() throws InvalidArgumentException {
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 2);
    }
    
    @Test
    public void testApplyFourConnected() {
        BinaryImage image = new BinaryImage(4, 4);
        try {
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
            GrayImage result = cc.apply(image);
            
            assertEquals(4, result.X());
            assertEquals(4, result.Y());
            
            for (int x = 0; x < result.X(); x++) {
                for (int y = 0; y < result.Y(); y++) {
                    assertEquals(0, result.get(x, y));
                }
            }
        } catch (InvalidArgumentException e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testApplyEightConnected() {
        BinaryImage image = new BinaryImage(4, 4);
        try {
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 1);
            GrayImage result = cc.apply(image);
            
            assertEquals(4, result.X());
            assertEquals(4, result.Y());
            
            for (int x = 0; x < result.X(); x++) {
                for (int y = 0; y < result.Y(); y++) {
                    assertEquals(0, result.get(x, y));
                }
            }
        } catch (InvalidArgumentException e) {
            fail("Exception thrown");
        }
    }
}