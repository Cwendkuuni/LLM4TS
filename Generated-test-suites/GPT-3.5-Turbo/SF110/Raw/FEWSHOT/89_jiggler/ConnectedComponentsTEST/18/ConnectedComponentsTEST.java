package jigl.image.ops;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import jigl.image.BinaryImage;
import jigl.image.GrayImage;
import jigl.image.Image;
import jigl.image.exceptions.InvalidArgumentException;
import jigl.image.types.ImageAccessException;
import jigl.image.Image.ColorModel;

import org.junit.Test;

public class ConnectedComponentsTest {
    
    @Test
    public void testConstructorWithValidParameters() {
        try {
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
            assertEquals(ConnectedComponents.FOUR_CONNECTED, cc.getNeighbors());
            assertEquals(0, cc.getBackground());
        } catch (InvalidArgumentException e) {
            fail("Unexpected InvalidArgumentException");
        }
        
        try {
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 1);
            assertEquals(ConnectedComponents.EIGHT_CONNECTED, cc.getNeighbors());
            assertEquals(1, cc.getBackground());
        } catch (InvalidArgumentException e) {
            fail("Unexpected InvalidArgumentException");
        }
    }
    
    @Test(expected=InvalidArgumentException.class)
    public void testConstructorWithInvalidParameters() throws InvalidArgumentException {
        ConnectedComponents cc = new ConnectedComponents(2, 0);
    }
    
    @Test
    public void testApplyWithValidBinaryImage() {
        try {
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
            
            BinaryImage binaryImage = new BinaryImage(10, 10);
            
            for (int x = 0; x < binaryImage.X(); x++) {
                for (int y = 0; y < binaryImage.Y(); y++) {
                    binaryImage.set(x, y, (byte) 1); // set all pixels to foreground
                }
            }
            
            binaryImage.set(5, 5, (byte) 0); // set one pixel to background
            
            GrayImage result = cc.apply(binaryImage);
            
            for (int x = 0; x < result.X(); x++) {
                for (int y = 0; y < result.Y(); y++) {
                    assertTrue(result.get(x, y) >= 0); // expect non-negative label values
                }
            }
        } catch (ImageAccessException e) {
            fail("Unexpected ImageAccessException");
        }
    }
    
    @Test(expected=ImageAccessException.class)
    public void testApplyWithInvalidBinaryImage() throws ImageAccessException {
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        
        Image image = new Image(10, 10, ColorModel.GRAY);
        
        cc.apply(image);
    }
}