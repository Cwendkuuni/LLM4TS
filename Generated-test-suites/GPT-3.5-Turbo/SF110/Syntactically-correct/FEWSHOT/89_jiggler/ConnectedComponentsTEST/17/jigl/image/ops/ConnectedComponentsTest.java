package jigl.image.ops;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jigl.image.BinaryImage;
import jigl.image.GrayImage;
import jigl.image.exceptions.InvalidArgumentException;

public class ConnectedComponentsTest {

    @Test
    public void testConstructor_validValues() throws InvalidArgumentException {
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        assertEquals(ConnectedComponents.FOUR_CONNECTED, cc.getNeighbors());
        assertEquals(0, cc.getBackground());

        cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 1);
        assertEquals(ConnectedComponents.EIGHT_CONNECTED, cc.getNeighbors());
        assertEquals(1, cc.getBackground());
    }

    @Test(expected = InvalidArgumentException.class)
    public void testConstructor_invalidValues() throws InvalidArgumentException {
        new ConnectedComponents(2, 0);
    }

    @Test
    public void testApply_fourConnected() {
        BinaryImage inputImage = new BinaryImage(3, 3);
        inputImage.set(0, 0, 1);
        inputImage.set(0, 1, 0);
        inputImage.set(0, 2, 1);
        inputImage.set(1, 0, 0);
        inputImage.set(1, 1, 1);
        inputImage.set(1, 2, 0);
        inputImage.set(2, 0, 1);
        inputImage.set(2, 1, 0);
        inputImage.set(2, 2, 1);

        GrayImage expectedOutput = new GrayImage(3, 3);
        expectedOutput.set(0, 0, 1);
        expectedOutput.set(0, 1, 0);
        expectedOutput.set(0, 2, 2);
        expectedOutput.set(1, 0, 0);
        expectedOutput.set(1, 1, 3);
        expectedOutput.set(1, 2, 0);
        expectedOutput.set(2, 0, 4);
        expectedOutput.set(2, 1, 0);
        expectedOutput.set(2, 2, 5);

        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        GrayImage result = cc.apply(inputImage);

        assertEquals(expectedOutput, result);
    }

    @Test
    public void testApply_eightConnected() {
        BinaryImage inputImage = new BinaryImage(3, 3);
        inputImage.set(0, 0, 1);
        inputImage.set(0, 1, 0);
        inputImage.set(0, 2, 1);
        inputImage.set(1, 0, 0);
        inputImage.set(1, 1, 1);
        inputImage.set(1, 2, 0);
        inputImage.set(2, 0, 1);
        inputImage.set(2, 1, 0);
        inputImage.set(2, 2, 1);

        GrayImage expectedOutput = new GrayImage(3, 3);
        expectedOutput.set(0, 0, 1);
        expectedOutput.set(0, 1, 0);
        expectedOutput.set(0, 2, 2);
        expectedOutput.set(1, 0, 0);
        expectedOutput.set(1, 1, 3);
        expectedOutput.set(1, 2, 0);
        expectedOutput.set(2, 0, 2);
        expectedOutput.set(2, 1, 0);
        expectedOutput.set(2, 2, 4);

        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);
        GrayImage result = cc.apply(inputImage);

        assertEquals(expectedOutput, result);
    }
}