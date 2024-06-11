package glengineer.blocks;

import glengineer.positions.*;
import java.util.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class BlockTest {

    @Test
    public void testSubblock() {
        Scheme scheme = new Scheme();
        Block block = new Block(scheme, 0, 0, 5, 5);
        Block subblock = block.subblock(1, 1, 3, 3);
        assertEquals(subblock, new Block(scheme, 1, 1, 3, 3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubblockWithInvalidCoordinates() {
        Scheme scheme = new Scheme();
        Block block = new Block(scheme, 0, 0, 5, 5);
        block.subblock(6, 6, 8, 8);
    }

    @Test
    public void testCharAt() {
        Scheme scheme = new Scheme();
        scheme.lines[0] = "abcdefghij";
        Block block = new Block(scheme, 0, 0, 10, 1);
        assertEquals('a', block.charAt(0, 0));
        assertEquals('b', block.charAt(1, 0));
        assertEquals('c', block.charAt(2, 0));
        assertEquals('d', block.charAt(3, 0));
        assertEquals('e', block.charAt(4, 0));
        assertEquals('f', block.charAt(5, 0));
        assertEquals('g', block.charAt(6, 0));
        assertEquals('h', block.charAt(7, 0));
        assertEquals('i', block.charAt(8, 0));
        assertEquals('j', block.charAt(9, 0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCharAtWithInvalidPosition() {
        Scheme scheme = new Scheme();
        Block block = new Block(scheme, 0, 0, 0, 0);
        block.charAt(1, 1);
    }

    @Test
    public void testTextAt() {
        Scheme scheme = new Scheme();
        scheme.lines[0] = "abcdefghij";
        Block block = new Block(scheme, 0, 0, 10, 1);
        assertEquals("abcdefghij", block.textAt(0, 10, 0));
        assertEquals("abcd", block.textAt(0, 4, 0));
        assertEquals("efgh", block.textAt(4, 8, 0));
        assertEquals("ghij", block.textAt(6, 10, 0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTextAtWithInvalidCoordinates() {
        Scheme scheme = new Scheme();
        Block block = new Block(scheme, 0, 0, 0, 0);
        block.textAt(1, 2, 0);
    }

    @Test
    public void testContainsOnlyOneWord() {
        Scheme scheme = new Scheme();
        Block block = new Block(scheme, 0, 0, 5, 5);
        assertFalse(block.containsOnlyOneWord());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWordWithTrivialBlock() {
        Scheme scheme = new Scheme();
        Block block = new Block(scheme);
        block.containsOnlyOneWord();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWordWithEmptyBlock() {
        Scheme scheme = new Scheme();
        Block block = new Block(scheme, 0, 0, 5, 0);
        block.containsOnlyOneWord();
    }

    @Test
    public void testExtractSequentialSubblocks() {
        Scheme scheme = new Scheme();
        Block block = new Block(scheme, 0, 0, 5, 5);
        List<Block> subblocks = block.extractSequentialSubblocks();
        assertTrue(subblocks.isEmpty());
    }

    @Test
    public void testExtractParallelSubblocks() {
        Scheme scheme = new Scheme();
        Block block = new Block(scheme, 0, 0, 5, 5);
        List<Block> subblocks = block.extractParallelSubblocks();
        assertTrue(subblocks.isEmpty());
    }

    @Test
    public void testToString() {
        Scheme scheme = new Scheme();
        scheme.lines[0] = "abcdefghij";
        Block block = new Block(scheme, 0, 0, 10, 1);
        String expectedResult = "Block(0,0, 10,1):\n";
        expectedResult += " ####################\n";
        expectedResult += " # abcdefghij #\n";
        expectedResult += " ####################";
        assertEquals(expectedResult, block.toString());
    }

    @Test
    public void testGetUpperLine() {
        Scheme scheme = new Scheme();
        scheme.lines[0] = "abcdefghij";
        Block block = new Block(scheme, 0, 0, 10, 1);
        assertEquals("abcdefghij", block.getUpperLine());
    }
}