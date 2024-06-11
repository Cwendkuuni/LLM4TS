package jigl.image.ops;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ConnectedComponentsTest {
    @Test
    public void testConstructor() {
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 1);
        assertNotNull(cc);
        assertEquals(ConnectedComponents.FOUR_CONNECTED, cc.neighbors);
        assertEquals(1, cc.background);
        
        cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);
        assertNotNull(cc);
        assertEquals(ConnectedComponents.EIGHT_CONNECTED, cc.neighbors);
        assertEquals(0, cc.background);
        
        // Invalid arguments
        try {
            cc = new ConnectedComponents(2, 1);
        } catch (InvalidArgumentException e) {
            assertNotNull(e);
        }
        
        try {
            cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 2);
        } catch (InvalidArgumentException e) {
            assertNotNull(e);
        }
    }
    
    @Test
    public void testApply() {
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 1);
        BinaryImage image = new BinaryImage(3, 3);
        
        // Case 1: No connected components
        GrayImage result = cc.apply(image);
        assertEquals(3, result.X());
        assertEquals(3, result.Y());
        for (int x = 0; x < result.X(); x++) {
            for (int y = 0; y < result.Y(); y++) {
                assertEquals(0, result.get(x, y).intValue());
            }
        }
        
        // Case 2: One connected component
        image.set(1, 1, 1);
        result = cc.apply(image);
        assertEquals(3, result.X());
        assertEquals(3, result.Y());
        for (int x = 0; x < result.X(); x++) {
            for (int y = 0; y < result.Y(); y++) {
                if (x == 1 && y == 1) {
                    assertNotEquals(0, result.get(x, y).intValue());
                } else {
                    assertEquals(0, result.get(x, y).intValue());
                }
            }
        }
        
        // Case 3: Multiple connected components
        image = new BinaryImage(4, 4);
        image.set(0, 0, 1);
        image.set(1, 1, 1);
        image.set(3, 1, 1);
        result = cc.apply(image);
        assertEquals(4, result.X());
        assertEquals(4, result.Y());
        for (int x = 0; x < result.X(); x++) {
            for (int y = 0; y < result.Y(); y++) {
                if ((x == 0 && y == 0) || (x == 1 && y == 1) || (x == 3 && y == 1)) {
                    assertNotEquals(0, result.get(x, y).intValue());
                } else {
                    assertEquals(0, result.get(x, y).intValue());
                }
            }
        }
    }
}