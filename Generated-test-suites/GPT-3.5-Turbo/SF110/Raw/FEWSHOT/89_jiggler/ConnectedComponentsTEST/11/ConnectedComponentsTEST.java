package jigl.image.ops;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import jigl.image.BinaryImage;
import jigl.image.GrayImage;
import jigl.image.exceptions.InvalidArgumentException;
import jigl.image.ops.ConnectedComponents;

public class ConnectedComponentsTest {
    
    private ConnectedComponents cc;
    private BinaryImage binaryImage;
    
    @Before
    public void setUp() {
        try {
            cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
            binaryImage = new BinaryImage(10, 10);
        } catch (InvalidArgumentException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testConstructor() {
        assertNotNull(cc);
        assertEquals(ConnectedComponents.FOUR_CONNECTED, cc.getNeighbors());
        assertEquals(0, cc.getBackground());
        
        try {
            cc = new ConnectedComponents(5, 10);
        } catch (InvalidArgumentException e) {
            assertTrue(e instanceof InvalidArgumentException);
        }
    }
    
    @Test
    public void testApply() {
        GrayImage grayImage = cc.apply(binaryImage);
        
        assertNotNull(grayImage);
        assertEquals(binaryImage.X(), grayImage.X());
        assertEquals(binaryImage.Y(), grayImage.Y());
        
        // Add more assertions to test the conversion process
        // ...
    }
    
    // Add more test methods to cover all scenarios of the apply() method
    
}