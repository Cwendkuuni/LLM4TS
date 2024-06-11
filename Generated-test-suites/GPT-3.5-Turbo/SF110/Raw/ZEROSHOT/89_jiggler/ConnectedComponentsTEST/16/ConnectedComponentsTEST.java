import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ConnectedComponentsTest {

    private ConnectedComponents connectedComponents;

    @Before
    public void setUp() throws Exception {
        connectedComponents = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
    }

    @Test
    public void testApply() {
        BinaryImage image = new BinaryImage(3, 3);
        int[][] data = {{0, 1, 0}, {1, 0, 1}, {0, 1, 0}};
        image.setData(data);

        GrayImage result = connectedComponents.apply(image);

        int[][] expected = {{0, 1, 0}, {1, 0, 2}, {0, 3, 0}};
        assertArrayEquals(expected, result.getData());
    }
    
    @Test(expected = InvalidArgumentException.class)
    public void testConstructorInvalidArgument1() throws InvalidArgumentException {
        ConnectedComponents invalidConnectedComponents = new ConnectedComponents(2, 0);
    }

    @Test(expected = InvalidArgumentException.class)
    public void testConstructorInvalidArgument2() throws InvalidArgumentException {
        ConnectedComponents invalidConnectedComponents = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 2);
    }
    
}