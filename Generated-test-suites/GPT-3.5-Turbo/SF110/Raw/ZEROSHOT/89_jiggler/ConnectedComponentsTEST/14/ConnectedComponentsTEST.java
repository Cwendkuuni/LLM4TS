import jigl.image.BinaryImage;
import jigl.image.GrayImage;
import jigl.image.types.ImageNotSupportedException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ConnectedComponentsTest {

    private ConnectedComponents connectedComponents;
    private BinaryImage binaryImage;

    @Before
    public void setUp() throws Exception {
        connectedComponents = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);
        binaryImage = new BinaryImage(5, 5);
    }

    @Test
    public void testApply() throws Exception {
        binaryImage.set(1, 1, 1);
        binaryImage.set(1, 2, 1);
        binaryImage.set(2, 1, 1);
        binaryImage.set(2, 2, 1);
        binaryImage.set(3, 3, 1);

        GrayImage result = connectedComponents.apply(binaryImage);

        assertEquals(1, result.get(1, 1).intValue());
        assertEquals(1, result.get(1, 2).intValue());
        assertEquals(1, result.get(2, 1).intValue());
        assertEquals(1, result.get(2, 2).intValue());
        assertEquals(0, result.get(3, 3).intValue());
    }

    @Test(expected = ImageNotSupportedException.class)
    public void testApplyThrowsImageNotSupportedException() throws Exception {
        BinaryImage unsupportedImage = new BinaryImage(5, 5, 3);
        connectedComponents.apply(unsupportedImage);
    }
}