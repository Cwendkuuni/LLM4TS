package glengineer.blocks;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class BlockTest {

    // Test constructors
    @Test
    public void testBlockConstructorSchemeAndDimensions() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 1, 1, 5, 5);
        assertEquals(1, block.x1);
        assertEquals(1, block.y1);
        assertEquals(5, block.x2);
        assertEquals(5, block.y2);
        assertEquals(scheme, block.scheme);
    }

    @Test
    public void testBlockConstructorSchemeAndPositions() {
        Scheme scheme = new Scheme(10, 10);
        CharPosition p1 = new CharPosition(1, 1);
        CharPosition p2 = new CharPosition(5, 5);
        Block block = new ConcreteBlock(scheme, p1, p2);
        assertEquals(1, block.x1);
        assertEquals(1, block.y1);
        assertEquals(5, block.x2);
        assertEquals(5, block.y2);
        assertEquals(scheme, block.scheme);
    }

    @Test
    public void testBlockConstructorScheme() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme);
        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(scheme.x2, block.x2);
        assertEquals(scheme.y2, block.y2);
        assertEquals(scheme, block.scheme);
    }

    // Test abstract method
    @Test
    public void testSubblock() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 1, 1, 5, 5);
        Block subblock = block.subblock(2, 2, 4, 4);
        assertEquals(2, subblock.x1);
        assertEquals(2, subblock.y1);
        assertEquals(4, subblock.x2);
        assertEquals(4, subblock.y2);
        assertEquals(scheme, subblock.scheme);
    }

    // Test charAt()
    @Test
    public void testCharAtValidPosition() {
        Scheme scheme = new Scheme(3, 3);
        scheme.lines[0] = "abc";
        scheme.lines[1] = "def";
        scheme.lines[2] = "ghi";
        Block block = new ConcreteBlock(scheme, 1, 1, 2, 2);
        assertEquals('f', block.charAt(1, 1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCharAtInvalidPosition() {
        Scheme scheme = new Scheme(3, 3);
        scheme.lines[0] = "abc";
        scheme.lines[1] = "def";
        scheme.lines[2] = "ghi";
        Block block = new ConcreteBlock(scheme, 1, 1, 2, 2);
        block.charAt(3, 3);
    }

    // Test textAt()
    @Test
    public void testTextAtValidCoordinates() {
        Scheme scheme = new Scheme(5, 5);
        scheme.lines[0] = "abcde";
        scheme.lines[1] = "fghij";
        scheme.lines[2] = "klmno";
        scheme.lines[3] = "pqrst";
        scheme.lines[4] = "uvwxy";
        Block block = new ConcreteBlock(scheme, 1, 1, 3, 3);
        assertEquals("ghi", block.textAt(1, 4, 1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTextAtInvalidCoordinates() {
        Scheme scheme = new Scheme(5, 5);
        scheme.lines[0] = "abcde";
        scheme.lines[1] = "fghij";
        scheme.lines[2] = "klmno";
        scheme.lines[3] = "pqrst";
        scheme.lines[4] = "uvwxy";
        Block block = new ConcreteBlock(scheme, 1, 1, 3, 3);
        block.textAt(4, 6, 1);
    }

    // Test containsOnlyOneWord()
    @Test
    public void testContainsOnlyOneWordSingleLine() {
        Scheme scheme = new Scheme(5, 1);
        scheme.lines[0] = "foo bar";
        Block block = new ConcreteBlock(scheme, 0, 0, 5, 1);
        assertFalse(block.containsOnlyOneWord());
    }

    @Test
    public void testContainsOnlyOneWordMultipleLines() {
        Scheme scheme = new Scheme(5, 2);
        scheme.lines[0] = "foo bar";
        scheme.lines[1] = "baz";
        Block block = new ConcreteBlock(scheme, 0, 0, 5, 2);
        assertFalse(block.containsOnlyOneWord());
    }

    @Test
    public void testContainsOnlyOneWordTrivialBlock() {
        Scheme scheme = new Scheme(5, 1);
        Block block = new ConcreteBlock(scheme, 0, 0, 0, 1);
        assertTrue(block.containsOnlyOneWord());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWordEmptyBlock() {
        Scheme scheme = new Scheme(5, 1);
        Block block = new ConcreteBlock(scheme, 0, 0, 5, 1);
        block.containsOnlyOneWord();
    }

    // Test extractSequentialSubblocks()
    // ...

    // Test extractParallelSubblocks()
    // ...

    // Test toString()
    // ...

    // Test getUpperLine()
    // ...

    private static class ConcreteBlock extends Block {

        public ConcreteBlock(Scheme scheme, int x1, int y1, int x2, int y2) {
            super(scheme, x1, y1, x2, y2);
        }

        public ConcreteBlock(Scheme scheme, CharPosition p1, CharPosition p2) {
            super(scheme, p1, p2);
        }

        public ConcreteBlock(Scheme scheme) {
            super(scheme);
        }

        @Override
        public Block subblock(int p0, int p1, int p2, int p3) {
            return null; // Dummy implementation for testing purposes
        }

        @Override
        public List<Block> extractSequentialSubblocks() {
            return null; // Dummy implementation for testing purposes
        }

        @Override
        public List<Block> extractParallelSubblocks() {
            return null; // Dummy implementation for testing purposes
        }
    }
}