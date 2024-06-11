import static org.junit.Assert.*;
import org.junit.Test;

public class BlockTest {

    @Test
    public void testBlockConstructor1() {
        Scheme scheme = new Scheme(5, 5);
        try {
            Block block = new ConcreteBlock(scheme, -1, 0, 4, 4);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("improper block definition: -1,0, 4,4.", e.getMessage());
        }
    }

    @Test
    public void testBlockConstructor2() {
        Scheme scheme = new Scheme(5, 5);
        try {
            Block block = new ConcreteBlock(scheme, 0, 0, 6, 6);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("improper block definition: 0,0, 6,6.", e.getMessage());
        }
    }

    @Test
    public void testBlockConstructor3() {
        Scheme scheme = new Scheme(5, 5);
        try {
            Block block = new ConcreteBlock(scheme, 1, 5, 4, 4);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("improper block definition: 1,5, 4,4.", e.getMessage());
        }
    }
    
    @Test
    public void testBlockConstructor4() {
        Scheme scheme = new Scheme(5, 5);
        ConcreteBlock block = new ConcreteBlock(scheme, 1, 1, 3, 3);
        assertEquals(scheme, block.getScheme());
        assertEquals(1, block.getX1());
        assertEquals(1, block.getY1());
        assertEquals(3, block.getX2());
        assertEquals(3, block.getY2());
    }
    
    @Test
    public void testBlockConstructor5() {
        Scheme scheme = new Scheme(5, 5);
        CharPosition p1 = new CharPosition(1, 1);
        CharPosition p2 = new CharPosition(3, 3);
        ConcreteBlock block = new ConcreteBlock(scheme, p1, p2);
        assertEquals(scheme, block.getScheme());
        assertEquals(1, block.getX1());
        assertEquals(1, block.getY1());
        assertEquals(3, block.getX2());
        assertEquals(3, block.getY2());
    }

    @Test
    public void testBlockConstructor6() {
        Scheme scheme = new Scheme(5, 5);
        ConcreteBlock block = new ConcreteBlock(scheme);
        assertEquals(scheme, block.getScheme());
        assertEquals(0, block.getX1());
        assertEquals(0, block.getY1());
        assertEquals(5, block.getX2());
        assertEquals(5, block.getY2());
    }

    @Test
    public void testSubblock() {
        Scheme scheme = new Scheme(5, 5);
        ConcreteBlock block = new ConcreteBlock(scheme, 1, 1, 3, 3);
        Block subblock = block.subblock(2, 2, 3, 3);
        assertEquals(2, subblock.getX1());
        assertEquals(2, subblock.getY1());
        assertEquals(3, subblock.getX2());
        assertEquals(3, subblock.getY2());
    }

    @Test
    public void testCharAt() {
        Scheme scheme = new Scheme(5, 5);
        scheme.lines = new String[] {"ABCDE", "FGHIJ", "KLMNO", "PQRST", "UVWXY"};
        ConcreteBlock block = new ConcreteBlock(scheme, 1, 1, 3, 3);
        assertEquals('H', block.charAt(2, 1));
    }

    @Test
    public void testTextAt() {
        Scheme scheme = new Scheme(5, 5);
        scheme.lines = new String[] {"ABCDE", "FGHIJ", "KLMNO", "PQRST", "UVWXY"};
        ConcreteBlock block = new ConcreteBlock(scheme, 1, 1, 4, 3);
        assertEquals("HIJ", block.textAt(2, 5, 1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTextAtInvalidCoordinates() {
        Scheme scheme = new Scheme(5, 5);
        scheme.lines = new String[] {"ABCDE", "FGHIJ", "KLMNO", "PQRST", "UVWXY"};
        ConcreteBlock block = new ConcreteBlock(scheme, 1, 1, 4, 3);
        block.textAt(2, 6, 1);
    }

    @Test
    public void testContainsOnlyOneWordWithTrivialBlock() {
        Scheme scheme = new Scheme(5, 5);
        ConcreteBlock block = new ConcreteBlock(scheme, 0, 0, 0, 0);
        try {
            block.containsOnlyOneWord();
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("cannot search in a trivial block", e.getMessage());
        }
    }

    @Test
    public void testContainsOnlyOneWordWithEmptyBlock() {
        Scheme scheme = new Scheme(5, 5);
        ConcreteBlock block = new ConcreteBlock(scheme, 1, 4, 3, 4);
        try {
            block.containsOnlyOneWord();
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("cannot verify unicity of an word in an empty block", e.getMessage());
        }
    }

    @Test
    public void testContainsOnlyOneWordValid() {
        Scheme scheme = new Scheme(5, 5);
        scheme.lines = new String[] {"ABCDE", "FGHIJ", "KLOKK", "PQRST", "UVWXY"};
        ConcreteBlock block = new ConcreteBlock(scheme, 1, 2, 4, 3);
        assertTrue(block.containsOnlyOneWord());
    }

    @Test
    public void testContainsOnlyOneWordInvalid() {
        Scheme scheme = new Scheme(5, 5);
        scheme.lines = new String[] {"ABCDE", "FGHIJ", "KLOLK", "PQRST", "UVWXY"};
        ConcreteBlock block = new ConcreteBlock(scheme, 1, 2, 4, 3);
        assertFalse(block.containsOnlyOneWord());
    }

    @Test
    public void testExtractSequentialSubblocks() {
        Scheme scheme = new Scheme(5, 5);
        scheme.lines = new String[] {"ABCDE", "FGHIJ", "KLOLK", "PQRST", "UVWXY"};
        ConcreteBlock block = new ConcreteBlock(scheme, 0, 1, 4, 4);
        List<Block> subblocks = block.extractSequentialSubblocks();
        assertEquals(3, subblocks.size());
        assertEquals(0, subblocks.get(0).getX1());
        assertEquals(1, subblocks.get(0).getY1());
        assertEquals(4, subblocks.get(0).getX2());
        assertEquals(2, subblocks.get(0).getY2());
        assertEquals(0, subblocks.get(1).getX1());
        assertEquals(2, subblocks.get(1).getY1());
        assertEquals(4, subblocks.get(1).getX2());
        assertEquals(3, subblocks.get(1).getY2());
        assertEquals(0, subblocks.get(2).getX1());
        assertEquals(3, subblocks.get(2).getY1());
        assertEquals(4, subblocks.get(2).getX2());
        assertEquals(4, subblocks.get(2).getY2());
    }

    @Test
    public void testExtractParallelSubblocks() {
        Scheme scheme = new Scheme(5, 5);
        scheme.lines = new String[] {"ABCDE", "FGHIJ", "KLOLK", "PQRST", "UVWXY"};
        ConcreteBlock block = new ConcreteBlock(scheme, 0, 1, 4, 4);
        List<Block> subblocks = block.extractParallelSubblocks();
        assertEquals(4, subblocks.size());
        assertEquals(0, subblocks.get(0).getX1());
        assertEquals(1, subblocks.get(0).getY1());
        assertEquals(0, subblocks.get(0).getX2());
        assertEquals(4, subblocks.get(0).getY2());
        assertEquals(1, subblocks.get(1).getX1());
        assertEquals(1, subblocks.get(1).getY1());
        assertEquals(1, subblocks.get(1).getX2());
        assertEquals(4, subblocks.get(1).getY2());
        assertEquals(2, subblocks.get(2).getX1());
        assertEquals(1, subblocks.get(2).getY1());
        assertEquals(2, subblocks.get(2).getX2());
        assertEquals(4, subblocks.get(2).getY2());
        assertEquals(3, subblocks.get(3).getX1());
        assertEquals(1, subblocks.get(3).getY1());
        assertEquals(3, subblocks.get(3).getX2());
        assertEquals(4, subblocks.get(3).getY2());
    }

    @Test
    public void testToString() {
        Scheme scheme = new Scheme(5, 5);
        scheme.lines = new String[] {"ABCDE", "FGHIJ", "KLOLK", "PQRST", "UVWXY"};
        ConcreteBlock block = new ConcreteBlock(scheme, 1, 1, 3, 3);
        String expected = "Block(1,1, 3,3):\n" +
                          " ###### \n" +
                          " #LOL# \n" +
                          " #TOR# \n" +
                          " ###### ";
        assertEquals(expected, block.toString());
    }
    
    @Test
    public void testGetUpperLine() {
        Scheme scheme = new Scheme(5, 5);
        scheme.lines = new String[] {"ABCDE", "FGHIJ", "KLOLK", "PQRST", "UVWXY"};
        ConcreteBlock block = new ConcreteBlock(scheme, 0, 1, 4, 4);
        assertEquals("FGHIJ", block.getUpperLine());
    }
}