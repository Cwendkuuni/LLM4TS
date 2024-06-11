import org.junit.Test;
import static org.junit.Assert.*;

public class ConnectedComponentsTest {

    @Test
    public void testApply() {
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        BinaryImage image = new BinaryImage(5, 5);
        image.set(0, 0, 1);
        image.set(1, 0, 1);
        image.set(0, 1, 1);
        image.set(1, 1, 1);
        image.set(4, 4, 1);
        
        GrayImage result = cc.apply(image);
        
        assertEquals(1, result.get(0, 0));
        assertEquals(1, result.get(1, 0));
        assertEquals(1, result.get(0, 1));
        assertEquals(1, result.get(1, 1));
        assertEquals(0, result.get(4, 4));
    }

}