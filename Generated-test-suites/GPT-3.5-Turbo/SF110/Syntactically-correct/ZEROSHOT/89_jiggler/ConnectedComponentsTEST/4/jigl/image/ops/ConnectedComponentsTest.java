import org.junit.*;
import static org.junit.Assert.*;

public class ConnectedComponentsTest {

    @Test
    public void testApply() {
        BinaryImage image = new BinaryImage(3, 3);
        image.set(0, 0, 0);
        image.set(0, 1, 1);
        image.set(0, 2, 0);
        image.set(1, 0, 1);
        image.set(1, 1, 1);
        image.set(1, 2, 1);
        image.set(2, 0, 0);
        image.set(2, 1, 1);
        image.set(2, 2, 0);

        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        GrayImage result = cc.apply(image);

        GrayImage expected = new GrayImage(3, 3);
        expected.set(0, 0, 0);
        expected.set(0, 1, 1);
        expected.set(0, 2, 0);
        expected.set(1, 0, 2);
        expected.set(1, 1, 2);
        expected.set(1, 2, 2);
        expected.set(2, 0, 0);
        expected.set(2, 1, 3);
        expected.set(2, 2, 0);

        assertEquals(expected, result);
    }

}