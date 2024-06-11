import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ConnectedComponentsTest {

    @Test
    public void constructor_validParameters_noExceptionThrown() {
        assertDoesNotThrow(() -> new ConnectedComponents(0, 0));
    }

    @Test
    public void constructor_invalidNumberNeighbors_throwsInvalidArgumentException() {
        assertThrows(InvalidArgumentException.class, () -> new ConnectedComponents(2, 0));
    }

    @Test
    public void constructor_invalidBackgroundColor_throwsInvalidArgumentException() {
        assertThrows(InvalidArgumentException.class, () -> new ConnectedComponents(0, 2));
    }

    @Test
    public void apply_backgroundColor_zeroReturn() {
        BinaryImage image = new BinaryImage(2, 2);
        image.set(0, 0, 0);
        image.set(0, 1, 1);
        image.set(1, 0, 1);
        image.set(1, 1, 1);

        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        GrayImage result = cc.apply(image);

        assertEquals(0, result.get(0, 0));
        assertEquals(0, result.get(0, 1));
        assertEquals(1, result.get(1, 0));
        assertEquals(1, result.get(1, 1));
    }

    @Test
    public void apply_fourConnected_zeroAndOneReturn() {
        BinaryImage image = new BinaryImage(2, 2);
        image.set(0, 0, 1);
        image.set(0, 1, 1);
        image.set(1, 0, 1);
        image.set(1, 1, 1);

        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        GrayImage result = cc.apply(image);

        assertEquals(1, result.get(0, 0));
        assertEquals(1, result.get(0, 1));
        assertEquals(1, result.get(1, 0));
        assertEquals(1, result.get(1, 1));
    }

    @Test
    public void apply_eightConnected_zeroAndOneReturn() {
        BinaryImage image = new BinaryImage(2, 2);
        image.set(0, 0, 1);
        image.set(0, 1, 1);
        image.set(1, 0, 1);
        image.set(1, 1, 1);

        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);
        GrayImage result = cc.apply(image);

        assertEquals(1, result.get(0, 0));
        assertEquals(1, result.get(0, 1));
        assertEquals(1, result.get(1, 0));
        assertEquals(1, result.get(1, 1));
    }

    @Test
    public void apply_eightConnected_threeDistinctLabels() {
        BinaryImage image = new BinaryImage(2, 2);
        image.set(0, 0, 1);
        image.set(0, 1, 0);
        image.set(1, 0, 0);
        image.set(1, 1, 1);

        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);
        GrayImage result = cc.apply(image);

        assertEquals(1, result.get(0, 0));
        assertEquals(0, result.get(0, 1));
        assertEquals(0, result.get(1, 0));
        assertEquals(2, result.get(1, 1));
    }

    @Test
    public void apply_fourConnected_allSameLabel() {
        BinaryImage image = new BinaryImage(2, 2);
        image.set(0, 0, 0);
        image.set(0, 1, 0);
        image.set(1, 0, 0);
        image.set(1, 1, 0);

        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        GrayImage result = cc.apply(image);

        assertEquals(0, result.get(0, 0));
        assertEquals(0, result.get(0, 1));
        assertEquals(0, result.get(1, 0));
        assertEquals(0, result.get(1, 1));
    }

    @Test
    public void apply_fourConnected_diffLabelsConnected() {
        BinaryImage image = new BinaryImage(2, 2);
        image.set(0, 0, 0);
        image.set(0, 1, 1);
        image.set(1, 0, 1);
        image.set(1, 1, 0);

        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        GrayImage result = cc.apply(image);

        assertEquals(0, result.get(0, 0));
        assertEquals(1, result.get(0, 1));
        assertEquals(1, result.get(1, 0));
        assertEquals(0, result.get(1, 1));
    }

    @Test
    public void apply_eightConnected_diffLabelsConnected() {
        BinaryImage image = new BinaryImage(2, 2);
        image.set(0, 0, 0);
        image.set(0, 1, 1);
        image.set(1, 0, 1);
        image.set(1, 1, 0);

        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);
        GrayImage result = cc.apply(image);

        assertEquals(0, result.get(0, 0));
        assertEquals(1, result.get(0, 1));
        assertEquals(1, result.get(1, 0));
        assertEquals(0, result.get(1, 1));
    }

    @Test
    public void apply_backgroundNotZero_notChangeColor() {
        BinaryImage image = new BinaryImage(2, 2);
        image.set(0, 0, 1);
        image.set(0, 1, 1);
        image.set(1, 0, 1);
        image.set(1, 1, 1);

        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 2);
        GrayImage result = cc.apply(image);

        assertEquals(1, result.get(0, 0));
        assertEquals(1, result.get(0, 1));
        assertEquals(1, result.get(1, 0));
        assertEquals(1, result.get(1, 1));
    }

}