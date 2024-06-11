
import org.junit.Test;
import static org.junit.Assert.*;

public class BlockTest {
    
    @Test
    public void testBlockConstructor_WithValidParameters_ShouldCreateBlock() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new BlockImpl(scheme, 1, 1, 3, 3);
        assertEquals(scheme, block.scheme);
        assertEquals(1, block.x1);
        assertEquals(1, block.y1);
        assertEquals(3, block.x2);
        assertEquals(3, block.y2);
    }
    
    @Test
    public void testBlockConstructor_WithInvalidParameters_ShouldThrowIllegalArgumentException() {
        Scheme scheme = new Scheme(5, 5);
        try {
            Block block = new BlockImpl(scheme, -1, 1, 3, 3);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("improper block definition: -1,1, 3,3.", e.getMessage());
        }
    }
    
    @Test
    public void testBlockConstructor_WithCharPositionParameters_ShouldCreateBlock() {
        Scheme scheme = new Scheme(5, 5);
        CharPosition p1 = new CharPosition(1, 1);
        CharPosition p2 = new CharPosition(3, 3);
        Block block = new BlockImpl(scheme, p1, p2);
        assertEquals(scheme, block.scheme);
        assertEquals(1, block.x1);
        assertEquals(1, block.y1);
        assertEquals(3, block.x2);
        assertEquals(3, block.y2);
    }
    
    @Test
    public void testBlockConstructor_WithSchemeParameter_ShouldCreateBlock() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new BlockImpl(scheme);
        assertEquals(scheme, block.scheme);
        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(5, block.x2);
        assertEquals(5, block.y2);
    }
    
    @Test
    public void testSubblock_WithValidParameters_ShouldReturnSubblock() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new BlockImpl(scheme, 1, 1, 4, 4);
        Block subblock = block.subblock(2, 2, 3, 3);
        assertEquals(scheme, subblock.scheme);
        assertEquals(2, subblock.x1);
        assertEquals(2, subblock.y1);
        assertEquals(3, subblock.x2);
        assertEquals(3, subblock.y2);
    }
    
    @Test
    public void testSubblock_WithInvalidParameters_ShouldThrowIllegalArgumentException() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new BlockImpl(scheme, 1, 1, 4, 4);
        try {
            Block subblock = block.subblock(1, 1, 6, 6);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("improper block definition: 1,1, 6,6.", e.getMessage());
        }
    }
    
    @Test
    public void testCharAt_WithValidCoordinates_ShouldReturnCharacter() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new BlockImpl(scheme, 1, 1, 4, 4);
        char character = block.charAt(2, 2);
        assertEquals(scheme.lines[2].charAt(2), character);
    }
    
    @Test
    public void testCharAt_WithInvalidCoordinates_ShouldThrowIllegalArgumentException() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new BlockImpl(scheme, 1, 1, 4, 4);
        try {
            char character = block.charAt(6, 6);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("char position (6,6) out of block.\n" + block.toString(), e.getMessage());
        }
    }
    
    @Test
    public void testTextAt_WithValidCoordinates_ShouldReturnText() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new BlockImpl(scheme, 1, 1, 4, 4);
        String text = block.textAt(2, 4, 2);
        assertEquals(scheme.lines[2].substring(2, 4), text);
    }
    
    @Test
    public void testTextAt_WithInvalidCoordinates_ShouldThrowIllegalArgumentException() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new BlockImpl(scheme, 1, 1, 4, 4);
        try {
            String text = block.textAt(5, 6, 2);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Improper subline coordinates.", e.getMessage());
        }
    }
    
    @Test
    public void testContainsOnlyOneWord_WithTrivialBlock_ShouldThrowIllegalArgumentException() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new BlockImpl(scheme);
        try {
            boolean containsOneWord = block.containsOnlyOneWord();
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("cannot search in a trivial block", e.getMessage());
        }
    }
    
    @Test
    public void testContainsOnlyOneWord_WithEmptyBlock_ShouldThrowIllegalArgumentException() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new BlockImpl(scheme, 1, 5, 2, 5);
        try {
            boolean containsOneWord = block.containsOnlyOneWord();
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("cannot verify unicity of a word in an empty block", e.getMessage());
        }
    }
    
    @Test
    public void testContainsOnlyOneWord_WithMultipleWords_ShouldReturnFalse() {
        Scheme scheme = new Scheme(7, 7);
        scheme.lines[3] = "hello world";
        Block block = new BlockImpl(scheme, 1, 3, 7, 3);
        boolean containsOneWord = block.containsOnlyOneWord();
        assertFalse(containsOneWord);
    }
    
    @Test
    public void testContainsOnlyOneWord_WithOneWord_ShouldReturnTrue() {
        Scheme scheme = new Scheme(7, 7);
        scheme.lines[3] = "hello";
        Block block = new BlockImpl(scheme, 1, 3, 7, 3);
        boolean containsOneWord = block.containsOnlyOneWord();
        assertTrue(containsOneWord);
    }
    
    @Test
    public void testExtractSequentialSubblocks_ShouldReturnSequentialSubblocks() {
        Scheme scheme = new Scheme(7, 7);
        Block block = new BlockImpl(scheme, 1, 1, 7, 7);
        List<Block> sequentialSubblocks = block.extractSequentialSubblocks();
        // TODO: Add assertions for subblocks
    }
    
    @Test
    public void testExtractParallelSubblocks_ShouldReturnParallelSubblocks() {
        Scheme scheme = new Scheme(7, 7);
        Block block = new BlockImpl(scheme, 1, 1, 7, 7);
        List<Block> parallelSubblocks = block.extractParallelSubblocks();
        // TODO: Add assertions for subblocks
    }
    
    @Test
    public void testToString_ShouldReturnStringRepresentationOfBlock() {
        Scheme scheme = new Scheme(7, 7);
        scheme.lines[2] = "line 1";
        scheme.lines[3] = "line 2";
        scheme.lines[4] = "line 3";
        Block block = new BlockImpl(scheme, 1, 2, 7, 4);
        String blockString = block.toString();
        String expectedString = "Block(1,2, 7,4):\n" +
                                " ####################\n" +
                                " # line 1           #\n" +
                                " # line 2           #\n" +
                                " # line 3           #\n" +
                                " ####################";
        assertEquals(expectedString, blockString);
    }
    
    @Test
    public void testGetUpperLine_ShouldReturnUpperLineOfBlock() {
        Scheme scheme = new Scheme(7, 7);
        scheme.lines[2] = "line";
        scheme.lines[3] = "another line";
        Block block = new BlockImpl(scheme, 1, 2, 7, 4);
        String upperLine = block.getUpperLine();
        assertEquals("line", upperLine);
    }
    
    private class BlockImpl extends Block {
        
        public BlockImpl(Scheme scheme, int x1, int y1, int x2, int y2) {
            super(scheme, x1, y1, x2, y2);
        }
        
        public BlockImpl(Scheme scheme, CharPosition p1, CharPosition p2) {
            super(scheme, p1, p2);
        }
        
        public BlockImpl(Scheme scheme) {
            super(scheme);
        }
        
        @Override
        public Block subblock(int p0, int p1, int p2, int p3) {
            return null;
        }
        
        @Override
        public List<Block> extractSequentialSubblocks() {
            return null;
        }
        
        @Override
        public List<Block> extractParallelSubblocks() {
            return null;
        }
        
    }
    
}
