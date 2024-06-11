import glengineer.blocks.Block;
import glengineer.positions.CharPosition;
import glengineer.positions.HWordPosition;
import glengineer.positions.Scheme;
import glengineer.positions.WordPosition;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class BlockTest {

    @Test
    public void testBlockConstructorWithCoordinates() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new BlockImpl(scheme, 1, 1, 5, 5);

        assertEquals(scheme, block.getScheme());
        assertEquals(1, block.getX1());
        assertEquals(1, block.getY1());
        assertEquals(5, block.getX2());
        assertEquals(5, block.getY2());
    }

    @Test
    public void testBlockConstructorWithCharPositions() {
        Scheme scheme = new Scheme(10, 10);
        CharPosition p1 = new CharPosition(1, 1);
        CharPosition p2 = new CharPosition(5, 5);
        Block block = new BlockImpl(scheme, p1, p2);

        assertEquals(scheme, block.getScheme());
        assertEquals(1, block.getX1());
        assertEquals(1, block.getY1());
        assertEquals(5, block.getX2());
        assertEquals(5, block.getY2());
    }

    @Test
    public void testBlockConstructorWithoutCoordinates() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new BlockImpl(scheme);

        assertEquals(scheme, block.getScheme());
        assertEquals(0, block.getX1());
        assertEquals(0, block.getY1());
        assertEquals(10, block.getX2());
        assertEquals(10, block.getY2());
    }

    @Test
    public void testSubblock() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new BlockImpl(scheme, 1, 1, 5, 5);
        Block subblock = block.subblock(2, 2, 4, 4);

        assertEquals(2, subblock.getX1());
        assertEquals(2, subblock.getY1());
        assertEquals(4, subblock.getX2());
        assertEquals(4, subblock.getY2());
    }

    @Test
    public void testCharAt() {
        Scheme scheme = new Scheme(10, 10);
        scheme.lines = new String[]{"0123456789"};

        Block block = new BlockImpl(scheme, 1, 1, 5, 5);
        assertEquals('5', block.charAt(2, 2));
    }

    @Test
    public void testCharAtOutOfBounds() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new BlockImpl(scheme, 1, 1, 5, 5);

        try {
            block.charAt(6, 6);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("char position (6,6) out of block.\n" + block.toString(), e.getMessage());
        }
    }

    @Test
    public void testTextAt() {
        Scheme scheme = new Scheme(10, 10);
        scheme.lines = new String[]{"0123456789"};

        Block block = new BlockImpl(scheme, 1, 1, 5, 5);
        assertEquals("234", block.textAt(2, 5, 2));
    }

    @Test
    public void testTextAtOutOfBounds() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new BlockImpl(scheme, 1, 1, 5, 5);

        try {
            block.textAt(6, 8, 2);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Improper subline coordinates.", e.getMessage());
        }
    }

    @Test
    public void testContainsOnlyOneWord() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new BlockImpl(scheme, 1, 1, 5, 5);

        try {
            block.containsOnlyOneWord();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("cannot search in a trivial block", e.getMessage());
        }
    }

    @Test
    public void testContainsOnlyOneWordEmptyBlock() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new BlockImpl(scheme, 1, 5, 5, 5);

        try {
            block.containsOnlyOneWord();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("cannot verify unicity of a word in an empty block", e.getMessage());
        }
    }

    @Test
    public void testExtractSequentialSubblocks() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new BlockImpl(scheme, 1, 1, 5, 5);
        List<Block> sequentialSubblocks = block.extractSequentialSubblocks();

        assertEquals(1, sequentialSubblocks.size());
        Block subblock = sequentialSubblocks.get(0);
        assertEquals(1, subblock.getX1());
        assertEquals(1, subblock.getY1());
        assertEquals(5, subblock.getX2());
        assertEquals(5, subblock.getY2());
    }

    @Test
    public void testExtractParallelSubblocks() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new BlockImpl(scheme, 1, 1, 5, 5);
        List<Block> parallelSubblocks = block.extractParallelSubblocks();

        assertEquals(1, parallelSubblocks.size());
        Block subblock = parallelSubblocks.get(0);
        assertEquals(1, subblock.getX1());
        assertEquals(1, subblock.getY1());
        assertEquals(5, subblock.getX2());
        assertEquals(5, subblock.getY2());
    }

    @Test
    public void testToString() {
        Scheme scheme = new Scheme(10, 10);
        scheme.lines = new String[]{"abcdefghij"};

        Block block = new BlockImpl(scheme, 1, 1, 5, 5);
        assertEquals("Block(1,1, 5,5):\n" +
                " ####\n" +
                " #cde#\n" +
                " #cde#\n" +
                " #cde#\n" +
                " ####", block.toString());
    }

    @Test
    public void testGetUpperLine() {
        Scheme scheme = new Scheme(10, 10);
        scheme.lines = new String[]{"abcdefghij"};

        Block block = new BlockImpl(scheme, 1, 1, 5, 5);
        assertEquals("cde", block.getUpperLine());
    }

    private class BlockImpl extends Block {

        public BlockImpl(Scheme scheme, int x1, int y1, int x2, int y2) {
            super(scheme, x1, y1, x2, y2);
        }

        public BlockImpl(Scheme scheme, CharPosition p1, CharPosition p2) {
            super(scheme, p1, p2);
        }

        public BlockImpl(Scheme scheme) {
            super(scheme);
        }

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
    }
}