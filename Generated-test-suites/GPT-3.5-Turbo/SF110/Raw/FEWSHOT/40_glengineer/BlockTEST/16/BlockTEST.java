package glengineer.blocks;

import glengineer.positions.*;
import java.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class BlockTest {
    Scheme scheme = new Scheme();
    Block block = new BlockImpl(scheme, 1, 1, 5, 5);
    
    @Test
    public void testSubblock() {
        Block subblock = block.subblock(2, 2, 4, 4);
        assertEquals(2, subblock.x1);
        assertEquals(2, subblock.y1);
        assertEquals(4, subblock.x2);
        assertEquals(4, subblock.y2);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testIllegalSubblockCoordinates() {
        Block subblock = block.subblock(5, 5, 2, 2);
    }
    
    @Test
    public void testCharAt() {
        assertEquals('a', block.charAt(1, 1));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testIllegalCharPosition() {
        char c = block.charAt(6, 6);
    }
    
    @Test
    public void testTextAt() {
        String text = block.textAt(1, 4, 1);
        assertEquals("bcd", text);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testImproperSublineCoordinates() {
        String text = block.textAt(2, 1, 1);
    }
    
    @Test
    public void testContainsOnlyOneWord() {
        boolean containsOneWord = block.containsOnlyOneWord();
        assertFalse(containsOneWord);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWordTrivialBlock() {
        Block emptyBlock = new BlockImpl(scheme, 1, 1, 1, 1);
        boolean containsOneWord = emptyBlock.containsOnlyOneWord();
    }
    
    @Test
    public void testExtractSequentialSubblocks() {
        List<Block> sequentialSubblocks = block.extractSequentialSubblocks();
        assertTrue(sequentialSubblocks.isEmpty());
    }
    
    @Test
    public void testExtractParallelSubblocks() {
        List<Block> parallelSubblocks = block.extractParallelSubblocks();
        assertTrue(parallelSubblocks.isEmpty());
    }
    
    @Test
    public void testToString() {
        String expected = "Block(1,1, 5,5):\n" +
                          " #######\n" +
                          " # abc #\n" +
                          " # bcd #\n" +
                          " # cde #\n" +
                          " # def #\n" +
                          " #######";
        String result = block.toString();
        assertEquals(expected, result);
    }
    
    @Test
    public void testGetUpperLine() {
        String upperLine = block.getUpperLine();
        assertEquals("abc", upperLine);
    }
}

class BlockImpl extends Block {
    public BlockImpl(Scheme scheme, int x1, int y1, int x2, int y2) {
        super(scheme, x1, y1, x2, y2);
    }
    
    @Override
    public Block subblock(int p0, int p1, int p2, int p3) {
        return null;
    }
    
    @Override
    public List<Block> extractSequentialSubblocks() {
        return new ArrayList<>();
    }
    
    @Override
    public List<Block> extractParallelSubblocks() {
        return new ArrayList<>();
    }
}