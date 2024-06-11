
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import glengineer.blocks.Block;
import glengineer.positions.*;
import glengineer.blocks.Scheme;

public class BlockTest {

    private Scheme scheme;
    private Block block;

    @Before
    public void setUp() {
        // Initialize Scheme
        scheme = new Scheme(5, 5);
        scheme.lines[0] = "ABCDE";
        scheme.lines[1] = "FGHIJ";
        scheme.lines[2] = "KLMNO";
        scheme.lines[3] = "PQRST";
        scheme.lines[4] = "UVWXY";

        // Initialize Block
        block = new Block(scheme, 0, 0, 4, 4) {
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
    public void testBlockConstructorWithCoordinates() {
        try {
            Block block = new Block(scheme, -1, 0, 4, 4);
            fail("IllegalArgumentException should be thrown when x1 < 0");
        } catch (IllegalArgumentException e) {
            assertEquals("improper block definition: -1,0, 4,4.", e.getMessage());
        }
        
        try {
            Block block = new Block(scheme, 0, -1, 4, 4);
            fail("IllegalArgumentException should be thrown when y1 < 0");
        } catch (IllegalArgumentException e) {
            assertEquals("improper block definition: 0,-1, 4,4.", e.getMessage());
        }
        
        try {
            Block block = new Block(scheme, 4, 4, 0, 0);
            fail("IllegalArgumentException should be thrown when x1 > x2");
        } catch (IllegalArgumentException e) {
            assertEquals("improper block definition: 4,4, 0,0.", e.getMessage());
        }
        
        try {
            Block block = new Block(scheme, 0, 0, 4, 5);
            fail("IllegalArgumentException should be thrown when y2 > scheme.y2");
        } catch (IllegalArgumentException e) {
            assertEquals("improper block definition: 0,0, 4,5.", e.getMessage());
        }
    }

    @Test
    public void testBlockConstructorWithCharPositions() {
        try {
            Block block = new Block(scheme, new CharPosition(-1, 0), new CharPosition(4, 4));
            fail("IllegalArgumentException should be thrown when x1 < 0");
        } catch (IllegalArgumentException e) {
            assertEquals("improper block definition: -1,0, 4,4.", e.getMessage());
        }
        
        try {
            Block block = new Block(scheme, new CharPosition(0, -1), new CharPosition(4, 4));
            fail("IllegalArgumentException should be thrown when y1 < 0");
        } catch (IllegalArgumentException e) {
            assertEquals("improper block definition: 0,-1, 4,4.", e.getMessage());
        }
        
        try {
            Block block = new Block(scheme, new CharPosition(4, 4), new CharPosition(0, 0));
            fail("IllegalArgumentException should be thrown when x1 > x2");
        } catch (IllegalArgumentException e) {
            assertEquals("improper block definition: 4,4, 0,0.", e.getMessage());
        }
        
        try {
            Block block = new Block(scheme, new CharPosition(0, 0), new CharPosition(4, 5));
            fail("IllegalArgumentException should be thrown when y2 > scheme.y2");
        } catch (IllegalArgumentException e) {
            assertEquals("improper block definition: 0,0, 4,5.", e.getMessage());
        }
    }

    @Test
    public void testBlockConstructorWithScheme() {
        Block block = new Block(scheme);
        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(5, block.x2);
        assertEquals(5, block.y2);
        assertEquals(scheme, block.scheme);
    }

    @Test
    public void testCharAt() {
        assertEquals('C', block.charAt(2, 0));
        
        try {
            block.charAt(5, 1);
            fail("IllegalArgumentException should be thrown when char position is out of block");
        } catch (IllegalArgumentException e) {
            assertEquals("char position (5, 1) out of block.\n" + block.toString(), e.getMessage());
        }
    }

    @Test
    public void testTextAt() {
        assertEquals("ABC", block.textAt(0, 3, 0));
        
        try {
            block.textAt(3, 6, 2);
            fail("IllegalArgumentException should be thrown when subline coordinates are improper");
        } catch (IllegalArgumentException e) {
            assertEquals("Improper subline coordinates.", e.getMessage());
        }
    }

    @Test
    public void testContainsOnlyOneWord() {
        try {
            block.containsOnlyOneWord();
            fail("IllegalArgumentException should be thrown when searching in a trivial block");
        } catch (IllegalArgumentException e) {
            assertEquals("cannot search in a trivial block", e.getMessage());
        }
        
        Block emptyBlock = new Block(scheme, 0, 4, 4, 4) {
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
            emptyBlock.containsOnlyOneWord();
            fail("IllegalArgumentException should be thrown when verifying unicity of word in an empty block");
        } catch (IllegalArgumentException e) {
            assertEquals("cannot verify unicity of an word in an empty block", e.getMessage());
        }

        Block nonEmptyBlock = new Block(scheme, 0, 0, 2, 0) {
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

        assertFalse(nonEmptyBlock.containsOnlyOneWord());

    }

    @Test
    public void testToString() {
        String expected = "Block(0,0, 4,4):\n" +
                " #########\n" +
                " # ABCDE #\n" +
                " # FGHIJ #\n" +
                " # KLMNO #\n" +
                " # PQRST #\n" +
                " #########";
        assertEquals(expected, block.toString());
    }

    @Test
    public void testGetUpperLine() {
        assertEquals("ABCDE", block.getUpperLine());
    }

    // Additional tests for abstract methods subblock, extractSequentialSubblocks and extractParallelSubblocks should be added by subclasses.

}

