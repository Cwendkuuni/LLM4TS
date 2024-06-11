import static org.junit.Assert.*;
import jigl.image.*;
import jigl.image.ops.*;
import org.junit.Test;

public class ConnectedComponentsTest {

    @Test
    public void testApply() {
        // Test case 1: Basic functionality
        BinaryImage image1 = new BinaryImage(5, 5);
        image1.set(0, 0, 1);
        image1.set(1, 1, 1);
        image1.set(2, 2, 1);
        image1.set(3, 3, 1);
        image1.set(4, 4, 1);
        int[] equiv1 = new int[100000];
        equiv1[0] = -1;
        GrayImage expected1 = new GrayImage(5, 5);
        expected1.set(0, 0, Integer.valueOf(1));
        expected1.set(1, 1, Integer.valueOf(1));
        expected1.set(2, 2, Integer.valueOf(1));
        expected1.set(3, 3, Integer.valueOf(1));
        expected1.set(4, 4, Integer.valueOf(1));
        ConnectedComponents cc1 = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        GrayImage result1 = cc1.apply(image1);
        assertArrayEquals(expected1.getData(), result1.getData());

        // Test case 2: Empty image
        BinaryImage image2 = new BinaryImage(0, 0);
        int[] equiv2 = new int[100000];
        equiv2[0] = -1;
        GrayImage expected2 = new GrayImage(0, 0);
        ConnectedComponents cc2 = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        GrayImage result2 = cc2.apply(image2);
        assertArrayEquals(expected2.getData(), result2.getData());
    }

}