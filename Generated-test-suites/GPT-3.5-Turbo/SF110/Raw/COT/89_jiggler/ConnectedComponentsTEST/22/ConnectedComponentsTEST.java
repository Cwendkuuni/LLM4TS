import static org.junit.Assert.assertEquals;
import org.junit.Test;
import jigl.image.types.BinaryImage;
import jigl.image.types.GrayImage;
import jigl.image.exceptions.InvalidArgumentException;

public class ConnectedComponentsTest {

    @Test
    public void testConstructor() {
        try {
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        } catch (InvalidArgumentException e) {
            // Make sure no exceptions are thrown
            // Test constructor with valid arguments
            assertEquals(ConnectedComponents.FOUR_CONNECTED, cc.neighbors);
            assertEquals(0, cc.background);
        }
    }

    @Test
    public void testApply() {
        BinaryImage image = new BinaryImage(imageData); // provide sample image data
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        GrayImage result = cc.apply(image);
        // Assert the correctness of the result
        
        // Test with a 1x1 image
        BinaryImage image1x1 = new BinaryImage(1, 1);
        image1x1.set(0, 0, 1);
        ConnectedComponents cc1x1 = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        GrayImage result1x1 = cc1x1.apply(image1x1);
        assertEquals(1, result1x1.get(0, 0));
        
        // Test with a 2x2 image
        BinaryImage image2x2 = new BinaryImage(2, 2);
        image2x2.set(0, 0, 1);
        image2x2.set(1, 1, 1);
        ConnectedComponents cc2x2 = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        GrayImage result2x2 = cc2x2.apply(image2x2);
        assertEquals(1, result2x2.get(0, 0));
        assertEquals(2, result2x2.get(1, 1));
    }
}

