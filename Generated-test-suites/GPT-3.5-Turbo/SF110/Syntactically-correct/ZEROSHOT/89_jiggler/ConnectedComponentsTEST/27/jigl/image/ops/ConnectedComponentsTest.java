import static org.junit.Assert.*;
import org.junit.Test;

public class ConnectedComponentsTest {

    @Test
    public void testApplyFourConnected() {
        try {
            int[][] pixels = {{0, 1, 1, 0, 0},
                              {1, 0, 1, 0, 0},
                              {0, 0, 1, 0, 0},
                              {0, 0, 0, 1, 1}};
            BinaryImage image = new BinaryImage(pixels);
    
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
            GrayImage result = cc.apply(image);
    
            int[][] expectedPixels = {{0, 1, 1, 0, 0},
                                      {1, 0, 1, 0, 0},
                                      {0, 0, 1, 0, 0},
                                      {0, 0, 0, 2, 2}};
            GrayImage expectedImage = new GrayImage(expectedPixels);
    
            assertEquals(expectedImage, result);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testApplyEightConnected() {
        try {
            int[][] pixels = {{0, 1, 1, 0, 0},
                              {1, 0, 1, 0, 0},
                              {0, 0, 1, 0, 0},
                              {0, 0, 0, 1, 1}};
            BinaryImage image = new BinaryImage(pixels);
    
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);
            GrayImage result = cc.apply(image);
    
            int[][] expectedPixels = {{0, 1, 1, 0, 0},
                                      {1, 0, 1, 0, 0},
                                      {0, 0, 1, 0, 0},
                                      {0, 0, 0, 1, 1}};
            GrayImage expectedImage = new GrayImage(expectedPixels);
    
            assertEquals(expectedImage, result);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test(expected = InvalidArgumentException.class)
    public void testInvalidNumberNeighbors() throws InvalidArgumentException {
        new ConnectedComponents(2, 0);
    }

    @Test(expected = InvalidArgumentException.class)
    public void testInvalidBackgroundColor() throws InvalidArgumentException {
        new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 2);
    }
}