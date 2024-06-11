import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BlockTest {

    private Block block;

    @Before
    public void setUp() {
        Scheme scheme = new Scheme(5, 5);
        block = new ConcreteBlock(scheme, 1, 2, 3, 4);
    }

    @Test
    public void testBlock_constructorWithCoordinates_validArguments() {
        assertEquals(1, block.x1);
        assertEquals(2, block.y1);
        assertEquals(3, block.x2);
        assertEquals(4, block.y2);
        assertEquals(5, block.scheme.x2);
        assertEquals(5, block.scheme.y2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBlock_constructorWithCoordinates_invalidArguments() {
        new ConcreteBlock(block.scheme, -1, 2, 3, 4);
    }

    @Test
    public void testBlock_constructorWithPositions_validArguments() {
        CharPosition p1 = new CharPosition(1, 2);
        CharPosition p2 = new CharPosition(3, 4);
        Block block = new ConcreteBlock(block.scheme, p1, p2);

        assertEquals(1, block.x1);
        assertEquals(2, block.y1);
        assertEquals(3, block.x2);
        assertEquals(4, block.y2);
        assertEquals(5, block.scheme.x2);
        assertEquals(5, block.scheme.y2);
    }

    @Test
    public void testBlock_constructorWithScheme() {
        Block block = new ConcreteBlock(block.scheme);

        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(5, block.x2);
        assertEquals(5, block.y2);
        assertEquals(5, block.scheme.x2);
        assertEquals(5, block.scheme.y2);
    }

    @Test
    public void testBlock_subblock() {
        Block subblock = block.subblock(2, 3, 3, 4);

        assertEquals(2, subblock.x1);
        assertEquals(3, subblock.y1);
        assertEquals(3, subblock.x2);
        assertEquals(4, subblock.y2);
        assertEquals(5, subblock.scheme.x2);
        assertEquals(5, subblock.scheme.y2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCharAt_invalidPosition() {
        block.charAt(6, 2);
    }

    @Test
    public void testCharAt_validPosition() {
        char result = block.charAt(2, 3);
        assertEquals('#', result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTextAt_invalidCoordinates() {
        block.textAt(1, 6, 3);
    }

    @Test
    public void testTextAt_validCoordinates() {
        String result = block.textAt(1, 3, 3);
        assertEquals("##", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWord_trivialBlock() {
        Block trivialBlock = new ConcreteBlock(block.scheme, 2, 2, 2, 2);
        trivialBlock.containsOnlyOneWord();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWord_emptyBlock() {
        Block emptyBlock = new ConcreteBlock(block.scheme, 1, 4, 3, 4);
        emptyBlock.containsOnlyOneWord();
    }

    @Test
    public void testContainsOnlyOneWord_success() {
        boolean result = block.containsOnlyOneWord();
        assertEquals(false, result);
    }

    @Test
    public void testExtractSequentialSubblocks() {
        List<Block> subblocks = block.extractSequentialSubblocks();

        assertEquals(2, subblocks.size());
        assertEquals(1, subblocks.get(0).x1);
        assertEquals(2, subblocks.get(0).y1);
        assertEquals(3, subblocks.get(0).x2);
        assertEquals(4, subblocks.get(0).y2);
        assertEquals(5, subblocks.get(0).scheme.x2);
        assertEquals(5, subblocks.get(0).scheme.y2);
        assertEquals(3, subblocks.get(1).x1);
        assertEquals(2, subblocks.get(1).y1);
        assertEquals(5, subblocks.get(1).x2);
        assertEquals(4, subblocks.get(1).y2);
        assertEquals(5, subblocks.get(1).scheme.x2);
        assertEquals(5, subblocks.get(1).scheme.y2);
    }

    @Test
    public void testExtractParallelSubblocks() {
        List<Block> subblocks = block.extractParallelSubblocks();

        assertEquals(2, subblocks.size());
        assertEquals(1, subblocks.get(0).x1);
        assertEquals(2, subblocks.get(0).y1);
        assertEquals(3, subblocks.get(0).x2);
        assertEquals(4, subblocks.get(0).y2);
        assertEquals(5, subblocks.get(0).scheme.x2);
        assertEquals(5, subblocks.get(0).scheme.y2);
        assertEquals(1, subblocks.get(1).x1);
        assertEquals(4, subblocks.get(1).y1);
        assertEquals(3, subblocks.get(1).x2);
        assertEquals(5, subblocks.get(1).y2);
        assertEquals(5, subblocks.get(1).scheme.x2);
        assertEquals(5, subblocks.get(1).scheme.y2);
    }

    @Test
    public void testToString() {
        String result = block.toString();

        String expected = "Block(1,2, 3,4):\n" +
                " ####\n" +
                " # ##\n" +
                " ####";

        assertEquals(expected, result);
    }

    @Test
    public void testGetUpperLine() {
        String result = block.getUpperLine();

        assertEquals(" ## ", result);
    }
}