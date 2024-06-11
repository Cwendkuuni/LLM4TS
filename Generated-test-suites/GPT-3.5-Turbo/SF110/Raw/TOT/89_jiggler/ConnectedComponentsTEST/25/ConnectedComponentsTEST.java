import static org.junit.Assert.*;
import org.junit.Test;

public class ConnectedComponentsTest {

    // Test case for constructor with valid values
    @Test
    public void testConstructorValidValues() {
        try {
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
            assertNotNull(cc);
        } catch (InvalidArgumentException e) {
            fail("Exception should not be thrown");
        }
    }

    // Test case for constructor with invalid number of neighbors
    @Test
    public void testConstructorInvalidNumberNeighbors() {
        try {
            ConnectedComponents cc = new ConnectedComponents(2, 0);
            fail("InvalidArgumentException should be thrown");
        } catch (InvalidArgumentException e) {
            assertEquals("Invalid argument", e.getMessage());
        }
    }

    // Test case for constructor with invalid background color
    @Test
    public void testConstructorInvalidBackgroundColor() {
        try {
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 2);
            fail("InvalidArgumentException should be thrown");
        } catch (InvalidArgumentException e) {
            assertEquals("Invalid argument", e.getMessage());
        }
    }

    // Test case for apply method with background color as 0
    @Test
    public void testApplyBackgroundColorZero() {
        try {
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
            BinaryImage image = new BinaryImage(2, 2);
            image.set(0, 0, 1);
            image.set(0, 1, 0);
            image.set(1, 0, 0);
            image.set(1, 1, 1);
            
            GrayImage result = cc.apply(image);
            
            assertEquals(0, result.get(0, 0).intValue());
            assertEquals(1, result.get(0, 1).intValue());
            assertEquals(1, result.get(1, 0).intValue());
            assertEquals(0, result.get(1, 1).intValue());
        } catch (InvalidArgumentException e) {
            fail("Exception should not be thrown");
        }
    }

    // Test case for apply method with background color as 1
    @Test
    public void testApplyBackgroundColorOne() {
        try {
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 1);
            BinaryImage image = new BinaryImage(2, 2);
            image.set(0, 0, 1);
            image.set(0, 1, 0);
            image.set(1, 0, 0);
            image.set(1, 1, 1);
            
            GrayImage result = cc.apply(image);
            
            assertEquals(1, result.get(0, 0).intValue());
            assertEquals(0, result.get(0, 1).intValue());
            assertEquals(0, result.get(1, 0).intValue());
            assertEquals(1, result.get(1, 1).intValue());
        } catch (InvalidArgumentException e) {
            fail("Exception should not be thrown");
        }
    }

    // Test case for apply method with invalid image
    @Test
    public void testApplyInvalidImage() {
        try {
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
            GrayImage image = new GrayImage(2, 2);

            cc.apply(image);

            fail("Exception should be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid image", e.getMessage());
        }
    }

}