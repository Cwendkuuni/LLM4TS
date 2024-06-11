import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;

import org.junit.Before;
import org.junit.Test;

import glengineer.blocks.Block;
import glengineer.positions.Scheme;

public class BlockTest {

    private Scheme scheme;

    @Before
    public void setup() {
        scheme = new Scheme(10, 10);
    }

    @Test
    public void testBlockConstructorWithCoordinates() {
        Block block = new TestBlock(scheme, 1, 1, 5, 5);
        assertEquals(1, block.x1);
        assertEquals(1, block.y1);
        assertEquals(5, block.x2);
        assertEquals(5, block.y2);
    }

    @Test
    public void testBlockConstructorWithCharPositions() {
        Block block = new TestBlock(scheme, new CharPosition(1, 1), new CharPosition(5, 5));
        assertEquals(1, block.x1);
        assertEquals(1, block.y1);
        assertEquals(5, block.x2);
        assertEquals(5, block.y2);
    }

    @Test
    public void testBlockConstructorWithDefaultValues() {
        Block block = new TestBlock(scheme);
        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(10, block.x2);
        assertEquals(10, block.y2);
    }

    @Test
    public void testSubblock() {
        Block block = new TestBlock(scheme, 1, 1, 5, 5);
        Block subblock = block.subblock(2, 2, 4, 4);
        assertEquals(2, subblock.x1);
        assertEquals(2, subblock.y1);
        assertEquals(4, subblock.x2);
        assertEquals(4, subblock.y2);
    }

    @Test
    public void testCharAtWithinBlock() {
        Block block = new TestBlock(scheme, 1, 1, 5, 5);
        assertEquals('a', block.charAt(1, 1));
    }

    @Test
    public void testCharAtOutOfBlock() {
        Block block = new TestBlock(scheme, 1, 1, 5, 5);
        assertThrows(IllegalArgumentException.class, () -> block.charAt(6, 6));
    }

    @Test
    public void testTextAtWithinBlock() {
        Block block = new TestBlock(scheme, 1, 1, 5, 5);
        assertEquals("abc", block.textAt(1, 4, 1));
    }

    @Test
    public void testTextAtOutOfBlock() {
        Block block = new TestBlock(scheme, 1, 1, 5, 5);
        assertThrows(IllegalArgumentException.class, () -> block.textAt(6, 8, 1));
    }

    @Test
    public void testContainsOnlyOneWord() {
        Block block = new TestBlock(scheme, 1, 1, 5, 5);
        assertTrue(block.containsOnlyOneWord());
        
        block = new TestBlock(scheme, 1, 1, 1, 1);
        assertFalse(block.containsOnlyOneWord());
        
        block = new TestBlock(scheme, 5, 5, 10, 10);
        assertFalse(block.containsOnlyOneWord());
    }

    @Test
    public void testExtractSequentialSubblocks() {
        Block block = new TestBlock(scheme, 1, 1, 5, 5);
        assertEquals(2, block.extractSequentialSubblocks().size());
    }

    @Test
    public void testExtractParallelSubblocks() {
        Block block = new TestBlock(scheme, 1, 1, 5, 5);
        assertEquals(2, block.extractParallelSubblocks().size());
    }

    @Test
    public void testGetUpperLine() {
        Block block = new TestBlock(scheme, 1, 1, 5, 5);
        assertEquals("abc", block.getUpperLine());
    }

    private class TestBlock extends Block {

        public TestBlock(Scheme scheme, int x1, int y1, int x2, int y2) {
            super(scheme, x1, y1, x2, y2);
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