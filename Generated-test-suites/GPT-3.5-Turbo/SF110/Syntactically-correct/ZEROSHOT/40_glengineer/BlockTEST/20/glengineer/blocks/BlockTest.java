import glengineer.blocks.*;
import glengineer.positions.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class BlockTest {
    
    // Test data
    private Scheme scheme;
    private Block block;
    
    @Before
    public void setUp() {
        scheme = new Scheme(10, 10);
        block = new Block(scheme, 0, 0, 5, 5);
    }
    
    @Test
    public void testConstructorWithCoordinates() {
        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(5, block.x2);
        assertEquals(5, block.y2);
        assertEquals(scheme, block.scheme);
    }
    
    @Test
    public void testConstructorWithPositions() {
        CharPosition p1 = new CharPosition(0, 0);
        CharPosition p2 = new CharPosition(5, 5);
        Block newBlock = new Block(scheme, p1, p2);
        
        assertEquals(0, newBlock.x1);
        assertEquals(0, newBlock.y1);
        assertEquals(5, newBlock.x2);
        assertEquals(5, newBlock.y2);
        assertEquals(scheme, newBlock.scheme);
    }
    
    @Test
    public void testDefaultConstructor() {
        Block defaultBlock = new Block(scheme);
        
        assertEquals(0, defaultBlock.x1);
        assertEquals(0, defaultBlock.y1);
        assertEquals(scheme.x2, defaultBlock.x2);
        assertEquals(scheme.y2, defaultBlock.y2);
        assertEquals(scheme, defaultBlock.scheme);
    }
    
    @Test
    public void testSubblock() {
        Block subblock = block.subblock(2, 2, 4, 4);
        
        assertEquals(2, subblock.x1);
        assertEquals(2, subblock.y1);
        assertEquals(4, subblock.x2);
        assertEquals(4, subblock.y2);
        assertEquals(scheme, subblock.scheme);
    }
    
    @Test
    public void testCharAt() {
        char expectedChar = scheme.lines[0].charAt(2);
        assertEquals(expectedChar, block.charAt(2, 0));
    }
    
    @Test
    public void testTextAt() {
        String expectedText = scheme.lines[0].substring(2, 5);
        assertEquals(expectedText, block.textAt(2, 5, 0));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWordWithTrivialBlock() {
        block.containsOnlyOneWord();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWordWithEmptyBlock() {
        Block emptyBlock = new Block(scheme, 0, 0, 0, 0);
        emptyBlock.containsOnlyOneWord();
    }
    
    @Test
    public void testContainsOnlyOneWord() {
        Block nonTrivialBlock = new Block(scheme, 0, 0, 1, 1);
        boolean result = nonTrivialBlock.containsOnlyOneWord();
        assertTrue(result);
    }
    
    @Test
    public void testExtractSequentialSubblocks() {
        List<Block> sequentialSubblocks = block.extractSequentialSubblocks();
        assertEquals(1, sequentialSubblocks.size());
        assertEquals(block, sequentialSubblocks.get(0));
    }
    
    @Test
    public void testExtractParallelSubblocks() {
        List<Block> parallelSubblocks = block.extractParallelSubblocks();
        assertEquals(1, parallelSubblocks.size());
        assertEquals(block, parallelSubblocks.get(0));
    }
    
    @Test
    public void testToString() {
        String expectedString = "Block(0,0, 5,5):\n" +
                " #### \n" +
                " #    \n" +
                " #    \n" +
                " #    \n" +
                " #    \n" +
                " #### ";
        assertEquals(expectedString, block.toString());
    }
    
    @Test
    public void testGetUpperLine() {
        String expectedUpperLine = scheme.lines[0].substring(0, 5);
        assertEquals(expectedUpperLine, block.getUpperLine());
    }
    
    // Other tests...
    
}

