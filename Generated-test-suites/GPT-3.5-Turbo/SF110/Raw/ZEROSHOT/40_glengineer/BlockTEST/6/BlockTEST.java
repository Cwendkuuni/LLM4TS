package glengineer.blocks;

import glengineer.positions.*;
import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

public class BlockTest {

    @Test
    public void testBlockConstructorWithCoordinates() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new BlockImpl(scheme, 0, 0, 2, 2);
        
        // Check if the block is created properly
        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(2, block.x2);
        assertEquals(2, block.y2);
        assertEquals(scheme, block.scheme);
    }

    @Test
    public void testBlockConstructorWithCharPositions() {
        Scheme scheme = new Scheme(10, 10);
        CharPosition p1 = new CharPosition(0, 0);
        CharPosition p2 = new CharPosition(2, 2);
        Block block = new BlockImpl(scheme, p1, p2);
        
        // Check if the block is created properly
        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(2, block.x2);
        assertEquals(2, block.y2);
        assertEquals(scheme, block.scheme);
    }

    @Test
    public void testBlockConstructorWithScheme() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new BlockImpl(scheme);
        
        // Check if the block is created with default coordinates
        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(scheme.x2, block.x2);
        assertEquals(scheme.y2, block.y2);
        assertEquals(scheme, block.scheme);
    }

    @Test
    public void testSubblock() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new BlockImpl(scheme, 0, 0, 2, 2);
        Block subblock = block.subblock(1, 1, 2, 2);
        
        // Check if the subblock is created properly
        assertEquals(1, subblock.x1);
        assertEquals(1, subblock.y1);
        assertEquals(2, subblock.x2);
        assertEquals(2, subblock.y2);
        assertEquals(scheme, subblock.scheme);
    }

    @Test
    public void testCharAt() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new BlockImpl(scheme, 0, 0, 2, 2);
        
        // Check if charAt returns the correct character
        assertEquals('#', block.charAt(0, 0));
    }

    @Test
    public void testTextAt() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new BlockImpl(scheme, 0, 0, 2, 2);
        
        // Check if textAt returns the correct substring
        assertEquals("##", block.textAt(0, 2, 0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWordWithTrivialBlock() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new TrivialBlock(scheme);
        
        // Check if an exception is thrown when searching in a trivial block
        block.containsOnlyOneWord();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWordWithEmptyBlock() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new BlockImpl(scheme, 0, 0, 1, 1);
        
        // Check if an exception is thrown when verifying unicity in an empty block
        block.containsOnlyOneWord();
    }

    @Test
    public void testContainsOnlyOneWordWithNonTrivialBlock() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new BlockImpl(scheme, 0, 0, 2, 2);
        
        // Check if containsOnlyOneWord returns the correct result
        assertFalse(block.containsOnlyOneWord());
    }

    @Test
    public void testExtractSequentialSubblocks() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new BlockImpl(scheme, 0, 0, 2, 2);
        
        // Check if extractSequentialSubblocks returns a list of blocks
        assertNotNull(block.extractSequentialSubblocks());
        assertTrue(block.extractSequentialSubblocks() instanceof List);
    }

    @Test
    public void testExtractParallelSubblocks() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new BlockImpl(scheme, 0, 0, 2, 2);
        
        // Check if extractParallelSubblocks returns a list of blocks
        assertNotNull(block.extractParallelSubblocks());
        assertTrue(block.extractParallelSubblocks() instanceof List);
    }

    @Test
    public void testToString() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new BlockImpl(scheme, 0, 0, 2, 2);
        
        // Check if toString returns the correct string representation of the block
        String expected = "Block(0,0, 2,2):\n ####\n #  ##\n #  ##\n ####";
        assertEquals(expected, block.toString());
    }

    @Test
    public void testGetUpperLine() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new BlockImpl(scheme, 0, 0, 2, 2);
        
        // Check if getUpperLine returns the correct upper line string
        assertEquals("##", block.getUpperLine());
    }
}