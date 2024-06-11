import static org.junit.Assert.*;
import org.junit.Test;
import jigl.image.exceptions.InvalidArgumentException;
import jigl.image.ops.ConnectedComponents;
import jigl.image.types.BinaryImage;
import jigl.image.types.GrayImage;

public class ConnectedComponentsTest {

    @Test
    public void testConstructorFourConnected() {
        try {
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
            assertNotNull(cc);
        } catch (InvalidArgumentException e) {
            fail("Test failed! InvalidArgumentException should not have been thrown.");
        }
    }
    
    @Test
    public void testConstructorEightConnected() {
        try {
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 1);
            assertNotNull(cc);
        } catch (InvalidArgumentException e) {
            fail("Test failed! InvalidArgumentException should not have been thrown.");
        }
    }
    
    @Test
    public void testConstructorInvalidNumberNeighbors() {
        try {
            ConnectedComponents cc = new ConnectedComponents(2, 0);
            fail("Test failed! InvalidArgumentException should have been thrown.");
        } catch (InvalidArgumentException e) {
            assertEquals("Test failed! The exception message is incorrect.", "Invalid number of neighbors.", e.getMessage());
        }
    }
    
    @Test
    public void testConstructorInvalidBackgroundColor() {
        try {
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 2);
            fail("Test failed! InvalidArgumentException should have been thrown.");
        } catch (InvalidArgumentException e) {
            assertEquals("Test failed! The exception message is incorrect.", "Invalid background color.", e.getMessage());
        }
    }
    
    @Test
    public void testApplyFourConnected() {
        try {
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
            BinaryImage image = new BinaryImage(3, 3);
            image.set(0, 0, 1);
            image.set(0, 1, 0);
            image.set(0, 2, 1);
            image.set(1, 0, 1);
            image.set(1, 1, 0);
            image.set(1, 2, 1);
            image.set(2, 0, 0);
            image.set(2, 1, 0);
            image.set(2, 2, 0);
            
            GrayImage result = cc.apply(image);
            
            assertNotNull(result);
            assertEquals(1, result.get(0, 0));
            assertEquals(0, result.get(0, 1));
            assertEquals(2, result.get(0, 2));
            assertEquals(1, result.get(1, 0));
            assertEquals(0, result.get(1, 1));
            assertEquals(3, result.get(1, 2));
            assertEquals(0, result.get(2, 0));
            assertEquals(0, result.get(2, 1));
            assertEquals(0, result.get(2, 2));
            
        } catch (InvalidArgumentException e) {
            fail("Test failed! InvalidArgumentException should not have been thrown.");
        }
    }
    
    @Test
    public void testApplyEightConnected() {
        try {
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 1);
            BinaryImage image = new BinaryImage(3, 3);
            image.set(0, 0, 1);
            image.set(0, 1, 0);
            image.set(0, 2, 1);
            image.set(1, 0, 1);
            image.set(1, 1, 0);
            image.set(1, 2, 1);
            image.set(2, 0, 0);
            image.set(2, 1, 0);
            image.set(2, 2, 0);
            
            GrayImage result = cc.apply(image);
            
            assertNotNull(result);
            assertEquals(1, result.get(0, 0));
            assertEquals(0, result.get(0, 1));
            assertEquals(2, result.get(0, 2));
            assertEquals(1, result.get(1, 0));
            assertEquals(0, result.get(1, 1));
            assertEquals(2, result.get(1, 2));
            assertEquals(0, result.get(2, 0));
            assertEquals(0, result.get(2, 1));
            assertEquals(0, result.get(2, 2));
            
        } catch (InvalidArgumentException e) {
            fail("Test failed! InvalidArgumentException should not have been thrown.");
        }
    }
    
    @Test
    public void testApplyFourConnectedOnlyBackgroundPixels() {
        try {
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
            BinaryImage image = new BinaryImage(3, 3);
            
            GrayImage result = cc.apply(image);
            
            assertNotNull(result);
            assertEquals(0, result.get(0, 0));
            assertEquals(0, result.get(0, 1));
            assertEquals(0, result.get(0, 2));
            assertEquals(0, result.get(1, 0));
            assertEquals(0, result.get(1, 1));
            assertEquals(0, result.get(1, 2));
            assertEquals(0, result.get(2, 0));
            assertEquals(0, result.get(2, 1));
            assertEquals(0, result.get(2, 2));
            
        } catch (InvalidArgumentException e) {
            fail("Test failed! InvalidArgumentException should not have been thrown.");
        }
    }
    
    @Test
    public void testApplyEightConnectedOnlyBackgroundPixels() {
        try {
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 1);
            BinaryImage image = new BinaryImage(3, 3);
            
            GrayImage result = cc.apply(image);
            
            assertNotNull(result);
            assertEquals(0, result.get(0, 0));
            assertEquals(0, result.get(0, 1));
            assertEquals(0, result.get(0, 2));
            assertEquals(0, result.get(1, 0));
            assertEquals(0, result.get(1, 1));
            assertEquals(0, result.get(1, 2));
            assertEquals(0, result.get(2, 0));
            assertEquals(0, result.get(2, 1));
            assertEquals(0, result.get(2, 2));
            
        } catch (InvalidArgumentException e) {
            fail("Test failed! InvalidArgumentException should not have been thrown.");
        }
    }
    
    @Test
    public void testApplyFourConnectedAllForegroundPixels() {
        try {
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
            BinaryImage image = new BinaryImage(3, 3);
            image.setAll(1);
            
            GrayImage result = cc.apply(image);
            
            assertNotNull(result);
            assertEquals(1, result.get(0, 0));
            assertEquals(1, result.get(0, 1));
            assertEquals(1, result.get(0, 2));
            assertEquals(1, result.get(1, 0));
            assertEquals(1, result.get(1, 1));
            assertEquals(1, result.get(1, 2));
            assertEquals(1, result.get(2, 0));
            assertEquals(1, result.get(2, 1));
            assertEquals(1, result.get(2, 2));
            
        } catch (InvalidArgumentException e) {
            fail("Test failed! InvalidArgumentException should not have been thrown.");
        }
    }
    
    @Test
    public void testApplyEightConnectedAllForegroundPixels() {
        try {
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 1);
            BinaryImage image = new BinaryImage(3, 3);
            image.setAll(1);
            
            GrayImage result = cc.apply(image);
            
            assertNotNull(result);
            assertEquals(1, result.get(0, 0));
            assertEquals(1, result.get(0, 1));
            assertEquals(1, result.get(0, 2));
            assertEquals(1, result.get(1, 0));
            assertEquals(1, result.get(1, 1));
            assertEquals(1, result.get(1, 2));
            assertEquals(1, result.get(2, 0));
            assertEquals(1, result.get(2, 1));
            assertEquals(1, result.get(2, 2));
            
        } catch (InvalidArgumentException e) {
            fail("Test failed! InvalidArgumentException should not have been thrown.");
        }
    }
    
    @Test
    public void testApplyFourConnectedOnePixel() {
        try {
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
            BinaryImage image = new BinaryImage(1, 1);
            image.set(0, 0, 1);
            
            GrayImage result = cc.apply(image);
            
            assertNotNull(result);
            assertEquals(1, result.get(0, 0));
            
        } catch (InvalidArgumentException e) {
            fail("Test failed! InvalidArgumentException should not have been thrown.");
        }
    }
    
    @Test
    public void testApplyEightConnectedOnePixel() {
        try {
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 1);
            BinaryImage image = new BinaryImage(1, 1);
            image.set(0, 0, 1);
            
            GrayImage result = cc.apply(image);
            
            assertNotNull(result);
            assertEquals(1, result.get(0, 0));
            
        } catch (InvalidArgumentException e) {
            fail("Test failed! InvalidArgumentException should not have been thrown.");
        }
    }
}