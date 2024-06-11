package glengineer.blocks;

import glengineer.positions.*;
import java.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class BlockTest {

    @Test
    public void testSubblock() {
        Scheme scheme = new Scheme();
        Block block = new BlockImpl(scheme, 0, 0, 2, 2);
        Block subblock = block.subblock(1, 1, 2, 2);
        assertEquals(1, subblock.x1);
        assertEquals(1, subblock.y1);
        assertEquals(2, subblock.x2);
        assertEquals(2, subblock.y2);
    }

    @Test
    public void testCharAt() {
        Scheme scheme = new Scheme();
        scheme.lines = new String[] {"abc", "def", "ghi"};
        Block block = new BlockImpl(scheme, 0, 0, 2, 2);
        assertEquals('a', block.charAt(0, 0));
        assertEquals('d', block.charAt(0, 1));
        assertEquals('g', block.charAt(0, 2));
    }

    @Test
    public void testTextAt() {
        Scheme scheme = new Scheme();
        scheme.lines = new String[] {"abc", "def", "ghi"};
        Block block = new BlockImpl(scheme, 0, 0, 2, 2);
        assertEquals("ab", block.textAt(0, 2, 0));
        assertEquals("de", block.textAt(0, 2, 1));
        assertEquals("gh", block.textAt(0, 2, 2));
    }

    @Test
    public void testContainsOnlyOneWord() {
        Scheme scheme = new Scheme();
        Block block1 = new BlockImpl(scheme, 0, 0, 0, 0);
        assertFalse(block1.containsOnlyOneWord());

        Block block2 = new BlockImpl(scheme, 0, 0, 1, 1);
        assertTrue(block2.containsOnlyOneWord());

        Block block3 = new BlockImpl(scheme, 0, 0, 2, 2);
        assertFalse(block3.containsOnlyOneWord());
    }

    @Test
    public void testExtractSequentialSubblocks() {
        Scheme scheme = new Scheme();
        Block block = new BlockImpl(scheme, 0, 0, 2, 2);
        List<Block> subblocks = block.extractSequentialSubblocks();
        assertEquals(4, subblocks.size());
        for (Block subblock : subblocks) {
            assertTrue(subblock instanceof BlockImpl);
        }
    }

    @Test
    public void testExtractParallelSubblocks() {
        Scheme scheme = new Scheme();
        Block block = new BlockImpl(scheme, 0, 0, 2, 2);
        List<Block> subblocks = block.extractParallelSubblocks();
        assertEquals(4, subblocks.size());
        for (Block subblock : subblocks) {
            assertTrue(subblock instanceof BlockImpl);
        }
    }

    @Test
    public void testToString() {
        Scheme scheme = new Scheme();
        Block block = new BlockImpl(scheme, 0, 0, 2, 2);
        String expected = "Block(0,0, 2,2):\n"
                        + " ####\n"
                        + " # abc #\n"
                        + " # def #\n"
                        + " # ghi #\n"
                        + " ####";
        assertEquals(expected, block.toString());
    }

    @Test
    public void testGetUpperLine() {
        Scheme scheme = new Scheme();
        scheme.lines = new String[] {"abc", "def", "ghi"};
        Block block = new BlockImpl(scheme, 0, 0, 2, 2);
        assertEquals("abc", block.getUpperLine());
    }
}