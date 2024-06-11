
package glengineer.blocks;

import glengineer.positions.*;
import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;

public class BlockTest {
    
    @Test
    public void testConstructorWithSchemeAndCoordinates() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new BlockImpl(scheme, 1, 1, 5, 5);
        assertEquals(1, block.x1);
        assertEquals(1, block.y1);
        assertEquals(5, block.x2);
        assertEquals(5, block.y2);
        assertEquals(scheme, block.scheme);
    }
    
    @Test
    public void testConstructorWithSchemeAndPositions() {
        Scheme scheme = new Scheme(10, 10);
        CharPosition p1 = new CharPosition(1, 1);
        CharPosition p2 = new CharPosition(5, 5);
        Block block = new BlockImpl(scheme, p1, p2);
        assertEquals(1, block.x1);
        assertEquals(1, block.y1);
        assertEquals(5, block.x2);
        assertEquals(5, block.y2);
        assertEquals(scheme, block.scheme);
    }
    
    @Test
    public void testConstructorWithScheme() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new BlockImpl(scheme);
        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(scheme.x2, block.x2);
        assertEquals(scheme.y2, block.y2);
        assertEquals(scheme, block.scheme);
    }
    
    @Test
    public void testSubblock() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new BlockImpl(scheme, 1, 1, 5, 5);
        Block subblock = block.subblock(2, 2, 4, 4);
        assertEquals(2, subblock.x1);
        assertEquals(2, subblock.y1);
        assertEquals(4, subblock.x2);
        assertEquals(4, subblock.y2);
        assertEquals(scheme, subblock.scheme);
    }
    
    @Test
    public void testCharAt() {
        Scheme scheme = new Scheme(10, 10);
        scheme.lines[1] = "abcdefghij";
        Block block = new BlockImpl(scheme, 1, 1, 5, 5);
        assertEquals('e', block.charAt(3, 3));
    }
    
    @Test
    public void testTextAt() {
        Scheme scheme = new Scheme(10, 10);
        scheme.lines[1] = "abcdefghij";
        Block block = new BlockImpl(scheme, 1, 1, 5, 5);
        assertEquals("cde", block.textAt(2, 5, 2));
    }
    
    @Test
    public void testContainsOnlyOneWord() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new BlockImpl(scheme, 1, 1, 5, 5);
        assertFalse(block.containsOnlyOneWord());
    }
    
    @Test
    public void testExtractSequentialSubblocks() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new BlockImpl(scheme, 1, 1, 5, 5);
        List<Block> subblocks = block.extractSequentialSubblocks();
        assertEquals(0, subblocks.size());
    }
    
    @Test
    public void testExtractParallelSubblocks() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new BlockImpl(scheme, 1, 1, 5, 5);
        List<Block> subblocks = block.extractParallelSubblocks();
        assertEquals(0, subblocks.size());
    }
    
    @Test
    public void testToString() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new BlockImpl(scheme, 1, 1, 5, 5);
        String expected = "Block(1,1, 5,5):\n" +
                          " #################\n" +
                          " #               #\n" +
                          " #               #\n" +
                          " #               #\n" +
                          " #               #\n" +
                          " #################";
        assertEquals(expected, block.toString());
    }
    
    @Test
    public void testGetUpperLine() {
        Scheme scheme = new Scheme(10, 10);
        scheme.lines[1] = "abcdefghij";
        Block block = new BlockImpl(scheme, 1, 1, 5, 5);
        assertEquals("abcde", block.getUpperLine());
    }
}
