import org.junit.*;
import static org.junit.Assert.*;

public class ConnectedComponentsTest {

    @Test
    public void testConstructor_fourConnected_success() {
        try {
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
            assertNotNull(cc);
        } catch (InvalidArgumentException e) {
            fail("Exception occurred");
        }
    }
    
    @Test
    public void testConstructor_eightConnected_success() {
        try {
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 1);
            assertNotNull(cc);
        } catch (InvalidArgumentException e) {
            fail("Exception occurred");
        }
    }
    
    @Test
    public void testConstructor_invalidNumberNeighbors_failure() {
        try {
            ConnectedComponents cc = new ConnectedComponents(2, 1);
            fail("Expected InvalidArgumentException");
        } catch (InvalidArgumentException e) {
            assertEquals("Invalid argument", e.getMessage());
        }
    }
    
    @Test
    public void testConstructor_invalidBackgroundColor_failure() {
        try {
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 2);
            fail("Expected InvalidArgumentException");
        } catch (InvalidArgumentException e) {
            assertEquals("Invalid argument", e.getMessage());
        }
    }

    @Test
    public void testApply_backgroundZero_success() {
        BinaryImage image = new BinaryImage(3, 3);
        image.set(0, 0, 0);
        image.set(0, 1, 1);
        image.set(0, 2, 0);
        image.set(1, 0, 1);
        image.set(1, 1, 1);
        image.set(1, 2, 0);
        image.set(2, 0, 0);
        image.set(2, 1, 0);
        image.set(2, 2, 1);

        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        GrayImage result = cc.apply(image);

        assertEquals(0, result.get(0, 0));
        assertEquals(1, result.get(0, 1));
        assertEquals(0, result.get(0, 2));
        assertEquals(2, result.get(1, 0));
        assertEquals(2, result.get(1, 1));
        assertEquals(0, result.get(1, 2));
        assertEquals(0, result.get(2, 0));
        assertEquals(0, result.get(2, 1));
        assertEquals(3, result.get(2, 2));
    }

    @Test
    public void testApply_backgroundOne_success() {
        BinaryImage image = new BinaryImage(3, 3);
        image.set(0, 0, 1);
        image.set(0, 1, 0);
        image.set(0, 2, 1);
        image.set(1, 0, 0);
        image.set(1, 1, 0);
        image.set(1, 2, 1);
        image.set(2, 0, 1);
        image.set(2, 1, 1);
        image.set(2, 2, 0);

        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 1);
        GrayImage result = cc.apply(image);

        assertEquals(1, result.get(0, 0));
        assertEquals(0, result.get(0, 1));
        assertEquals(1, result.get(0, 2));
        assertEquals(0, result.get(1, 0));
        assertEquals(0, result.get(1, 1));
        assertEquals(1, result.get(1, 2));
        assertEquals(1, result.get(2, 0));
        assertEquals(1, result.get(2, 1));
        assertEquals(0, result.get(2, 2));
    }

    @Test
    public void testApply_fourConnectedEdgeCase_success() {
        BinaryImage image = new BinaryImage(1, 1);
        image.set(0, 0, 1);

        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 1);
        GrayImage result = cc.apply(image);

        assertEquals(1, result.get(0, 0));
    }

    @Test
    public void testApply_eightConnectedEdgeCase_success() {
        BinaryImage image = new BinaryImage(1, 1);
        image.set(0, 0, 1);

        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 1);
        GrayImage result = cc.apply(image);

        assertEquals(1, result.get(0, 0));
    }

    @Test
    public void testApply_invalidBackgroundColor_failure() {
        BinaryImage image = new BinaryImage(1, 1);
        image.set(0, 0, 1);

        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 2);
        try {
            GrayImage result = cc.apply(image);
            fail("Expected InvalidArgumentException");
        } catch (InvalidArgumentException e) {
            assertEquals("Invalid background color", e.getMessage());
        }
    }
    
    @Test
    public void testApply_invalidNumberNeighbors_failure() {
        BinaryImage image = new BinaryImage(1, 1);
        image.set(0, 0, 1);

        ConnectedComponents cc = new ConnectedComponents(2, 1);
        try {
            GrayImage result = cc.apply(image);
            fail("Expected InvalidArgumentException");
        } catch (InvalidArgumentException e) {
            assertEquals("Invalid number of neighbors", e.getMessage());
        }
    }
}