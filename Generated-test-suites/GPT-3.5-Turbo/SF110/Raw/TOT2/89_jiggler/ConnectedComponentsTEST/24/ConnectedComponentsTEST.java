import jigl.image.ops.ConnectedComponents;
import jigl.image.types.BinaryImage;
import jigl.image.GrayImage;
import org.junit.Test;
import static org.junit.Assert.*;

public class ConnectedComponentsTest {

    @Test
    public void testConstructorValidArguments() {
        try {
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
            assertNotNull(cc);
        } catch (Exception e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testConstructorInvalidArguments() {
        try {
            ConnectedComponents cc = new ConnectedComponents(2, 0);
            fail("Invalid arguments, expected InvalidArgumentException");
        } catch (InvalidArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testApplyBasicFunctionality() {
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        BinaryImage inputImage = new BinaryImage(5, 5);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (i == 2 && j == 2) {
                    inputImage.set(i, j, 1);
                } else {
                    inputImage.set(i, j, 0);
                }
            }
        }
        GrayImage resultImage = cc.apply(inputImage);
        assertNotNull(resultImage);
    }

    @Test
    public void testApplyEdgeCases() {
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 1);
        BinaryImage inputImage = new BinaryImage(1, 1);
        inputImage.set(0, 0, 1);
        GrayImage resultImage = cc.apply(inputImage);
        assertNotNull(resultImage);
    }

    @Test
    public void testApplyExceptionHandling() {
        try {
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
            BinaryImage inputImage = new BinaryImage(5, 5);
            cc.apply(inputImage); // Input image size is different
            fail("Expected InvalidArgumentException");
        } catch (InvalidArgumentException e) {
            // Expected exception
        }
    }
}