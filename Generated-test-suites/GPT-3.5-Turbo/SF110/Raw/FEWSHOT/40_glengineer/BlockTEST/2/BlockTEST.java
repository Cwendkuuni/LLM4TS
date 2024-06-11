package glengineer.blocks.test;

import static org.junit.Assert.*;
import org.junit.Test;
import glengineer.blocks.Block;
import glengineer.positions.*;

public class BlockTest {

    @Test
    public void testBlockConstructor() {
        Scheme scheme = new Scheme();
        Block block = new Block(scheme, 0, 0, 10, 10);
        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(10, block.x2);
        assertEquals(10, block.y2);
        assertEquals(scheme, block.scheme);
    }
    
    @Test
    public void testBlockConstructorWithCharPosition() {
        Scheme scheme = new Scheme();
        CharPosition p1 = new CharPosition(0, 0);
        CharPosition p2 = new CharPosition(10, 10);
        Block block = new Block(scheme, p1, p2);
        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(10, block.x2);
        assertEquals(10, block.y2);
        assertEquals(scheme, block.scheme);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructor_InvalidArguments() {
        Scheme scheme = new Scheme();
        Block block = new Block(scheme, -1, 0, 10, 10);
    }
    
    @Test
    public void testBlockConstructor_Default() {
        Scheme scheme = new Scheme();
        Block block = new Block(scheme);
        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(scheme.x2, block.x2);
        assertEquals(scheme.y2, block.y2);
        assertEquals(scheme, block.scheme);
    }
    
    @Test
    public void testSubblock() {
        Scheme scheme = new Scheme();
        Block block = new Block(scheme, 0, 0, 10, 10);
        Block subblock = block.subblock(2, 8, 4, 7);
        assertEquals(2, subblock.x1);
        assertEquals(4, subblock.y1);
        assertEquals(8, subblock.x2);
        assertEquals(7, subblock.y2);
        assertEquals(scheme, subblock.scheme);
    }
    
    @Test
    public void testCharAt() {
        Scheme scheme = new Scheme();
        scheme.lines[0] = "abcd";
        scheme.lines[1] = "efgh";
        Block block = new Block(scheme, 0, 0, 4, 2);
        assertEquals('a', block.charAt(0, 0));
        assertEquals('d', block.charAt(3, 0));
        assertEquals('e', block.charAt(0, 1));
        assertEquals('h', block.charAt(3, 1));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCharAt_InvalidPosition() {
        Scheme scheme = new Scheme();
        Block block = new Block(scheme, 0, 0, 2, 2);
        block.charAt(3, 0);
    }
    
    @Test
    public void testTextAt() {
        Scheme scheme = new Scheme();
        scheme.lines[0] = "abcd";
        scheme.lines[1] = "efgh";
        Block block = new Block(scheme, 0, 0, 4, 2);
        assertEquals("ab", block.textAt(0, 2, 0));
        assertEquals("cd", block.textAt(2, 4, 0));
        assertEquals("ef", block.textAt(0, 2, 1));
        assertEquals("gh", block.textAt(2, 4, 1));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testTextAt_InvalidCoordinates() {
        Scheme scheme = new Scheme();
        Block block = new Block(scheme, 0, 0, 2, 2);
        block.textAt(2, 3, 0);
    }
    
    @Test
    public void testContainsOnlyOneWord_True() {
        Scheme scheme = new Scheme();
        scheme.lines[0] = "abcd";
        scheme.lines[1] = "efgh";
        Block block = new Block(scheme, 0, 0, 4, 2);
        assertTrue(block.containsOnlyOneWord());
    }
    
    @Test
    public void testContainsOnlyOneWord_False() {
        Scheme scheme = new Scheme();
        scheme.lines[0] = "abcd";
        scheme.lines[1] = "efij";
        Block block = new Block(scheme, 0, 0, 4, 2);
        assertFalse(block.containsOnlyOneWord());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWord_InvalidBlock() {
        Scheme scheme = new Scheme();
        Block block = new Block(scheme);
        block.containsOnlyOneWord();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWord_EmptyBlock() {
        Scheme scheme = new Scheme();
        Block block = new Block(scheme, 0, 10, 0, 10);
        block.containsOnlyOneWord();
    }
    
    @Test
    public void testExtractSequentialSubblocks() {
        Scheme scheme = new Scheme();
        scheme.lines[0] = "abcd";
        scheme.lines[1] = "efgh";
        Block block = new Block(scheme, 0, 0, 4, 2);
        assertEquals(1, block.extractSequentialSubblocks().size());
    }
    
    @Test
    public void testExtractParallelSubblocks() {
        Scheme scheme = new Scheme();
        scheme.lines[0] = "abcd";
        scheme.lines[1] = "efgh";
        Block block = new Block(scheme, 0, 0, 4, 2);
        assertEquals(2, block.extractParallelSubblocks().size());
    }
    
    @Test
    public void testToString() {
        Scheme scheme = new Scheme();
        scheme.lines[0] = "abcd";
        scheme.lines[1] = "efgh";
        Block block = new Block(scheme, 0, 0, 4, 2);
        String expected = "Block(0,0, 4,2):\n" +
                          " ###############\n" +
                          " # abcd #\n" +
                          " # efgh #\n" +
                          " ###############";
        assertEquals(expected, block.toString());
    }
    
    @Test
    public void testGetUpperLine() {
        Scheme scheme = new Scheme();
        scheme.lines[0] = "abcd";
        scheme.lines[1] = "efgh";
        Block block = new Block(scheme, 0, 0, 4, 2);
        assertEquals("abcd", block.getUpperLine());
    }
}