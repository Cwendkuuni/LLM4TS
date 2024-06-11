import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import jigl.image.BinaryImage;
import jigl.image.GrayImage;
import jigl.image.exceptions.InvalidArgumentException;
import jigl.image.ops.ConnectedComponents;

public class ConnectedComponentsTest {
    
    private ConnectedComponents testInstance1;
    private ConnectedComponents testInstance2;
    
    @Before
    public void setup() throws InvalidArgumentException {
        testInstance1 = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 1);
        testInstance2 = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);
    }
    
    @Test
    public void testConstructor_withValidArgs_setsNeighborsAndBackground() {
        Assert.assertEquals(ConnectedComponents.FOUR_CONNECTED, testInstance1.getNeighbors());
        Assert.assertEquals(1, testInstance1.getBackground());
        Assert.assertEquals(ConnectedComponents.EIGHT_CONNECTED, testInstance2.getNeighbors());
        Assert.assertEquals(0, testInstance2.getBackground());
    }
    
    @Test(expected = InvalidArgumentException.class)
    public void testConstructor_withInvalidNeighbors_throwsInvalidArgumentException() throws InvalidArgumentException {
        new ConnectedComponents(2, 1);
    }
    
    @Test(expected = InvalidArgumentException.class)
    public void testConstructor_withInvalidBackground_throwsInvalidArgumentException() throws InvalidArgumentException {
        new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 2);
    }
    
    @Test
    public void testApply_withFourConnected_neighborsEqualToZero_returnsExpectedConnectedComponents() {
        BinaryImage inputImage = new BinaryImage(4, 4);
        inputImage.initFrom2DIntArray(new int[][] {
            {1, 0, 1, 0},
            {0, 1, 1, 0},
            {0, 1, 1, 0},
            {1, 0, 0, 1}
        });
        
        GrayImage expectedOutput = new GrayImage(4, 4);
        expectedOutput.initFrom2DIntArray(new int[][] {
            {1, 0, 2, 0},
            {0, 3, 2, 0},
            {0, 3, 2, 0},
            {4, 0, 0, 5}
        });
        
        GrayImage actualOutput = testInstance1.apply(inputImage);
        
        Assert.assertEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void testApply_withEightConnected_neighborsEqualToOne_returnsExpectedConnectedComponents() {
        BinaryImage inputImage = new BinaryImage(4, 4);
        inputImage.initFrom2DIntArray(new int[][] {
            {1, 0, 1, 0},
            {0, 1, 1, 0},
            {0, 1, 1, 0},
            {1, 0, 0, 1}
        });
        
        GrayImage expectedOutput = new GrayImage(4, 4);
        expectedOutput.initFrom2DIntArray(new int[][] {
            {1, 0, 1, 0},
            {0, 1, 1, 0},
            {0, 1, 1, 0},
            {4, 0, 0, 4}
        });
        
        GrayImage actualOutput = testInstance2.apply(inputImage);
        
        Assert.assertEquals(expectedOutput, actualOutput);
    }
}