import org.junit.Test;
import static org.junit.Assert.*;

public class ConnectedComponentsTest {

    @Test
    public void testConnectedComponentsFourConnected() {
        try {
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 1);
            BinaryImage image = new BinaryImage(3, 3);
            // Set all pixels as background
            for (int x = 0; x < image.X(); x++) {
                for (int y = 0; y < image.Y(); y++) {
                    image.set(x, y, 1);
                }
            }
            // Set some pixels as foreground
            image.set(1, 1, 0);
            image.set(1, 2, 0);
            GrayImage result = cc.apply(image);
            // Check if the result is as expected
            assertEquals(result.get(0, 0), Integer.valueOf(0));
            assertEquals(result.get(1, 1), Integer.valueOf(1));
            assertEquals(result.get(1, 2), Integer.valueOf(1));
        } catch (InvalidArgumentException e) {
            fail("Invalid argument exception");
        }
    }

    @Test
    public void testConnectedComponentsEightConnected() {
        try {
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 1);
            BinaryImage image = new BinaryImage(3, 3);
            // Set all pixels as background
            for (int x = 0; x < image.X(); x++) {
                for (int y = 0; y < image.Y(); y++) {
                    image.set(x, y, 1);
                }
            }
            // Set some pixels as foreground
            image.set(1, 1, 0);
            image.set(1, 2, 0);
            GrayImage result = cc.apply(image);
            // Check if the result is as expected
            assertEquals(result.get(0, 0), Integer.valueOf(0));
            assertEquals(result.get(1, 1), Integer.valueOf(1));
            assertEquals(result.get(1, 2), Integer.valueOf(1));
        } catch (InvalidArgumentException e) {
            fail("Invalid argument exception");
        }
    }
  
    @Test
    public void testConnectedComponentsInvalidArgument() {
        try {
            ConnectedComponents cc = new ConnectedComponents(2, 1);
            fail("Did not throw invalid argument exception");
        } catch (InvalidArgumentException e) {
            assertTrue(true);
        }
    }

}