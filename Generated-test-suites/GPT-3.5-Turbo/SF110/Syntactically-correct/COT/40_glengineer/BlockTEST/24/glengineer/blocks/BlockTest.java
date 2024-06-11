
package glengineer.blocks;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

public class BlockTest {

    private Scheme scheme;
    private Block block;

    @Before
    public void setup() {
        scheme = new Scheme();
        block = new ConcreteBlock(scheme);
    }
    
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void testBlockConstructorWithCoordinates() {
        Block block = new ConcreteBlock(scheme, 0, 0, 2, 2);
        assertEquals("Expected x1 to be 0", 0, block.x1);
        assertEquals("Expected y1 to be 0", 0, block.y1);
        assertEquals("Expected x2 to be 2", 2, block.x2);
        assertEquals("Expected y2 to be 2", 2, block.y2);
        assertEquals("Expected scheme to match", scheme, block.scheme);
    }

    @Test
    public void testBlockConstructorWithCharPosition() {
        CharPosition p1 = new CharPosition(0, 0);
        CharPosition p2 = new CharPosition(2, 2);
        Block block = new ConcreteBlock(scheme, p1, p2);
        assertEquals("Expected x1 to be 0", 0, block.x1);
        assertEquals("Expected y1 to be 0", 0, block.y1);
        assertEquals("Expected x2 to be 2", 2, block.x2);
        assertEquals("Expected y2 to be 2", 2, block.y2);
        assertEquals("Expected scheme to match", scheme, block.scheme);
    }

    @Test
    public void testBlockConstructorDefault() {
        Block block = new ConcreteBlock(scheme);
        assertEquals("Expected x1 to be 0", 0, block.x1);
        assertEquals("Expected y1 to be 0", 0, block.y1);
        assertEquals("Expected x2 to match scheme", scheme.x2, block.x2);
        assertEquals("Expected y2 to match scheme", scheme.y2, block.y2);
        assertEquals("Expected scheme to match", scheme, block.scheme);
    }

    @Test
    public void testSubblock() {
        Block subblock = block.subblock(1, 1, 2, 2);
        assertEquals("Expected x1 to be 1", 1, subblock.x1);
        assertEquals("Expected y1 to be 1", 1, subblock.y1);
        assertEquals("Expected x2 to be 2", 2, subblock.x2);
        assertEquals("Expected y2 to be 2", 2, subblock.y2);
        assertEquals("Expected scheme to match", scheme, subblock.scheme);
    }

    @Test
    public void testCharAtValidPosition() {
        assertEquals("Expected character at position (0, 0) to be '#'", '#', block.charAt(0, 0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCharAtInvalidPosition() {
        block.charAt(10, 10);
    }

    @Test
    public void testTextAtValidCoordinates() {
        assertEquals("Expected text '##' at coordinates (0, 0, 1)", "##", block.textAt(0, 2, 1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTextAtInvalidCoordinates() {
        block.textAt(10, 20, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWordTrivialBlock() {
        block.containsOnlyOneWord();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWordEmptyBlock() {
        Block emptyBlock = new ConcreteBlock(scheme, 0, 0, 0, 0);
        emptyBlock.containsOnlyOneWord();
    }

    @Test
    public void testContainsOnlyOneWordValidBlock() {
        Block validBlock = new ConcreteBlock(scheme, 0, 0, 2, 2);
        assertFalse("Expected false for containsOnlyOneWord", validBlock.containsOnlyOneWord());
    }

    @Test
    public void testExtractSequentialSubblocks() {
        assertEquals("Expected empty list for extractSequentialSubblocks", 0, block.extractSequentialSubblocks().size());
    }

    @Test
    public void testExtractParallelSubblocks() {
        assertEquals("Expected empty list for extractParallelSubblocks", 0, block.extractParallelSubblocks().size());
    }

    @Test
    public void testToString() {
        String expected = "Block(0,0, 10,10):\n" +
                " #################### \n" +
                " #                    \n" +
                " #                    \n" +
                " #                    \n" +
                " #                    \n" +
                " #                    \n" +
                " #                    \n" +
                " #                    \n" +
                " #                    \n" +
                " #################### ";
        assertEquals("Expected toString output to match", expected, block.toString());
    }

    @Test
    public void testGetUpperLine() {
        assertEquals("Expected upper line to match", "##############################", block.getUpperLine());
    }
}

class ConcreteBlock extends Block {
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
        // TODO: Implement subblock
        return null;
    }

    @Override
    public List<Block> extractSequentialSubblocks() {
        // TODO: Implement extractSequentialSubblocks
        return null;
    }

    @Override
    public List<Block> extractParallelSubblocks() {
        // TODO: Implement extractParallelSubblocks
        return null;
    }

    @Override
    public char charAt(int x, int y) {
        // TODO: Implement charAt
        return 0;
    }

    @Override
    public String textAt(int x1, int x2, int y) {
        // TODO: Implement textAt
        return null;
    }
}
