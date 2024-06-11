package jigl.image.ops;

import jigl.image.exceptions.InvalidArgumentException;
import jigl.image.types.BinaryImage;
import jigl.image.types.GrayImage;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

public class ConnectedComponentsTest extends TestCase {

    ConnectedComponents connectedComponents;

    @Before
    public void setUp() throws Exception {
        connectedComponents = new ConnectedComponents(0, 0);
    }

    @Test
    public void testApply() {
        BinaryImage image = new BinaryImage(10, 10);
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                image.set(x, y, 1);
            }
        }
        GrayImage result = connectedComponents.apply(image);
        assertEquals(1, result.get(0, 0));
    }
    
}