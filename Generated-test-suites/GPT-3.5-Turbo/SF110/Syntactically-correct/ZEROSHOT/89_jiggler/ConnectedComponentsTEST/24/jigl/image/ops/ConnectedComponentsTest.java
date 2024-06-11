import org.junit.Test;
import org.junit.Assert.*;

public class ConnectedComponentsTest {

    @Test
    public void testApply() {
        // Test case 1: BinaryImage with all background pixels
        BinaryImage image1 = new BinaryImage(5, 5);
        image1.init(0);
        ConnectedComponents cc1 = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        GrayImage result1 = cc1.apply(image1);
        assertEquals(result1.X(), 5);
        assertEquals(result1.Y(), 5);
        for (int x = 0; x < result1.X(); x++) {
            for (int y = 0; y < result1.Y(); y++) {
                assertEquals(result1.get(x, y), 0);
            }
        }
        
        // Test case 2: BinaryImage with all foreground pixels
        BinaryImage image2 = new BinaryImage(5, 5);
        image2.init(1);
        ConnectedComponents cc2 = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        GrayImage result2 = cc2.apply(image2);
        assertEquals(result2.X(), 5);
        assertEquals(result2.Y(), 5);
        for (int x = 0; x < result2.X(); x++) {
            for (int y = 0; y < result2.Y(); y++) {
                assertEquals(result2.get(x, y), 1);
            }
        }
        
        // Test case 3: BinaryImage with some background and some foreground pixels
        BinaryImage image3 = new BinaryImage(5, 5);
        image3.set(1, 1, 1);
        image3.set(3, 3, 1);
        ConnectedComponents cc3 = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);
        GrayImage result3 = cc3.apply(image3);
        assertEquals(result3.X(), 5);
        assertEquals(result3.Y(), 5);
        assertEquals(result3.get(0, 0), 0);
        assertEquals(result3.get(1, 1), 2);
        assertEquals(result3.get(3, 3), 3);
        for (int x = 0; x < result3.X(); x++) {
            for (int y = 0; y < result3.Y(); y++) {
                if (x != 1 && y != 1 && x != 3 && y != 3) {
                    assertEquals(result3.get(x, y), 0);
                }
            }
        }
    }
}