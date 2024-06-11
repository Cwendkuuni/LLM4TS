package glengineer.blocks;

import glengineer.positions.CharPosition;
import glengineer.positions.Scheme;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BlockTest {

    private Block block;
    private Scheme scheme;
    private CharPosition p1;
    private CharPosition p2;

    @Before
    public void setUp() {
        scheme = new Scheme(10, 10);
        p1 = new CharPosition(1, 1);
        p2 = new CharPosition(5, 5);
        block = new TestBlock(scheme, p1, p2);
    }

    @Test
    public void testBlockCreation() {
        assertEquals(1, block.x1);
        assertEquals(1, block.y1);
        assertEquals(5, block.x2);
        assertEquals(5, block.y2);
        assertEquals(scheme, block.scheme);
    }

    @Test
    public void testBlockCreationWithInvalidCoordinatesThrowsException() {
        try {
            new TestBlock(scheme, 5, 5, 1, 1);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertEquals("improper block definition: 5,5, 1,1.", e.getMessage());
        }
    }

    @Test
    public void testCharAt() {
        scheme.lines = new String[]{"ABCDEFGHIJ", "KLMNOPQRST", "UVWXYZ"};
        assertEquals('M', block.charAt(2, 1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCharAtWithInvalidCoordinatesThrowsException() {
        block.charAt(10, 10);
    }

    @Test
    public void testTextAt() {
        scheme.lines = new String[]{"ABCDEFGHIJ", "KLMNOPQRST", "UVWXYZ"};
        assertEquals("LMN", block.textAt(2, 5, 1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTextAtWithInvalidCoordinatesThrowsException() {
        block.textAt(1, 10, 1);
    }

    @Test
    public void testContainsOnlyOneWord() {
        scheme.lines = new String[]{"ABCDEFGHIJ", "KLMNOPQRST", "UVWXYZ"};
        assertFalse(block.containsOnlyOneWord());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWordWithTrivialBlockThrowsException() {
        assertTrue(block.containsOnlyOneWord());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWordWithEmptyBlockThrowsException() {
        scheme.lines = new String[]{"ABCDEFGHIJ", "KLMNOPQRST", "UVWXYZ"};
        block.y2 = 2;
        assertTrue(block.containsOnlyOneWord());
    }

    // Other test methods for extractSequentialSubblocks and extractParallelSubblocks

    private class TestBlock extends Block {
        public TestBlock(Scheme scheme, CharPosition p1, CharPosition p2) {
            super(scheme, p1, p2);
        }

        @Override
        public Block subblock(int p0, int p1, int p2, int p3) {
            return null;
        }

        @Override
        public java.util.List<Block> extractSequentialSubblocks() {
            return null;
        }

        @Override
        public java.util.List<Block> extractParallelSubblocks() {
            return null;
        }
    }

}