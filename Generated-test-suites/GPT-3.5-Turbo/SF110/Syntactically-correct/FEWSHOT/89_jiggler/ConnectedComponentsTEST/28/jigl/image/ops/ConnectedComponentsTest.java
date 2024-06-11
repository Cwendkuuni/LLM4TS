package jigl.image.ops;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ConnectedComponentsTest {
 
    @Test
    public void testConstructor() throws Exception {
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 1);
    }
    
    @Test(expected = InvalidArgumentException.class)
    public void testInvalidConstructor() throws Exception {
        ConnectedComponents cc = new ConnectedComponents(2, 1);
    }

    @Test
    public void testApply() throws Exception {
        BinaryImage binaryImage = new BinaryImage(3, 3);
        binaryImage.set(0,0,1);
        binaryImage.set(0,1,0);
        binaryImage.set(0,2,1);
        binaryImage.set(1,0,0);
        binaryImage.set(1,1,1);
        binaryImage.set(1,2,0);
        binaryImage.set(2,0,0);
        binaryImage.set(2,1,1);
        binaryImage.set(2,2,0);
        
        GrayImage expectedImage = new GrayImage(3, 3);
        expectedImage.set(0,0,1);
        expectedImage.set(0,1,0);
        expectedImage.set(0,2,2);
        expectedImage.set(1,0,0);
        expectedImage.set(1,1,1);
        expectedImage.set(1,2,0);
        expectedImage.set(2,0,0);
        expectedImage.set(2,1,3);
        expectedImage.set(2,2,0);
        
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        GrayImage resultImage = cc.apply(binaryImage);
        
        assertEquals(expectedImage, resultImage);
    }
}