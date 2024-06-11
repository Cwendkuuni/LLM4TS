import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import java.util.List;

public class BlockTest {
    private Scheme scheme;
    private Block block;
    
    @Before
    public void setUp() {
        scheme = new Scheme(5, 5);
        block = new BlockTestImpl(scheme, 0, 0, 2, 2);
    }
    
    @Test
    public void testBlockConstructor() {
        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(2, block.x2);
        assertEquals(2, block.y2);
        assertEquals(scheme, block.scheme);
    }
    
    @Test
    public void testSubblock() {
        Block subblock = block.subblock(0, 0, 1, 1);
        assertEquals(0, subblock.x1);
        assertEquals(0, subblock.y1);
        assertEquals(1, subblock.x2);
        assertEquals(1, subblock.y2);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSubblockException() {
        block.subblock(1, 1, 0, 0);
    }
    
    @Test
    public void testContainsOnlyOneWord() {
        assertFalse(block.containsOnlyOneWord());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWordException() {
        Block trivialBlock = new BlockTestImpl(scheme);
        trivialBlock.containsOnlyOneWord();
    }
    
    @Test
    public void testExtractSequentialSubblocks() {
        List<Block> subblocks = block.extractSequentialSubblocks();
        assertEquals(0, subblocks.size());
    }
    
    @Test
    public void testExtractParallelSubblocks() {
        List<Block> subblocks = block.extractParallelSubblocks();
        assertEquals(0, subblocks.size());
    }
    
    @Test
    public void testToString() {
        String result = block.toString();
        assertNotNull(result);
    }
    
    @Test
    public void testGetUpperLine() {
        String upperLine = block.getUpperLine();
        assertNotNull(upperLine);
    }
    
    private class BlockTestImpl extends Block {
        public BlockTestImpl(Scheme scheme, int x1, int y1, int x2, int y2) {
            super(scheme, x1, y1, x2, y2);
        }
        
        @Override
        public Block subblock(int p0, int p1, int p2, int p3) {
            return new BlockTestImpl(scheme, p0, p1, p2, p3);
        }
        
        @Override
        public List<Block> extractSequentialSubblocks() {
            return null;
        }
        
        @Override
        public List<Block> extractParallelSubblocks() {
            return null;
        }
        
        @Override
        public char charAt(int x, int y) {
            return ' ';
        }
        
        @Override
        public String textAt(int x1, int x2, int y) {
            return "";
        }
        
        @Override
        public boolean isTrivial() {
            return false;
        }
        
        @Override
        public HWordPosition firstWord_Horizontally() {
            return new HWordPosition(0, 0);
        }
        
        @Override
        public WordPosition lastWord_Horizontally() {
            return new WordPosition(0, 0);
        }
    }
}