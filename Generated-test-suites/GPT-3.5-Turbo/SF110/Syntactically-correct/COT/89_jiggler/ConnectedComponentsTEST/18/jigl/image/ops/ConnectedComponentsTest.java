
import jigl.image.*;
import jigl.image.exceptions.*;
import org.junit.*;
import static org.junit.Assert.*;

public class ConnectedComponentsTest {
    
    @Test
    public void testConstructor() {
        try {
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 1);
            
            assertEquals(ConnectedComponents.FOUR_CONNECTED, cc.getNeighbors());
            assertEquals(1, cc.getBackground());
            
            cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);
            
            assertEquals(ConnectedComponents.EIGHT_CONNECTED, cc.getNeighbors());
            assertEquals(0, cc.getBackground());
        } catch (InvalidArgumentException e) {
            fail("Exception occurred");
        }
    }
    
    @Test(expected = InvalidArgumentException.class)
    public void testInvalidConstructor() throws InvalidArgumentException {
        ConnectedComponents cc = new ConnectedComponents(2, 1);
    }
    
    @Test
    public void testApply() {
        boolean[][] data = { { true, false, true }, { false, true, false }, { true, false, true } };
        BinaryImage image = new BinaryImage(data);
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        
        GrayImage result = cc.apply(image);
        
        for (int x = 0; x < result.X(); x++) {
            for (int y = 0; y < result.Y(); y++) {
                if (data[x][y]) {
                    assertEquals(x + 1, result.get(x, y).intValue());
                } else {
                    assertEquals(0, result.get(x, y).intValue());
                }
            }
        }
    }
    
    @Test
    public void testApplyWithExceptions() {
        try {
            boolean[][] data = { { true, false, true }, { false, true, false }, { true, false, true } };
            BinaryImage image = new BinaryImage(data);
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 2);
            
            GrayImage result = cc.apply(image);
            
            fail("Exception not thrown");
        } catch (InvalidArgumentException e) {
            //expected exception
        }
    }
    
}
