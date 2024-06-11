import org.junit.Test;
import static org.junit.Assert.*;
import jigl.image.types.*;
import jigl.image.exceptions.*;

public class ConnectedComponentsTest {

    @Test
    public void testConstructorValidInputs() {
        try {
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, BinaryImage.WHITE);
            assertNotNull(cc);
        } catch (InvalidArgumentException e) {
            fail("Should not throw exception");
        }
    }
    
    @Test(expected = InvalidArgumentException.class)
    public void testConstructorInvalidInputs() throws InvalidArgumentException {
        ConnectedComponents cc = new ConnectedComponents(2, 5);
    }
    
    @Test
    public void testApplyBackgroundPixels() {
        BinaryImage image = new BinaryImage(5, 5);
        for(int x = 0; x < image.X(); x++) {
            for(int y = 0; y < image.Y(); y++) {
                image.set(x, y, BinaryImage.WHITE);
            }
        }
        
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, BinaryImage.WHITE);
        GrayImage result = cc.apply(image);
        
        for(int x = 0; x < result.X(); x++) {
            for(int y = 0; y < result.Y(); y++) {
                assertEquals(0, result.get(x, y));
            }
        }
    }
    
    @Test
    public void testApplyFourConnected() {
        BinaryImage image = new BinaryImage(5, 5);
        for(int x = 0; x < image.X(); x++) {
            for(int y = 0; y < image.Y(); y++) {
                image.set(x, y, BinaryImage.BLACK);
            }
        }
        
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, BinaryImage.BLACK);
        GrayImage result = cc.apply(image);
        
        for(int x = 0; x < result.X(); x++) {
            for(int y = 0; y < result.Y(); y++) {
                assertEquals(1, result.get(x, y));
            }
        }
    }
    
    @Test
    public void testApplyEightConnected() {
        BinaryImage image = new BinaryImage(5, 5);
        for(int x = 0; x < image.X(); x++) {
            for(int y = 0; y < image.Y(); y++) {
                image.set(x, y, BinaryImage.BLACK);
            }
        }
        
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, BinaryImage.BLACK);
        GrayImage result = cc.apply(image);
        
        for(int x = 0; x < result.X(); x++) {
            for(int y = 0; y < result.Y(); y++) {
                assertEquals(1, result.get(x, y));
            }
        }
    }
}