import glengineer.positions.CharPosition;
import glengineer.positions.WordPosition;
import org.junit.Test;
import static org.junit.Assert.*;

public class BlockTest {

    @Test
    public void testSubblock() {
        Scheme scheme = new Scheme();
        Block block = new ConcreteBlock(scheme, 0, 0, 2, 2);
        Block subblock = block.subblock(1, 1, 2, 2);
        assertEquals(block.scheme, subblock.scheme);
        assertEquals(1, subblock.x1);
        assertEquals(1, subblock.y1);
        assertEquals(2, subblock.x2);
        assertEquals(2, subblock.y2);
    }

    @Test
    public void testCharAt() {
        Scheme scheme = new Scheme();
        Block block = new ConcreteBlock(scheme, 0, 0, 3, 3);
        scheme.lines[0] = "abcd";
        scheme.lines[1] = "efgh";
        scheme.lines[2] = "ijkl";
        scheme.lines[3] = "mnop";
        assertEquals('a', block.charAt(0, 0));
        assertEquals('h', block.charAt(3, 1));
        assertEquals('k', block.charAt(2, 2));
        try {
            block.charAt(4, 4);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("char position (4,4) out of block.\nBlock(0,0, 3,3):\n ####\n #abc#\n #efg#\n #ijk#\n ####", e.getMessage());
        }
    }

    @Test
    public void testTextAt() {
        Scheme scheme = new Scheme();
        Block block = new ConcreteBlock(scheme, 0, 0, 4, 4);
        scheme.lines[0] = "abcdef";
        scheme.lines[1] = "ghijkl";
        scheme.lines[2] = "mnopqr";
        scheme.lines[3] = "stuvwx";
        assertEquals("abc", block.textAt(0, 3, 0));
        assertEquals("ijk", block.textAt(2, 5, 1));
        assertEquals("opqr", block.textAt(2, 6, 2));
        try {
            block.textAt(6, 7, 3);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Improper subline coodinates.", e.getMessage());
        }
    }

    @Test
    public void testContainsOnlyOneWord() {
        Scheme scheme = new Scheme();
        Block block1 = new ConcreteBlock(scheme, 0, 0, 2, 2);
        assertFalse(block1.containsOnlyOneWord());

        Block block2 = new ConcreteBlock(scheme);
        try {
            block2.containsOnlyOneWord();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("cannot search in a trivial block", e.getMessage());
        }

        Block block3 = new ConcreteBlock(scheme, 0, 0, 3, 3);
        scheme.lines[0] = "abcd";
        scheme.lines[1] = "a    ";
        scheme.lines[2] = "abcd";
        scheme.lines[3] = "efgh";
        assertTrue(block3.containsOnlyOneWord());
    }

    @Test
    public void testExtractSequentialSubblocks() {
        Scheme scheme = new Scheme();
        Block block = new ConcreteBlock(scheme, 0, 0, 4, 4);
        List<Block> subblocks = block.extractSequentialSubblocks();
        assertEquals(20, subblocks.size());
        assertEquals("a", subblocks.get(0).getUpperLine());
        assertEquals("ef", subblocks.get(9).getUpperLine());
        assertEquals("wr", subblocks.get(19).getUpperLine());
    }

    @Test
    public void testExtractParallelSubblocks() {
        Scheme scheme = new Scheme();
        Block block = new ConcreteBlock(scheme, 0, 0, 4, 4);
        List<Block> subblocks = block.extractParallelSubblocks();
        assertEquals(20, subblocks.size());
        assertEquals("crov", subblocks.get(0).getUpperLine());
        assertEquals("crov", subblocks.get(9).getUpperLine());
        assertEquals("crov", subblocks.get(19).getUpperLine());
    }

    @Test
    public void testToString() {
        Scheme scheme = new Scheme();
        Block block = new ConcreteBlock(scheme, 0, 0, 4, 4);
        scheme.lines[0] = "abc";
        scheme.lines[1] = "def";
        scheme.lines[2] = "ghi";
        scheme.lines[3] = "jkl";
        assertEquals("Block(0,0, 4,4):\n" +
                " ####\n" +
                " #abc#\n" +
                " #def#\n" +
                " #ghi#\n" +
                " ####", block.toString());
    }

    private class ConcreteBlock extends Block {

        public ConcreteBlock(Scheme scheme, int x1, int y1, int x2, int y2) {
            super(scheme, x1, y1, x2, y2);
        }

        public Block subblock(int p0, int p1, int p2, int p3) {
            return null;
        }

        public List<Block> extractSequentialSubblocks() {
            return null;
        }

        public List<Block> extractParallelSubblocks() {
            return null;
        }
    }
}