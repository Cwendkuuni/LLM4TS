package glengineer.blocks;

import glengineer.positions.*;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class BlockTest {
    @Test
    public void testSubblock() {
        Scheme scheme = new Scheme();
        CharPosition charPosition1 = new CharPosition(1, 1);
        CharPosition charPosition2 = new CharPosition(2, 2);
        Block block = new Block(scheme, charPosition1, charPosition2);
        
        Block subblock = block.subblock(0, 1, 0, 1);
        
        assertEquals(0, subblock.x1);
        assertEquals(1, subblock.x2);
        assertEquals(0, subblock.y1);
        assertEquals(1, subblock.y2);
    }
    
    @Test
    public void testCharAt() {
        Scheme scheme = new Scheme();
        scheme.lines[0] = "abcdefg";
        Block block = new Block(scheme);
        
        assertEquals('a', block.charAt(0, 0));
        assertEquals('b', block.charAt(1, 0));
        assertEquals('e', block.charAt(4, 0));
        assertEquals('f', block.charAt(5, 0));
        assertEquals('g', block.charAt(6, 0));
    }
    
    @Test
    public void testTextAt() {
        Scheme scheme = new Scheme();
        scheme.lines[0] = "abcdefg";
        Block block = new Block(scheme);
        
        assertEquals("a", block.textAt(0, 1, 0));
        assertEquals("bcdefg", block.textAt(1, 7, 0));
        assertEquals("cde", block.textAt(2, 5, 0));
    }
    
    @Test
    public void testContainsOnlyOneWord() {
        Scheme scheme = new Scheme();
        scheme.lines[0] = "abcdefg";
        Block block = new Block(scheme);
        
        assertFalse(block.containsOnlyOneWord());
    }
    
    @Test
    public void testExtractSequentialSubblocks() {
        Scheme scheme = new Scheme();
        scheme.lines[0] = "abcdefg";
        Block block = new Block(scheme);
        
        List<Block> subblocks = block.extractSequentialSubblocks();
        
        assertEquals(7, subblocks.size());
    }
    
    @Test
    public void testExtractParallelSubblocks() {
        Scheme scheme = new Scheme();
        scheme.lines[0] = "abcdefg";
        Block block = new Block(scheme);
        
        List<Block> subblocks = block.extractParallelSubblocks();
        
        assertEquals(1, subblocks.size());
    }
    
    @Test
    public void testToString() {
        Scheme scheme = new Scheme();
        scheme.lines[0] = "abcdefg";
        scheme.lines[1] = "hijklmn";
        Block block = new Block(scheme);
        
        String expected = "Block(0,0, 7,1):\n";
        expected += " ####################\n";
        expected += " # abcdefg #\n";
        expected += " # hijklmn #\n";
        expected += " ####################";
        
        assertEquals(expected, block.toString());
    }
    
    @Test
    public void testGetUpperLine() {
        Scheme scheme = new Scheme();
        scheme.lines[0] = "abcdefg";
        scheme.lines[1] = "hijklmn";
        Block block = new Block(scheme);
        
        assertEquals("abcdefg", block.getUpperLine());
    }
}