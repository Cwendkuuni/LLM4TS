import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import jigl.image.ops.ConnectedComponents;
import jigl.image.exceptions.InvalidArgumentException;

public class ConnectedComponentsTest {

    @Before
    public void setUp() throws Exception {
        // Initialize any necessary objects or variables before each test case
    }

    @Test
    public void testConstructor_validArguments() {
        try {
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
            assertNotNull(cc);
        } catch (InvalidArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = InvalidArgumentException.class)
    public void testConstructor_invalidNumberNeighbors() throws InvalidArgumentException {
        ConnectedComponents cc = new ConnectedComponents(2, 0);
    }

    @Test(expected = InvalidArgumentException.class)
    public void testConstructor_invalidBackgroundColor() throws InvalidArgumentException {
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 2);
    }

    @Test
    public void testApply_imageWithNoBackground() {
        BinaryImage image = new BinaryImage(3, 3, 255);
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        GrayImage result = cc.apply(image);

        assertEquals(image.X(), result.X());
        assertEquals(image.Y(), result.Y());

        for (int x = 0; x < image.X(); x++) {
            for (int y = 0; y < image.Y(); y++) {
                assertEquals(0, result.get(x, y));
            }
        }
    }

    @Test
    public void testApply_imageWithBackground() {
        BinaryImage image = new BinaryImage(3, 3, 0);
        image.set(0, 0, 1);
        image.set(1, 1, 1);
        image.set(2, 2, 1);

        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        GrayImage result = cc.apply(image);

        assertEquals(image.X(), result.X());
        assertEquals(image.Y(), result.Y());

        assertEquals(1, result.get(0, 0));
        assertEquals(2, result.get(1, 1));
        assertEquals(3, result.get(2, 2));
    }

    @Test
    public void testApply_imageWithBackground_ConnectedEight() {
        BinaryImage image = new BinaryImage(3, 3, 0);
        image.set(0, 0, 1);
        image.set(1, 1, 1);
        image.set(2, 2, 1);

        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);
        GrayImage result = cc.apply(image);

        assertEquals(image.X(), result.X());
        assertEquals(image.Y(), result.Y());

        assertEquals(1, result.get(0, 0));
        assertEquals(1, result.get(1, 1));
        assertEquals(1, result.get(2, 2));
    }

    @Test
    public void testApply_imageWithBackground_ConnectedEight_Corners() {
        BinaryImage image = new BinaryImage(3, 3, 0);
        image.set(0, 0, 1);
        image.set(2, 0, 1);
        image.set(0, 2, 1);
        image.set(2, 2, 1);

        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);
        GrayImage result = cc.apply(image);

        assertEquals(image.X(), result.X());
        assertEquals(image.Y(), result.Y());

        assertEquals(1, result.get(0, 0));
        assertEquals(2, result.get(2, 0));
        assertEquals(3, result.get(0, 2));
        assertEquals(4, result.get(2, 2));
    }

    @Test
    public void testApply_imageWithBackground_ConnectedFour_TwoComponents() {
        BinaryImage image = new BinaryImage(3, 3, 0);
        image.set(0, 1, 1);
        image.set(1, 0, 1);
        image.set(2, 1, 1);
        image.set(1, 2, 1);

        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        GrayImage result = cc.apply(image);

        assertEquals(image.X(), result.X());
        assertEquals(image.Y(), result.Y());

        assertEquals(1, result.get(0, 1));
        assertEquals(1, result.get(1, 0));
        assertEquals(2, result.get(2, 1));
        assertEquals(2, result.get(1, 2));
    }

    @Test
    public void testApply_imageWithBackground_ConnectedEight_TwoComponents() {
        BinaryImage image = new BinaryImage(3, 3, 0);
        image.set(0, 1, 1);
        image.set(1, 0, 1);
        image.set(2, 1, 1);
        image.set(1, 2, 1);

        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);
        GrayImage result = cc.apply(image);

        assertEquals(image.X(), result.X());
        assertEquals(image.Y(), result.Y());

        assertEquals(1, result.get(0, 1));
        assertEquals(1, result.get(1, 0));
        assertEquals(1, result.get(2, 1));
        assertEquals(1, result.get(1, 2));
    }

    @Test
    public void testApply_imageWithBackground_ConnectedFour_SameComponent() {
        BinaryImage image = new BinaryImage(3, 3, 0);
        image.set(0, 2, 1);
        image.set(1, 1, 1);
        image.set(2, 2, 1);

        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        GrayImage result = cc.apply(image);

        assertEquals(image.X(), result.X());
        assertEquals(image.Y(), result.Y());

        assertEquals(1, result.get(0, 2));
        assertEquals(1, result.get(1, 1));
        assertEquals(1, result.get(2, 2));
    }

    @Test
    public void testApply_imageWithBackground_ConnectedEight_SameComponent() {
        BinaryImage image = new BinaryImage(3, 3, 0);
        image.set(0, 2, 1);
        image.set(1, 1, 1);
        image.set(2, 2, 1);

        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);
        GrayImage result = cc.apply(image);

        assertEquals(image.X(), result.X());
        assertEquals(image.Y(), result.Y());

        assertEquals(1, result.get(0, 2));
        assertEquals(1, result.get(1, 1));
        assertEquals(1, result.get(2, 2));
    }

    @Test
    public void testApply_imageWithMultipleComponents() {
        BinaryImage image = new BinaryImage(4, 4, 0);
        image.set(0, 0, 1);
        image.set(0, 2, 1);
        image.set(2, 0, 1);
        image.set(3, 3, 1);

        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        GrayImage result = cc.apply(image);

        assertEquals(image.X(), result.X());
        assertEquals(image.Y(), result.Y());

        assertEquals(1, result.get(0, 0));
        assertEquals(1, result.get(0, 2));
        assertEquals(2, result.get(2, 0));
        assertEquals(3, result.get(3, 3));
    }

    @Test
    public void testApply_imageWithNoComponents() {
        BinaryImage image = new BinaryImage(3, 3, 0);

        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        GrayImage result = cc.apply(image);

        assertEquals(image.X(), result.X());
        assertEquals(image.Y(), result.Y());

        for (int x = 0; x < image.X(); x++) {
            for (int y = 0; y < image.Y(); y++) {
                assertEquals(0, result.get(x, y));
            }
        }
    }

    @Test
    public void testApply_imageWithNoComponents_ConnectedEight() {
        BinaryImage image = new BinaryImage(3, 3, 0);

        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);
        GrayImage result = cc.apply(image);

        assertEquals(image.X(), result.X());
        assertEquals(image.Y(), result.Y());

        for (int x = 0; x < image.X(); x++) {
            for (int y = 0; y < image.Y(); y++) {
                assertEquals(0, result.get(x, y));
            }
        }
    }
}