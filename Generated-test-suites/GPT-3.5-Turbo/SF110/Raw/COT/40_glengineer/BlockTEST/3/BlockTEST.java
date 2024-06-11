
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.List;
import glengineer.positions.CharPosition;
import glengineer.positions.WordPosition;
import glengineer.positions.HWordPosition;

public class BlockTest {
    private Scheme scheme;
    private Block block;
    
    @Before
    public void setUp() {
        scheme = new Scheme();
        block = new BlockStub(scheme);
    }
    
    @Test
    public void testSubblock() {
        Block subblock = block.subblock(1, 1, 4, 4);
        assertNotNull(subblock);
        assertEquals(1, subblock.getX1());
        assertEquals(1, subblock.getY1());
        assertEquals(4, subblock.getX2());
        assertEquals(4, subblock.getY2());
    }
    
    @Test
    public void testCharAtInsideBlock() {
        char c = block.charAt(2, 2);
        assertEquals('A', c);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCharAtOutsideBlock() {
        block.charAt(10, 10);
    }
    
    @Test
    public void testTextAtInsideBlock() {
        String text = block.textAt(2, 4, 2);
        assertEquals("AB", text);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testTextAtOutsideBlock() {
        block.textAt(10, 20, 2);
    }
    
    @Test
    public void testContainsOnlyOneWord() {
        boolean result = block.containsOnlyOneWord();
        assertFalse(result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWordTrivialBlock() {
        BlockStub trivialBlock = new BlockStub(scheme);
        trivialBlock.setTrivial(true);
        trivialBlock.containsOnlyOneWord();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWordEmptyBlock() {
        BlockStub emptyBlock = new BlockStub(scheme);
        emptyBlock.setY2(0);
        emptyBlock.containsOnlyOneWord();
    }
    
    @Test
    public void testExtractSequentialSubblocks() {
        List<Block> subblocks = block.extractSequentialSubblocks();
        assertEquals(2, subblocks.size());
        assertEquals(1, subblocks.get(0).getX1());
        assertEquals(1, subblocks.get(0).getY1());
        assertEquals(4, subblocks.get(0).getX2());
        assertEquals(2, subblocks.get(0).getY2());
        assertEquals(1, subblocks.get(1).getX1());
        assertEquals(2, subblocks.get(1).getY1());
        assertEquals(4, subblocks.get(1).getX2());
        assertEquals(4, subblocks.get(1).getY2());
    }
    
    @Test
    public void testExtractParallelSubblocks() {
        List<Block> subblocks = block.extractParallelSubblocks();
        assertEquals(4, subblocks.size());
        assertEquals(1, subblocks.get(0).getX1());
        assertEquals(1, subblocks.get(0).getY1());
        assertEquals(2, subblocks.get(0).getX2());
        assertEquals(4, subblocks.get(0).getY2());
        assertEquals(2, subblocks.get(1).getX1());
        assertEquals(1, subblocks.get(1).getY1());
        assertEquals(3, subblocks.get(1).getX2());
        assertEquals(4, subblocks.get(1).getY2());
        assertEquals(3, subblocks.get(2).getX1());
        assertEquals(1, subblocks.get(2).getY1());
        assertEquals(4, subblocks.get(2).getX2());
        assertEquals(4, subblocks.get(2).getY2());
        assertEquals(1, subblocks.get(3).getX1());
        assertEquals(3, subblocks.get(3).getY1());
        assertEquals(4, subblocks.get(3).getX2());
        assertEquals(4, subblocks.get(3).getY2());
    }
    
    @Test
    public void testToString() {
        String blockString = block.toString();
        String expectedString = "Block(0,0, 3,3):\n" +
                                " ####\n" +
                                " #AB#\n" +
                                " #CD#\n" +
                                " ####";
        assertEquals(expectedString, blockString);
    }
    
    @Test
    public void testGetUpperLine() {
        String upperLine = block.getUpperLine();
        assertEquals("AB", upperLine);
    }
    
    // Additional test cases for edge cases and exceptions
    // ...
}
