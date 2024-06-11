
package glengineer.blocks;

import glengineer.positions.*;
import java.util.*;

import static org.junit.Assert.*;
import org.junit.*;

public class BlockTest {
    private Scheme scheme;
    private Block block;

    @Before
    public void setUp() {
        scheme = new Scheme("test");
        block = new Block(scheme, 0, 0, 1, 1);
    }

    @Test
    public void testSubblock() {
        Block subblock = block.subblock(0, 0, 1, 1);
        assertEquals(block.x1, subblock.x1);
        assertEquals(block.y1, subblock.y1);
        assertEquals(block.x2, subblock.x2);
        assertEquals(block.y2, subblock.y2);
        assertEquals(block.scheme, subblock.scheme);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubblockInvalidParameters() {
        block.subblock(2, 2, 3, 3);
    }

    @Test
    public void testCharAt() {
        scheme.lines[0] = "ab";
        scheme.lines[1] = "cd";
        assertEquals('a', block.charAt(0, 0));
        assertEquals('b', block.charAt(1, 0));
        assertEquals('c', block.charAt(0, 1));
        assertEquals('d', block.charAt(1, 1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCharAtOutOfBounds() {
        scheme.lines[0] = "ab";
        scheme.lines[1] = "cd";
        block.charAt(2, 2);
    }

    @Test
    public void testTextAt() {
        scheme.lines[0] = "abcd";
        assertEquals("ab", block.textAt(0, 2, 0));
        assertEquals("cd", block.textAt(2, 4, 0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTextAtInvalidCoordinates() {
        scheme.lines[0] = "abcd";
        block.textAt(0, 5, 0);
    }

    @Test
    public void testContainsOnlyOneWord() {
        scheme.lines[0] = "hello world";
        scheme.lines[1] = "hello world";
        assertTrue(block.containsOnlyOneWord());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWordWithTrivialBlock() {
        block.containsOnlyOneWord();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWordEmptyBlock() {
        scheme.lines[0] = "";
        scheme.lines[1] = "";
        block.containsOnlyOneWord();
    }

    @Test
    public void testExtractSequentialSubblocks() {
        List<Block> subblocks = block.extractSequentialSubblocks();
        assertEquals(1, subblocks.size());
        assertEquals(block, subblocks.get(0));
    }

    @Test
    public void testExtractParallelSubblocks() {
        List<Block> subblocks = block.extractParallelSubblocks();
        assertEquals(1, subblocks.size());
        assertEquals(block, subblocks.get(0));
    }

    @Test
    public void testToString() {
        scheme.lines[0] = "abcd";
        assertEquals("Block(0,0, 1,1):\n ##\n # ab #\n ##", block.toString());
    }

    @Test
    public void testGetUpperLine() {
        scheme.lines[0] = "abcd";
        assertEquals("ab", block.getUpperLine());
    }
}
