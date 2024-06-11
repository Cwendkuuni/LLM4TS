import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class BlockTest {
    private Scheme scheme;
    private Block block;

    @Before
    public void setUp() {
        scheme = new Scheme();
        block = new BlockImpl(scheme, 0, 0, 5, 5);
    }

    @Test
    public void testBlockConstructor() {
        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(5, block.x2);
        assertEquals(5, block.y2);
        assertEquals(scheme, block.scheme);
    }

    @Test
    public void testBlockConstructorWithCharPosition() {
        CharPosition p1 = new CharPosition(0, 0);
        CharPosition p2 = new CharPosition(5, 5);
        block = new BlockImpl(scheme, p1, p2);

        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(5, block.x2);
        assertEquals(5, block.y2);
        assertEquals(scheme, block.scheme);
    }

    @Test
    public void testBlockConstructorWithDefaultScheme() {
        block = new BlockImpl(scheme);

        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(scheme.x2, block.x2);
        assertEquals(scheme.y2, block.y2);
        assertEquals(scheme, block.scheme);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructorWithInvalidCoordinates() {
        new BlockImpl(scheme, 0, 0, -1, 5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructorWithInvalidCoordinates2() {
        new BlockImpl(scheme, 0, 0, 5, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructorWithInvalidCoordinates3() {
        new BlockImpl(scheme, -1, 0, 5, 5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructorWithInvalidCoordinates4() {
        new BlockImpl(scheme, 0, -1, 5, 5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructorWithInvalidCoordinates5() {
        new BlockImpl(scheme, 5, 0, 4, 5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructorWithInvalidCoordinates6() {
        new BlockImpl(scheme, 0, 5, 5, 4);
    }

    @Test
    public void testSubblock() {
        Block subblock = block.subblock(1, 1, 4, 4);

        assertEquals(1, subblock.x1);
        assertEquals(1, subblock.y1);
        assertEquals(4, subblock.x2);
        assertEquals(4, subblock.y2);
        assertEquals(scheme, subblock.scheme);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubblockWithInvalidCoordinates() {
        block.subblock(-1, 1, 4, 4);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubblockWithInvalidCoordinates2() {
        block.subblock(1, 1, 6, 4);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubblockWithInvalidCoordinates3() {
        block.subblock(1, -1, 4, 4);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubblockWithInvalidCoordinates4() {
        block.subblock(1, 1, 4, 6);
    }

    @Test
    public void testCharAt() {
        scheme.lines = new String[] { "abcde", "fghij", "klmno", "pqrst", "uvwxy" };

        assertEquals('a', block.charAt(0, 0));
        assertEquals('b', block.charAt(1, 0));
        assertEquals('f', block.charAt(0, 1));
        assertEquals('p', block.charAt(0, 3));
        assertEquals('y', block.charAt(4, 4));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCharAtWithInvalidCoordinates() {
        block.charAt(6, 5);
    }

    @Test
    public void testTextAt() {
        scheme.lines = new String[] { "abcde", "fghij", "klmno", "pqrst", "uvwxy" };

        assertEquals("abcd", block.textAt(0, 4, 0));
        assertEquals("ij", block.textAt(3, 5, 1));
        assertEquals("rst", block.textAt(1, 4, 3));
        assertEquals("wxy", block.textAt(2, 5, 4));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTextAtWithInvalidCoordinates() {
        block.textAt(6, 5, 0);
    }

    @Test
    public void testContainsOnlyOneWord() {
        scheme.lines = new String[] { "abcde", "fghij", "klmno", "pqrst", "uvwxy" };

        assertFalse(block.containsOnlyOneWord());

        scheme.lines = new String[] { "abcd", "abcd", "abcd", "abcd", "abcd" };

        assertTrue(block.containsOnlyOneWord());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWordWithTrivialBlock() {
        block.containsOnlyOneWord();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWordWithEmptyBlock() {
        scheme.lines = new String[] { "abcde" };

        block.containsOnlyOneWord();
    }

    @Test
    public void testExtractSequentialSubblocks() {
        scheme.lines = new String[] { "abcde", "fghij", "klmno", "pqrst", "uvwxy" };

        List<Block> subblocks = block.extractSequentialSubblocks();

        assertEquals(5, subblocks.size());
        assertEquals("a", subblocks.get(0).getUpperLine());
        assertEquals("b", subblocks.get(1).getUpperLine());
        assertEquals("c", subblocks.get(2).getUpperLine());
        assertEquals("d", subblocks.get(3).getUpperLine());
        assertEquals("e", subblocks.get(4).getUpperLine());
    }

    @Test
    public void testExtractSequentialSubblocksWithEmptyBlock() {
        scheme.lines = new String[] { "abcde" };

        List<Block> subblocks = block.extractSequentialSubblocks();

        assertEquals(1, subblocks.size());
        assertEquals("abcde", subblocks.get(0).getUpperLine());
    }

    @Test
    public void testExtractParallelSubblocks() {
        scheme.lines = new String[] { "abcde", "fghij", "klmno", "pqrst", "uvwxy" };

        List<Block> subblocks = block.extractParallelSubblocks();

        assertEquals(5, subblocks.size());
        assertEquals("afkp", subblocks.get(0).getUpperLine());
        assertEquals("bgjlq", subblocks.get(1).getUpperLine());
        assertEquals("chmru", subblocks.get(2).getUpperLine());
        assertEquals("dinos", subblocks.get(3).getUpperLine());
        assertEquals("ejptv", subblocks.get(4).getUpperLine());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExtractParallelSubblocksWithEmptyBlock() {
        scheme.lines = new String[] { "abcde" };

        block.extractParallelSubblocks();
    }

    @Test
    public void testToString() {
        scheme.lines = new String[] { "abcde", "fghij", "klmno", "pqrst", "uvwxy" };

        String expected = "Block(0,0, 5,5):\n";
        expected += " ######\n";
        expected += " # abcde #\n";
        expected += " # fghij #\n";
        expected += " # klmno #\n";
        expected += " # pqrst #\n";
        expected += " # uvwxy #\n";
        expected += " ######";

        assertEquals(expected, block.toString());
    }

    @Test
    public void testGetUpperLine() {
        scheme.lines = new String[] { "abcde", "fghij", "klmno", "pqrst", "uvwxy" };

        assertEquals("abcde", block.getUpperLine());
    }
}