package jigl.image.ops;

import static org.junit.Assert.assertEquals;

import jigl.image.BinaryImage;
import jigl.image.GrayImage;
import jigl.image.exceptions.InvalidArgumentException;
import org.junit.Test;

public class ConnectedComponentsTest {

    @Test(expected = InvalidArgumentException.class)
    public void testConstructorInvalidParams() throws InvalidArgumentException {
        new ConnectedComponents(2, 1);
    }

    @Test
    public void testConstructorValidParams() throws InvalidArgumentException {
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        assertEquals(ConnectedComponents.FOUR_CONNECTED, cc.getNeighbors());
        assertEquals(0, cc.getBackground());

        cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 1);
        assertEquals(ConnectedComponents.EIGHT_CONNECTED, cc.getNeighbors());
        assertEquals(1, cc.getBackground());
    }

    @Test
    public void testApply() {
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        BinaryImage image = new BinaryImage(10, 10);

        // Test case 1: all background pixels
        GrayImage result = cc.apply(image);
        for (int x = 0; x < result.X(); x++) {
            for (int y = 0; y < result.Y(); y++) {
                assertEquals(0, result.get(x, y));
            }
        }

        // Test case 2: all foreground pixels
        image.setAll(1);
        result = cc.apply(image);
        int currentLabel = 1;
        for (int x = 0; x < result.X(); x++) {
            for (int y = 0; y < result.Y(); y++) {
                assertEquals(currentLabel, result.get(x, y));
                currentLabel++;
            }
        }

        // Test case 3: custom configuration
        image.set(0, 0, 0);
        image.set(0, 1, 0);
        image.set(1, 0, 1);
        image.set(1, 1, 0);
        image.set(2, 0, 1);
        image.set(2, 1, 1);
        image.set(2, 2, 0);
        image.set(3, 0, 0);
        image.set(3, 1, 1);
        image.set(3, 2, 0);
        image.set(3, 3, 1);
        image.set(4, 2, 1);
        image.set(4, 3, 1);
        image.set(4, 4, 0);

        result = cc.apply(image);
        int[][] expectedLabels = {
                {0, 0, 1, 1, 2},
                {0, 3, 3, 1, 2},
                {0, 3, 4, 1, 2},
                {0, 0, 4, 5, 5},
                {0, 0, 4, 5, 5}
        };

        for (int x = 0; x < result.X(); x++) {
            for (int y = 0; y < result.Y(); y++) {
                assertEquals(expectedLabels[x][y], result.get(x, y));
            }
        }
    }
}