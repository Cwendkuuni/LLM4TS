package glengineer.blocks;

import glengineer.positions.*;
import java.util.*;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BlockTest {

    @Test
    public void testBlockConstructorWithValidCoordinates() {
        Scheme scheme = new Scheme();
        Block block = new BlockImpl(scheme, 0, 0, 2, 2);
        
        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(2, block.x2);
        assertEquals(2, block.y2);
        assertEquals(scheme, block.scheme);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructorWithInvalidCoordinates() {
        Scheme scheme = new Scheme();
        Block block = new BlockImpl(scheme, -1, 0, 2, 2);
    }
    
    @Test
    public void testBlockConstructorWithCharPositions() {
        Scheme scheme = new Scheme();
        CharPosition p1 = new CharPosition(0, 0);
        CharPosition p2 = new CharPosition(2, 2);
        Block block = new BlockImpl(scheme, p1, p2);
        
        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(2, block.x2);
        assertEquals(2, block.y2);
        assertEquals(scheme, block.scheme);
    }
    
    @Test
    public void testBlockConstructorWithScheme() {
        Scheme scheme = new Scheme();
        Block block = new BlockImpl(scheme);
        
        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(scheme.x2, block.x2);
        assertEquals(scheme.y2, block.y2);
        assertEquals(scheme, block.scheme);
    }
    
    @Test
    public void testSubblock() {
        Scheme scheme = new Scheme();
        Block block = new BlockImpl(scheme, 0, 0, 4, 4);
        Block subblock = block.subblock(1, 1, 2, 2);
        
        assertEquals(1, subblock.x1);
        assertEquals(1, subblock.y1);
        assertEquals(2, subblock.x2);
        assertEquals(2, subblock.y2);
    }
    
    @Test
    public void testCharAt() {
        Scheme scheme = new Scheme();
        scheme.lines[0] = "abcdefg";
        Block block = new BlockImpl(scheme, 0, 0, 7, 1);
        
        assertEquals('a', block.charAt(0, 0));
        assertEquals('b', block.charAt(1, 0));
        assertEquals('c', block.charAt(2, 0));
        assertEquals('d', block.charAt(3, 0));
        assertEquals('e', block.charAt(4, 0));
        assertEquals('f', block.charAt(5, 0));
        assertEquals('g', block.charAt(6, 0));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCharAtOutOfBounds() {
        Scheme scheme = new Scheme();
        Block block = new BlockImpl(scheme, 0, 0, 0, 0);
        block.charAt(1, 1);
    }
    
    @Test
    public void testTextAt() {
        Scheme scheme = new Scheme();
        scheme.lines[0] = "abcdefg";
        Block block = new BlockImpl(scheme, 0, 0, 7, 1);
        
        assertEquals("abc", block.textAt(0, 3, 0));
        assertEquals("cde", block.textAt(2, 5, 0));
        assertEquals("abcdefg", block.textAt(0, 7, 0));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testTextAtInvalidCoordinates() {
        Scheme scheme = new Scheme();
        Block block = new BlockImpl(scheme, 0, 0, 0, 0);
        block.textAt(1, 2, 1);
    }
    
    @Test
    public void testContainsOnlyOneWord() {
        Scheme scheme = new Scheme();
        scheme.lines[0] = "hello";
        Block block = new BlockImpl(scheme, 0, 0, 5, 1);
        
        assertTrue(block.containsOnlyOneWord());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWordInTrivialBlock() {
        Scheme scheme = new Scheme();
        Block block = new BlockImpl(scheme, 0, 0, 0, 0);
        block.containsOnlyOneWord();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWordInEmptyBlock() {
        Scheme scheme = new Scheme();
        Block block = new BlockImpl(scheme, 0, 0, 7, 1);
        block.containsOnlyOneWord();
    }
    
    @Test
    public void testExtractSequentialSubblocks() {
        Scheme scheme = new Scheme();
        scheme.lines[0] = "aaa";
        scheme.lines[1] = "bbb";
        scheme.lines[2] = "ccc";
        Block block = new BlockImpl(scheme, 0, 0, 3, 3);
        
        List<Block> subblocks = block.extractSequentialSubblocks();
        
        assertEquals(9, subblocks.size());
        assertTrue(subblocks.contains(new BlockImpl(scheme, 0, 0, 1, 1)));
        assertTrue(subblocks.contains(new BlockImpl(scheme, 1, 0, 2, 1)));
        assertTrue(subblocks.contains(new BlockImpl(scheme, 2, 0, 3, 1)));
        assertTrue(subblocks.contains(new BlockImpl(scheme, 0, 1, 1, 2)));
        assertTrue(subblocks.contains(new BlockImpl(scheme, 1, 1, 2, 2)));
        assertTrue(subblocks.contains(new BlockImpl(scheme, 2, 1, 3, 2)));
        assertTrue(subblocks.contains(new BlockImpl(scheme, 0, 2, 1, 3)));
        assertTrue(subblocks.contains(new BlockImpl(scheme, 1, 2, 2, 3)));
        assertTrue(subblocks.contains(new BlockImpl(scheme, 2, 2, 3, 3)));
    }
    
    @Test
    public void testExtractParallelSubblocks() {
        Scheme scheme = new Scheme();
        scheme.lines[0] = "aaa";
        scheme.lines[1] = "bbb";
        scheme.lines[2] = "ccc";
        Block block = new BlockImpl(scheme, 0, 0, 3, 3);
        
        List<Block> subblocks = block.extractParallelSubblocks();
        
        assertEquals(3, subblocks.size());
        assertTrue(subblocks.contains(new BlockImpl(scheme, 0, 0, 3, 1)));
        assertTrue(subblocks.contains(new BlockImpl(scheme, 0, 1, 3, 2)));
        assertTrue(subblocks.contains(new BlockImpl(scheme, 0, 2, 3, 3)));
    }
    
    @Test
    public void testToString() {
        Scheme scheme = new Scheme();
        scheme.lines[0] = "abc";
        scheme.lines[1] = "def";
        scheme.lines[2] = "ghi";
        Block block = new BlockImpl(scheme, 0, 0, 3, 3);
        
        String expected = "Block(0,0, 3,3):\n" +
                          " #############\n" +
                          " # abc #\n" +
                          " # def #\n" +
                          " # ghi #\n" +
                          " #############";
        
        assertEquals(expected, block.toString());
    }
    
    @Test
    public void testGetUpperLine() {
        Scheme scheme = new Scheme();
        scheme.lines[0] = "hello";
        Block block = new BlockImpl(scheme, 0, 0, 5, 1);
        
        assertEquals("hello", block.getUpperLine());
    }

    private class BlockImpl extends Block {
        
        public BlockImpl(Scheme scheme, int x1, int y1, int x2, int y2) {
            super(scheme, x1, y1, x2, y2);
        }
        
        public Block subblock(int p0, int p1, int p2, int p3) {
            return new BlockImpl(scheme, p0, p1, p2, p3);
        }
        
        public List<Block> extractSequentialSubblocks() {
            List<Block> subblocks = new ArrayList<>();
            for (int y = y1; y < y2; y++) {
                for (int x = x1; x < x2; x++) {
                    subblocks.add(new BlockImpl(scheme, x, y, x + 1, y + 1));
                }
            }
            return subblocks;
        }
        
        public List<Block> extractParallelSubblocks() {
            List<Block> subblocks = new ArrayList<>();
            for (int y = y1; y < y2; y++) {
                subblocks.add(new BlockImpl(scheme, x1, y, x2, y + 1));
            }
            return subblocks;
        }
    }
}