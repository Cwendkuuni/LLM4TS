package glengineer.blocks;

import static org.junit.Assert.*;
import org.junit.Test;
import glengineer.positions.*;

public class BlockTest {
    @Test
    public void testBlockConstructor_invalidArguments() {
        Scheme scheme = new Scheme(10, 10);
        
        try {
            Block block1 = new Block(scheme, -1, 1, 2, 3);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("improper block definition: -1,1, 2,3.", e.getMessage());
        }
        
        try {
            Block block2 = new Block(scheme, 1, -1, 2, 3);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("improper block definition: 1,-1, 2,3.", e.getMessage());
        }
        
        try {
            Block block3 = new Block(scheme, 1, 1, 3, 2);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("improper block definition: 1,1, 3,2.", e.getMessage());
        }
        
        try {
            Block block4 = new Block(scheme, 1, 1, 2, 11);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("improper block definition: 1,1, 2,11.", e.getMessage());
        }
    }
    
    @Test
    public void testBlockConstructor_validArguments() {
        Scheme scheme = new Scheme(10, 10);
        
        Block block1 = new Block(scheme, 0, 0, 2, 2);
        assertEquals(0, block1.x1);
        assertEquals(0, block1.y1);
        assertEquals(2, block1.x2);
        assertEquals(2, block1.y2);
        
        Block block2 = new Block(scheme, new CharPosition(0, 0), new CharPosition(2, 2));
        assertEquals(0, block2.x1);
        assertEquals(0, block2.y1);
        assertEquals(2, block2.x2);
        assertEquals(2, block2.y2);
        
        Block block3 = new Block(scheme);
        assertEquals(0, block3.x1);
        assertEquals(0, block3.y1);
        assertEquals(10, block3.x2);
        assertEquals(10, block3.y2);
    }
    
    @Test
    public void testSubblock() {
        Block block = new Block(new Scheme(10, 10), 0, 0, 5, 5);
        Block subblock = block.subblock(2, 2, 4, 4);
        assertEquals(2, subblock.x1);
        assertEquals(2, subblock.y1);
        assertEquals(4, subblock.x2);
        assertEquals(4, subblock.y2);
    }
    
    @Test
    public void testCharAt() {
        Block block = new Block(new Scheme(10, 10), 0, 0, 3, 3);
        assertEquals('A', block.charAt(0, 0));
        assertEquals('B', block.charAt(1, 1));
        assertEquals('C', block.charAt(2, 2));
    }
    
    @Test
    public void testTextAt() {
        Block block = new Block(new Scheme(10, 10), 0, 0, 5, 5);
        assertEquals("ABCD", block.textAt(0, 4, 0));
        assertEquals("EFGHI", block.textAt(0, 5, 1));
        assertEquals("JKLMNOP", block.textAt(0, 6, 2));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWord_trivialBlock() {
        Block block = new Block(new Scheme(10, 10));
        block.containsOnlyOneWord();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWord_emptyBlock() {
        Block block = new Block(new Scheme(10, 10), 0, 0, 5, 0);
        block.containsOnlyOneWord();
    }
    
    @Test
    public void testContainsOnlyOneWord() {
        Block block = new Block(new Scheme(10, 10), 0, 0, 5, 5);
        assertTrue(block.containsOnlyOneWord());
    }
    
    @Test
    public void testExtractSequentialSubblocks() {
        Block block = new Block(new Scheme(10, 10), 0, 0, 5, 5);
        assertEquals(1, block.extractSequentialSubblocks().size());
        
        Block block2 = new Block(new Scheme(10, 10), 0, 0, 5, 3);
        assertEquals(2, block2.extractSequentialSubblocks().size());
        
        Block block3 = new Block(new Scheme(10, 10), 2, 2, 4, 4);
        assertEquals(1, block3.extractSequentialSubblocks().size());
    }
    
    @Test
    public void testExtractParallelSubblocks() {
        Block block = new Block(new Scheme(10, 10), 0, 0, 5, 5);
        assertEquals(1, block.extractParallelSubblocks().size());
        
        Block block2 = new Block(new Scheme(10, 10), 0, 0, 3, 5);
        assertEquals(2, block2.extractParallelSubblocks().size());
        
        Block block3 = new Block(new Scheme(10, 10), 2, 2, 4, 4);
        assertEquals(1, block3.extractParallelSubblocks().size());
    }
    
    @Test
    public void testToString() {
        Block block = new Block(new Scheme(10, 10), 0, 0, 2, 2);
        String expected = "Block(0,0, 2,2):\n" +
                          " #### \n" +
                          " # AB #\n" +
                          " # CD #\n" +
                          " #### ";
        assertEquals(expected, block.toString());
        
        Block block2 = new Block(new Scheme(10, 10), 0, 0, 4, 3);
        String expected2 = "Block(0,0, 4,3):\n" +
                           " ##### \n" +
                           " # ABC #\n" +
                           " # DEF #\n" +
                           " ##### ";
        assertEquals(expected2, block2.toString());
    }
    
    @Test
    public void testGetUpperLine() {
        Block block = new Block(new Scheme(10, 10), 0, 0, 4, 3);
        assertEquals("ABC", block.getUpperLine());
        
        Block block2 = new Block(new Scheme(10, 10), 2, 2, 4, 4);
        assertEquals("CD", block2.getUpperLine());
    }
}