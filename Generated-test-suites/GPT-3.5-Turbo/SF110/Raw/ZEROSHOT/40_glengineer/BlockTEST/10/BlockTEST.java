import static org.junit.Assert.*;
import org.junit.*;
import glengineer.blocks.*;
import glengineer.positions.*;
import java.util.*;

public class BlockTest {
    
    @Test
    public void testSubblock() {
        Scheme scheme = new Scheme();
        Block block = new Block(scheme, 0, 0, 4, 4);
        
        Block subblock = block.subblock(1, 1, 3, 3);
        
        assertEquals(1, subblock.x1);
        assertEquals(1, subblock.y1);
        assertEquals(3, subblock.x2);
        assertEquals(3, subblock.y2);
    }
    
    @Test
    public void testCharAt() {
        Scheme scheme = new Scheme();
        scheme.lines = new String[] { "abcd", "efgh", "ijkl" };
        Block block = new Block(scheme, 0, 0, 4, 3);
        
        char result = block.charAt(2, 1);
        
        assertEquals('h', result);
    }
    
    @Test
    public void testTextAt() {
        Scheme scheme = new Scheme();
        scheme.lines = new String[] { "abcdefghij", "klmnopqrst" };
        Block block = new Block(scheme, 2, 0, 8, 1);
        
        String result = block.textAt(3, 7, 1);
        
        assertEquals("mnopq", result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWord_trivialBlock() {
        Scheme scheme = new Scheme();
        Block block = new Block(scheme, 0, 0, 0, 0);
        
        block.containsOnlyOneWord();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWord_emptyBlock() {
        Scheme scheme = new Scheme();
        Block block = new Block(scheme, 0, 0, 2, 2);
        
        block.containsOnlyOneWord();
    }
    
    @Test
    public void testContainsOnlyOneWord_singleWord() {
        Scheme scheme = new Scheme();
        scheme.lines = new String[] { "abcdefghij" };
        Block block = new Block(scheme, 2, 0, 7, 0);
        
        boolean result = block.containsOnlyOneWord();
        
        assertTrue(result);
    }
    
    @Test
    public void testExtractSequentialSubblocks() {
        Scheme scheme = new Scheme();
        scheme.lines = new String[] { "abcdefghij", "klmnopqrst", "uvwxyzabcd" };
        Block block = new Block(scheme, 0, 0, 10, 2);
        
        List<Block> result = block.extractSequentialSubblocks();
        
        assertEquals(3, result.size());
        assertEquals(0, result.get(0).x1);
        assertEquals(0, result.get(0).y1);
        assertEquals(10, result.get(0).x2);
        assertEquals(0, result.get(0).y2);
        assertEquals(0, result.get(1).x1);
        assertEquals(1, result.get(1).y1);
        assertEquals(10, result.get(1).x2);
        assertEquals(1, result.get(1).y2);
        assertEquals(0, result.get(2).x1);
        assertEquals(2, result.get(2).y1);
        assertEquals(10, result.get(2).x2);
        assertEquals(2, result.get(2).y2);
    }
    
    @Test
    public void testExtractParallelSubblocks() {
        Scheme scheme = new Scheme();
        scheme.lines = new String[] { "abcdefghij", "klmnopqrst", "uvwxyzabcd" };
        Block block = new Block(scheme, 0, 0, 10, 2);
        
        List<Block> result = block.extractParallelSubblocks();
        
        assertEquals(11, result.size());
        assertEquals(0, result.get(0).x1);
        assertEquals(0, result.get(0).y1);
        assertEquals(0, result.get(0).x2);
        assertEquals(2, result.get(0).y2);
        assertEquals(1, result.get(1).x1);
        assertEquals(0, result.get(1).y1);
        assertEquals(1, result.get(1).x2);
        assertEquals(2, result.get(1).y2);
        assertEquals(2, result.get(2).x1);
        assertEquals(0, result.get(2).y1);
        assertEquals(2, result.get(2).x2);
        assertEquals(2, result.get(2).y2);
        // and so on...
    }
    
    @Test
    public void testToString() {
        Scheme scheme = new Scheme();
        scheme.lines = new String[] { "abcdefghij", "klmnopqrst", "uvwxyzabcd" };
        Block block = new Block(scheme, 2, 0, 7, 2);
        
        String result = block.toString();
        
        assertEquals("Block(2,0, 7,2):\n"
                     + " ######\n"
                     + " #cdefg#\n"
                     + " #klmno#\n"
                     + " #uvwxz#\n"
                     + " ######", result);
    }
    
    @Test
    public void testGetUpperLine() {
        Scheme scheme = new Scheme();
        scheme.lines = new String[] { "abcdefghij", "klmnopqrst", "uvwxyzabcd" };
        Block block = new Block(scheme, 2, 0, 7, 2);
        
        String result = block.getUpperLine();
        
        assertEquals("cdefg", result);
    }
}