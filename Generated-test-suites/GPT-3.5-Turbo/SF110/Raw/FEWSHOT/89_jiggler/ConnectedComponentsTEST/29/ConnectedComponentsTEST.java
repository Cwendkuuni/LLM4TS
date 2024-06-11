import static org.junit.Assert.assertEquals;
import jigl.image.BinaryImage;
import jigl.image.GrayImage;
import jigl.image.exceptions.InvalidArgumentException;
import org.junit.Test;

public class ConnectedComponentsTest {

    @Test
    public void testConstructor() throws InvalidArgumentException {
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        assertEquals(ConnectedComponents.FOUR_CONNECTED, cc.neighbors);
        assertEquals(0, cc.background);

        cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 1);
        assertEquals(ConnectedComponents.EIGHT_CONNECTED, cc.neighbors);
        assertEquals(1, cc.background);
    }

    @Test(expected = InvalidArgumentException.class)
    public void testConstructorInvalidArguments() throws InvalidArgumentException {
        new ConnectedComponents(2, 0);
    }

    @Test
    public void testApply() {
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);

        BinaryImage binaryImage = new BinaryImage(10, 10);
        binaryImage.set(0, 0, 1);
        binaryImage.set(0, 1, 1);
        binaryImage.set(1, 0, 1);

        GrayImage result = cc.apply(binaryImage);

        for (int x = 0; x < result.X(); x++) {
            for (int y = 0; y < result.Y(); y++) {
                if ((x == 0 && y == 0) || (x == 0 && y == 1) || (x == 1 && y == 0)) {
                    assertEquals(1, result.get(x, y).intValue());
                } else {
                    assertEquals(0, result.get(x, y).intValue());
                }
            }
        }
    }
}