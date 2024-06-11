import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ConnectedComponentsTest {

    @Before
    public void setUp() {

    }

    @Test
    public void testConstructor() {
        try {
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
            assertNotNull(cc);
        } catch (InvalidArgumentException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testConstructor_invalidNeighbor() {
        try {
            ConnectedComponents cc = new ConnectedComponents(2, 0);
            fail("Exception not thrown");
        } catch (InvalidArgumentException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testConstructor_invalidBackgroundColor() {
        try {
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 2);
            fail("Exception not thrown");
        } catch (InvalidArgumentException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testApply_fourConnected() {
        BinaryImage image = new BinaryImage(2, 2);
        image.set(0, 0, 0);
        image.set(0, 1, 0);
        image.set(1, 0, 1);
        image.set(1, 1, 0);

        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        GrayImage result = cc.apply(image);

        assertEquals(0, result.get(0, 0).intValue());
        assertEquals(0, result.get(0, 1).intValue());
        assertEquals(1, result.get(1, 0).intValue());
        assertEquals(0, result.get(1, 1).intValue());
    }

    @Test
    public void testApply_eightConnected() {
        BinaryImage image = new BinaryImage(2, 2);
        image.set(0, 0, 0);
        image.set(0, 1, 0);
        image.set(1, 0, 1);
        image.set(1, 1, 0);

        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);
        GrayImage result = cc.apply(image);

        assertEquals(0, result.get(0, 0).intValue());
        assertEquals(0, result.get(0, 1).intValue());
        assertEquals(1, result.get(1, 0).intValue());
        assertEquals(0, result.get(1, 1).intValue());
    }

}