import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ConnectedComponentsTest {

    ConnectedComponents connectedComponents;

    @Before
    public void setUp() {
        connectedComponents = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
    }

    @Test
    public void testApply() {
        BinaryImage image = new BinaryImage(10, 10);
        GrayImage result = connectedComponents.apply(image);

        // Test if the result is not null
        assertNotNull(result);

        // Test if the dimensions of the result match the dimensions of the input image
        assertEquals(image.X(), result.X());
        assertEquals(image.Y(), result.Y());

        // Test if the result contains valid labels (0 or positive integers)
        for (int x = 0; x < result.X(); x++) {
            for (int y = 0; y < result.Y(); y++) {
                int label = result.get(x, y);
                assertTrue(label >= 0);
            }
        }
    }

}