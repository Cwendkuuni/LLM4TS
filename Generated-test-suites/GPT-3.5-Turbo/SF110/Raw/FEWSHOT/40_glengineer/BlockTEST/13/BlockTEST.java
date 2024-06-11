package glengineer.blocks;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;

import glengineer.positions.CharPosition;
import glengineer.blocks.Block.Scheme;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class BlockTest {

    private Scheme scheme;
    private Block block;

    @Before
    public void setUp() {
        scheme = new Scheme();
        block = new Block(scheme, 0, 0, 5, 5);
    }

    @Test
    public void testConstructorWithCoordinates() {
        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(5, block.x2);
        assertEquals(5, block.y2);
    }

    @Test
    public void testConstructorWithPositions() {
        CharPosition p1 = new CharPosition(0, 0);
        CharPosition p2 = new CharPosition(5, 5);
        block = new Block(scheme, p1, p2);

        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(5, block.x2);
        assertEquals(5, block.y2);
    }

    @Test
    public void testConstructorWithScheme() {
        block = new Block(scheme);

        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(scheme.x2, block.x2);
        assertEquals(scheme.y2, block.y2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidCoordinates() {
        block = new Block(scheme, 0, 0, -1, 5);
    }

    @Test
    public void testSubblock() {
        Block subblock = block.subblock(1, 1, 4, 4);

        assertEquals(1, subblock.x1);
        assertEquals(1, subblock.y1);
        assertEquals(4, subblock.x2);
        assertEquals(4, subblock.y2);
        assertNotSame(block, subblock);
    }

    @Test
    public void testCharAt() {
        scheme.lines[0] = "abcde";
        scheme.lines[1] = "fghij";
        scheme.lines[2] = "klmno";

        assertEquals('a', block.charAt(0, 0));
        assertEquals('b', block.charAt(1, 0));
        assertEquals('e', block.charAt(4, 0));
        assertEquals('f', block.charAt(0, 1));
        assertEquals('j', block.charAt(4, 1));
        assertEquals('k', block.charAt(0, 2));
        assertEquals('o', block.charAt(4, 2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCharAtOutOfBounds() {
        scheme.lines[0] = "abcde";

        block.charAt(5, 0);
    }

    @Test
    public void testTextAt() {
        scheme.lines[0] = "abcde";
        scheme.lines[1] = "fghij";
        scheme.lines[2] = "klmno";

        assertEquals("abc", block.textAt(0, 3, 0));
        assertEquals("fgh", block.textAt(0, 3, 1));
        assertEquals("klm", block.textAt(0, 3, 2));
        assertEquals("bcd", block.textAt(1, 4, 0));
        assertEquals("ghi", block.textAt(1, 4, 1));
        assertEquals("lmn", block.textAt(1, 4, 2));
        assertEquals("cde", block.textAt(2, 5, 0));
        assertEquals("hij", block.textAt(2, 5, 1));
        assertEquals("mno", block.textAt(2, 5, 2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTextAtOutOfBounds() {
        scheme.lines[0] = "abcde";

        block.textAt(0, 6, 0);
    }

    @Test
    public void testContainsOnlyOneWord() {
        scheme.lines[0] = "abcde";
        scheme.lines[1] = "fghij";
        scheme.lines[2] = "klmno";

        assertFalse(block.containsOnlyOneWord());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWordWithTrivialBlock() {
        block.containsOnlyOneWord();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWordWithEmptyBlock() {
        scheme.lines[0] = "";

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
        scheme.lines[0] = "abcde";
        scheme.lines[1] = "fghij";
        scheme.lines[2] = "klmno";

        String expected = "Block(0,0, 5,5):\n"
                + " #############\n"
                + " # abcde     #\n"
                + " # fghij     #\n"
                + " # klmno     #\n"
                + " #############";

        assertEquals(expected, block.toString());
    }

    @Test
    public void testGetUpperLine() {
        scheme.lines[0] = "abcde";
        assertEquals("abcde", block.getUpperLine());

        scheme.lines[0] = "fghij";
        assertEquals("fghij", block.getUpperLine());

        scheme.lines[0] = "klmno";
        assertEquals("klmno", block.getUpperLine());
    }
}