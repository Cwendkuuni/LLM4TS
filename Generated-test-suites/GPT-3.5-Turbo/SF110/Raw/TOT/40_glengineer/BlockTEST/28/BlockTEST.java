import org.junit.Test;
import static org.junit.Assert.*;

public class BlockTest {

    @Test
    public void testBlockConstructor1() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 0, 0, 5, 5);
        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(5, block.x2);
        assertEquals(5, block.y2);
        assertEquals(scheme, block.scheme);
    }

    @Test
    public void testBlockConstructor2() {
        Scheme scheme = new Scheme(10, 10);
        CharPosition p1 = new CharPosition(1, 1);
        CharPosition p2 = new CharPosition(9, 9);
        Block block = new ConcreteBlock(scheme, p1, p2);
        assertEquals(p1.x, block.x1);
        assertEquals(p1.y, block.y1);
        assertEquals(p2.x, block.x2);
        assertEquals(p2.y, block.y2);
        assertEquals(scheme, block.scheme);
    }

    @Test
    public void testBlockConstructor3() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme);
        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(scheme.x2, block.x2);
        assertEquals(scheme.y2, block.y2);
        assertEquals(scheme, block.scheme);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructor_IllegalArguments1() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, -1, 0, 5, 5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructor_IllegalArguments2() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 0, -1, 5, 5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructor_IllegalArguments3() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 5, 5, 4, 4);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBlockConstructor_IllegalArguments4() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 0, 0, 5, 11);
    }

    @Test
    public void testSubblock() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 1, 1, 9, 9);
        Block subblock = block.subblock(2, 2, 8, 8);
        assertEquals(2, subblock.x1);
        assertEquals(2, subblock.y1);
        assertEquals(8, subblock.x2);
        assertEquals(8, subblock.y2);
        assertEquals(scheme, subblock.scheme);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCharAt_OutOfBlock() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 0, 0, 5, 5);
        char c = block.charAt(6, 6);
    }

    @Test
    public void testTextAt() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 1, 1, 9, 9);
        String text = block.textAt(2, 5, 5);
        assertEquals("lmno", text);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTextAt_ImproperCoordinates() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 1, 1, 9, 9);
        String text = block.textAt(5, 2, 5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWord_TrivialBlock() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 0, 0, 1, 1);
        block.containsOnlyOneWord();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWord_EmptyBlock() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 0, 0, 5, 5);
        block.containsOnlyOneWord();
    }

    @Test
    public void testContainsOnlyOneWord_True() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 0, 0, 5, 5);
        boolean result = block.containsOnlyOneWord();
        assertTrue(result);
    }

    @Test
    public void testExtractSequentialSubblocks() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 1, 1, 9, 9);
        List<Block> subblocks = block.extractSequentialSubblocks();
        assertEquals(1, subblocks.size());
        Block subblock = subblocks.get(0);
        assertEquals(1, subblock.x1);
        assertEquals(1, subblock.y1);
        assertEquals(9, subblock.x2);
        assertEquals(9, subblock.y2);
        assertEquals(scheme, subblock.scheme);
    }

    @Test
    public void testExtractParallelSubblocks() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 1, 1, 9, 9);
        List<Block> subblocks = block.extractParallelSubblocks();
        assertEquals(1, subblocks.size());
        Block subblock = subblocks.get(0);
        assertEquals(1, subblock.x1);
        assertEquals(1, subblock.y1);
        assertEquals(9, subblock.x2);
        assertEquals(9, subblock.y2);
        assertEquals(scheme, subblock.scheme);
    }

    @Test
    public void testToString() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 1, 1, 5, 5);
        String expected = "Block(1,1, 5,5):\n" +
                " #### \n" +
                " #lMn# \n" +
                " #opq# \n" +
                " #rst# \n" +
                " #### ";
        String result = block.toString();
        assertEquals(expected, result);
    }

    @Test
    public void testGetUpperLine() {
        Scheme scheme = new Scheme(10, 10);
        Block block = new ConcreteBlock(scheme, 1, 1, 5, 5);
        String line = block.getUpperLine();
        assertEquals("lMn", line);
    }
}