package glengineer.blocks;

import static org.junit.Assert.*;
import org.junit.Test;

public class BlockTest {

    @Test
    public void testBlockWithCoordinates() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new ConcreteBlock(scheme, 1, 1, 3, 3);
        assertEquals(1, block.x1);
        assertEquals(1, block.y1);
        assertEquals(3, block.x2);
        assertEquals(3, block.y2);
    }
    
    // Additional test cases for other methods will be added here
    
}