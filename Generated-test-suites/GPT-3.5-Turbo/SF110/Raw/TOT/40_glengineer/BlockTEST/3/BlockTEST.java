import static org.junit.Assert.*;
import org.junit.*;
import glengineer.blocks.*;
import glengineer.positions.*;

public class BlockTest {

    private Scheme scheme;
    private Block block;

    @Before
    public void setUp() {
        scheme = new Scheme(5, 5);
    }

    @Test
    public void testBlockConstructor1() {
        try {
            block = new Block(scheme, -1, 0, 2, 2);
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("improper block definition: -1,0, 2,2.", e.getMessage());
        }
    }

    @Test
    public void testBlockConstructor2() {
        try {
            block = new Block(scheme, new CharPosition(1, 1), new CharPosition(2, 0));
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("improper block definition: 1,2, 2,0.", e.getMessage());
        }
    }

    @Test
    public void testBlockConstructor3() {
        block = new Block(scheme);
        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(5, block.x2);
        assertEquals(5, block.y2);
        assertEquals(scheme, block.scheme);
    }

    @Test
    public void testSubblock() {
        block = new Block(scheme, 1, 2, 3, 3);
        Block subblock = block.subblock(2, 3, 2, 3);
        assertEquals(2, subblock.x1);
        assertEquals(2, subblock.y1);
        assertEquals(3, subblock.x2);
        assertEquals(3, subblock.y2);
    }

    @Test
    public void testCharAt1() {
        block = new Block(scheme, 1, 1, 3, 3);
        char result = block.charAt(1, 2);
        assertEquals(' ', result);
    }

    @Test
    public void testCharAt2() {
        try {
            block = new Block(scheme, 1, 1, 3, 3);
            block.charAt(5, 4);
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("char position (5,4) out of block.\nBlock(1,1, 3,3):\n ####\n #   \n #   \n ####", e.getMessage());
        }
    }

    @Test
    public void testTextAt1() {
        block = new Block(scheme, 1, 1, 3, 3);
        String result = block.textAt(1, 3, 2);
        assertEquals("  ", result);
    }

    @Test
    public void testTextAt2() {
        try {
            block = new Block(scheme, 1, 1, 3, 3);
            block.textAt(3, 5, 2);
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Improper subline coodinates.", e.getMessage());
        }
    }

    @Test
    public void testContainsOnlyOneWord1() {
        block = new Block(scheme, 1, 1, 3, 3);
        boolean result = block.containsOnlyOneWord();
        assertFalse(result);
    }

    @Test
    public void testContainsOnlyOneWord2() {
        try {
            block = new Block(scheme, 1, 1, 3, 3);
            block.containsOnlyOneWord();
            fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("cannot verify unicity of an word in an empty block", e.getMessage());
        }
    }

    @Test
    public void testExtractSequentialSubblocks() {
        block = new Block(scheme, 1, 1, 3, 3);
        List<Block> subblocks = block.extractSequentialSubblocks();
        // Test the implementation logic for extracting sequential subblocks
        // ...
    }

    @Test
    public void testExtractParallelSubblocks() {
        block = new Block(scheme, 1, 1, 3, 3);
        List<Block> subblocks = block.extractParallelSubblocks();
        // Test the implementation logic for extracting parallel subblocks
        // ...
    }

    @Test
    public void testToString() {
        block = new Block(scheme, 1, 1, 3, 3);
        String expected = "Block(1,1, 3,3):\n ####\n #  #\n #  #\n ####";
        assertEquals(expected, block.toString());
    }

    @Test
    public void testGetUpperLine() {
        block = new Block(scheme, 1, 1, 3, 3);
        String result = block.getUpperLine();
        assertEquals(" # ", result);
    }

}