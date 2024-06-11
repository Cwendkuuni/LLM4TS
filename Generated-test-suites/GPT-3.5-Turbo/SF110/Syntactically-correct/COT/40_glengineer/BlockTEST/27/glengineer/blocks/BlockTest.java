
import org.junit.Test;
import glengineer.positions.*;

public class BlockTest {

    @Test
    public void testConstructorWithSchemeAndCoordinates() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new BlockImpl(scheme, 1, 1, 3, 3);

        // Assertions
        assertEquals(1, block.getX1());
        assertEquals(1, block.getY1());
        assertEquals(3, block.getX2());
        assertEquals(3, block.getY2());
        assertEquals(scheme, block.getScheme());
    }

    @Test
    public void testConstructorWithSchemeAndCharPositions() {
        Scheme scheme = new Scheme(5, 5);
        CharPosition p1 = new CharPosition(1, 1);
        CharPosition p2 = new CharPosition(3, 3);
        Block block = new BlockImpl(scheme, p1, p2);

        // Assertions
        assertEquals(1, block.getX1());
        assertEquals(1, block.getY1());
        assertEquals(3, block.getX2());
        assertEquals(3, block.getY2());
        assertEquals(scheme, block.getScheme());
    }

    @Test
    public void testConstructorWithScheme() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new BlockImpl(scheme);

        // Assertions
        assertEquals(0, block.getX1());
        assertEquals(0, block.getY1());
        assertEquals(5, block.getX2());
        assertEquals(5, block.getY2());
        assertEquals(scheme, block.getScheme());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubblock() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new BlockImpl(scheme, 1, 1, 3, 3);
        Block subblock = block.subblock(2, 2, 4, 4);

        // Assertions
        assertEquals(2, subblock.getX1());
        assertEquals(2, subblock.getY1());
        assertEquals(4, subblock.getX2());
        assertEquals(4, subblock.getY2());
        assertEquals(scheme, block.getScheme());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCharAt() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new BlockImpl(scheme, 1, 1, 3, 3);
        char result = block.charAt(-1, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTextAt() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new BlockImpl(scheme, 1, 1, 3, 3);
        String result = block.textAt(1, 6, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWord() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new BlockImpl(scheme, 1, 1, 3, 3);
        boolean result = block.containsOnlyOneWord();
    }

    @Test
    public void testExtractSequentialSubblocks() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new BlockImpl(scheme, 1, 1, 3, 3);
        List<Block> subblocks = block.extractSequentialSubblocks();

        // Assertions
        assertEquals(1, subblocks.size());
        assertEquals(block, subblocks.get(0));
    }

    @Test
    public void testExtractParallelSubblocks() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new BlockImpl(scheme, 1, 1, 3, 3);
        List<Block> subblocks = block.extractParallelSubblocks();

        // Assertions
        assertEquals(1, subblocks.size());
        assertEquals(block, subblocks.get(0));
    }

    @Test
    public void testToString() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new BlockImpl(scheme, 1, 1, 3, 3);
        String result = block.toString();

        // Assertions
        assertNotNull(result);
        assertTrue(result.contains("Block(1,1, 3,3):"));
        assertTrue(result.contains("# # # # #"));
        assertTrue(result.contains("#   #"));
        assertTrue(result.contains("#   #"));
        assertTrue(result.contains("#   #"));
        assertTrue(result.contains("# # # # #"));
    }

    @Test
    public void testGetUpperLine() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new BlockImpl(scheme, 1, 1, 3, 3);
        String result = block.getUpperLine();

        // Assertions
        assertNotNull(result);
        assertEquals("   ", result);
    }
}

class BlockImpl extends Block {
    public BlockImpl(Scheme scheme, int x1, int y1, int x2, int y2) {
        super(scheme, x1, y1, x2, y2);
    }

    public BlockImpl(Scheme scheme, CharPosition p1, CharPosition p2) {
        super(scheme, p1, p2);
    }

    public BlockImpl(Scheme scheme) {
        super(scheme);
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
