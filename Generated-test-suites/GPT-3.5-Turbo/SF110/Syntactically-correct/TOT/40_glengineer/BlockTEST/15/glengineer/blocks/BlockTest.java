import org.junit.*;
import static org.junit.Assert.*;

public class BlockTest {

    private Scheme scheme;
    private Block block;
    
    @Before
    public void setUp() {
        scheme = new Scheme();
        block = new ConcreteBlock(scheme, 0, 0, 5, 5);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testConstructorInvalidArguments() {
        Block invalidBlock = new ConcreteBlock(scheme, -1, -1, 3, 3);
    }
    
    @Test
    public void testConstructorValidArguments() {
        assertNotNull(block);
    }
    
    @Test
    public void testGetX1() {
        assertEquals(0, block.getX1());
    }
    
    @Test
    public void testGetY1() {
        assertEquals(0, block.getY1());
    }
    
    @Test
    public void testGetX2() {
        assertEquals(5, block.getX2());
    }
    
    @Test
    public void testGetY2() {
        assertEquals(5, block.getY2());
    }
    
    @Test
    public void testCharAtValidPosition() {
        assertEquals('a', block.charAt(1, 1));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testCharAtInvalidPosition() {
        block.charAt(6, 6);
    }
    
    @Test
    public void testTextAtValidPosition() {
        assertEquals("bcdef", block.textAt(1, 6, 1));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testTextAtInvalidPosition() {
        block.textAt(6, 7, 1);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testContainsOnlyOneWordInTrivialBlock() {
        block.containsOnlyOneWord();
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testContainsOnlyOneWordInEmptyBlock() {
        scheme.setLineAtIndex("abcde", 0);
        scheme.setLineAtIndex("", 1);
        
        block.containsOnlyOneWord();
    }
    
    @Test
    public void testContainsOnlyOneWordTrue() {
        scheme.setLineAtIndex("abc", 0);
        scheme.setLineAtIndex("abc", 1);
        
        assertTrue(block.containsOnlyOneWord());
    }
    
    @Test
    public void testContainsOnlyOneWordFalse() {
        scheme.setLineAtIndex("abc", 0);
        scheme.setLineAtIndex("def", 1);
        
        assertFalse(block.containsOnlyOneWord());
    }
    
    @Test
    public void testExtractSequentialSubblocksEmpty() {
        assertTrue(block.extractSequentialSubblocks().isEmpty());
    }
    
    @Test
    public void testExtractSequentialSubblocks() {
        scheme.setLineAtIndex("abcde", 0);
        scheme.setLineAtIndex("fghij", 1);
        scheme.setLineAtIndex("klmno", 2);
        
        assertEquals(3, block.extractSequentialSubblocks().size());
    }
    
    @Test
    public void testExtractParallelSubblocksEmpty() {
        assertTrue(block.extractParallelSubblocks().isEmpty());
    }
    
    @Test
    public void testExtractParallelSubblocks() {
        scheme.setLineAtIndex("abcde", 0);
        scheme.setLineAtIndex("abcde", 1);
        scheme.setLineAtIndex("abcde", 2);
        
        assertEquals(5, block.extractParallelSubblocks().size());
    }
    
    @Test
    public void testToString() {
        String expected = "Block(0,0, 5,5):\n"
                        + " ######\n"
                        + " #     #\n"
                        + " #     #\n"
                        + " #     #\n"
                        + " #     #\n"
                        + " #     #\n"
                        + " ######";
                        
        assertEquals(expected, block.toString());
    }
    
    @Test
    public void testGetUpperLine() {
        scheme.setLineAtIndex("abc", 0);
        
        assertEquals("abc", block.getUpperLine());
    }
}