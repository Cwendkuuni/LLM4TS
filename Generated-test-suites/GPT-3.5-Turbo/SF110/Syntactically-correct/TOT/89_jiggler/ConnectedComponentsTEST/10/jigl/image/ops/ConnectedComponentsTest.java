
import org.junit.Test;
import static org.junit.Assert.*;

public class ConnectedComponentsTest {

    @Test
    public void testConstructor_InvalidArguments() {
        try {
            ConnectedComponents cc = new ConnectedComponents(2, 0);
            fail("InvalidArgumentException should have been thrown");
        } catch (InvalidArgumentException e) {
            // InvalidArgumentException should be thrown
        }
    }
    
    @Test
    public void testConstructor_ValidArguments() {
        try {
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 1);
            
            // Verify that constructor sets neighbors and background correctly
            assertEquals(ConnectedComponents.FOUR_CONNECTED, cc.getNeighbors());
            assertEquals(1, cc.getBackground());
            
        } catch (InvalidArgumentException e) {
            fail("InvalidArgumentException should not have been thrown");
        }
    }
    
    @Test
    public void testApply_AllBackground() {
        try {
            // Create a BinaryImage with all pixels as background
            BinaryImage image = new BinaryImage(10, 10);
            
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
            GrayImage result = cc.apply(image);
            
            // Verify that all pixels in result are set to 0
            for (int x = 0; x < result.X(); ++x) {
                for (int y = 0; y < result.Y(); ++y) {
                    assertEquals(0, result.get(x, y).intValue());
                }
            }
            
        } catch (InvalidArgumentException e) {
            fail("InvalidArgumentException should not have been thrown");
        }
    }
    
    @Test
    public void testApply_AllForegroundFourConnected() {
        try {
            // Create a BinaryImage with all pixels as foreground (1) and apply four connected algorithm
            BinaryImage image = new BinaryImage(10, 10);
            image.fill(1);
            
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
            GrayImage result = cc.apply(image);
            
            // Verify that all pixels in result are set to unique values starting from 1
            int currentValue = 1;
            for (int x = 0; x < result.X(); ++x) {
                for (int y = 0; y < result.Y(); ++y) {
                    assertEquals(currentValue, result.get(x, y).intValue());
                    currentValue++;
                }
            }
            
        } catch (InvalidArgumentException e) {
            fail("InvalidArgumentException should not have been thrown");
        }
    }
    
    @Test
    public void testApply_AllForegroundEightConnected() {
        try {
            // Create a BinaryImage with all pixels as foreground (1) and apply eight connected algorithm
            BinaryImage image = new BinaryImage(10, 10);
            image.fill(1);
            
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);
            GrayImage result = cc.apply(image);
            
            // Verify that all pixels in result are set to unique values starting from 1
            int currentValue = 1;
            for (int x = 0; x < result.X(); ++x) {
                for (int y = 0; y < result.Y(); ++y) {
                    assertEquals(currentValue, result.get(x, y).intValue());
                    currentValue++;
                }
            }
            
        } catch (InvalidArgumentException e) {
            fail("InvalidArgumentException should not have been thrown");
        }
    }
    
    @Test
    public void testApply_MixedForegroundAndBackgroundFourConnected() {
        try {
            // Create a BinaryImage with a mix of foreground and background pixels and apply four connected algorithm
            BinaryImage image = new BinaryImage(5, 5);
            image.set(0, 0, 1);
            image.set(0, 1, 1);
            image.set(0, 2, 0);
            image.set(1, 0, 0);
            image.set(1, 1, 1);
            image.set(1, 2, 0);
            image.set(2, 0, 1);
            image.set(2, 1, 0);
            image.set(2, 2, 0);
            
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
            GrayImage result = cc.apply(image);
            
            // Verify that connected foreground pixels are assigned the same value and background pixels are set to 0
            assertEquals(result.get(0, 0).intValue(), result.get(0, 1).intValue());
            assertEquals(result.get(1, 1).intValue(), result.get(2, 0).intValue());
            assertEquals(result.get(0, 2).intValue(), 0);
            
        } catch (InvalidArgumentException e) {
            fail("InvalidArgumentException should not have been thrown");
        }
    }
    
    @Test
    public void testApply_MixedForegroundAndBackgroundEightConnected() {
        try {
            // Create a BinaryImage with a mix of foreground and background pixels and apply eight connected algorithm
            BinaryImage image = new BinaryImage(5, 5);
            image.set(0, 0, 1);
            image.set(0, 1, 1);
            image.set(0, 2, 0);
            image.set(1, 0, 0);
            image.set(1, 1, 1);
            image.set(1, 2, 0);
            image.set(2, 0, 1);
            image.set(2, 1, 0);
            image.set(2, 2, 0);
            
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);
            GrayImage result = cc.apply(image);
            
            // Verify that connected foreground pixels are assigned the same value and background pixels are set to 0
            assertEquals(result.get(0, 0).intValue(), result.get(0, 1).intValue());
            assertEquals(result.get(1, 1).intValue(), result.get(2, 0).intValue());
            assertEquals(result.get(0, 2).intValue(), 0);
            
        } catch (InvalidArgumentException e) {
            fail("InvalidArgumentException should not have been thrown");
        }
    }
    
    @Test
    public void testApply_EdgeCase_Negatives() {
        try {
            // Create a BinaryImage with all negative pixel values and apply four connected algorithm
            BinaryImage image = new BinaryImage(10, 10);
            image.fill(-1);
            
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
            GrayImage result = cc.apply(image);
            
            // Verify that all pixels in result are set to unique values starting from 1
            int currentValue = 1;
            for (int x = 0; x < result.X(); ++x) {
                for (int y = 0; y < result.Y(); ++y) {
                    assertEquals(currentValue, result.get(x, y).intValue());
                    currentValue++;
                }
            }
        } catch (InvalidArgumentException e) {
            fail("InvalidArgumentException should not have been thrown");
        }
    }
    
    @Test
    public void testApply_ErrorScenario_InvalidImage() {
        try {
            // Create a null BinaryImage and attempt to apply the algorithm
            BinaryImage image = null;
            
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
            GrayImage result = cc.apply(image);
            
            fail("NullPointerException should have been thrown");
        } catch (NullPointerException e) {
            // NullPointerException should be thrown
        } catch (InvalidArgumentException e) {
            fail("InvalidArgumentException should not have been thrown");
        }
    }
    
}
