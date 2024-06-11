import static org.junit.Assert.*;
import org.junit.Test;
import glengineer.blocks.*;
import glengineer.positions.*;

public class BlockTest {

    @Test
    public void testConstructorWithCoordinates() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new BlockDummy(scheme, 1, 1, 3, 3);
        
        assertEquals(1, block.x1);
        assertEquals(1, block.y1);
        assertEquals(3, block.x2);
        assertEquals(3, block.y2);
        assertEquals(scheme, block.scheme);
    }
    
    @Test
    public void testConstructorWithCharPositions() {
        Scheme scheme = new Scheme(5, 5);
        CharPosition p1 = new CharPosition(1, 1);
        CharPosition p2 = new CharPosition(3, 3);
        Block block = new BlockDummy(scheme, p1, p2);
        
        assertEquals(1, block.x1);
        assertEquals(1, block.y1);
        assertEquals(3, block.x2);
        assertEquals(3, block.y2);
        assertEquals(scheme, block.scheme);
    }
    
    @Test
    public void testDefaultConstructor() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new BlockDummy(scheme);
        
        assertEquals(0, block.x1);
        assertEquals(0, block.y1);
        assertEquals(scheme.x2, block.x2);
        assertEquals(scheme.y2, block.y2);
        assertEquals(scheme, block.scheme);
    }
    
    @Test
    public void testSubblock() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new BlockDummy(scheme, 1, 1, 3, 3);
        Block subblock = block.subblock(1, 1, 2, 2);
        
        assertEquals(1, subblock.x1);
        assertEquals(1, subblock.y1);
        assertEquals(2, subblock.x2);
        assertEquals(2, subblock.y2);
        assertEquals(scheme, subblock.scheme);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCharAt_OutOfBounds() {
        Scheme scheme = new Scheme(5, 5);
        scheme.lines = new String[]{"12345"};
        Block block = new BlockDummy(scheme, 0, 0, 5, 5);
        
        char c = block.charAt(10, 10);
    }
    
    @Test
    public void testCharAt_Valid() {
        Scheme scheme = new Scheme(5, 5);
        scheme.lines = new String[]{"12345"};
        Block block = new BlockDummy(scheme, 0, 0, 5, 5);
        
        char c = block.charAt(2, 0);
        
        assertEquals('3', c);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testTextAt_ImproperCoordinates() {
        Scheme scheme = new Scheme(5, 5);
        scheme.lines = new String[]{"12345"};
        Block block = new BlockDummy(scheme, 0, 0, 5, 5);
        
        String text = block.textAt(5, 10, 0);
    }
    
    @Test
    public void testTextAt_Valid() {
        Scheme scheme = new Scheme(5, 5);
        scheme.lines = new String[]{"12345"};
        Block block = new BlockDummy(scheme, 0, 0, 5, 5);
        
        String text = block.textAt(1, 4, 0);
        
        assertEquals("234", text);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWord_TrivialBlock() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new BlockDummy(scheme);
        
        block.containsOnlyOneWord();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContainsOnlyOneWord_EmptyBlock() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new BlockDummy(scheme, 0, 5, 0, 5);
        
        block.containsOnlyOneWord();
    }
    
    @Test
    public void testContainsOnlyOneWord_Valid() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new BlockDummy(scheme, 0, 0, 5, 5);
        
        boolean result = block.containsOnlyOneWord();
        
        assertTrue(result);
    }
    
    @Test
    public void testExtractSequentialSubblocks() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new BlockDummy(scheme);
        
        List<Block> subblocks = block.extractSequentialSubblocks();
        
        assertEquals(0, subblocks.size());
    }
    
    @Test
    public void testExtractParallelSubblocks() {
        Scheme scheme = new Scheme(5, 5);
        Block block = new BlockDummy(scheme);
        
        List<Block> subblocks = block.extractParallelSubblocks();
        
        assertEquals(0, subblocks.size());
    }
    
    @Test
    public void testToString() {
        Scheme scheme = new Scheme(5, 5);
        scheme.lines = new String[]{"12345"};
        Block block = new BlockDummy(scheme, 1, 1, 4, 3);
        
        String expected = "Block(1,1, 4,3):\n" +
                          " #####\n" +
                          " #345#\n" +
                          " #   #\n" +
                          " #   #\n" +
                          " #####";
                          
        assertEquals(expected, block.toString());
    }
    
    @Test
    public void testGetUpperLine() {
        Scheme scheme = new Scheme(5, 5);
        scheme.lines = new String[]{"12345"};
        Block block = new BlockDummy(scheme, 1, 1, 4, 3);
        
        String upperLine = block.getUpperLine();
        
        assertEquals("345", upperLine);
    }
}