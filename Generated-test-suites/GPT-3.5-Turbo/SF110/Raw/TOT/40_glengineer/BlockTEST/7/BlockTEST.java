import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import glengineer.blocks.*;
import glengineer.positions.*;

public class BlockTest {

    private Scheme scheme;
    private Block block;

    @Before
    public void setUp() {
        scheme = new Scheme(5, 5);
        scheme.lines[0] = "#####";
        scheme.lines[1] = "#ABC#";
        scheme.lines[2] = "#DEFG";
        scheme.lines[3] = "#####";
        scheme.lines[4] = "#####";
        
        block = new Block(scheme, 1, 1, 3, 3) {
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

    @Test
    public void testBlockConstructorWithCoordinates_ValidParameters() {
        Block testBlock = new Block(scheme, 1, 1, 3, 3);
        assertEquals(scheme, testBlock.scheme);
        assertEquals(1, testBlock.x1);
        assertEquals(1, testBlock.y1);
        assertEquals(3, testBlock.x2);
        assertEquals(3, testBlock.y2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructorWithCoordinates_InvalidParameters() {
        Block testBlock = new Block(scheme, -1, 1, 3, 3);
    }

    @Test
    public void testBlockConstructorWithCharPositions_ValidParameters() {
        CharPosition p1 = new CharPosition(1, 1);
        CharPosition p2 = new CharPosition(3, 3);
        Block testBlock = new Block(scheme, p1, p2);
        assertEquals(scheme, testBlock.scheme);
        assertEquals(1, testBlock.x1);
        assertEquals(1, testBlock.y1);
        assertEquals(3, testBlock.x2);
        assertEquals(3, testBlock.y2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructorWithCharPositions_InvalidParameters() {
        CharPosition p1 = new CharPosition(1, 1);
        CharPosition p2 = new CharPosition(6, 3);
        Block testBlock = new Block(scheme, p1, p2);
    }

    @Test
    public void testBlockConstructorWithoutParameters() {
        Block testBlock = new Block(scheme);
        assertEquals(scheme, testBlock.scheme);
        assertEquals(0, testBlock.x1);
        assertEquals(0, testBlock.y1);
        assertEquals(scheme.x2, testBlock.x2);
        assertEquals(scheme.y2, testBlock.y2);
    }

    @Test
    public void testCharAt_ValidCoordinates() {
        char ch = block.charAt(2, 2);
        assertEquals('G', ch);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCharAt_InvalidCoordinates() {
        char ch = block.charAt(6, 2);
    }

    @Test
    public void testTextAt_ValidCoordinates() {
        String text = block.textAt(1, 3, 2);
        assertEquals("EFG", text);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTextAt_InvalidCoordinates() {
        String text = block.textAt(1, 5, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWord_TrivialBlock() {
        Block testBlock = new Block(scheme, 1, 0, 1, 0) {
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

            @Override
            public boolean isTrivial() {
                return true;
            }
        };
        testBlock.containsOnlyOneWord();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWord_EmptyBlock() {
        Block testBlock = new Block(scheme, 1, 4, 3, 4) {
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
        testBlock.containsOnlyOneWord();
    }

    @Test
    public void testContainsOnlyOneWord_ValidBlock() {
        boolean result = block.containsOnlyOneWord();
        assertTrue(result);
    }

    // Additional test cases for extractSequentialSubblocks() and extractParallelSubblocks()

    @Test
    public void testToString() {
        String expected = "Block(1,1, 3,3):\n" +
                " ######\n" +
                " ##ABC#\n" +
                " ##DEFG\n" +
                " ######";

        String result = block.toString();

        assertEquals(expected, result);
    }

    @Test
    public void testGetUpperLine() {
        String upperLine = block.getUpperLine();
        assertEquals("ABC", upperLine);
    }
}