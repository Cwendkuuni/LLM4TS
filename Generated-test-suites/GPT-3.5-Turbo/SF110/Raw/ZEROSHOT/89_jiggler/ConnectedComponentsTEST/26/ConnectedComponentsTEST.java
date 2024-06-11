import org.junit.Test;
import static org.junit.Assert.*;

public class ConnectedComponentsTest {

    @Test
    public void testConstructor() throws InvalidArgumentException {
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        assertNotNull(cc);
    }

    @Test(expected = InvalidArgumentException.class)
    public void testConstructorInvalidNeighbors() throws InvalidArgumentException {
        ConnectedComponents cc = new ConnectedComponents(3, 0);
    }

    @Test(expected = InvalidArgumentException.class)
    public void testConstructorInvalidBackground() throws InvalidArgumentException {
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 2);
    }

    @Test
    public void testApply() throws InvalidArgumentException {
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        BinaryImage binaryImage = new BinaryImage(10, 10);
        GrayImage result = cc.apply(binaryImage);
        assertNotNull(result);
    }
}