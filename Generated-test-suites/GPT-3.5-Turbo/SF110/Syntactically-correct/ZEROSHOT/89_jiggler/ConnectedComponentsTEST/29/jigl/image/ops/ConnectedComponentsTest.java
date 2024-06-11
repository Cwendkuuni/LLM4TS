import jigl.image.BinaryImage;
import jigl.image.GrayImage;
import jigl.image.exceptions.InvalidArgumentException;
import org.junit.Assert;
import org.junit.Test;

public class ConnectedComponentsTest {

    @Test
    public void testApply() {
        ConnectedComponents cc = null;
        try {
            cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        } catch (InvalidArgumentException e) {
            Assert.fail("Unexpected exception thrown");
        }
        
        BinaryImage image = new BinaryImage(2, 2);
        image.set(0, 0, 1);
        image.set(1, 0, 0);
        image.set(0, 1, 1);
        image.set(1, 1, 1);
        
        GrayImage result = cc.apply(image);
        
        Assert.assertEquals(1, result.get(0, 0));
        Assert.assertEquals(0, result.get(1, 0));
        Assert.assertEquals(2, result.get(0, 1));
        Assert.assertEquals(2, result.get(1, 1));
    }
    
    @Test
    public void testApplyWithInvalidNeighbors() {
        try {
            new ConnectedComponents(2, 0);
            Assert.fail("Expected InvalidArgumentException not thrown");
        } catch (InvalidArgumentException e) {
            // exception correctly thrown
        }
    }
    
    @Test
    public void testApplyWithInvalidBackground() {
        try {
            new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 2);
            Assert.fail("Expected InvalidArgumentException not thrown");
        } catch (InvalidArgumentException e) {
            // exception correctly thrown
        }
    }
}