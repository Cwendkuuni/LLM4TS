
import org.junit.Test;

public class BlockTest {

    @Test
    public void testConstructor1WithValidArguments() {
        Scheme scheme = new Scheme();
        Block block = new BlockImpl(scheme, 0, 0, 2, 2);
        assertEquals(scheme, block.getScheme());
        assertEquals(0, block.getX1());
        assertEquals(0, block.getY1());
        assertEquals(2, block.getX2());
        assertEquals(2, block.getY2());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor1WithInvalidArguments() {
        Scheme scheme = new Scheme();
        Block block = new BlockImpl(scheme, -1, 0, 2, 2);
    }

    @Test
    public void testConstructor2WithValidArguments() {
        Scheme scheme = new Scheme();
        CharPosition p1 = new CharPosition(0, 0);
        CharPosition p2 = new CharPosition(2, 2);
        Block block = new BlockImpl(scheme, p1, p2);
        assertEquals(scheme, block.getScheme());
        assertEquals(p1.getX(), block.getX1());
        assertEquals(p1.getY(), block.getY1());
        assertEquals(p2.getX(), block.getX2());
        assertEquals(p2.getY(), block.getY2());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor2WithInvalidArguments() {
        Scheme scheme = new Scheme();
        CharPosition p1 = new CharPosition(0, 0);
        CharPosition p2 = new CharPosition(2, 2);
        Block block = new BlockImpl(scheme, p2, p1);
    }

    @Test
    public void testConstructor3() {
        Scheme scheme = new Scheme();
        Block block = new BlockImpl(scheme);
        assertEquals(scheme, block.getScheme());
        assertEquals(0, block.getX1());
        assertEquals(0, block.getY1());
        assertEquals(scheme.getX2(), block.getX2());
        assertEquals(scheme.getY2(), block.getY2());
    }

    @Test
    public void testSubblock() {
        Scheme scheme = new Scheme();
        Block block = new BlockImpl(scheme);
        Block subblock = block.subblock(1, 1, 2, 2);
        assertEquals(1, subblock.getX1());
        assertEquals(1, subblock.getY1());
        assertEquals(2, subblock.getX2());
        assertEquals(2, subblock.getY2());
    }

    @Test
    public void testCharAt() {
        Scheme scheme = new Scheme();
        CharTable charTable = new CharTableImpl();
        scheme.setCharTable(charTable);
        Block block = new BlockImpl(scheme);
        charTable.setCharAt(1, 1, 'A');
        assertEquals('A', block.charAt(1, 1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCharAtWithInvalidPosition() {
        Scheme scheme = new Scheme();
        CharTable charTable = new CharTableImpl();
        scheme.setCharTable(charTable);
        Block block = new BlockImpl(scheme);
        block.charAt(10, 10);
    }

    @Test
    public void testTextAt() {
        Scheme scheme = new Scheme();
        CharTable charTable = new CharTableImpl();
        scheme.setCharTable(charTable);
        Block block = new BlockImpl(scheme);
        charTable.setTextAt(1, 3, 1, "ABC");
        assertEquals("BC", block.textAt(1, 3, 1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTextAtWithInvalidCoordinates() {
        Scheme scheme = new Scheme();
        CharTable charTable = new CharTableImpl();
        scheme.setCharTable(charTable);
        Block block = new BlockImpl(scheme);
        block.textAt(2, 1, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWordWithTrivialBlock() {
        Scheme scheme = new Scheme();
        Block block = new TrivialBlock(scheme, 0, 0);
        block.containsOnlyOneWord();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWordWithEmptyBlock() {
        Scheme scheme = new Scheme();
        Block block = new BlockImpl(scheme, 0, 0, 0, 0);
        block.containsOnlyOneWord();
    }
    
    @Test
    public void testContainsOnlyOneWordWithValidBlock() {
        Scheme scheme = new Scheme();
        Block block = new BlockImpl(scheme, 0, 0, 1, 1);
        HWordPosition firstWord = new HWordPosition(0, 0, 0);
        HWordPosition lastWord = new HWordPosition(0, 0, 1);
        block.setFirstWord_Horizontally(firstWord);
        block.setLastWord_Horizontally(lastWord);
        assertTrue(block.containsOnlyOneWord());
    }
    
    @Test
    public void testExtractSequentialSubblocks() {
        Scheme scheme = new Scheme();
        Block block = new BlockImpl(scheme);
        Block subblock = new BlockImpl(scheme, 1, 1, 2, 2);
        List<Block> expected = new ArrayList<>();
        expected.add(subblock);
        block.addSubblock(subblock);
        List<Block> actual = block.extractSequentialSubblocks();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testExtractParallelSubblocks() {
        Scheme scheme = new Scheme();
        Block block = new BlockImpl(scheme);
        Block subblock = new BlockImpl(scheme, 1, 1, 2, 2);
        List<Block> expected = new ArrayList<>();
        expected.add(subblock);
        block.addSubblock(subblock);
        List<Block> actual = block.extractParallelSubblocks();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testToString() {
        Scheme scheme = new Scheme();
        Block block = new BlockImpl(scheme, 0, 0, 2, 2);
        block.setScheme(scheme);
        assertEquals("Block(0,0, 2,2):\n ####\n #  #\n #  #\n ####", block.toString());
    }
    
    @Test
    public void testGetUpperLine() {
        Scheme scheme = new Scheme();
        Block block = new BlockImpl(scheme, 0, 0, 2, 2);
        CharTable charTable = new CharTableImpl();
        scheme.setCharTable(charTable);
        charTable.setTextAt(0, 2, 0, "ABC");
        assertEquals("ABC", block.getUpperLine());
    }
}

