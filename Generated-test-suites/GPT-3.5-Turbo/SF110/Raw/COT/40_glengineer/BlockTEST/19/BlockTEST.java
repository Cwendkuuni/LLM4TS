
import org.junit.Test;
import static org.junit.Assert.*;

public class BlockTest {

    @Test
    public void testConstructor1() {
        Scheme scheme = new Scheme(10,10);
        Block block = new ConcreteBlock(scheme, 1, 1, 5, 5);

        assertEquals(1, block.x1);
        assertEquals(1, block.y1);
        assertEquals(5, block.x2);
        assertEquals(5, block.y2);
        assertEquals(scheme, block.scheme);
    }

    @Test
    public void testConstructor2() {
        Scheme scheme = new Scheme(10,10);
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
    public void testConstructor3() {
        Scheme scheme = new Scheme(10,10);
        Block block = new ConcreteBlock(scheme);

        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(10, block.x2);
        assertEquals(10, block.y2);
        assertEquals(scheme, block.scheme);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_InvalidCoords() {
        Scheme scheme = new Scheme(10,10);
        Block block = new ConcreteBlock(scheme, -1, -1, 5, 5);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_InvalidCoords2() {
        Scheme scheme = new Scheme(10,10);
        Block block = new ConcreteBlock(scheme, 1, 1, 11, 11);
    }

    @Test
    public void testSubblock() {
        Scheme scheme = new Scheme(10,10);
        Block block = new ConcreteBlock(scheme, 1, 1, 5, 5);
        Block subblock = block.subblock(2, 3, 4, 4);

        assertEquals(2, subblock.x1);
        assertEquals(3, subblock.y1);
        assertEquals(4, subblock.x2);
        assertEquals(4, subblock.y2);
        assertEquals(scheme, subblock.scheme);
    }

    @Test
    public void testCharAt() {
        Scheme scheme = new Scheme(10,10);
        scheme.addLine("1234567890");
        scheme.addLine("abcdefghij");
        Block block = new ConcreteBlock(scheme, 1, 1, 5, 5);

        assertEquals('e', block.charAt(2, 2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCharAt_OutOfBlock() {
        Scheme scheme = new Scheme(10,10);
        scheme.addLine("1234567890");
        scheme.addLine("abcdefghij");
        Block block = new ConcreteBlock(scheme, 1, 1, 5, 5);

        block.charAt(10, 2);
    }

    @Test
    public void testTextAt() {
        Scheme scheme = new Scheme(10,10);
        scheme.addLine("1234567890");
        scheme.addLine("abcdefghij");
        Block block = new ConcreteBlock(scheme, 1, 1, 5, 5);

        assertEquals("cde", block.textAt(2, 5, 2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTextAt_ImproperCoords() {
        Scheme scheme = new Scheme(10,10);
        scheme.addLine("1234567890");
        scheme.addLine("abcdefghij");
        Block block = new ConcreteBlock(scheme, 1, 1, 5, 5);

        block.textAt(4, 10, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWord_TrivialBlock() {
        Scheme scheme = new Scheme(10,10);
        Block block = new ConcreteBlock(scheme);

        block.containsOnlyOneWord();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWord_EmptyBlock() {
        Scheme scheme = new Scheme(10,10);
        Block block = new ConcreteBlock(scheme, 1, 10, 2, 10);

        block.containsOnlyOneWord();
    }
    
    @Test
    public void testContainsOnlyOneWord_ValidBlock() {
        Scheme scheme = new Scheme(10,10);
        scheme.addLine(" abcdefgh ");
        scheme.addLine(" bcdefghi ");
        scheme.addLine(" cdefghij ");
        Block block = new ConcreteBlock(scheme, 1, 1, 9, 3);

        assertTrue(block.containsOnlyOneWord());
    }

    @Test
    public void testExtractSequentialSubblocks() {
        Scheme scheme = new Scheme(10,10);
        scheme.addLine(" abcdefgh ");
        scheme.addLine(" bcdefghi ");
        scheme.addLine(" cdefghij ");
        Block block = new ConcreteBlock(scheme, 1, 1, 9, 3);

        List<Block> subblocks = block.extractSequentialSubblocks();

        assertEquals(3, subblocks.size());
        assertEquals("abcdefghi", subblocks.get(0).getUpperLine());
        assertEquals("bcdefghij", subblocks.get(1).getUpperLine());
        assertEquals("cdefghij", subblocks.get(2).getUpperLine());
    }

    @Test
    public void testExtractParallelSubblocks() {
        Scheme scheme = new Scheme(10,10);
        scheme.addLine(" abcdefgh ");
        scheme.addLine(" bcdefghi ");
        scheme.addLine(" cdefghij ");
        Block block = new ConcreteBlock(scheme, 1, 1, 9, 3);

        List<Block> subblocks = block.extractParallelSubblocks();

        assertEquals(10, subblocks.size());
        assertEquals("a", subblocks.get(0).getUpperLine());
        assertEquals("b", subblocks.get(1).getUpperLine());
        assertEquals("c", subblocks.get(2).getUpperLine());
        assertEquals("d", subblocks.get(3).getUpperLine());
        assertEquals("e", subblocks.get(4).getUpperLine());
        assertEquals("f", subblocks.get(5).getUpperLine());
        assertEquals("g", subblocks.get(6).getUpperLine());
        assertEquals("h", subblocks.get(7).getUpperLine());
        assertEquals("i", subblocks.get(8).getUpperLine());
        assertEquals("j", subblocks.get(9).getUpperLine());
    }

    @Test
    public void testToString() {
        Scheme scheme = new Scheme(10,10);
        scheme.addLine(" abcdefgh ");
        scheme.addLine(" bcdefghi ");
        scheme.addLine(" cdefghij ");
        Block block = new ConcreteBlock(scheme, 1, 1, 9, 3);

        String expected = "Block(1,1, 9,3):\n" +
                " ############# \n" +
                " # abcdefghi # \n" +
                " # bcdefghij # \n" +
                " # cdefghij  # \n" +
                " ############# ";

        assertEquals(expected, block.toString());
    }

    @Test
    public void testGetUpperLine() {
        Scheme scheme = new Scheme(10,10);
        scheme.addLine(" abcdefgh ");
        scheme.addLine(" bcdefghi ");
        scheme.addLine(" cdefghij ");
        Block block = new ConcreteBlock(scheme, 1, 1, 9, 3);

        assertEquals(" abcdefgh ", block.getUpperLine());
    }
}
