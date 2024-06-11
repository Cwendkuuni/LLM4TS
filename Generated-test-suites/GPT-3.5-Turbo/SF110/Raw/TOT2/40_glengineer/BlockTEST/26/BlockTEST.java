package glengineer.blocks;

import static org.junit.Assert.*;
import org.junit.Test;

public class BlockTest {

    @Test
    public void testBlockWithCoordinates() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 1, 1, 2, 2);
        assertEquals('#', block.charAt(1, 1));
    }
    
    @Test
    public void testBlockWithPositions() {
        Scheme scheme = new Scheme(10, 10);
        CharPosition p1 = new CharPosition(1, 1);
        CharPosition p2 = new CharPosition(2, 2);
        Block block = new ConcreteBlock(scheme, p1, p2);
        assertEquals(" #", block.textAt(1, 2, 1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWordTrivialBlock() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme);
        block.containsOnlyOneWord();
    }

    @Test
    public void testSubblock() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 1, 1, 2, 2);
        Block subblock = block.subblock(1, 1, 2, 2);
        assertEquals(1, subblock.x1);
    }

    @Test
    public void testExtractSequentialSubblocks() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme);
        List<Block> subblocks = block.extractSequentialSubblocks();
        assertEquals(1, subblocks.size());
    }

    @Test
    public void testCharAtOutOfBoundsException() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 1, 1, 2, 2);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            block.charAt(12, 12);
        });
        assertTrue(exception.getMessage().contains("out of block."));
    }

    @Test
    public void testToString() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 1, 1, 2, 2);
        assertTrue(block.toString().startsWith("Block(1,1,2,2):\n"));
    }

    // Additional test cases can be added for other methods.

}