import org.junit.Test;
import static org.junit.Assert.*;

public class ConnectedComponentsTest {

    @Test
    public void testConstructor_validArguments() {
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        assertEquals(ConnectedComponents.FOUR_CONNECTED, cc.neighbors);
        assertEquals(0, cc.background);
    }

    @Test(expected = InvalidArgumentException.class)
    public void testConstructor_invalidArguments() {
        ConnectedComponents cc = new ConnectedComponents(2, 3);
    }

    @Test
    public void testApply_basicFunctionality() {
        BinaryImage image = new BinaryImage(2, 2);
        image.set(0, 0, 1);
        image.set(1, 1, 1);
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        GrayImage result = cc.apply(image);
        assertEquals(Integer.valueOf(1), result.get(0, 0));
        assertEquals(Integer.valueOf(2), result.get(1, 1));
    }

    @Test
    public void testApply_edgeCases() {
        // Test when BinaryImage has only one pixel
        BinaryImage image = new BinaryImage(1, 1);
        image.set(0, 0, 1);
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);
        GrayImage result = cc.apply(image);
        assertEquals(Integer.valueOf(1), result.get(0, 0));

        // Test when BinaryImage has all pixels set to background color
        BinaryImage image2 = new BinaryImage(2, 2);
        image2.setAll(0);
        ConnectedComponents cc2 = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        GrayImage result2 = cc2.apply(image2);
        assertEquals(Integer.valueOf(0), result2.get(0, 0));
        assertEquals(Integer.valueOf(0), result2.get(1, 1));
    }

}