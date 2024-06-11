package jigl.image.ops;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jigl.image.BinaryImage;
import jigl.image.GrayImage;
import jigl.image.exceptions.InvalidArgumentException;

public class ConnectedComponentsTest {
    
    @Test
    public void testApply_fourConnected() {
        try {
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
            
            BinaryImage image = new BinaryImage(3, 3);
            image.set(0, 0, 1);
            image.set(1, 0, 1);
            image.set(2, 0, 0);
            image.set(0, 1, 1);
            image.set(1, 1, 1);
            image.set(2, 1, 0);
            image.set(0, 2, 0);
            image.set(1, 2, 0);
            image.set(2, 2, 0);
            
            GrayImage result = cc.apply(image);
            
            assertEquals(1, result.get(0, 0));
            assertEquals(1, result.get(1, 0));
            assertEquals(0, result.get(2, 0));
            assertEquals(1, result.get(0, 1));
            assertEquals(1, result.get(1, 1));
            assertEquals(0, result.get(2, 1));
            assertEquals(0, result.get(0, 2));
            assertEquals(0, result.get(1, 2));
            assertEquals(0, result.get(2, 2));
            
        } catch (InvalidArgumentException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testApply_eightConnected() {
        try {
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);
            
            BinaryImage image = new BinaryImage(3, 3);
            image.set(0, 0, 1);
            image.set(1, 0, 1);
            image.set(2, 0, 0);
            image.set(0, 1, 1);
            image.set(1, 1, 1);
            image.set(2, 1, 0);
            image.set(0, 2, 0);
            image.set(1, 2, 0);
            image.set(2, 2, 0);
            
            GrayImage result = cc.apply(image);
            
            assertEquals(1, result.get(0, 0));
            assertEquals(1, result.get(1, 0));
            assertEquals(0, result.get(2, 0));
            assertEquals(1, result.get(0, 1));
            assertEquals(1, result.get(1, 1));
            assertEquals(0, result.get(2, 1));
            assertEquals(0, result.get(0, 2));
            assertEquals(0, result.get(1, 2));
            assertEquals(0, result.get(2, 2));
            
        } catch (InvalidArgumentException e) {
            e.printStackTrace();
        }
    }
}