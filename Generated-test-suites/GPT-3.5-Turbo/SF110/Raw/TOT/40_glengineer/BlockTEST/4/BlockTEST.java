import org.junit.*;
import static org.junit.Assert.*;
import glengineer.positions.*;
import java.util.*;

public class BlockTest {
    
    private Scheme scheme;
    private Block block;

    @Before
    public void setUp() {
        scheme = new Scheme(5, 5);
        block = new BlockImpl(scheme, 1, 1, 3, 3);
    }

    // Test cases for Block constructor

    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructorInvalidArguments() {
        Block invalidBlock = new BlockImpl(scheme, 4, 3, 1, 2);
    }

    @Test
    public void testBlockConstructorValidArguments() {
        Block validBlock = new BlockImpl(scheme, 1, 1, 3, 3);
        assertEquals(1, validBlock.getX1());
        assertEquals(1, validBlock.getY1());
        assertEquals(3, validBlock.getX2());
        assertEquals(3, validBlock.getY2());
        assertEquals(scheme, validBlock.getScheme());
    }

    // Test cases for subblock() method

    @Test
    public void testSubblockValidArguments() {
        Block subBlock = block.subblock(1, 1, 2, 2);
        assertEquals(1, subBlock.getX1());
        assertEquals(1, subBlock.getY1());
        assertEquals(2, subBlock.getX2());
        assertEquals(2, subBlock.getY2());
        assertEquals(scheme, subBlock.getScheme());
    }

    // Test cases for charAt() method

    @Test(expected = IllegalArgumentException.class)
    public void testCharAtOutOfBounds() {
        block.charAt(4, 4);
    }

    @Test
    public void testCharAtValidPosition() {
        char result = block.charAt(2, 2);
        assertEquals('#', result);
    }

    // Test cases for textAt() method

    @Test(expected = IllegalArgumentException.class)
    public void testTextAtInvalidCoordinates() {
        block.textAt(2, 1, 2);
    }

    @Test
    public void testTextAtValidCoordinates() {
        String result = block.textAt(1, 3, 1);
        assertEquals("###", result);
    }

    // Test cases for containsOnlyOneWord() method

    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWordInTrivialBlock() {
        Block trivialBlock = new BlockImpl(scheme);
        trivialBlock.containsOnlyOneWord();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWordInEmptyBlock() {
        Block emptyBlock = new BlockImpl(scheme, 1, 5, 3, 5);
        emptyBlock.containsOnlyOneWord();
    }

    @Test
    public void testContainsOnlyOneWordValid() {
        boolean result = block.containsOnlyOneWord();
        assertFalse(result);
    }

    // Test cases for extractSequentialSubblocks() method

    @Test
    public void testExtractSequentialSubblocksNoSubblocks() {
        List<Block> subblocks = block.extractSequentialSubblocks();
        assertTrue(subblocks.isEmpty());
    }

    // Test cases for extractParallelSubblocks() method

    @Test
    public void testExtractParallelSubblocksNoSubblocks() {
        List<Block> subblocks = block.extractParallelSubblocks();
        assertTrue(subblocks.isEmpty());
    }

    // Test cases for toString() method

    @Test
    public void testToString() {
        String result = block.toString();
        String expected = "Block(1,1, 3,3):\n #### \n #### \n # ###\n #### ";
        assertEquals(expected, result);
    }

    // Test cases for getUpperLine() method

    @Test
    public void testGetUpperLine() {
        String result = block.getUpperLine();
        assertEquals("###", result);
    }
}