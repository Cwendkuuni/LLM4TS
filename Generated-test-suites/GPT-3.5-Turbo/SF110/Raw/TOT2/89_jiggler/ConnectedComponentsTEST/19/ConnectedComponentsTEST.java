import org.junit.Test;

import static org.junit.Assert.*;

public class ConnectedComponentsTest {

    @Test
    public void testConstructor_validInput() {
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        assertNotNull(cc);
    }

    @Test(expected = InvalidArgumentException.class)
    public void testConstructor_invalidInput() {
        ConnectedComponents cc = new ConnectedComponents(2, 1);
    }

    @Test
    public void testApply_fourConnected() {
        BinaryImage image = new BinaryImage(3, 3);
        image.set(0, 0, 1);
        image.set(0, 1, 0);
        image.set(0, 2, 1);
        image.set(1, 0, 0);
        image.set(1, 1, 0);
        image.set(1, 2, 1);
        image.set(2, 0, 1);
        image.set(2, 1, 1);
        image.set(2, 2, 1);

        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        GrayImage result = cc.apply(image);

        assertNotNull(result);
        assertEquals(3, result.get(0, 0).intValue());
        assertEquals(0, result.get(0, 1).intValue());
        assertEquals(4, result.get(0, 2).intValue());
        assertEquals(0, result.get(1, 0).intValue());
        assertEquals(0, result.get(1, 1).intValue());
        assertEquals(5, result.get(1, 2).intValue());
        assertEquals(6, result.get(2, 0).intValue());
        assertEquals(6, result.get(2, 1).intValue());
        assertEquals(6, result.get(2, 2).intValue());
    }

    // Add more test cases for other public methods

}