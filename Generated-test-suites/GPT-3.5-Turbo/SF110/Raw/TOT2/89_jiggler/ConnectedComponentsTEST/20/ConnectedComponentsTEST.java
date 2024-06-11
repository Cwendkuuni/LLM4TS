import org.junit.Test;
import static org.junit.Assert.*;

public class ConnectedComponentsTest {

    @Test
    public void testConstructorValidArguments() {
        ConnectedComponents cc = new ConnectedComponents(0, 0);
        assertEquals(0, cc.neighbors);
        assertEquals(0, cc.background);
    }

    @Test(expected = InvalidArgumentException.class)
    public void testConstructorInvalidArguments() {
        new ConnectedComponents(2, 3);
    }

    @Test
    public void testApply() {
        BinaryImage inputImage = new BinaryImage(3, 3);
        inputImage.set(0, 0, 1);
        inputImage.set(1, 1, 1);
        inputImage.set(2, 2, 1);

        ConnectedComponents cc = new ConnectedComponents(1, 0);
        GrayImage resultImage = cc.apply(inputImage);

        // Add assertions to validate the resultImage
        // Example:
        // assertEquals(1, resultImage.get(0, 0));
    }

    // Add more test cases for other methods as needed
    
}