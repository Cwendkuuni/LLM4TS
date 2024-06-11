package glengineer.blocks;

import glengineer.positions.*;
import java.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class BlockTest {

    @Test
    public void testConstructorWithCoordinates() {
        Block block = new Block(new Scheme(), 1, 2, 3, 4);
        assertEquals(1, block.x1);
        assertEquals(2, block.y1);
        assertEquals(3, block.x2);
        assertEquals(4, block.y2);
    }
    
    @Test
    public void testConstructorWithCharPositions() {
        CharPosition p1 = new CharPosition(1, 2);
        CharPosition p2 = new CharPosition(3, 4);
        Block block = new Block(new Scheme(), p1, p2);
        assertEquals(1, block.x1);
        assertEquals(2, block.y1);
        assertEquals(3, block.x2);
        assertEquals(4, block.y2);
    }
    
    @Test
    public void testDefaultConstructor() {
        Scheme scheme = new Scheme();
        Block block = new Block(scheme);
        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(scheme.x2, block.x2);
        assertEquals(scheme.y2, block.y2);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSubblock() {
        Block block = new Block(new Scheme(), 1, 2, 3, 4);
        Block subblock = block.subblock(1, 2, 3, 4);
        assertNotNull(subblock);
    }
    
    @Test
    public void testCharAt() {
        Scheme scheme = new Scheme();
        Block block = new Block(scheme);
        char result = block.charAt(1, 2);
        assertEquals(scheme.lines[2].charAt(1), result);
    }
    
    @Test
    public void testTextAt() {
        Scheme scheme = new Scheme();
        Block block = new Block(scheme);
        String result = block.textAt(1, 3, 2);
        assertEquals(scheme.lines[2].substring(1, 3), result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWord() {
        Block block = new Block(new Scheme());
        boolean result = block.containsOnlyOneWord();
    }
    
    @Test
    public void testExtractSequentialSubblocks() {
        Block block = new Block(new Scheme());
        List<Block> subblocks = block.extractSequentialSubblocks();
        assertNotNull(subblocks);
    }
    
    @Test
    public void testExtractParallelSubblocks() {
        Block block = new Block(new Scheme());
        List<Block> subblocks = block.extractParallelSubblocks();
        assertNotNull(subblocks);
    }
    
    @Test
    public void testToString() {
        Block block = new Block(new Scheme(), 1, 1, 2, 2);
        String result = block.toString();
        String expected = "Block(1,1, 2,2):\n ####\n #  #\n #  #\n ####";
        assertEquals(expected, result);
    }
    
    @Test
    public void testGetUpperLine() {
        Scheme scheme = new Scheme();
        Block block = new Block(scheme);
        String result = block.getUpperLine();
        assertEquals(scheme.textAt(0, scheme.x2, 0), result);
    }
}