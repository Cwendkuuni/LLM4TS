package glengineer.blocks;

import glengineer.positions.*;
import java.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class BlockTest {
    
    @Test
    public void testSubblock() {
        Scheme scheme = new Scheme();
        Block block = new ConcreteBlock(scheme, 0, 0, 2, 2);
        Block subblock = block.subblock(1, 1, 2, 2);
        assertEquals(1, subblock.getX1());
        assertEquals(1, subblock.getY1());
        assertEquals(2, subblock.getX2());
        assertEquals(2, subblock.getY2());
    }
    
    @Test
    public void testCharAt() {
        Scheme scheme = new Scheme();
        scheme.lines = new char[][]{{'a', 'b', 'c'}, {'d', 'e', 'f'}, {'g', 'h', 'i'}};
        Block block = new ConcreteBlock(scheme, 0, 0, 2, 2);
        assertEquals('a', block.charAt(0, 0));
        assertEquals('e', block.charAt(1, 1));
        assertEquals('i', block.charAt(2, 2));
        
        try {
            block.charAt(3, 0);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertEquals("char position (3,0) out of block.\nBlock(0,0, 2,2):\n ####\n # abc #\n # def #\n #####", e.getMessage());
        }
    }
    
    @Test
    public void testTextAt() {
        Scheme scheme = new Scheme();
        scheme.lines = new char[][]{{'a', 'b', 'c'}, {'d', 'e', 'f'}, {'g', 'h', 'i'}};
        Block block = new ConcreteBlock(scheme, 0, 0, 2, 2);
        assertEquals("abc", block.textAt(0, 3, 0));
        assertEquals("ef", block.textAt(1, 3, 1));
        assertEquals("i", block.textAt(2, 3, 2));
        
        try {
            block.textAt(0, 4, 0);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertEquals("Improper subline coodinates.", e.getMessage());
        }
    }
    
    @Test
    public void testContainsOnlyOneWord() {
        Scheme scheme = new Scheme();
        scheme.lines = new char[][]{{'a', 'b', 'c'}, {'d', 'e', 'f'}, {'g', 'h', 'i'}};
        Block block = new ConcreteBlock(scheme, 0, 0, 2, 2);
        assertFalse(block.containsOnlyOneWord());
        
        try {
            Block emptyBlock = new ConcreteBlock(scheme, 0, 2, 2, 2);
            emptyBlock.containsOnlyOneWord();
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertEquals("cannot verify unicity of an word in an empty block", e.getMessage());
        }
    }
    
    @Test
    public void testToString() {
        Scheme scheme = new Scheme();
        scheme.lines = new char[][]{{'a', 'b', 'c'}, {'d', 'e', 'f'}, {'g', 'h', 'i'}};
        Block block = new ConcreteBlock(scheme, 0, 0, 2, 2);
        String expected = "Block(0,0, 2,2):\n ####\n # abc #\n # def #\n #####";
        assertEquals(expected, block.toString());
    }
    
    @Test
    public void testGetUpperLine() {
        Scheme scheme = new Scheme();
        scheme.lines = new char[][]{{'a', 'b', 'c'}, {'d', 'e', 'f'}, {'g', 'h', 'i'}};
        Block block = new ConcreteBlock(scheme, 0, 0, 2, 2);
        assertEquals("abc", block.getUpperLine());
    }
    
}