import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import jigl.image.types.*;
import jigl.image.*;
import jigl.image.exceptions.*;

public class ConnectedComponentsTest {
    ConnectedComponents connectedComponents;
    
    @Before
    public void setUp() {
        try {
            connectedComponents = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        } catch (InvalidArgumentException e) {
            fail("Failed to create ConnectedComponents object");
        }
    }
    
    @Test
    public void testApply() {
        BinaryImage image = new BinaryImage(10, 10);
        // Set the values of the image
        for (int x = 0; x < image.X(); x++) {
            for (int y = 0; y < image.Y(); y++) {
                if (x < 5 && y < 5) {
                    image.set(x, y, 1);
                } else {
                    image.set(x, y, 0);
                }
            }
        }
        
        GrayImage expected = new GrayImage(image.X(), image.Y());
        // Set the expected values of the gray image
        for (int x = 0; x < expected.X(); x++) {
            for (int y = 0; y < expected.Y(); y++) {
                if (x < 5 && y < 5) {
                    expected.set(x, y, 1);
                } else {
                    expected.set(x, y, 0);
                }
            }
        }
        
        GrayImage result = connectedComponents.apply(image);
        
        // Compare the result with the expected values
        for (int x = 0; x < result.X(); x++) {
            for (int y = 0; y < result.Y(); y++) {
                assertEquals(expected.get(x, y), result.get(x, y));
            }
        }
    }
}