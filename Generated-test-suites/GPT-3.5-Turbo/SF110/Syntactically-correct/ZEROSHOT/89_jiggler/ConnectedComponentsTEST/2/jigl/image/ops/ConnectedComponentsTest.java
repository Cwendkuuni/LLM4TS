import junit.framework.TestCase;
import jigl.image.BinaryImage;
import jigl.image.GrayImage;
import jigl.image.exceptions.InvalidArgumentException;

public class ConnectedComponentsTest extends TestCase {

    public void testApply() {
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        
        // Test case 1: empty image
        BinaryImage emptyImage = new BinaryImage(0, 0);
        GrayImage result1 = cc.apply(emptyImage);
        assertTrue(result1 instanceof GrayImage);
        assertEquals(result1.X(), 0);
        assertEquals(result1.Y(), 0);
        
        // Test case 2: background color equals connected component color
        BinaryImage image2 = new BinaryImage(new boolean[][] {
            {true, true, true},
            {true, true, true},
            {true, true, true}
        });
        GrayImage result2 = cc.apply(image2);
        assertTrue(result2 instanceof GrayImage);
        assertEquals(result2.X(), 3);
        assertEquals(result2.Y(), 3);
        assertTrue(result2.get(0, 0) == 0);
        assertTrue(result2.get(1, 0) == 0);
        assertTrue(result2.get(2, 0) == 0);
        assertTrue(result2.get(0, 1) == 0);
        assertTrue(result2.get(1, 1) == 0);
        assertTrue(result2.get(2, 1) == 0);
        assertTrue(result2.get(0, 2) == 0);
        assertTrue(result2.get(1, 2) == 0);
        assertTrue(result2.get(2, 2) == 0);
        
        // Test case 3: background color equals 0, connected component color equals 1
        BinaryImage image3 = new BinaryImage(new boolean[][] {
            {false, false, true},
            {false, false, true},
            {true, true, true}
        });
        GrayImage result3 = cc.apply(image3);
        assertTrue(result3 instanceof GrayImage);
        assertEquals(result3.X(), 3);
        assertEquals(result3.Y(), 3);
        assertTrue(result3.get(0, 0) == 0);
        assertTrue(result3.get(1, 0) == 0);
        assertTrue(result3.get(2, 0) == 1);
        assertTrue(result3.get(0, 1) == 0);
        assertTrue(result3.get(1, 1) == 0);
        assertTrue(result3.get(2, 1) == 1);
        assertTrue(result3.get(0, 2) == 1);
        assertTrue(result3.get(1, 2) == 1);
        assertTrue(result3.get(2, 2) == 1);
        
        // Test case 4: background color equals 0, connected component color equals 1, four-connected
        ConnectedComponents cc4 = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        BinaryImage image4 = new BinaryImage(new boolean[][] {
            {false, false, true},
            {false, false, true},
            {true, true, true}
        });
        GrayImage result4 = cc4.apply(image4);
        assertTrue(result4 instanceof GrayImage);
        assertEquals(result4.X(), 3);
        assertEquals(result4.Y(), 3);
        assertTrue(result4.get(0, 0) == 0);
        assertTrue(result4.get(1, 0) == 0);
        assertTrue(result4.get(2, 0) == 1);
        assertTrue(result4.get(0, 1) == 0);
        assertTrue(result4.get(1, 1) == 0);
        assertTrue(result4.get(2, 1) == 1);
        assertTrue(result4.get(0, 2) == 1);
        assertTrue(result4.get(1, 2) == 1);
        assertTrue(result4.get(2, 2) == 1);
        
        // Test case 5: background color equals 0, connected component color equals 1, eight-connected
        ConnectedComponents cc8 = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);
        BinaryImage image5 = new BinaryImage(new boolean[][] {
            {false, false, true},
            {false, false, true},
            {true, true, true}
        });
        GrayImage result5 = cc8.apply(image5);
        assertTrue(result5 instanceof GrayImage);
        assertEquals(result5.X(), 3);
        assertEquals(result5.Y(), 3);
        assertTrue(result5.get(0, 0) == 0);
        assertTrue(result5.get(1, 0) == 0);
        assertTrue(result5.get(2, 0) == 1);
        assertTrue(result5.get(0, 1) == 0);
        assertTrue(result5.get(1, 1) == 0);
        assertTrue(result5.get(2, 1) == 1);
        assertTrue(result5.get(0, 2) == 1);
        assertTrue(result5.get(1, 2) == 1);
        assertTrue(result5.get(2, 2) == 1);
    }
    
    public void testConnectedComponentsConstructor() {
        // Test case 6: valid input
        try {
            ConnectedComponents cc6 = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
            assertTrue(cc6 instanceof ConnectedComponents);
            assertEquals(cc6.neighbors, 0);
            assertEquals(cc6.background, 0);
        } catch (InvalidArgumentException e) {
            fail("Unexpected InvalidArgumentException");
        }
        
        // Test case 7: invalid input
        try {
            ConnectedComponents cc7 = new ConnectedComponents(2, 0);
            fail("Expected InvalidArgumentException");
        } catch (InvalidArgumentException e) {
            // expected exception
        }
    }

}