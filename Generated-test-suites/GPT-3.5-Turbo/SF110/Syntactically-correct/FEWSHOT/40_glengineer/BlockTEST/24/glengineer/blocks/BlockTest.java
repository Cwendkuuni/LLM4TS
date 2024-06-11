package glengineer.blocks;

import glengineer.positions.*;
import java.util.*;

import static org.junit.Assert.*;
import org.junit.Test;

public class BlockTest {
    @Test
    public void testSubblock() {
        Scheme scheme = new Scheme();
        Block block = new TestBlock(scheme, 0, 0, 4, 4);
        Block subblock = block.subblock(1, 1, 3, 3);
        
        assertEquals(1, subblock.x1);
        assertEquals(1, subblock.x2);
        assertEquals(1, subblock.y1);
        assertEquals(1, subblock.y2);
    }
    
    @Test
    public void testCharAt() {
        Scheme scheme = new Scheme();
        Block block = new TestBlock(scheme, 0, 0, 4, 4);
        
        assertEquals('A', block.charAt(0, 0));
        assertEquals('B', block.charAt(3, 3));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCharAt_outOfBounds() {
        Scheme scheme = new Scheme();
        Block block = new TestBlock(scheme, 0, 0, 4, 4);
        
        block.charAt(5, 5);
    }
    
    @Test
    public void testTextAt() {
        Scheme scheme = new Scheme();
        Block block = new TestBlock(scheme, 0, 0, 4, 4);
        
        assertEquals("ABC", block.textAt(0, 3, 0));
        assertEquals("DEF", block.textAt(0, 3, 3));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testTextAt_improperCoordinates() {
        Scheme scheme = new Scheme();
        Block block = new TestBlock(scheme, 0, 0, 4, 4);
        
        block.textAt(0, 5, 0);
    }

    @Test
    public void testExtractSequentialSubblocks() {
        Scheme scheme = new Scheme();
        Block block = new TestBlock(scheme, 0, 0, 4, 4);
        
        List<Block> subblocks = block.extractSequentialSubblocks();
        
        assertEquals(4, subblocks.size());
        assertEquals("A", subblocks.get(0).getUpperLine());
        assertEquals("B", subblocks.get(1).getUpperLine());
        assertEquals("C", subblocks.get(2).getUpperLine());
        assertEquals("D", subblocks.get(3).getUpperLine());
    }
    
    @Test
    public void testExtractParallelSubblocks() {
        Scheme scheme = new Scheme();
        Block block = new TestBlock(scheme, 0, 0, 4, 4);
        
        List<Block> subblocks = block.extractParallelSubblocks();
        
        assertEquals(4, subblocks.size());
        assertEquals("A\nB\nC\nD", subblocks.get(0).toString());
        assertEquals("A\nB\nC\nD", subblocks.get(1).toString());
        assertEquals("A\nB\nC\nD", subblocks.get(2).toString());
        assertEquals("A\nB\nC\nD", subblocks.get(3).toString());
    }
    
    @Test
    public void testToString() {
        Scheme scheme = new Scheme();
        Block block = new TestBlock(scheme, 0, 0, 4, 2);
        
        String expectedToString = "Block(0,0, 4,2):\n" +
                                  " ####\n" +
                                  " # AB #\n" +
                                  " # CD #\n" +
                                  " ####";
        
        assertEquals(expectedToString, block.toString());
    }
    
    @Test
    public void testGetUpperLine() {
        Scheme scheme = new Scheme();
        Block block = new TestBlock(scheme, 0, 0, 4, 2);
        
        assertEquals("AB", block.getUpperLine());
    }
}

class TestBlock extends Block {
    public TestBlock(Scheme scheme, int x1, int y1, int x2, int y2) {
        super(scheme, x1, y1, x2, y2);
    }
    
    public Block subblock(int p0, int p1, int p2, int p3) {
        return null;
    }
    
    public List<Block> extractSequentialSubblocks() {
        return null;
    }
    
    public List<Block> extractParallelSubblocks() {
        return null;
    }
}