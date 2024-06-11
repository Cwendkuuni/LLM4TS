package glengineer.blocks;

import org.junit.Test;
import static org.junit.Assert.*;

public class BlockTest {

    @Test
    public void testConstructor() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new BlockImpl(scheme, 0, 0, 5, 5);
        
        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(5, block.x2);
        assertEquals(5, block.y2);
        assertEquals(scheme, block.scheme);
    }

    @Test
    public void testSubblock() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new BlockImpl(scheme, 0, 0, 5, 5);
        Block subblock = block.subblock(1, 1, 3, 3);
        
        assertEquals(1, subblock.x1);
        assertEquals(1, subblock.y1);
        assertEquals(3, subblock.x2);
        assertEquals(3, subblock.y2);
    }

    // Add more test cases for other public methods
}