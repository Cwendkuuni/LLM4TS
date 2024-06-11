package jigl.image.ops;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import jigl.image.*;
import jigl.image.exceptions.InvalidArgumentException;
import org.junit.Before;
import org.junit.Test;

public class ConnectedComponentsTest {
    
    private ConnectedComponents cc;
    private BinaryImage binaryImage;
    private GrayImage result;
    
    @Before
    public void setUp() throws Exception {
        cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        binaryImage = new BinaryImage(10, 10);
        result = new GrayImage(10, 10);
    }
    
    @Test(expected = InvalidArgumentException.class)
    public void testInvalidArguments() throws Exception {
        cc = new ConnectedComponents(2, 3);
    }
    
    @Test
    public void testApply() throws Exception {
        binaryImage.set(0, 0, 1);
        binaryImage.set(1, 0, 1);
        binaryImage.set(0, 1, 1);
        binaryImage.set(1, 1, 1);
        
        result.set(0, 0, 1);
        result.set(1, 0, 1);
        result.set(0, 1, 1);
        result.set(1, 1, 1);
        
        GrayImage actualResult = cc.apply(binaryImage);
        
        assertNotNull(actualResult);
        assertEquals(result, actualResult);
    }
}