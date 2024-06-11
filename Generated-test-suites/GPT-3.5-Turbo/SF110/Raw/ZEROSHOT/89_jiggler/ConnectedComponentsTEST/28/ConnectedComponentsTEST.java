import static org.junit.Assert.*;
import org.junit.Test;

public class ConnectedComponentsTest {

    @Test
    public void testApply() {
        // Test case 1
        BinaryImage image1 = new BinaryImage(3, 3);
        image1.set(0, 0, 0);
        image1.set(1, 0, 1);
        image1.set(2, 0, 0);
        image1.set(0, 1, 1);
        image1.set(1, 1, 1);
        image1.set(2, 1, 1);
        image1.set(0, 2, 0);
        image1.set(1, 2, 0);
        image1.set(2, 2, 0);
        
        ConnectedComponents cc1 = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        GrayImage result1 = cc1.apply(image1);
        GrayImage expected1 = new GrayImage(3, 3);
        expected1.set(0, 0, 0);
        expected1.set(1, 0, 1);
        expected1.set(2, 0, 0);
        expected1.set(0, 1, 2);
        expected1.set(1, 1, 2);
        expected1.set(2, 1, 2);
        expected1.set(0, 2, 0);
        expected1.set(1, 2, 0);
        expected1.set(2, 2, 0);
        assertEquals(expected1, result1);
        
        // Test case 2
        BinaryImage image2 = new BinaryImage(3, 3);
        image2.set(0, 0, 0);
        image2.set(1, 0, 0);
        image2.set(2, 0, 0);
        image2.set(0, 1, 0);
        image2.set(1, 1, 1);
        image2.set(2, 1, 0);
        image2.set(0, 2, 0);
        image2.set(1, 2, 0);
        image2.set(2, 2, 0);
        
        ConnectedComponents cc2 = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);
        GrayImage result2 = cc2.apply(image2);
        GrayImage expected2 = new GrayImage(3, 3);
        expected2.set(0, 0, 0);
        expected2.set(1, 0, 0);
        expected2.set(2, 0, 0);
        expected2.set(0, 1, 0);
        expected2.set(1, 1, 1);
        expected2.set(2, 1, 0);
        expected2.set(0, 2, 0);
        expected2.set(1, 2, 0);
        expected2.set(2, 2, 0);
        assertEquals(expected2, result2);
    }
}