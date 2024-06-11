package glengineer.blocks;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BlockTest {
    
    private Scheme scheme;
    private Block block;
    
    @Before
    public void setUp() {
        scheme = new Scheme(10, 10);
        block = new BlockImpl(scheme, 1, 1, 4, 4);
    }
    
    @Test
    public void testSubblock() {
        Block subblock = block.subblock(2, 2, 3, 3);
        assertEquals(2, subblock.getX1());
        assertEquals(2, subblock.getY1());
        assertEquals(3, subblock.getX2());
        assertEquals(3, subblock.getY2());
    }
    
    @Test
    public void testCharAt_ValidPosition_ReturnsChar() {
        char c = block.charAt(2, 2);
        assertEquals('O', c);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCharAt_InvalidPosition_ThrowsException() {
        block.charAt(5, 5);
    }
    
    @Test
    public void testTextAt_ValidCoordinates_ReturnsSubstring() {
        String text = block.textAt(2, 4, 2);
        assertEquals("OB", text);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testTextAt_InvalidCoordinates_ThrowsException() {
        block.textAt(5, 10, 2);
    }
    
    @Test
    public void testContainsOnlyOneWord_NoWords_ReturnsFalse() {
        assertFalse(block.containsOnlyOneWord());
    }
    
    @Test
    public void testContainsOnlyOneWord_OneWord_ReturnsTrue() {
        Block singleWordBlock = new BlockImpl(scheme, 2, 2, 3, 2);
        assertTrue(singleWordBlock.containsOnlyOneWord());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWord_TrivialBlock_ThrowsException() {
        Block trivialBlock = new BlockImpl(scheme);
        trivialBlock.containsOnlyOneWord();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWord_EmptyBlock_ThrowsException() {
        Block emptyBlock = new BlockImpl(scheme, 1, 1, 1, 1);
        emptyBlock.containsOnlyOneWord();
    }
    
    @Test
    public void testExtractSequentialSubblocks_NoSubblocks_ReturnsEmptyList() {
        List<Block> subblocks = block.extractSequentialSubblocks();
        assertTrue(subblocks.isEmpty());
    }
    
    @Test
    public void testExtractSequentialSubblocks_WithSubblocks_ReturnsSubblocksList() {
        Block subblock1 = new BlockImpl(scheme, 2, 2, 3, 3);
        Block subblock2 = new BlockImpl(scheme, 2, 2, 4, 4);
        block.addSubblock(subblock1);
        block.addSubblock(subblock2);
        
        List<Block> subblocks = block.extractSequentialSubblocks();
        assertEquals(2, subblocks.size());
        assertTrue(subblocks.contains(subblock1));
        assertTrue(subblocks.contains(subblock2));
    }
    
    @Test
    public void testExtractParallelSubblocks_NoSubblocks_ReturnsEmptyList() {
        List<Block> subblocks = block.extractParallelSubblocks();
        assertTrue(subblocks.isEmpty());
    }
    
    @Test
    public void testExtractParallelSubblocks_WithSubblocks_ReturnsSubblocksList() {
        Block subblock1 = new BlockImpl(scheme, 2, 2, 3, 3);
        Block subblock2 = new BlockImpl(scheme, 2, 2, 4, 4);
        block.addSubblock(subblock1);
        block.addSubblock(subblock2);
        
        List<Block> subblocks = block.extractParallelSubblocks();
        assertEquals(2, subblocks.size());
        assertTrue(subblocks.contains(subblock1));
        assertTrue(subblocks.contains(subblock2));
    }
    
    @Test
    public void testToString() {
        String expected = "Block(1,1, 4,4):\n" +
                " ####\n" +
                " # O #\n" +
                " # O #\n" +
                " # O #\n" +
                " ####";
        assertEquals(expected, block.toString());
    }
    
    @Test
    public void testGetUpperLine() {
        String upperLine = block.getUpperLine();
        assertEquals("OOO", upperLine);
    }
    
    private class BlockImpl extends Block {
        
        private BlockImpl(Scheme scheme, int x1, int y1, int x2, int y2) {
            super(scheme, x1, y1, x2, y2);
        }
        
        @Override
        public Block subblock(int x1, int y1, int x2, int y2) {
            return new BlockImpl(scheme, x1, y1, x2, y2);
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
}