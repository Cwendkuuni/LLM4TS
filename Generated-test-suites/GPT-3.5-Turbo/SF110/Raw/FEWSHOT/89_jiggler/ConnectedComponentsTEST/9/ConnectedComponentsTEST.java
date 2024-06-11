package jigl.image.ops;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class ConnectedComponentsTest {

    @Test
    public void testApply() {
        ConnectedComponents cc = new ConnectedComponents(0, 1);
        
        BinaryImage image1 = new BinaryImage(3, 3);
        image1.set(0, 0, 1);
        image1.set(0, 1, 0);
        image1.set(0, 2, 1);
        image1.set(1, 0, 0);
        image1.set(1, 1, 0);
        image1.set(1, 2, 0);
        image1.set(2, 0, 1);
        image1.set(2, 1, 1);
        image1.set(2, 2, 1);
        
        GrayImage result1 = cc.apply(image1);
        GrayImage expected1 = new GrayImage(3, 3);
        expected1.set(0, 0, 1);
        expected1.set(0, 1, -1);
        expected1.set(0, 2, 2);
        expected1.set(1, 0, -1);
        expected1.set(1, 1, -1);
        expected1.set(1, 2, -1);
        expected1.set(2, 0, 3);
        expected1.set(2, 1, 3);
        expected1.set(2, 2, 3);
        assertEquals(expected1, result1);
        
        BinaryImage image2 = new BinaryImage(5, 5);
        image2.set(0, 0, 0);
        image2.set(0, 1, 1);
        image2.set(0, 2, 0);
        image2.set(0, 3, 1);
        image2.set(0, 4, 1);
        image2.set(1, 0, 0);
        image2.set(1, 1, 1);
        image2.set(1, 2, 0);
        image2.set(1, 3, 1);
        image2.set(1, 4, 1);
        image2.set(2, 0, 0);
        image2.set(2, 1, 1);
        image2.set(2, 2, 0);
        image2.set(2, 3, 0);
        image2.set(2, 4, 1);
        image2.set(3, 0, 0);
        image2.set(3, 1, 1);
        image2.set(3, 2, 0);
        image2.set(3, 3, 0);
        image2.set(3, 4, 1);
        image2.set(4, 0, 0);
        image2.set(4, 1, 0);
        image2.set(4, 2, 0);
        image2.set(4, 3, 0);
        image2.set(4, 4, 0);
        
        GrayImage result2 = cc.apply(image2);
        GrayImage expected2 = new GrayImage(5, 5);
        expected2.set(0, 0, -1);
        expected2.set(0, 1, 1);
        expected2.set(0, 2, -1);
        expected2.set(0, 3, 2);
        expected2.set(0, 4, 2);
        expected2.set(1, 0, -1);
        expected2.set(1, 1, 1);
        expected2.set(1, 2, -1);
        expected2.set(1, 3, 3);
        expected2.set(1, 4, 3);
        expected2.set(2, 0, -1);
        expected2.set(2, 1, 1);
        expected2.set(2, 2, -1);
        expected2.set(2, 3, -1);
        expected2.set(2, 4, 4);
        expected2.set(3, 0, -1);
        expected2.set(3, 1, 1);
        expected2.set(3, 2, -1);
        expected2.set(3, 3, -1);
        expected2.set(3, 4, 4);
        expected2.set(4, 0, -1);
        expected2.set(4, 1, -1);
        expected2.set(4, 2, -1);
        expected2.set(4, 3, -1);
        expected2.set(4, 4, -1);
        assertEquals(expected2, result2);
        
    }

}