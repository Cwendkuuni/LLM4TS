
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class BlockTest {
    private Block block;
    private Scheme scheme;

    @Before
    public void setUp() {
        scheme = new Scheme(10, 10);
        block = new BlockImpl(scheme, 1, 2, 3, 4);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidCoordinates() {
        new BlockImpl(scheme, -1, 2, 3, 4);
    }
    
    @Test
    public void testConstructorWithValidCoordinates() {
        assertEquals("Block constructor did not set scheme correctly", scheme, block.scheme);
        assertEquals("Block constructor did not set x1 correctly", 1, block.x1);
        assertEquals("Block constructor did not set y1 correctly", 2, block.y1);
        assertEquals("Block constructor did not set x2 correctly", 3, block.x2);
        assertEquals("Block constructor did not set y2 correctly", 4, block.y2);
    }
    
    @Test
    public void testConstructorWithCharPosition() {
        CharPosition p1 = new CharPosition(1, 2);
        CharPosition p2 = new CharPosition(3, 4);
        
        Block block = new BlockImpl(scheme, p1, p2);
        
        assertEquals("Block constructor with CharPosition did not set x1 correctly", p1.x, block.x1);
        assertEquals("Block constructor with CharPosition did not set y1 correctly", p1.y, block.y1);
        assertEquals("Block constructor with CharPosition did not set x2 correctly", p2.x, block.x2);
        assertEquals("Block constructor with CharPosition did not set y2 correctly", p2.y, block.y2);
    }
    
    @Test
    public void testSubblock() {
        Block subblock = block.subblock(2, 3, 4, 5);
        
        assertEquals("subblock method did not set scheme correctly", scheme, subblock.scheme);
        assertEquals("subblock method did not set x1 correctly", 2, subblock.x1);
        assertEquals("subblock method did not set y1 correctly", 3, subblock.y1);
        assertEquals("subblock method did not set x2 correctly", 4, subblock.x2);
        assertEquals("subblock method did not set y2 correctly", 5, subblock.y2);
    }
    
    @Test
    public void testCharAt() {
        char result = block.charAt(2, 3);
        
        assertEquals("charAt method did not return the correct char", '#', result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCharAtWithInvalidPosition() {
        block.charAt(10, 10);
    }
    
    @Test
    public void testTextAt() {
        String result = block.textAt(1, 3, 2);
        
        assertEquals("textAt method did not return the correct substring", "lo", result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testTextAtWithInvalidCoordinates() {
        block.textAt(1, 10, 2);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWordWithTrivialBlock() {
        block.containsOnlyOneWord();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWordWithEmptyBlock() {
        scheme.lines[4] = "";
        block.containsOnlyOneWord();
    }
    
    @Test
    public void testExtractSequentialSubblocks() {
        List<Block> result = block.extractSequentialSubblocks();
        
        assertNotNull("extractSequentialSubblocks method did not return a non-null list", result);
        assertEquals("extractSequentialSubblocks method did not return an empty list", 0, result.size());
    }
    
    @Test
    public void testExtractParallelSubblocks() {
        List<Block> result = block.extractParallelSubblocks();
        
        assertNotNull("extractParallelSubblocks method did not return a non-null list", result);
        assertEquals("extractParallelSubblocks method did not return an empty list", 0, result.size());
    }
    
    @Test
    public void testToString() {
        String expected = "Block(1,2, 3,4):\n" +
                          " ######\n" +
                          " #    #\n" +
                          " ##   #\n" +
                          " ######";
        
        String result = block.toString();
        
        assertEquals("toString method did not return the correct string representation", expected, result);
    }
    
    @Test
    public void testGetUpperLine() {
        String result = block.getUpperLine();
        
        assertEquals("getUpperLine method did not return the correct string", "lo", result);
    }
}
