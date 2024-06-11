import org.junit.*;
import static org.junit.Assert.*;

public class BlockTest {

    private Scheme scheme;

    @Before
    public void setUp() {
        scheme = new Scheme(5, 5);
    }

    @Test
    public void testBlockConstructor() {
        try {
            Block block = new Block(scheme, 1, 1, 0, 2);
            fail("IllegalArgumentException should be thrown for improper block definition");
        } catch (IllegalArgumentException e) {
            assertEquals("improper block definition: 1,1, 0,2.", e.getMessage());
        }
    }

    @Test
    public void testBlockConstructorWithCharPositions() {
        CharPosition p1 = new CharPosition(1, 1);
        CharPosition p2 = new CharPosition(0, 2);

        try {
            Block block = new Block(scheme, p1, p2);
            fail("IllegalArgumentException should be thrown for improper block definition");
        } catch (IllegalArgumentException e) {
            assertEquals("improper block definition: 1,1, 0,2.", e.getMessage());
        }
    }

    @Test
    public void testBlockDefaultConstructor() {
        Block block = new Block(scheme);

        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(scheme.x2, block.x2);
        assertEquals(scheme.y2, block.y2);
    }

    @Test
    public void testSubblock() {
        Block block = new Block(scheme);

        Block subblock = block.subblock(1, 1, 3, 3);

        assertEquals(1, subblock.x1);
        assertEquals(1, subblock.y1);
        assertEquals(3, subblock.x2);
        assertEquals(3, subblock.y2);
    }

    @Test
    public void testCharAt() {
        Block block = new Block(scheme);
        scheme.lines[0] = "ABCDE";
        scheme.lines[1] = "FGHIJ";
        scheme.lines[2] = "KLMNO";
        scheme.lines[3] = "PQRST";
        scheme.lines[4] = "UVWXY";

        assertEquals('A', block.charAt(0, 0));
        assertEquals('D', block.charAt(3, 0));
        assertEquals('V', block.charAt(0, 4));
        assertEquals('Y', block.charAt(4, 4));
    }

    @Test
    public void testCharAtOutOfBounds() {
        Block block = new Block(scheme);
        scheme.lines[0] = "ABCDE";
        scheme.lines[1] = "FGHIJ";

        try {
            block.charAt(5, 0);
            fail("IllegalArgumentException should be thrown for char position out of block");
        } catch (IllegalArgumentException e) {
            assertEquals("char position (5,0) out of block.\nBlock(0,0, 5,5):\n #####\n # ABCD #\n # FGHI #\n #####", e.getMessage());
        }
    }

    @Test
    public void testTextAt() {
        Block block = new Block(scheme);
        scheme.lines[0] = "ABCDE";
        scheme.lines[1] = "FGHIJ";
        scheme.lines[2] = "KLMNO";
        scheme.lines[3] = "PQRST";
        scheme.lines[4] = "UVWXY";

        assertEquals("ABCDE", block.textAt(0, 5, 0));
        assertEquals("HIJ", block.textAt(2, 5, 1));
        assertEquals("U", block.textAt(0, 1, 4));
        assertEquals("XY", block.textAt(3, 5, 4));
    }

    @Test
    public void testTextAtOutOfBounds() {
        Block block = new Block(scheme);
        scheme.lines[0] = "ABCDEF";

        try {
            block.textAt(1, 7, 0);
            fail("IllegalArgumentException should be thrown for improper subline coodinates");
        } catch (IllegalArgumentException e) {
            assertEquals("Improper subline coodinates.", e.getMessage());
        }
    }

    @Test
    public void testContainsOnlyOneWord() {
        Block block = new Block(scheme);
        scheme.lines[0] = "ABC DEF";
        scheme.lines[1] = "GHI JKL";
        scheme.lines[2] = "MNO PQR";
        scheme.lines[3] = "STU VWX";

        assertFalse(block.containsOnlyOneWord());

        scheme.lines[0] = "ABC DEF";
        scheme.lines[1] = "GHI JKL";
        scheme.lines[2] = "MNO PQR";
        scheme.lines[3] = "STU VWX";
        scheme.lines[4] = "YZA BCD";

        assertTrue(block.containsOnlyOneWord());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testContainsOnlyOneWordInTrivialBlock() {
        Block block = new Block(scheme);

        block.containsOnlyOneWord();
    }

    @Test(expected=IllegalArgumentException.class)
    public void testContainsOnlyOneWordInEmptyBlock() {
        Block block = new Block(scheme);
        block.y2 = 0;

        block.containsOnlyOneWord();
    }

    @Test
    public void testExtractSequentialSubblocks() {
        Block block = new Block(scheme);
        scheme.lines[0] = "ABC DEF";
        scheme.lines[1] = "GHI JKL";
        scheme.lines[2] = "MNO PQR";
        scheme.lines[3] = "STU VWX";

        List<Block> subblocks = block.extractSequentialSubblocks();

        assertEquals(4, subblocks.size());
        assertEquals("ABC", subblocks.get(0).getUpperLine());
        assertEquals("DEF", subblocks.get(1).getUpperLine());
        assertEquals("GHI", subblocks.get(2).getUpperLine());
        assertEquals("JKL", subblocks.get(3).getUpperLine());
    }

    @Test
    public void testExtractParallelSubblocks() {
        Block block = new Block(scheme);
        scheme.lines[0] = "ABCD";
        scheme.lines[1] = "EFGH";
        scheme.lines[2] = "IJKL";
        scheme.lines[3] = "MNOP";

        List<Block> subblocks = block.extractParallelSubblocks();

        assertEquals(4, subblocks.size());
        assertEquals("A", subblocks.get(0).getUpperLine());
        assertEquals("B", subblocks.get(1).getUpperLine());
        assertEquals("C", subblocks.get(2).getUpperLine());
        assertEquals("D", subblocks.get(3).getUpperLine());
    }

    @Test
    public void testToString() {
        Block block = new Block(scheme);
        scheme.lines[0] = "ABCDEF";
        scheme.lines[1] = "GHIJKL";
        scheme.lines[2] = "MNOPQR";
        scheme.lines[3] = "STUVWX";
        scheme.lines[4] = "YZABCD";

        assertEquals("Block(0,0, 5,5):\n" +
                " #####\n" +
                " # ABCD #\n" +
                " # GHIJ #\n" +
                " # MNOP #\n" +
                " # STUV #\n" +
                " #####", block.toString());
    }

    @Test
    public void testGetUpperLine() {
        Block block = new Block(scheme);
        scheme.lines[0] = "ABCDEFG";

        assertEquals("ABCDEFG", block.getUpperLine());
    }

}