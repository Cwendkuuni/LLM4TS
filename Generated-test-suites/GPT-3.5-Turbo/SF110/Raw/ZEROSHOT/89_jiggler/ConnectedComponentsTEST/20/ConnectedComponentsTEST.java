import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import jigl.image.BinaryImage;
import jigl.image.GrayImage;
import jigl.image.exceptions.InvalidArgumentException;


public class ConnectedComponentsTest {

    private ConnectedComponents cc;

    @Before
    public void setUp() {
        cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
    }

    @Test
    public void testApply_fourConnected_zeroBackground() {
        BinaryImage inputImage = new BinaryImage(5, 5);
        inputImage.set(1, 1, 1);
        inputImage.set(1, 2, 1);
        inputImage.set(2, 2, 1);
        inputImage.set(3, 3, 1);
        inputImage.set(4, 4, 1);

        GrayImage expectedOutput = new GrayImage(5, 5);
        expectedOutput.set(1, 1, 1);
        expectedOutput.set(1, 2, 1);
        expectedOutput.set(1, 2, 1);
        expectedOutput.set(3, 3, 2);
        expectedOutput.set(4, 4, 3);

        GrayImage actualOutput = cc.apply(inputImage);

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testApply_fourConnected_nonzeroBackground() {
        BinaryImage inputImage = new BinaryImage(5, 5);
        inputImage.set(1, 1, 0);
        inputImage.set(1, 2, 0);
        inputImage.set(2, 2, 0);
        inputImage.set(3, 3, 0);
        inputImage.set(4, 4, 0);

        GrayImage expectedOutput = new GrayImage(5, 5);
        expectedOutput.set(1, 1, 0);
        expectedOutput.set(1, 2, 0);
        expectedOutput.set(1, 2, 0);
        expectedOutput.set(3, 3, 0);
        expectedOutput.set(4, 4, 0);

        GrayImage actualOutput = cc.apply(inputImage);

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testApply_eightConnected_zeroBackground() {
        ConnectedComponents cc8 = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);

        BinaryImage inputImage = new BinaryImage(5, 5);
        inputImage.set(1, 1, 1);
        inputImage.set(1, 2, 1);
        inputImage.set(2, 2, 1);
        inputImage.set(3, 3, 1);
        inputImage.set(4, 4, 1);

        GrayImage expectedOutput = new GrayImage(5, 5);
        expectedOutput.set(1, 1, 1);
        expectedOutput.set(1, 2, 1);
        expectedOutput.set(1, 2, 1);
        expectedOutput.set(3, 3, 2);
        expectedOutput.set(4, 4, 3);

        GrayImage actualOutput = cc8.apply(inputImage);

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testApply_eightConnected_nonzeroBackground() {
        ConnectedComponents cc8 = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);

        BinaryImage inputImage = new BinaryImage(5, 5);
        inputImage.set(1, 1, 0);
        inputImage.set(1, 2, 0);
        inputImage.set(2, 2, 0);
        inputImage.set(3, 3, 0);
        inputImage.set(4, 4, 0);

        GrayImage expectedOutput = new GrayImage(5, 5);
        expectedOutput.set(1, 1, 0);
        expectedOutput.set(1, 2, 0);
        expectedOutput.set(1, 2, 0);
        expectedOutput.set(3, 3, 0);
        expectedOutput.set(4, 4, 0);

        GrayImage actualOutput = cc8.apply(inputImage);

        assertEquals(expectedOutput, actualOutput);
    }

    @Test(expected = InvalidArgumentException.class)
    public void testConstructor_invalidNeighbors() throws InvalidArgumentException {
        new ConnectedComponents(2, 0);
    }

    @Test(expected = InvalidArgumentException.class)
    public void testConstructor_invalidBackground() throws InvalidArgumentException {
        new ConnectedComponents(0, 2);
    }
}