package glengineer.blocks;

import org.junit.Before;
import org.junit.Test;
import glengineer.positions.*;
import static org.junit.Assert.*;

public class BlockTest {

    private Block block;

    @Before
    public void setUp() {
        Scheme scheme = new Scheme(10, 10);
        block = new Block(scheme, 1, 1, 5, 5) {

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
        };
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_InvalidPosition() {
        Scheme scheme = new Scheme(10, 10);
        Block invalidBlock = new Block(scheme, -1, 1, 5, 5);
    }

    @Test
    public void testConstructor_ValidPosition() {
        Scheme scheme = new Scheme(10, 10);
        Block validBlock = new Block(scheme, 1, 1, 5, 5);
        assertNotNull(validBlock);
    }

    @Test
    public void testConstructor_PosCharPositions() {
        Scheme scheme = new Scheme(10, 10);
        CharPosition p1 = new CharPosition(1, 1);
        CharPosition p2 = new CharPosition(5, 5);
        Block block = new Block(scheme, p1, p2);
        assertNotNull(block);
    }

    @Test
    public void testConstructor_PosScheme() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new Block(scheme);
        assertNotNull(block);
    }

    @Test
    public void testSubblock() {
        Block subblock = block.subblock(1, 1, 2, 2);
        assertNull(subblock);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCharAt_InvalidPosition() {
        block.charAt(6, 6);
    }

    @Test
    public void testCharAt_ValidPosition() {
        char result = block.charAt(3, 3);
        assertEquals('#', result);
    }

    @Test
    public void testTextAt() {
        String result = block.textAt(1, 5, 1);
        assertEquals("#####", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWord_TrivialBlock() {
        block.containsOnlyOneWord();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWord_EmptyBlock() {
        Block emptyBlock = new Block(new Scheme(10, 10), 1, 1, 1, 1);
        emptyBlock.containsOnlyOneWord();
    }

    @Test
    public void testContainsOnlyOneWord_ValidBlock() {
        Block validBlock = new Block(new Scheme(10, 10), 1, 1, 3, 3) {

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
        };
        boolean result = validBlock.containsOnlyOneWord();
        assertTrue(result);
    }

    @Test
    public void testExtractSequentialSubblocks() {
        List<Block> subblocks = block.extractSequentialSubblocks();
        assertNull(subblocks);
    }

    @Test
    public void testExtractParallelSubblocks() {
        List<Block> subblocks = block.extractParallelSubblocks();
        assertNull(subblocks);
    }

    @Test
    public void testToString() {
        String result = block.toString();
        assertEquals("Block(1,1, 5,5):\n #####\n #     #\n #     #\n #     #\n #     #\n #####", result);
    }

    @Test
    public void testGetUpperLine() {
        String result = block.getUpperLine();
        assertEquals("#####", result);
    }
}