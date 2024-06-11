import org.junit.Test;
import static org.junit.Assert.*;

public class ConnectedComponentsTest {

    @Test
    public void testApply_fourConnected() {
        // Test case for FOUR_CONNECTED
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        BinaryImage image = new BinaryImage(2, 2);
        image.set(0, 0, 1);
        image.set(1, 1, 1);
        GrayImage result = cc.apply(image);
        assertEquals(1, result.get(0, 0).intValue());
        assertEquals(2, result.get(1, 1).intValue());
    }
}