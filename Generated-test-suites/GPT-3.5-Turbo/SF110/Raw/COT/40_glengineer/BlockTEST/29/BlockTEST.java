import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import glengineer.blocks.Block;
import glengineer.positions.Scheme;

public class BlockTest {
    private Scheme scheme;
    private Block block;
    
    @Before
    public void setUp() {
        scheme = new Scheme();
        block = new Block(scheme, 0, 0, 10, 10);
    }
    
    @Test
    public void testSubblock() {
        Block subblock = block.subblock(1, 1, 5, 5);
        assertEquals("Subblock x1 should be 1", 1, subblock.x1);
        assertEquals("Subblock y1 should be 1", 1, subblock.y1);
        assertEquals("Subblock x2 should be 5", 5, subblock.x2);
        assertEquals("Subblock y2 should be 5", 5, subblock.y2);
        assertEquals("Subblock scheme should be equal to Block scheme", block.scheme, subblock.scheme);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSubblockInvalidCoordinates() {
        Block subblock = block.subblock(5, 5, 1, 1);
    }
    
    @Test
    public void testCharAt() {
        char c = block.charAt(0, 0);
        assertEquals("Char at (0, 0) should be equal to the first character of the first line of the scheme", scheme.lines[0].charAt(0), c);
        
        c = block.charAt(5, 5);
        assertEquals("Char at (5, 5) should be equal to the sixth character of the sixth line of the scheme", scheme.lines[5].charAt(5), c);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCharAtOutOfBounds() {
        char c = block.charAt(15, 15);
    }
    
    @Test
    public void testTextAt() {
        String text = block.textAt(0, 5, 0);
        assertEquals("Text at (0, 5, 0) should be equal to the first five characters of the first line of the scheme", scheme.lines[0].substring(0, 5), text);
        
        text = block.textAt(5, 10, 5);
        assertEquals("Text at (5, 10, 5) should be equal to the characters from 5 to 10 of the sixth line of the scheme", scheme.lines[5].substring(5, 10), text);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testTextAtInvalidCoordinates() {
        String text = block.textAt(10, 5, 0);
    }
    
    @Test
    public void testContainsOnlyOneWord() {
        assertTrue("The block should contain only one word", block.containsOnlyOneWord());
        
        scheme.lines[2] = "Hello, World!";
        assertFalse("The block should not contain only one word", block.containsOnlyOneWord());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWordInTrivialBlock() {
        Block trivialBlock = new Block(scheme, 1, 1, 1, 1);
        trivialBlock.containsOnlyOneWord();
    }
    
    // Additional test methods for extractSequentialSubblocks() and extractParallelSubblocks()
    
    @Test
    public void testToString() {
        String expected = "Block(0,0, 10,10):\n" +
                          " ####################\n" +
                          " #                    #\n" +
                          " #                    #\n" +
                          " #                    #\n" +
                          " #                    #\n" +
                          " #                    #\n" +
                          " #                    #\n" +
                          " #                    #\n" +
                          " #                    #\n" +
                          " #                    #\n" +
                          " ####################";
        
        assertEquals("toString method should return the expected string representation of the block", expected, block.toString());
    }
    
    @Test
    public void testGetUpperLine() {
        String upperLine = block.getUpperLine();
        assertEquals("The upper line of the block should be equal to the first line of the scheme", scheme.lines[0], upperLine);
    }
}