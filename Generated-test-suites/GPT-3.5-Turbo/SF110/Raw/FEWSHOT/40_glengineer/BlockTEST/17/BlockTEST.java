package glengineer.blocks;

import glengineer.positions.*;
import java.util.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class BlockTest {

    @Test
    public void testSubblock() {
        Scheme scheme = new Scheme();
        Block block = new ConcreteBlock(scheme, 0, 0, 2, 2);
        Block subblock = block.subblock(0, 0, 1, 1);
        assertEquals("Subblock x1", 0, subblock.x1);
        assertEquals("Subblock y1", 0, subblock.y1);
        assertEquals("Subblock x2", 1, subblock.x2);
        assertEquals("Subblock y2", 1, subblock.y2);
    }

    @Test
    public void testCharAt() {
        Scheme scheme = new Scheme();
        Block block = new ConcreteBlock(scheme, 0, 0, 2, 2);
        char result = block.charAt(1, 1);
        assertEquals("Char at position (1,1)", ' ', result);
    }

    @Test
    public void testTextAt() {
        Scheme scheme = new Scheme();
        Block block = new ConcreteBlock(scheme, 0, 0, 2, 2);
        String result = block.textAt(0, 1, 1);
        assertEquals("Text at coordinates (0,0) to (1,1)", "", result);
    }

    @Test
    public void testContainsOnlyOneWord() {
        Scheme scheme = new Scheme();
        Block block = new ConcreteBlock(scheme, 0, 0, 2, 2);
        boolean result = block.containsOnlyOneWord();
        assertFalse("Contains only one word", result);
    }

    @Test
    public void testExtractSequentialSubblocks() {
        Scheme scheme = new Scheme();
        Block block = new ConcreteBlock(scheme, 0, 0, 2, 2);
        List<Block> subblocks = block.extractSequentialSubblocks();
        assertTrue("Number of subblocks", subblocks.size() > 0);
    }

    @Test
    public void testExtractParallelSubblocks() {
        Scheme scheme = new Scheme();
        Block block = new ConcreteBlock(scheme, 0, 0, 2, 2);
        List<Block> subblocks = block.extractParallelSubblocks();
        assertTrue("Number of subblocks", subblocks.size() > 0);
    }

    @Test
    public void testToString() {
        Scheme scheme = new Scheme();
        Block block = new ConcreteBlock(scheme, 0, 0, 2, 2);
        String result = block.toString();
        assertNotNull("Block string representation", result);
    }

    @Test
    public void testGetUpperLine() {
        Scheme scheme = new Scheme();
        Block block = new ConcreteBlock(scheme, 0, 0, 2, 2);
        String result = block.getUpperLine();
        assertNotNull("Upper line of block", result);
    }
}