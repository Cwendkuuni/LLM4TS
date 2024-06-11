import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ConnectedComponentsTest {

    private ConnectedComponents cc;

    @Before
    public void setUp() {
        cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
    }

    // Test case for the constructor
    @Test
    public void testConstructor() {
        // Typical use case
        assertDoesNotThrow(() -> {
            ConnectedComponents cc = new ConnectedComponents(0, 1);
        });

        // Edge case: numberNeighbors is not 0 or 1
        assertThrows(InvalidArgumentException.class, () -> {
            ConnectedComponents cc = new ConnectedComponents(2, 0);
        });

        // Edge case: backgroundColor is not 0 or 1
        assertThrows(InvalidArgumentException.class, () -> {
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 2);
        });
    }

    // Test case for apply() method
    @Test
    public void testApply() {
        // Typical use case
        BinaryImage image = new BinaryImage(3, 3);
        image.set(0, 0, 1);
        image.set(0, 1, 0);
        image.set(0, 2, 1);
        image.set(1, 0, 1);
        image.set(1, 1, 0);
        image.set(1, 2, 1);
        image.set(2, 0, 1);
        image.set(2, 1, 1);
        image.set(2, 2, 1);

        GrayImage expected = new GrayImage(3, 3);
        expected.set(0, 0, 0);
        expected.set(0, 1, 0);
        expected.set(0, 2, 1);
        expected.set(1, 0, 0);
        expected.set(1, 1, 0);
        expected.set(1, 2, 1);
        expected.set(2, 0, 0);
        expected.set(2, 1, 0);
        expected.set(2, 2, 0);

        assertEquals(expected, cc.apply(image));

        // Edge case: all pixels are background
        BinaryImage allBackground = new BinaryImage(3, 3);

        GrayImage expectedAllBackground = new GrayImage(3, 3);

        assertEquals(expectedAllBackground, cc.apply(allBackground));
    }

}