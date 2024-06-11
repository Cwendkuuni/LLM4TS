import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class BlockTest
{
    private Block block;
    private Scheme scheme;
    
    @Before
    public void setUp() {
        scheme = new Scheme(10, 10);
        block = new Block(scheme, 1, 1, 5, 5) {
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
        };
    }
    
    @Test
    public void testCharAt() {
        assertEquals('#', block.charAt(0, 0));
        assertEquals(' ', block.charAt(1, 1));
        assertEquals(' ', block.charAt(1, 2));
        assertEquals(' ', block.charAt(2, 3));
        assertEquals(' ', block.charAt(5, 5));
        try {
            block.charAt(-1, 0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("char position CharPosition [-1, 0] out of block.\nBlock(1,1, 5,5):\n ######\n #    #\n #    #\n #    #\n #    #\n #    #\n ######", e.getMessage());
        }
        try {
            block.charAt(6, 0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("char position CharPosition [6, 0] out of block.\nBlock(1,1, 5,5):\n ######\n #    #\n #    #\n #    #\n #    #\n #    #\n ######", e.getMessage());
        }
        try {
            block.charAt(0, -1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("char position CharPosition [0, -1] out of block.\nBlock(1,1, 5,5):\n ######\n #    #\n #    #\n #    #\n #    #\n #    #\n ######", e.getMessage());
        }
        try {
            block.charAt(0, 6);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("char position CharPosition [0, 6] out of block.\nBlock(1,1, 5,5):\n ######\n #    #\n #    #\n #    #\n #    #\n #    #\n ######", e.getMessage());
        }
    }
    
    @Test
    public void testTextAt() {
        assertEquals("#    ", block.textAt(0, 5, 0));
        assertEquals(" #   ", block.textAt(0, 5, 1));
        assertEquals("  #  ", block.textAt(0, 5, 2));
        assertEquals("   # ", block.textAt(0, 5, 3));
        assertEquals("    #", block.textAt(0, 5, 4));
        try {
            block.textAt(-1, 5, 0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Improper subline coodinates.", e.getMessage());
        }
        try {
            block.textAt(0, 6, 0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Improper subline coodinates.", e.getMessage());
        }
        try {
            block.textAt(0, 5, -1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Improper subline coodinates.", e.getMessage());
        }
        try {
            block.textAt(0, 5, 5);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Improper subline coodinates.", e.getMessage());
        }
    }
    
    @Test
    public void testContainsOnlyOneWord() {
        assertFalse(block.containsOnlyOneWord());
        block = new Block(scheme, 0, 0, 1, 1) {
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
        };
        try {
            block.containsOnlyOneWord();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("cannot verify unicity of an word in an empty block", e.getMessage());
        }
    }
    
    @Test
    public void testToString() {
        assertEquals("Block(1,1, 5,5):\n ######\n #    #\n #    #\n #    #\n #    #\n #    #\n ######", block.toString());
    }
    
    @Test
    public void testGetUpperLine() {
        assertEquals("#    ", block.getUpperLine());
    }
}