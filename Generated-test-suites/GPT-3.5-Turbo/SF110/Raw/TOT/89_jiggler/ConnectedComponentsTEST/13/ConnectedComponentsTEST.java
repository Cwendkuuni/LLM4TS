import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ConnectedComponentsTest {

    private ConnectedComponents cc;

    @Before
    public void setUp() {
        cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
    }

    @Test
    public void testApply_WithBackgroundZero_ShouldReturnImageWithOnlyZeroValues() {
        BinaryImage image = new BinaryImage(3, 3);
        image.setAll(0);
        GrayImage result = cc.apply(image);
        assertEquals(0, result.get(0, 0));
        assertEquals(0, result.get(1, 1));
        assertEquals(0, result.get(2, 2));
    }

    @Test
    public void testApply_WithBackgroundOne_ShouldReturnImageWithOnlyNonZeroValues() {
        BinaryImage image = new BinaryImage(3, 3);
        image.setAll(1);
        GrayImage result = cc.apply(image);
        assertNotEquals(0, result.get(0, 0));
        assertNotEquals(0, result.get(1, 1));
        assertNotEquals(0, result.get(2, 2));
    }

    @Test
    public void testApply_WithFourConnectedNeighbors_ShouldReturnImageWithCorrectConnectedComponents() {
        BinaryImage image = new BinaryImage(3, 3);
        image.set(0, 0, 1);
        image.set(2, 0, 1);
        image.set(0, 2, 1);
        image.set(2, 2, 1);
        GrayImage result = cc.apply(image);
        assertEquals(result.get(0, 0), result.get(2, 0));
        assertEquals(result.get(0, 0), result.get(0, 2));
        assertNotEquals(result.get(0, 0), result.get(2, 2));
    }

    @Test
    public void testApply_WithEightConnectedNeighbors_ShouldReturnImageWithCorrectConnectedComponents() {
        cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);
        BinaryImage image = new BinaryImage(3, 3);
        image.set(0, 0, 1);
        image.set(2, 0, 1);
        image.set(0, 2, 1);
        image.set(2, 2, 1);
        GrayImage result = cc.apply(image);
        assertEquals(result.get(0, 0), result.get(2, 0));
        assertEquals(result.get(0, 0), result.get(0, 2));
        assertEquals(result.get(0, 0), result.get(2, 2));
    }

    @Test(expected = InvalidArgumentException.class)
    public void testConstructor_WithInvalidNumberNeighbors_ShouldThrowException() {
        new ConnectedComponents(2, 0);
    }

    @Test(expected = InvalidArgumentException.class)
    public void testConstructor_WithInvalidBackgroundColor_ShouldThrowException() {
        new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 2);
    }

}