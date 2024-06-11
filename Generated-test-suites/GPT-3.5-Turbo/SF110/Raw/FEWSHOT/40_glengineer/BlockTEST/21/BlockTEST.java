import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

import glengineer.blocks.Block;
import glengineer.positions.Scheme;

public class BlockTest {
    private Scheme scheme;
    
    @Before
    public void setUp() {
        scheme = new Scheme();
    }
    
    @Test
    public void testBlockConstructor_withValidCoordinates() {
        Block block = new ConcreteBlock(scheme, 0, 0, 2, 2);
        assertEquals(0, block.x1); 
        assertEquals(0, block.y1);
        assertEquals(2, block.x2);
        assertEquals(2, block.y2);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructor_withInvalidCoordinates() {
        Block block = new ConcreteBlock(scheme, 2, 2, 0, 0);
    }
    
    @Test
    public void testSubblock() {
        Block block = new ConcreteBlock(scheme, 0, 0, 5, 5);
        Block subblock = block.subblock(1, 1, 3, 3);
        assertEquals(1, subblock.x1); 
        assertEquals(1, subblock.y1);
        assertEquals(3, subblock.x2);
        assertEquals(3, subblock.y2);
    }
    
    @Test
    public void testCharAt() {
        Block block = new ConcreteBlock(scheme, 0, 0, 5, 5);
        char c = block.charAt(2, 2);
        assertEquals(' ', c);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCharAt_withInvalidPosition() {
        Block block = new ConcreteBlock(scheme, 0, 0, 5, 5);
        char c = block.charAt(6, 6);
    }
    
    @Test
    public void testTextAt() {
        Block block = new ConcreteBlock(scheme, 0, 0, 5, 5);
        String text = block.textAt(1, 3, 2);
        assertEquals("  ", text);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testTextAt_withInvalidCoordinates() {
        Block block = new ConcreteBlock(scheme, 0, 0, 5, 5);
        String text = block.textAt(6, 8, 4);
    }
    
    @Test
    public void testContainsOnlyOneWord_withNonTrivialBlock() {
        Block block = new ConcreteBlock(scheme, 0, 0, 5, 5);
        assertTrue(block.containsOnlyOneWord());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWord_withTrivialBlock() {
        Block block = new ConcreteBlock(scheme, 0, 0, 0, 0); 
        block.containsOnlyOneWord();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWord_withEmptyBlock() {
        Block block = new ConcreteBlock(scheme, 0, 0, 5, 0);
        block.containsOnlyOneWord();
    }
    
    @Test
    public void testExtractSequentialSubblocks() {
        Block block = new ConcreteBlock(scheme, 0, 0, 5, 5);
        List<Block> subblocks = block.extractSequentialSubblocks();
        assertEquals(1, subblocks.size());
        assertEquals(block, subblocks.get(0));
    }
    
    @Test
    public void testExtractParallelSubblocks() {
        Block block = new ConcreteBlock(scheme, 0, 0, 5, 5);
        List<Block> subblocks = block.extractParallelSubblocks();
        assertEquals(1, subblocks.size());
        assertEquals(block, subblocks.get(0));
    }
    
    @Test
    public void testToString() {
        Block block = new ConcreteBlock(scheme, 0, 0, 5, 5);
        String expected = "Block(0,0, 5,5):\n" +
                          " ###################\n" +
                          " #                 #\n" +
                          " #                 #\n" +
                          " #                 #\n" +
                          " #                 #\n" +
                          " #                 #\n" +
                          " ###################";
                          
        assertEquals(expected, block.toString());
    }
    
    @Test
    public void testGetUpperLine() {
        Block block = new ConcreteBlock(scheme, 0, 0, 5, 5);
        String upperLine = block.getUpperLine();
        assertEquals(" ###################", upperLine);
    }
}

class ConcreteBlock extends Block {
    public ConcreteBlock(Scheme scheme, int x1, int y1, int x2, int y2) {
        super(scheme, x1, y1, x2, y2);
    }
    
    @Override
    public Block subblock(final int p0, final int p1, final int p2, final int p3) {
        // TODO: Implement this method
        return null;
    }
    
    @Override
    public List<Block> extractSequentialSubblocks() {
        // TODO: Implement this method
        return null;
    }
    
    @Override
    public List<Block> extractParallelSubblocks() {
        // TODO: Implement this method
        return null;
    }
}