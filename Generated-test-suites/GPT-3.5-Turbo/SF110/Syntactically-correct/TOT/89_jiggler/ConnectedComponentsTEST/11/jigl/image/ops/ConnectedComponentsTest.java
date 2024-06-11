import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import jigl.image.*;
import jigl.image.exceptions.*;

public class ConnectedComponentsTest {

    private ConnectedComponents component;

    @Before
    public void setUp() throws InvalidArgumentException {
        // Initialize the component with four-connected neighbors and background as 0
        component = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
    }

    // Test for positive cases

    @Test
    public void testApply_PositiveCase1() {
        // Test with a binary image containing no foreground pixels
        BinaryImage inputImage = new BinaryImage(10, 10);
        GrayImage expectedOutput = new GrayImage(10, 10);
        GrayImage actualOutput = component.apply(inputImage);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testApply_PositiveCase2() {
        // Test with a binary image containing one connected component
        BinaryImage inputImage = new BinaryImage(10, 10);
        inputImage.set(0, 0, 1);
        inputImage.set(0, 1, 1);
        inputImage.set(1, 0, 1);
        inputImage.set(1, 1, 1);
        GrayImage expectedOutput = new GrayImage(10, 10);
        expectedOutput.set(0, 0, 1);
        expectedOutput.set(0, 1, 1);
        expectedOutput.set(1, 0, 1);
        expectedOutput.set(1, 1, 1);
        GrayImage actualOutput = component.apply(inputImage);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testApply_PositiveCase3() {
        // Test with a binary image containing multiple connected components
        BinaryImage inputImage = new BinaryImage(10, 10);
        inputImage.set(0, 0, 1);
        inputImage.set(0, 1, 1);
        inputImage.set(1, 0, 1);
        inputImage.set(1, 1, 1);
        inputImage.set(5, 5, 1); // Additional connected component
        GrayImage expectedOutput = new GrayImage(10, 10);
        expectedOutput.set(0, 0, 1);
        expectedOutput.set(0, 1, 1);
        expectedOutput.set(1, 0, 1);
        expectedOutput.set(1, 1, 1);
        expectedOutput.set(5, 5, 2);
        GrayImage actualOutput = component.apply(inputImage);
        assertEquals(expectedOutput, actualOutput);
    }

    // Test for edge cases

    @Test
    public void testApply_EdgeCase1() {
        // Test with the minimum allowed dimensions (1x1) for the input image
        BinaryImage inputImage = new BinaryImage(1, 1);
        inputImage.set(0, 0, 1);
        GrayImage expectedOutput = new GrayImage(1, 1);
        expectedOutput.set(0, 0, 1);
        GrayImage actualOutput = component.apply(inputImage);
        assertEquals(actualOutput, expectedOutput);
    }

    @Test
    public void testApply_EdgeCase2() {
        // Test with the maximum allowed dimensions (1000x1000) for the input image
        BinaryImage inputImage = new BinaryImage(1000, 1000);
        GrayImage expectedOutput = new GrayImage(1000, 1000);
        GrayImage actualOutput = component.apply(inputImage);
        assertEquals(actualOutput, expectedOutput);
    }

    // Test for error scenarios

    @Test(expected=InvalidArgumentException.class)
    public void testConstructor_InvalidNeighbor() throws InvalidArgumentException {
        // Initialize the component with an invalid neighbors value
        ConnectedComponents invalidComponent = new ConnectedComponents(2, 0);
    }

    @Test(expected=InvalidArgumentException.class)
    public void testConstructor_InvalidBackground() throws InvalidArgumentException {
        // Initialize the component with an invalid background value
        ConnectedComponents invalidComponent = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 2);
    }

    @Test(expected=InvalidArgumentException.class)
    public void testApply_NullInputImage() throws InvalidArgumentException {
        // Test with a null input image
        BinaryImage nullImage = null;
        component.apply(nullImage);
    }

}