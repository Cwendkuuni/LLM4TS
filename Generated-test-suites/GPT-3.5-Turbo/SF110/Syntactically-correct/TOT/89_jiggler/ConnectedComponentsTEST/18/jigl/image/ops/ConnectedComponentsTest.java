import org.junit.Before;
import org.junit.Test;

public class ConnectedComponentsTest {

    private ConnectedComponents connectedComponents;

    @Before
    public void setUp() throws Exception {
        connectedComponents = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
    }

    @Test
    public void testapply_AllBackground() {
        BinaryImage image = new BinaryImage(10, 10);
        image.fill(0);
        GrayImage actualResult = connectedComponents.apply(image);
        GrayImage expectedResult = new GrayImage(10, 10);
        expectedResult.fill(0);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testapply_EverythingConnected() {
        BinaryImage image = new BinaryImage(5, 5);
        image.fill(1);
        GrayImage actualResult = connectedComponents.apply(image);
        GrayImage expectedResult = new GrayImage(5, 5);
        expectedResult.fill(1);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testapply_OnePixelConnected() {
        BinaryImage image = new BinaryImage(10, 10);
        image.fill(0);
        image.set(5, 5, 1);
        GrayImage actualResult = connectedComponents.apply(image);
        GrayImage expectedResult = new GrayImage(10, 10);
        expectedResult.fill(0);
        expectedResult.set(5, 5, 1);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testapply_EdgeCase() {
        BinaryImage image = new BinaryImage(1, 1);
        image.fill(1);
        GrayImage actualResult = connectedComponents.apply(image);
        GrayImage expectedResult = new GrayImage(1, 1);
        expectedResult.fill(1);
        assertEquals(expectedResult, actualResult);
    }

    @Test(expected = InvalidArgumentException.class)
    public void testConnectedComponents_InvalidArguments() throws InvalidArgumentException {
        ConnectedComponents connectedComponents = new ConnectedComponents(3, 1);
    }

}