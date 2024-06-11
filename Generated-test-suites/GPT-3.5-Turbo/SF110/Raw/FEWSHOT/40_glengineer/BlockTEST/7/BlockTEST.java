import glengineer.blocks.*;
import glengineer.positions.*;
import java.util.*;

public class BlockTest {

    @Test
    public void testConstructorWithCoordinates() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 1, 4, 4);

        assertEquals(1, block.x1);
        assertEquals(1, block.y1);
        assertEquals(4, block.x2);
        assertEquals(4, block.y2);
        assertSame(scheme, block.scheme);
    }

    @Test
    public void testConstructorWithCharPositions() {
        Scheme scheme = new Scheme(5, 5);
        CharPosition p1 = new CharPosition(1, 1);
        CharPosition p2 = new CharPosition(4, 4);
        Block block = new Block(scheme, p1, p2);

        assertEquals(1, block.x1);
        assertEquals(1, block.y1);
        assertEquals(4, block.x2);
        assertEquals(4, block.y2);
        assertSame(scheme, block.scheme);
    }

    @Test
    public void testConstructorWithoutCoordinates() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme);

        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(5, block.x2);
        assertEquals(5, block.y2);
        assertSame(scheme, block.scheme);
    }

    @Test
    public void testSubblock() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new Block(scheme, 1, 1, 4, 4);
        Block subblock = block.subblock(2, 2, 3, 3);

        assertEquals(2, subblock.x1);
        assertEquals(2, subblock.y1);
        assertEquals(3, subblock.x2);
        assertEquals(3, subblock.y2);

        Block emptyBlock = new Block(scheme);
        Block emptySubblock = emptyBlock.subblock(0, 0, 0, 0);

        assertEquals(0, emptySubblock.x1);
        assertEquals(0, emptySubblock.y1);
        assertEquals(0, emptySubblock.x2);
        assertEquals(0, emptySubblock.y2);
    }

    @Test
    public void testCharAt() {
        Scheme scheme = new Scheme(5, 5);
        scheme.lines = new String[]{"ABCDE", "FGHIJ", "KLMNO", "PQRST", "UVWXY"};
        Block block = new Block(scheme, 1, 1, 4, 4);

        assertEquals('H', block.charAt(2, 1));
        assertEquals('M', block.charAt(2, 2));
        assertEquals('I', block.charAt(3, 1));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> block.charAt(5, 5)
        );
        assertEquals("char position (5,5) out of block.\n" + block, exception.getMessage());
    }

    @Test
    public void testTextAt() {
        Scheme scheme = new Scheme(5, 5);
        scheme.lines = new String[]{"ABCDE", "FGHIJ", "KLMNO", "PQRST", "UVWXY"};
        Block block = new Block(scheme, 1, 1, 4, 4);

        assertEquals("FG", block.textAt(2, 4, 1));
        assertEquals("KL", block.textAt(2, 4, 2));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> block.textAt(5, 5, 5)
        );
        assertEquals("Improper subline coodinates.", exception.getMessage());
    }

    @Test
    public void testContainsOnlyOneWord() {
        Scheme scheme = new Scheme(5, 5);
        scheme.lines = new String[]{"ABCDE", "FGHIJ", "KLMNO", "PQQRST", "UVWXY"};
        Block block = new Block(scheme, 2, 2, 3, 3);

        assertFalse(block.containsOnlyOneWord());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> block.containsOnlyOneWord()
        );
        assertEquals("cannot search in a trivial block", exception.getMessage());

        Block emptyBlock = new Block(scheme);
        exception = assertThrows(
                IllegalArgumentException.class,
                () -> emptyBlock.containsOnlyOneWord()
        );
        assertEquals("cannot verify unicity of a word in an empty block", exception.getMessage());
    }

    @Test
    public void testExtractSequentialSubblocks() {
        Scheme scheme = new Scheme(5, 5);
        scheme.lines = new String[]{"ABCDE", "FGHIJ", "KLMNO", "PQRST", "UVWXY"};
        Block block = new Block(scheme, 1, 1, 4, 3);

        List<Block> expectedSubblocks = new ArrayList<>();
        expectedSubblocks.add(new Block(scheme, 1, 1, 4, 2));
        expectedSubblocks.add(new Block(scheme, 1, 2, 4, 3));

        List<Block> subblocks = block.extractSequentialSubblocks();

        assertEquals(expectedSubblocks.size(), subblocks.size());
        assertTrue(subblocks.containsAll(expectedSubblocks));
    }

    @Test
    public void testExtractParallelSubblocks() {
        Scheme scheme = new Scheme(5, 5);
        scheme.lines = new String[]{"ABCDE", "FGHIJ", "KLMNO", "PQTIR", "UVWSY"};
        Block block = new Block(scheme, 1, 1, 4, 4);

        List<Block> expectedSubblocks = new ArrayList<>();
        expectedSubblocks.add(new Block(scheme, 1, 1, 4, 1));
        expectedSubblocks.add(new Block(scheme, 1, 2, 4, 2));
        expectedSubblocks.add(new Block(scheme, 1, 3, 4, 3));
        expectedSubblocks.add(new Block(scheme, 1, 4, 4, 4));

        List<Block> subblocks = block.extractParallelSubblocks();

        assertEquals(expectedSubblocks.size(), subblocks.size());
        assertTrue(subblocks.containsAll(expectedSubblocks));
    }

    @Test
    public void testToString() {
        Scheme scheme = new Scheme(5, 5);
        scheme.lines = new String[]{"ABCDE", "FGHIJ", "KLMNO", "PQRST", "UVWXY"};
        Block block = new Block(scheme, 1, 1, 4, 4);

        String expectedString = "Block(1,1, 4,4):\n" +
                " ###### \n" +
                " #FGHIJ# \n" +
                " #KLMNO# \n" +
                " #PQRST# \n" +
                " ###### ";

        assertEquals(expectedString, block.toString());
    }

    @Test
    public void testGetUpperLine() {
        Scheme scheme = new Scheme(5, 5);
        scheme.lines = new String[]{"ABCDE", "FGHIJ", "KLMNO", "PQRZT", "UVWXY"};
        Block block = new Block(scheme, 1, 1, 4, 4);

        assertEquals("JKLM", block.getUpperLine());
    }

}