import jigl.image.types.BinaryImage;
import jigl.image.types.GrayImage;
import jigl.image.exceptions.InvalidArgumentException;

import org.junit.Test;
import org.junit.Assert;

public class ConnectedComponentsTest {

    @Test
    public void testConstructorValidInput() {
        try {
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
            Assert.assertNotNull(cc);
        } catch (InvalidArgumentException e) {
            Assert.fail("Valid input should not throw exception");
        }
    }

    @Test
    public void testConstructorInvalidInput() {
        try {
            ConnectedComponents cc = new ConnectedComponents(2, 0);
            Assert.fail("Invalid input should throw InvalidArgumentException");
        } catch (InvalidArgumentException e) {
            // Do nothing, exception is expected
        }
    }

    @Test
    public void testApplyBasicFunctionality() {
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        BinaryImage image = new BinaryImage(5, 5);
        GrayImage result = cc.apply(image);
        Assert.assertEquals(5, result.X());
        Assert.assertEquals(5, result.Y());
    }

    // Add more test cases for edge cases and exception handling scenarios

}