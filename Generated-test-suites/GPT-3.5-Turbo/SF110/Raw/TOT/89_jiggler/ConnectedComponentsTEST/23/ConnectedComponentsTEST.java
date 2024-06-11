import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import jigl.image.types.*;
import jigl.image.*;
import jigl.image.exceptions.*;

public class ConnectedComponentsTest {

    ConnectedComponents cc;

    @Before
    public void setUp() throws Exception {
        cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
    }

    @Test
    public void testConstructor_InvalidArguments() {
        try {
            cc = new ConnectedComponents(2, 1);
            fail("InvalidArgumentException should have been thrown");
        } catch (InvalidArgumentException e) {
            // Exception is expected
        }
    }

    @Test
    public void testConstructor_ValidArguments() {
        try {
            cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        } catch (InvalidArgumentException e) {
            fail("InvalidArgumentException should not have been thrown");
        }
    }

    @Test
    public void testApply_EmptyBinaryImage() {
        // Create an empty binary image
        BinaryImage image = new BinaryImage(10, 10);
        GrayImage result = cc.apply(image);
        assertEquals(10, result.X());
        assertEquals(10, result.Y());
        for (int x = 0; x < result.X(); x++) {
            for (int y = 0; y < result.Y(); y++) {
                assertEquals(0, result.get(x, y));
            }
        }
    }

    @Test
    public void testApply_AllBackgroundBinaryImage() {
        // Create a binary image with all background pixels
        BinaryImage image = new BinaryImage(10, 10);
        for (int x = 0; x < image.X(); x++) {
            for (int y = 0; y < image.Y(); y++) {
                image.set(x, y, 0);
            }
        }
        GrayImage result = cc.apply(image);
        assertEquals(10, result.X());
        assertEquals(10, result.Y());
        for (int x = 0; x < result.X(); x++) {
            for (int y = 0; y < result.Y(); y++) {
                assertEquals(0, result.get(x, y));
            }
        }
    }

    @Test
    public void testApply_AllForegroundBinaryImage() {
        // Create a binary image with all foreground pixels
        BinaryImage image = new BinaryImage(10, 10);
        for (int x = 0; x < image.X(); x++) {
            for (int y = 0; y < image.Y(); y++) {
                image.set(x, y, 1);
            }
        }
        GrayImage result = cc.apply(image);
        assertEquals(10, result.X());
        assertEquals(10, result.Y());
        int current = 1;
        for (int x = 0; x < result.X(); x++) {
            for (int y = 0; y < result.Y(); y++) {
                assertEquals(current, result.get(x, y));
            }
        }
    }

    @Test
    public void testApply_FourConnectedCase() {
        // Create a binary image with a single connected component in the middle
        BinaryImage image = new BinaryImage(5, 5);
        for (int x = 0; x < image.X(); x++) {
            for (int y = 0; y < image.Y(); y++) {
                image.set(x, y, 0);
            }
        }
        image.set(1, 1, 1);
        image.set(2, 1, 1);
        image.set(3, 1, 1);
        image.set(1, 2, 1);
        image.set(2, 2, 1);
        image.set(3, 2, 1);
        image.set(1, 3, 1);
        image.set(2, 3, 1);
        image.set(3, 3, 1);
        GrayImage result = cc.apply(image);
        assertEquals(5, result.X());
        assertEquals(5, result.Y());
        for (int x = 0; x < result.X(); x++) {
            for (int y = 0; y < result.Y(); y++) {
                if (x == 1 && y == 1) {
                    assertEquals(1, result.get(x, y));
                } else if (x == 2 && y == 1) {
                    assertEquals(1, result.get(x, y));
                } else if (x == 3 && y == 1) {
                    assertEquals(1, result.get(x, y));
                } else if (x == 1 && y == 2) {
                    assertEquals(1, result.get(x, y));
                } else if (x == 2 && y == 2) {
                    assertEquals(1, result.get(x, y));
                } else if (x == 3 && y == 2) {
                    assertEquals(1, result.get(x, y));
                } else if (x == 1 && y == 3) {
                    assertEquals(1, result.get(x, y));
                } else if (x == 2 && y == 3) {
                    assertEquals(1, result.get(x, y));
                } else if (x == 3 && y == 3) {
                    assertEquals(1, result.get(x, y));
                } else {
                    assertEquals(0, result.get(x, y));
                }
            }
        }
    }

    @Test
    public void testApply_EightConnectedCase() {
        // Create a binary image with a single connected component in the middle
        BinaryImage image = new BinaryImage(5, 5);
        for (int x = 0; x < image.X(); x++) {
            for (int y = 0; y < image.Y(); y++) {
                image.set(x, y, 0);
            }
        }
        image.set(1, 1, 1);
        image.set(2, 1, 1);
        image.set(3, 1, 1);
        image.set(1, 2, 1);
        image.set(2, 2, 1);
        image.set(3, 2, 1);
        image.set(1, 3, 1);
        image.set(2, 3, 1);
        image.set(3, 3, 1);
        cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);
        GrayImage result = cc.apply(image);
        assertEquals(5, result.X());
        assertEquals(5, result.Y());
        for (int x = 0; x < result.X(); x++) {
            for (int y = 0; y < result.Y(); y++) {
                if (x == 1 && y == 1) {
                    assertEquals(1, result.get(x, y));
                } else if (x == 2 && y == 1) {
                    assertEquals(1, result.get(x, y));
                } else if (x == 3 && y == 1) {
                    assertEquals(1, result.get(x, y));
                } else if (x == 1 && y == 2) {
                    assertEquals(1, result.get(x, y));
                } else if (x == 2 && y == 2) {
                    assertEquals(1, result.get(x, y));
                } else if (x == 3 && y == 2) {
                    assertEquals(1, result.get(x, y));
                } else if (x == 1 && y == 3) {
                    assertEquals(1, result.get(x, y));
                } else if (x == 2 && y == 3) {
                    assertEquals(1, result.get(x, y));
                } else if (x == 3 && y == 3) {
                    assertEquals(1, result.get(x, y));
                } else {
                    assertEquals(0, result.get(x, y));
                }
            }
        }
    }

}